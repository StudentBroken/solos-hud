package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcle extends ahz<zzcle> {
    private static volatile zzcle[] zzbvm;
    public String name = null;
    public Boolean zzbvn = null;
    public Boolean zzbvo = null;

    public zzcle() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcle[] zzzw() {
        if (zzbvm == null) {
            synchronized (aid.zzcve) {
                if (zzbvm == null) {
                    zzbvm = new zzcle[0];
                }
            }
        }
        return zzbvm;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcle)) {
            return false;
        }
        zzcle zzcleVar = (zzcle) obj;
        if (this.name == null) {
            if (zzcleVar.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzcleVar.name)) {
            return false;
        }
        if (this.zzbvn == null) {
            if (zzcleVar.zzbvn != null) {
                return false;
            }
        } else if (!this.zzbvn.equals(zzcleVar.zzbvn)) {
            return false;
        }
        if (this.zzbvo == null) {
            if (zzcleVar.zzbvo != null) {
                return false;
            }
        } else if (!this.zzbvo.equals(zzcleVar.zzbvo)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzcleVar.zzcuW == null || zzcleVar.zzcuW.isEmpty() : this.zzcuW.equals(zzcleVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbvo == null ? 0 : this.zzbvo.hashCode()) + (((this.zzbvn == null ? 0 : this.zzbvn.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31;
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
                case 16:
                    this.zzbvn = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 24:
                    this.zzbvo = Boolean.valueOf(ahwVar.zzLT());
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
        if (this.name != null) {
            ahxVar.zzl(1, this.name);
        }
        if (this.zzbvn != null) {
            ahxVar.zzk(2, this.zzbvn.booleanValue());
        }
        if (this.zzbvo != null) {
            ahxVar.zzk(3, this.zzbvo.booleanValue());
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.name != null) {
            iZzn += ahx.zzm(1, this.name);
        }
        if (this.zzbvn != null) {
            this.zzbvn.booleanValue();
            iZzn += ahx.zzcs(2) + 1;
        }
        if (this.zzbvo == null) {
            return iZzn;
        }
        this.zzbvo.booleanValue();
        return iZzn + ahx.zzcs(3) + 1;
    }
}
