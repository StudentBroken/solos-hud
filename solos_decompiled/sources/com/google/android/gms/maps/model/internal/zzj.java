package com.google.android.gms.maps.model.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public interface zzj extends IInterface {
    int getActiveLevelIndex() throws RemoteException;

    int getDefaultLevelIndex() throws RemoteException;

    List<IBinder> getLevels() throws RemoteException;

    int hashCodeRemote() throws RemoteException;

    boolean isUnderground() throws RemoteException;

    boolean zzb(zzj zzjVar) throws RemoteException;
}
