package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class PhoneNumberActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PhoneNumberActivityDelegate(new PhoneNumberScribeService(Digits.getInstance().getScribeClient()));
    }
}
