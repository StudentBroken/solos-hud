package com.google.android.gms.maps.model.internal;

import android.os.IInterface;
import android.os.RemoteException;

/* JADX INFO: loaded from: classes10.dex */
public interface zzm extends IInterface {
    void activate() throws RemoteException;

    String getName() throws RemoteException;

    String getShortName() throws RemoteException;

    int hashCodeRemote() throws RemoteException;

    boolean zza(zzm zzmVar) throws RemoteException;
}
