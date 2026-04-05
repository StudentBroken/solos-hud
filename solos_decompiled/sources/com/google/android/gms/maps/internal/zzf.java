package com.google.android.gms.maps.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.StreetViewPanoramaOptions;

/* JADX INFO: loaded from: classes10.dex */
public final class zzf extends zzed implements zze {
    zzf(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.ICreator");
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final IMapFragmentDelegate zzH(IObjectWrapper iObjectWrapper) throws RemoteException {
        IMapFragmentDelegate zzjVar;
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(2, parcelZzY);
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzjVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IMapFragmentDelegate");
            zzjVar = iInterfaceQueryLocalInterface instanceof IMapFragmentDelegate ? (IMapFragmentDelegate) iInterfaceQueryLocalInterface : new zzj(strongBinder);
        }
        parcelZza.recycle();
        return zzjVar;
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final IStreetViewPanoramaFragmentDelegate zzI(IObjectWrapper iObjectWrapper) throws RemoteException {
        IStreetViewPanoramaFragmentDelegate zzbtVar;
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        Parcel parcelZza = zza(8, parcelZzY);
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbtVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
            zzbtVar = iInterfaceQueryLocalInterface instanceof IStreetViewPanoramaFragmentDelegate ? (IStreetViewPanoramaFragmentDelegate) iInterfaceQueryLocalInterface : new zzbt(strongBinder);
        }
        parcelZza.recycle();
        return zzbtVar;
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final IMapViewDelegate zza(IObjectWrapper iObjectWrapper, GoogleMapOptions googleMapOptions) throws RemoteException {
        IMapViewDelegate zzkVar;
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, googleMapOptions);
        Parcel parcelZza = zza(3, parcelZzY);
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzkVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IMapViewDelegate");
            zzkVar = iInterfaceQueryLocalInterface instanceof IMapViewDelegate ? (IMapViewDelegate) iInterfaceQueryLocalInterface : new zzk(strongBinder);
        }
        parcelZza.recycle();
        return zzkVar;
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final IStreetViewPanoramaViewDelegate zza(IObjectWrapper iObjectWrapper, StreetViewPanoramaOptions streetViewPanoramaOptions) throws RemoteException {
        IStreetViewPanoramaViewDelegate zzbuVar;
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, streetViewPanoramaOptions);
        Parcel parcelZza = zza(7, parcelZzY);
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbuVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
            zzbuVar = iInterfaceQueryLocalInterface instanceof IStreetViewPanoramaViewDelegate ? (IStreetViewPanoramaViewDelegate) iInterfaceQueryLocalInterface : new zzbu(strongBinder);
        }
        parcelZza.recycle();
        return zzbuVar;
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final void zzi(IObjectWrapper iObjectWrapper, int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        parcelZzY.writeInt(i);
        zzb(6, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final ICameraUpdateFactoryDelegate zzwg() throws RemoteException {
        ICameraUpdateFactoryDelegate zzbVar;
        Parcel parcelZza = zza(4, zzY());
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate");
            zzbVar = iInterfaceQueryLocalInterface instanceof ICameraUpdateFactoryDelegate ? (ICameraUpdateFactoryDelegate) iInterfaceQueryLocalInterface : new zzb(strongBinder);
        }
        parcelZza.recycle();
        return zzbVar;
    }

    @Override // com.google.android.gms.maps.internal.zze
    public final com.google.android.gms.maps.model.internal.zza zzwh() throws RemoteException {
        Parcel parcelZza = zza(5, zzY());
        com.google.android.gms.maps.model.internal.zza zzaVarZzaa = com.google.android.gms.maps.model.internal.zzb.zzaa(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzaVarZzaa;
    }
}
