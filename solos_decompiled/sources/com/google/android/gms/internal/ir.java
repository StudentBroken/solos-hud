package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes6.dex */
public final class ir extends ahz<ir> {
    private static volatile ir[] zzbTK;
    public String name = "";
    public is zzbTL = null;

    public ir() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static ir[] zzDX() {
        if (zzbTK == null) {
            synchronized (aid.zzcve) {
                if (zzbTK == null) {
                    zzbTK = new ir[0];
                }
            }
        }
        return zzbTK;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ir)) {
            return false;
        }
        ir irVar = (ir) obj;
        if (this.name == null) {
            if (irVar.name != null) {
                return false;
            }
        } else if (!this.name.equals(irVar.name)) {
            return false;
        }
        if (this.zzbTL == null) {
            if (irVar.zzbTL != null) {
                return false;
            }
        } else if (!this.zzbTL.equals(irVar.zzbTL)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? irVar.zzcuW == null || irVar.zzcuW.isEmpty() : this.zzcuW.equals(irVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbTL == null ? 0 : this.zzbTL.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    this.name = ahwVar.readString();
                    break;
                case 18:
                    if (this.zzbTL == null) {
                        this.zzbTL = new is();
                    }
                    ahwVar.zzb(this.zzbTL);
                    break;
                default:
                    if (!super.zza(ahwVar, iZzLQ)) {
                    }
                    break;
            }
        }
        return this;
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    public final void zza(ahx ahxVar) throws IOException {
        ahxVar.zzl(1, this.name);
        if (this.zzbTL != null) {
            ahxVar.zza(2, this.zzbTL);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn() + ahx.zzm(1, this.name);
        return this.zzbTL != null ? iZzn + ahx.zzb(2, this.zzbTL) : iZzn;
    }
}
