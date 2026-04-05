package com.google.android.gms.internal;

import com.kopin.pupil.aria.app.TimedAppState;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcgn {
    private static zzcgo<Boolean> zzbpS = zzcgo.zzb("measurement.service_enabled", true, true);
    private static zzcgo<Boolean> zzbpT = zzcgo.zzb("measurement.service_client_enabled", true, true);
    private static zzcgo<Boolean> zzbpU = zzcgo.zzb("measurement.log_third_party_store_events_enabled", false, false);
    private static zzcgo<Boolean> zzbpV = zzcgo.zzb("measurement.log_installs_enabled", false, false);
    private static zzcgo<Boolean> zzbpW = zzcgo.zzb("measurement.log_upgrades_enabled", false, false);
    private static zzcgo<Boolean> zzbpX = zzcgo.zzb("measurement.log_androidId_enabled", false, false);
    public static zzcgo<Boolean> zzbpY = zzcgo.zzb("measurement.upload_dsid_enabled", false, false);
    public static zzcgo<String> zzbpZ = zzcgo.zzj("measurement.log_tag", "FA", "FA-SVC");
    public static zzcgo<Long> zzbqa = zzcgo.zzb("measurement.ad_id_cache_time", TimedAppState.DEFAULT_CONFIRM_TIMEOUT, TimedAppState.DEFAULT_CONFIRM_TIMEOUT);
    public static zzcgo<Long> zzbqb = zzcgo.zzb("measurement.monitoring.sample_period_millis", 86400000, 86400000);
    public static zzcgo<Long> zzbqc = zzcgo.zzb("measurement.config.cache_time", 86400000, 3600000);
    public static zzcgo<String> zzbqd = zzcgo.zzj("measurement.config.url_scheme", "https", "https");
    public static zzcgo<String> zzbqe = zzcgo.zzj("measurement.config.url_authority", "app-measurement.com", "app-measurement.com");
    public static zzcgo<Integer> zzbqf = zzcgo.zzm("measurement.upload.max_bundles", 100, 100);
    public static zzcgo<Integer> zzbqg = zzcgo.zzm("measurement.upload.max_batch_size", 65536, 65536);
    public static zzcgo<Integer> zzbqh = zzcgo.zzm("measurement.upload.max_bundle_size", 65536, 65536);
    public static zzcgo<Integer> zzbqi = zzcgo.zzm("measurement.upload.max_events_per_bundle", 1000, 1000);
    public static zzcgo<Integer> zzbqj = zzcgo.zzm("measurement.upload.max_events_per_day", 100000, 100000);
    public static zzcgo<Integer> zzbqk = zzcgo.zzm("measurement.upload.max_error_events_per_day", 1000, 1000);
    public static zzcgo<Integer> zzbql = zzcgo.zzm("measurement.upload.max_public_events_per_day", 50000, 50000);
    public static zzcgo<Integer> zzbqm = zzcgo.zzm("measurement.upload.max_conversions_per_day", 500, 500);
    public static zzcgo<Integer> zzbqn = zzcgo.zzm("measurement.upload.max_realtime_events_per_day", 10, 10);
    public static zzcgo<Integer> zzbqo = zzcgo.zzm("measurement.store.max_stored_events_per_app", 100000, 100000);
    public static zzcgo<String> zzbqp = zzcgo.zzj("measurement.upload.url", "https://app-measurement.com/a", "https://app-measurement.com/a");
    public static zzcgo<Long> zzbqq = zzcgo.zzb("measurement.upload.backoff_period", 43200000, 43200000);
    public static zzcgo<Long> zzbqr = zzcgo.zzb("measurement.upload.window_interval", 3600000, 3600000);
    public static zzcgo<Long> zzbqs = zzcgo.zzb("measurement.upload.interval", 3600000, 3600000);
    public static zzcgo<Long> zzbqt = zzcgo.zzb("measurement.upload.realtime_upload_interval", TimedAppState.DEFAULT_CONFIRM_TIMEOUT, TimedAppState.DEFAULT_CONFIRM_TIMEOUT);
    public static zzcgo<Long> zzbqu = zzcgo.zzb("measurement.upload.debug_upload_interval", 1000, 1000);
    public static zzcgo<Long> zzbqv = zzcgo.zzb("measurement.upload.minimum_delay", 500, 500);
    public static zzcgo<Long> zzbqw = zzcgo.zzb("measurement.alarm_manager.minimum_interval", 60000, 60000);
    public static zzcgo<Long> zzbqx = zzcgo.zzb("measurement.upload.stale_data_deletion_interval", 86400000, 86400000);
    public static zzcgo<Long> zzbqy = zzcgo.zzb("measurement.upload.refresh_blacklisted_config_interval", 604800000, 604800000);
    public static zzcgo<Long> zzbqz = zzcgo.zzb("measurement.upload.initial_upload_delay_time", 15000, 15000);
    public static zzcgo<Long> zzbqA = zzcgo.zzb("measurement.upload.retry_time", 1800000, 1800000);
    public static zzcgo<Integer> zzbqB = zzcgo.zzm("measurement.upload.retry_count", 6, 6);
    public static zzcgo<Long> zzbqC = zzcgo.zzb("measurement.upload.max_queue_time", 2419200000L, 2419200000L);
    public static zzcgo<Integer> zzbqD = zzcgo.zzm("measurement.lifetimevalue.max_currency_tracked", 4, 4);
    public static zzcgo<Integer> zzbqE = zzcgo.zzm("measurement.audience.filter_result_max_count", 200, 200);
    public static zzcgo<Long> zzbqF = zzcgo.zzb("measurement.service_client.idle_disconnect_millis", 5000, 5000);
}
