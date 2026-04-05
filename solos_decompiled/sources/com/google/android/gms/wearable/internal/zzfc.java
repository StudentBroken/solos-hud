package com.google.android.gms.wearable.internal;

import com.google.android.gms.internal.zzbcl;

/* JADX INFO: loaded from: classes6.dex */
class zzfc<T> extends zza {
    private zzbcl<T> zzajN;

    public zzfc(zzbcl<T> zzbclVar) {
        this.zzajN = zzbclVar;
    }

    public final void zzR(T t) {
        zzbcl<T> zzbclVar = this.zzajN;
        if (zzbclVar != null) {
            zzbclVar.setResult(t);
            this.zzajN = null;
        }
    }
}
