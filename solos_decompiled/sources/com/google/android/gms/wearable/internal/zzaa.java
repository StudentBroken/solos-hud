package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes6.dex */
public final class zzaa extends com.google.android.gms.common.internal.safeparcel.zza implements CapabilityInfo {
    public static final Parcelable.Creator<zzaa> CREATOR = new zzab();
    private final String mName;
    private final List<zzeg> zzbSf;
    private final Object mLock = new Object();
    private Set<Node> zzbSc = null;

    public zzaa(String str, List<zzeg> list) {
        this.mName = str;
        this.zzbSf = list;
        com.google.android.gms.common.internal.zzbr.zzu(this.mName);
        com.google.android.gms.common.internal.zzbr.zzu(this.zzbSf);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzaa zzaaVar = (zzaa) obj;
        if (this.mName == null ? zzaaVar.mName != null : !this.mName.equals(zzaaVar.mName)) {
            return false;
        }
        if (this.zzbSf != null) {
            if (this.zzbSf.equals(zzaaVar.zzbSf)) {
                return true;
            }
        } else if (zzaaVar.zzbSf == null) {
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.wearable.CapabilityInfo
    public final String getName() {
        return this.mName;
    }

    @Override // com.google.android.gms.wearable.CapabilityInfo
    public final Set<Node> getNodes() {
        Set<Node> set;
        synchronized (this.mLock) {
            if (this.zzbSc == null) {
                this.zzbSc = new HashSet(this.zzbSf);
            }
            set = this.zzbSc;
        }
        return set;
    }

    public final int hashCode() {
        return (((this.mName != null ? this.mName.hashCode() : 0) + 31) * 31) + (this.zzbSf != null ? this.zzbSf.hashCode() : 0);
    }

    public final String toString() {
        String str = this.mName;
        String strValueOf = String.valueOf(this.zzbSf);
        return new StringBuilder(String.valueOf(str).length() + 18 + String.valueOf(strValueOf).length()).append("CapabilityInfo{").append(str).append(", ").append(strValueOf).append("}").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, getName(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzbSf, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
