package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class PinCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PinCodeActivityDelegate(new PinCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
