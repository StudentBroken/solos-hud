package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataRequest;

/* JADX INFO: loaded from: classes6.dex */
public interface zzdn extends IInterface {
    void zza(zzdi zzdiVar) throws RemoteException;

    void zza(zzdi zzdiVar, int i) throws RemoteException;

    void zza(zzdi zzdiVar, Uri uri) throws RemoteException;

    void zza(zzdi zzdiVar, Uri uri, int i) throws RemoteException;

    void zza(zzdi zzdiVar, Asset asset) throws RemoteException;

    void zza(zzdi zzdiVar, PutDataRequest putDataRequest) throws RemoteException;

    void zza(zzdi zzdiVar, zzd zzdVar) throws RemoteException;

    void zza(zzdi zzdiVar, zzdg zzdgVar, String str) throws RemoteException;

    void zza(zzdi zzdiVar, zzeo zzeoVar) throws RemoteException;

    void zza(zzdi zzdiVar, String str) throws RemoteException;

    void zza(zzdi zzdiVar, String str, int i) throws RemoteException;

    void zza(zzdi zzdiVar, String str, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void zza(zzdi zzdiVar, String str, ParcelFileDescriptor parcelFileDescriptor, long j, long j2) throws RemoteException;

    void zza(zzdi zzdiVar, String str, String str2) throws RemoteException;

    void zza(zzdi zzdiVar, String str, String str2, byte[] bArr) throws RemoteException;

    void zzb(zzdi zzdiVar) throws RemoteException;

    void zzb(zzdi zzdiVar, Uri uri, int i) throws RemoteException;

    void zzb(zzdi zzdiVar, zzdg zzdgVar, String str) throws RemoteException;

    void zzb(zzdi zzdiVar, String str) throws RemoteException;

    void zzb(zzdi zzdiVar, String str, int i) throws RemoteException;

    void zzc(zzdi zzdiVar) throws RemoteException;

    void zzc(zzdi zzdiVar, String str) throws RemoteException;
}
