package com.facebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.share.internal.ShareConstants;
import com.twitter.sdk.android.core.internal.oauth.OAuthConstants;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
final class AccessTokenManager {
    static final String ACTION_CURRENT_ACCESS_TOKEN_CHANGED = "com.facebook.sdk.ACTION_CURRENT_ACCESS_TOKEN_CHANGED";
    static final String EXTRA_NEW_ACCESS_TOKEN = "com.facebook.sdk.EXTRA_NEW_ACCESS_TOKEN";
    static final String EXTRA_OLD_ACCESS_TOKEN = "com.facebook.sdk.EXTRA_OLD_ACCESS_TOKEN";
    private static final String ME_PERMISSIONS_GRAPH_PATH = "me/permissions";
    static final String SHARED_PREFERENCES_NAME = "com.facebook.AccessTokenManager.SharedPreferences";
    static final String TAG = "AccessTokenManager";
    private static final String TOKEN_EXTEND_GRAPH_PATH = "oauth/access_token";
    private static final int TOKEN_EXTEND_RETRY_SECONDS = 3600;
    private static final int TOKEN_EXTEND_THRESHOLD_SECONDS = 86400;
    private static volatile AccessTokenManager instance;
    private final AccessTokenCache accessTokenCache;
    private AccessToken currentAccessToken;
    private final LocalBroadcastManager localBroadcastManager;
    private AtomicBoolean tokenRefreshInProgress = new AtomicBoolean(false);
    private Date lastAttemptedTokenExtendDate = new Date(0);

    AccessTokenManager(LocalBroadcastManager localBroadcastManager, AccessTokenCache accessTokenCache) {
        Validate.notNull(localBroadcastManager, "localBroadcastManager");
        Validate.notNull(accessTokenCache, "accessTokenCache");
        this.localBroadcastManager = localBroadcastManager;
        this.accessTokenCache = accessTokenCache;
    }

