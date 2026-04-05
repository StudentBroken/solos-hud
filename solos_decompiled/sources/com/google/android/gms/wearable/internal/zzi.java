package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzi extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzi> CREATOR = new zzj();
    private final String mValue;
    private byte zzbRP;
    private final byte zzbRQ;

    public zzi(byte b, byte b2, String str) {
        this.zzbRP = b;
        this.zzbRQ = b2;
        this.mValue = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzi zziVar = (zzi) obj;
        return this.zzbRP == zziVar.zzbRP && this.zzbRQ == zziVar.zzbRQ && this.mValue.equals(zziVar.mValue);
    }

    public final int hashCode() {
        return ((((this.zzbRP + 31) * 31) + this.zzbRQ) * 31) + this.mValue.hashCode();
    }

    public final String toString() {
        byte b = this.zzbRP;
        byte b2 = this.zzbRQ;
        String str = this.mValue;
        return new StringBuilder(String.valueOf(str).length() + 73).append("AmsEntityUpdateParcelable{, mEntityId=").append((int) b).append(", mAttributeId=").append((int) b2).append(", mValue='").append(str).append("'}").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbRP);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbRQ);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.mValue, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
