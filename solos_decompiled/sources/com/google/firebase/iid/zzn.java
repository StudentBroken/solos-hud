package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* JADX INFO: loaded from: classes35.dex */
final class zzn extends BroadcastReceiver {
    private /* synthetic */ zzl zzcnD;

    zzn(zzl zzlVar) {
        this.zzcnD = zzlVar;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (Log.isLoggable("InstanceID/Rpc", 3)) {
            String strValueOf = String.valueOf(intent.getExtras());
            Log.d("InstanceID/Rpc", new StringBuilder(String.valueOf(strValueOf).length() + 44).append("Received GSF callback via dynamic receiver: ").append(strValueOf).toString());
        }
        this.zzcnD.zzi(intent);
    }
}
