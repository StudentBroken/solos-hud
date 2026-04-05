package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

/* JADX INFO: loaded from: classes67.dex */
final class zzb implements DynamiteModule.zzd {
    zzb() {
    }

    @Override // com.google.android.gms.dynamite.DynamiteModule.zzd
    public final zzi zza(Context context, String str, zzh zzhVar) throws DynamiteModule.zzc {
        zzi zziVar = new zzi();
        zziVar.zzaSZ = zzhVar.zzb(context, str, true);
        if (zziVar.zzaSZ != 0) {
            zziVar.zzaTa = 1;
        } else {
            zziVar.zzaSY = zzhVar.zzF(context, str);
            if (zziVar.zzaSY != 0) {
                zziVar.zzaTa = -1;
            }
        }
        return zziVar;
    }
}
