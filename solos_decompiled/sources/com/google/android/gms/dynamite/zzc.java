package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

/* JADX INFO: loaded from: classes67.dex */
final class zzc implements DynamiteModule.zzd {
    zzc() {
    }

    @Override // com.google.android.gms.dynamite.DynamiteModule.zzd
    public final zzi zza(Context context, String str, zzh zzhVar) throws DynamiteModule.zzc {
        zzi zziVar = new zzi();
        zziVar.zzaSY = zzhVar.zzF(context, str);
        if (zziVar.zzaSY != 0) {
            zziVar.zzaTa = -1;
        } else {
            zziVar.zzaSZ = zzhVar.zzb(context, str, true);
            if (zziVar.zzaSZ != 0) {
                zziVar.zzaTa = 1;
            }
        }
        return zziVar;
    }
}
