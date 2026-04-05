package com.digits.sdk.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/* JADX INFO: loaded from: classes18.dex */
public class ContactsActionBarActivity extends ActionBarActivity {
    ContactsActivityDelegateImpl delegate;

    /* JADX WARN: Multi-variable type inference failed */
    public void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra(ThemeUtils.THEME_RESOURCE_ID, R.style.Theme_AppCompat_Light));
        super.onCreate(savedInstanceState);
        this.delegate = new ContactsActivityDelegateImpl(this);
        this.delegate.init();
    }
}
