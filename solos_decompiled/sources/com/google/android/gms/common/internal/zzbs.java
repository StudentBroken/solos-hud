package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/* JADX INFO: loaded from: classes3.dex */
public final class zzbs extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzbs> CREATOR = new zzbt();
    private final int zzaIq;
    private final GoogleSignInAccount zzaIr;
    private final Account zzajd;
    private int zzakw;

    zzbs(int i, Account account, int i2, GoogleSignInAccount googleSignInAccount) {
        this.zzakw = i;
        this.zzajd = account;
        this.zzaIq = i2;
        this.zzaIr = googleSignInAccount;
    }

    public zzbs(Account account, int i, GoogleSignInAccount googleSignInAccount) {
        this(2, account, i, googleSignInAccount);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 1, this.zzakw);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, (Parcelable) this.zzajd, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzc(parcel, 3, this.zzaIq);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, (Parcelable) this.zzaIr, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
