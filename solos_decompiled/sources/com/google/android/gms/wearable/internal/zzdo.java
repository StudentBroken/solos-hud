package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataRequest;

/* JADX INFO: loaded from: classes6.dex */
public final class zzdo extends com.google.android.gms.internal.zzed implements zzdn {
    zzdo(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.wearable.internal.IWearableService");
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        zzb(8, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeInt(i);
        zzb(43, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, Uri uri) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, uri);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, Uri uri, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, uri);
        parcelZzY.writeInt(i);
        zzb(40, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, Asset asset) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, asset);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, PutDataRequest putDataRequest) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, putDataRequest);
        zzb(6, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, zzd zzdVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdVar);
        zzb(16, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, zzdg zzdgVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdgVar);
        parcelZzY.writeString(str);
        zzb(34, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, zzeo zzeoVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzeoVar);
        zzb(17, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        zzb(46, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        parcelZzY.writeInt(i);
        zzb(42, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        com.google.android.gms.internal.zzef.zza(parcelZzY, parcelFileDescriptor);
        zzb(38, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str, ParcelFileDescriptor parcelFileDescriptor, long j, long j2) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        com.google.android.gms.internal.zzef.zza(parcelZzY, parcelFileDescriptor);
        parcelZzY.writeLong(j);
        parcelZzY.writeLong(j2);
        zzb(39, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str, String str2) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        zzb(31, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zza(zzdi zzdiVar, String str, String str2, byte[] bArr) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        parcelZzY.writeString(str2);
        parcelZzY.writeByteArray(bArr);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzb(zzdi zzdiVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        zzb(14, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzb(zzdi zzdiVar, Uri uri, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, uri);
        parcelZzY.writeInt(i);
        zzb(41, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzb(zzdi zzdiVar, zzdg zzdgVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdgVar);
        parcelZzY.writeString(str);
        zzb(35, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzb(zzdi zzdiVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        zzb(47, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzb(zzdi zzdiVar, String str, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        parcelZzY.writeInt(i);
        zzb(33, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzc(zzdi zzdiVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        zzb(15, parcelZzY);
    }

    @Override // com.google.android.gms.wearable.internal.zzdn
    public final void zzc(zzdi zzdiVar, String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        com.google.android.gms.internal.zzef.zza(parcelZzY, zzdiVar);
        parcelZzY.writeString(str);
        zzb(32, parcelZzY);
    }
}
