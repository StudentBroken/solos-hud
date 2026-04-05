package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.util.Log;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
public final class zzcd extends com.google.android.gms.common.data.zzc implements DataItem {
    private final int zzbcT;

    public zzcd(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.zzbcT = i2;
    }

    @Override // com.google.android.gms.common.data.Freezable
    public final /* synthetic */ DataItem freeze() {
        return new zzca(this);
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final Map<String, DataItemAsset> getAssets() {
        HashMap map = new HashMap(this.zzbcT);
        for (int i = 0; i < this.zzbcT; i++) {
            zzbz zzbzVar = new zzbz(this.zzaCZ, this.zzaFz + i);
            if (zzbzVar.getDataItemKey() != null) {
                map.put(zzbzVar.getDataItemKey(), zzbzVar);
            }
        }
        return map;
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final byte[] getData() {
        return getByteArray(ShareConstants.WEB_DIALOG_PARAM_DATA);
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final Uri getUri() {
        return Uri.parse(getString("path"));
    }

    @Override // com.google.android.gms.wearable.DataItem
    public final DataItem setData(byte[] bArr) {
        throw new UnsupportedOperationException();
    }

    public final String toString() {
        boolean zIsLoggable = Log.isLoggable("DataItem", 3);
        byte[] data = getData();
        Map<String, DataItemAsset> assets = getAssets();
        StringBuilder sb = new StringBuilder("DataItemInternal{ ");
        String strValueOf = String.valueOf(getUri());
        sb.append(new StringBuilder(String.valueOf(strValueOf).length() + 4).append("uri=").append(strValueOf).toString());
        String strValueOf2 = String.valueOf(data == null ? "null" : Integer.valueOf(data.length));
        sb.append(new StringBuilder(String.valueOf(strValueOf2).length() + 9).append(", dataSz=").append(strValueOf2).toString());
        sb.append(new StringBuilder(23).append(", numAssets=").append(assets.size()).toString());
        if (zIsLoggable && !assets.isEmpty()) {
            sb.append(", assets=[");
            String str = "";
            Iterator<Map.Entry<String, DataItemAsset>> it = assets.entrySet().iterator();
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
