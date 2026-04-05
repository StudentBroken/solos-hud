package com.google.android.gms.dynamic;

import android.os.Bundle;
import java.util.Iterator;

/* JADX INFO: Add missing generic type declarations: [T] */
/* JADX INFO: loaded from: classes3.dex */
final class zzb<T> implements zzo<T> {
    private /* synthetic */ zza zzaSz;

    zzb(zza zzaVar) {
        this.zzaSz = zzaVar;
    }

    /* JADX WARN: Incorrect types in method signature: (TT;)V */
    @Override // com.google.android.gms.dynamic.zzo
    public final void zza(LifecycleDelegate lifecycleDelegate) {
        this.zzaSz.zzaSv = lifecycleDelegate;
        Iterator it = this.zzaSz.zzaSx.iterator();
        while (it.hasNext()) {
            ((zzi) it.next()).zzb(this.zzaSz.zzaSv);
        }
        this.zzaSz.zzaSx.clear();
        zza.zza(this.zzaSz, (Bundle) null);
    }
}
