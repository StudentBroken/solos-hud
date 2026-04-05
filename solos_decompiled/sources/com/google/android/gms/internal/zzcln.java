package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcln extends ahz<zzcln> {
    private static volatile zzcln[] zzbwk;
    public Long zzbwl = null;
    public String name = null;
    public String zzaIH = null;
    public Long zzbvE = null;
    private Float zzbuE = null;
    public Double zzbuF = null;

    public zzcln() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzcln[] zzzC() {
        if (zzbwk == null) {
            synchronized (aid.zzcve) {
                if (zzbwk == null) {
                    zzbwk = new zzcln[0];
                }
            }
        }
        return zzbwk;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcln)) {
            return false;
        }
        zzcln zzclnVar = (zzcln) obj;
        if (this.zzbwl == null) {
            if (zzclnVar.zzbwl != null) {
                return false;
            }
        } else if (!this.zzbwl.equals(zzclnVar.zzbwl)) {
            return false;
        }
        if (this.name == null) {
            if (zzclnVar.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzclnVar.name)) {
            return false;
        }
        if (this.zzaIH == null) {
            if (zzclnVar.zzaIH != null) {
                return false;
            }
        } else if (!this.zzaIH.equals(zzclnVar.zzaIH)) {
            return false;
        }
        if (this.zzbvE == null) {
            if (zzclnVar.zzbvE != null) {
                return false;
            }
        } else if (!this.zzbvE.equals(zzclnVar.zzbvE)) {
            return false;
        }
        if (this.zzbuE == null) {
            if (zzclnVar.zzbuE != null) {
                return false;
            }
        } else if (!this.zzbuE.equals(zzclnVar.zzbuE)) {
            return false;
        }
        if (this.zzbuF == null) {
            if (zzclnVar.zzbuF != null) {
                return false;
            }
        } else if (!this.zzbuF.equals(zzclnVar.zzbuF)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclnVar.zzcuW == null || zzclnVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclnVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbuF == null ? 0 : this.zzbuF.hashCode()) + (((this.zzbuE == null ? 0 : this.zzbuE.hashCode()) + (((this.zzbvE == null ? 0 : this.zzbvE.hashCode()) + (((this.zzaIH == null ? 0 : this.zzaIH.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + (((this.zzbwl == null ? 0 : this.zzbwl.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
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
                    this.zzbwl = Long.valueOf(ahwVar.zzLW());
                    break;
                case 18:
                    this.name = ahwVar.readString();
                    break;
                case 26:
                    this.zzaIH = ahwVar.readString();
                    break;
                case 32:
                    this.zzbvE = Long.valueOf(ahwVar.zzLW());
                    break;
                case MotionEventCompat.AXIS_GENERIC_14 /* 45 */:
                    this.zzbuE = Float.valueOf(Float.intBitsToFloat(ahwVar.zzLX()));
                    break;
                case 49:
                    this.zzbuF = Double.valueOf(Double.longBitsToDouble(ahwVar.zzLY()));
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
        if (this.zzbwl != null) {
            ahxVar.zzb(1, this.zzbwl.longValue());
        }
        if (this.name != null) {
            ahxVar.zzl(2, this.name);
        }
        if (this.zzaIH != null) {
            ahxVar.zzl(3, this.zzaIH);
        }
        if (this.zzbvE != null) {
            ahxVar.zzb(4, this.zzbvE.longValue());
        }
        if (this.zzbuE != null) {
            ahxVar.zzc(5, this.zzbuE.floatValue());
        }
        if (this.zzbuF != null) {
            ahxVar.zza(6, this.zzbuF.doubleValue());
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbwl != null) {
            iZzn += ahx.zze(1, this.zzbwl.longValue());
        }
        if (this.name != null) {
            iZzn += ahx.zzm(2, this.name);
        }
        if (this.zzaIH != null) {
            iZzn += ahx.zzm(3, this.zzaIH);
        }
        if (this.zzbvE != null) {
            iZzn += ahx.zze(4, this.zzbvE.longValue());
        }
        if (this.zzbuE != null) {
            this.zzbuE.floatValue();
            iZzn += ahx.zzcs(5) + 4;
        }
        if (this.zzbuF == null) {
            return iZzn;
        }
        this.zzbuF.doubleValue();
        return iZzn + ahx.zzcs(6) + 8;
    }
}
