package com.google.android.gms.auth.api.signin.internal;

/* JADX INFO: loaded from: classes3.dex */
public final class zzo {
    private static int zzamu = 31;
    private int zzamv = 1;

    public final zzo zzP(boolean z) {
        this.zzamv = (z ? 1 : 0) + (this.zzamv * zzamu);
        return this;
    }

    public final int zzmH() {
        return this.zzamv;
    }

    public final zzo zzo(Object obj) {
        this.zzamv = (obj == null ? 0 : obj.hashCode()) + (this.zzamv * zzamu);
        return this;
    }
}
