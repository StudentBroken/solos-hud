package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclj extends ahz<zzclj> {
    private static volatile zzclj[] zzbvD;
    public String name = null;
    public String zzaIH = null;
    public Long zzbvE = null;
    private Float zzbuE = null;
    public Double zzbuF = null;

    public zzclj() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzclj[] zzzA() {
        if (zzbvD == null) {
            synchronized (aid.zzcve) {
                if (zzbvD == null) {
                    zzbvD = new zzclj[0];
                }
            }
        }
        return zzbvD;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclj)) {
            return false;
        }
        zzclj zzcljVar = (zzclj) obj;
        if (this.name == null) {
            if (zzcljVar.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzcljVar.name)) {
            return false;
        }
        if (this.zzaIH == null) {
            if (zzcljVar.zzaIH != null) {
                return false;
            }
        } else if (!this.zzaIH.equals(zzcljVar.zzaIH)) {
            return false;
        }
        if (this.zzbvE == null) {
            if (zzcljVar.zzbvE != null) {
                return false;
            }
        } else if (!this.zzbvE.equals(zzcljVar.zzbvE)) {
            return false;
        }
        if (this.zzbuE == null) {
            if (zzcljVar.zzbuE != null) {
                return false;
            }
        } else if (!this.zzbuE.equals(zzcljVar.zzbuE)) {
            return false;
        }
        if (this.zzbuF == null) {
            if (zzcljVar.zzbuF != null) {
                return false;
            }
        } else if (!this.zzbuF.equals(zzcljVar.zzbuF)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzcljVar.zzcuW == null || zzcljVar.zzcuW.isEmpty() : this.zzcuW.equals(zzcljVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbuF == null ? 0 : this.zzbuF.hashCode()) + (((this.zzbuE == null ? 0 : this.zzbuE.hashCode()) + (((this.zzbvE == null ? 0 : this.zzbvE.hashCode()) + (((this.zzaIH == null ? 0 : this.zzaIH.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
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
                    this.zzaIH = ahwVar.readString();
                    break;
                case 24:
                    this.zzbvE = Long.valueOf(ahwVar.zzLW());
                    break;
                case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                    this.zzbuE = Float.valueOf(Float.intBitsToFloat(ahwVar.zzLX()));
                    break;
                case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
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
        if (this.name != null) {
            ahxVar.zzl(1, this.name);
        }
        if (this.zzaIH != null) {
            ahxVar.zzl(2, this.zzaIH);
        }
        if (this.zzbvE != null) {
            ahxVar.zzb(3, this.zzbvE.longValue());
        }
        if (this.zzbuE != null) {
            ahxVar.zzc(4, this.zzbuE.floatValue());
        }
        if (this.zzbuF != null) {
            ahxVar.zza(5, this.zzbuF.doubleValue());
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.name != null) {
            iZzn += ahx.zzm(1, this.name);
        }
        if (this.zzaIH != null) {
            iZzn += ahx.zzm(2, this.zzaIH);
        }
        if (this.zzbvE != null) {
            iZzn += ahx.zze(3, this.zzbvE.longValue());
        }
        if (this.zzbuE != null) {
            this.zzbuE.floatValue();
            iZzn += ahx.zzcs(4) + 4;
        }
        if (this.zzbuF == null) {
            return iZzn;
        }
        this.zzbuF.doubleValue();
        return iZzn + ahx.zzcs(5) + 8;
    }
}
