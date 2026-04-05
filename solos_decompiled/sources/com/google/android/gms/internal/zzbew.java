package com.google.android.gms.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbew extends BroadcastReceiver {
    private Context mContext;
    private final zzbex zzaEC;

    public zzbew(zzbex zzbexVar) {
        this.zzaEC = zzbexVar;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if ("com.google.android.gms".equals(data != null ? data.getSchemeSpecificPart() : null)) {
            this.zzaEC.zzpy();
            unregister();
        }
    }

    public final void setContext(Context context) {
        this.mContext = context;
    }

    public final synchronized void unregister() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this);
        }
        this.mContext = null;
    }
}
