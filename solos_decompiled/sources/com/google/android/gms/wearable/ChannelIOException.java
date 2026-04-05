package com.google.android.gms.wearable;

import java.io.IOException;

/* JADX INFO: loaded from: classes6.dex */
public class ChannelIOException extends IOException {
    private final int zzbRb;
    private final int zzbRc;

    public ChannelIOException(String str, int i, int i2) {
        super(str);
        this.zzbRb = i;
        this.zzbRc = i2;
    }

    public int getAppSpecificErrorCode() {
        return this.zzbRc;
    }

    public int getCloseReason() {
        return this.zzbRb;
    }
}
