package com.google.android.gms.internal;

import com.google.android.gms.internal.ahz;
import java.io.IOException;

/* JADX INFO: loaded from: classes67.dex */
public abstract class ahz<M extends ahz<M>> extends aif {
    protected aib zzcuW;

    @Override // com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMd, reason: merged with bridge method [inline-methods] */
    public M clone() throws CloneNotSupportedException {
        M m = (M) super.clone();
        aid.zza(this, m);
        return m;
    }

    @Override // com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (ahz) clone();
    }

    public final <T> T zza(aia<M, T> aiaVar) {
        aic aicVarZzcw;
        if (this.zzcuW == null || (aicVarZzcw = this.zzcuW.zzcw(aiaVar.tag >>> 3)) == null) {
            return null;
        }
        return (T) aicVarZzcw.zzb(aiaVar);
    }

    @Override // com.google.android.gms.internal.aif
    public void zza(ahx ahxVar) throws IOException {
        if (this.zzcuW == null) {
            return;
        }
        for (int i = 0; i < this.zzcuW.size(); i++) {
            this.zzcuW.zzcx(i).zza(ahxVar);
        }
    }

    protected final boolean zza(ahw ahwVar, int i) throws IOException {
        int position = ahwVar.getPosition();
        if (!ahwVar.zzcl(i)) {
            return false;
        }
        int i2 = i >>> 3;
        aii aiiVar = new aii(i, ahwVar.zzp(position, ahwVar.getPosition() - position));
        aic aicVarZzcw = null;
        if (this.zzcuW == null) {
            this.zzcuW = new aib();
        } else {
            aicVarZzcw = this.zzcuW.zzcw(i2);
        }
        if (aicVarZzcw == null) {
            aicVarZzcw = new aic();
            this.zzcuW.zza(i2, aicVarZzcw);
        }
        aicVarZzcw.zza(aiiVar);
        return true;
    }

    @Override // com.google.android.gms.internal.aif
    protected int zzn() {
        if (this.zzcuW == null) {
            return 0;
        }
        int iZzn = 0;
        for (int i = 0; i < this.zzcuW.size(); i++) {
            iZzn += this.zzcuW.zzcx(i).zzn();
        }
        return iZzn;
    }
}
