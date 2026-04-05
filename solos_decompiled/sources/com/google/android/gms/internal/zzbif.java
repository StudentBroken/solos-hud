package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.android.gms.common.internal.zzbr;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public class zzbif extends zzbhx {
    public static final Parcelable.Creator<zzbif> CREATOR = new zzbig();
    private final String mClassName;
    private final zzbia zzaIR;
    private final Parcel zzaIY;
    private final int zzaIZ = 2;
    private int zzaJa;
    private int zzaJb;
    private final int zzakw;

    zzbif(int i, Parcel parcel, zzbia zzbiaVar) {
        this.zzakw = i;
        this.zzaIY = (Parcel) zzbr.zzu(parcel);
        this.zzaIR = zzbiaVar;
        if (this.zzaIR == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.zzaIR.zzrQ();
        }
        this.zzaJa = 2;
    }

    private static void zza(StringBuilder sb, int i, Object obj) {
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"").append(com.google.android.gms.common.util.zzq.zzcK(obj.toString())).append("\"");
                return;
            case 8:
                sb.append("\"").append(com.google.android.gms.common.util.zzd.zzg((byte[]) obj)).append("\"");
                return;
            case 9:
                sb.append("\"").append(com.google.android.gms.common.util.zzd.zzh((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                com.google.android.gms.common.util.zzr.zza(sb, (HashMap) obj);
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException(new StringBuilder(26).append("Unknown type = ").append(i).toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void zza(StringBuilder sb, zzbhv<?, ?> zzbhvVar, Parcel parcel, int i) {
        double[] dArrCreateDoubleArray = null;
        BigInteger[] bigIntegerArr = null;
        int i2 = 0;
        if (!zzbhvVar.zzaIM) {
            switch (zzbhvVar.zzaIL) {
                case 0:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i));
                    return;
                case 1:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzk(parcel, i));
                    return;
                case 2:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i));
                    return;
                case 3:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i));
                    return;
                case 4:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzn(parcel, i));
                    return;
                case 5:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzp(parcel, i));
                    return;
                case 6:
                    sb.append(com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i));
                    return;
                case 7:
                    sb.append("\"").append(com.google.android.gms.common.util.zzq.zzcK(com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i))).append("\"");
                    return;
                case 8:
                    sb.append("\"").append(com.google.android.gms.common.util.zzd.zzg(com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i))).append("\"");
                    return;
                case 9:
                    sb.append("\"").append(com.google.android.gms.common.util.zzd.zzh(com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i)));
                    sb.append("\"");
                    return;
                case 10:
                    Bundle bundleZzs = com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i);
                    Set<String> setKeySet = bundleZzs.keySet();
                    setKeySet.size();
                    sb.append("{");
                    Object[] objArr = true;
                    for (String str : setKeySet) {
                        if (objArr == false) {
                            sb.append(",");
                        }
                        sb.append("\"").append(str).append("\"");
                        sb.append(":");
                        sb.append("\"").append(com.google.android.gms.common.util.zzq.zzcK(bundleZzs.getString(str))).append("\"");
                        objArr = false;
                    }
                    sb.append("}");
                    return;
                case 11:
                    Parcel parcelZzD = com.google.android.gms.common.internal.safeparcel.zzb.zzD(parcel, i);
                    parcelZzD.setDataPosition(0);
                    zza(sb, zzbhvVar.zzrO(), parcelZzD);
                    return;
                default:
                    throw new IllegalStateException("Unknown field type out");
            }
        }
        sb.append("[");
        switch (zzbhvVar.zzaIL) {
            case 0:
                int[] iArrZzw = com.google.android.gms.common.internal.safeparcel.zzb.zzw(parcel, i);
                int length = iArrZzw.length;
                while (i2 < length) {
                    if (i2 != 0) {
                        sb.append(",");
                    }
                    sb.append(Integer.toString(iArrZzw[i2]));
                    i2++;
                }
                break;
            case 1:
                int iZza = com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i);
                int iDataPosition = parcel.dataPosition();
                if (iZza != 0) {
                    int i3 = parcel.readInt();
                    bigIntegerArr = new BigInteger[i3];
                    while (i2 < i3) {
                        bigIntegerArr[i2] = new BigInteger(parcel.createByteArray());
                        i2++;
                    }
                    parcel.setDataPosition(iZza + iDataPosition);
                }
                com.google.android.gms.common.util.zzc.zza(sb, bigIntegerArr);
                break;
            case 2:
                com.google.android.gms.common.util.zzc.zza(sb, com.google.android.gms.common.internal.safeparcel.zzb.zzx(parcel, i));
                break;
            case 3:
                com.google.android.gms.common.util.zzc.zza(sb, com.google.android.gms.common.internal.safeparcel.zzb.zzy(parcel, i));
                break;
            case 4:
                int iZza2 = com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i);
                int iDataPosition2 = parcel.dataPosition();
                if (iZza2 != 0) {
                    dArrCreateDoubleArray = parcel.createDoubleArray();
                    parcel.setDataPosition(iZza2 + iDataPosition2);
                }
                com.google.android.gms.common.util.zzc.zza(sb, dArrCreateDoubleArray);
                break;
            case 5:
                com.google.android.gms.common.util.zzc.zza(sb, com.google.android.gms.common.internal.safeparcel.zzb.zzz(parcel, i));
                break;
            case 6:
                com.google.android.gms.common.util.zzc.zza(sb, com.google.android.gms.common.internal.safeparcel.zzb.zzv(parcel, i));
                break;
            case 7:
                com.google.android.gms.common.util.zzc.zza(sb, com.google.android.gms.common.internal.safeparcel.zzb.zzA(parcel, i));
                break;
            case 8:
            case 9:
            case 10:
                throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
            case 11:
                Parcel[] parcelArrZzE = com.google.android.gms.common.internal.safeparcel.zzb.zzE(parcel, i);
                int length2 = parcelArrZzE.length;
                for (int i4 = 0; i4 < length2; i4++) {
                    if (i4 > 0) {
                        sb.append(",");
                    }
                    parcelArrZzE[i4].setDataPosition(0);
                    zza(sb, zzbhvVar.zzrO(), parcelArrZzE[i4]);
                }
                break;
            default:
                throw new IllegalStateException("Unknown field type out.");
        }
        sb.append("]");
    }

    private final void zza(StringBuilder sb, Map<String, zzbhv<?, ?>> map, Parcel parcel) {
        SparseArray sparseArray = new SparseArray();
        for (Map.Entry<String, zzbhv<?, ?>> entry : map.entrySet()) {
            sparseArray.put(entry.getValue().zzaIO, entry);
        }
        sb.append('{');
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        boolean z = false;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            Map.Entry entry2 = (Map.Entry) sparseArray.get(65535 & i);
            if (entry2 != null) {
                if (z) {
                    sb.append(",");
                }
                String str = (String) entry2.getKey();
                zzbhv<?, ?> zzbhvVar = (zzbhv) entry2.getValue();
                sb.append("\"").append(str).append("\":");
                if (zzbhvVar.zzrN()) {
                    switch (zzbhvVar.zzaIL) {
                        case 0:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, Integer.valueOf(com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i))));
                            break;
                        case 1:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, com.google.android.gms.common.internal.safeparcel.zzb.zzk(parcel, i)));
                            break;
                        case 2:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, Long.valueOf(com.google.android.gms.common.internal.safeparcel.zzb.zzi(parcel, i))));
                            break;
                        case 3:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, Float.valueOf(com.google.android.gms.common.internal.safeparcel.zzb.zzl(parcel, i))));
                            break;
                        case 4:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, Double.valueOf(com.google.android.gms.common.internal.safeparcel.zzb.zzn(parcel, i))));
                            break;
                        case 5:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, com.google.android.gms.common.internal.safeparcel.zzb.zzp(parcel, i)));
                            break;
                        case 6:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, Boolean.valueOf(com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i))));
                            break;
                        case 7:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i)));
                            break;
                        case 8:
                        case 9:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, com.google.android.gms.common.internal.safeparcel.zzb.zzt(parcel, i)));
                            break;
                        case 10:
                            zzb(sb, zzbhvVar, zza(zzbhvVar, zzo(com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i))));
                            break;
                        case 11:
                            throw new IllegalArgumentException("Method does not accept concrete type.");
                        default:
                            throw new IllegalArgumentException(new StringBuilder(36).append("Unknown field out type = ").append(zzbhvVar.zzaIL).toString());
                    }
                } else {
                    zza(sb, zzbhvVar, parcel, i);
                }
                z = true;
            }
        }
        if (parcel.dataPosition() != iZzd) {
            throw new com.google.android.gms.common.internal.safeparcel.zzc(new StringBuilder(37).append("Overread allowed size end=").append(iZzd).toString(), parcel);
        }
        sb.append('}');
    }

    private final void zzb(StringBuilder sb, zzbhv<?, ?> zzbhvVar, Object obj) {
        if (!zzbhvVar.zzaIK) {
            zza(sb, zzbhvVar.zzaIJ, obj);
            return;
        }
        ArrayList arrayList = (ArrayList) obj;
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append(",");
            }
            zza(sb, zzbhvVar.zzaIJ, arrayList.get(i));
        }
        sb.append("]");
    }

    private static HashMap<String, String> zzo(Bundle bundle) {
        HashMap<String, String> map = new HashMap<>();
        for (String str : bundle.keySet()) {
            map.put(str, bundle.getString(str));
        }
        return map;
    }

    private Parcel zzrS() {
        switch (this.zzaJa) {
            case 0:
                this.zzaJb = com.google.android.gms.common.internal.safeparcel.zzd.zze(this.zzaIY);
            case 1:
                com.google.android.gms.common.internal.safeparcel.zzd.zzI(this.zzaIY, this.zzaJb);
                this.zzaJa = 2;
                break;
        }
        return this.zzaIY;
    }

    @Override // com.google.android.gms.internal.zzbhu
    public String toString() {
        zzbr.zzb(this.zzaIR, "Cannot convert to JSON on client side.");
        Parcel parcelZzrS = zzrS();
        parcelZzrS.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        zza(sb, this.zzaIR.zzcJ(this.mClassName), parcelZzrS);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        zzbia zzbiaVar;
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, zzrS(), false);
        switch (this.zzaIZ) {
            case 0:
                zzbiaVar = null;
                break;
            case 1:
                zzbiaVar = this.zzaIR;
                break;
            case 2:
                zzbiaVar = this.zzaIR;
                break;
            default:
                throw new IllegalStateException(new StringBuilder(34).append("Invalid creation type: ").append(this.zzaIZ).toString());
        }
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable) zzbiaVar, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    @Override // com.google.android.gms.internal.zzbhx, com.google.android.gms.internal.zzbhu
    public final Object zzcH(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.internal.zzbhx, com.google.android.gms.internal.zzbhu
    public final boolean zzcI(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.internal.zzbhu
    public final Map<String, zzbhv<?, ?>> zzrK() {
        if (this.zzaIR == null) {
            return null;
        }
        return this.zzaIR.zzcJ(this.mClassName);
    }
}
