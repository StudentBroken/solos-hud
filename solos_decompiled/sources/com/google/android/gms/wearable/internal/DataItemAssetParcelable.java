package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.wearable.DataItemAsset;

/* JADX INFO: loaded from: classes6.dex */
@KeepName
public class DataItemAssetParcelable extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable, DataItemAsset {
    public static final Parcelable.Creator<DataItemAssetParcelable> CREATOR = new zzby();
    private final String zzBQ;
    private final String zzIl;

    public DataItemAssetParcelable(DataItemAsset dataItemAsset) {
        this.zzIl = (String) com.google.android.gms.common.internal.zzbr.zzu(dataItemAsset.getId());
        this.zzBQ = (String) com.google.android.gms.common.internal.zzbr.zzu(dataItemAsset.getDataItemKey());
    }

    DataItemAssetParcelable(String str, String str2) {
        this.zzIl = str;
        this.zzBQ = str2;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public /* bridge */ /* synthetic */ DataItemAsset freeze() {
        return this;
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public String getDataItemKey() {
        return this.zzBQ;
    }

    @Override // com.google.android.gms.wearable.DataItemAsset
    public String getId() {
        return this.zzIl;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DataItemAssetParcelable[");
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        if (this.zzIl == null) {
            sb.append(",noid");
        } else {
            sb.append(",");
            sb.append(this.zzIl);
        }
        sb.append(", key=");
        sb.append(this.zzBQ);
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, getId(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getDataItemKey(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
