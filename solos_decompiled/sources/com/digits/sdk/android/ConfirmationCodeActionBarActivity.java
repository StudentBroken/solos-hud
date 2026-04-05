package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class ConfirmationCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new ConfirmationCodeActivityDelegate(new ConfirmationCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
