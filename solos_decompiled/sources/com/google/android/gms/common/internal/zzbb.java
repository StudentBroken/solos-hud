package com.google.android.gms.common.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes67.dex */
public interface zzbb extends IInterface {
    boolean zza(com.google.android.gms.common.zzm zzmVar, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean zze(String str, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean zzf(String str, IObjectWrapper iObjectWrapper) throws RemoteException;

    IObjectWrapper zzrE() throws RemoteException;

    IObjectWrapper zzrF() throws RemoteException;
}
