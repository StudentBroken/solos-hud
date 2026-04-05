package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzew extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzew> CREATOR = new zzex();
    private int statusCode;
    private long zzbTe;
    private List<zzek> zzbTg;

    public zzew(int i, long j, List<zzek> list) {
        this.statusCode = i;
        this.zzbTe = j;
        this.zzbTg = list;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbTe);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, this.zzbTg, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
