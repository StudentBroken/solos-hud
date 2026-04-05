package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;

/* JADX INFO: loaded from: classes6.dex */
public abstract class zzdl extends com.google.android.gms.internal.zzee implements zzdk {
    public zzdl() {
        attachInterface(this, "com.google.android.gms.wearable.internal.IWearableListener");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                zzS((DataHolder) com.google.android.gms.internal.zzef.zza(parcel, DataHolder.CREATOR));
                break;
            case 2:
                zza((zzdx) com.google.android.gms.internal.zzef.zza(parcel, zzdx.CREATOR));
                break;
            case 3:
                zza((zzeg) com.google.android.gms.internal.zzef.zza(parcel, zzeg.CREATOR));
                break;
            case 4:
                zzb((zzeg) com.google.android.gms.internal.zzef.zza(parcel, zzeg.CREATOR));
                break;
            case 5:
                onConnectedNodes(parcel.createTypedArrayList(zzeg.CREATOR));
                break;
            case 6:
                zza((zzl) com.google.android.gms.internal.zzef.zza(parcel, zzl.CREATOR));
                break;
            case 7:
                zza((zzai) com.google.android.gms.internal.zzef.zza(parcel, zzai.CREATOR));
                break;
            case 8:
                zza((zzaa) com.google.android.gms.internal.zzef.zza(parcel, zzaa.CREATOR));
                break;
            case 9:
                zza((zzi) com.google.android.gms.internal.zzef.zza(parcel, zzi.CREATOR));
                break;
            default:
                return false;
        }
        return true;
    }
}
