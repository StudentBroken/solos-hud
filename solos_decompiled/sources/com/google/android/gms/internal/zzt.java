package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
public final class zzt<T> {
    public final T result;
    public final zzc zzae;
    public final zzaa zzaf;
    public boolean zzag;

    private zzt(zzaa zzaaVar) {
        this.zzag = false;
        this.result = null;
        this.zzae = null;
        this.zzaf = zzaaVar;
    }

    private zzt(T t, zzc zzcVar) {
        this.zzag = false;
        this.result = t;
        this.zzae = zzcVar;
        this.zzaf = null;
    }

    public static <T> zzt<T> zza(T t, zzc zzcVar) {
        return new zzt<>(t, zzcVar);
    }

    public static <T> zzt<T> zzc(zzaa zzaaVar) {
        return new zzt<>(zzaaVar);
    }
}
