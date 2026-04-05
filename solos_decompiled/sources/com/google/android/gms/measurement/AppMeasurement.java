package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.internal.zzbey;
import com.google.android.gms.internal.zzcfy;
import com.google.android.gms.internal.zzchx;
import com.google.android.gms.internal.zzcix;
import com.google.android.gms.internal.zzcku;
import com.google.android.gms.internal.zzckx;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes36.dex */
@Keep
@Deprecated
public class AppMeasurement {

    @KeepForSdk
    public static final String CRASH_ORIGIN = "crash";

    @KeepForSdk
    public static final String FCM_ORIGIN = "fcm";
    private final zzchx zzboi;

    public static class ConditionalUserProperty {

        @Keep
        public boolean mActive;

        @Keep
        public String mAppId;

        @Keep
        public long mCreationTimestamp;

        @Keep
        public String mExpiredEventName;

        @Keep
        public Bundle mExpiredEventParams;

        @Keep
        public String mName;

        @Keep
        public String mOrigin;

        @Keep
        public long mTimeToLive;

        @Keep
        public String mTimedOutEventName;

        @Keep
        public Bundle mTimedOutEventParams;

        @Keep
        public String mTriggerEventName;

        @Keep
        public long mTriggerTimeout;

        @Keep
        public String mTriggeredEventName;

        @Keep
        public Bundle mTriggeredEventParams;

        @Keep
        public long mTriggeredTimestamp;

        @Keep
        public Object mValue;

        public ConditionalUserProperty() {
        }

