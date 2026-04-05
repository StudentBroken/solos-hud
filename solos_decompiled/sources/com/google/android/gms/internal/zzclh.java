package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclh extends ahz<zzclh> {
    private static volatile zzclh[] zzbvv;
    public Integer zzbuM = null;
    public zzclm zzbvw = null;
    public zzclm zzbvx = null;
    public Boolean zzbvy = null;

    public zzclh() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzclh[] zzzy() {
        if (zzbvv == null) {
            synchronized (aid.zzcve) {
                if (zzbvv == null) {
                    zzbvv = new zzclh[0];
                }
            }
        }
        return zzbvv;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclh)) {
            return false;
        }
        zzclh zzclhVar = (zzclh) obj;
        if (this.zzbuM == null) {
            if (zzclhVar.zzbuM != null) {
                return false;
            }
        } else if (!this.zzbuM.equals(zzclhVar.zzbuM)) {
            return false;
        }
        if (this.zzbvw == null) {
            if (zzclhVar.zzbvw != null) {
                return false;
            }
        } else if (!this.zzbvw.equals(zzclhVar.zzbvw)) {
            return false;
        }
        if (this.zzbvx == null) {
            if (zzclhVar.zzbvx != null) {
                return false;
            }
        } else if (!this.zzbvx.equals(zzclhVar.zzbvx)) {
            return false;
        }
        if (this.zzbvy == null) {
            if (zzclhVar.zzbvy != null) {
                return false;
            }
        } else if (!this.zzbvy.equals(zzclhVar.zzbvy)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclhVar.zzcuW == null || zzclhVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclhVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzbvy == null ? 0 : this.zzbvy.hashCode()) + (((this.zzbvx == null ? 0 : this.zzbvx.hashCode()) + (((this.zzbvw == null ? 0 : this.zzbvw.hashCode()) + (((this.zzbuM == null ? 0 : this.zzbuM.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31;
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
                    this.zzbuM = Integer.valueOf(ahwVar.zzLV());
                    break;
                case 18:
                    if (this.zzbvw == null) {
                        this.zzbvw = new zzclm();
                    }
                    ahwVar.zzb(this.zzbvw);
                    break;
                case 26:
                    if (this.zzbvx == null) {
                        this.zzbvx = new zzclm();
                    }
                    ahwVar.zzb(this.zzbvx);
                    break;
                case 32:
                    this.zzbvy = Boolean.valueOf(ahwVar.zzLT());
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
        if (this.zzbuM != null) {
            ahxVar.zzr(1, this.zzbuM.intValue());
        }
        if (this.zzbvw != null) {
            ahxVar.zza(2, this.zzbvw);
        }
        if (this.zzbvx != null) {
            ahxVar.zza(3, this.zzbvx);
        }
        if (this.zzbvy != null) {
            ahxVar.zzk(4, this.zzbvy.booleanValue());
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbuM != null) {
            iZzn += ahx.zzs(1, this.zzbuM.intValue());
        }
        if (this.zzbvw != null) {
            iZzn += ahx.zzb(2, this.zzbvw);
        }
        if (this.zzbvx != null) {
            iZzn += ahx.zzb(3, this.zzbvx);
        }
        if (this.zzbvy == null) {
            return iZzn;
        }
        this.zzbvy.booleanValue();
        return iZzn + ahx.zzcs(4) + 1;
    }
}
