package com.digits.sdk.android;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
class ConfirmationCodeActivityDelegate extends DigitsActivityDelegateImpl {
    Activity activity;
    DigitsController controller;
    EditText editText;
    SmsBroadcastReceiver receiver;
    TextView resendText;
    DigitsScribeService scribeService;
    StateButton stateButton;
    TextView termsText;

    public ConfirmationCodeActivityDelegate(DigitsScribeService scribeService) {
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
        this.editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        this.stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        this.termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        this.resendText = (TextView) activity.findViewById(R.id.dgts__resendConfirmation);
        this.controller = initController(bundle);
        setUpEditText(activity, this.controller, this.editText);
        setUpSendButton(activity, this.controller, this.stateButton);
        setUpTermsText(activity, this.controller, this.termsText);
        setUpResendText(activity, this.resendText);
        setUpSmsIntercept(activity, this.editText);
        CommonUtils.openKeyboard(activity, this.editText);
    }

    DigitsController initController(Bundle bundle) {
        return new ConfirmationCodeController((ResultReceiver) bundle.getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER), this.stateButton, this.editText, bundle.getString(DigitsClient.EXTRA_PHONE), this.scribeService, bundle.getBoolean(DigitsClient.EXTRA_EMAIL));
    }

    protected void setUpResendText(final Activity activity, TextView resendText) {
        resendText.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.ConfirmationCodeActivityDelegate.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ConfirmationCodeActivityDelegate.this.scribeService.click(DigitsScribeConstants.Element.RESEND);
                activity.setResult(300);
                activity.finish();
            }
        });
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        termsText.setText(getFormattedTerms(activity, R.string.dgts__terms_text_create));
        super.setUpTermsText(activity, controller, termsText);
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onResume() {
        this.scribeService.impression();
        this.controller.onResume();
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl, com.digits.sdk.android.ActivityLifecycle
    public void onDestroy() {
        if (this.receiver != null) {
            this.activity.unregisterReceiver(this.receiver);
        }
    }

    protected void setUpSmsIntercept(Activity activity, EditText editText) {
        if (CommonUtils.checkPermission(activity, "android.permission.RECEIVE_SMS")) {
            IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            this.receiver = new SmsBroadcastReceiver(editText);
            activity.registerReceiver(this.receiver, filter);
        }
    }
}
