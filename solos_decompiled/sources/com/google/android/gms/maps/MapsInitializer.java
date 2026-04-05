package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.maps.internal.zzbx;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* JADX INFO: loaded from: classes10.dex */
public final class MapsInitializer {
    private static boolean initialized = false;

    private MapsInitializer() {
    }

    public static synchronized int initialize(Context context) {
        int i = 0;
        synchronized (MapsInitializer.class) {
            zzbr.zzb(context, "Context is null");
            if (!initialized) {
                try {
                    com.google.android.gms.maps.internal.zze zzeVarZzbh = zzbx.zzbh(context);
                    try {
                        CameraUpdateFactory.zza(zzeVarZzbh.zzwg());
                        BitmapDescriptorFactory.zza(zzeVarZzbh.zzwh());
                        initialized = true;
                    } catch (RemoteException e) {
                        throw new RuntimeRemoteException(e);
                    }
                } catch (GooglePlayServicesNotAvailableException e2) {
                    i = e2.errorCode;
                }
            }
        }
        return i;
    }
}
