package com.digits.sdk.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/* JADX INFO: loaded from: classes18.dex */
public abstract class DigitsActivity extends Activity {
    static final int REQUEST_CODE = 140;
    static final int RESULT_FINISH_DIGITS = 200;
    static final int RESULT_RESEND_CONFIRMATION = 300;
    DigitsActivityDelegate delegate;

    abstract DigitsActivityDelegate getActivityDelegate();

    @Override // android.app.Activity
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

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.delegate.onResume();
    }

    @Override // android.app.Activity
    public void onDestroy() {
        this.delegate.onDestroy();
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.delegate.onActivityResult(requestCode, resultCode, this);
        if (resultCode == 200 && requestCode == 140) {
            finish();
        }
    }
}
