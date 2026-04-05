package com.google.android.gms.wearable;

import android.net.Uri;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zze implements Parcelable.Creator<Asset> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Asset createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Uri uri = null;
        ParcelFileDescriptor parcelFileDescriptor = null;
        String strZzq = null;
        byte[] bArrZzt = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 2:
                    bArrZzt = com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i);
                    break;
                case 3:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 4:
                    parcelFileDescriptor = (ParcelFileDescriptor) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, ParcelFileDescriptor.CREATOR);
                    break;
                case 5:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Uri.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new Asset(bArrZzt, strZzq, parcelFileDescriptor, uri);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Asset[] newArray(int i) {
        return new Asset[i];
    }
}
