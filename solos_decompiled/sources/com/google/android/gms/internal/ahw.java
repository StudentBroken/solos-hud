package com.google.android.gms.internal;

import com.digits.sdk.vcard.VCardConfig;
import com.kopin.accessory.utility.CallHelper;
import java.io.IOException;

/* JADX INFO: loaded from: classes67.dex */
public final class ahw {
    private final byte[] buffer;
    private int zzcuM;
    private int zzcuN;
    private int zzcuO;
    private int zzcuP;
    private int zzcuQ;
    private int zzcuS;
    private int zzcuR = Integer.MAX_VALUE;
    private int zzcuT = 64;
    private int zzcuU = VCardConfig.FLAG_APPEND_TYPE_PARAM;

    private ahw(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzcuM = i;
        this.zzcuN = i + i2;
        this.zzcuP = i;
    }

    public static ahw zzI(byte[] bArr) {
        return zzb(bArr, 0, bArr.length);
    }

    private final void zzLZ() {
        this.zzcuN += this.zzcuO;
        int i = this.zzcuN;
        if (i <= this.zzcuR) {
            this.zzcuO = 0;
        } else {
            this.zzcuO = i - this.zzcuR;
            this.zzcuN -= this.zzcuO;
        }
    }

    private final byte zzMb() throws IOException {
        if (this.zzcuP == this.zzcuN) {
            throw aie.zzMg();
        }
        byte[] bArr = this.buffer;
        int i = this.zzcuP;
        this.zzcuP = i + 1;
        return bArr[i];
    }

    public static ahw zzb(byte[] bArr, int i, int i2) {
        return new ahw(bArr, 0, i2);
    }

    private final void zzcp(int i) throws IOException {
        if (i < 0) {
            throw aie.zzMh();
        }
        if (this.zzcuP + i > this.zzcuR) {
            zzcp(this.zzcuR - this.zzcuP);
            throw aie.zzMg();
        }
        if (i > this.zzcuN - this.zzcuP) {
            throw aie.zzMg();
        }
        this.zzcuP += i;
    }

    public final int getPosition() {
        return this.zzcuP - this.zzcuM;
    }

    public final byte[] readBytes() throws IOException {
        int iZzLV = zzLV();
        if (iZzLV < 0) {
            throw aie.zzMh();
        }
        if (iZzLV == 0) {
            return aij.zzcvs;
        }
        if (iZzLV > this.zzcuN - this.zzcuP) {
            throw aie.zzMg();
        }
        byte[] bArr = new byte[iZzLV];
        System.arraycopy(this.buffer, this.zzcuP, bArr, 0, iZzLV);
        this.zzcuP = iZzLV + this.zzcuP;
        return bArr;
    }

    public final String readString() throws IOException {
        int iZzLV = zzLV();
        if (iZzLV < 0) {
            throw aie.zzMh();
        }
        if (iZzLV > this.zzcuN - this.zzcuP) {
            throw aie.zzMg();
        }
        String str = new String(this.buffer, this.zzcuP, iZzLV, aid.UTF_8);
        this.zzcuP = iZzLV + this.zzcuP;
        return str;
    }

    public final int zzLQ() throws IOException {
        if (this.zzcuP == this.zzcuN) {
            this.zzcuQ = 0;
            return 0;
        }
        this.zzcuQ = zzLV();
        if (this.zzcuQ == 0) {
            throw new aie("Protocol message contained an invalid tag (zero).");
        }
        return this.zzcuQ;
    }

    public final long zzLR() throws IOException {
        return zzLW();
    }

    public final int zzLS() throws IOException {
        return zzLV();
    }

    public final boolean zzLT() throws IOException {
        return zzLV() != 0;
    }

    public final long zzLU() throws IOException {
        long jZzLW = zzLW();
        return (-(jZzLW & 1)) ^ (jZzLW >>> 1);
    }

