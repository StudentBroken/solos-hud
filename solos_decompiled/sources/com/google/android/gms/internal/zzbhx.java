package com.google.android.gms.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Iterator;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbhx extends zzbhu implements SafeParcelable {
    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!getClass().isInstance(obj)) {
            return false;
        }
        zzbhu zzbhuVar = (zzbhu) obj;
        for (zzbhv<?, ?> zzbhvVar : zzrK().values()) {
            if (zza(zzbhvVar)) {
                if (zzbhuVar.zza(zzbhvVar) && zzb(zzbhvVar).equals(zzbhuVar.zzb(zzbhvVar))) {
                }
                return false;
            }
            if (zzbhuVar.zza(zzbhvVar)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int iHashCode = 0;
        Iterator<zzbhv<?, ?>> it = zzrK().values().iterator();
        while (true) {
            int i = iHashCode;
            if (!it.hasNext()) {
                return i;
            }
            zzbhv<?, ?> next = it.next();
            if (zza(next)) {
                iHashCode = zzb(next).hashCode() + (i * 31);
            } else {
                iHashCode = i;
            }
        }
    }

    @Override // com.google.android.gms.internal.zzbhu
    public Object zzcH(String str) {
        return null;
    }

    @Override // com.google.android.gms.internal.zzbhu
    public boolean zzcI(String str) {
        return false;
    }
}
