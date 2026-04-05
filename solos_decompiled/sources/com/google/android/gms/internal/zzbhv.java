package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbj;
import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbhv<I, O> extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final zzbhy CREATOR = new zzbhy();
    protected final int zzaIJ;
    protected final boolean zzaIK;
    protected final int zzaIL;
    protected final boolean zzaIM;
    protected final String zzaIN;
    protected final int zzaIO;
    protected final Class<? extends zzbhu> zzaIP;
    private String zzaIQ;
    private zzbia zzaIR;
    private zzbhw<I, O> zzaIS;
    private final int zzakw;

    zzbhv(int i, int i2, boolean z, int i3, boolean z2, String str, int i4, String str2, zzbho zzbhoVar) {
        this.zzakw = i;
        this.zzaIJ = i2;
        this.zzaIK = z;
        this.zzaIL = i3;
        this.zzaIM = z2;
        this.zzaIN = str;
        this.zzaIO = i4;
        if (str2 == null) {
            this.zzaIP = null;
            this.zzaIQ = null;
        } else {
            this.zzaIP = zzbif.class;
            this.zzaIQ = str2;
        }
        if (zzbhoVar == null) {
            this.zzaIS = null;
        } else {
            this.zzaIS = (zzbhw<I, O>) zzbhoVar.zzrJ();
        }
    }

    private zzbhv(int i, boolean z, int i2, boolean z2, String str, int i3, Class<? extends zzbhu> cls, zzbhw<I, O> zzbhwVar) {
        this.zzakw = 1;
        this.zzaIJ = i;
        this.zzaIK = z;
        this.zzaIL = i2;
        this.zzaIM = z2;
        this.zzaIN = str;
        this.zzaIO = i3;
        this.zzaIP = cls;
        if (cls == null) {
            this.zzaIQ = null;
        } else {
            this.zzaIQ = cls.getCanonicalName();
        }
        this.zzaIS = zzbhwVar;
    }

    public static zzbhv zza(String str, int i, zzbhw<?, ?> zzbhwVar, boolean z) {
        return new zzbhv(7, false, 0, false, str, i, null, zzbhwVar);
    }

    public static <T extends zzbhu> zzbhv<T, T> zza(String str, int i, Class<T> cls) {
        return new zzbhv<>(11, false, 11, false, str, i, cls, null);
    }

    public static <T extends zzbhu> zzbhv<ArrayList<T>, ArrayList<T>> zzb(String str, int i, Class<T> cls) {
        return new zzbhv<>(11, true, 11, true, str, i, cls, null);
    }

    public static zzbhv<Integer, Integer> zzj(String str, int i) {
        return new zzbhv<>(0, false, 0, false, str, i, null, null);
    }

    public static zzbhv<Boolean, Boolean> zzk(String str, int i) {
        return new zzbhv<>(6, false, 6, false, str, i, null, null);
    }

    public static zzbhv<String, String> zzl(String str, int i) {
        return new zzbhv<>(7, false, 7, false, str, i, null, null);
    }

    private String zzrM() {
        if (this.zzaIQ == null) {
            return null;
        }
        return this.zzaIQ;
    }

    public final I convertBack(O o) {
        return this.zzaIS.convertBack(o);
    }

    public final String toString() {
        zzbj zzbjVarZzg = zzbh.zzt(this).zzg("versionCode", Integer.valueOf(this.zzakw)).zzg("typeIn", Integer.valueOf(this.zzaIJ)).zzg("typeInArray", Boolean.valueOf(this.zzaIK)).zzg("typeOut", Integer.valueOf(this.zzaIL)).zzg("typeOutArray", Boolean.valueOf(this.zzaIM)).zzg("outputFieldName", this.zzaIN).zzg("safeParcelFieldId", Integer.valueOf(this.zzaIO)).zzg("concreteTypeName", zzrM());
        Class<? extends zzbhu> cls = this.zzaIP;
        if (cls != null) {
            zzbjVarZzg.zzg("concreteType.class", cls.getCanonicalName());
        }
        if (this.zzaIS != null) {
            zzbjVarZzg.zzg("converterName", this.zzaIS.getClass().getCanonicalName());
        }
        return zzbjVarZzg.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.zzaIJ);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzaIK);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, this.zzaIL);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzaIM);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzaIN, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 7, this.zzaIO);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, zzrM(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, (Parcelable) (this.zzaIS == null ? null : zzbho.zza(this.zzaIS)), i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final void zza(zzbia zzbiaVar) {
        this.zzaIR = zzbiaVar;
    }

    public final int zzrL() {
        return this.zzaIO;
    }

    public final boolean zzrN() {
        return this.zzaIS != null;
    }

    public final Map<String, zzbhv<?, ?>> zzrO() {
        zzbr.zzu(this.zzaIQ);
        zzbr.zzu(this.zzaIR);
        return this.zzaIR.zzcJ(this.zzaIQ);
    }
}
