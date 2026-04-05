package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;

/* JADX INFO: loaded from: classes10.dex */
public final class zzk extends zzed implements IMapViewDelegate {
    zzk(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IMapViewDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final IGoogleMapDelegate getMap() throws RemoteException {
        IGoogleMapDelegate zzgVar;
        Parcel parcelZza = zza(1, zzY());
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzgVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IGoogleMapDelegate");
            zzgVar = iInterfaceQueryLocalInterface instanceof IGoogleMapDelegate ? (IGoogleMapDelegate) iInterfaceQueryLocalInterface : new zzg(strongBinder);
        }
        parcelZza.recycle();
        return zzgVar;
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void getMapAsync(zzap zzapVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzapVar);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final IObjectWrapper getView() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onCreate(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onDestroy() throws RemoteException {
        zzb(5, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onEnterAmbient(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(10, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onExitAmbient() throws RemoteException {
        zzb(11, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onLowMemory() throws RemoteException {
        zzb(6, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onPause() throws RemoteException {
        zzb(4, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onResume() throws RemoteException {
        zzb(3, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onSaveInstanceState(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(7, parcelZzY);
        if (parcelZza.readInt() != 0) {
            bundle.readFromParcel(parcelZza);
        }
        parcelZza.recycle();
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onStart() throws RemoteException {
        zzb(12, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapViewDelegate
    public final void onStop() throws RemoteException {
        zzb(13, zzY());
    }
}
