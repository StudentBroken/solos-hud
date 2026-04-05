package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zzb;

/* JADX INFO: loaded from: classes67.dex */
public final class zzl implements Parcelable.Creator<PlaceReport> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PlaceReport createFromParcel(Parcel parcel) {
        String strZzq = null;
        int iZzd = zzb.zzd(parcel);
        String strZzq2 = null;
        int iZzg = 0;
        String strZzq3 = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = zzb.zzg(parcel, i);
                    break;
                case 2:
                    strZzq2 = zzb.zzq(parcel, i);
                    break;
                case 3:
                    strZzq3 = zzb.zzq(parcel, i);
                    break;
                case 4:
                    strZzq = zzb.zzq(parcel, i);
                    break;
                default:
                    zzb.zzb(parcel, i);
                    break;
            }
        }
        zzb.zzF(parcel, iZzd);
        return new PlaceReport(iZzg, strZzq2, strZzq3, strZzq);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ PlaceReport[] newArray(int i) {
        return new PlaceReport[i];
    }
}
