package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import java.io.IOException;

/* JADX INFO: loaded from: classes3.dex */
public final class aiz extends ahz<aiz> implements Cloneable {
    private String[] zzcwu = aij.EMPTY_STRING_ARRAY;
    private String[] zzcwv = aij.EMPTY_STRING_ARRAY;
    private int[] zzcww = aij.zzcvm;
    private long[] zzcwx = aij.zzcvn;
    private long[] zzcwy = aij.zzcvn;

    public aiz() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMq, reason: merged with bridge method [inline-methods] */
    public aiz clone() {
        try {
            aiz aizVar = (aiz) super.clone();
            if (this.zzcwu != null && this.zzcwu.length > 0) {
                aizVar.zzcwu = (String[]) this.zzcwu.clone();
            }
            if (this.zzcwv != null && this.zzcwv.length > 0) {
                aizVar.zzcwv = (String[]) this.zzcwv.clone();
            }
            if (this.zzcww != null && this.zzcww.length > 0) {
                aizVar.zzcww = (int[]) this.zzcww.clone();
            }
            if (this.zzcwx != null && this.zzcwx.length > 0) {
                aizVar.zzcwx = (long[]) this.zzcwx.clone();
            }
            if (this.zzcwy != null && this.zzcwy.length > 0) {
                aizVar.zzcwy = (long[]) this.zzcwy.clone();
            }
            return aizVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof aiz)) {
            return false;
        }
        aiz aizVar = (aiz) obj;
        if (aid.equals(this.zzcwu, aizVar.zzcwu) && aid.equals(this.zzcwv, aizVar.zzcwv) && aid.equals(this.zzcww, aizVar.zzcww) && aid.equals(this.zzcwx, aizVar.zzcwx) && aid.equals(this.zzcwy, aizVar.zzcwy)) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? aizVar.zzcuW == null || aizVar.zzcuW.isEmpty() : this.zzcuW.equals(aizVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        return ((this.zzcuW == null || this.zzcuW.isEmpty()) ? 0 : this.zzcuW.hashCode()) + ((((((((((((getClass().getName().hashCode() + 527) * 31) + aid.hashCode(this.zzcwu)) * 31) + aid.hashCode(this.zzcwv)) * 31) + aid.hashCode(this.zzcww)) * 31) + aid.hashCode(this.zzcwx)) * 31) + aid.hashCode(this.zzcwy)) * 31);
    }

