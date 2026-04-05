package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;

/* JADX INFO: loaded from: classes67.dex */
public final class zzz implements Parcelable.Creator<zzy> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzy createFromParcel(Parcel parcel) {
        int iZzg = 0;
        com.google.android.gms.common.zzc[] zzcVarArr = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        Account account = null;
        Bundle bundleZzs = null;
        Scope[] scopeArr = null;
        IBinder iBinderZzr = null;
        String strZzq = null;
        int iZzg2 = 0;
        int iZzg3 = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg3 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 4:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 5:
                    iBinderZzr = com.google.android.gms.common.internal.safeparcel.zzb.zzr(parcel, i);
                    break;
                case 6:
                    scopeArr = (Scope[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, Scope.CREATOR);
                    break;
                case 7:
                    bundleZzs = com.google.android.gms.common.internal.safeparcel.zzb.zzs(parcel, i);
                    break;
                case 8:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Account.CREATOR);
                    break;
                case 9:
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
                case 10:
                    zzcVarArr = (com.google.android.gms.common.zzc[]) com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i, com.google.android.gms.common.zzc.CREATOR);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzy(iZzg3, iZzg2, iZzg, strZzq, iBinderZzr, scopeArr, bundleZzs, account, zzcVarArr);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzy[] newArray(int i) {
        return new zzy[i];
    }
}
