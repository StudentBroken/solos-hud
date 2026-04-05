package com.google.android.gms.wearable.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.common.data.DataHolder;
import java.util.List;

/* JADX INFO: loaded from: classes6.dex */
public interface zzdk extends IInterface {
    void onConnectedNodes(List<zzeg> list) throws RemoteException;

    void zzS(DataHolder dataHolder) throws RemoteException;

    void zza(zzaa zzaaVar) throws RemoteException;

    void zza(zzai zzaiVar) throws RemoteException;

    void zza(zzdx zzdxVar) throws RemoteException;

    void zza(zzeg zzegVar) throws RemoteException;

    void zza(zzi zziVar) throws RemoteException;

    void zza(zzl zzlVar) throws RemoteException;

    void zzb(zzeg zzegVar) throws RemoteException;
}
