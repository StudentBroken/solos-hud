package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zzd {
    private static int zzG(Parcel parcel, int i) {
        parcel.writeInt((-65536) | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void zzH(Parcel parcel, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.setDataPosition(i - 4);
        parcel.writeInt(iDataPosition - i);
        parcel.setDataPosition(iDataPosition);
    }

    public static void zzI(Parcel parcel, int i) {
        zzH(parcel, i);
    }

    public static void zza(Parcel parcel, int i, byte b) {
        zzb(parcel, i, 4);
        parcel.writeInt(b);
    }

    public static void zza(Parcel parcel, int i, double d) {
        zzb(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void zza(Parcel parcel, int i, float f) {
        zzb(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void zza(Parcel parcel, int i, long j) {
        zzb(parcel, i, 8);
        parcel.writeLong(j);
    }

    public static void zza(Parcel parcel, int i, Bundle bundle, boolean z) {
        if (bundle == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeBundle(bundle);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, IBinder iBinder, boolean z) {
        if (iBinder == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeStrongBinder(iBinder);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, Parcel parcel2, boolean z) {
        if (parcel2 == null) {
            return;
        }
        int iZzG = zzG(parcel, 2);
        parcel.appendFrom(parcel2, 0, parcel2.dataSize());
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, Parcelable parcelable, int i2, boolean z) {
        if (parcelable == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
        } else {
            int iZzG = zzG(parcel, i);
            parcelable.writeToParcel(parcel, i2);
            zzH(parcel, iZzG);
        }
    }

    public static void zza(Parcel parcel, int i, Boolean bool, boolean z) {
        if (bool == null) {
            return;
        }
        zzb(parcel, 3, 4);
        parcel.writeInt(bool.booleanValue() ? 1 : 0);
    }

    public static void zza(Parcel parcel, int i, Double d, boolean z) {
        if (d == null) {
            return;
        }
        zzb(parcel, 8, 8);
        parcel.writeDouble(d.doubleValue());
    }

    public static void zza(Parcel parcel, int i, Float f, boolean z) {
        if (f == null) {
            return;
        }
        zzb(parcel, i, 4);
        parcel.writeFloat(f.floatValue());
    }

    public static void zza(Parcel parcel, int i, Integer num, boolean z) {
        if (num == null) {
            return;
        }
        zzb(parcel, i, 4);
        parcel.writeInt(num.intValue());
    }

    public static void zza(Parcel parcel, int i, Long l, boolean z) {
        if (l == null) {
            return;
        }
        zzb(parcel, i, 8);
        parcel.writeLong(l.longValue());
    }

    public static void zza(Parcel parcel, int i, String str, boolean z) {
        if (str == null) {
            if (z) {
                zzb(parcel, i, 0);
            }
        } else {
            int iZzG = zzG(parcel, i);
            parcel.writeString(str);
            zzH(parcel, iZzG);
        }
    }

    public static void zza(Parcel parcel, int i, List<Integer> list, boolean z) {
        if (list == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        int size = list.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            parcel.writeInt(list.get(i2).intValue());
        }
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, short s) {
        zzb(parcel, 3, 4);
        parcel.writeInt(s);
    }

    public static void zza(Parcel parcel, int i, boolean z) {
        zzb(parcel, i, 4);
        parcel.writeInt(z ? 1 : 0);
    }

    public static void zza(Parcel parcel, int i, byte[] bArr, boolean z) {
        if (bArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeByteArray(bArr);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, float[] fArr, boolean z) {
        if (fArr == null) {
            return;
        }
        int iZzG = zzG(parcel, 7);
        parcel.writeFloatArray(fArr);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, int[] iArr, boolean z) {
        if (iArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeIntArray(iArr);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, long[] jArr, boolean z) {
        if (jArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeLongArray(jArr);
        zzH(parcel, iZzG);
    }

    public static <T extends Parcelable> void zza(Parcel parcel, int i, T[] tArr, int i2, boolean z) {
        if (tArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeInt(tArr.length);
        for (T t : tArr) {
            if (t == null) {
                parcel.writeInt(0);
            } else {
                zza(parcel, t, i2);
            }
        }
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, String[] strArr, boolean z) {
        if (strArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeStringArray(strArr);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, boolean[] zArr, boolean z) {
        if (zArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeBooleanArray(zArr);
        zzH(parcel, iZzG);
    }

    public static void zza(Parcel parcel, int i, byte[][] bArr, boolean z) {
        if (bArr == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeInt(bArr.length);
        for (byte[] bArr2 : bArr) {
            parcel.writeByteArray(bArr2);
        }
        zzH(parcel, iZzG);
    }

    private static <T extends Parcelable> void zza(Parcel parcel, T t, int i) {
        int iDataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int iDataPosition2 = parcel.dataPosition();
        t.writeToParcel(parcel, i);
        int iDataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(iDataPosition);
        parcel.writeInt(iDataPosition3 - iDataPosition2);
        parcel.setDataPosition(iDataPosition3);
    }

    private static void zzb(Parcel parcel, int i, int i2) {
        if (i2 < 65535) {
            parcel.writeInt((i2 << 16) | i);
        } else {
            parcel.writeInt((-65536) | i);
            parcel.writeInt(i2);
        }
    }

    public static void zzb(Parcel parcel, int i, List<String> list, boolean z) {
        if (list == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeStringList(list);
        zzH(parcel, iZzG);
    }

    public static void zzc(Parcel parcel, int i, int i2) {
        zzb(parcel, i, 4);
        parcel.writeInt(i2);
    }

    public static <T extends Parcelable> void zzc(Parcel parcel, int i, List<T> list, boolean z) {
        if (list == null) {
            if (z) {
                zzb(parcel, i, 0);
                return;
            }
            return;
        }
        int iZzG = zzG(parcel, i);
        int size = list.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            T t = list.get(i2);
            if (t == null) {
                parcel.writeInt(0);
            } else {
                zza(parcel, t, 0);
            }
        }
        zzH(parcel, iZzG);
    }

    public static void zzd(Parcel parcel, int i, List list, boolean z) {
        if (list == null) {
            return;
        }
        int iZzG = zzG(parcel, i);
        parcel.writeList(list);
        zzH(parcel, iZzG);
    }

    public static int zze(Parcel parcel) {
        return zzG(parcel, 20293);
    }
}
