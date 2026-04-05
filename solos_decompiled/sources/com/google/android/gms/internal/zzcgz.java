package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgz {
    private final int mPriority;
    private /* synthetic */ zzcgx zzbra;
    private final boolean zzbrb;
    private final boolean zzbrc;

    zzcgz(zzcgx zzcgxVar, int i, boolean z, boolean z2) {
        this.zzbra = zzcgxVar;
        this.mPriority = i;
        this.zzbrb = z;
        this.zzbrc = z2;
    }

    public final void log(String str) {
        this.zzbra.zza(this.mPriority, this.zzbrb, this.zzbrc, str, null, null, null);
    }

    public final void zzd(String str, Object obj, Object obj2, Object obj3) {
        this.zzbra.zza(this.mPriority, this.zzbrb, this.zzbrc, str, obj, obj2, obj3);
    }

    public final void zze(String str, Object obj, Object obj2) {
        this.zzbra.zza(this.mPriority, this.zzbrb, this.zzbrc, str, obj, obj2, null);
    }

    public final void zzj(String str, Object obj) {
        this.zzbra.zza(this.mPriority, this.zzbrb, this.zzbrc, str, obj, null, null);
    }
}
