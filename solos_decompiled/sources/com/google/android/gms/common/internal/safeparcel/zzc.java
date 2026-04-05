package com.google.android.gms.common.internal.safeparcel;

import android.os.Parcel;

/* JADX INFO: loaded from: classes67.dex */
public final class zzc extends RuntimeException {
    /* JADX WARN: Illegal instructions before constructor call */
    public zzc(String str, Parcel parcel) {
        int iDataPosition = parcel.dataPosition();
        super(new StringBuilder(String.valueOf(str).length() + 41).append(str).append(" Parcel: pos=").append(iDataPosition).append(" size=").append(parcel.dataSize()).toString());
    }
}
