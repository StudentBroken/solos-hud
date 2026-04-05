package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public final class aja extends ahz<aja> implements Cloneable {
    private int zzbpf = 0;
    private String zzcwz = "";
    private String version = "";

    public aja() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMr, reason: merged with bridge method [inline-methods] */
    public aja clone() {
        try {
            return (aja) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof aja)) {
            return false;
        }
        aja ajaVar = (aja) obj;
        if (this.zzbpf != ajaVar.zzbpf) {
            return false;
        }
        if (this.zzcwz == null) {
            if (ajaVar.zzcwz != null) {
                return false;
            }
        } else if (!this.zzcwz.equals(ajaVar.zzcwz)) {
            return false;
        }
        if (this.version == null) {
            if (ajaVar.version != null) {
                return false;
            }
        } else if (!this.version.equals(ajaVar.version)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? ajaVar.zzcuW == null || ajaVar.zzcuW.isEmpty() : this.zzcuW.equals(ajaVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.version == null ? 0 : this.version.hashCode()) + (((this.zzcwz == null ? 0 : this.zzcwz.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.zzbpf) * 31)) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (aja) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (aja) clone();
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 8:
                    this.zzbpf = ahwVar.zzLS();
                    break;
                case 18:
                    this.zzcwz = ahwVar.readString();
                    break;
                case 26:
                    this.version = ahwVar.readString();
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
        if (this.zzbpf != 0) {
            ahxVar.zzr(1, this.zzbpf);
        }
        if (this.zzcwz != null && !this.zzcwz.equals("")) {
            ahxVar.zzl(2, this.zzcwz);
        }
        if (this.version != null && !this.version.equals("")) {
            ahxVar.zzl(3, this.version);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzbpf != 0) {
            iZzn += ahx.zzs(1, this.zzbpf);
        }
        if (this.zzcwz != null && !this.zzcwz.equals("")) {
            iZzn += ahx.zzm(2, this.zzcwz);
        }
        return (this.version == null || this.version.equals("")) ? iZzn : iZzn + ahx.zzm(3, this.version);
    }
}