    public final int zzLV() throws IOException {
        byte bZzMb = zzMb();
        if (bZzMb >= 0) {
            return bZzMb;
        }
        int i = bZzMb & 127;
        byte bZzMb2 = zzMb();
        if (bZzMb2 >= 0) {
            return i | (bZzMb2 << 7);
        }
        int i2 = i | ((bZzMb2 & 127) << 7);
        byte bZzMb3 = zzMb();
        if (bZzMb3 >= 0) {
            return i2 | (bZzMb3 << CallHelper.CallState.CALL_ENDED);
        }
        int i3 = i2 | ((bZzMb3 & 127) << 14);
        byte bZzMb4 = zzMb();
        if (bZzMb4 >= 0) {
            return i3 | (bZzMb4 << 21);
        }
        int i4 = i3 | ((bZzMb4 & 127) << 21);
        byte bZzMb5 = zzMb();
        int i5 = i4 | (bZzMb5 << 28);
        if (bZzMb5 >= 0) {
            return i5;
        }
        for (int i6 = 0; i6 < 5; i6++) {
            if (zzMb() >= 0) {
                return i5;
            }
        }
        throw aie.zzMi();
    }

    public final long zzLW() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte bZzMb = zzMb();
            j |= ((long) (bZzMb & 127)) << i;
            if ((bZzMb & CallHelper.CallState.FLAG_CALL_ACTIVE) == 0) {
                return j;
            }
        }
        throw aie.zzMi();
    }

    public final int zzLX() throws IOException {
        return (zzMb() & 255) | ((zzMb() & 255) << 8) | ((zzMb() & 255) << 16) | ((zzMb() & 255) << 24);
    }

    public final long zzLY() throws IOException {
        byte bZzMb = zzMb();
        return ((((long) zzMb()) & 255) << 8) | (((long) bZzMb) & 255) | ((((long) zzMb()) & 255) << 16) | ((((long) zzMb()) & 255) << 24) | ((((long) zzMb()) & 255) << 32) | ((((long) zzMb()) & 255) << 40) | ((((long) zzMb()) & 255) << 48) | ((((long) zzMb()) & 255) << 56);
    }

    public final int zzMa() {
        if (this.zzcuR == Integer.MAX_VALUE) {
            return -1;
        }
        return this.zzcuR - this.zzcuP;
    }

    public final void zza(aif aifVar, int i) throws IOException {
        if (this.zzcuS >= this.zzcuT) {
            throw aie.zzMj();
        }
        this.zzcuS++;
        aifVar.zza(this);
        zzck((i << 3) | 4);
        this.zzcuS--;
    }

    public final void zzb(aif aifVar) throws IOException {
        int iZzLV = zzLV();
        if (this.zzcuS >= this.zzcuT) {
            throw aie.zzMj();
        }
        int iZzcm = zzcm(iZzLV);
        this.zzcuS++;
        aifVar.zza(this);
        zzck(0);
        this.zzcuS--;
        zzcn(iZzcm);
    }

    public final void zzck(int i) throws aie {
        if (this.zzcuQ != i) {
            throw new aie("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzcl(int i) throws IOException {
        int iZzLQ;
        switch (i & 7) {
            case 0:
                zzLV();
                return true;
            case 1:
                zzLY();
                return true;
            case 2:
                zzcp(zzLV());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzLX();
                return true;
            default:
                throw new aie("Protocol message tag had invalid wire type.");
        }
        do {
            iZzLQ = zzLQ();
            if (iZzLQ != 0) {
            }
            zzck(((i >>> 3) << 3) | 4);
            return true;
        } while (zzcl(iZzLQ));
        zzck(((i >>> 3) << 3) | 4);
        return true;
    }

    public final int zzcm(int i) throws aie {
        if (i < 0) {
            throw aie.zzMh();
        }
        int i2 = this.zzcuP + i;
        int i3 = this.zzcuR;
        if (i2 > i3) {
            throw aie.zzMg();
        }
        this.zzcuR = i2;
        zzLZ();
        return i3;
    }

    public final void zzcn(int i) {
        this.zzcuR = i;
        zzLZ();
    }

    public final void zzco(int i) {
        zzq(i, this.zzcuQ);
    }

    public final byte[] zzp(int i, int i2) {
        if (i2 == 0) {
            return aij.zzcvs;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzcuM + i, bArr, 0, i2);
        return bArr;
    }

    final void zzq(int i, int i2) {
        if (i > this.zzcuP - this.zzcuM) {
            throw new IllegalArgumentException(new StringBuilder(50).append("Position ").append(i).append(" is beyond current ").append(this.zzcuP - this.zzcuM).toString());
        }
        if (i < 0) {
            throw new IllegalArgumentException(new StringBuilder(24).append("Bad position ").append(i).toString());
        }
        this.zzcuP = this.zzcuM + i;
        this.zzcuQ = i2;
    }
}
