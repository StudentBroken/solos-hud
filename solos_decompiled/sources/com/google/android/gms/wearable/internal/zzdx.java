package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.MessageEvent;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdx extends com.google.android.gms.common.internal.safeparcel.zza implements MessageEvent {
    public static final Parcelable.Creator<zzdx> CREATOR = new zzdy();
    private final String mPath;
    private final int zzaLX;
    private final String zzaeM;
    private final byte[] zzbec;

    public zzdx(int i, String str, byte[] bArr, String str2) {
        this.zzaLX = i;
        this.mPath = str;
        this.zzbec = bArr;
        this.zzaeM = str2;
    }

    @Override // com.google.android.gms.wearable.MessageEvent
    public final byte[] getData() {
        return this.zzbec;
    }

    @Override // com.google.android.gms.wearable.MessageEvent
    public final String getPath() {
        return this.mPath;
    }

    @Override // com.google.android.gms.wearable.MessageEvent
    public final int getRequestId() {
        return this.zzaLX;
    }

    @Override // com.google.android.gms.wearable.MessageEvent
    public final String getSourceNodeId() {
        return this.zzaeM;
    }

    public final String toString() {
        int i = this.zzaLX;
        String str = this.mPath;
        String strValueOf = String.valueOf(this.zzbec == null ? "null" : Integer.valueOf(this.zzbec.length));
        return new StringBuilder(String.valueOf(str).length() + 43 + String.valueOf(strValueOf).length()).append("MessageEventParcelable[").append(i).append(",").append(str).append(", size=").append(strValueOf).append("]").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, getRequestId());
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getPath(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, getData(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getSourceNodeId(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
