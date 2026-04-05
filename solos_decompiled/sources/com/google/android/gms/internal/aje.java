package com.google.android.gms.internal;

import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public final class aje extends ahz<aje> implements Cloneable {
    private int zzcwY = -1;
    private int zzcwZ = 0;

    public aje() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMw, reason: merged with bridge method [inline-methods] */
    public aje clone() {
        try {
            return (aje) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof aje)) {
            return false;
        }
        aje ajeVar = (aje) obj;
        if (this.zzcwY == ajeVar.zzcwY && this.zzcwZ == ajeVar.zzcwZ) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? ajeVar.zzcuW == null || ajeVar.zzcuW.isEmpty() : this.zzcuW.equals(ajeVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.zzcuW == null || this.zzcuW.isEmpty()) ? 0 : this.zzcuW.hashCode()) + ((((((getClass().getName().hashCode() + 527) * 31) + this.zzcwY) * 31) + this.zzcwZ) * 31);
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (aje) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (aje) clone();
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
                    int iZzLS = ahwVar.zzLS();
                    switch (iZzLS) {
                        case -1:
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                            this.zzcwY = iZzLS;
                            break;
                        default:
                            ahwVar.zzco(position);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
                    break;
                case 16:
                    int position2 = ahwVar.getPosition();
                    int iZzLS2 = ahwVar.zzLS();
                    switch (iZzLS2) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 100:
                            this.zzcwZ = iZzLS2;
                            break;
                        default:
                            ahwVar.zzco(position2);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
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
        if (this.zzcwY != -1) {
            ahxVar.zzr(1, this.zzcwY);
        }
        if (this.zzcwZ != 0) {
            ahxVar.zzr(2, this.zzcwZ);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzcwY != -1) {
            iZzn += ahx.zzs(1, this.zzcwY);
        }
        return this.zzcwZ != 0 ? iZzn + ahx.zzs(2, this.zzcwZ) : iZzn;
    }
}
