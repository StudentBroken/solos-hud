package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.flags.ModuleDescriptor;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcbn {
    private boolean zzuK = false;
    private zzcbo zzaXK = null;

    public final void initialize(Context context) {
        synchronized (this) {
            if (this.zzuK) {
                return;
            }
            try {
                this.zzaXK = zzcbp.asInterface(DynamiteModule.zza(context, DynamiteModule.zzaST, ModuleDescriptor.MODULE_ID).zzcW("com.google.android.gms.flags.impl.FlagProviderImpl"));
                this.zzaXK.init(com.google.android.gms.dynamic.zzn.zzw(context));
                this.zzuK = true;
            } catch (RemoteException | DynamiteModule.zzc e) {
                Log.w("FlagValueProvider", "Failed to initialize flags module.", e);
            }
        }
    }

    public final <T> T zzb(zzcbg<T> zzcbgVar) {
        synchronized (this) {
            if (this.zzuK) {
                return zzcbgVar.zza(this.zzaXK);
            }
            return zzcbgVar.zzdH();
        }
    }
}
