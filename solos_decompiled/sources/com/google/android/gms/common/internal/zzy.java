package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;

/* JADX INFO: loaded from: classes67.dex */
public final class zzy extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzy> CREATOR = new zzz();
    private int version;
    Scope[] zzaHA;
    Bundle zzaHB;
    Account zzaHC;
    com.google.android.gms.common.zzc[] zzaHD;
    private int zzaHw;
    private int zzaHx;
    String zzaHy;
    IBinder zzaHz;

    public zzy(int i) {
        this.version = 3;
        this.zzaHx = com.google.android.gms.common.zze.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        this.zzaHw = i;
    }

    zzy(int i, int i2, int i3, String str, IBinder iBinder, Scope[] scopeArr, Bundle bundle, Account account, com.google.android.gms.common.zzc[] zzcVarArr) {
        Account accountZza = null;
        zzam zzaoVar = null;
        this.version = i;
        this.zzaHw = i2;
        this.zzaHx = i3;
        if ("com.google.android.gms".equals(str)) {
            this.zzaHy = "com.google.android.gms";
        } else {
            this.zzaHy = str;
        }
        if (i < 2) {
            if (iBinder != null) {
                if (iBinder != null) {
                    IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IAccountAccessor");
                    zzaoVar = iInterfaceQueryLocalInterface instanceof zzam ? (zzam) iInterfaceQueryLocalInterface : new zzao(iBinder);
                }
                accountZza = zza.zza(zzaoVar);
            }
            this.zzaHC = accountZza;
        } else {
            this.zzaHz = iBinder;
            this.zzaHC = account;
        }
        this.zzaHA = scopeArr;
        this.zzaHB = bundle;
        this.zzaHD = zzcVarArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.version);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 2, this.zzaHw);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaHx);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzaHy, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzaHz, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 6, (Parcelable[]) this.zzaHA, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 7, this.zzaHB, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 8, (Parcelable) this.zzaHC, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 10, (Parcelable[]) this.zzaHD, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }

    public final Bundle zzrw() {
        return this.zzaHB;
    }
}
