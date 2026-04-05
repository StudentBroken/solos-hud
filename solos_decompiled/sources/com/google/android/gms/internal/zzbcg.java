package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzbr;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes3.dex */
public class zzbcg extends zzbcm {
    private final SparseArray<zza> zzaBD;

    class zza implements GoogleApiClient.OnConnectionFailedListener {
        public final int zzaBE;
        public final GoogleApiClient zzaBF;
        public final GoogleApiClient.OnConnectionFailedListener zzaBG;

        public zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            this.zzaBE = i;
            this.zzaBF = googleApiClient;
            this.zzaBG = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            String strValueOf = String.valueOf(connectionResult);
            Log.d("AutoManageHelper", new StringBuilder(String.valueOf(strValueOf).length() + 27).append("beginFailureResolution for ").append(strValueOf).toString());
            zzbcg.this.zzb(connectionResult, this.zzaBE);
        }
    }

    private zzbcg(zzbff zzbffVar) {
        super(zzbffVar);
        this.zzaBD = new SparseArray<>();
        this.zzaEI.zza("AutoManageHelper", this);
    }

    public static zzbcg zza(zzbfd zzbfdVar) {
        zzbff zzbffVarZzb = zzb(zzbfdVar);
        zzbcg zzbcgVar = (zzbcg) zzbffVarZzb.zza("AutoManageHelper", zzbcg.class);
        return zzbcgVar != null ? zzbcgVar : new zzbcg(zzbffVarZzb);
    }

    @Nullable
    private final zza zzam(int i) {
        if (this.zzaBD.size() <= i) {
            return null;
        }
        return this.zzaBD.get(this.zzaBD.keyAt(i));
    }

    @Override // com.google.android.gms.internal.zzbfe
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.zzaBD.size(); i++) {
            zza zzaVarZzam = zzam(i);
            if (zzaVarZzam != null) {
                printWriter.append((CharSequence) str).append("GoogleApiClient #").print(zzaVarZzam.zzaBE);
                printWriter.println(":");
                zzaVarZzam.zzaBF.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
            }
        }
    }

    @Override // com.google.android.gms.internal.zzbcm, com.google.android.gms.internal.zzbfe
    public final void onStart() {
        super.onStart();
        boolean z = this.mStarted;
        String strValueOf = String.valueOf(this.zzaBD);
        Log.d("AutoManageHelper", new StringBuilder(String.valueOf(strValueOf).length() + 14).append("onStart ").append(z).append(" ").append(strValueOf).toString());
        if (this.zzaBP.get() == null) {
            for (int i = 0; i < this.zzaBD.size(); i++) {
                zza zzaVarZzam = zzam(i);
                if (zzaVarZzam != null) {
                    zzaVarZzam.zzaBF.connect();
                }
            }
        }
    }

    @Override // com.google.android.gms.internal.zzbcm, com.google.android.gms.internal.zzbfe
    public final void onStop() {
        super.onStop();
        for (int i = 0; i < this.zzaBD.size(); i++) {
            zza zzaVarZzam = zzam(i);
            if (zzaVarZzam != null) {
                zzaVarZzam.zzaBF.disconnect();
            }
        }
    }

    public final void zza(int i, GoogleApiClient googleApiClient, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        zzbr.zzb(googleApiClient, "GoogleApiClient instance cannot be null");
        zzbr.zza(this.zzaBD.indexOfKey(i) < 0, new StringBuilder(54).append("Already managing a GoogleApiClient with id ").append(i).toString());
        zzbcn zzbcnVar = this.zzaBP.get();
        boolean z = this.mStarted;
        String strValueOf = String.valueOf(zzbcnVar);
        Log.d("AutoManageHelper", new StringBuilder(String.valueOf(strValueOf).length() + 49).append("starting AutoManage for client ").append(i).append(" ").append(z).append(" ").append(strValueOf).toString());
        this.zzaBD.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && zzbcnVar == null) {
            String strValueOf2 = String.valueOf(googleApiClient);
            Log.d("AutoManageHelper", new StringBuilder(String.valueOf(strValueOf2).length() + 11).append("connecting ").append(strValueOf2).toString());
            googleApiClient.connect();
        }
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zza(ConnectionResult connectionResult, int i) {
        Log.w("AutoManageHelper", "Unresolved error while connecting client. Stopping auto-manage.");
        if (i < 0) {
            Log.wtf("AutoManageHelper", "AutoManageLifecycleHelper received onErrorResolutionFailed callback but no failing client ID is set", new Exception());
            return;
        }
        zza zzaVar = this.zzaBD.get(i);
        if (zzaVar != null) {
            zzal(i);
            GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = zzaVar.zzaBG;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
    }

    public final void zzal(int i) {
        zza zzaVar = this.zzaBD.get(i);
        this.zzaBD.remove(i);
        if (zzaVar != null) {
            zzaVar.zzaBF.unregisterConnectionFailedListener(zzaVar);
            zzaVar.zzaBF.disconnect();
        }
    }

    @Override // com.google.android.gms.internal.zzbcm
    protected final void zzpq() {
        for (int i = 0; i < this.zzaBD.size(); i++) {
            zza zzaVarZzam = zzam(i);
            if (zzaVarZzam != null) {
                zzaVarZzam.zzaBF.connect();
            }
        }
    }
}
