package com.google.android.gms.security;

import android.content.Context;
import android.os.AsyncTask;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.zze;
import com.google.android.gms.security.ProviderInstaller;

/* JADX INFO: loaded from: classes67.dex */
final class zza extends AsyncTask<Void, Void, Integer> {
    private /* synthetic */ ProviderInstaller.ProviderInstallListener zzbCM;
    private /* synthetic */ Context zztI;

    zza(Context context, ProviderInstaller.ProviderInstallListener providerInstallListener) {
        this.zztI = context;
        this.zzbCM = providerInstallListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // android.os.AsyncTask
    /* JADX INFO: renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public final Integer doInBackground(Void... voidArr) {
        try {
            ProviderInstaller.installIfNeeded(this.zztI);
            return 0;
        } catch (GooglePlayServicesNotAvailableException e) {
            return Integer.valueOf(e.errorCode);
        } catch (GooglePlayServicesRepairableException e2) {
            return Integer.valueOf(e2.getConnectionStatusCode());
        }
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(Integer num) {
        Integer num2 = num;
        if (num2.intValue() == 0) {
            this.zzbCM.onProviderInstalled();
            return;
        }
        zze unused = ProviderInstaller.zzbCK;
        this.zzbCM.onProviderInstallFailed(num2.intValue(), zze.zza(this.zztI, num2.intValue(), "pi"));
    }
}