    static AccessTokenManager getInstance() {
        if (instance == null) {
            synchronized (AccessTokenManager.class) {
                if (instance == null) {
                    Context applicationContext = FacebookSdk.getApplicationContext();
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext);
                    AccessTokenCache accessTokenCache = new AccessTokenCache();
                    instance = new AccessTokenManager(localBroadcastManager, accessTokenCache);
                }
            }
        }
        return instance;
    }

    AccessToken getCurrentAccessToken() {
        return this.currentAccessToken;
    }

    boolean loadCurrentAccessToken() {
        AccessToken accessToken = this.accessTokenCache.load();
        if (accessToken == null) {
            return false;
        }
        setCurrentAccessToken(accessToken, false);
        return true;
    }

    void setCurrentAccessToken(AccessToken currentAccessToken) {
        setCurrentAccessToken(currentAccessToken, true);
    }

    private void setCurrentAccessToken(AccessToken currentAccessToken, boolean saveToCache) {
        AccessToken oldAccessToken = this.currentAccessToken;
        this.currentAccessToken = currentAccessToken;
        this.tokenRefreshInProgress.set(false);
        this.lastAttemptedTokenExtendDate = new Date(0L);
        if (saveToCache) {
            if (currentAccessToken != null) {
                this.accessTokenCache.save(currentAccessToken);
            } else {
                this.accessTokenCache.clear();
                Utility.clearFacebookCookies(FacebookSdk.getApplicationContext());
            }
        }
        if (!Utility.areObjectsEqual(oldAccessToken, currentAccessToken)) {
            sendCurrentAccessTokenChangedBroadcast(oldAccessToken, currentAccessToken);
        }
    }

    private void sendCurrentAccessTokenChangedBroadcast(AccessToken oldAccessToken, AccessToken currentAccessToken) {
        Intent intent = new Intent(ACTION_CURRENT_ACCESS_TOKEN_CHANGED);
        intent.putExtra(EXTRA_OLD_ACCESS_TOKEN, oldAccessToken);
        intent.putExtra(EXTRA_NEW_ACCESS_TOKEN, currentAccessToken);
        this.localBroadcastManager.sendBroadcast(intent);
    }

    void extendAccessTokenIfNeeded() {
        if (shouldExtendAccessToken()) {
            refreshCurrentAccessToken(null);
        }
    }

    private boolean shouldExtendAccessToken() {
        if (this.currentAccessToken == null) {
            return false;
        }
        Long now = Long.valueOf(new Date().getTime());
        return this.currentAccessToken.getSource().canExtendToken() && now.longValue() - this.lastAttemptedTokenExtendDate.getTime() > 3600000 && now.longValue() - this.currentAccessToken.getLastRefresh().getTime() > 86400000;
    }

    private static GraphRequest createGrantedPermissionsRequest(AccessToken accessToken, GraphRequest.Callback callback) {
        Bundle parameters = new Bundle();
        return new GraphRequest(accessToken, ME_PERMISSIONS_GRAPH_PATH, parameters, HttpMethod.GET, callback);
    }

    private static GraphRequest createExtendAccessTokenRequest(AccessToken accessToken, GraphRequest.Callback callback) {
        Bundle parameters = new Bundle();
        parameters.putString(OAuthConstants.PARAM_GRANT_TYPE, "fb_extend_sso_token");
        return new GraphRequest(accessToken, TOKEN_EXTEND_GRAPH_PATH, parameters, HttpMethod.GET, callback);
    }

    private static class RefreshResult {
        public String accessToken;
        public int expiresAt;

        private RefreshResult() {
        }
    }

    void refreshCurrentAccessToken(final AccessToken.AccessTokenRefreshCallback callback) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            refreshCurrentAccessTokenImpl(callback);
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() { // from class: com.facebook.AccessTokenManager.1
                @Override // java.lang.Runnable
                public void run() {
                    AccessTokenManager.this.refreshCurrentAccessTokenImpl(callback);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshCurrentAccessTokenImpl(final AccessToken.AccessTokenRefreshCallback callback) {
        final AccessToken accessToken = this.currentAccessToken;
        if (accessToken == null) {
            if (callback != null) {
                callback.OnTokenRefreshFailed(new FacebookException("No current access token to refresh"));
            }
        } else {
            if (!this.tokenRefreshInProgress.compareAndSet(false, true)) {
                if (callback != null) {
                    callback.OnTokenRefreshFailed(new FacebookException("Refresh already in progress"));
                    return;
                }
                return;
            }
            this.lastAttemptedTokenExtendDate = new Date();
            final Set<String> permissions = new HashSet<>();
            final Set<String> declinedPermissions = new HashSet<>();
            final AtomicBoolean permissionsCallSucceeded = new AtomicBoolean(false);
            final RefreshResult refreshResult = new RefreshResult();
            GraphRequestBatch batch = new GraphRequestBatch(createGrantedPermissionsRequest(accessToken, new GraphRequest.Callback() { // from class: com.facebook.AccessTokenManager.2
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    JSONArray permissionsArray;
                    JSONObject result = response.getJSONObject();
                    if (result != null && (permissionsArray = result.optJSONArray(ShareConstants.WEB_DIALOG_PARAM_DATA)) != null) {
                        permissionsCallSucceeded.set(true);
                        for (int i = 0; i < permissionsArray.length(); i++) {
                            JSONObject permissionEntry = permissionsArray.optJSONObject(i);
                            if (permissionEntry != null) {
                                String permission = permissionEntry.optString("permission");
                                String status = permissionEntry.optString("status");
                                if (!Utility.isNullOrEmpty(permission) && !Utility.isNullOrEmpty(status)) {
                                    String status2 = status.toLowerCase(Locale.US);
                                    if (status2.equals("granted")) {
                                        permissions.add(permission);
                                    } else if (status2.equals("declined")) {
                                        declinedPermissions.add(permission);
                                    } else {
                                        Log.w(AccessTokenManager.TAG, "Unexpected status: " + status2);
                                    }
                                }
                            }
                        }
                    }
                }
            }), createExtendAccessTokenRequest(accessToken, new GraphRequest.Callback() { // from class: com.facebook.AccessTokenManager.3
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    JSONObject data = response.getJSONObject();
                    if (data != null) {
                        refreshResult.accessToken = data.optString("access_token");
                        refreshResult.expiresAt = data.optInt(SettingsJsonConstants.EXPIRES_AT_KEY);
                    }
                }
            }));
            batch.addCallback(new GraphRequestBatch.Callback() { // from class: com.facebook.AccessTokenManager.4
                @Override // com.facebook.GraphRequestBatch.Callback
                public void onBatchCompleted(GraphRequestBatch batch2) throws Throwable {
                    AccessToken newAccessToken;
                    try {
                        if (AccessTokenManager.getInstance().getCurrentAccessToken() == null || AccessTokenManager.getInstance().getCurrentAccessToken().getUserId() != accessToken.getUserId()) {
                            if (callback != null) {
                                callback.OnTokenRefreshFailed(new FacebookException("No current access token to refresh"));
                            }
                            AccessTokenManager.this.tokenRefreshInProgress.set(false);
                            if (callback != null && 0 != 0) {
                                callback.OnTokenRefreshed(null);
                            }
                            return;
                        }
                        if (!permissionsCallSucceeded.get() && refreshResult.accessToken == null && refreshResult.expiresAt == 0) {
                            if (callback != null) {
                                callback.OnTokenRefreshFailed(new FacebookException("Failed to refresh access token"));
                            }
                            AccessTokenManager.this.tokenRefreshInProgress.set(false);
                            if (callback != null && 0 != 0) {
                                callback.OnTokenRefreshed(null);
                            }
                            return;
                        }
                        newAccessToken = new AccessToken(refreshResult.accessToken != null ? refreshResult.accessToken : accessToken.getToken(), accessToken.getApplicationId(), accessToken.getUserId(), permissionsCallSucceeded.get() ? permissions : accessToken.getPermissions(), permissionsCallSucceeded.get() ? declinedPermissions : accessToken.getDeclinedPermissions(), accessToken.getSource(), refreshResult.expiresAt != 0 ? new Date(((long) refreshResult.expiresAt) * 1000) : accessToken.getExpires(), new Date());
                        try {
                            AccessTokenManager.getInstance().setCurrentAccessToken(newAccessToken);
                            AccessTokenManager.this.tokenRefreshInProgress.set(false);
                            if (callback == null || newAccessToken == null) {
                                return;
                            }
                            callback.OnTokenRefreshed(newAccessToken);
                        } catch (Throwable th) {
                            th = th;
                            AccessTokenManager.this.tokenRefreshInProgress.set(false);
                            if (callback != null && newAccessToken != null) {
                                callback.OnTokenRefreshed(newAccessToken);
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        newAccessToken = null;
                    }
                }
            });
            batch.executeAsync();
        }
    }
}
