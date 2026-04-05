package com.google.android.gms.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzbs;

/* JADX INFO: loaded from: classes3.dex */
public final class zzcvg extends com.google.android.gms.common.internal.zzaa<zzcve> implements zzcuw {
    private final com.google.android.gms.common.internal.zzq zzaCC;
    private Integer zzaHp;
    private final Bundle zzbCP;
    private final boolean zzbCX;

    public zzcvg(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzq zzqVar, Bundle bundle, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, zzqVar, connectionCallbacks, onConnectionFailedListener);
        this.zzbCX = z;
        this.zzaCC = zzqVar;
        this.zzbCP = bundle;
        this.zzaHp = zzqVar.zzrs();
    }

    public zzcvg(Context context, Looper looper, boolean z, com.google.android.gms.common.internal.zzq zzqVar, zzcux zzcuxVar, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, true, zzqVar, zza(zzqVar), connectionCallbacks, onConnectionFailedListener);
    }

    public static Bundle zza(com.google.android.gms.common.internal.zzq zzqVar) {
        zzcux zzcuxVarZzrr = zzqVar.zzrr();
        Integer numZzrs = zzqVar.zzrs();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", zzqVar.getAccount());
        if (numZzrs != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", numZzrs.intValue());
        }
        if (zzcuxVarZzrr != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzcuxVarZzrr.zzAp());
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzcuxVarZzrr.isIdTokenRequested());
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzcuxVarZzrr.getServerClientId());
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzcuxVarZzrr.zzAq());
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", zzcuxVarZzrr.zzAr());
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", zzcuxVarZzrr.zzAs());
            if (zzcuxVarZzrr.zzAt() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.authApiSignInModuleVersion", zzcuxVarZzrr.zzAt().longValue());
            }
            if (zzcuxVarZzrr.zzAu() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.realClientLibraryVersion", zzcuxVarZzrr.zzAu().longValue());
            }
        }
        return bundle;
    }

    @Override // com.google.android.gms.internal.zzcuw
    public final void connect() {
        zza(new com.google.android.gms.common.internal.zzm(this));
    }

    @Override // com.google.android.gms.internal.zzcuw
    public final void zzAo() {
        try {
            ((zzcve) zzrd()).zzbu(this.zzaHp.intValue());
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }

    @Override // com.google.android.gms.internal.zzcuw
    public final void zza(com.google.android.gms.common.internal.zzam zzamVar, boolean z) {
        try {
            ((zzcve) zzrd()).zza(zzamVar, this.zzaHp.intValue(), z);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    @Override // com.google.android.gms.internal.zzcuw
    public final void zza(zzcvc zzcvcVar) {
        zzbr.zzb(zzcvcVar, "Expecting a valid ISignInCallbacks");
        try {
            Account accountZzrj = this.zzaCC.zzrj();
            ((zzcve) zzrd()).zza(new zzcvh(new zzbs(accountZzrj, this.zzaHp.intValue(), "<<default account>>".equals(accountZzrj.name) ? com.google.android.gms.auth.api.signin.internal.zzy.zzaj(getContext()).zzmL() : null)), zzcvcVar);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                zzcvcVar.zzb(new zzcvj(8));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
        return iInterfaceQueryLocalInterface instanceof zzcve ? (zzcve) iInterfaceQueryLocalInterface : new zzcvf(iBinder);
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzda() {
        return "com.google.android.gms.signin.service.START";
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final String zzdb() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    @Override // com.google.android.gms.common.internal.zzd
    protected final Bundle zzmm() {
        if (!getContext().getPackageName().equals(this.zzaCC.zzro())) {
            this.zzbCP.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzaCC.zzro());
        }
        return this.zzbCP;
    }

    @Override // com.google.android.gms.common.internal.zzd, com.google.android.gms.common.api.Api.zze
    public final boolean zzmt() {
        return this.zzbCX;
    }
}
