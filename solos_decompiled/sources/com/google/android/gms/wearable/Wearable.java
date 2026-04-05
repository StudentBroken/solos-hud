package com.google.android.gms.wearable;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.wearable.internal.zzac;
import com.google.android.gms.wearable.internal.zzbh;
import com.google.android.gms.wearable.internal.zzbi;
import com.google.android.gms.wearable.internal.zzds;
import com.google.android.gms.wearable.internal.zzdz;
import com.google.android.gms.wearable.internal.zzey;
import com.google.android.gms.wearable.internal.zzfw;
import com.google.android.gms.wearable.internal.zzgi;

/* JADX INFO: loaded from: classes6.dex */
public class Wearable {
    public static final DataApi DataApi = new zzbi();
    public static final CapabilityApi CapabilityApi = new com.google.android.gms.wearable.internal.zzo();
    public static final MessageApi MessageApi = new zzds();
    public static final NodeApi NodeApi = new zzdz();
    public static final ChannelApi ChannelApi = new zzac();
    private static zzc zzbRn = new com.google.android.gms.wearable.internal.zzk();
    private static zza zzbRo = new com.google.android.gms.wearable.internal.zzh();
    private static zzf zzbRp = new zzbh();
    private static zzi zzbRq = new zzey();
    private static zzu zzbRr = new zzgi();
    private static Api.zzf<zzfw> zzajT = new Api.zzf<>();
    private static final Api.zza<zzfw, WearableOptions> zzajU = new zzj();
    public static final Api<WearableOptions> API = new Api<>("Wearable.API", zzajU, zzajT);

    public static final class WearableOptions implements Api.ApiOptions.Optional {

        public static class Builder {
            public WearableOptions build() {
                return new WearableOptions(this, null);
            }
        }

        private WearableOptions(Builder builder) {
        }

        /* synthetic */ WearableOptions(Builder builder, zzj zzjVar) {
            this(builder);
        }
    }

    private Wearable() {
    }
}
