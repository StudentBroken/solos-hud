package com.facebook.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.FacebookServiceException;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.login.LoginClient;
import java.util.Locale;

/* JADX INFO: loaded from: classes41.dex */
abstract class WebLoginMethodHandler extends LoginMethodHandler {
    private static final String WEB_VIEW_AUTH_HANDLER_STORE = "com.facebook.login.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY";
    private static final String WEB_VIEW_AUTH_HANDLER_TOKEN_KEY = "TOKEN";
    private String e2e;

    abstract AccessTokenSource getTokenSource();

    private static final String getRedirectUri() {
        return "fb" + FacebookSdk.getApplicationId() + "://authorize";
    }

    WebLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    WebLoginMethodHandler(Parcel source) {
        super(source);
    }

    protected String getSSODevice() {
        return null;
    }

    protected Bundle getParameters(LoginClient.Request request) {
        Bundle parameters = new Bundle();
        if (!Utility.isNullOrEmpty(request.getPermissions())) {
            String scope = TextUtils.join(",", request.getPermissions());
            parameters.putString("scope", scope);
            addLoggingExtra("scope", scope);
        }
        DefaultAudience audience = request.getDefaultAudience();
        parameters.putString(ServerProtocol.DIALOG_PARAM_DEFAULT_AUDIENCE, audience.getNativeProtocolAudience());
        parameters.putString(ServerProtocol.DIALOG_PARAM_STATE, getClientState(request.getAuthId()));
        AccessToken previousToken = AccessToken.getCurrentAccessToken();
        String previousTokenString = previousToken != null ? previousToken.getToken() : null;
        if (previousTokenString != null && previousTokenString.equals(loadCookieToken())) {
            parameters.putString("access_token", previousTokenString);
            addLoggingExtra("access_token", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        } else {
            Utility.clearFacebookCookies(this.loginClient.getActivity());
            addLoggingExtra("access_token", "0");
        }
        return parameters;
    }

    protected Bundle addExtraParameters(Bundle parameters, LoginClient.Request request) {
        parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, getRedirectUri());
        parameters.putString("client_id", request.getApplicationId());
        LoginClient loginClient = this.loginClient;
        parameters.putString("e2e", LoginClient.getE2E());
        parameters.putString(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST);
        parameters.putString(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
        parameters.putString(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, ServerProtocol.DIALOG_REREQUEST_AUTH_TYPE);
        if (getSSODevice() != null) {
            parameters.putString(ServerProtocol.DIALOG_PARAM_SSO_DEVICE, getSSODevice());
        }
        return parameters;
    }

    protected void onComplete(LoginClient.Request request, Bundle values, FacebookException error) {
        LoginClient.Result outcome;
        this.e2e = null;
        if (values != null) {
            if (values.containsKey("e2e")) {
                this.e2e = values.getString("e2e");
            }
            try {
                AccessToken token = createAccessTokenFromWebBundle(request.getPermissions(), values, getTokenSource(), request.getApplicationId());
                outcome = LoginClient.Result.createTokenResult(this.loginClient.getPendingRequest(), token);
                CookieSyncManager syncManager = CookieSyncManager.createInstance(this.loginClient.getActivity());
                syncManager.sync();
                saveCookieToken(token.getToken());
            } catch (FacebookException ex) {
                outcome = LoginClient.Result.createErrorResult(this.loginClient.getPendingRequest(), null, ex.getMessage());
            }
        } else if (error instanceof FacebookOperationCanceledException) {
            outcome = LoginClient.Result.createCancelResult(this.loginClient.getPendingRequest(), "User canceled log in.");
        } else {
            this.e2e = null;
            String errorCode = null;
            String errorMessage = error.getMessage();
            if (error instanceof FacebookServiceException) {
                FacebookRequestError requestError = ((FacebookServiceException) error).getRequestError();
                errorCode = String.format(Locale.ROOT, "%d", Integer.valueOf(requestError.getErrorCode()));
                errorMessage = requestError.toString();
            }
            outcome = LoginClient.Result.createErrorResult(this.loginClient.getPendingRequest(), null, errorMessage, errorCode);
        }
        if (!Utility.isNullOrEmpty(this.e2e)) {
            logWebLoginCompleted(this.e2e);
        }
        this.loginClient.completeAndValidate(outcome);
    }

    private String loadCookieToken() {
        Context context = this.loginClient.getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEB_VIEW_AUTH_HANDLER_STORE, 0);
        return sharedPreferences.getString(WEB_VIEW_AUTH_HANDLER_TOKEN_KEY, "");
    }

    private void saveCookieToken(String token) {
        Context context = this.loginClient.getActivity();
        context.getSharedPreferences(WEB_VIEW_AUTH_HANDLER_STORE, 0).edit().putString(WEB_VIEW_AUTH_HANDLER_TOKEN_KEY, token).apply();
    }
}
