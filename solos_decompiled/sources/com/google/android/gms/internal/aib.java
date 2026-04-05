package com.google.android.gms.internal;

/* JADX INFO: loaded from: classes67.dex */
public final class aib implements Cloneable {
    private static final aic zzcuY = new aic();
    private int mSize;
    private boolean zzcuZ;
    private int[] zzcva;
    private aic[] zzcvb;

    aib() {
        this(10);
    }

    private aib(int i) {
        this.zzcuZ = false;
        int iIdealIntArraySize = idealIntArraySize(i);
        this.zzcva = new int[iIdealIntArraySize];
        this.zzcvb = new aic[iIdealIntArraySize];
        this.mSize = 0;
    }

    private static int idealIntArraySize(int i) {
        int i2 = i << 2;
        int i3 = 4;
        while (true) {
            if (i3 >= 32) {
                break;
            }
            if (i2 <= (1 << i3) - 12) {
                i2 = (1 << i3) - 12;
                break;
            }
            i3++;
        }
        return i2 / 4;
    }

    private final int zzcy(int i) {
        int i2 = 0;
        int i3 = this.mSize - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int i5 = this.zzcva[i4];
            if (i5 < i) {
                i2 = i4 + 1;
            } else {
                if (i5 <= i) {
                    return i4;
                }
                i3 = i4 - 1;
            }
        }
        return i2 ^ (-1);
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        int i = this.mSize;
        aib aibVar = new aib(i);
        System.arraycopy(this.zzcva, 0, aibVar.zzcva, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (this.zzcvb[i2] != null) {
                aibVar.zzcvb[i2] = (aic) this.zzcvb[i2].clone();
            }
        }
        aibVar.mSize = i;
        return aibVar;
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof aib)) {
            return false;
        }
        aib aibVar = (aib) obj;
        if (this.mSize != aibVar.mSize) {
            return false;
        }
        int[] iArr = this.zzcva;
        int[] iArr2 = aibVar.zzcva;
        int i = this.mSize;
        int i2 = 0;
        while (true) {
            if (i2 >= i) {
                z = true;
                break;
            }
            if (iArr[i2] != iArr2[i2]) {
                z = false;
                break;
            }
            i2++;
        }
        if (z) {
            aic[] aicVarArr = this.zzcvb;
            aic[] aicVarArr2 = aibVar.zzcvb;
            int i3 = this.mSize;
            int i4 = 0;
            while (true) {
                if (i4 >= i3) {
                    z2 = true;
                    break;
                }
                if (!aicVarArr[i4].equals(aicVarArr2[i4])) {
                    z2 = false;
                    break;
                }
                i4++;
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int iHashCode = 17;
        for (int i = 0; i < this.mSize; i++) {
            iHashCode = (((iHashCode * 31) + this.zzcva[i]) * 31) + this.zzcvb[i].hashCode();
        }
        return iHashCode;
    }

    public final boolean isEmpty() {
        return this.mSize == 0;
    }

    final int size() {
        return this.mSize;
    }

    final void zza(int i, aic aicVar) {
        int iZzcy = zzcy(i);
        if (iZzcy >= 0) {
            this.zzcvb[iZzcy] = aicVar;
            return;
        }
        int i2 = iZzcy ^ (-1);
        if (i2 < this.mSize && this.zzcvb[i2] == zzcuY) {
            this.zzcva[i2] = i;
            this.zzcvb[i2] = aicVar;
            return;
        }
        if (this.mSize >= this.zzcva.length) {
            int iIdealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[iIdealIntArraySize];
            aic[] aicVarArr = new aic[iIdealIntArraySize];
            System.arraycopy(this.zzcva, 0, iArr, 0, this.zzcva.length);
            System.arraycopy(this.zzcvb, 0, aicVarArr, 0, this.zzcvb.length);
            this.zzcva = iArr;
            this.zzcvb = aicVarArr;
        }
        if (this.mSize - i2 != 0) {
            System.arraycopy(this.zzcva, i2, this.zzcva, i2 + 1, this.mSize - i2);
            System.arraycopy(this.zzcvb, i2, this.zzcvb, i2 + 1, this.mSize - i2);
        }
        this.zzcva[i2] = i;
        this.zzcvb[i2] = aicVar;
        this.mSize++;
    }

    final aic zzcw(int i) {
        int iZzcy = zzcy(i);
        if (iZzcy < 0 || this.zzcvb[iZzcy] == zzcuY) {
            return null;
        }
        return this.zzcvb[iZzcy];
    }

    final aic zzcx(int i) {
        return this.zzcvb[i];
    }
}
