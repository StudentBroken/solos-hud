package com.google.android.gms.maps.internal;

import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.maps.model.CameraPosition;

/* JADX INFO: loaded from: classes10.dex */
public interface zzl extends IInterface {
    void onCameraChange(CameraPosition cameraPosition) throws RemoteException;
}
