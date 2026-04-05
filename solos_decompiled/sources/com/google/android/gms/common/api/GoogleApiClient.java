package com.google.android.gms.common.api;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.internal.zzbcg;
import com.google.android.gms.internal.zzbck;
import com.google.android.gms.internal.zzbcu;
import com.google.android.gms.internal.zzbeb;
import com.google.android.gms.internal.zzbfd;
import com.google.android.gms.internal.zzbfi;
import com.google.android.gms.internal.zzbfu;
import com.google.android.gms.internal.zzbge;
import com.google.android.gms.internal.zzcus;
import com.google.android.gms.internal.zzcuw;
import com.google.android.gms.internal.zzcux;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes3.dex */
public abstract class GoogleApiClient {
    public static final int SIGN_IN_MODE_OPTIONAL = 2;
    public static final int SIGN_IN_MODE_REQUIRED = 1;
    private static final Set<GoogleApiClient> zzaAU = Collections.newSetFromMap(new WeakHashMap());

    public static final class Builder {
        private final Context mContext;
        private final Set<Scope> zzaAV;
        private final Set<Scope> zzaAW;
        private int zzaAX;
        private View zzaAY;
        private String zzaAZ;
        private final Map<Api<?>, zzr> zzaBa;
        private final Map<Api<?>, Api.ApiOptions> zzaBb;
        private zzbfd zzaBc;
        private int zzaBd;
        private OnConnectionFailedListener zzaBe;
        private GoogleApiAvailability zzaBf;
        private Api.zza<? extends zzcuw, zzcux> zzaBg;
        private final ArrayList<ConnectionCallbacks> zzaBh;
        private final ArrayList<OnConnectionFailedListener> zzaBi;
        private boolean zzaBj;
        private Account zzajd;
        private String zzakg;
        private Looper zzrP;

        public Builder(@NonNull Context context) {
            this.zzaAV = new HashSet();
            this.zzaAW = new HashSet();
            this.zzaBa = new ArrayMap();
            this.zzaBb = new ArrayMap();
            this.zzaBd = -1;
            this.zzaBf = GoogleApiAvailability.getInstance();
            this.zzaBg = zzcus.zzajU;
            this.zzaBh = new ArrayList<>();
            this.zzaBi = new ArrayList<>();
            this.zzaBj = false;
            this.mContext = context;
            this.zzrP = context.getMainLooper();
            this.zzakg = context.getPackageName();
            this.zzaAZ = context.getClass().getName();
        }

        public Builder(@NonNull Context context, @NonNull ConnectionCallbacks connectionCallbacks, @NonNull OnConnectionFailedListener onConnectionFailedListener) {
            this(context);
            zzbr.zzb(connectionCallbacks, "Must provide a connected listener");
            this.zzaBh.add(connectionCallbacks);
            zzbr.zzb(onConnectionFailedListener, "Must provide a connection failed listener");
            this.zzaBi.add(onConnectionFailedListener);
        }

        private final <O extends Api.ApiOptions> void zza(Api<O> api, O o, Scope... scopeArr) {
            HashSet hashSet = new HashSet(api.zzoZ().zzn(o));
            for (Scope scope : scopeArr) {
                hashSet.add(scope);
            }
            this.zzaBa.put(api, new zzr(hashSet));
        }

        public final Builder addApi(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions> api) {
            zzbr.zzb(api, "Api must not be null");
            this.zzaBb.put(api, null);
            List<Scope> listZzn = api.zzoZ().zzn(null);
            this.zzaAW.addAll(listZzn);
            this.zzaAV.addAll(listZzn);
            return this;
        }

        public final <O extends Api.ApiOptions.HasOptions> Builder addApi(@NonNull Api<O> api, @NonNull O o) {
            zzbr.zzb(api, "Api must not be null");
            zzbr.zzb(o, "Null options are not permitted for this Api");
            this.zzaBb.put(api, o);
            List<Scope> listZzn = api.zzoZ().zzn(o);
            this.zzaAW.addAll(listZzn);
            this.zzaAV.addAll(listZzn);
            return this;
        }

        public final <O extends Api.ApiOptions.HasOptions> Builder addApiIfAvailable(@NonNull Api<O> api, @NonNull O o, Scope... scopeArr) {
            zzbr.zzb(api, "Api must not be null");
            zzbr.zzb(o, "Null options are not permitted for this Api");
            this.zzaBb.put(api, o);
            zza(api, o, scopeArr);
            return this;
        }

