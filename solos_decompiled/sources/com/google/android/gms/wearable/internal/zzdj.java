package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.goldeni.audio.GIAudioNative;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;

/* JADX INFO: loaded from: classes6.dex */
public abstract class zzdj extends com.google.android.gms.internal.zzee implements zzdi {
    public zzdj() {
        attachInterface(this, "com.google.android.gms.wearable.internal.IWearableCallbacks");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 2:
                zza((zzcu) com.google.android.gms.internal.zzef.zza(parcel, zzcu.CREATOR));
                break;
            case 3:
                zza((zzem) com.google.android.gms.internal.zzef.zza(parcel, zzem.CREATOR));
                break;
            case 4:
                zza((zzda) com.google.android.gms.internal.zzef.zza(parcel, zzda.CREATOR));
                break;
            case 5:
                zzT((DataHolder) com.google.android.gms.internal.zzef.zza(parcel, DataHolder.CREATOR));
                break;
            case 6:
                zza((zzce) com.google.android.gms.internal.zzef.zza(parcel, zzce.CREATOR));
                break;
            case 7:
                zza((zzes) com.google.android.gms.internal.zzef.zza(parcel, zzes.CREATOR));
                break;
            case 8:
                zza((zzdc) com.google.android.gms.internal.zzef.zza(parcel, zzdc.CREATOR));
                break;
            case 9:
                zza((zzde) com.google.android.gms.internal.zzef.zza(parcel, zzde.CREATOR));
                break;
            case 10:
                zza((zzcy) com.google.android.gms.internal.zzef.zza(parcel, zzcy.CREATOR));
                break;
            case 11:
                zza((Status) com.google.android.gms.internal.zzef.zza(parcel, Status.CREATOR));
                break;
            case 12:
                zza((zzew) com.google.android.gms.internal.zzef.zza(parcel, zzew.CREATOR));
                break;
            case 13:
                zza((zzcw) com.google.android.gms.internal.zzef.zza(parcel, zzcw.CREATOR));
                break;
            case 14:
                zza((zzei) com.google.android.gms.internal.zzef.zza(parcel, zzei.CREATOR));
                break;
            case 15:
                zza((zzbf) com.google.android.gms.internal.zzef.zza(parcel, zzbf.CREATOR));
                break;
            case 16:
                zzb((zzbf) com.google.android.gms.internal.zzef.zza(parcel, zzbf.CREATOR));
                break;
            case 17:
                zza((zzck) com.google.android.gms.internal.zzef.zza(parcel, zzck.CREATOR));
                break;
            case 18:
                zza((zzcm) com.google.android.gms.internal.zzef.zza(parcel, zzcm.CREATOR));
                break;
            case 19:
                zza((zzaz) com.google.android.gms.internal.zzef.zza(parcel, zzaz.CREATOR));
                break;
            case 20:
                zza((zzbb) com.google.android.gms.internal.zzef.zza(parcel, zzbb.CREATOR));
                break;
            case 21:
            case 24:
            case 25:
            default:
                return false;
            case 22:
                zza((zzci) com.google.android.gms.internal.zzef.zza(parcel, zzci.CREATOR));
                break;
            case 23:
                zza((zzcg) com.google.android.gms.internal.zzef.zza(parcel, zzcg.CREATOR));
                break;
            case 26:
                zza((zzf) com.google.android.gms.internal.zzef.zza(parcel, zzf.CREATOR));
                break;
            case 27:
                zza((zzeq) com.google.android.gms.internal.zzef.zza(parcel, zzeq.CREATOR));
                break;
            case 28:
                zza((zzcp) com.google.android.gms.internal.zzef.zza(parcel, zzcp.CREATOR));
                break;
            case GIAudioNative.AUDIO_CURRENT_CONFIGURATION /* 29 */:
                zza((zzct) com.google.android.gms.internal.zzef.zza(parcel, zzct.CREATOR));
                break;
            case 30:
                zza((zzcr) com.google.android.gms.internal.zzef.zza(parcel, zzcr.CREATOR));
                break;
        }
        parcel2.writeNoException();
        return true;
    }
}
