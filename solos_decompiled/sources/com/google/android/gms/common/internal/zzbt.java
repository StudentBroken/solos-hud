package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbt implements Parcelable.Creator<zzbs> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbs createFromParcel(Parcel parcel) {
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        int iZzg = 0;
        Account account = null;
        int iZzg2 = 0;
        GoogleSignInAccount googleSignInAccount = null;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg2 = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Account.CREATOR);
                    break;
                case 3:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 4:
                    googleSignInAccount = (GoogleSignInAccount) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, GoogleSignInAccount.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new zzbs(iZzg2, account, iZzg, googleSignInAccount);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzbs[] newArray(int i) {
        return new zzbs[i];
    }
}
