package com.facebook.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.internal.FacebookDialogFragment;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.WebDialog;
import com.facebook.login.LoginClient;

/* JADX INFO: loaded from: classes41.dex */
class WebViewLoginMethodHandler extends WebLoginMethodHandler {
    public static final Parcelable.Creator<WebViewLoginMethodHandler> CREATOR = new Parcelable.Creator() { // from class: com.facebook.login.WebViewLoginMethodHandler.2
        @Override // android.os.Parcelable.Creator
        public WebViewLoginMethodHandler createFromParcel(Parcel source) {
            return new WebViewLoginMethodHandler(source);
        }

        @Override // android.os.Parcelable.Creator
        public WebViewLoginMethodHandler[] newArray(int size) {
            return new WebViewLoginMethodHandler[size];
        }
    };
    private String e2e;
    private WebDialog loginDialog;

    WebViewLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    @Override // com.facebook.login.LoginMethodHandler
    String getNameForLogging() {
        return "web_view";
    }

    @Override // com.facebook.login.WebLoginMethodHandler
    AccessTokenSource getTokenSource() {
        return AccessTokenSource.WEB_VIEW;
    }

    @Override // com.facebook.login.LoginMethodHandler
    boolean needsInternetPermission() {
        return true;
    }

    @Override // com.facebook.login.LoginMethodHandler
    void cancel() {
        if (this.loginDialog != null) {
            this.loginDialog.cancel();
            this.loginDialog = null;
        }
    }

    @Override // com.facebook.login.LoginMethodHandler
    boolean tryAuthorize(final LoginClient.Request request) {
        Bundle parameters = getParameters(request);
        WebDialog.OnCompleteListener listener = new WebDialog.OnCompleteListener() { // from class: com.facebook.login.WebViewLoginMethodHandler.1
            @Override // com.facebook.internal.WebDialog.OnCompleteListener
            public void onComplete(Bundle values, FacebookException error) {
                WebViewLoginMethodHandler.this.onWebDialogComplete(request, values, error);
            }
        };
        this.e2e = LoginClient.getE2E();
        addLoggingExtra("e2e", this.e2e);
        FragmentActivity fragmentActivity = this.loginClient.getActivity();
        WebDialog.Builder builder = new AuthDialogBuilder(fragmentActivity, request.getApplicationId(), parameters).setE2E(this.e2e).setIsRerequest(request.isRerequest()).setOnCompleteListener(listener);
        this.loginDialog = builder.build();
        FacebookDialogFragment dialogFragment = new FacebookDialogFragment();
        dialogFragment.setRetainInstance(true);
        dialogFragment.setDialog(this.loginDialog);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), FacebookDialogFragment.TAG);
        return true;
    }

    void onWebDialogComplete(LoginClient.Request request, Bundle values, FacebookException error) {
        super.onComplete(request, values, error);
    }

    static class AuthDialogBuilder extends WebDialog.Builder {
        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";
        private String e2e;
        private boolean isRerequest;

        public AuthDialogBuilder(Context context, String applicationId, Bundle parameters) {
            super(context, applicationId, OAUTH_DIALOG, parameters);
        }

        public AuthDialogBuilder setE2E(String e2e) {
            this.e2e = e2e;
            return this;
        }

        public AuthDialogBuilder setIsRerequest(boolean isRerequest) {
            this.isRerequest = isRerequest;
            return this;
        }

        @Override // com.facebook.internal.WebDialog.Builder
        public WebDialog build() {
            Bundle parameters = getParameters();
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, "fbconnect://success");
            parameters.putString("client_id", getApplicationId());
            parameters.putString("e2e", this.e2e);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            parameters.putString(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE);
            return new WebDialog(getContext(), OAUTH_DIALOG, parameters, getTheme(), getListener());
        }
    }

    WebViewLoginMethodHandler(Parcel source) {
        super(source);
        this.e2e = source.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.facebook.login.LoginMethodHandler, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.e2e);
    }
}
