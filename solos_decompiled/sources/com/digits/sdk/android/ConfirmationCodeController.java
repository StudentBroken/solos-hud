package com.digits.sdk.android;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import android.widget.EditText;
import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
class ConfirmationCodeController extends DigitsControllerImpl {
    private final Boolean isEmailCollection;
    private final String phoneNumber;

    ConfirmationCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, String phoneNumber, DigitsScribeService scribeService, boolean isEmailCollection) {
        this(resultReceiver, stateButton, phoneEditText, phoneNumber, Digits.getSessionManager(), Digits.getInstance().getDigitsClient(), new ConfirmationErrorCodes(stateButton.getContext().getResources()), Digits.getInstance().getActivityClassManager(), scribeService, isEmailCollection);
    }

    ConfirmationCodeController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, String phoneNumber, SessionManager<DigitsSession> sessionManager, DigitsClient client, ErrorCodes errors, ActivityClassManager activityClassManager, DigitsScribeService scribeService, boolean isEmailCollection) {
        super(resultReceiver, stateButton, phoneEditText, client, errors, activityClassManager, sessionManager, scribeService);
        this.phoneNumber = phoneNumber;
        this.isEmailCollection = Boolean.valueOf(isEmailCollection);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            String code = this.editText.getText().toString();
            this.digitsClient.createAccount(code, this.phoneNumber, new DigitsCallback<DigitsUser>(context, this) { // from class: com.digits.sdk.android.ConfirmationCodeController.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<DigitsUser> result) {
                    ConfirmationCodeController.this.scribeService.success();
                    DigitsSession session = DigitsSession.create(result, ConfirmationCodeController.this.phoneNumber);
                    if (!ConfirmationCodeController.this.isEmailCollection.booleanValue()) {
                        ConfirmationCodeController.this.loginSuccess(context, session, ConfirmationCodeController.this.phoneNumber);
                    } else {
                        ConfirmationCodeController.this.sessionManager.setActiveSession(session);
                        ConfirmationCodeController.this.startEmailRequest(context, ConfirmationCodeController.this.phoneNumber);
                    }
                }
            });
        }
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return DigitsConstants.TWITTER_TOS;
    }
}
