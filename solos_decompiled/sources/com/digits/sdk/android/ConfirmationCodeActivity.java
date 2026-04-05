package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class ConfirmationCodeActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new ConfirmationCodeActivityDelegate(new ConfirmationCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
