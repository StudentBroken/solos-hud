package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.internal.zzn;
import com.google.android.gms.common.api.Scope;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public final class zzd implements Parcelable.Creator<GoogleSignInOptions> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleSignInOptions createFromParcel(Parcel parcel) {
        boolean zZzc = false;
        ArrayList arrayListZzc = null;
        int iZzd = com.google.android.gms.common.internal.safeparcel.zzb.zzd(parcel);
        String strZzq = null;
        String strZzq2 = null;
        boolean zZzc2 = false;
        boolean zZzc3 = false;
        Account account = null;
        ArrayList arrayListZzc2 = null;
        int iZzg = 0;
        while (parcel.dataPosition() < iZzd) {
            int i = parcel.readInt();
            switch (65535 & i) {
                case 1:
                    iZzg = com.google.android.gms.common.internal.safeparcel.zzb.zzg(parcel, i);
                    break;
                case 2:
                    arrayListZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, Scope.CREATOR);
                    break;
                case 3:
                    account = (Account) com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, i, Account.CREATOR);
                    break;
                case 4:
                    zZzc3 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 5:
                    zZzc2 = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 6:
                    zZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i);
                    break;
                case 7:
                    strZzq2 = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 8:
                    strZzq = com.google.android.gms.common.internal.safeparcel.zzb.zzq(parcel, i);
                    break;
                case 9:
                    arrayListZzc = com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, i, zzn.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, i);
                    break;
            }
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzF(parcel, iZzd);
        return new GoogleSignInOptions(iZzg, (ArrayList<Scope>) arrayListZzc2, account, zZzc3, zZzc2, zZzc, strZzq2, strZzq, (ArrayList<zzn>) arrayListZzc);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ GoogleSignInOptions[] newArray(int i) {
        return new GoogleSignInOptions[i];
    }
}
