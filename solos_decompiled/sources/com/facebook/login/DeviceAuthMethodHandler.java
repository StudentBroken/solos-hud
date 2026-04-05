package com.facebook.login;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.login.LoginClient;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/* JADX INFO: loaded from: classes41.dex */
class DeviceAuthMethodHandler extends LoginMethodHandler {
    public static final Parcelable.Creator<DeviceAuthMethodHandler> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.DeviceAuthMethodHandler.1
        @Override // android.os.Parcelable.Creator
        public DeviceAuthMethodHandler createFromParcel(Parcel source) {
            return new DeviceAuthMethodHandler(source);
        }

        @Override // android.os.Parcelable.Creator
        public DeviceAuthMethodHandler[] newArray(int size) {
            return new DeviceAuthMethodHandler[size];
        }
    };
    private static ScheduledThreadPoolExecutor backgroundExecutor;

    DeviceAuthMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    @Override // com.facebook.login.LoginMethodHandler
    boolean tryAuthorize(LoginClient.Request request) {
        showDialog(request);
        return true;
    }

    private void showDialog(LoginClient.Request request) {
        DeviceAuthDialog dialog = new DeviceAuthDialog();
        dialog.show(this.loginClient.getActivity().getSupportFragmentManager(), "login_with_facebook");
        dialog.startLogin(request);
    }

    public void onCancel() {
        LoginClient.Result outcome = LoginClient.Result.createCancelResult(this.loginClient.getPendingRequest(), "User canceled log in.");
        this.loginClient.completeAndValidate(outcome);
    }

    public void onError(Exception ex) {
        LoginClient.Result outcome = LoginClient.Result.createErrorResult(this.loginClient.getPendingRequest(), null, ex.getMessage());
        this.loginClient.completeAndValidate(outcome);
    }

    public void onSuccess(String accessToken, String applicationId, String userId, Collection<String> permissions, Collection<String> declinedPermissions, AccessTokenSource accessTokenSource, Date expirationTime, Date lastRefreshTime) {
        AccessToken token = new AccessToken(accessToken, applicationId, userId, permissions, declinedPermissions, accessTokenSource, expirationTime, lastRefreshTime);
        LoginClient.Result outcome = LoginClient.Result.createTokenResult(this.loginClient.getPendingRequest(), token);
        this.loginClient.completeAndValidate(outcome);
    }

    public static synchronized ScheduledThreadPoolExecutor getBackgroundExecutor() {
        if (backgroundExecutor == null) {
            backgroundExecutor = new ScheduledThreadPoolExecutor(1);
        }
        return backgroundExecutor;
    }

    protected DeviceAuthMethodHandler(Parcel parcel) {
        super(parcel);
    }

    @Override // com.facebook.login.LoginMethodHandler
    String getNameForLogging() {
        return "device_auth";
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
