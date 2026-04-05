package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import java.util.Iterator;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzaa<T extends IInterface> extends zzd<T> implements Api.zze, zzae {
    private final zzq zzaCC;
    private final Account zzajd;
    private final Set<Scope> zzamg;

    protected zzaa(Context context, Looper looper, int i, zzq zzqVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, zzaf.zzaC(context), GoogleApiAvailability.getInstance(), i, zzqVar, (GoogleApiClient.ConnectionCallbacks) zzbr.zzu(connectionCallbacks), (GoogleApiClient.OnConnectionFailedListener) zzbr.zzu(onConnectionFailedListener));
    }

    private zzaa(Context context, Looper looper, zzaf zzafVar, GoogleApiAvailability googleApiAvailability, int i, zzq zzqVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, zzafVar, googleApiAvailability, i, connectionCallbacks == null ? null : new zzab(connectionCallbacks), onConnectionFailedListener == null ? null : new zzac(onConnectionFailedListener), zzqVar.zzrp());
        this.zzaCC = zzqVar;
        this.zzajd = zzqVar.getAccount();
        Set<Scope> setZzrm = zzqVar.zzrm();
        Set<Scope> setZzb = zzb(setZzrm);
        Iterator<Scope> it = setZzb.iterator();
        while (it.hasNext()) {
            if (!setZzrm.contains(it.next())) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        this.zzamg = setZzb;
    }

    @Override // com.google.android.gms.common.internal.zzd
    public final Account getAccount() {
        return this.zzajd;
    }

    @NonNull
    protected Set<Scope> zzb(@NonNull Set<Scope> set) {
        return set;
    }

    @Override // com.google.android.gms.common.internal.zzd
    public com.google.android.gms.common.zzc[] zzrb() {
        return new com.google.android.gms.common.zzc[0];
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final Set<Scope> zzrf() {
        return this.zzamg;
    }

    protected final zzq zzrx() {
        return this.zzaCC;
    }
}
