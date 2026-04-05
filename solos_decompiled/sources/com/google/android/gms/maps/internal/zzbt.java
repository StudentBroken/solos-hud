package com.google.android.gms.maps.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
import com.google.android.gms.maps.StreetViewPanoramaOptions;

/* JADX INFO: loaded from: classes10.dex */
public final class zzbt extends zzed implements IStreetViewPanoramaFragmentDelegate {
    zzbt(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
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

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void getStreetViewPanoramaAsync(zzbn zzbnVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbnVar);
        zzb(12, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final boolean isReady() throws RemoteException {
        Parcel parcelZza = zza(11, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onCreate(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(3, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
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

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onDestroy() throws RemoteException {
        zzb(8, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onDestroyView() throws RemoteException {
        zzb(7, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onInflate(IObjectWrapper iObjectWrapper, StreetViewPanoramaOptions streetViewPanoramaOptions, Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, streetViewPanoramaOptions);
        zzef.zza(parcelZzY, bundle);
        zzb(2, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onLowMemory() throws RemoteException {
        zzb(9, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onPause() throws RemoteException {
        zzb(6, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onResume() throws RemoteException {
        zzb(5, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate
    public final void onSaveInstanceState(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(10, parcelZzY);
        if (parcelZza.readInt() != 0) {
            bundle.readFromParcel(parcelZza);
        }
        parcelZza.recycle();
    }
}
