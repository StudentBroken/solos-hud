package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
public final class zzca implements DataItem {
    private Uri mUri;
    private Map<String, DataItemAsset> zzbSG;
    private byte[] zzbec;

    public zzca(DataItem dataItem) {
        this.mUri = dataItem.getUri();
        this.zzbec = dataItem.getData();
        HashMap map = new HashMap();
        for (Map.Entry<String, DataItemAsset> entry : dataItem.getAssets().entrySet()) {
            if (entry.getKey() != null) {
                map.put(entry.getKey(), entry.getValue().freeze());
            }
        }
        this.zzbSG = Collections.unmodifiableMap(map);
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
    public final DataItem setData(byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    public final String toString() {
        boolean zIsLoggable = Log.isLoggable("DataItem", 3);
        StringBuilder sb = new StringBuilder("DataItemEntity{ ");
        String strValueOf = String.valueOf(this.mUri);
        sb.append(new StringBuilder(String.valueOf(strValueOf).length() + 4).append("uri=").append(strValueOf).toString());
        String strValueOf2 = String.valueOf(this.zzbec == null ? "null" : Integer.valueOf(this.zzbec.length));
        sb.append(new StringBuilder(String.valueOf(strValueOf2).length() + 9).append(", dataSz=").append(strValueOf2).toString());
        sb.append(new StringBuilder(23).append(", numAssets=").append(this.zzbSG.size()).toString());
        if (zIsLoggable && !this.zzbSG.isEmpty()) {
            sb.append(", assets=[");
            String str = "";
            Iterator<Map.Entry<String, DataItemAsset>> it = this.zzbSG.entrySet().iterator();
            while (true) {
                String str2 = str;
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<String, DataItemAsset> next = it.next();
                String key = next.getKey();
                String strValueOf3 = String.valueOf(next.getValue().getId());
                sb.append(new StringBuilder(String.valueOf(str2).length() + 2 + String.valueOf(key).length() + String.valueOf(strValueOf3).length()).append(str2).append(key).append(": ").append(strValueOf3).toString());
                str = ", ";
            }
            sb.append("]");
        }
        sb.append(" }");
        return sb.toString();
    }
}
