package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class PhoneNumberActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PhoneNumberActivityDelegate(new PhoneNumberScribeService(Digits.getInstance().getScribeClient()));
    }
}
