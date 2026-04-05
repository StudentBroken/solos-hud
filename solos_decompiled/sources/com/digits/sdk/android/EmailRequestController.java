package com.digits.sdk.android;

import android.content.Context;
import android.net.Uri;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;
import com.digits.sdk.android.DigitsApiClient;
import com.digits.sdk.android.DigitsScribeConstants;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.regex.Matcher;

/* JADX INFO: loaded from: classes18.dex */
public class EmailRequestController extends DigitsControllerImpl {
    private String phoneNumber;

    @Override // com.digits.sdk.android.DigitsControllerImpl, android.text.TextWatcher
    public /* bridge */ /* synthetic */ void afterTextChanged(Editable x0) {
        super.afterTextChanged(x0);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, android.text.TextWatcher
    public /* bridge */ /* synthetic */ void beforeTextChanged(CharSequence x0, int x1, int x2, int x3) {
        super.beforeTextChanged(x0, x1, x2, x3);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ void clearError() {
        super.clearError();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ int getErrorCount() {
        return super.getErrorCount();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ ErrorCodes getErrors() {
        return super.getErrors();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ TextWatcher getTextWatcher() {
        return super.getTextWatcher();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ void handleError(Context x0, DigitsException x1) {
        super.handleError(x0, x1);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ void onResume() {
        super.onResume();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, android.text.TextWatcher
    public /* bridge */ /* synthetic */ void onTextChanged(CharSequence x0, int x1, int x2, int x3) {
        super.onTextChanged(x0, x1, x2, x3);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ void showTOS(Context x0) {
        super.showTOS(x0);
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public /* bridge */ /* synthetic */ void startFallback(Context x0, ResultReceiver x1, DigitsException x2) {
        super.startFallback(x0, x1, x2);
    }

    EmailRequestController(StateButton stateButton, EditText editText, ResultReceiver resultReceiver, String phoneNumber, DigitsScribeService scribeService) {
        this(resultReceiver, stateButton, editText, Digits.getSessionManager(), Digits.getInstance().getActivityClassManager(), new DigitsClient(), phoneNumber, scribeService, new EmailErrorCodes(stateButton.getContext().getResources()));
    }

    EmailRequestController(ResultReceiver resultReceiver, StateButton stateButton, EditText editText, SessionManager<DigitsSession> sessionManager, ActivityClassManager activityClassManager, DigitsClient client, String phoneNumber, DigitsScribeService scribeService, ErrorCodes emailErrorCodes) {
        super(resultReceiver, stateButton, editText, client, emailErrorCodes, activityClassManager, sessionManager, scribeService);
        this.phoneNumber = phoneNumber;
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return DigitsConstants.DIGITS_TOS;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            String email = this.editText.getText().toString();
            final DigitsSession session = (DigitsSession) this.sessionManager.getActiveSession();
            if (session != null && !session.isLoggedOutUser()) {
                DigitsApiClient.SdkService service = getSdkService(session);
                service.email(email, new DigitsCallback<DigitsSessionResponse>(context, this) { // from class: com.digits.sdk.android.EmailRequestController.1
                    @Override // com.twitter.sdk.android.core.Callback
                    public void success(Result<DigitsSessionResponse> result) {
                        EmailRequestController.this.scribeService.success();
                        EmailRequestController.this.loginSuccess(context, session, EmailRequestController.this.phoneNumber);
                    }
                });
                return;
            } else {
                handleError(context, new UnrecoverableException(""));
                return;
            }
        }
        this.editText.setError(context.getString(R.string.dgts__invalid_email));
    }

    DigitsApiClient.SdkService getSdkService(DigitsSession session) {
        return new DigitsApiClient(session).getSdkService();
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public boolean validateInput(CharSequence text) {
        return !TextUtils.isEmpty(text) && validate(text.toString());
    }

    private boolean validate(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.find();
    }
}
