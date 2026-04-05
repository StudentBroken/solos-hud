package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes36.dex */
public final class zzciw {
    final Context mContext;

    public zzciw(Context context) {
        zzbr.zzu(context);
        Context applicationContext = context.getApplicationContext();
        zzbr.zzu(applicationContext);
        this.mContext = applicationContext;
    }
}
