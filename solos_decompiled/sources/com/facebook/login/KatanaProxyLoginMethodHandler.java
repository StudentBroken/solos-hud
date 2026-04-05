package com.facebook.login;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.NativeProtocol;
import com.facebook.login.LoginClient;

/* JADX INFO: loaded from: classes41.dex */
class KatanaProxyLoginMethodHandler extends NativeAppLoginMethodHandler {
    public static final Parcelable.Creator<KatanaProxyLoginMethodHandler> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.KatanaProxyLoginMethodHandler.1
        @Override // android.os.Parcelable.Creator
        public KatanaProxyLoginMethodHandler createFromParcel(Parcel source) {
            return new KatanaProxyLoginMethodHandler(source);
        }

        @Override // android.os.Parcelable.Creator
        public KatanaProxyLoginMethodHandler[] newArray(int size) {
            return new KatanaProxyLoginMethodHandler[size];
        }
    };

    KatanaProxyLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    @Override // com.facebook.login.LoginMethodHandler
    String getNameForLogging() {
        return "katana_proxy_auth";
    }

    @Override // com.facebook.login.NativeAppLoginMethodHandler, com.facebook.login.LoginMethodHandler
    boolean tryAuthorize(LoginClient.Request request) {
        String e2e = LoginClient.getE2E();
        Intent intent = NativeProtocol.createProxyAuthIntent(this.loginClient.getActivity(), request.getApplicationId(), request.getPermissions(), e2e, request.isRerequest(), request.hasPublishPermission(), request.getDefaultAudience(), getClientState(request.getAuthId()));
        addLoggingExtra("e2e", e2e);
        return tryIntent(intent, LoginClient.getLoginRequestCode());
    }

    KatanaProxyLoginMethodHandler(Parcel source) {
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
