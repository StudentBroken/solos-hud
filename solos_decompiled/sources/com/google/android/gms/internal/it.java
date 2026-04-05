package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import com.kopin.solos.view.graphics.Bar;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes6.dex */
public final class it extends ahz<it> {
    public byte[] zzbTO = aij.zzcvs;
    public String zzbTP = "";
    public double zzbTQ = 0.0d;
    public float zzbTR = 0.0f;
    public long zzbTS = 0;
    public int zzbTT = 0;
    public int zzbTU = 0;
    public boolean zzbTV = false;
    public ir[] zzbTW = ir.zzDX();
    public is[] zzbTX = is.zzDY();
    public String[] zzbTY = aij.EMPTY_STRING_ARRAY;
    public long[] zzbTZ = aij.zzcvn;
    public float[] zzbUa = aij.zzcvo;
    public long zzbUb = 0;

    public it() {
        this.zzcuW = null;
        this.zzcvf = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof it)) {
            return false;
        }
        it itVar = (it) obj;
        if (!Arrays.equals(this.zzbTO, itVar.zzbTO)) {
            return false;
        }
        if (this.zzbTP == null) {
            if (itVar.zzbTP != null) {
                return false;
            }
        } else if (!this.zzbTP.equals(itVar.zzbTP)) {
            return false;
        }
        if (Double.doubleToLongBits(this.zzbTQ) == Double.doubleToLongBits(itVar.zzbTQ) && Float.floatToIntBits(this.zzbTR) == Float.floatToIntBits(itVar.zzbTR) && this.zzbTS == itVar.zzbTS && this.zzbTT == itVar.zzbTT && this.zzbTU == itVar.zzbTU && this.zzbTV == itVar.zzbTV && aid.equals(this.zzbTW, itVar.zzbTW) && aid.equals(this.zzbTX, itVar.zzbTX) && aid.equals(this.zzbTY, itVar.zzbTY) && aid.equals(this.zzbTZ, itVar.zzbTZ) && aid.equals(this.zzbUa, itVar.zzbUa) && this.zzbUb == itVar.zzbUb) {
            return (this.zzcuW == null || this.zzcuW.isEmpty()) ? itVar.zzcuW == null || itVar.zzcuW.isEmpty() : this.zzcuW.equals(itVar.zzcuW);
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 0;
        int iHashCode2 = (this.zzbTP == null ? 0 : this.zzbTP.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzbTO)) * 31);
        long jDoubleToLongBits = Double.doubleToLongBits(this.zzbTQ);
        int iFloatToIntBits = ((((((((((((((this.zzbTV ? 1231 : 1237) + (((((((((((iHashCode2 * 31) + ((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32)))) * 31) + Float.floatToIntBits(this.zzbTR)) * 31) + ((int) (this.zzbTS ^ (this.zzbTS >>> 32)))) * 31) + this.zzbTT) * 31) + this.zzbTU) * 31)) * 31) + aid.hashCode(this.zzbTW)) * 31) + aid.hashCode(this.zzbTX)) * 31) + aid.hashCode(this.zzbTY)) * 31) + aid.hashCode(this.zzbTZ)) * 31) + aid.hashCode(this.zzbUa)) * 31) + ((int) (this.zzbUb ^ (this.zzbUb >>> 32)))) * 31;
        if (this.zzcuW != null && !this.zzcuW.isEmpty()) {
            iHashCode = this.zzcuW.hashCode();
        }
        return iFloatToIntBits + iHashCode;
    }

    @Override // com.google.android.gms.internal.aif
    public final /* synthetic */ aif zza(ahw ahwVar) throws IOException {
        while (true) {
            int iZzLQ = ahwVar.zzLQ();
            switch (iZzLQ) {
                case 0:
                    break;
                case 10:
                    this.zzbTO = ahwVar.readBytes();
                    break;
                case 18:
                    this.zzbTP = ahwVar.readString();
                    break;
                case 25:
                    this.zzbTQ = Double.longBitsToDouble(ahwVar.zzLY());
                    break;
                case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                    this.zzbTR = Float.intBitsToFloat(ahwVar.zzLX());
                    break;
                case 40:
                    this.zzbTS = ahwVar.zzLW();
                    break;
                case Bar.DEFAULT_HEIGHT /* 48 */:
                    this.zzbTT = ahwVar.zzLV();
                    break;
                case 56:
                    int iZzLV = ahwVar.zzLV();
                    this.zzbTU = (-(iZzLV & 1)) ^ (iZzLV >>> 1);
                    break;
                case 64:
                    this.zzbTV = ahwVar.zzLT();
                    break;
                case 74:
                    int iZzb = aij.zzb(ahwVar, 74);
                    int length = this.zzbTW == null ? 0 : this.zzbTW.length;
                    ir[] irVarArr = new ir[iZzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzbTW, 0, irVarArr, 0, length);
                    }
                    while (length < irVarArr.length - 1) {
                        irVarArr[length] = new ir();
                        ahwVar.zzb(irVarArr[length]);
                        ahwVar.zzLQ();
                        length++;
                    }
                    irVarArr[length] = new ir();
                    ahwVar.zzb(irVarArr[length]);
                    this.zzbTW = irVarArr;
                    break;
                case 82:
                    int iZzb2 = aij.zzb(ahwVar, 82);
                    int length2 = this.zzbTX == null ? 0 : this.zzbTX.length;
                    is[] isVarArr = new is[iZzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzbTX, 0, isVarArr, 0, length2);
                    }
                    while (length2 < isVarArr.length - 1) {
                        isVarArr[length2] = new is();
                        ahwVar.zzb(isVarArr[length2]);
                        ahwVar.zzLQ();
                        length2++;
                    }
                    isVarArr[length2] = new is();
                    ahwVar.zzb(isVarArr[length2]);
                    this.zzbTX = isVarArr;
                    break;
                case 90:
                    int iZzb3 = aij.zzb(ahwVar, 90);
                    int length3 = this.zzbTY == null ? 0 : this.zzbTY.length;
                    String[] strArr = new String[iZzb3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzbTY, 0, strArr, 0, length3);
                    }
                    while (length3 < strArr.length - 1) {
                        strArr[length3] = ahwVar.readString();
                        ahwVar.zzLQ();
                        length3++;
                    }
                    strArr[length3] = ahwVar.readString();
                    this.zzbTY = strArr;
                    break;
                case 96:
                    int iZzb4 = aij.zzb(ahwVar, 96);
                    int length4 = this.zzbTZ == null ? 0 : this.zzbTZ.length;
                    long[] jArr = new long[iZzb4 + length4];
                    if (length4 != 0) {
                        System.arraycopy(this.zzbTZ, 0, jArr, 0, length4);
                    }
                    while (length4 < jArr.length - 1) {
                        jArr[length4] = ahwVar.zzLW();
                        ahwVar.zzLQ();
                        length4++;
                    }
                    jArr[length4] = ahwVar.zzLW();
                    this.zzbTZ = jArr;
                    break;
                case 98:
                    int iZzcm = ahwVar.zzcm(ahwVar.zzLV());
                    int position = ahwVar.getPosition();
                    int i = 0;
                    while (ahwVar.zzMa() > 0) {
                        ahwVar.zzLW();
                        i++;
                    }
                    ahwVar.zzco(position);
                    int length5 = this.zzbTZ == null ? 0 : this.zzbTZ.length;
                    long[] jArr2 = new long[i + length5];
                    if (length5 != 0) {
                        System.arraycopy(this.zzbTZ, 0, jArr2, 0, length5);
                    }
                    while (length5 < jArr2.length) {
                        jArr2[length5] = ahwVar.zzLW();
                        length5++;
                    }
                    this.zzbTZ = jArr2;
                    ahwVar.zzcn(iZzcm);
                    break;
                case 104:
                    this.zzbUb = ahwVar.zzLW();
                    break;
                case 114:
                    int iZzLV2 = ahwVar.zzLV();
                    int iZzcm2 = ahwVar.zzcm(iZzLV2);
                    int i2 = iZzLV2 / 4;
                    int length6 = this.zzbUa == null ? 0 : this.zzbUa.length;
                    float[] fArr = new float[i2 + length6];
                    if (length6 != 0) {
                        System.arraycopy(this.zzbUa, 0, fArr, 0, length6);
                    }
                    while (length6 < fArr.length) {
                        fArr[length6] = Float.intBitsToFloat(ahwVar.zzLX());
                        length6++;
                    }
                    this.zzbUa = fArr;
                    ahwVar.zzcn(iZzcm2);
                    break;
                case 117:
                    int iZzb5 = aij.zzb(ahwVar, 117);
                    int length7 = this.zzbUa == null ? 0 : this.zzbUa.length;
                    float[] fArr2 = new float[iZzb5 + length7];
                    if (length7 != 0) {
                        System.arraycopy(this.zzbUa, 0, fArr2, 0, length7);
                    }
                    while (length7 < fArr2.length - 1) {
                        fArr2[length7] = Float.intBitsToFloat(ahwVar.zzLX());
                        ahwVar.zzLQ();
                        length7++;
                    }
                    fArr2[length7] = Float.intBitsToFloat(ahwVar.zzLX());
                    this.zzbUa = fArr2;
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
        if (!Arrays.equals(this.zzbTO, aij.zzcvs)) {
            ahxVar.zzb(1, this.zzbTO);
        }
        if (this.zzbTP != null && !this.zzbTP.equals("")) {
            ahxVar.zzl(2, this.zzbTP);
        }
        if (Double.doubleToLongBits(this.zzbTQ) != Double.doubleToLongBits(0.0d)) {
            ahxVar.zza(3, this.zzbTQ);
        }
        if (Float.floatToIntBits(this.zzbTR) != Float.floatToIntBits(0.0f)) {
            ahxVar.zzc(4, this.zzbTR);
        }
        if (this.zzbTS != 0) {
            ahxVar.zzb(5, this.zzbTS);
        }
        if (this.zzbTT != 0) {
            ahxVar.zzr(6, this.zzbTT);
        }
        if (this.zzbTU != 0) {
            int i = this.zzbTU;
            ahxVar.zzt(7, 0);
            ahxVar.zzct(ahx.zzcv(i));
        }
        if (this.zzbTV) {
            ahxVar.zzk(8, this.zzbTV);
        }
        if (this.zzbTW != null && this.zzbTW.length > 0) {
            for (int i2 = 0; i2 < this.zzbTW.length; i2++) {
                ir irVar = this.zzbTW[i2];
                if (irVar != null) {
                    ahxVar.zza(9, irVar);
                }
            }
        }
        if (this.zzbTX != null && this.zzbTX.length > 0) {
            for (int i3 = 0; i3 < this.zzbTX.length; i3++) {
                is isVar = this.zzbTX[i3];
                if (isVar != null) {
                    ahxVar.zza(10, isVar);
                }
            }
        }
        if (this.zzbTY != null && this.zzbTY.length > 0) {
            for (int i4 = 0; i4 < this.zzbTY.length; i4++) {
                String str = this.zzbTY[i4];
                if (str != null) {
                    ahxVar.zzl(11, str);
                }
            }
        }
        if (this.zzbTZ != null && this.zzbTZ.length > 0) {
            for (int i5 = 0; i5 < this.zzbTZ.length; i5++) {
                ahxVar.zzb(12, this.zzbTZ[i5]);
            }
        }
        if (this.zzbUb != 0) {
            ahxVar.zzb(13, this.zzbUb);
        }
        if (this.zzbUa != null && this.zzbUa.length > 0) {
            for (int i6 = 0; i6 < this.zzbUa.length; i6++) {
                ahxVar.zzc(14, this.zzbUa[i6]);
            }
        }
        super.zza(ahxVar);
    }

    @Override // com.google.android.gms.internal.ahz, com.google.android.gms.internal.aif
    protected final int zzn() {
        int iZzn = super.zzn();
        if (!Arrays.equals(this.zzbTO, aij.zzcvs)) {
            iZzn += ahx.zzc(1, this.zzbTO);
        }
        if (this.zzbTP != null && !this.zzbTP.equals("")) {
            iZzn += ahx.zzm(2, this.zzbTP);
        }
        if (Double.doubleToLongBits(this.zzbTQ) != Double.doubleToLongBits(0.0d)) {
            iZzn += ahx.zzcs(3) + 8;
        }
        if (Float.floatToIntBits(this.zzbTR) != Float.floatToIntBits(0.0f)) {
            iZzn += ahx.zzcs(4) + 4;
        }
        if (this.zzbTS != 0) {
            iZzn += ahx.zze(5, this.zzbTS);
        }
        if (this.zzbTT != 0) {
            iZzn += ahx.zzs(6, this.zzbTT);
        }
        if (this.zzbTU != 0) {
            iZzn += ahx.zzcu(ahx.zzcv(this.zzbTU)) + ahx.zzcs(7);
        }
        if (this.zzbTV) {
            iZzn += ahx.zzcs(8) + 1;
        }
        if (this.zzbTW != null && this.zzbTW.length > 0) {
            int iZzb = iZzn;
            for (int i = 0; i < this.zzbTW.length; i++) {
                ir irVar = this.zzbTW[i];
                if (irVar != null) {
                    iZzb += ahx.zzb(9, irVar);
                }
            }
            iZzn = iZzb;
        }
        if (this.zzbTX != null && this.zzbTX.length > 0) {
            int iZzb2 = iZzn;
            for (int i2 = 0; i2 < this.zzbTX.length; i2++) {
                is isVar = this.zzbTX[i2];
                if (isVar != null) {
                    iZzb2 += ahx.zzb(10, isVar);
                }
            }
            iZzn = iZzb2;
        }
        if (this.zzbTY != null && this.zzbTY.length > 0) {
            int iZzip = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < this.zzbTY.length; i4++) {
                String str = this.zzbTY[i4];
                if (str != null) {
                    i3++;
                    iZzip += ahx.zzip(str);
                }
            }
            iZzn = iZzn + iZzip + (i3 * 1);
        }
        if (this.zzbTZ != null && this.zzbTZ.length > 0) {
            int iZzaP = 0;
            for (int i5 = 0; i5 < this.zzbTZ.length; i5++) {
                iZzaP += ahx.zzaP(this.zzbTZ[i5]);
            }
            iZzn = iZzn + iZzaP + (this.zzbTZ.length * 1);
        }
        if (this.zzbUb != 0) {
            iZzn += ahx.zze(13, this.zzbUb);
        }
        return (this.zzbUa == null || this.zzbUa.length <= 0) ? iZzn : iZzn + (this.zzbUa.length * 4) + (this.zzbUa.length * 1);
    }
}
