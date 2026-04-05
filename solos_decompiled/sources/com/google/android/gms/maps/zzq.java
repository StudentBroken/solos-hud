package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
final class zzq extends zzbr {
    private /* synthetic */ GoogleMap.SnapshotReadyCallback zzblT;

    zzq(GoogleMap googleMap, GoogleMap.SnapshotReadyCallback snapshotReadyCallback) {
        this.zzblT = snapshotReadyCallback;
    }

    @Override // com.google.android.gms.maps.internal.zzbq
    public final void onSnapshotReady(Bitmap bitmap) throws RemoteException {
        this.zzblT.onSnapshotReady(bitmap);
    }

    @Override // com.google.android.gms.maps.internal.zzbq
    public final void zzG(IObjectWrapper iObjectWrapper) throws RemoteException {
        this.zzblT.onSnapshotReady((Bitmap) com.google.android.gms.dynamic.zzn.zzE(iObjectWrapper));
    }
}
