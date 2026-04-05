package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.RuntimeRemoteException;

/* JADX INFO: loaded from: classes10.dex */
public class zzbx {
    private static Context zzbnd;
    private static zze zzbne;

    private static <T> T zza(ClassLoader classLoader, String str) {
        try {
            return (T) zzd(((ClassLoader) com.google.android.gms.common.internal.zzbr.zzu(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            String strValueOf = String.valueOf(str);
            throw new IllegalStateException(strValueOf.length() != 0 ? "Unable to find dynamic class ".concat(strValueOf) : new String("Unable to find dynamic class "));
        }
    }

    public static zze zzbh(Context context) throws GooglePlayServicesNotAvailableException {
        zze zzfVar;
        com.google.android.gms.common.internal.zzbr.zzu(context);
        if (zzbne != null) {
            return zzbne;
        }
        int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (iIsGooglePlayServicesAvailable) {
            case 0:
                Log.i(zzbx.class.getSimpleName(), "Making Creator dynamically");
                IBinder iBinder = (IBinder) zza(zzbi(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl");
                if (iBinder == null) {
                    zzfVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
                    zzfVar = iInterfaceQueryLocalInterface instanceof zze ? (zze) iInterfaceQueryLocalInterface : new zzf(iBinder);
                }
                zzbne = zzfVar;
                try {
                    zzbne.zzi(com.google.android.gms.dynamic.zzn.zzw(zzbi(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                    return zzbne;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            default:
                throw new GooglePlayServicesNotAvailableException(iIsGooglePlayServicesAvailable);
        }
    }

    private static Context zzbi(Context context) {
        if (zzbnd != null) {
            return zzbnd;
        }
        Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
        zzbnd = remoteContext;
        return remoteContext;
    }

    private static <T> T zzd(Class<?> cls) {
        try {
            return (T) cls.newInstance();
        } catch (IllegalAccessException e) {
            String strValueOf = String.valueOf(cls.getName());
            throw new IllegalStateException(strValueOf.length() != 0 ? "Unable to call the default constructor of ".concat(strValueOf) : new String("Unable to call the default constructor of "));
        } catch (InstantiationException e2) {
            String strValueOf2 = String.valueOf(cls.getName());
            throw new IllegalStateException(strValueOf2.length() != 0 ? "Unable to instantiate the dynamic class ".concat(strValueOf2) : new String("Unable to instantiate the dynamic class "));
        }
    }
}
