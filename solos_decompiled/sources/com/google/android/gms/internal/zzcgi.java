package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Iterator;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgi extends com.google.android.gms.common.internal.safeparcel.zza implements Iterable<String> {
    public static final Parcelable.Creator<zzcgi> CREATOR = new zzcgk();
    private final Bundle zzbpN;

    zzcgi(Bundle bundle) {
        this.zzbpN = bundle;
    }

    final Object get(String str) {
        return this.zzbpN.get(str);
    }

    @Override // java.lang.Iterable
    public final Iterator<String> iterator() {
        return new zzcgj(this);
    }

    public final int size() {
        return this.zzbpN.size();
    }

    public final String toString() {
        return this.zzbpN.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, zzyr(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final Bundle zzyr() {
        return new Bundle(this.zzbpN);
    }
}
