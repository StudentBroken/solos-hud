package com.digits.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import com.digits.sdk.android.DigitsApiClient;
import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
class LoginCodeController extends DigitsControllerImpl {
    private final Boolean emailCollection;
    private final String phoneNumber;
    private final String requestId;
    private final long userId;

    LoginCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, String requestId, long userId, String phoneNumber, DigitsScribeService scribeService, Boolean emailCollection) {
        this(resultReceiver, stateButton, phoneEditText, Digits.getSessionManager(), Digits.getInstance().getDigitsClient(), requestId, userId, phoneNumber, new ConfirmationErrorCodes(stateButton.getContext().getResources()), Digits.getInstance().getActivityClassManager(), scribeService, emailCollection);
    }

    LoginCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText loginEditText, SessionManager<DigitsSession> sessionManager, DigitsClient client, String requestId, long userId, String phoneNumber, ErrorCodes errors, ActivityClassManager activityClassManager, DigitsScribeService scribeService, Boolean emailCollection) {
        super(resultReceiver, stateButton, loginEditText, client, errors, activityClassManager, sessionManager, scribeService);
        this.requestId = requestId;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.emailCollection = emailCollection;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            String code = this.editText.getText().toString();
            this.digitsClient.loginDevice(this.requestId, this.userId, code, new DigitsCallback<DigitsSessionResponse>(context, this) { // from class: com.digits.sdk.android.LoginCodeController.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<DigitsSessionResponse> result) {
                    LoginCodeController.this.scribeService.success();
                    if (result.data.isEmpty()) {
                        LoginCodeController.this.startPinCodeActivity(context);
                    } else if (LoginCodeController.this.emailCollection.booleanValue()) {
                        DigitsSession session = DigitsSession.create(result.data, LoginCodeController.this.phoneNumber);
                        LoginCodeController.this.emailRequest(context, session);
                    } else {
                        DigitsSession session2 = DigitsSession.create(result.data, LoginCodeController.this.phoneNumber);
                        LoginCodeController.this.loginSuccess(context, session2, LoginCodeController.this.phoneNumber);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emailRequest(final Context context, final DigitsSession session) {
        getAccountService(session).verifyAccount(new DigitsCallback<VerifyAccountResponse>(context, this) { // from class: com.digits.sdk.android.LoginCodeController.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<VerifyAccountResponse> result) {
                DigitsSession newSession = DigitsSession.create(result.data);
                if (!LoginCodeController.this.canRequestEmail(newSession, session)) {
                    LoginCodeController.this.loginSuccess(context, newSession, LoginCodeController.this.phoneNumber);
                } else {
                    LoginCodeController.this.sessionManager.setActiveSession(newSession);
                    LoginCodeController.this.startEmailRequest(context, LoginCodeController.this.phoneNumber);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startPinCodeActivity(Context context) {
        Intent intent = new Intent(context, this.activityClassManager.getPinCodeActivity());
        Bundle bundle = getBundle(this.phoneNumber);
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, this.resultReceiver);
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, this.requestId);
        bundle.putLong("user_id", this.userId);
        bundle.putBoolean(DigitsClient.EXTRA_EMAIL, this.emailCollection.booleanValue());
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return DigitsConstants.TWITTER_TOS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canRequestEmail(DigitsSession newSession, DigitsSession session) {
        return this.emailCollection.booleanValue() && newSession.getEmail().equals(DigitsSession.DEFAULT_EMAIL) && newSession.getId() == session.getId();
    }

    DigitsApiClient.AccountService getAccountService(DigitsSession session) {
        return new DigitsApiClient(session).getAccountService();
    }
}
