package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.EditText;
import android.widget.TextView;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
public class EmailRequestActivityDelegate extends DigitsActivityDelegateImpl {
    Activity activity;
    DigitsController controller;
    EditText editText;
    TextView resendText;
    DigitsScribeService scribeService;
    StateButton stateButton;
    TextView termsText;
    TextView titleText;

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl, com.digits.sdk.android.ActivityLifecycle
    public /* bridge */ /* synthetic */ void onActivityResult(int x0, int x1, Activity x2) {
        super.onActivityResult(x0, x1, x2);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl, com.digits.sdk.android.ActivityLifecycle
    public /* bridge */ /* synthetic */ void onDestroy() {
        super.onDestroy();
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public /* bridge */ /* synthetic */ void setUpSendButton(Activity x0, DigitsController x1, StateButton x2) {
        super.setUpSendButton(x0, x1, x2);
    }

    EmailRequestActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public int getLayoutId() {
        return R.layout.dgts__activity_confirmation;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, DigitsClient.EXTRA_RESULT_RECEIVER, DigitsClient.EXTRA_PHONE);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public void init(Activity activity, Bundle bundle) {
        this.activity = activity;
        this.titleText = (TextView) activity.findViewById(R.id.dgts__titleText);
        this.editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        this.stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        this.termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        this.resendText = (TextView) activity.findViewById(R.id.dgts__resendConfirmation);
        this.controller = initController(bundle);
        this.editText.setHint(R.string.dgts__email_request_edit_hint);
        this.titleText.setText(R.string.dgts__email_request_title);
        setUpEditText(activity, this.controller, this.editText);
        setUpSendButton(activity, this.controller, this.stateButton);
        setUpTermsText(activity, this.controller, this.termsText);
        setUpResendText(this.resendText);
        CommonUtils.openKeyboard(activity, this.editText);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpEditText(Activity activity, DigitsController controller, EditText editText) {
        editText.setInputType(32);
        super.setUpEditText(activity, controller, editText);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        termsText.setText(getFormattedTerms(activity, R.string.dgts__terms_email_request));
        super.setUpTermsText(activity, controller, termsText);
    }

    private void setUpResendText(TextView resendText) {
        resendText.setVisibility(8);
    }

    private DigitsController initController(Bundle bundle) {
        return new EmailRequestController(this.stateButton, this.editText, (ResultReceiver) bundle.getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER), bundle.getString(DigitsClient.EXTRA_PHONE), this.scribeService);
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onResume() {
        this.scribeService.impression();
        this.controller.onResume();
    }
}
