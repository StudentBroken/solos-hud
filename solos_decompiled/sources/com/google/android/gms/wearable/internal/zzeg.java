package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.Node;

/* JADX INFO: loaded from: classes6.dex */
public final class zzeg extends com.google.android.gms.common.internal.safeparcel.zza implements Node {
    public static final Parcelable.Creator<zzeg> CREATOR = new zzeh();
    private final String zzIl;
    private final String zzalR;
    private final int zzbTc;
    private final boolean zzbTd;

    public zzeg(String str, String str2, int i, boolean z) {
        this.zzIl = str;
        this.zzalR = str2;
        this.zzbTc = i;
        this.zzbTd = z;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof zzeg) {
            return ((zzeg) obj).zzIl.equals(this.zzIl);
        }
        return false;
    }

    @Override // com.google.android.gms.wearable.Node
    public final String getDisplayName() {
        return this.zzalR;
    }

    @Override // com.google.android.gms.wearable.Node
    public final String getId() {
        return this.zzIl;
    }

    public final int hashCode() {
        return this.zzIl.hashCode();
    }

    @Override // com.google.android.gms.wearable.Node
    public final boolean isNearby() {
        return this.zzbTd;
    }

    public final String toString() {
        String str = this.zzalR;
        String str2 = this.zzIl;
        int i = this.zzbTc;
        return new StringBuilder(String.valueOf(str).length() + 45 + String.valueOf(str2).length()).append("Node{").append(str).append(", id=").append(str2).append(", hops=").append(i).append(", isNearby=").append(this.zzbTd).append("}").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, getId(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, this.zzbTc);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, isNearby());
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
