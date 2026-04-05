package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzl extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzl> CREATOR = new zzm();
    private final String mAppId;
    private int mId;
    private final String mPackageName;
    private final String zzalR;
    private final String zzaoA;
    private final String zzapU;
    private final String zzbRR;
    private final String zzbRS;
    private final byte zzbRT;
    private final byte zzbRU;
    private final byte zzbRV;
    private final byte zzbRW;

    public zzl(int i, String str, String str2, String str3, String str4, String str5, String str6, byte b, byte b2, byte b3, byte b4, String str7) {
        this.mId = i;
        this.mAppId = str;
        this.zzbRR = str2;
        this.zzapU = str3;
        this.zzaoA = str4;
        this.zzbRS = str5;
        this.zzalR = str6;
        this.zzbRT = b;
        this.zzbRU = b2;
        this.zzbRV = b3;
        this.zzbRW = b4;
        this.mPackageName = str7;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzl zzlVar = (zzl) obj;
        if (this.mId == zzlVar.mId && this.zzbRT == zzlVar.zzbRT && this.zzbRU == zzlVar.zzbRU && this.zzbRV == zzlVar.zzbRV && this.zzbRW == zzlVar.zzbRW && this.mAppId.equals(zzlVar.mAppId)) {
            if (this.zzbRR == null ? zzlVar.zzbRR != null : !this.zzbRR.equals(zzlVar.zzbRR)) {
                return false;
            }
            if (this.zzapU.equals(zzlVar.zzapU) && this.zzaoA.equals(zzlVar.zzaoA) && this.zzbRS.equals(zzlVar.zzbRS)) {
                if (this.zzalR == null ? zzlVar.zzalR != null : !this.zzalR.equals(zzlVar.zzalR)) {
                    return false;
                }
                return this.mPackageName != null ? this.mPackageName.equals(zzlVar.mPackageName) : zzlVar.mPackageName == null;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        return (((((((((((this.zzalR != null ? this.zzalR.hashCode() : 0) + (((((((((this.zzbRR != null ? this.zzbRR.hashCode() : 0) + ((((this.mId + 31) * 31) + this.mAppId.hashCode()) * 31)) * 31) + this.zzapU.hashCode()) * 31) + this.zzaoA.hashCode()) * 31) + this.zzbRS.hashCode()) * 31)) * 31) + this.zzbRT) * 31) + this.zzbRU) * 31) + this.zzbRV) * 31) + this.zzbRW) * 31) + (this.mPackageName != null ? this.mPackageName.hashCode() : 0);
    }

    public final String toString() {
        int i = this.mId;
        String str = this.mAppId;
        String str2 = this.zzbRR;
        String str3 = this.zzapU;
        String str4 = this.zzaoA;
        String str5 = this.zzbRS;
        String str6 = this.zzalR;
        byte b = this.zzbRT;
        byte b2 = this.zzbRU;
        byte b3 = this.zzbRV;
        byte b4 = this.zzbRW;
        String str7 = this.mPackageName;
        return new StringBuilder(String.valueOf(str).length() + 211 + String.valueOf(str2).length() + String.valueOf(str3).length() + String.valueOf(str4).length() + String.valueOf(str5).length() + String.valueOf(str6).length() + String.valueOf(str7).length()).append("AncsNotificationParcelable{, id=").append(i).append(", appId='").append(str).append("', dateTime='").append(str2).append("', notificationText='").append(str3).append("', title='").append(str4).append("', subtitle='").append(str5).append("', displayName='").append(str6).append("', eventId=").append((int) b).append(", eventFlags=").append((int) b2).append(", categoryId=").append((int) b3).append(", categoryCount=").append((int) b4).append(", packageName='").append(str7).append("'}").toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.mId);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, this.mAppId, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbRR, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzapU, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, this.zzaoA, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzbRS, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, this.zzalR == null ? this.mAppId : this.zzalR, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 9, this.zzbRT);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, this.zzbRU);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 11, this.zzbRV);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 12, this.zzbRW);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 13, this.mPackageName, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
