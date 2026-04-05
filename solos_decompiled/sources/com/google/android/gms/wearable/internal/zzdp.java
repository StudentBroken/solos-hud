package com.google.android.gms.wearable.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzbcl;
import com.google.android.gms.wearable.WearableStatusCodes;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes6.dex */
final class zzdp<T> {
    private final Map<T, zzga<T>> zzaWY = new HashMap();

    zzdp() {
    }

    public final void zza(zzfw zzfwVar, zzbcl<Status> zzbclVar, T t) throws RemoteException {
        synchronized (this.zzaWY) {
            zzga<T> zzgaVarRemove = this.zzaWY.remove(t);
            if (zzgaVarRemove == null) {
                zzbclVar.setResult(new Status(WearableStatusCodes.UNKNOWN_LISTENER));
            } else {
                zzgaVarRemove.clear();
                ((zzdn) zzfwVar.zzrd()).zza(new zzdr(this.zzaWY, t, zzbclVar), new zzeo(zzgaVarRemove));
            }
        }
    }

    public final void zza(zzfw zzfwVar, zzbcl<Status> zzbclVar, T t, zzga<T> zzgaVar) throws RemoteException {
        synchronized (this.zzaWY) {
            if (this.zzaWY.get(t) != null) {
                zzbclVar.setResult(new Status(WearableStatusCodes.DUPLICATE_LISTENER));
                return;
            }
            this.zzaWY.put(t, zzgaVar);
            try {
                ((zzdn) zzfwVar.zzrd()).zza(new zzdq(this.zzaWY, t, zzbclVar), new zzd(zzgaVar));
            } catch (RemoteException e) {
                this.zzaWY.remove(t);
                throw e;
            }
        }
    }

    public final void zzam(IBinder iBinder) {
        zzdn zzdoVar;
        synchronized (this.zzaWY) {
            if (iBinder == null) {
                zzdoVar = null;
            } else {
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.wearable.internal.IWearableService");
                zzdoVar = iInterfaceQueryLocalInterface instanceof zzdn ? (zzdn) iInterfaceQueryLocalInterface : new zzdo(iBinder);
            }
            zzfp zzfpVar = new zzfp();
            for (Map.Entry<T, zzga<T>> entry : this.zzaWY.entrySet()) {
                zzga<T> value = entry.getValue();
                try {
                    zzdoVar.zza(zzfpVar, new zzd(value));
                    if (Log.isLoggable("WearableClient", 2)) {
                        String strValueOf = String.valueOf(entry.getKey());
                        String strValueOf2 = String.valueOf(value);
                        Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf).length() + 27 + String.valueOf(strValueOf2).length()).append("onPostInitHandler: added: ").append(strValueOf).append("/").append(strValueOf2).toString());
                    }
                } catch (RemoteException e) {
                    String strValueOf3 = String.valueOf(entry.getKey());
                    String strValueOf4 = String.valueOf(value);
                    Log.d("WearableClient", new StringBuilder(String.valueOf(strValueOf3).length() + 32 + String.valueOf(strValueOf4).length()).append("onPostInitHandler: Didn't add: ").append(strValueOf3).append("/").append(strValueOf4).toString());
                }
            }
        }
    }
}
