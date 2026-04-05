package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;

/* JADX INFO: loaded from: classes18.dex */
public class FailureActivity extends Activity {
    FailureActivityDelegateImpl delegate;

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(Digits.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        this.delegate = new FailureActivityDelegateImpl(this);
        this.delegate.init();
    }
}
