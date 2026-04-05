package com.google.android.gms.wearable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.wearable.internal.DataItemAssetParcelable;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes6.dex */
public class PutDataRequest extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final String WEAR_URI_SCHEME = "wear";
    private final Uri mUri;
    private final Bundle zzbRl;
    private long zzbRm;
    private byte[] zzbec;
    public static final Parcelable.Creator<PutDataRequest> CREATOR = new zzh();
    private static final long zzbRj = TimeUnit.MINUTES.toMillis(30);
    private static final Random zzbRk = new SecureRandom();

    private PutDataRequest(Uri uri) {
        this(uri, new Bundle(), null, zzbRj);
    }

    PutDataRequest(Uri uri, Bundle bundle, byte[] bArr, long j) {
        this.mUri = uri;
        this.zzbRl = bundle;
        this.zzbRl.setClassLoader(DataItemAssetParcelable.class.getClassLoader());
        this.zzbec = bArr;
        this.zzbRm = j;
    }

    public static PutDataRequest create(String str) {
        return zzt(zzgl(str));
    }

    public static PutDataRequest createFromDataItem(DataItem dataItem) {
        PutDataRequest putDataRequestZzt = zzt(dataItem.getUri());
        for (Map.Entry<String, DataItemAsset> entry : dataItem.getAssets().entrySet()) {
            if (entry.getValue().getId() == null) {
                String strValueOf = String.valueOf(entry.getKey());
                throw new IllegalStateException(strValueOf.length() != 0 ? "Cannot create an asset for a put request without a digest: ".concat(strValueOf) : new String("Cannot create an asset for a put request without a digest: "));
            }
            putDataRequestZzt.putAsset(entry.getKey(), Asset.createFromRef(entry.getValue().getId()));
        }
        putDataRequestZzt.setData(dataItem.getData());
        return putDataRequestZzt;
    }

    public static PutDataRequest createWithAutoAppendedId(String str) {
        StringBuilder sb = new StringBuilder(str);
        if (!str.endsWith("/")) {
            sb.append("/");
        }
        sb.append("PN").append(zzbRk.nextLong());
        return new PutDataRequest(zzgl(sb.toString()));
    }

    private static Uri zzgl(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("An empty path was supplied.");
        }
        if (!str.startsWith("/")) {
            throw new IllegalArgumentException("A path must start with a single / .");
        }
        if (str.startsWith("//")) {
            throw new IllegalArgumentException("A path must start with a single / .");
        }
        return new Uri.Builder().scheme(WEAR_URI_SCHEME).path(str).build();
    }

    public static PutDataRequest zzt(Uri uri) {
        return new PutDataRequest(uri);
    }

    public Asset getAsset(String str) {
        return (Asset) this.zzbRl.getParcelable(str);
    }

    public Map<String, Asset> getAssets() {
        HashMap map = new HashMap();
        for (String str : this.zzbRl.keySet()) {
            map.put(str, (Asset) this.zzbRl.getParcelable(str));
        }
        return Collections.unmodifiableMap(map);
    }

    public byte[] getData() {
        return this.zzbec;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public boolean hasAsset(String str) {
        return this.zzbRl.containsKey(str);
    }

    public boolean isUrgent() {
        return this.zzbRm == 0;
    }

    public PutDataRequest putAsset(String str, Asset asset) {
        zzbr.zzu(str);
        zzbr.zzu(asset);
        this.zzbRl.putParcelable(str, asset);
        return this;
    }

    public PutDataRequest removeAsset(String str) {
        this.zzbRl.remove(str);
        return this;
    }

    public PutDataRequest setData(byte[] bArr) {
        this.zzbec = bArr;
        return this;
    }

    public PutDataRequest setUrgent() {
        this.zzbRm = 0L;
        return this;
    }

    public String toString() {
        return toString(Log.isLoggable(DataMap.TAG, 3));
    }

    public String toString(boolean z) {
        StringBuilder sb = new StringBuilder("PutDataRequest[");
        String strValueOf = String.valueOf(this.zzbec == null ? "null" : Integer.valueOf(this.zzbec.length));
        sb.append(new StringBuilder(String.valueOf(strValueOf).length() + 7).append("dataSz=").append(strValueOf).toString());
        sb.append(new StringBuilder(23).append(", numAssets=").append(this.zzbRl.size()).toString());
        String strValueOf2 = String.valueOf(this.mUri);
        sb.append(new StringBuilder(String.valueOf(strValueOf2).length() + 6).append(", uri=").append(strValueOf2).toString());
        sb.append(new StringBuilder(35).append(", syncDeadline=").append(this.zzbRm).toString());
        if (!z) {
            sb.append("]");
            return sb.toString();
        }
        sb.append("]\n  assets: ");
        for (String str : this.zzbRl.keySet()) {
            String strValueOf3 = String.valueOf(this.zzbRl.getParcelable(str));
            sb.append(new StringBuilder(String.valueOf(str).length() + 7 + String.valueOf(strValueOf3).length()).append("\n    ").append(str).append(": ").append(strValueOf3).toString());
        }
        sb.append("\n  ]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) getUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbRl, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, getData(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzbRm);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