        public ConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
            zzbr.zzu(conditionalUserProperty);
            this.mAppId = conditionalUserProperty.mAppId;
            this.mOrigin = conditionalUserProperty.mOrigin;
            this.mCreationTimestamp = conditionalUserProperty.mCreationTimestamp;
            this.mName = conditionalUserProperty.mName;
            if (conditionalUserProperty.mValue != null) {
                this.mValue = zzckx.zzD(conditionalUserProperty.mValue);
                if (this.mValue == null) {
                    this.mValue = conditionalUserProperty.mValue;
                }
            }
            this.mValue = conditionalUserProperty.mValue;
            this.mActive = conditionalUserProperty.mActive;
            this.mTriggerEventName = conditionalUserProperty.mTriggerEventName;
            this.mTriggerTimeout = conditionalUserProperty.mTriggerTimeout;
            this.mTimedOutEventName = conditionalUserProperty.mTimedOutEventName;
            if (conditionalUserProperty.mTimedOutEventParams != null) {
                this.mTimedOutEventParams = new Bundle(conditionalUserProperty.mTimedOutEventParams);
            }
            this.mTriggeredEventName = conditionalUserProperty.mTriggeredEventName;
            if (conditionalUserProperty.mTriggeredEventParams != null) {
                this.mTriggeredEventParams = new Bundle(conditionalUserProperty.mTriggeredEventParams);
            }
            this.mTriggeredTimestamp = conditionalUserProperty.mTriggeredTimestamp;
            this.mTimeToLive = conditionalUserProperty.mTimeToLive;
            this.mExpiredEventName = conditionalUserProperty.mExpiredEventName;
            if (conditionalUserProperty.mExpiredEventParams != null) {
                this.mExpiredEventParams = new Bundle(conditionalUserProperty.mExpiredEventParams);
            }
        }
    }

    @KeepForSdk
    public static final class Event extends FirebaseAnalytics.Event {
        public static final String[] zzboj = {"app_clear_data", "app_exception", "app_remove", "app_upgrade", "app_install", "app_update", "firebase_campaign", "error", "first_open", "first_visit", "in_app_purchase", "notification_dismiss", "notification_foreground", "notification_open", "notification_receive", "os_update", "session_start", "user_engagement", "ad_exposure", "adunit_exposure", "ad_query", "ad_activeview", "ad_impression", "ad_click", "screen_view", "firebase_extra_parameter"};

        @KeepForSdk
        public static final String APP_EXCEPTION = "_ae";
        public static final String[] zzbok = {"_cd", APP_EXCEPTION, "_ui", "_ug", "_in", "_au", "_cmp", "_err", "_f", "_v", "_iap", "_nd", "_nf", "_no", "_nr", "_ou", "_s", "_e", "_xa", "_xu", "_aq", "_aa", "_ai", "_ac", "_vs", "_ep"};

        private Event() {
        }

        public static String zzdG(String str) {
            return zzckx.zza(str, zzboj, zzbok);
        }
    }

    @KeepForSdk
    public interface EventInterceptor {
        @WorkerThread
        @KeepForSdk
        void interceptEvent(String str, String str2, Bundle bundle, long j);
    }

    @KeepForSdk
    public interface OnEventListener {
        @WorkerThread
        @KeepForSdk
        void onEvent(String str, String str2, Bundle bundle, long j);
    }

    @KeepForSdk
    public static final class Param extends FirebaseAnalytics.Param {

        @KeepForSdk
        public static final String FATAL = "fatal";

        @KeepForSdk
        public static final String TIMESTAMP = "timestamp";
        public static final String[] zzbol = {"firebase_conversion", "engagement_time_msec", "exposure_time", "ad_event_id", "ad_unit_id", "firebase_error", "firebase_error_value", "firebase_error_length", "firebase_event_origin", "firebase_screen", "firebase_screen_class", "firebase_screen_id", "firebase_previous_screen", "firebase_previous_class", "firebase_previous_id", "message_device_time", "message_id", "message_name", "message_time", "previous_app_version", "previous_os_version", "topic", "update_with_analytics", "previous_first_open_count", "system_app", "system_app_update", "previous_install_count", "firebase_event_id", "firebase_extra_params_ct", "firebase_group_name", "firebase_list_length", "firebase_index", "firebase_event_name"};
        public static final String[] zzbom = {"_c", "_et", "_xt", "_aeid", "_ai", "_err", "_ev", "_el", "_o", "_sn", "_sc", "_si", "_pn", "_pc", "_pi", "_ndt", "_nmid", "_nmn", "_nmt", "_pv", "_po", "_nt", "_uwa", "_pfo", "_sys", "_sysu", "_pin", "_eid", "_epc", "_gn", "_ll", "_i", "_en"};

        private Param() {
        }

        public static String zzdG(String str) {
            return zzckx.zza(str, zzbol, zzbom);
        }
    }

    @KeepForSdk
    public static final class UserProperty extends FirebaseAnalytics.UserProperty {
        public static final String[] zzboq = {"firebase_last_notification", "first_open_time", "first_visit_time", "last_deep_link_referrer", "user_id", "first_open_after_install"};

        @KeepForSdk
        public static final String FIREBASE_LAST_NOTIFICATION = "_ln";
        public static final String[] zzbor = {FIREBASE_LAST_NOTIFICATION, "_fot", "_fvt", "_ldl", "_id", "_fi"};

        private UserProperty() {
        }

        public static String zzdG(String str) {
            return zzckx.zza(str, zzboq, zzbor);
        }
    }

    public interface zza {
        @MainThread
        boolean zza(zzb zzbVar, zzb zzbVar2);
    }

    public static class zzb {
        public String zzbon;
        public String zzboo;
        public long zzbop;

        public zzb() {
        }

        public zzb(zzb zzbVar) {
            this.zzbon = zzbVar.zzbon;
            this.zzboo = zzbVar.zzboo;
            this.zzbop = zzbVar.zzbop;
        }
    }

    public AppMeasurement(zzchx zzchxVar) {
        zzbr.zzu(zzchxVar);
        this.zzboi = zzchxVar;
    }

    @Keep
    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"})
    @Deprecated
    public static AppMeasurement getInstance(Context context) {
        return zzchx.zzbj(context).zzyQ();
    }

    @Keep
    public void beginAdUnitExposure(@Size(min = 1) @NonNull String str) {
        this.zzboi.zzwq().beginAdUnitExposure(str);
    }

    @Keep
    protected void clearConditionalUserProperty(@Size(max = 24, min = 1) @NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        this.zzboi.zzws().clearConditionalUserProperty(str, str2, bundle);
    }

    @Keep
    protected void clearConditionalUserPropertyAs(@Size(min = 1) @NonNull String str, @Size(max = 24, min = 1) @NonNull String str2, @Nullable String str3, @Nullable Bundle bundle) {
        this.zzboi.zzws().clearConditionalUserPropertyAs(str, str2, str3, bundle);
    }

    @Keep
    public void endAdUnitExposure(@Size(min = 1) @NonNull String str) {
        this.zzboi.zzwq().endAdUnitExposure(str);
    }

    @Keep
    public long generateEventId() {
        return this.zzboi.zzwA().zzzq();
    }

    @Keep
    @Nullable
    public String getAppInstanceId() {
        return this.zzboi.zzws().zzyF();
    }

    @WorkerThread
    @Keep
    protected List<ConditionalUserProperty> getConditionalUserProperties(@Nullable String str, @Size(max = 23, min = 1) @Nullable String str2) {
        return this.zzboi.zzws().getConditionalUserProperties(str, str2);
    }

    @WorkerThread
    @Keep
    protected List<ConditionalUserProperty> getConditionalUserPropertiesAs(@Size(min = 1) @NonNull String str, @Nullable String str2, @Size(max = 23, min = 1) @Nullable String str3) {
        return this.zzboi.zzws().getConditionalUserPropertiesAs(str, str2, str3);
    }

    @Keep
    @Nullable
    public String getCurrentScreenClass() {
        zzb zzbVarZzzg = this.zzboi.zzww().zzzg();
        if (zzbVarZzzg != null) {
            return zzbVarZzzg.zzboo;
        }
        return null;
    }

    @Keep
    @Nullable
    public String getCurrentScreenName() {
        zzb zzbVarZzzg = this.zzboi.zzww().zzzg();
        if (zzbVarZzzg != null) {
            return zzbVarZzzg.zzbon;
        }
        return null;
    }

    @Keep
    @Nullable
    public String getGmpAppId() {
        try {
            return zzbey.zzqy();
        } catch (IllegalStateException e) {
            this.zzboi.zzwE().zzyv().zzj("getGoogleAppId failed with exception", e);
            return null;
        }
    }

    @WorkerThread
    @Keep
    protected int getMaxUserProperties(@Size(min = 1) @NonNull String str) {
        this.zzboi.zzws();
        return zzcix.getMaxUserProperties(str);
    }

    @WorkerThread
    @Keep
    protected Map<String, Object> getUserProperties(@Nullable String str, @Size(max = 24, min = 1) @Nullable String str2, boolean z) {
        return this.zzboi.zzws().getUserProperties(str, str2, z);
    }

    @WorkerThread
    @KeepForSdk
    public Map<String, Object> getUserProperties(boolean z) {
        List<zzcku> listZzao = this.zzboi.zzws().zzao(z);
        ArrayMap arrayMap = new ArrayMap(listZzao.size());
        for (zzcku zzckuVar : listZzao) {
            arrayMap.put(zzckuVar.name, zzckuVar.getValue());
        }
        return arrayMap;
    }

    @WorkerThread
    @Keep
    protected Map<String, Object> getUserPropertiesAs(@Size(min = 1) @NonNull String str, @Nullable String str2, @Size(max = 23, min = 1) @Nullable String str3, boolean z) {
        return this.zzboi.zzws().getUserPropertiesAs(str, str2, str3, z);
    }

    public final void logEvent(@Size(max = 40, min = 1) @NonNull String str, Bundle bundle) {
        int iZzeq;
        if (bundle == null) {
            bundle = new Bundle();
        }
        zzcfy.zzxD();
        if ("_iap".equals(str) || (iZzeq = this.zzboi.zzwA().zzeq(str)) == 0) {
            this.zzboi.zzws().zza(SettingsJsonConstants.APP_KEY, str, bundle, true);
        } else {
            this.zzboi.zzwA();
            this.zzboi.zzwA().zza(iZzeq, "_ev", zzckx.zza(str, zzcfy.zzxg(), true), str != null ? str.length() : 0);
        }
    }

    @Keep
    public void logEventInternal(String str, String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.zzboi.zzws().zzd(str, str2, bundle);
    }

    @KeepForSdk
    public void logEventInternalNoInterceptor(String str, String str2, Bundle bundle, long j) {
        this.zzboi.zzws().zza(str, str2, bundle == null ? new Bundle() : bundle, j);
    }

    @KeepForSdk
    public void registerOnMeasurementEventListener(OnEventListener onEventListener) {
        this.zzboi.zzws().registerOnMeasurementEventListener(onEventListener);
    }

    @Keep
    public void registerOnScreenChangeCallback(@NonNull zza zzaVar) {
        this.zzboi.zzww().registerOnScreenChangeCallback(zzaVar);
    }

    @Keep
    protected void setConditionalUserProperty(@NonNull ConditionalUserProperty conditionalUserProperty) {
        this.zzboi.zzws().setConditionalUserProperty(conditionalUserProperty);
    }

    @Keep
    protected void setConditionalUserPropertyAs(@NonNull ConditionalUserProperty conditionalUserProperty) {
        this.zzboi.zzws().setConditionalUserPropertyAs(conditionalUserProperty);
    }

    @WorkerThread
    @KeepForSdk
    public void setEventInterceptor(EventInterceptor eventInterceptor) {
        this.zzboi.zzws().setEventInterceptor(eventInterceptor);
    }

    @Deprecated
    public void setMeasurementEnabled(boolean z) {
        this.zzboi.zzws().setMeasurementEnabled(z);
    }

    public final void setMinimumSessionDuration(long j) {
        this.zzboi.zzws().setMinimumSessionDuration(j);
    }

    public final void setSessionTimeoutDuration(long j) {
        this.zzboi.zzws().setSessionTimeoutDuration(j);
    }

    public final void setUserProperty(@Size(max = 24, min = 1) @NonNull String str, @Size(max = 36) @Nullable String str2) {
        int iZzes = this.zzboi.zzwA().zzes(str);
        if (iZzes == 0) {
            setUserPropertyInternal(SettingsJsonConstants.APP_KEY, str, str2);
        } else {
            this.zzboi.zzwA();
            this.zzboi.zzwA().zza(iZzes, "_ev", zzckx.zza(str, zzcfy.zzxh(), true), str != null ? str.length() : 0);
        }
    }

    @KeepForSdk
    public void setUserPropertyInternal(String str, String str2, Object obj) {
        this.zzboi.zzws().zzb(str, str2, obj);
    }

    @KeepForSdk
    public void unregisterOnMeasurementEventListener(OnEventListener onEventListener) {
        this.zzboi.zzws().unregisterOnMeasurementEventListener(onEventListener);
    }

    @Keep
    public void unregisterOnScreenChangeCallback(@NonNull zza zzaVar) {
        this.zzboi.zzww().unregisterOnScreenChangeCallback(zzaVar);
    }
}
