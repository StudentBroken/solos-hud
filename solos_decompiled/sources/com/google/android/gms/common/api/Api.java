package com.google.android.gms.common.api;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzam;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzq;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/* JADX INFO: loaded from: classes3.dex */
public final class Api<O extends ApiOptions> {
    private final String mName;
    private final zzh<?, O> zzaAA;
    private final zzf<?> zzaAB;
    private final zzi<?> zzaAC;
    private final zza<?, O> zzaAz;

    public interface ApiOptions {

        public interface HasOptions extends ApiOptions {
        }

        public static final class NoOptions implements NotRequiredOptions {
            private NoOptions() {
            }
        }

        public interface NotRequiredOptions extends ApiOptions {
        }

        public interface Optional extends HasOptions, NotRequiredOptions {
        }
    }

    public static abstract class zza<T extends zze, O> extends zzd<T, O> {
        public abstract T zza(Context context, Looper looper, zzq zzqVar, O o, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener);
    }

    public interface zzb {
    }

    public static class zzc<C extends zzb> {
    }

    public static abstract class zzd<T extends zzb, O> {
        public int getPriority() {
            return Integer.MAX_VALUE;
        }

        public List<Scope> zzn(O o) {
            return Collections.emptyList();
        }
    }

    public interface zze extends zzb {
        void disconnect();

        void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

        boolean isConnected();

        boolean isConnecting();

        void zza(zzam zzamVar, Set<Scope> set);

        void zza(zzj zzjVar);

        boolean zzmE();

        Intent zzmF();

        boolean zzmt();

        boolean zzpc();
    }

    public static final class zzf<C extends zze> extends zzc<C> {
    }

    public interface zzg<T extends IInterface> extends zzb {
        T zzd(IBinder iBinder);

        String zzda();

        String zzdb();
    }

    public static abstract class zzh<T extends zzg, O> extends zzd<T, O> {
    }

    public static final class zzi<C extends zzg> extends zzc<C> {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <C extends zze> Api(String str, zza<C, O> zzaVar, zzf<C> zzfVar) {
        zzbr.zzb(zzaVar, "Cannot construct an Api with a null ClientBuilder");
        zzbr.zzb(zzfVar, "Cannot construct an Api with a null ClientKey");
        this.mName = str;
        this.zzaAz = zzaVar;
        this.zzaAA = null;
        this.zzaAB = zzfVar;
        this.zzaAC = null;
    }

    public final String getName() {
        return this.mName;
    }

    public final zzd<?, O> zzoZ() {
        return this.zzaAz;
    }

    public final zza<?, O> zzpa() {
        zzbr.zza(this.zzaAz != null, "This API was constructed with a SimpleClientBuilder. Use getSimpleClientBuilder");
        return this.zzaAz;
    }

    public final zzc<?> zzpb() {
        if (this.zzaAB != null) {
            return this.zzaAB;
        }
        throw new IllegalStateException("This API was constructed with null client keys. This should not be possible.");
    }
}
