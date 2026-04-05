package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;

/* JADX INFO: loaded from: classes67.dex */
public final class zza extends zzan {
    private int zzaGI;

    public static Account zza(zzam zzamVar) {
        Account account = null;
        if (zzamVar != null) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                account = zzamVar.getAccount();
            } catch (RemoteException e) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return account;
    }

    public final boolean equals(Object obj) {
        Account account = null;
        if (this == obj) {
            return true;
        }
        if (obj instanceof zza) {
            return account.equals(null);
        }
        return false;
    }

    @Override // com.google.android.gms.common.internal.zzam
    public final Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid != this.zzaGI) {
            if (!com.google.android.gms.common.zzo.zzf(null, callingUid)) {
                throw new SecurityException("Caller is not GooglePlayServices");
            }
            this.zzaGI = callingUid;
        }
        return null;
    }
}
