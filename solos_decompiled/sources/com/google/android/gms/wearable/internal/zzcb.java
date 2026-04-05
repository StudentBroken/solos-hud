package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcb extends com.google.android.gms.common.internal.safeparcel.zza implements DataItem {
    public static final Parcelable.Creator<zzcb> CREATOR = new zzcc();
    private final Uri mUri;
    private final Map<String, DataItemAsset> zzbSG;
    private byte[] zzbec;

    zzcb(Uri uri, Bundle bundle, byte[] bArr) {
        this.mUri = uri;
        HashMap map = new HashMap();
        bundle.setClassLoader(DataItemAssetParcelable.class.getClassLoader());
        for (String str : bundle.keySet()) {
            map.put(str, (DataItemAssetParcelable) bundle.getParcelable(str));
        }
        this.zzbSG = map;
        this.zzbec = bArr;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* bridge */ /* synthetic */ DataItem freeze() {
        return this;
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final Map<String, DataItemAsset> getAssets() {
        return this.zzbSG;
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final byte[] getData() {
        return this.zzbec;
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final Uri getUri() {
        return this.mUri;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final boolean isDataValid() {
        return true;
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final /* synthetic */ DataItem setData(byte[] bArr) {
        this.zzbec = bArr;
        return this;
    }

    public final String toString() {
        boolean zIsLoggable = Log.isLoggable("DataItem", 3);
        StringBuilder sb = new StringBuilder("DataItemParcelable[");
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        String strValueOf = String.valueOf(this.zzbec == null ? "null" : Integer.valueOf(this.zzbec.length));
        sb.append(new StringBuilder(String.valueOf(strValueOf).length() + 8).append(",dataSz=").append(strValueOf).toString());
        sb.append(new StringBuilder(23).append(", numAssets=").append(this.zzbSG.size()).toString());
        String strValueOf2 = String.valueOf(this.mUri);
        sb.append(new StringBuilder(String.valueOf(strValueOf2).length() + 6).append(", uri=").append(strValueOf2).toString());
        if (!zIsLoggable) {
            sb.append("]");
            return sb.toString();
        }
        sb.append("]\n  assets: ");
        for (String str : this.zzbSG.keySet()) {
            String strValueOf3 = String.valueOf(this.zzbSG.get(str));
            sb.append(new StringBuilder(String.valueOf(str).length() + 7 + String.valueOf(strValueOf3).length()).append("\n    ").append(str).append(": ").append(strValueOf3).toString());
        }
        sb.append("\n  ]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getUri(), i, false);
        Bundle bundle = new Bundle();
        bundle.setClassLoader(DataItemAssetParcelable.class.getClassLoader());
        for (Map.Entry<String, DataItemAsset> entry : this.zzbSG.entrySet()) {
            bundle.putParcelable(entry.getKey(), new DataItemAssetParcelable(entry.getValue()));
        }
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, bundle, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getData(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
