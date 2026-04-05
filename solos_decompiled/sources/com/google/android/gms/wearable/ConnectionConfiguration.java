package com.google.android.gms.wearable;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbh;
import java.util.Arrays;

/* JADX INFO: loaded from: classes6.dex */
public class ConnectionConfiguration extends com.google.android.gms.common.internal.safeparcel.zza implements ReflectedParcelable {
    public static final Parcelable.Creator<ConnectionConfiguration> CREATOR = new zzg();
    private final String mName;
    private final int zzaMN;
    private final String zzaTp;
    private volatile boolean zzait;
    private final int zzamt;
    private final boolean zzbRd;
    private volatile String zzbRe;
    private boolean zzbRf;
    private String zzbRg;

    ConnectionConfiguration(String str, String str2, int i, int i2, boolean z, boolean z2, String str3, boolean z3, String str4) {
        this.mName = str;
        this.zzaTp = str2;
        this.zzamt = i;
        this.zzaMN = i2;
        this.zzbRd = z;
        this.zzait = z2;
        this.zzbRe = str3;
        this.zzbRf = z3;
        this.zzbRg = str4;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ConnectionConfiguration)) {
            return false;
        }
        ConnectionConfiguration connectionConfiguration = (ConnectionConfiguration) obj;
        return zzbh.equal(this.mName, connectionConfiguration.mName) && zzbh.equal(this.zzaTp, connectionConfiguration.zzaTp) && zzbh.equal(Integer.valueOf(this.zzamt), Integer.valueOf(connectionConfiguration.zzamt)) && zzbh.equal(Integer.valueOf(this.zzaMN), Integer.valueOf(connectionConfiguration.zzaMN)) && zzbh.equal(Boolean.valueOf(this.zzbRd), Boolean.valueOf(connectionConfiguration.zzbRd)) && zzbh.equal(Boolean.valueOf(this.zzbRf), Boolean.valueOf(connectionConfiguration.zzbRf));
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.mName, this.zzaTp, Integer.valueOf(this.zzamt), Integer.valueOf(this.zzaMN), Boolean.valueOf(this.zzbRd), Boolean.valueOf(this.zzbRf)});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ConnectionConfiguration[ ");
        String strValueOf = String.valueOf(this.mName);
        sb.append(strValueOf.length() != 0 ? "mName=".concat(strValueOf) : new String("mName="));
        String strValueOf2 = String.valueOf(this.zzaTp);
        sb.append(strValueOf2.length() != 0 ? ", mAddress=".concat(strValueOf2) : new String(", mAddress="));
        sb.append(new StringBuilder(19).append(", mType=").append(this.zzamt).toString());
        sb.append(new StringBuilder(19).append(", mRole=").append(this.zzaMN).toString());
        sb.append(new StringBuilder(16).append(", mEnabled=").append(this.zzbRd).toString());
        sb.append(new StringBuilder(20).append(", mIsConnected=").append(this.zzait).toString());
        String strValueOf3 = String.valueOf(this.zzbRe);
        sb.append(strValueOf3.length() != 0 ? ", mPeerNodeId=".concat(strValueOf3) : new String(", mPeerNodeId="));
        sb.append(new StringBuilder(21).append(", mBtlePriority=").append(this.zzbRf).toString());
        String strValueOf4 = String.valueOf(this.zzbRg);
        sb.append(strValueOf4.length() != 0 ? ", mNodeId=".concat(strValueOf4) : new String(", mNodeId="));
        sb.append("]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.mName, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.zzaTp, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 4, this.zzamt);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 5, this.zzaMN);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzbRd);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzait);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzbRe, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, this.zzbRf);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, this.zzbRg, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
