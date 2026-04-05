package com.digits.sdk.android;

/* JADX INFO: loaded from: classes18.dex */
public class EmailRequestActivity extends DigitsActivity {
    @Override // com.digits.sdk.android.DigitsActivity
    DigitsActivityDelegate getActivityDelegate() {
        return new EmailRequestActivityDelegate(new EmailRequestScribeService(Digits.getInstance().getScribeClient()));
    }
}
