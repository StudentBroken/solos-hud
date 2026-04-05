package com.google.android.gms.wearable.internal;

import android.content.IntentFilter;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes6.dex */
public final class zzd extends com.google.android.gms.common.internal.safeparcel.zza {
    public static final Parcelable.Creator<zzd> CREATOR = new zze();
    private zzdk zzbRL;
    private IntentFilter[] zzbRM;
    private String zzbRN;
    private String zzbRO;

    zzd(IBinder iBinder, IntentFilter[] intentFilterArr, String str, String str2) {
        zzdk zzdmVar = null;
        if (iBinder != null) {
            if (iBinder != null) {
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableListener");
                zzdmVar = iInterfaceQueryLocalInterface instanceof zzdk ? (zzdk) iInterfaceQueryLocalInterface : new zzdm(iBinder);
            }
            this.zzbRL = zzdmVar;
        } else {
            this.zzbRL = null;
        }
        this.zzbRM = intentFilterArr;
        this.zzbRN = str;
        this.zzbRO = str2;
    }

    public zzd(zzga zzgaVar) {
        this.zzbRL = zzgaVar;
        this.zzbRM = zzgaVar.zzDV();
        this.zzbRN = zzgaVar.zzDW();
        this.zzbRO = null;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i) {
        int iZze = com.google.android.gms.common.internal.safeparcel.zzd.zze(parcel);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 2, this.zzbRL == null ? null : this.zzbRL.asBinder(), false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 3, (Parcelable[]) this.zzbRM, i, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 4, this.zzbRN, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zza(parcel, 5, this.zzbRO, false);
        com.google.android.gms.common.internal.safeparcel.zzd.zzI(parcel, iZze);
    }
}
