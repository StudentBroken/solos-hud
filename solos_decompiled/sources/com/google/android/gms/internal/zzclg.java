package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes36.dex */
public final class zzclg extends ahz<zzclg> {
    private static volatile zzclg[] zzbvu;
    public String key = null;
    public String value = null;

    public zzclg() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static zzclg[] zzzx() {
        if (zzbvu == null) {
            synchronized (aid.zzcve) {
                if (zzbvu == null) {
                    zzbvu = new zzclg[0];
                }
            }
        }
        return zzbvu;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclg)) {
            return false;
        }
        zzclg zzclgVar = (zzclg) obj;
        if (this.key == null) {
            if (zzclgVar.key != null) {
                return false;
            }
        } else if (!this.key.equals(zzclgVar.key)) {
            return false;
        }
        if (this.value == null) {
            if (zzclgVar.value != null) {
                return false;
            }
        } else if (!this.value.equals(zzclgVar.value)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? zzclgVar.zzcuW == null || zzclgVar.zzcuW.isEmpty() : this.zzcuW.equals(zzclgVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.value == null ? 0 : this.value.hashCode()) + (((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
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
                    this.key = ahwVar.readString();
                    break;
                case 18:
                    this.value = ahwVar.readString();
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
        if (this.key != null) {
            ahxVar.zzl(1, this.key);
        }
        if (this.value != null) {
            ahxVar.zzl(2, this.value);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.key != null) {
            iZzn += ahx.zzm(1, this.key);
        }
        return this.value != null ? iZzn + ahx.zzm(2, this.value) : iZzn;
    }
}
