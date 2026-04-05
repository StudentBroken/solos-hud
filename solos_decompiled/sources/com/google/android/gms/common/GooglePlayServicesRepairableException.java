package com.google.android.gms.common;

import android.content.Intent;

/* JADX INFO: loaded from: classes67.dex */
public class GooglePlayServicesRepairableException extends UserRecoverableException {
    private final int zzakv;

    public GooglePlayServicesRepairableException(int i, String str, Intent intent) {
        super(str, intent);
        this.zzakv = i;
    }

    public int getConnectionStatusCode() {
        return this.zzakv;
    }
}
