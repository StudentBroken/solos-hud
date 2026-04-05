package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import com.digits.sdk.vcard.VCardConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes67.dex */
public final class zzcrz extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzcrz> CREATOR = new zzcsf();
    private static byte[][] zzazk = new byte[0][];
    private static zzcrz zzbAg = new zzcrz("", null, zzazk, zzazk, zzazk, zzazk, null, null);
    private static final zzcse zzbAp = new zzcsa();
    private static final zzcse zzbAq = new zzcsb();
    private static final zzcse zzbAr = new zzcsc();
    private static final zzcse zzbAs = new zzcsd();
    private String zzbAh;
    private byte[] zzbAi;
    private byte[][] zzbAj;
    private byte[][] zzbAk;
    private byte[][] zzbAl;
    private byte[][] zzbAm;
    private int[] zzbAn;
    private byte[][] zzbAo;

    public zzcrz(String str, byte[] bArr, byte[][] bArr2, byte[][] bArr3, byte[][] bArr4, byte[][] bArr5, int[] iArr, byte[][] bArr6) {
        this.zzbAh = str;
        this.zzbAi = bArr;
        this.zzbAj = bArr2;
        this.zzbAk = bArr3;
        this.zzbAl = bArr4;
        this.zzbAm = bArr5;
        this.zzbAn = iArr;
        this.zzbAo = bArr6;
    }

    private static void zza(StringBuilder sb, String str, int[] iArr) {
        sb.append(str);
        sb.append("=");
        if (iArr == null) {
            sb.append("null");
            return;
        }
        sb.append("(");
        int length = iArr.length;
        boolean z = true;
        int i = 0;
        while (i < length) {
            int i2 = iArr[i];
            if (!z) {
                sb.append(", ");
            }
            sb.append(i2);
            i++;
            z = false;
        }
        sb.append(")");
    }

    private static void zza(StringBuilder sb, String str, byte[][] bArr) {
        sb.append(str);
        sb.append("=");
        if (bArr == null) {
            sb.append("null");
            return;
        }
        sb.append("(");
        int length = bArr.length;
        boolean z = true;
        int i = 0;
        while (i < length) {
            byte[] bArr2 = bArr[i];
            if (!z) {
                sb.append(", ");
            }
            sb.append("'");
            sb.append(Base64.encodeToString(bArr2, 3));
            sb.append("'");
            i++;
            z = false;
        }
        sb.append(")");
    }

    private static List<String> zzb(byte[][] bArr) {
        if (bArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(bArr.length);
        for (byte[] bArr2 : bArr) {
            arrayList.add(Base64.encodeToString(bArr2, 3));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static List<Integer> zzc(int[] iArr) {
        if (iArr == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(iArr.length);
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzcrz)) {
            return false;
        }
        zzcrz zzcrzVar = (zzcrz) obj;
        return zzcsg.equals(this.zzbAh, zzcrzVar.zzbAh) && Arrays.equals(this.zzbAi, zzcrzVar.zzbAi) && zzcsg.equals(zzb(this.zzbAj), zzb(zzcrzVar.zzbAj)) && zzcsg.equals(zzb(this.zzbAk), zzb(zzcrzVar.zzbAk)) && zzcsg.equals(zzb(this.zzbAl), zzb(zzcrzVar.zzbAl)) && zzcsg.equals(zzb(this.zzbAm), zzb(zzcrzVar.zzbAm)) && zzcsg.equals(zzc(this.zzbAn), zzc(zzcrzVar.zzbAn)) && zzcsg.equals(zzb(this.zzbAo), zzb(zzcrzVar.zzbAo));
    }

    public final String toString() {
        String string;
        StringBuilder sb = new StringBuilder("ExperimentTokens");
        sb.append("(");
        if (this.zzbAh == null) {
            string = "null";
        } else {
            String strValueOf = String.valueOf("'");
            String str = this.zzbAh;
            String strValueOf2 = String.valueOf("'");
            string = new StringBuilder(String.valueOf(strValueOf).length() + String.valueOf(str).length() + String.valueOf(strValueOf2).length()).append(strValueOf).append(str).append(strValueOf2).toString();
        }
        sb.append(string);
        sb.append(", ");
        byte[] bArr = this.zzbAi;
        sb.append("direct");
        sb.append("=");
        if (bArr == null) {
            sb.append("null");
        } else {
            sb.append("'");
            sb.append(Base64.encodeToString(bArr, 3));
            sb.append("'");
        }
        sb.append(", ");
        zza(sb, "GAIA", this.zzbAj);
        sb.append(", ");
        zza(sb, "PSEUDO", this.zzbAk);
        sb.append(", ");
        zza(sb, "ALWAYS", this.zzbAl);
        sb.append(", ");
        zza(sb, VCardConstants.PARAM_PHONE_EXTRA_TYPE_OTHER, this.zzbAm);
        sb.append(", ");
        zza(sb, "weak", this.zzbAn);
        sb.append(", ");
        zza(sb, "directs", this.zzbAo);
        sb.append(")");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbAh, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzbAi, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbAj, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzbAk, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzbAl, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzbAm, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzbAn, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, this.zzbAo, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
