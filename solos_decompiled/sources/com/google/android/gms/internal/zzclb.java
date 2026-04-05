package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclb extends ahz<zzclb> {
    public Integer zzbva = null;
    public Boolean zzbvb = null;
    public String zzbvc = null;
    public String zzbvd = null;
    public String zzbve = null;

    public zzclb() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclb)) {
            return false;
        }
        zzclb zzclbVar = (zzclb) obj;
        if (this.zzbva == null) {
            if (zzclbVar.zzbva != null) {
                return false;
            }
        } else if (!this.zzbva.equals(zzclbVar.zzbva)) {
            return false;
        }
        if (this.zzbvb == null) {
            if (zzclbVar.zzbvb != null) {
                return false;
            }
        } else if (!this.zzbvb.equals(zzclbVar.zzbvb)) {
            return false;
        }
        if (this.zzbvc == null) {
            if (zzclbVar.zzbvc != null) {
                return false;
            }
        } else if (!this.zzbvc.equals(zzclbVar.zzbvc)) {
            return false;
        }
        if (this.zzbvd == null) {
            if (zzclbVar.zzbvd != null) {
                return false;
            }
        } else if (!this.zzbvd.equals(zzclbVar.zzbvd)) {
            return false;
        }
        if (this.zzbve == null) {
            if (zzclbVar.zzbve != null) {
                return false;
            }
        } else if (!this.zzbve.equals(zzclbVar.zzbve)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclbVar.zzcuW == null || zzclbVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclbVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbve == null ? 0 : this.zzbve.hashCode()) + (((this.zzbvd == null ? 0 : this.zzbvd.hashCode()) + (((this.zzbvc == null ? 0 : this.zzbvc.hashCode()) + (((this.zzbvb == null ? 0 : this.zzbvb.hashCode()) + (((this.zzbva == null ? 0 : this.zzbva.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
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
                    int position = ahwVar.getPosition();
                    int iZzLV = ahwVar.zzLV();
                    switch (iZzLV) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            this.zzbva = Integer.valueOf(iZzLV);
                            break;
                        default:
                            ahwVar.zzco(position);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
                    break;
                case 16:
                    this.zzbvb = Boolean.valueOf(ahwVar.zzLT());
                    break;
                case 26:
                    this.zzbvc = ahwVar.readString();
                    break;
                case 34:
                    this.zzbvd = ahwVar.readString();
                    break;
                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                    this.zzbve = ahwVar.readString();
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
        if (this.zzbva != null) {
            ahxVar.zzr(1, this.zzbva.intValue());
        }
        if (this.zzbvb != null) {
            ahxVar.zzk(2, this.zzbvb.booleanValue());
        }
        if (this.zzbvc != null) {
            ahxVar.zzl(3, this.zzbvc);
        }
        if (this.zzbvd != null) {
            ahxVar.zzl(4, this.zzbvd);
        }
        if (this.zzbve != null) {
            ahxVar.zzl(5, this.zzbve);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbva != null) {
            iZzn += ahx.zzs(1, this.zzbva.intValue());
        }
        if (this.zzbvb != null) {
            this.zzbvb.booleanValue();
            iZzn += ahx.zzcs(2) + 1;
        }
        if (this.zzbvc != null) {
            iZzn += ahx.zzm(3, this.zzbvc);
        }
        if (this.zzbvd != null) {
            iZzn += ahx.zzm(4, this.zzbvd);
        }
        return this.zzbve != null ? iZzn + ahx.zzm(5, this.zzbve) : iZzn;
    }
}
