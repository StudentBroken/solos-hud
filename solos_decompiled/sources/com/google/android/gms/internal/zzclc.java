package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclc extends ahz<zzclc> {
    private static volatile zzclc[] zzbvf;
    public Integer zzbuQ = null;
    public String zzbvg = null;
    public zzcla zzbvh = null;

    public zzclc() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzclc[] zzzv() {
        if (zzbvf == null) {
            synchronized (aid.zzcve) {
                if (zzbvf == null) {
                    zzbvf = new zzclc[0];
                }
            }
        }
        return zzbvf;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclc)) {
            return false;
        }
        zzclc zzclcVar = (zzclc) obj;
        if (this.zzbuQ == null) {
            if (zzclcVar.zzbuQ != null) {
                return false;
            }
        } else if (!this.zzbuQ.equals(zzclcVar.zzbuQ)) {
            return false;
        }
        if (this.zzbvg == null) {
            if (zzclcVar.zzbvg != null) {
                return false;
            }
        } else if (!this.zzbvg.equals(zzclcVar.zzbvg)) {
            return false;
        }
        if (this.zzbvh == null) {
            if (zzclcVar.zzbvh != null) {
                return false;
            }
        } else if (!this.zzbvh.equals(zzclcVar.zzbvh)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclcVar.zzcuW == null || zzclcVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclcVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbvh == null ? 0 : this.zzbvh.hashCode()) + (((this.zzbvg == null ? 0 : this.zzbvg.hashCode()) + (((this.zzbuQ == null ? 0 : this.zzbuQ.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31;
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
                case 8:
                    this.zzbuQ = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 18:
                    this.zzbvg = ahwVar.readString();
                    break;
                case 26:
                    if (this.zzbvh == null) {
                        this.zzbvh = new zzcla();
                    }
                    ahwVar.zzb(this.zzbvh);
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
        if (this.zzbuQ != null) {
            ahxVar.zzr(1, this.zzbuQ.intValue());
        }
        if (this.zzbvg != null) {
            ahxVar.zzl(2, this.zzbvg);
        }
        if (this.zzbvh != null) {
            ahxVar.zza(3, this.zzbvh);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbuQ != null) {
            iZzn += ahx.zzs(1, this.zzbuQ.intValue());
        }
        if (this.zzbvg != null) {
            iZzn += ahx.zzm(2, this.zzbvg);
        }
        return this.zzbvh != null ? iZzn + ahx.zzb(3, this.zzbvh) : iZzn;
    }
}
