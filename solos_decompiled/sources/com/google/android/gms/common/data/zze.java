package com.google.android.gms.common.data;

import android.content.ContentValues;
import com.google.android.gms.common.data.DataHolder;
import java.util.HashMap;

/* JADX INFO: loaded from: classes3.dex */
final class zze extends DataHolder.zza {
    zze(String[] strArr, String str) {
        super(strArr, null, null);
    }

    @Override // com.google.android.gms.common.data.DataHolder.zza
    public final DataHolder.zza zza(ContentValues contentValues) {
        throw new UnsupportedOperationException("Cannot add data to empty builder");
    }

    @Override // com.google.android.gms.common.data.DataHolder.zza
    public final DataHolder.zza zza(HashMap<String, Object> map) {
        throw new UnsupportedOperationException("Cannot add data to empty builder");
    }
}
