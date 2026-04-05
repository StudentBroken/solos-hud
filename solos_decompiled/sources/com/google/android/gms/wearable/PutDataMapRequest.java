package com.google.android.gms.wearable;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.internal.aif;
import com.google.android.gms.internal.io;
import com.google.android.gms.internal.ip;

/* JADX INFO: loaded from: classes6.dex */
public class PutDataMapRequest {
    private final DataMap zzbRh = new DataMap();
    private final PutDataRequest zzbRi;

    private PutDataMapRequest(PutDataRequest putDataRequest, DataMap dataMap) {
        this.zzbRi = putDataRequest;
        if (dataMap != null) {
            this.zzbRh.putAll(dataMap);
        }
    }

    public static PutDataMapRequest create(String str) {
        return new PutDataMapRequest(PutDataRequest.create(str), null);
    }

    public static PutDataMapRequest createFromDataMapItem(DataMapItem dataMapItem) {
        return new PutDataMapRequest(PutDataRequest.zzt(dataMapItem.getUri()), dataMapItem.getDataMap());
    }

    public static PutDataMapRequest createWithAutoAppendedId(String str) {
        return new PutDataMapRequest(PutDataRequest.createWithAutoAppendedId(str), null);
    }

    public PutDataRequest asPutDataRequest() {
        ip ipVarZza = io.zza(this.zzbRh);
        this.zzbRi.setData(aif.zzd(ipVarZza.zzbTH));
        int size = ipVarZza.zzbTI.size();
        for (int i = 0; i < size; i++) {
            String string = Integer.toString(i);
            Asset asset = ipVarZza.zzbTI.get(i);
            if (string == null) {
                String strValueOf = String.valueOf(asset);
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 26).append("asset key cannot be null: ").append(strValueOf).toString());
            }
            if (asset == null) {
                String strValueOf2 = String.valueOf(string);
                throw new IllegalStateException(strValueOf2.length() != 0 ? "asset cannot be null: key=".concat(strValueOf2) : new String("asset cannot be null: key="));
            }
            if (Log.isLoggable(DataMap.TAG, 3)) {
                String strValueOf3 = String.valueOf(asset);
                Log.d(DataMap.TAG, new StringBuilder(String.valueOf(string).length() + 33 + String.valueOf(strValueOf3).length()).append("asPutDataRequest: adding asset: ").append(string).append(" ").append(strValueOf3).toString());
            }
            this.zzbRi.putAsset(string, asset);
        }
        return this.zzbRi;
    }

    public DataMap getDataMap() {
        return this.zzbRh;
    }

    public Uri getUri() {
        return this.zzbRi.getUri();
    }

    public boolean isUrgent() {
        return this.zzbRi.isUrgent();
    }

    public PutDataMapRequest setUrgent() {
        this.zzbRi.setUrgent();
        return this;
    }
}
