package com.facebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public final class AccessToken implements Parcelable {
    public static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String APPLICATION_ID_KEY = "application_id";
    private static final int CURRENT_JSON_FORMAT = 1;
    private static final String DECLINED_PERMISSIONS_KEY = "declined_permissions";
    private static final String EXPIRES_AT_KEY = "expires_at";
    public static final String EXPIRES_IN_KEY = "expires_in";
    private static final String LAST_REFRESH_KEY = "last_refresh";
    private static final String PERMISSIONS_KEY = "permissions";
    private static final String SOURCE_KEY = "source";
    private static final String TOKEN_KEY = "token";
    public static final String USER_ID_KEY = "user_id";
    private static final String VERSION_KEY = "version";
    private final String applicationId;
    private final Set<String> declinedPermissions;
    private final Date expires;
    private final Date lastRefresh;
    private final Set<String> permissions;
    private final AccessTokenSource source;
    private final String token;
    private final String userId;
    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
    private static final Date DEFAULT_LAST_REFRESH_TIME = new Date();
    private static final AccessTokenSource DEFAULT_ACCESS_TOKEN_SOURCE = AccessTokenSource.FACEBOOK_APPLICATION_WEB;
    public static final Parcelable.Creator<AccessToken> CREATOR = new Parcelable.Creator() { // from class: com.facebook.AccessToken.2
        @Override // android.os.Parcelable.Creator
        public AccessToken createFromParcel(Parcel source) {
            return new AccessToken(source);
        }

        @Override // android.os.Parcelable.Creator
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

    public interface AccessTokenCreationCallback {
        void onError(FacebookException facebookException);

        void onSuccess(AccessToken accessToken);
    }

    public interface AccessTokenRefreshCallback {
        void OnTokenRefreshFailed(FacebookException facebookException);

        void OnTokenRefreshed(AccessToken accessToken);
    }

    public AccessToken(String accessToken, String applicationId, String userId, @Nullable Collection<String> permissions, @Nullable Collection<String> declinedPermissions, @Nullable AccessTokenSource accessTokenSource, @Nullable Date expirationTime, @Nullable Date lastRefreshTime) {
        Validate.notNullOrEmpty(accessToken, "accessToken");
        Validate.notNullOrEmpty(applicationId, "applicationId");
        Validate.notNullOrEmpty(userId, "userId");
        this.expires = expirationTime == null ? DEFAULT_EXPIRATION_TIME : expirationTime;
        this.permissions = Collections.unmodifiableSet(permissions != null ? new HashSet(permissions) : new HashSet());
        this.declinedPermissions = Collections.unmodifiableSet(declinedPermissions != null ? new HashSet(declinedPermissions) : new HashSet());
        this.token = accessToken;
        this.source = accessTokenSource == null ? DEFAULT_ACCESS_TOKEN_SOURCE : accessTokenSource;
        this.lastRefresh = lastRefreshTime == null ? DEFAULT_LAST_REFRESH_TIME : lastRefreshTime;
        this.applicationId = applicationId;
        this.userId = userId;
    }

    public static AccessToken getCurrentAccessToken() {
        return AccessTokenManager.getInstance().getCurrentAccessToken();
    }

    public static void setCurrentAccessToken(AccessToken accessToken) {
        AccessTokenManager.getInstance().setCurrentAccessToken(accessToken);
    }

    public static void refreshCurrentAccessTokenAsync() {
        AccessTokenManager.getInstance().refreshCurrentAccessToken(null);
    }

    public static void refreshCurrentAccessTokenAsync(AccessTokenRefreshCallback callback) {
        AccessTokenManager.getInstance().refreshCurrentAccessToken(callback);
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpires() {
        return this.expires;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

    public Set<String> getDeclinedPermissions() {
        return this.declinedPermissions;
    }

    public AccessTokenSource getSource() {
        return this.source;
    }

    public Date getLastRefresh() {
        return this.lastRefresh;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getUserId() {
        return this.userId;
    }

    public static void createFromNativeLinkingIntent(Intent intent, final String applicationId, final AccessTokenCreationCallback accessTokenCallback) {
        Validate.notNull(intent, "intent");
        if (intent.getExtras() == null) {
            accessTokenCallback.onError(new FacebookException("No extras found on intent"));
            return;
        }
        final Bundle extras = new Bundle(intent.getExtras());
        String accessToken = extras.getString("access_token");
        if (accessToken == null || accessToken.isEmpty()) {
            accessTokenCallback.onError(new FacebookException("No access token found on intent"));
            return;
        }
        String userId = extras.getString("user_id");
        if (userId == null || userId.isEmpty()) {
            Utility.getGraphMeRequestWithCacheAsync(accessToken, new Utility.GraphMeRequestWithCacheCallback() { // from class: com.facebook.AccessToken.1
                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onSuccess(JSONObject userInfo) {
                    try {
                        String userId2 = userInfo.getString("id");
                        extras.putString("user_id", userId2);
                        accessTokenCallback.onSuccess(AccessToken.createFromBundle(null, extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB, new Date(), applicationId));
                    } catch (JSONException e) {
                        accessTokenCallback.onError(new FacebookException("Unable to generate access token due to missing user id"));
                    }
                }

                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onFailure(FacebookException error) {
                    accessTokenCallback.onError(error);
                }
            });
        } else {
            accessTokenCallback.onSuccess(createFromBundle(null, extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB, new Date(), applicationId));
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{AccessToken");
        builder.append(" token:").append(tokenToString());
        appendPermissions(builder);
        builder.append("}");
        return builder.toString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AccessToken)) {
            return false;
        }
        AccessToken o = (AccessToken) other;
        return this.expires.equals(o.expires) && this.permissions.equals(o.permissions) && this.declinedPermissions.equals(o.declinedPermissions) && this.token.equals(o.token) && this.source == o.source && this.lastRefresh.equals(o.lastRefresh) && (this.applicationId != null ? this.applicationId.equals(o.applicationId) : o.applicationId == null) && this.userId.equals(o.userId);
    }

    public int hashCode() {
        int result = this.expires.hashCode() + 527;
        return (((((((((((((result * 31) + this.permissions.hashCode()) * 31) + this.declinedPermissions.hashCode()) * 31) + this.token.hashCode()) * 31) + this.source.hashCode()) * 31) + this.lastRefresh.hashCode()) * 31) + (this.applicationId == null ? 0 : this.applicationId.hashCode())) * 31) + this.userId.hashCode();
    }

    @SuppressLint({"FieldGetter"})
    static AccessToken createFromRefresh(AccessToken current, Bundle bundle) {
        if (current.source != AccessTokenSource.FACEBOOK_APPLICATION_WEB && current.source != AccessTokenSource.FACEBOOK_APPLICATION_NATIVE && current.source != AccessTokenSource.FACEBOOK_APPLICATION_SERVICE) {
            throw new FacebookException("Invalid token source: " + current.source);
        }
        Date expires = Utility.getBundleLongAsDate(bundle, EXPIRES_IN_KEY, new Date(0L));
        String token = bundle.getString("access_token");
        if (Utility.isNullOrEmpty(token)) {
            return null;
        }
        return new AccessToken(token, current.applicationId, current.getUserId(), current.getPermissions(), current.getDeclinedPermissions(), current.source, expires, new Date());
    }

    static AccessToken createFromLegacyCache(Bundle bundle) {
        List<String> permissions = getPermissionsFromBundle(bundle, LegacyTokenHelper.PERMISSIONS_KEY);
        List<String> declinedPermissions = getPermissionsFromBundle(bundle, LegacyTokenHelper.DECLINED_PERMISSIONS_KEY);
        String applicationId = LegacyTokenHelper.getApplicationId(bundle);
        if (Utility.isNullOrEmpty(applicationId)) {
            applicationId = FacebookSdk.getApplicationId();
        }
        String tokenString = LegacyTokenHelper.getToken(bundle);
        JSONObject userInfo = Utility.awaitGetGraphMeRequestWithCache(tokenString);
        try {
            String userId = userInfo.getString("id");
            return new AccessToken(tokenString, applicationId, userId, permissions, declinedPermissions, LegacyTokenHelper.getSource(bundle), LegacyTokenHelper.getDate(bundle, LegacyTokenHelper.EXPIRATION_DATE_KEY), LegacyTokenHelper.getDate(bundle, LegacyTokenHelper.LAST_REFRESH_DATE_KEY));
        } catch (JSONException e) {
            return null;
        }
    }

    static List<String> getPermissionsFromBundle(Bundle bundle, String key) {
        List<String> originalPermissions = bundle.getStringArrayList(key);
        if (originalPermissions == null) {
            List<String> permissions = Collections.emptyList();
            return permissions;
        }
        List<String> permissions2 = Collections.unmodifiableList(new ArrayList(originalPermissions));
        return permissions2;
    }

    public boolean isExpired() {
        return new Date().after(this.expires);
    }

    JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("version", 1);
        jsonObject.put(TOKEN_KEY, this.token);
        jsonObject.put("expires_at", this.expires.getTime());
        JSONArray permissionsArray = new JSONArray((Collection) this.permissions);
        jsonObject.put("permissions", permissionsArray);
        JSONArray declinedPermissionsArray = new JSONArray((Collection) this.declinedPermissions);
        jsonObject.put(DECLINED_PERMISSIONS_KEY, declinedPermissionsArray);
        jsonObject.put(LAST_REFRESH_KEY, this.lastRefresh.getTime());
        jsonObject.put("source", this.source.name());
        jsonObject.put(APPLICATION_ID_KEY, this.applicationId);
        jsonObject.put("user_id", this.userId);
        return jsonObject;
    }

    static AccessToken createFromJSONObject(JSONObject jsonObject) throws JSONException {
        int version = jsonObject.getInt("version");
        if (version > 1) {
            throw new FacebookException("Unknown AccessToken serialization format.");
        }
        String token = jsonObject.getString(TOKEN_KEY);
        Date expiresAt = new Date(jsonObject.getLong("expires_at"));
        JSONArray permissionsArray = jsonObject.getJSONArray("permissions");
        JSONArray declinedPermissionsArray = jsonObject.getJSONArray(DECLINED_PERMISSIONS_KEY);
        Date lastRefresh = new Date(jsonObject.getLong(LAST_REFRESH_KEY));
        AccessTokenSource source = AccessTokenSource.valueOf(jsonObject.getString("source"));
        String applicationId = jsonObject.getString(APPLICATION_ID_KEY);
        String userId = jsonObject.getString("user_id");
        return new AccessToken(token, applicationId, userId, Utility.jsonArrayToStringList(permissionsArray), Utility.jsonArrayToStringList(declinedPermissionsArray), source, expiresAt, lastRefresh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AccessToken createFromBundle(List<String> requestedPermissions, Bundle bundle, AccessTokenSource source, Date expirationBase, String applicationId) {
        String token = bundle.getString("access_token");
        Date expires = Utility.getBundleLongAsDate(bundle, EXPIRES_IN_KEY, expirationBase);
        String userId = bundle.getString("user_id");
        if (Utility.isNullOrEmpty(token) || expires == null) {
            return null;
        }
        return new AccessToken(token, applicationId, userId, requestedPermissions, null, source, expires, new Date());
    }

    private String tokenToString() {
        if (this.token == null) {
            return "null";
        }
        if (FacebookSdk.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS)) {
            return this.token;
        }
        return "ACCESS_TOKEN_REMOVED";
    }

    private void appendPermissions(StringBuilder builder) {
        builder.append(" permissions:");
        if (this.permissions == null) {
            builder.append("null");
            return;
        }
        builder.append("[");
        builder.append(TextUtils.join(", ", this.permissions));
        builder.append("]");
    }

    AccessToken(Parcel parcel) {
        this.expires = new Date(parcel.readLong());
        ArrayList<String> permissionsList = new ArrayList<>();
        parcel.readStringList(permissionsList);
        this.permissions = Collections.unmodifiableSet(new HashSet(permissionsList));
        permissionsList.clear();
        parcel.readStringList(permissionsList);
        this.declinedPermissions = Collections.unmodifiableSet(new HashSet(permissionsList));
        this.token = parcel.readString();
        this.source = AccessTokenSource.valueOf(parcel.readString());
        this.lastRefresh = new Date(parcel.readLong());
        this.applicationId = parcel.readString();
        this.userId = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.expires.getTime());
        dest.writeStringList(new ArrayList(this.permissions));
        dest.writeStringList(new ArrayList(this.declinedPermissions));
        dest.writeString(this.token);
        dest.writeString(this.source.name());
        dest.writeLong(this.lastRefresh.getTime());
        dest.writeString(this.applicationId);
        dest.writeString(this.userId);
    }
}
