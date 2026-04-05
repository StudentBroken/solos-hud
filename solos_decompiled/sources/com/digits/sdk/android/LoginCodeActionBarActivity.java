package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class LoginCodeActionBarActivity extends DigitsActionBarActivity {
    @Override // com.digits.sdk.android.DigitsActionBarActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new LoginCodeActivityDelegate(new LoginCodeScribeService(Digits.getInstance().getScribeClient()));
    }
}
