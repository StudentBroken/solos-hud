package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class PinCodeActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new PinCodeActivityDelegate(new PinCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
