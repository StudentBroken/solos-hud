package com.google.android.gms.internal;

import com.nuance.android.vocalizer.VocalizerEngine;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes3.dex */
public final class ajc extends ahz<ajc> implements Cloneable {
    public long zzcwE = 0;
    public long zzcwF = 0;
    private long zzcwG = 0;
    private String tag = "";
    public int zzcwH = 0;
    public int zzrE = 0;
    private boolean zzcfX = false;
    private ajd[] zzcwI = ajd.zzMu();
    private byte[] zzcwJ = aij.zzcvs;
    private aja zzcwK = null;
    public byte[] zzcwL = aij.zzcvs;
    private String zzcwM = "";
    private String zzcwN = "";
    private aiz zzcwO = null;
    private String zzcwP = "";
    public long zzcwQ = 180000;
    private ajb zzcwR = null;
    public byte[] zzcwS = aij.zzcvs;
    private String zzcwT = "";
    private int zzcwU = 0;
    private int[] zzcwV = aij.zzcvm;
    private long zzcwW = 0;
    private aje zzcpG = null;

    public ajc() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMt, reason: merged with bridge method [inline-methods] */
    public final ajc clone() {
        try {
            ajc ajcVar = (ajc) super.clone();
            if (this.zzcwI != null && this.zzcwI.length > 0) {
                ajcVar.zzcwI = new ajd[this.zzcwI.length];
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= this.zzcwI.length) {
                        break;
                    }
                    if (this.zzcwI[i2] != null) {
                        ajcVar.zzcwI[i2] = (ajd) this.zzcwI[i2].clone();
                    }
                    i = i2 + 1;
                }
            }
            if (this.zzcwK != null) {
                ajcVar.zzcwK = (aja) this.zzcwK.clone();
            }
            if (this.zzcwO != null) {
                ajcVar.zzcwO = (aiz) this.zzcwO.clone();
            }
            if (this.zzcwR != null) {
                ajcVar.zzcwR = (ajb) this.zzcwR.clone();
            }
            if (this.zzcwV != null && this.zzcwV.length > 0) {
                ajcVar.zzcwV = (int[]) this.zzcwV.clone();
            }
            if (this.zzcpG != null) {
                ajcVar.zzcpG = (aje) this.zzcpG.clone();
            }
            return ajcVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ajc)) {
            return false;
        }
        ajc ajcVar = (ajc) obj;
        if (this.zzcwE == ajcVar.zzcwE && this.zzcwF == ajcVar.zzcwF && this.zzcwG == ajcVar.zzcwG) {
            if (this.tag == null) {
                if (ajcVar.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(ajcVar.tag)) {
                return false;
            }
            if (this.zzcwH == ajcVar.zzcwH && this.zzrE == ajcVar.zzrE && this.zzcfX == ajcVar.zzcfX && aid.equals(this.zzcwI, ajcVar.zzcwI) && Arrays.equals(this.zzcwJ, ajcVar.zzcwJ)) {
                if (this.zzcwK == null) {
                    if (ajcVar.zzcwK != null) {
                        return false;
                    }
                } else if (!this.zzcwK.equals(ajcVar.zzcwK)) {
                    return false;
                }
                if (!Arrays.equals(this.zzcwL, ajcVar.zzcwL)) {
                    return false;
                }
                if (this.zzcwM == null) {
                    if (ajcVar.zzcwM != null) {
                        return false;
                    }
                } else if (!this.zzcwM.equals(ajcVar.zzcwM)) {
                    return false;
                }
                if (this.zzcwN == null) {
                    if (ajcVar.zzcwN != null) {
                        return false;
                    }
                } else if (!this.zzcwN.equals(ajcVar.zzcwN)) {
                    return false;
                }
                if (this.zzcwO == null) {
                    if (ajcVar.zzcwO != null) {
                        return false;
                    }
                } else if (!this.zzcwO.equals(ajcVar.zzcwO)) {
                    return false;
                }
                if (this.zzcwP == null) {
                    if (ajcVar.zzcwP != null) {
                        return false;
                    }
                } else if (!this.zzcwP.equals(ajcVar.zzcwP)) {
                    return false;
                }
                if (this.zzcwQ != ajcVar.zzcwQ) {
                    return false;
                }
                if (this.zzcwR == null) {
                    if (ajcVar.zzcwR != null) {
                        return false;
                    }
                } else if (!this.zzcwR.equals(ajcVar.zzcwR)) {
                    return false;
                }
                if (!Arrays.equals(this.zzcwS, ajcVar.zzcwS)) {
                    return false;
                }
                if (this.zzcwT == null) {
                    if (ajcVar.zzcwT != null) {
                        return false;
                    }
                } else if (!this.zzcwT.equals(ajcVar.zzcwT)) {
                    return false;
                }
                if (this.zzcwU == ajcVar.zzcwU && aid.equals(this.zzcwV, ajcVar.zzcwV) && this.zzcwW == ajcVar.zzcwW) {
                    if (this.zzcpG == null) {
                        if (ajcVar.zzcpG != null) {
                            return false;
                        }
                    } else if (!this.zzcpG.equals(ajcVar.zzcpG)) {
                        return false;
                    }
                    return (this.zzcuW == null || this.zzcuW.isEmpty()) ? ajcVar.zzcuW == null || ajcVar.zzcuW.isEmpty() : this.zzcuW.equals(ajcVar.zzcuW);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = ((this.zzcpG == null ? 0 : this.zzcpG.hashCode()) + (((((((((this.zzcwT == null ? 0 : this.zzcwT.hashCode()) + (((((this.zzcwR == null ? 0 : this.zzcwR.hashCode()) + (((((this.zzcwP == null ? 0 : this.zzcwP.hashCode()) + (((this.zzcwO == null ? 0 : this.zzcwO.hashCode()) + (((this.zzcwN == null ? 0 : this.zzcwN.hashCode()) + (((this.zzcwM == null ? 0 : this.zzcwM.hashCode()) + (((((this.zzcwK == null ? 0 : this.zzcwK.hashCode()) + (((((((this.zzcfX ? 1231 : 1237) + (((((((this.tag == null ? 0 : this.tag.hashCode()) + ((((((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzcwE ^ (this.zzcwE >>> 32)))) * 31) + ((int) (this.zzcwF ^ (this.zzcwF >>> 32)))) * 31) + ((int) (this.zzcwG ^ (this.zzcwG >>> 32)))) * 31)) * 31) + this.zzcwH) * 31) + this.zzrE) * 31)) * 31) + aid.hashCode(this.zzcwI)) * 31) + Arrays.hashCode(this.zzcwJ)) * 31)) * 31) + Arrays.hashCode(this.zzcwL)) * 31)) * 31)) * 31)) * 31)) * 31) + ((int) (this.zzcwQ ^ (this.zzcwQ >>> 32)))) * 31)) * 31) + Arrays.hashCode(this.zzcwS)) * 31)) * 31) + this.zzcwU) * 31) + aid.hashCode(this.zzcwV)) * 31) + ((int) (this.zzcwW ^ (this.zzcwW >>> 32)))) * 31)) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iHashCode2 + iHashCode;
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (ajc) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (ajc) clone();
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 8:
                    this.zzcwE = ahwVar.zzLR();
                    break;
                case 18:
                    this.tag = ahwVar.readString();
                    break;
                case 26:
                    int iZzb = aij.zzb(ahwVar, 26);
                    int length = this.zzcwI == null ? 0 : this.zzcwI.length;
                    ajd[] ajdVarArr = new ajd[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzcwI, 0, ajdVarArr, 0, length);
                    }
                    while (length < ajdVarArr.length - 1) {
                        ajdVarArr[length] = new ajd();
                        ahwVar.zzb(ajdVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    ajdVarArr[length] = new ajd();
                    ahwVar.zzb(ajdVarArr[length]);
                    this.zzcwI = ajdVarArr;
                    break;
                case 34:
                    this.zzcwJ = ahwVar.readBytes();
                    break;
                case 50:
                    this.zzcwL = ahwVar.readBytes();
                    break;
                case 58:
                    if (this.zzcwO == null) {
                        this.zzcwO = new aiz();
                    }
                    ahwVar.zzb(this.zzcwO);
                    break;
                case 66:
                    this.zzcwM = ahwVar.readString();
                    break;
                case 74:
                    if (this.zzcwK == null) {
                        this.zzcwK = new aja();
                    }
                    ahwVar.zzb(this.zzcwK);
                    break;
                case VocalizerEngine.DEFAULT_VOLUME /* 80 */:
                    this.zzcfX = ahwVar.zzLT();
                    break;
                case TwitterApiErrorConstants.RATE_LIMIT_EXCEEDED /* 88 */:
                    this.zzcwH = ahwVar.zzLS();
                    break;
                case 96:
                    this.zzrE = ahwVar.zzLS();
                    break;
                case 106:
                    this.zzcwN = ahwVar.readString();
                    break;
                case 114:
                    this.zzcwP = ahwVar.readString();
                    break;
                case TwitterApiErrorConstants.EMAIL_ALREADY_REGISTERED /* 120 */:
                    this.zzcwQ = ahwVar.zzLU();
                    break;
                case 130:
                    if (this.zzcwR == null) {
                        this.zzcwR = new ajb();
                    }
                    ahwVar.zzb(this.zzcwR);
                    break;
                case 136:
                    this.zzcwF = ahwVar.zzLR();
                    break;
                case 146:
                    this.zzcwS = ahwVar.readBytes();
                    break;
                case 152:
                    int position = ahwVar.getPosition();
                    int iZzLS = ahwVar.zzLS();
                    switch (iZzLS) {
                        case 0:
                        case 1:
                        case 2:
                            this.zzcwU = iZzLS;
                            break;
                        default:
                            ahwVar.zzco(position);
                            zza(ahwVar, iZzLQ);
                            break;
                    }
                    break;
                case 160:
                    int iZzb2 = aij.zzb(ahwVar, 160);
                    int length2 = this.zzcwV == null ? 0 : this.zzcwV.length;
                    int[] iArr = new int[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzcwV, 0, iArr, 0, length2);
                    }
                    while (length2 < iArr.length - 1) {
                        iArr[length2] = ahwVar.zzLS();
                        ahwVar.zzLQ();
                        length2++;
                    }
                    iArr[length2] = ahwVar.zzLS();
                    this.zzcwV = iArr;
                    break;
                case 162:
                    int iZzcm = ahwVar.zzcm(ahwVar.zzLV());
                    int position2 = ahwVar.getPosition();
                    int i = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLS();
                        i++;
                    }
                    ahwVar.zzco(position2);
                    int length3 = this.zzcwV == null ? 0 : this.zzcwV.length;
                    int[] iArr2 = new int[i + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzcwV, 0, iArr2, 0, length3);
                    }
                    while (length3 < iArr2.length) {
                        iArr2[length3] = ahwVar.zzLS();
                        length3++;
                    }
                    this.zzcwV = iArr2;
                    ahwVar.zzcn(iZzcm);
                    break;
                case 168:
                    this.zzcwG = ahwVar.zzLR();
                    break;
                case 176:
                    this.zzcwW = ahwVar.zzLR();
                    break;
                case 186:
                    if (this.zzcpG == null) {
                        this.zzcpG = new aje();
                    }
                    ahwVar.zzb(this.zzcpG);
                    break;
                case 194:
                    this.zzcwT = ahwVar.readString();
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
        if (this.zzcwE != 0) {
            ahxVar.zzb(1, this.zzcwE);
        }
        if (this.tag != null && !this.tag.equals("")) {
            ahxVar.zzl(2, this.tag);
        }
        if (this.zzcwI != null && this.zzcwI.length > 0) {
            for (int i = 0; i < this.zzcwI.length; i++) {
                ajd ajdVar = this.zzcwI[i];
                if (ajdVar != null) {
                    ahxVar.zza(3, ajdVar);
                }
            }
        }
        if (!Arrays.equals(this.zzcwJ, aij.zzcvs)) {
            ahxVar.zzb(4, this.zzcwJ);
        }
        if (!Arrays.equals(this.zzcwL, aij.zzcvs)) {
            ahxVar.zzb(6, this.zzcwL);
        }
        if (this.zzcwO != null) {
            ahxVar.zza(7, this.zzcwO);
        }
        if (this.zzcwM != null && !this.zzcwM.equals("")) {
            ahxVar.zzl(8, this.zzcwM);
        }
        if (this.zzcwK != null) {
            ahxVar.zza(9, this.zzcwK);
        }
        if (this.zzcfX) {
            ahxVar.zzk(10, this.zzcfX);
        }
        if (this.zzcwH != 0) {
            ahxVar.zzr(11, this.zzcwH);
        }
        if (this.zzrE != 0) {
            ahxVar.zzr(12, this.zzrE);
        }
        if (this.zzcwN != null && !this.zzcwN.equals("")) {
            ahxVar.zzl(13, this.zzcwN);
        }
        if (this.zzcwP != null && !this.zzcwP.equals("")) {
            ahxVar.zzl(14, this.zzcwP);
        }
        if (this.zzcwQ != 180000) {
            ahxVar.zzd(15, this.zzcwQ);
        }
        if (this.zzcwR != null) {
            ahxVar.zza(16, this.zzcwR);
        }
        if (this.zzcwF != 0) {
            ahxVar.zzb(17, this.zzcwF);
        }
        if (!Arrays.equals(this.zzcwS, aij.zzcvs)) {
            ahxVar.zzb(18, this.zzcwS);
        }
        if (this.zzcwU != 0) {
            ahxVar.zzr(19, this.zzcwU);
        }
        if (this.zzcwV != null && this.zzcwV.length > 0) {
            for (int i2 = 0; i2 < this.zzcwV.length; i2++) {
                ahxVar.zzr(20, this.zzcwV[i2]);
            }
        }
        if (this.zzcwG != 0) {
            ahxVar.zzb(21, this.zzcwG);
        }
        if (this.zzcwW != 0) {
            ahxVar.zzb(22, this.zzcwW);
        }
        if (this.zzcpG != null) {
            ahxVar.zza(23, this.zzcpG);
        }
        if (this.zzcwT != null && !this.zzcwT.equals("")) {
            ahxVar.zzl(24, this.zzcwT);
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (this.zzcwE != 0) {
            iZzn += ahx.zze(1, this.zzcwE);
        }
        if (this.tag != null && !this.tag.equals("")) {
            iZzn += ahx.zzm(2, this.tag);
        }
        if (this.zzcwI != null && this.zzcwI.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzcwI.length; i++) {
                ajd ajdVar = this.zzcwI[i];
                if (ajdVar != null) {
                    iZzb += ahx.zzb(3, ajdVar);
                }
            }
            iZzn = iZzb;
        }
        if (!Arrays.equals(this.zzcwJ, aij.zzcvs)) {
            iZzn += ahx.zzc(4, this.zzcwJ);
        }
        if (!Arrays.equals(this.zzcwL, aij.zzcvs)) {
            iZzn += ahx.zzc(6, this.zzcwL);
        }
        if (this.zzcwO != null) {
            iZzn += ahx.zzb(7, this.zzcwO);
        }
        if (this.zzcwM != null && !this.zzcwM.equals("")) {
            iZzn += ahx.zzm(8, this.zzcwM);
        }
        if (this.zzcwK != null) {
            iZzn += ahx.zzb(9, this.zzcwK);
        }
        if (this.zzcfX) {
            iZzn += ahx.zzcs(10) + 1;
        }
        if (this.zzcwH != 0) {
            iZzn += ahx.zzs(11, this.zzcwH);
        }
        if (this.zzrE != 0) {
            iZzn += ahx.zzs(12, this.zzrE);
        }
        if (this.zzcwN != null && !this.zzcwN.equals("")) {
            iZzn += ahx.zzm(13, this.zzcwN);
        }
        if (this.zzcwP != null && !this.zzcwP.equals("")) {
            iZzn += ahx.zzm(14, this.zzcwP);
        }
        if (this.zzcwQ != 180000) {
            iZzn += ahx.zzf(15, this.zzcwQ);
        }
        if (this.zzcwR != null) {
            iZzn += ahx.zzb(16, this.zzcwR);
        }
        if (this.zzcwF != 0) {
            iZzn += ahx.zze(17, this.zzcwF);
        }
        if (!Arrays.equals(this.zzcwS, aij.zzcvs)) {
            iZzn += ahx.zzc(18, this.zzcwS);
        }
        if (this.zzcwU != 0) {
            iZzn += ahx.zzs(19, this.zzcwU);
        }
        if (this.zzcwV != null && this.zzcwV.length > 0) {
            int iZzcq = 0;
            for (int i2 = 0; i2 < this.zzcwV.length; i2++) {
                iZzcq += ahx.zzcq(this.zzcwV[i2]);
            }
            iZzn = iZzn + iZzcq + (this.zzcwV.length * 2);
        }
        if (this.zzcwG != 0) {
            iZzn += ahx.zze(21, this.zzcwG);
        }
        if (this.zzcwW != 0) {
            iZzn += ahx.zze(22, this.zzcwW);
        }
        if (this.zzcpG != null) {
            iZzn += ahx.zzb(23, this.zzcpG);
        }
        return (this.zzcwT == null || this.zzcwT.equals("")) ? iZzn : iZzn + ahx.zzm(24, this.zzcwT);
    }
}
