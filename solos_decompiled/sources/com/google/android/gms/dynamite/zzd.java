package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;

/* JADX INFO: loaded from: classes67.dex */
final class zzd implements DynamiteModule.zzd {
    zzd() {
    }

    @Override // com.google.android.gms.dynamite.DynamiteModule.zzd
    public final zzi zza(Context context, String str, zzh zzhVar) throws DynamiteModule.zzc {
        zzi zziVar = new zzi();
        zziVar.zzaSY = zzhVar.zzF(context, str);
        zziVar.zzaSZ = zzhVar.zzb(context, str, true);
        if (zziVar.zzaSY == 0 && zziVar.zzaSZ == 0) {
            zziVar.zzaTa = 0;
        } else if (zziVar.zzaSY >= zziVar.zzaSZ) {
            zziVar.zzaTa = -1;
        } else {
            zziVar.zzaTa = 1;
        }
        return zziVar;
    }
}
