package com.google.android.gms.internal;

import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

/* JADX INFO: loaded from: classes36.dex */
public interface zzcgp extends IInterface {
    List<zzcku> zza(zzcft zzcftVar, boolean z) throws RemoteException;

    List<zzcfw> zza(String str, String str2, zzcft zzcftVar) throws RemoteException;

    List<zzcku> zza(String str, String str2, String str3, boolean z) throws RemoteException;

    List<zzcku> zza(String str, String str2, boolean z, zzcft zzcftVar) throws RemoteException;

    void zza(long j, String str, String str2, String str3) throws RemoteException;

    void zza(zzcft zzcftVar) throws RemoteException;

    void zza(zzcfw zzcfwVar, zzcft zzcftVar) throws RemoteException;

    void zza(zzcgl zzcglVar, zzcft zzcftVar) throws RemoteException;

    void zza(zzcgl zzcglVar, String str, String str2) throws RemoteException;

    void zza(zzcku zzckuVar, zzcft zzcftVar) throws RemoteException;

    byte[] zza(zzcgl zzcglVar, String str) throws RemoteException;

    void zzb(zzcft zzcftVar) throws RemoteException;

    void zzb(zzcfw zzcfwVar) throws RemoteException;

    String zzc(zzcft zzcftVar) throws RemoteException;

    List<zzcfw> zzk(String str, String str2, String str3) throws RemoteException;
}
