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
class LoginCodeActivityDelegate extends DigitsActivityDelegateImpl {
    Activity activity;
    AuthConfig config;
    DigitsController controller;
    EditText editText;
    SmsBroadcastReceiver receiver;
    private final DigitsScribeService scribeService;
    StateButton stateButton;
    TextView termsText;

    LoginCodeActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public void init(Activity activity, Bundle bundle) {
        this.activity = activity;
        this.editText = (EditText) activity.findViewById(R.id.dgts__confirmationEditText);
        this.stateButton = (StateButton) activity.findViewById(R.id.dgts__createAccount);
        this.termsText = (TextView) activity.findViewById(R.id.dgts__termsTextCreateAccount);
        TextView resendText = (TextView) activity.findViewById(R.id.dgts__resendConfirmation);
        this.config = (AuthConfig) bundle.getParcelable(DigitsClient.EXTRA_AUTH_CONFIG);
        this.controller = initController(bundle);
        setUpEditText(activity, this.controller, this.editText);
        setUpSendButton(activity, this.controller, this.stateButton);
        setUpTermsText(activity, this.controller, this.termsText);
        setUpResendText(activity, resendText);
        setUpSmsIntercept(activity, this.editText);
        CommonUtils.openKeyboard(activity, this.editText);
    }

    DigitsController initController(Bundle bundle) {
        return new LoginCodeController((ResultReceiver) bundle.getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER), this.stateButton, this.editText, bundle.getString(DigitsClient.EXTRA_REQUEST_ID), bundle.getLong("user_id"), bundle.getString(DigitsClient.EXTRA_PHONE), this.scribeService, Boolean.valueOf(bundle.getBoolean(DigitsClient.EXTRA_EMAIL)));
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        if (this.config != null && this.config.tosUpdate) {
            termsText.setText(getFormattedTerms(activity, R.string.dgts__terms_text_sign_in));
            super.setUpTermsText(activity, controller, termsText);
        } else {
            termsText.setVisibility(8);
        }
    }

    protected void setUpResendText(final Activity activity, TextView resendText) {
        resendText.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.LoginCodeActivityDelegate.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                LoginCodeActivityDelegate.this.scribeService.click(DigitsScribeConstants.Element.RESEND);
                activity.setResult(300);
                activity.finish();
            }
        });
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public int getLayoutId() {
        return R.layout.dgts__activity_confirmation;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, DigitsClient.EXTRA_RESULT_RECEIVER, DigitsClient.EXTRA_PHONE, DigitsClient.EXTRA_REQUEST_ID, "user_id");
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
