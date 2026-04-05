package com.digits.sdk.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.widget.EditText;
import com.digits.sdk.android.DigitsScribeConstants;
import com.digits.sdk.android.PhoneNumberTask;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes18.dex */
class PhoneNumberController extends DigitsControllerImpl implements PhoneNumberTask.Listener {
    final CountryListSpinner countryCodeSpinner;
    boolean emailCollection;
    String phoneNumber;
    boolean resendState;
    private final TosView tosView;
    boolean voiceEnabled;

    PhoneNumberController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, CountryListSpinner countryCodeSpinner, TosView tosView, DigitsScribeService scribeService, boolean emailCollection) {
        this(resultReceiver, stateButton, phoneEditText, countryCodeSpinner, Digits.getInstance().getDigitsClient(), new PhoneNumberErrorCodes(stateButton.getContext().getResources()), Digits.getInstance().getActivityClassManager(), Digits.getSessionManager(), tosView, scribeService, emailCollection);
    }

    PhoneNumberController(ResultReceiver resultReceiver, StateButton stateButton, EditText phoneEditText, CountryListSpinner countryCodeSpinner, DigitsClient client, ErrorCodes errors, ActivityClassManager activityClassManager, SessionManager<DigitsSession> sessionManager, TosView tosView, DigitsScribeService scribeService, boolean emailCollection) {
        super(resultReceiver, stateButton, phoneEditText, client, errors, activityClassManager, sessionManager, scribeService);
        this.countryCodeSpinner = countryCodeSpinner;
        this.tosView = tosView;
        this.voiceEnabled = false;
        this.resendState = false;
        this.emailCollection = emailCollection;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        if (PhoneNumber.isValid(phoneNumber)) {
            this.editText.setText(phoneNumber.getPhoneNumber());
            this.editText.setSelection(phoneNumber.getPhoneNumber().length());
        }
    }

    public void setCountryCode(PhoneNumber phoneNumber) {
        if (PhoneNumber.isCountryValid(phoneNumber)) {
            this.countryCodeSpinner.setSelectedForCountry(new Locale("", phoneNumber.getCountryIso()).getDisplayName(), phoneNumber.getCountryCode());
        }
    }

    @Override // com.digits.sdk.android.DigitsController
    public void executeRequest(final Context context) {
        scribeRequest();
        if (validateInput(this.editText.getText())) {
            this.sendButton.showProgress();
            CommonUtils.hideKeyboard(context, this.editText);
            int code = ((Integer) this.countryCodeSpinner.getTag()).intValue();
            String number = this.editText.getText().toString();
            this.phoneNumber = getNumber(code, number);
            this.digitsClient.authDevice(this.phoneNumber, getVerificationType(), new DigitsCallback<AuthResponse>(context, this) { // from class: com.digits.sdk.android.PhoneNumberController.1
                @Override // com.twitter.sdk.android.core.Callback
                public void success(final Result<AuthResponse> result) {
                    PhoneNumberController.this.sendButton.showFinish();
                    AuthConfig config = result.data.authConfig;
                    if (config != null) {
                        PhoneNumberController.this.voiceEnabled = config.isVoiceEnabled;
                        PhoneNumberController.this.emailCollection = config.isEmailEnabled && PhoneNumberController.this.emailCollection;
                    }
                    PhoneNumberController.this.editText.postDelayed(new Runnable() { // from class: com.digits.sdk.android.PhoneNumberController.1.1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.lang.Runnable
                        public void run() {
                            AuthResponse response = (AuthResponse) result.data;
                            PhoneNumberController.this.phoneNumber = response.normalizedPhoneNumber == null ? PhoneNumberController.this.phoneNumber : response.normalizedPhoneNumber;
                            PhoneNumberController.this.startSignIn(context, (AuthResponse) result.data);
                        }
                    }, 1500L);
                }
            });
        }
    }

    private void scribeRequest() {
        if (isRetry()) {
            this.scribeService.click(DigitsScribeConstants.Element.RETRY);
        } else {
            this.scribeService.click(DigitsScribeConstants.Element.SUBMIT);
        }
    }

    private boolean isRetry() {
        return this.errorCount > 0;
    }

    @NonNull
    private Verification getVerificationType() {
        return (this.resendState && this.voiceEnabled) ? Verification.voicecall : Verification.sms;
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, com.digits.sdk.android.DigitsController
    public void handleError(final Context context, DigitsException digitsException) {
        if (digitsException instanceof CouldNotAuthenticateException) {
            this.digitsClient.registerDevice(this.phoneNumber, getVerificationType(), new DigitsCallback<DeviceRegistrationResponse>(context, this) { // from class: com.digits.sdk.android.PhoneNumberController.2
                @Override // com.twitter.sdk.android.core.Callback
                public void success(Result<DeviceRegistrationResponse> result) {
                    DeviceRegistrationResponse response = result.data;
                    AuthConfig config = response.authConfig;
                    if (config != null) {
                        PhoneNumberController.this.voiceEnabled = config.isVoiceEnabled;
                        PhoneNumberController.this.emailCollection = config.isEmailEnabled && PhoneNumberController.this.emailCollection;
                    }
                    PhoneNumberController.this.phoneNumber = response.normalizedPhoneNumber == null ? PhoneNumberController.this.phoneNumber : response.normalizedPhoneNumber;
                    PhoneNumberController.this.sendButton.showFinish();
                    PhoneNumberController.this.startNextStep(context, result.data);
                }
            });
        } else {
            if (digitsException instanceof OperatorUnsupportedException) {
                this.voiceEnabled = digitsException.getConfig().isVoiceEnabled;
                resend();
                super.handleError(context, digitsException);
                return;
            }
            super.handleError(context, digitsException);
        }
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl
    Uri getTosUri() {
        return DigitsConstants.DIGITS_TOS;
    }

    void startSignIn(Context context, AuthResponse response) {
        this.scribeService.success();
        Intent intent = new Intent(context, this.activityClassManager.getLoginCodeActivity());
        Bundle bundle = getBundle();
        bundle.putString(DigitsClient.EXTRA_REQUEST_ID, response.requestId);
        bundle.putLong("user_id", response.userId);
        bundle.putParcelable(DigitsClient.EXTRA_AUTH_CONFIG, response.authConfig);
        bundle.putBoolean(DigitsClient.EXTRA_EMAIL, this.emailCollection);
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNextStep(Context context, DeviceRegistrationResponse response) {
        this.scribeService.success();
        Intent intent = new Intent(context, this.activityClassManager.getConfirmationActivity());
        Bundle bundle = getBundle();
        bundle.putParcelable(DigitsClient.EXTRA_AUTH_CONFIG, response.authConfig);
        bundle.putBoolean(DigitsClient.EXTRA_EMAIL, this.emailCollection);
        intent.putExtras(bundle);
        startActivityForResult((Activity) context, intent);
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(DigitsClient.EXTRA_PHONE, this.phoneNumber);
        bundle.putParcelable(DigitsClient.EXTRA_RESULT_RECEIVER, this.resultReceiver);
        return bundle;
    }

    private String getNumber(long countryCode, String numberTextView) {
        return "+" + String.valueOf(countryCode) + numberTextView;
    }

    @Override // com.digits.sdk.android.PhoneNumberTask.Listener
    public void onLoadComplete(PhoneNumber phoneNumber) {
        setPhoneNumber(phoneNumber);
        setCountryCode(phoneNumber);
    }

    public void resend() {
        this.resendState = true;
        if (this.voiceEnabled) {
            this.sendButton.setStatesText(R.string.dgts__call_me, R.string.dgts__calling, R.string.dgts__calling);
            this.tosView.setText(R.string.dgts__terms_text_call_me);
        }
    }

    @Override // com.digits.sdk.android.DigitsControllerImpl, android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
        if (Verification.voicecall.equals(getVerificationType())) {
            this.resendState = false;
            this.sendButton.setStatesText(R.string.dgts__continue, R.string.dgts__sending, R.string.dgts__done);
            this.sendButton.showStart();
            this.tosView.setText(R.string.dgts__terms_text);
        }
    }
}
