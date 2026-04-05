package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcy extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcy> CREATOR = new zzcz();
    public final int statusCode;
    public final List<zzeg> zzbSQ;

    public zzcy(int i, List<zzeg> list) {
        this.statusCode = i;
        this.zzbSQ = list;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzbSQ, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
