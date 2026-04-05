package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class Tile extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<Tile> CREATOR = new zzq();
    public final byte[] data;
    public final int height;
    public final int width;

    public Tile(int i, int i2, byte[] bArr) {
        this.width = i;
        this.height = i2;
        this.data = bArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.width);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.height);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.data, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
