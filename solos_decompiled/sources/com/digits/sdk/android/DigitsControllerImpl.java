package com.digits.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
abstract class DigitsControllerImpl implements DigitsController, TextWatcher {
    public static final int MAX_ERRORS = 5;
    static final long POST_DELAY_MS = 1500;
    final ActivityClassManager activityClassManager;
    final DigitsClient digitsClient;
    final EditText editText;
    int errorCount = 0;
    final ErrorCodes errors;
    final ResultReceiver resultReceiver;
    final DigitsScribeService scribeService;
    final StateButton sendButton;
    final SessionManager<DigitsSession> sessionManager;

    abstract Uri getTosUri();

    DigitsControllerImpl(ResultReceiver resultReceiver, StateButton stateButton, EditText editText, DigitsClient client, ErrorCodes errors, ActivityClassManager activityClassManager, SessionManager<DigitsSession> sessionManager, DigitsScribeService scribeService) {
        this.resultReceiver = resultReceiver;
        this.digitsClient = client;
        this.activityClassManager = activityClassManager;
        this.sendButton = stateButton;
        this.editText = editText;
        this.errors = errors;
        this.sessionManager = sessionManager;
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void showTOS(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(getTosUri());
        context.startActivity(intent);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void handleError(Context context, DigitsException exception) {
        this.errorCount++;
        this.scribeService.error(exception);
        if (isUnrecoverable(exception)) {
            this.scribeService.failure();
            startFallback(context, this.resultReceiver, exception);
        } else {
            this.editText.setError(exception.getLocalizedMessage());
            this.sendButton.showError();
        }
    }

    private boolean isUnrecoverable(DigitsException exception) {
        return this.errorCount == 5 || (exception instanceof UnrecoverableException);
    }

    void startActivityForResult(Activity activity, Intent intent) {
        activity.startActivityForResult(intent, 140);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void startFallback(Context context, ResultReceiver receiver, DigitsException reason) {
        Intent intent = new Intent(context, this.activityClassManager.getFailureActivity());
        intent.putExtra(DigitsClient.EXTRA_RESULT_RECEIVER, receiver);
        intent.putExtra(DigitsClient.EXTRA_FALLBACK_REASON, reason);
        context.startActivity(intent);
        CommonUtils.finishAffinity(context, 200);
    }

    @Override // com.digits.sdk.android.DigitsController
    public boolean validateInput(CharSequence text) {
        return !TextUtils.isEmpty(text);
    }

    @Override // com.digits.sdk.android.DigitsController
    public void clearError() {
        this.editText.setError(null);
    }

    @Override // com.digits.sdk.android.DigitsController
    public ErrorCodes getErrors() {
        return this.errors;
    }

    @Override // com.digits.sdk.android.DigitsController
    public int getErrorCount() {
        return this.errorCount;
    }

    @Override // com.digits.sdk.android.DigitsController
    public void onResume() {
        this.sendButton.showStart();
    }

    @Override // com.digits.sdk.android.DigitsController
    public TextWatcher getTextWatcher() {
        return this;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clearError();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
    }

    Bundle getBundle(String phoneNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(DigitsClient.EXTRA_PHONE, phoneNumber);
        return bundle;
    }

    void loginSuccess(final Context context, DigitsSession session, final String phoneNumber) {
        this.sessionManager.setActiveSession(session);
        this.sendButton.showFinish();
        this.editText.postDelayed(new Runnable() { // from class: com.digits.sdk.android.DigitsControllerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                DigitsControllerImpl.this.resultReceiver.send(200, DigitsControllerImpl.this.getBundle(phoneNumber));
                CommonUtils.finishAffinity((Activity) context, 200);
            }
        }, POST_DELAY_MS);
    }

    void startEmailRequest(Context context, String phoneNumber) {
        this.sendButton.showFinish();
        Intent intent = new Intent(context, this.activityClassManager.getEmailRequestActivity());
        Bundle bundle = getBundle(phoneNumber);
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, this.resultReceiver);
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
    }
}