        public final Builder addApiIfAvailable(@NonNull Api<? extends Api.ApiOptions.NotRequiredOptions> api, Scope... scopeArr) {
            zzbr.zzb(api, "Api must not be null");
            this.zzaBb.put(api, null);
            zza(api, null, scopeArr);
            return this;
        }

        public final Builder addConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {
            zzbr.zzb(connectionCallbacks, "Listener must not be null");
            this.zzaBh.add(connectionCallbacks);
            return this;
        }

        public final Builder addOnConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
            zzbr.zzb(onConnectionFailedListener, "Listener must not be null");
            this.zzaBi.add(onConnectionFailedListener);
            return this;
        }

        public final Builder addScope(@NonNull Scope scope) {
            zzbr.zzb(scope, "Scope must not be null");
            this.zzaAV.add(scope);
            return this;
        }

        public final GoogleApiClient build() {
            zzbr.zzb(!this.zzaBb.isEmpty(), "must call addApi() to add at least one API");
            zzq zzqVarZzpl = zzpl();
            Api<?> api = null;
            Map<Api<?>, zzr> mapZzrn = zzqVarZzpl.zzrn();
            ArrayMap arrayMap = new ArrayMap();
            ArrayMap arrayMap2 = new ArrayMap();
            ArrayList arrayList = new ArrayList();
            boolean z = false;
            for (Api<?> api2 : this.zzaBb.keySet()) {
                Api.ApiOptions apiOptions = this.zzaBb.get(api2);
                boolean z2 = mapZzrn.get(api2) != null;
                arrayMap.put(api2, Boolean.valueOf(z2));
                zzbcu zzbcuVar = new zzbcu(api2, z2);
                arrayList.add(zzbcuVar);
                Api.zza<?, O> zzaVarZzpa = api2.zzpa();
                Api.zze zzeVarZza = zzaVarZzpa.zza(this.mContext, this.zzrP, zzqVarZzpl, apiOptions, zzbcuVar, zzbcuVar);
                arrayMap2.put(api2.zzpb(), zzeVarZza);
                boolean z3 = zzaVarZzpa.getPriority() == 1 ? apiOptions != null : z;
                if (!zzeVarZza.zzmE()) {
                    api2 = api;
                } else if (api != null) {
                    String strValueOf = String.valueOf(api2.getName());
                    String strValueOf2 = String.valueOf(api.getName());
                    throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 21 + String.valueOf(strValueOf2).length()).append(strValueOf).append(" cannot be used with ").append(strValueOf2).toString());
                }
                z = z3;
                api = api2;
            }
            if (api != null) {
                if (z) {
                    String strValueOf3 = String.valueOf(api.getName());
                    throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf3).length() + 82).append("With using ").append(strValueOf3).append(", GamesOptions can only be specified within GoogleSignInOptions.Builder").toString());
                }
                zzbr.zza(this.zzajd == null, "Must not set an account in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead", api.getName());
                zzbr.zza(this.zzaAV.equals(this.zzaAW), "Must not set scopes in GoogleApiClient.Builder when using %s. Set account in GoogleSignInOptions.Builder instead.", api.getName());
            }
            zzbeb zzbebVar = new zzbeb(this.mContext, new ReentrantLock(), this.zzrP, zzqVarZzpl, this.zzaBf, this.zzaBg, arrayMap, this.zzaBh, this.zzaBi, arrayMap2, this.zzaBd, zzbeb.zza(arrayMap2.values(), true), arrayList, false);
            synchronized (GoogleApiClient.zzaAU) {
                GoogleApiClient.zzaAU.add(zzbebVar);
            }
            if (this.zzaBd >= 0) {
                zzbcg.zza(this.zzaBc).zza(this.zzaBd, zzbebVar, this.zzaBe);
            }
            return zzbebVar;
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, int i, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            zzbfd zzbfdVar = new zzbfd(fragmentActivity);
            zzbr.zzb(i >= 0, "clientId must be non-negative");
            this.zzaBd = i;
            this.zzaBe = onConnectionFailedListener;
            this.zzaBc = zzbfdVar;
            return this;
        }

        public final Builder enableAutoManage(@NonNull FragmentActivity fragmentActivity, @Nullable OnConnectionFailedListener onConnectionFailedListener) {
            return enableAutoManage(fragmentActivity, 0, onConnectionFailedListener);
        }

        public final Builder setAccountName(String str) {
            this.zzajd = str == null ? null : new Account(str, "com.google");
            return this;
        }

        public final Builder setGravityForPopups(int i) {
            this.zzaAX = i;
            return this;
        }

        public final Builder setHandler(@NonNull Handler handler) {
            zzbr.zzb(handler, "Handler must not be null");
            this.zzrP = handler.getLooper();
            return this;
        }

        public final Builder setViewForPopups(@NonNull View view) {
            zzbr.zzb(view, "View must not be null");
            this.zzaAY = view;
            return this;
        }

        public final Builder useDefaultAccount() {
            return setAccountName("<<default account>>");
        }

        public final Builder zze(Account account) {
            this.zzajd = account;
            return this;
        }

        public final zzq zzpl() {
            zzcux zzcuxVar = zzcux.zzbCQ;
            if (this.zzaBb.containsKey(zzcus.API)) {
                zzcuxVar = (zzcux) this.zzaBb.get(zzcus.API);
            }
            return new zzq(this.zzajd, this.zzaAV, this.zzaBa, this.zzaAX, this.zzaAY, this.zzakg, this.zzaAZ, zzcuxVar);
        }
    }

    public interface ConnectionCallbacks {
        public static final int CAUSE_NETWORK_LOST = 2;
        public static final int CAUSE_SERVICE_DISCONNECTED = 1;

        void onConnected(@Nullable Bundle bundle);

        void onConnectionSuspended(int i);
    }

    public interface OnConnectionFailedListener {
        void onConnectionFailed(@NonNull ConnectionResult connectionResult);
    }

    public static void dumpAll(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        synchronized (zzaAU) {
            String strConcat = String.valueOf(str).concat("  ");
            int i = 0;
            for (GoogleApiClient googleApiClient : zzaAU) {
                printWriter.append((CharSequence) str).append("GoogleApiClient#").println(i);
                googleApiClient.dump(strConcat, fileDescriptor, printWriter, strArr);
                i++;
            }
        }
    }

    public static Set<GoogleApiClient> zzpi() {
        Set<GoogleApiClient> set;
        synchronized (zzaAU) {
            set = zzaAU;
        }
        return set;
    }

    public abstract ConnectionResult blockingConnect();

    public abstract ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit);

    public abstract PendingResult<Status> clearDefaultAccountAndReconnect();

    public abstract void connect();

    public void connect(int i) {
        throw new UnsupportedOperationException();
    }

    public abstract void disconnect();

    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    @NonNull
    public abstract ConnectionResult getConnectionResult(@NonNull Api<?> api);

    public Context getContext() {
        throw new UnsupportedOperationException();
    }

    public Looper getLooper() {
        throw new UnsupportedOperationException();
    }

    public abstract boolean hasConnectedApi(@NonNull Api<?> api);

    public abstract boolean isConnected();

    public abstract boolean isConnecting();

    public abstract boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    public abstract void reconnect();

    public abstract void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    public abstract void stopAutoManage(@NonNull FragmentActivity fragmentActivity);

    public abstract void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks);

    public abstract void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener);

    @NonNull
    public <C extends Api.zze> C zza(@NonNull Api.zzc<C> zzcVar) {
        throw new UnsupportedOperationException();
    }

    public void zza(zzbge zzbgeVar) {
        throw new UnsupportedOperationException();
    }

    public boolean zza(@NonNull Api<?> api) {
        throw new UnsupportedOperationException();
    }

    public boolean zza(zzbfu zzbfuVar) {
        throw new UnsupportedOperationException();
    }

    public void zzb(zzbge zzbgeVar) {
        throw new UnsupportedOperationException();
    }

    public <A extends Api.zzb, R extends Result, T extends zzbck<R, A>> T zzd(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    public <A extends Api.zzb, T extends zzbck<? extends Result, A>> T zze(@NonNull T t) {
        throw new UnsupportedOperationException();
    }

    public <L> zzbfi<L> zzp(@NonNull L l) {
        throw new UnsupportedOperationException();
    }

    public void zzpj() {
        throw new UnsupportedOperationException();
    }
}
