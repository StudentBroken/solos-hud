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
public final class zzbu extends zzed implements IStreetViewPanoramaViewDelegate {
    zzbu(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final IStreetViewPanoramaDelegate getStreetViewPanorama() throws RemoteException {
        IStreetViewPanoramaDelegate zzbsVar;
        Parcel parcelZza = zza(1, zzY());
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbsVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate");
            zzbsVar = iInterfaceQueryLocalInterface instanceof IStreetViewPanoramaDelegate ? (IStreetViewPanoramaDelegate) iInterfaceQueryLocalInterface : new zzbs(strongBinder);
        }
        parcelZza.recycle();
        return zzbsVar;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void getStreetViewPanoramaAsync(zzbn zzbnVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbnVar);
        zzb(9, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final IObjectWrapper getView() throws RemoteException {
        Parcel parcelZza = zza(8, zzY());
        IObjectWrapper iObjectWrapperZzM = IObjectWrapper.zza.zzM(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iObjectWrapperZzM;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onCreate(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onDestroy() throws RemoteException {
        zzb(5, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onLowMemory() throws RemoteException {
        zzb(6, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onPause() throws RemoteException {
        zzb(4, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onResume() throws RemoteException {
        zzb(3, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate
    public final void onSaveInstanceState(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(7, parcelZzY);
        if (parcelZza.readInt() != 0) {
            bundle.readFromParcel(parcelZza);
        }
        parcelZza.recycle();
    }
}
