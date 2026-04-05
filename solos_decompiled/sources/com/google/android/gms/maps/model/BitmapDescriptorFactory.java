package com.google.android.gms.maps.model;

import android.graphics.Bitmap;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
public final class BitmapDescriptorFactory {
    public static final float HUE_AZURE = 210.0f;
    public static final float HUE_BLUE = 240.0f;
    public static final float HUE_CYAN = 180.0f;
    public static final float HUE_GREEN = 120.0f;
    public static final float HUE_MAGENTA = 300.0f;
    public static final float HUE_ORANGE = 30.0f;
    public static final float HUE_RED = 0.0f;
    public static final float HUE_ROSE = 330.0f;
    public static final float HUE_VIOLET = 270.0f;
    public static final float HUE_YELLOW = 60.0f;
    private static com.google.android.gms.maps.model.internal.zza zzbnf;

    private BitmapDescriptorFactory() {
    }

    public static BitmapDescriptor defaultMarker() {
        try {
            return new BitmapDescriptor(zzwi().zzwk());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor defaultMarker(float f) {
        try {
            return new BitmapDescriptor(zzwi().zze(f));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromAsset(String str) {
        try {
            return new BitmapDescriptor(zzwi().zzdD(str));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromBitmap(Bitmap bitmap) {
        try {
            return new BitmapDescriptor(zzwi().zzd(bitmap));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromFile(String str) {
        try {
            return new BitmapDescriptor(zzwi().zzdE(str));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromPath(String str) {
        try {
            return new BitmapDescriptor(zzwi().zzdF(str));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static BitmapDescriptor fromResource(int i) {
        try {
            return new BitmapDescriptor(zzwi().zzbn(i));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static void zza(com.google.android.gms.maps.model.internal.zza zzaVar) {
        if (zzbnf != null) {
            return;
        }
        zzbnf = (com.google.android.gms.maps.model.internal.zza) zzbr.zzu(zzaVar);
    }

    private static com.google.android.gms.maps.model.internal.zza zzwi() {
        return (com.google.android.gms.maps.model.internal.zza) zzbr.zzb(zzbnf, "IBitmapDescriptorFactory is not initialized");
    }
}
