package com.digits.sdk.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* JADX INFO: loaded from: classes18.dex */
public class FailureActionBarActivity extends ActionBarActivity {
    FailureActivityDelegateImpl delegate;

    /* JADX WARN: Multi-variable type inference failed */
    public void onCreate(Bundle savedInstanceState) {
        setTheme(Digits.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        this.delegate = new FailureActivityDelegateImpl(this);
        this.delegate.init();
    }
}
