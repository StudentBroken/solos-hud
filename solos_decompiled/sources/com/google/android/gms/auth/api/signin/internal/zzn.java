package com.google.android.gms.auth.api.signin.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzd;

/* JADX INFO: loaded from: classes3.dex */
public final class zzn extends zza {
    public static final Parcelable.Creator<zzn> CREATOR = new zzm();
    private Bundle mBundle;
    private int versionCode;
    private int zzamt;

    zzn(int i, int i2, Bundle bundle) {
        this.versionCode = i;
        this.zzamt = i2;
        this.mBundle = bundle;
    }

    public zzn(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
        this(1, 1, googleSignInOptionsExtension.toBundle());
    }

    public final int getType() {
        return this.zzamt;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = zzd.zze(parcel);
        zzd.zzc(parcel, 1, this.versionCode);
        zzd.zzc(parcel, 2, this.zzamt);
        zzd.zza(parcel, 3, this.mBundle, false);
        zzd.zzI(parcel, iZze);
    }
}
