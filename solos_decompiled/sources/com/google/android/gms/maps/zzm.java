package com.google.android.gms.maps;

import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* JADX INFO: loaded from: classes10.dex */
final class zzm implements LocationSource.OnLocationChangedListener {
    private /* synthetic */ com.google.android.gms.maps.internal.zzah zzblP;

    zzm(zzl zzlVar, com.google.android.gms.maps.internal.zzah zzahVar) {
        this.zzblP = zzahVar;
    }

    @Override // com.google.android.gms.maps.LocationSource.OnLocationChangedListener
    public final void onLocationChanged(Location location) {
        try {
            this.zzblP.zzd(location);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
