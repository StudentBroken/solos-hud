package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class EmailRequestActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new EmailRequestActivityDelegate(new EmailRequestScribeService(Digits.getInstance().getScribeClient()));
    }
}
