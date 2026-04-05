package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public final class ajd extends ahz<ajd> implements Cloneable {
    private static volatile ajd[] zzcwX;
    private String key = "";
    private String value = "";

    public ajd() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public static ajd[] zzMu() {
        if (zzcwX == null) {
            synchronized (aid.zzcve) {
                if (zzcwX == null) {
                    zzcwX = new ajd[0];
                }
            }
        }
        return zzcwX;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMv, reason: merged with bridge method [inline-methods] */
    public ajd clone() {
        try {
            return (ajd) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ajd)) {
            return false;
        }
        ajd ajdVar = (ajd) obj;
        if (this.key == null) {
            if (ajdVar.key != null) {
                return false;
            }
        } else if (!this.key.equals(ajdVar.key)) {
            return false;
        }
        if (this.value == null) {
            if (ajdVar.value != null) {
                return false;
            }
        } else if (!this.value.equals(ajdVar.value)) {
            return false;
        }
        return (this.zzcuW == null || this.zzcuW.isEmpty()) ? ajdVar.zzcuW == null || ajdVar.zzcuW.isEmpty() : this.zzcuW.equals(ajdVar.zzcuW);
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.value == null ? 0 : this.value.hashCode()) + (((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (ajd) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (ajd) clone();
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
        if (this.key != null && !this.key.equals("")) {
            ahxVar.zzl(1, this.key);
        }
        if (this.value != null && !this.value.equals("")) {
            ahxVar.zzl(2, this.value);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.key != null && !this.key.equals("")) {
            iZzn += ahx.zzm(1, this.key);
        }
        return (this.value == null || this.value.equals("")) ? iZzn : iZzn + ahx.zzm(2, this.value);
    }
}
