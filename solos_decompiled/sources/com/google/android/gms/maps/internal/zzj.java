package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.GoogleMapOptions;

/* JADX INFO: loaded from: classes10.dex */
public final class zzj extends zzed implements IMapFragmentDelegate {
    zzj(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IMapFragmentDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
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

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void getMapAsync(zzap zzapVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzapVar);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final boolean isReady() throws RemoteException {
        Parcel parcelZza = zza(11, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onCreate(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final IObjectWrapper onCreateView(IObjectWrapper iObjectWrapper, IObjectWrapper iObjectWrapper2, Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, iObjectWrapper2);
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(4, parcelZzY);
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onDestroy() throws RemoteException {
        zzb(8, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onDestroyView() throws RemoteException {
        zzb(7, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onEnterAmbient(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(13, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onExitAmbient() throws RemoteException {
        zzb(14, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onInflate(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions, Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, googleMapOptions);
        zzef.zza(parcelZzY, bundle);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onLowMemory() throws RemoteException {
        zzb(9, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onPause() throws RemoteException {
        zzb(6, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onResume() throws RemoteException {
        zzb(5, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onSaveInstanceState(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(10, parcelZzY);
        if (parcelZza.readInt() != 0) {
            bundle.readFromParcel(parcelZza);
        }
        parcelZza.recycle();
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onStart() throws RemoteException {
        zzb(15, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IMapFragmentDelegate
    public final void onStop() throws RemoteException {
        zzb(16, zzY());
    }
}
