package com.facebook.login;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.internal.Utility;
import com.facebook.login.LoginClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
class GetTokenLoginMethodHandler extends LoginMethodHandler {
    public static final Parcelable.Creator<GetTokenLoginMethodHandler> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.GetTokenLoginMethodHandler.3
        @Override // android.os.Parcelable.Creator
        public GetTokenLoginMethodHandler createFromParcel(Parcel source) {
            return new GetTokenLoginMethodHandler(source);
        }

        @Override // android.os.Parcelable.Creator
        public GetTokenLoginMethodHandler[] newArray(int size) {
            return new GetTokenLoginMethodHandler[size];
        }
    };
    private GetTokenClient getTokenClient;

    GetTokenLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    @Override // com.facebook.login.LoginMethodHandler
    String getNameForLogging() {
        return "get_token";
    }

    @Override // com.facebook.login.LoginMethodHandler
    void cancel() {
        if (this.getTokenClient != null) {
            this.getTokenClient.cancel();
            this.getTokenClient.setCompletedListener(null);
            this.getTokenClient = null;
        }
    }

    @Override // com.facebook.login.LoginMethodHandler
    boolean tryAuthorize(final LoginClient.Request request) {
        this.getTokenClient = new GetTokenClient(this.loginClient.getActivity(), request.getApplicationId());
        if (!this.getTokenClient.start()) {
            return false;
        }
        this.loginClient.notifyBackgroundProcessingStart();
        PlatformServiceClient.CompletedListener callback = new PlatformServiceClient.CompletedListener() { // from class: com.facebook.login.GetTokenLoginMethodHandler.1
            @Override // com.facebook.internal.PlatformServiceClient.CompletedListener
            public void completed(Bundle result) {
                GetTokenLoginMethodHandler.this.getTokenCompleted(request, result);
            }
        };
        this.getTokenClient.setCompletedListener(callback);
        return true;
    }

    void getTokenCompleted(LoginClient.Request request, Bundle result) {
        if (this.getTokenClient != null) {
            this.getTokenClient.setCompletedListener(null);
        }
        this.getTokenClient = null;
        this.loginClient.notifyBackgroundProcessingStop();
        if (result != null) {
            ArrayList<String> currentPermissions = result.getStringArrayList(NativeProtocol.EXTRA_PERMISSIONS);
            Set<String> permissions = request.getPermissions();
            if (currentPermissions != null && (permissions == null || currentPermissions.containsAll(permissions))) {
                complete(request, result);
                return;
            }
            Set<String> newPermissions = new HashSet<>();
            for (String permission : permissions) {
                if (!currentPermissions.contains(permission)) {
                    newPermissions.add(permission);
                }
            }
            if (!newPermissions.isEmpty()) {
                addLoggingExtra("new_permissions", TextUtils.join(",", newPermissions));
            }
            request.setPermissions(newPermissions);
        }
        this.loginClient.tryNextHandler();
    }

    void onComplete(LoginClient.Request request, Bundle result) {
        AccessToken token = createAccessTokenFromNativeLogin(result, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE, request.getApplicationId());
        LoginClient.Result outcome = LoginClient.Result.createTokenResult(this.loginClient.getPendingRequest(), token);
        this.loginClient.completeAndValidate(outcome);
    }

    void complete(final LoginClient.Request request, final Bundle result) {
        String userId = result.getString(NativeProtocol.EXTRA_USER_ID);
        if (userId == null || userId.isEmpty()) {
            this.loginClient.notifyBackgroundProcessingStart();
            String accessToken = result.getString(NativeProtocol.EXTRA_ACCESS_TOKEN);
            Utility.getGraphMeRequestWithCacheAsync(accessToken, new Utility.GraphMeRequestWithCacheCallback() { // from class: com.facebook.login.GetTokenLoginMethodHandler.2
                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onSuccess(JSONObject userInfo) {
                    try {
                        String userId2 = userInfo.getString("id");
                        result.putString(NativeProtocol.EXTRA_USER_ID, userId2);
                        GetTokenLoginMethodHandler.this.onComplete(request, result);
                    } catch (JSONException ex) {
                        GetTokenLoginMethodHandler.this.loginClient.complete(LoginClient.Result.createErrorResult(GetTokenLoginMethodHandler.this.loginClient.getPendingRequest(), "Caught exception", ex.getMessage()));
                    }
                }

                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onFailure(FacebookException error) {
                    GetTokenLoginMethodHandler.this.loginClient.complete(LoginClient.Result.createErrorResult(GetTokenLoginMethodHandler.this.loginClient.getPendingRequest(), "Caught exception", error.getMessage()));
                }
            });
            return;
        }
        onComplete(request, result);
    }

    GetTokenLoginMethodHandler(Parcel source) {
        super(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.login.LoginMethodHandler, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
