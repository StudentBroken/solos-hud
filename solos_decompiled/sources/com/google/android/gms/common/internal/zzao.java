package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes67.dex */
public final class zzao extends zzed implements zzam {
    zzao(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IAccountAccessor");
    }

    @Override // com.google.android.gms.common.internal.zzam
    public final Account getAccount() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        Account account = (Account) zzef.zza(parcelZza, Account.CREATOR);
        parcelZza.recycle();
        return account;
    }
}
