package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zza implements Parcelable.Creator<BitmapTeleporter> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ BitmapTeleporter createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        ParcelFileDescriptor parcelFileDescriptor = null;
        int iZzg = 0;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    parcelFileDescriptor = (ParcelFileDescriptor) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, ParcelFileDescriptor.CREATOR);
                    break;
                case 3:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new BitmapTeleporter(iZzg, parcelFileDescriptor, iZzg2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ BitmapTeleporter[] newArray(int i) {
        return new BitmapTeleporter[i];
    }
}
