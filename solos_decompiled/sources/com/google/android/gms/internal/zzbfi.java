package com.google.android.gms.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbfi<L> {
    private volatile L mListener;
    private final zzbfj zzaEO;
    private final zzbfk<L> zzaEP;

    zzbfi(@NonNull Looper looper, @NonNull L l, @NonNull String str) {
        this.zzaEO = new zzbfj(this, looper);
        this.mListener = (L) zzbr.zzb(l, "Listener must not be null");
        this.zzaEP = new zzbfk<>(l, zzbr.zzcF(str));
    }

    public final void clear() {
        this.mListener = null;
    }

    public final void zza(zzbfl<? super L> zzbflVar) {
        zzbr.zzb(zzbflVar, "Notifier must not be null");
        this.zzaEO.sendMessage(this.zzaEO.obtainMessage(1, zzbflVar));
    }

    final void zzb(zzbfl<? super L> zzbflVar) {
        L l = this.mListener;
        if (l == null) {
            zzbflVar.zzpR();
            return;
        }
        try {
            zzbflVar.zzq(l);
        } catch (RuntimeException e) {
            zzbflVar.zzpR();
            throw e;
        }
    }

    public final boolean zzoa() {
        return this.mListener != null;
    }

    @NonNull
    public final zzbfk<L> zzqE() {
        return this.zzaEP;
    }
}
