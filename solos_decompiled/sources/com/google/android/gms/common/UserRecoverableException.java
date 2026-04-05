package com.google.android.gms.common;

import android.content.Intent;

/* JADX INFO: loaded from: classes67.dex */
public class UserRecoverableException extends Exception {
    private final Intent mIntent;

    public UserRecoverableException(String str, Intent intent) {
        super(str);
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return new Intent(this.mIntent);
    }
}
