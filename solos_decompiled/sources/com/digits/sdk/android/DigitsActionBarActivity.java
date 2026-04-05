package com.digits.sdk.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* JADX INFO: loaded from: classes18.dex */
public abstract class DigitsActionBarActivity extends ActionBarActivity {
    static final int REQUEST_CODE = 140;
    static final int RESULT_FINISH_DIGITS = 200;
    DigitsActivityDelegate delegate;

    abstract DigitsActivityDelegate getActivityDelegate();

    /* JADX WARN: Multi-variable type inference failed */
    public void onCreate(Bundle savedInstanceState) {
        setTheme(Digits.getInstance().getTheme());
        super.onCreate(savedInstanceState);
        this.delegate = getActivityDelegate();
        Bundle bundle = getIntent().getExtras();
        if (this.delegate.isValid(bundle)) {
            setContentView(this.delegate.getLayoutId());
            this.delegate.init(this, bundle);
        } else {
            finish();
            throw new IllegalAccessError("This activity can only be started from Digits");
        }
    }

    public void onResume() {
        super.onResume();
        this.delegate.onResume();
    }

    public void onDestroy() {
        this.delegate.onDestroy();
        super.onDestroy();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.delegate.onActivityResult(requestCode, resultCode, this);
        if (resultCode == 200 && requestCode == 140) {
            finish();
        }
    }
}
