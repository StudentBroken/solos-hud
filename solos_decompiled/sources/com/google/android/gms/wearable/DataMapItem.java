package com.google.android.gms.wearable;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.internal.aie;
import com.google.android.gms.internal.io;
import com.google.android.gms.internal.ip;
import com.google.android.gms.internal.iq;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes6.dex */
public class DataMapItem {
    private final Uri mUri;
    private final DataMap zzbRh;

    private DataMapItem(DataItem dataItem) {
        this.mUri = dataItem.getUri();
        this.zzbRh = zza(dataItem.freeze());
    }

    public static DataMapItem fromDataItem(DataItem dataItem) {
        if (dataItem == null) {
            throw new IllegalStateException("provided dataItem is null");
        }
        return new DataMapItem(dataItem);
    }

    private static DataMap zza(DataItem dataItem) {
        if (dataItem.getData() == null && dataItem.getAssets().size() > 0) {
            throw new IllegalArgumentException("Cannot create DataMapItem from a DataItem  that wasn't made with DataMapItem.");
        }
        if (dataItem.getData() == null) {
            return new DataMap();
        }
        try {
            ArrayList arrayList = new ArrayList();
            int size = dataItem.getAssets().size();
            for (int i = 0; i < size; i++) {
                DataItemAsset dataItemAsset = dataItem.getAssets().get(Integer.toString(i));
                if (dataItemAsset == null) {
                    String strValueOf = String.valueOf(dataItem);
                    throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 64).append("Cannot find DataItemAsset referenced in data at ").append(i).append(" for ").append(strValueOf).toString());
                }
                arrayList.add(Asset.createFromRef(dataItemAsset.getId()));
            }
            return io.zza(new ip(iq.zzz(dataItem.getData()), arrayList));
        } catch (aie | NullPointerException e) {
            String strValueOf2 = String.valueOf(dataItem.getUri());
            String strValueOf3 = String.valueOf(Base64.encodeToString(dataItem.getData(), 0));
            Log.w("DataItem", new StringBuilder(String.valueOf(strValueOf2).length() + 50 + String.valueOf(strValueOf3).length()).append("Unable to parse datamap from dataItem. uri=").append(strValueOf2).append(", data=").append(strValueOf3).toString());
            String strValueOf4 = String.valueOf(dataItem.getUri());
            throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf4).length() + 44).append("Unable to parse datamap from dataItem.  uri=").append(strValueOf4).toString(), e);
        }
    }

    public DataMap getDataMap() {
        return this.zzbRh;
    }

    public Uri getUri() {
        return this.mUri;
    }
}
