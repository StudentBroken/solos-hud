package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.zze;
import com.google.android.gms.common.zzo;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes67.dex */
public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static final zze zzbCK = zze.zzoU();
    private static final Object zzuI = new Object();
    private static Method zzbCL = null;

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    public static void installIfNeeded(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        zzbr.zzb(context, "Context must not be null");
        zze.zzas(context);
        Context remoteContext = zzo.getRemoteContext(context);
        if (remoteContext == null) {
            Log.e("ProviderInstaller", "Failed to get remote context");
            throw new GooglePlayServicesNotAvailableException(8);
        }
        synchronized (zzuI) {
            try {
                if (zzbCL == null) {
                    zzbCL = remoteContext.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", Context.class);
                }
                zzbCL.invoke(null, remoteContext);
            } catch (Exception e) {
                String strValueOf = String.valueOf(e.getMessage());
                Log.e("ProviderInstaller", strValueOf.length() != 0 ? "Failed to install provider: ".concat(strValueOf) : new String("Failed to install provider: "));
                throw new GooglePlayServicesNotAvailableException(8);
            }
        }
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        zzbr.zzb(context, "Context must not be null");
        zzbr.zzb(providerInstallListener, "Listener must not be null");
        zzbr.zzcz("Must be called on the UI thread");
        new zza(context, providerInstallListener).execute(new Void[0]);
    }
}
