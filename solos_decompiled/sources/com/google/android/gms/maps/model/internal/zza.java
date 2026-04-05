package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

/* JADX INFO: loaded from: classes10.dex */
public interface zza extends IInterface {
    IObjectWrapper zzbn(int i) throws RemoteException;

    IObjectWrapper zzd(Bitmap bitmap) throws RemoteException;

    IObjectWrapper zzdD(String str) throws RemoteException;

    IObjectWrapper zzdE(String str) throws RemoteException;

    IObjectWrapper zzdF(String str) throws RemoteException;

    IObjectWrapper zze(float f) throws RemoteException;

    IObjectWrapper zzwk() throws RemoteException;
}
