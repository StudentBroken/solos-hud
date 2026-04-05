package com.facebook.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.login.LoginClient;

/* JADX INFO: loaded from: classes41.dex */
abstract class NativeAppLoginMethodHandler extends LoginMethodHandler {
    @Override // com.facebook.login.LoginMethodHandler
    abstract boolean tryAuthorize(LoginClient.Request request);

    NativeAppLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    NativeAppLoginMethodHandler(Parcel source) {
        super(source);
    }

    @Override // com.facebook.login.LoginMethodHandler
    boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        LoginClient.Result outcome;
        LoginClient.Request request = this.loginClient.getPendingRequest();
        if (data == null) {
            outcome = LoginClient.Result.createCancelResult(request, "Operation canceled");
        } else if (resultCode == 0) {
            outcome = handleResultCancel(request, data);
        } else if (resultCode != -1) {
            outcome = LoginClient.Result.createErrorResult(request, "Unexpected resultCode from authorization.", null);
        } else {
            outcome = handleResultOk(request, data);
        }
        if (outcome != null) {
            this.loginClient.completeAndValidate(outcome);
            return true;
        }
        this.loginClient.tryNextHandler();
        return true;
    }

    private LoginClient.Result handleResultOk(LoginClient.Request request, Intent data) {
        Bundle extras = data.getExtras();
        String error = getError(extras);
        String errorCode = extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_CODE);
        String errorMessage = getErrorMessage(extras);
        String e2e = extras.getString("e2e");
        if (!Utility.isNullOrEmpty(e2e)) {
            logWebLoginCompleted(e2e);
        }
        if (error == null && errorCode == null && errorMessage == null) {
            try {
                AccessToken token = createAccessTokenFromWebBundle(request.getPermissions(), extras, AccessTokenSource.FACEBOOK_APPLICATION_WEB, request.getApplicationId());
                return LoginClient.Result.createTokenResult(request, token);
            } catch (FacebookException ex) {
                return LoginClient.Result.createErrorResult(request, null, ex.getMessage());
            }
        }
        if (ServerProtocol.errorsProxyAuthDisabled.contains(error)) {
            return null;
        }
        if (ServerProtocol.errorsUserCanceled.contains(error)) {
            return LoginClient.Result.createCancelResult(request, null);
        }
        return LoginClient.Result.createErrorResult(request, error, errorMessage, errorCode);
    }

    private LoginClient.Result handleResultCancel(LoginClient.Request request, Intent data) {
        Bundle extras = data.getExtras();
        String error = getError(extras);
        String errorCode = extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_CODE);
        if (!ServerProtocol.errorConnectionFailure.equals(errorCode)) {
            return LoginClient.Result.createCancelResult(request, error);
        }
        String errorMessage = getErrorMessage(extras);
        return LoginClient.Result.createErrorResult(request, error, errorMessage, errorCode);
    }

    private String getError(Bundle extras) {
        String error = extras.getString("error");
        if (error == null) {
            return extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_TYPE);
        }
        return error;
    }

    private String getErrorMessage(Bundle extras) {
        String errorMessage = extras.getString(AnalyticsEvents.PARAMETER_SHARE_ERROR_MESSAGE);
        if (errorMessage == null) {
            return extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_DESCRIPTION);
        }
        return errorMessage;
    }

    protected boolean tryIntent(Intent intent, int requestCode) {
        if (intent == null) {
            return false;
        }
        try {
            this.loginClient.getFragment().startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