    @Override // com.google.android.gms.internal.ahz
    /* JADX INFO: renamed from: zzMd */
    public final /* synthetic */ ahz clone() throws CloneNotSupportedException {
        return (aiz) clone();
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    /* JADX INFO: renamed from: zzMe */
    public final /* synthetic */ aif clone() throws CloneNotSupportedException {
        return (aiz) clone();
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    int iZzb = aij.zzb(ahwVar, 10);
                    int length = this.zzcwu == null ? 0 : this.zzcwu.length;
                    String[] strArr = new String[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzcwu, 0, strArr, 0, length);
                    }
                    while (length < strArr.length - 1) {
                        strArr[length] = ahwVar.readString();
                        ahwVar.zzLQ();
                        length++;
                    }
                    strArr[length] = ahwVar.readString();
                    this.zzcwu = strArr;
                    break;
                case 18:
                    int iZzb2 = aij.zzb(ahwVar, 18);
                    int length2 = this.zzcwv == null ? 0 : this.zzcwv.length;
                    String[] strArr2 = new String[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzcwv, 0, strArr2, 0, length2);
                    }
                    while (length2 < strArr2.length - 1) {
                        strArr2[length2] = ahwVar.readString();
                        ahwVar.zzLQ();
                        length2++;
                    }
                    strArr2[length2] = ahwVar.readString();
                    this.zzcwv = strArr2;
                    break;
                case 24:
                    int iZzb3 = aij.zzb(ahwVar, 24);
                    int length3 = this.zzcww == null ? 0 : this.zzcww.length;
                    int[] iArr = new int[iZzb3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzcww, 0, iArr, 0, length3);
                    }
                    while (length3 < iArr.length - 1) {
                        iArr[length3] = ahwVar.zzLS();
                        ahwVar.zzLQ();
                        length3++;
                    }
                    iArr[length3] = ahwVar.zzLS();
                    this.zzcww = iArr;
                    break;
                case 26:
                    int iZzcm = ahwVar.zzcm(ahwVar.zzLV());
                    int position = ahwVar.getPosition();
                    int i = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLS();
                        i++;
                    }
                    ahwVar.zzco(position);
                    int length4 = this.zzcww == null ? 0 : this.zzcww.length;
                    int[] iArr2 = new int[i + length4];
                    if (length4 != 0) {
                        System.arraycopy(this.zzcww, 0, iArr2, 0, length4);
                    }
                    while (length4 < iArr2.length) {
                        iArr2[length4] = ahwVar.zzLS();
                        length4++;
                    }
                    this.zzcww = iArr2;
                    ahwVar.zzcn(iZzcm);
                    break;
                case 32:
                    int iZzb4 = aij.zzb(ahwVar, 32);
                    int length5 = this.zzcwx == null ? 0 : this.zzcwx.length;
                    long[] jArr = new long[iZzb4 + length5];
                    if (length5 != 0) {
                        System.arraycopy(this.zzcwx, 0, jArr, 0, length5);
                    }
                    while (length5 < jArr.length - 1) {
                        jArr[length5] = ahwVar.zzLR();
                        ahwVar.zzLQ();
                        length5++;
                    }
                    jArr[length5] = ahwVar.zzLR();
                    this.zzcwx = jArr;
                    break;
                case 34:
                    int iZzcm2 = ahwVar.zzcm(ahwVar.zzLV());
                    int position2 = ahwVar.getPosition();
                    int i2 = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLR();
                        i2++;
                    }
                    ahwVar.zzco(position2);
                    int length6 = this.zzcwx == null ? 0 : this.zzcwx.length;
                    long[] jArr2 = new long[i2 + length6];
                    if (length6 != 0) {
                        System.arraycopy(this.zzcwx, 0, jArr2, 0, length6);
                    }
                    while (length6 < jArr2.length) {
                        jArr2[length6] = ahwVar.zzLR();
                        length6++;
                    }
                    this.zzcwx = jArr2;
                    ahwVar.zzcn(iZzcm2);
                    break;
                case 40:
                    int iZzb5 = aij.zzb(ahwVar, 40);
                    int length7 = this.zzcwy == null ? 0 : this.zzcwy.length;
                    long[] jArr3 = new long[iZzb5 + length7];
                    if (length7 != 0) {
                        System.arraycopy(this.zzcwy, 0, jArr3, 0, length7);
                    }
                    while (length7 < jArr3.length - 1) {
                        jArr3[length7] = ahwVar.zzLR();
                        ahwVar.zzLQ();
                        length7++;
                    }
                    jArr3[length7] = ahwVar.zzLR();
                    this.zzcwy = jArr3;
                    break;
                case MotionEventCompat.AXIS_GENERIC_11 /* 42 */:
                    int iZzcm3 = ahwVar.zzcm(ahwVar.zzLV());
                    int position3 = ahwVar.getPosition();
                    int i3 = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLR();
                        i3++;
                    }
                    ahwVar.zzco(position3);
                    int length8 = this.zzcwy == null ? 0 : this.zzcwy.length;
                    long[] jArr4 = new long[i3 + length8];
                    if (length8 != 0) {
                        System.arraycopy(this.zzcwy, 0, jArr4, 0, length8);
                    }
                    while (length8 < jArr4.length) {
                        jArr4[length8] = ahwVar.zzLR();
                        length8++;
                    }
                    this.zzcwy = jArr4;
                    ahwVar.zzcn(iZzcm3);
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
        if (this.zzcwu != null && this.zzcwu.length > 0) {
            for (int i = 0; i < this.zzcwu.length; i++) {
                String str = this.zzcwu[i];
                if (str != null) {
                    ahxVar.zzl(1, str);
                }
            }
        }
        if (this.zzcwv != null && this.zzcwv.length > 0) {
            for (int i2 = 0; i2 < this.zzcwv.length; i2++) {
                String str2 = this.zzcwv[i2];
                if (str2 != null) {
                    ahxVar.zzl(2, str2);
                }
            }
        }
        if (this.zzcww != null && this.zzcww.length > 0) {
            for (int i3 = 0; i3 < this.zzcww.length; i3++) {
                ahxVar.zzr(3, this.zzcww[i3]);
            }
        }
        if (this.zzcwx != null && this.zzcwx.length > 0) {
            for (int i4 = 0; i4 < this.zzcwx.length; i4++) {
                ahxVar.zzb(4, this.zzcwx[i4]);
            }
        }
        if (this.zzcwy != null && this.zzcwy.length > 0) {
            for (int i5 = 0; i5 < this.zzcwy.length; i5++) {
                ahxVar.zzb(5, this.zzcwy[i5]);
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int length;
        int iZzn = super.zzn();
        if (this.zzcwu == null || this.zzcwu.length <= 0) {
            length = iZzn;
        } else {
            int iZzip = 0;
            int i = 0;
            for (int i2 = 0; i2 < this.zzcwu.length; i2++) {
                String str = this.zzcwu[i2];
                if (str != null) {
                    i++;
                    iZzip += ahx.zzip(str);
                }
            }
            length = iZzn + iZzip + (i * 1);
        }
        if (this.zzcwv != null && this.zzcwv.length > 0) {
            int iZzip2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < this.zzcwv.length; i4++) {
                String str2 = this.zzcwv[i4];
                if (str2 != null) {
                    i3++;
                    iZzip2 += ahx.zzip(str2);
                }
            }
            length = length + iZzip2 + (i3 * 1);
        }
        if (this.zzcww != null && this.zzcww.length > 0) {
            int iZzcq = 0;
            for (int i5 = 0; i5 < this.zzcww.length; i5++) {
                iZzcq += ahx.zzcq(this.zzcww[i5]);
            }
            length = length + iZzcq + (this.zzcww.length * 1);
        }
        if (this.zzcwx != null && this.zzcwx.length > 0) {
            int iZzaP = 0;
            for (int i6 = 0; i6 < this.zzcwx.length; i6++) {
                iZzaP += ahx.zzaP(this.zzcwx[i6]);
            }
            length = length + iZzaP + (this.zzcwx.length * 1);
        }
        if (this.zzcwy == null || this.zzcwy.length <= 0) {
            return length;
        }
        int iZzaP2 = 0;
        for (int i7 = 0; i7 < this.zzcwy.length; i7++) {
            iZzaP2 += ahx.zzaP(this.zzcwy[i7]);
        }
        return length + iZzaP2 + (this.zzcwy.length * 1);
    }
}
