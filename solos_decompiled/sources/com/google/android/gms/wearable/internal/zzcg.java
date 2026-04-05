package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcg extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcg> CREATOR = new zzch();
    public final int statusCode;
    public final List<zzaa> zzbSI;

    public zzcg(int i, List<zzaa> list) {
        this.statusCode = i;
        this.zzbSI = list;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.statusCode);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzbSI, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
