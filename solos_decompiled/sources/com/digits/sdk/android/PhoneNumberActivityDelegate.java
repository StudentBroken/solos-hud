package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;
import com.digits.sdk.android.PhoneNumberTask;
import io.fabric.sdk.android.services.common.CommonUtils;

/* JADX INFO: loaded from: classes18.dex */
class PhoneNumberActivityDelegate extends DigitsActivityDelegateImpl implements PhoneNumberTask.Listener, TosView {
    private Activity activity;
    PhoneNumberController controller;
    CountryListSpinner countryCodeSpinner;
    EditText phoneEditText;
    private final DigitsScribeService scribeService;
    StateButton sendButton;
    TextView termsTextView;

    public PhoneNumberActivityDelegate(DigitsScribeService scribeService) {
        this.scribeService = scribeService;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public int getLayoutId() {
        return R.layout.dgts__activity_phone_number;
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public boolean isValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, DigitsClient.EXTRA_RESULT_RECEIVER);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegate
    public void init(Activity activity, Bundle bundle) {
        this.activity = activity;
        this.countryCodeSpinner = (CountryListSpinner) activity.findViewById(R.id.dgts__countryCode);
        this.sendButton = (StateButton) activity.findViewById(R.id.dgts__sendCodeButton);
        this.phoneEditText = (EditText) activity.findViewById(R.id.dgts__phoneNumberEditText);
        this.termsTextView = (TextView) activity.findViewById(R.id.dgts__termsText);
        this.controller = initController(bundle);
        setUpEditText(activity, this.controller, this.phoneEditText);
        setUpSendButton(activity, this.controller, this.sendButton);
        setUpTermsText(activity, this.controller, this.termsTextView);
        setUpCountrySpinner(this.countryCodeSpinner);
        executePhoneNumberTask(new PhoneNumberUtils(SimManager.createSimManager(activity)), bundle);
        CommonUtils.openKeyboard(activity, this.phoneEditText);
    }

    private void executePhoneNumberTask(PhoneNumberUtils phoneNumberUtils, Bundle bundle) {
        String phoneNumber = bundle.getString(DigitsClient.EXTRA_PHONE);
        if (TextUtils.isEmpty(phoneNumber)) {
            new PhoneNumberTask(phoneNumberUtils, this).executeOnExecutor(Digits.getInstance().getExecutorService(), new Void[0]);
        } else {
            new PhoneNumberTask(phoneNumberUtils, phoneNumber, this).executeOnExecutor(Digits.getInstance().getExecutorService(), new Void[0]);
        }
    }

    PhoneNumberController initController(Bundle bundle) {
        return new PhoneNumberController((ResultReceiver) bundle.getParcelable(DigitsClient.EXTRA_RESULT_RECEIVER), this.sendButton, this.phoneEditText, this.countryCodeSpinner, this, this.scribeService, bundle.getBoolean(DigitsClient.EXTRA_EMAIL));
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl
    public void setUpTermsText(Activity activity, DigitsController controller, TextView termsText) {
        termsText.setText(getFormattedTerms(activity, R.string.dgts__terms_text));
        super.setUpTermsText(activity, controller, termsText);
    }

    protected void setUpCountrySpinner(CountryListSpinner countryCodeSpinner) {
        countryCodeSpinner.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.PhoneNumberActivityDelegate.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PhoneNumberActivityDelegate.this.scribeService.click(DigitsScribeConstants.Element.COUNTRY_CODE);
                PhoneNumberActivityDelegate.this.controller.clearError();
            }
        });
    }

    @Override // com.digits.sdk.android.ActivityLifecycle
    public void onResume() {
        this.scribeService.impression();
        this.controller.onResume();
    }

    @Override // com.digits.sdk.android.PhoneNumberTask.Listener
    public void onLoadComplete(PhoneNumber phoneNumber) {
        this.controller.setPhoneNumber(phoneNumber);
        this.controller.setCountryCode(phoneNumber);
    }

    @Override // com.digits.sdk.android.DigitsActivityDelegateImpl, com.digits.sdk.android.ActivityLifecycle
    public void onActivityResult(int requestCode, int resultCode, Activity activity) {
        if (resultCode == 300 && requestCode == 140) {
            this.controller.resend();
        }
    }

    @Override // com.digits.sdk.android.TosView
    public void setText(int resourceId) {
        this.termsTextView.setText(getFormattedTerms(this.activity, resourceId));
    }
}
