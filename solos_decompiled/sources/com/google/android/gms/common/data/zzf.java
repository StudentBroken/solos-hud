package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes3.dex */
public final class zzf implements Parcelable.Creator<DataHolder> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DataHolder createFromParcel(Parcel parcel) {
        int iZzg = 0;
        Bundle bundleZzs = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        CursorWindow[] cursorWindowArr = null;
        String[] strArrZzA = null;
        int iZzg2 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    strArrZzA = com.google.android.gms.common.internal.safeparcel.zzb.zzA(parcel, i);
                    break;
                case 2:
                    cursorWindowArr = (CursorWindow[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, CursorWindow.CREATOR);
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 4:
                    bundleZzs = com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i);
                    break;
                case 1000:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        DataHolder dataHolder = new DataHolder(iZzg2, strArrZzA, cursorWindowArr, iZzg, bundleZzs);
        dataHolder.zzqP();
        return dataHolder;
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ DataHolder[] newArray(int i) {
        return new DataHolder[i];
    }
}
