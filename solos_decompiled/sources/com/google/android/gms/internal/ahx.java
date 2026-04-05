package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

/* JADX INFO: loaded from: classes67.dex */
public final class ahx {
    private final ByteBuffer zzcuV;

    private ahx(ByteBuffer byteBuffer) {
        this.zzcuV = byteBuffer;
        this.zzcuV.order(ByteOrder.LITTLE_ENDIAN);
    }

    private ahx(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static ahx zzJ(byte[] bArr) {
        return zzc(bArr, 0, bArr.length);
    }

    public static int zzK(byte[] bArr) {
        return zzcu(bArr.length) + bArr.length;
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int length = charSequence.length();
        int i4 = 0;
        int i5 = i + i2;
        while (i4 < length && i4 + i < i5) {
            char cCharAt = charSequence.charAt(i4);
            if (cCharAt >= 128) {
                break;
            }
            bArr[i + i4] = (byte) cCharAt;
            i4++;
        }
        if (i4 == length) {
            return i + length;
        }
        int i6 = i + i4;
        while (i4 < length) {
            char cCharAt2 = charSequence.charAt(i4);
            if (cCharAt2 < 128 && i6 < i5) {
                i3 = i6 + 1;
                bArr[i6] = (byte) cCharAt2;
            } else if (cCharAt2 < 2048 && i6 <= i5 - 2) {
                int i7 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> 6) | 960);
                i3 = i7 + 1;
                bArr[i7] = (byte) ((cCharAt2 & '?') | 128);
            } else {
                if ((cCharAt2 >= 55296 && 57343 >= cCharAt2) || i6 > i5 - 3) {
                    if (i6 > i5 - 4) {
                        throw new ArrayIndexOutOfBoundsException(new StringBuilder(37).append("Failed writing ").append(cCharAt2).append(" at index ").append(i6).toString());
                    }
                    if (i4 + 1 != charSequence.length()) {
                        i4++;
                        char cCharAt3 = charSequence.charAt(i4);
                        if (Character.isSurrogatePair(cCharAt2, cCharAt3)) {
                            int codePoint = Character.toCodePoint(cCharAt2, cCharAt3);
                            int i8 = i6 + 1;
                            bArr[i6] = (byte) ((codePoint >>> 18) | 240);
                            int i9 = i8 + 1;
                            bArr[i8] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i10 = i9 + 1;
                            bArr[i9] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i3 = i10 + 1;
                            bArr[i10] = (byte) ((codePoint & 63) | 128);
                        }
                    }
                    throw new IllegalArgumentException(new StringBuilder(39).append("Unpaired surrogate at index ").append(i4 - 1).toString());
                }
                int i11 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> '\f') | 480);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (((cCharAt2 >>> 6) & 63) | 128);
                i3 = i12 + 1;
                bArr[i12] = (byte) ((cCharAt2 & '?') | 128);
            }
            i4++;
            i6 = i3;
        }
        return i6;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (!byteBuffer.hasArray()) {
            zzb(charSequence, byteBuffer);
            return;
        }
        try {
            byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
        } catch (ArrayIndexOutOfBoundsException e) {
            BufferOverflowException bufferOverflowException = new BufferOverflowException();
            bufferOverflowException.initCause(e);
            throw bufferOverflowException;
        }
    }

    private final void zzaO(long j) throws IOException {
        while (((-128) & j) != 0) {
            zzcr((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzcr((int) j);
    }

    public static int zzaP(long j) {
        if (((-128) & j) == 0) {
            return 1;
        }
        if (((-16384) & j) == 0) {
            return 2;
        }
        if (((-2097152) & j) == 0) {
            return 3;
        }
        if (((-268435456) & j) == 0) {
            return 4;
        }
        if (((-34359738368L) & j) == 0) {
            return 5;
        }
        if (((-4398046511104L) & j) == 0) {
            return 6;
        }
        if (((-562949953421312L) & j) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & j) == 0) {
            return 8;
        }
        return (Long.MIN_VALUE & j) == 0 ? 9 : 10;
    }

    private final void zzaQ(long j) throws IOException {
        if (this.zzcuV.remaining() < 8) {
            throw new ahy(this.zzcuV.position(), this.zzcuV.limit());
        }
        this.zzcuV.putLong(j);
    }

    private static long zzaR(long j) {
        return (j << 1) ^ (j >> 63);
    }

    public static int zzb(int i, aif aifVar) {
        int iZzcs = zzcs(i);
        int iZzMl = aifVar.zzMl();
        return iZzcs + iZzMl + zzcu(iZzMl);
    }

    private static int zzb(CharSequence charSequence) {
        int i;
        int i2 = 0;
        int length = charSequence.length();
        int i3 = 0;
        while (i3 < length && charSequence.charAt(i3) < 128) {
            i3++;
        }
        int i4 = length;
        while (true) {
            if (i3 >= length) {
                i = i4;
                break;
            }
            char cCharAt = charSequence.charAt(i3);
            if (cCharAt < 2048) {
                i4 += (127 - cCharAt) >>> 31;
                i3++;
            } else {
                int length2 = charSequence.length();
                while (i3 < length2) {
                    char cCharAt2 = charSequence.charAt(i3);
                    if (cCharAt2 < 2048) {
                        i2 += (127 - cCharAt2) >>> 31;
                    } else {
                        i2 += 2;
                        if (55296 <= cCharAt2 && cCharAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i3) < 65536) {
                                throw new IllegalArgumentException(new StringBuilder(39).append("Unpaired surrogate at index ").append(i3).toString());
                            }
                            i3++;
                        }
                    }
                    i3++;
                }
                i = i4 + i2;
            }
        }
        if (i >= length) {
            return i;
        }
        throw new IllegalArgumentException(new StringBuilder(54).append("UTF-8 length does not fit in int: ").append(((long) i) + 4294967296L).toString());
    }

    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char cCharAt = charSequence.charAt(i);
            if (cCharAt < 128) {
                byteBuffer.put((byte) cCharAt);
            } else if (cCharAt < 2048) {
                byteBuffer.put((byte) ((cCharAt >>> 6) | 960));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            } else {
                if (cCharAt >= 55296 && 57343 >= cCharAt) {
                    if (i + 1 != charSequence.length()) {
                        i++;
                        char cCharAt2 = charSequence.charAt(i);
                        if (Character.isSurrogatePair(cCharAt, cCharAt2)) {
                            int codePoint = Character.toCodePoint(cCharAt, cCharAt2);
                            byteBuffer.put((byte) ((codePoint >>> 18) | 240));
                            byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                            byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                            byteBuffer.put((byte) ((codePoint & 63) | 128));
                        }
                    }
                    throw new IllegalArgumentException(new StringBuilder(39).append("Unpaired surrogate at index ").append(i - 1).toString());
                }
                byteBuffer.put((byte) ((cCharAt >>> '\f') | 480));
                byteBuffer.put((byte) (((cCharAt >>> 6) & 63) | 128));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            }
            i++;
        }
    }

    public static int zzc(int i, byte[] bArr) {
        return zzcs(i) + zzK(bArr);
    }

    public static ahx zzc(byte[] bArr, int i, int i2) {
        return new ahx(bArr, 0, i2);
    }

    public static int zzcq(int i) {
        if (i >= 0) {
            return zzcu(i);
        }
        return 10;
    }

    private final void zzcr(int i) throws IOException {
        byte b = (byte) i;
        if (!this.zzcuV.hasRemaining()) {
            throw new ahy(this.zzcuV.position(), this.zzcuV.limit());
        }
        this.zzcuV.put(b);
    }

    public static int zzcs(int i) {
        return zzcu(i << 3);
    }

    public static int zzcu(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return ((-268435456) & i) == 0 ? 4 : 5;
    }

    public static int zzcv(int i) {
        return (i << 1) ^ (i >> 31);
    }

    public static int zze(int i, long j) {
        return zzcs(i) + zzaP(j);
    }

    public static int zzf(int i, long j) {
        return zzcs(i) + zzaP(zzaR(j));
    }

    public static int zzip(String str) {
        int iZzb = zzb(str);
        return iZzb + zzcu(iZzb);
    }

    public static int zzm(int i, String str) {
        return zzcs(i) + zzip(str);
    }

    public static int zzs(int i, int i2) {
        return zzcs(i) + zzcq(i2);
    }

    public final void zzL(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.zzcuV.remaining() < length) {
            throw new ahy(this.zzcuV.position(), this.zzcuV.limit());
        }
        this.zzcuV.put(bArr, 0, length);
    }

    public final void zzMc() {
        if (this.zzcuV.remaining() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public final void zza(int i, double d) throws IOException {
        zzt(i, 1);
        zzaQ(Double.doubleToLongBits(d));
    }

    public final void zza(int i, long j) throws IOException {
        zzt(i, 0);
        zzaO(j);
    }

    public final void zza(int i, aif aifVar) throws IOException {
        zzt(i, 2);
        zzc(aifVar);
    }

    public final void zzb(int i, long j) throws IOException {
        zzt(i, 0);
        zzaO(j);
    }

    public final void zzb(int i, byte[] bArr) throws IOException {
        zzt(i, 2);
        zzct(bArr.length);
        zzL(bArr);
    }

    public final void zzc(int i, float f) throws IOException {
        zzt(i, 5);
        int iFloatToIntBits = Float.floatToIntBits(f);
        if (this.zzcuV.remaining() < 4) {
            throw new ahy(this.zzcuV.position(), this.zzcuV.limit());
        }
        this.zzcuV.putInt(iFloatToIntBits);
    }

    public final void zzc(int i, long j) throws IOException {
        zzt(i, 1);
        zzaQ(j);
    }

    public final void zzc(aif aifVar) throws IOException {
        zzct(aifVar.zzMk());
        aifVar.zza(this);
    }

    public final void zzct(int i) throws IOException {
        while ((i & (-128)) != 0) {
            zzcr((i & 127) | 128);
            i >>>= 7;
        }
        zzcr(i);
    }

    public final void zzd(int i, long j) throws IOException {
        zzt(i, 0);
        zzaO(zzaR(j));
    }

    public final void zzk(int i, boolean z) throws IOException {
        zzt(i, 0);
        byte b = (byte) (z ? 1 : 0);
        if (!this.zzcuV.hasRemaining()) {
            throw new ahy(this.zzcuV.position(), this.zzcuV.limit());
        }
        this.zzcuV.put(b);
    }

    public final void zzl(int i, String str) throws IOException {
        zzt(i, 2);
        try {
            int iZzcu = zzcu(str.length());
            if (iZzcu != zzcu(str.length() * 3)) {
                zzct(zzb(str));
                zza(str, this.zzcuV);
                return;
            }
            int iPosition = this.zzcuV.position();
            if (this.zzcuV.remaining() < iZzcu) {
                throw new ahy(iZzcu + iPosition, this.zzcuV.limit());
            }
            this.zzcuV.position(iPosition + iZzcu);
            zza(str, this.zzcuV);
            int iPosition2 = this.zzcuV.position();
            this.zzcuV.position(iPosition);
            zzct((iPosition2 - iPosition) - iZzcu);
            this.zzcuV.position(iPosition2);
        } catch (BufferOverflowException e) {
            ahy ahyVar = new ahy(this.zzcuV.position(), this.zzcuV.limit());
            ahyVar.initCause(e);
            throw ahyVar;
        }
    }

    public final void zzr(int i, int i2) throws IOException {
        zzt(i, 0);
        if (i2 >= 0) {
            zzct(i2);
        } else {
            zzaO(i2);
        }
    }

    public final void zzt(int i, int i2) throws IOException {
        zzct((i << 3) | i2);
    }
}
