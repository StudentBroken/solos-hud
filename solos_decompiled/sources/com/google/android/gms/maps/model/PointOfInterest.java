package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes10.dex */
public final class PointOfInterest extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<PointOfInterest> CREATOR = new zzj();
    public final LatLng latLng;
    public final String name;
    public final String placeId;

    public PointOfInterest(LatLng latLng, String str, String str2) {
        this.latLng = latLng;
        this.placeId = str;
        this.name = str2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.latLng, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.placeId, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.name, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
