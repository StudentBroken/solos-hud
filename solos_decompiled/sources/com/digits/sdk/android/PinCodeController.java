package com.digits.sdk.android;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import android.widget.EditText;
import com.digits.sdk.android.DigitsApiClient;
import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
class PinCodeController extends DigitsControllerImpl {
    private final Boolean isEmailCollection;
    private final String phoneNumber;
    private final String requestId;
    private final long userId;

    PinCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, String requestId, long userId, String phoneNumber, DigitsScribeService scribeService, Boolean isEmailCollection) {
        this(resultReceiver, stateButton, phoneEditText, Digits.getSessionManager(), Digits.getInstance().getDigitsClient(), requestId, userId, phoneNumber, new ConfirmationErrorCodes(stateButton.getContext().getResources()), Digits.getInstance().getActivityClassManager(), scribeService, isEmailCollection);
    }

    PinCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, SessionManager<DigitsSession> sessionManager, DigitsClient digitsClient, String requestId, long userId, String phoneNumber, ErrorCodes errors, ActivityClassManager activityClassManager, DigitsScribeService scribeService, Boolean isEmailCollection) {
        super(resultReceiver, stateButton, phoneEditText, digitsClient, errors, activityClassManager, sessionManager, scribeService);
        this.requestId = requestId;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.isEmailCollection = isEmailCollection;
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public void showTOS(Context context) {
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return null;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            String code = this.editText.getText().toString();
            this.digitsClient.verifyPin(this.requestId, this.userId, code, new DigitsCallback<DigitsSessionResponse>(context, this) { // from class: com.digits.sdk.android.PinCodeController.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<DigitsSessionResponse> result) {
                    PinCodeController.this.scribeService.success();
                    DigitsSession session = DigitsSession.create(result.data, PinCodeController.this.phoneNumber);
                    if (PinCodeController.this.isEmailCollection.booleanValue()) {
                        PinCodeController.this.emailRequest(context, session);
                    } else {
                        PinCodeController.this.loginSuccess(context, session, PinCodeController.this.phoneNumber);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canRequestEmail(DigitsSession newSession, DigitsSession session) {
        return this.isEmailCollection.booleanValue() && newSession.getEmail().equals(DigitsSession.DEFAULT_EMAIL) && newSession.getId() == session.getId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void emailRequest(final Context context, final DigitsSession session) {
        getAccountService(session).verifyAccount(new DigitsCallback<VerifyAccountResponse>(context, this) { // from class: com.digits.sdk.android.PinCodeController.2
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<VerifyAccountResponse> result) {
                DigitsSession newSession = DigitsSession.create(result.data);
                if (!PinCodeController.this.canRequestEmail(newSession, session)) {
                    PinCodeController.this.loginSuccess(context, newSession, PinCodeController.this.phoneNumber);
                } else {
                    PinCodeController.this.sessionManager.setActiveSession(newSession);
                    PinCodeController.this.startEmailRequest(context, PinCodeController.this.phoneNumber);
                }
            }
        });
    }

    DigitsApiClient.AccountService getAccountService(DigitsSession session) {
        return new DigitsApiClient(session).getAccountService();
    }
}
