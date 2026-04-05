package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes36.dex */
public final class zzcix extends zzciv {
    protected zzcjk zzbts;
    private AppMeasurement.EventInterceptor zzbtt;
    private final Set<AppMeasurement.OnEventListener> zzbtu;
    private boolean zzbtv;
    private final AtomicReference<String> zzbtw;

    protected zzcix(zzchx zzchxVar) {
        super(zzchxVar);
        this.zzbtu = new CopyOnWriteArraySet();
        this.zzbtw = new AtomicReference<>();
    }

    public static int getMaxUserProperties(String str) {
        zzbr.zzcF(str);
        return zzcfy.zzxt();
    }

    private final void zza(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        long jCurrentTimeMillis = super.zzkp().currentTimeMillis();
        zzbr.zzu(conditionalUserProperty);
        zzbr.zzcF(conditionalUserProperty.mName);
        zzbr.zzcF(conditionalUserProperty.mOrigin);
        zzbr.zzu(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = jCurrentTimeMillis;
        String str = conditionalUserProperty.mName;
        Object obj = conditionalUserProperty.mValue;
        if (super.zzwA().zzet(str) != 0) {
            super.zzwE().zzyv().zzj("Invalid conditional user property name", super.zzwz().zzdZ(str));
            return;
        }
        if (super.zzwA().zzl(str, obj) != 0) {
            super.zzwE().zzyv().zze("Invalid conditional user property value", super.zzwz().zzdZ(str), obj);
            return;
        }
        Object objZzm = super.zzwA().zzm(str, obj);
        if (objZzm == null) {
            super.zzwE().zzyv().zze("Unable to normalize conditional user property value", super.zzwz().zzdZ(str), obj);
            return;
        }
        conditionalUserProperty.mValue = objZzm;
        long j = conditionalUserProperty.mTriggerTimeout;
        if (!TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) && (j > zzcfy.zzxv() || j < 1)) {
            super.zzwE().zzyv().zze("Invalid conditional user property timeout", super.zzwz().zzdZ(str), Long.valueOf(j));
            return;
        }
        long j2 = conditionalUserProperty.mTimeToLive;
        if (j2 > zzcfy.zzxw() || j2 < 1) {
            super.zzwE().zzyv().zze("Invalid conditional user property time to live", super.zzwz().zzdZ(str), Long.valueOf(j2));
        } else {
            super.zzwD().zzj(new zzciz(this, conditionalUserProperty));
        }
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2;
        if (bundle == null) {
            bundle2 = new Bundle();
        } else {
            bundle2 = new Bundle(bundle);
            for (String str4 : bundle2.keySet()) {
                Object obj = bundle2.get(str4);
                if (obj instanceof Bundle) {
                    bundle2.putBundle(str4, new Bundle((Bundle) obj));
                } else if (obj instanceof Parcelable[]) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    int i = 0;
                    while (true) {
                        int i2 = i;
                        if (i2 < parcelableArr.length) {
                            if (parcelableArr[i2] instanceof Bundle) {
                                parcelableArr[i2] = new Bundle((Bundle) parcelableArr[i2]);
                            }
                            i = i2 + 1;
                        }
                    }
                } else if (obj instanceof ArrayList) {
                    ArrayList arrayList = (ArrayList) obj;
                    int i3 = 0;
                    while (true) {
                        int i4 = i3;
                        if (i4 < arrayList.size()) {
                            Object obj2 = arrayList.get(i4);
                            if (obj2 instanceof Bundle) {
                                arrayList.set(i4, new Bundle((Bundle) obj2));
                            }
                            i3 = i4 + 1;
                        }
                    }
                }
            }
        }
        super.zzwD().zzj(new zzcjf(this, str, str2, j, bundle2, z, z2, z3, str3));
    }

    private final void zza(String str, String str2, long j, Object obj) {
        super.zzwD().zzj(new zzcjg(this, str, str2, obj, j));
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zza(str, str2, super.zzkp().currentTimeMillis(), bundle, true, z2, z3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, String str2, Object obj, long j) {
        zzbr.zzcF(str);
        zzbr.zzcF(str2);
        super.zzjB();
        super.zzwo();
        zzkC();
        if (!this.zzboi.isEnabled()) {
            super.zzwE().zzyA().log("User property not set since app measurement is disabled");
        } else if (this.zzboi.zzyN()) {
            super.zzwE().zzyA().zze("Setting user property (FE)", super.zzwz().zzdX(str2), obj);
            super.zzwv().zzb(new zzcku(str2, j, obj, str));
        }
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long jCurrentTimeMillis = super.zzkp().currentTimeMillis();
        zzbr.zzcF(str2);
        AppMeasurement.ConditionalUserProperty conditionalUserProperty = new AppMeasurement.ConditionalUserProperty();
        conditionalUserProperty.mAppId = str;
        conditionalUserProperty.mName = str2;
        conditionalUserProperty.mCreationTimestamp = jCurrentTimeMillis;
        if (str3 != null) {
            conditionalUserProperty.mExpiredEventName = str3;
            conditionalUserProperty.mExpiredEventParams = bundle;
        }
        super.zzwD().zzj(new zzcja(this, conditionalUserProperty));
    }

    @Nullable
    private final String zzad(long j) {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            super.zzwD().zzj(new zzcjj(this, atomicReference));
            try {
                atomicReference.wait(j);
            } catch (InterruptedException e) {
                super.zzwE().zzyx().log("Interrupted waiting for app instance id");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzan(boolean z) {
        super.zzjB();
        super.zzwo();
        zzkC();
        super.zzwE().zzyA().zzj("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        super.zzwF().setMeasurementEnabled(z);
        super.zzwv().zzzh();
    }

    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        if (super.zzwD().zzyK()) {
            super.zzwE().zzyv().log("Cannot get user properties from analytics worker thread");
            return Collections.emptyMap();
        }
        super.zzwD();
        if (zzchs.zzR()) {
            super.zzwE().zzyv().log("Cannot get user properties from main thread");
            return Collections.emptyMap();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzboi.zzwD().zzj(new zzcjc(this, atomicReference, str, str2, str3, z));
            try {
                atomicReference.wait(5000L);
            } catch (InterruptedException e) {
                super.zzwE().zzyx().zzj("Interrupted waiting for get user properties", e);
            }
        }
        List<zzcku> list = (List) atomicReference.get();
        if (list == null) {
            super.zzwE().zzyx().log("Timed out waiting for get user properties");
            return Collections.emptyMap();
        }
        ArrayMap arrayMap = new ArrayMap(list.size());
        for (zzcku zzckuVar : list) {
            arrayMap.put(zzckuVar.name, zzckuVar.getValue());
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzb(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        super.zzjB();
        zzkC();
        zzbr.zzu(conditionalUserProperty);
        zzbr.zzcF(conditionalUserProperty.mName);
        zzbr.zzcF(conditionalUserProperty.mOrigin);
        zzbr.zzu(conditionalUserProperty.mValue);
        if (!this.zzboi.isEnabled()) {
            super.zzwE().zzyA().log("Conditional property not sent since Firebase Analytics is disabled");
            return;
        }
        zzcku zzckuVar = new zzcku(conditionalUserProperty.mName, conditionalUserProperty.mTriggeredTimestamp, conditionalUserProperty.mValue, conditionalUserProperty.mOrigin);
        try {
            zzcgl zzcglVarZza = super.zzwA().zza(conditionalUserProperty.mTriggeredEventName, conditionalUserProperty.mTriggeredEventParams, conditionalUserProperty.mOrigin, 0L, true, false);
            super.zzwv().zzf(new zzcfw(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, zzckuVar, conditionalUserProperty.mCreationTimestamp, false, conditionalUserProperty.mTriggerEventName, super.zzwA().zza(conditionalUserProperty.mTimedOutEventName, conditionalUserProperty.mTimedOutEventParams, conditionalUserProperty.mOrigin, 0L, true, false), conditionalUserProperty.mTriggerTimeout, zzcglVarZza, conditionalUserProperty.mTimeToLive, super.zzwA().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, 0L, true, false)));
        } catch (IllegalArgumentException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        int length;
        zzbr.zzcF(str);
        zzbr.zzcF(str2);
        zzbr.zzu(bundle);
        super.zzjB();
        zzkC();
        if (!this.zzboi.isEnabled()) {
            super.zzwE().zzyA().log("Event not sent since app measurement is disabled");
            return;
        }
        if (!this.zzbtv) {
            this.zzbtv = true;
            try {
                try {
                    Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", Context.class).invoke(null, super.getContext());
                } catch (Exception e) {
                    super.zzwE().zzyx().zzj("Failed to invoke Tag Manager's initialize() method", e);
                }
            } catch (ClassNotFoundException e2) {
                super.zzwE().zzyz().log("Tag Manager is not found and thus will not be used");
            }
        }
        boolean zEquals = "am".equals(str);
        boolean zZzey = zzckx.zzey(str2);
        if (z && this.zzbtt != null && !zZzey && !zEquals) {
            super.zzwE().zzyA().zze("Passing event to registered event handler (FE)", super.zzwz().zzdX(str2), super.zzwz().zzA(bundle));
            this.zzbtt.interceptEvent(str, str2, bundle, j);
            return;
        }
        if (this.zzboi.zzyN()) {
            int iZzer = super.zzwA().zzer(str2);
            if (iZzer != 0) {
                super.zzwA();
                this.zzboi.zzwA().zza(str3, iZzer, "_ev", zzckx.zza(str2, zzcfy.zzxg(), true), str2 != null ? str2.length() : 0);
                return;
            }
            List<String> listSingletonList = Collections.singletonList("_o");
            Bundle bundleZza = super.zzwA().zza(str2, bundle, listSingletonList, z3, true);
            ArrayList arrayList = new ArrayList();
            arrayList.add(bundleZza);
            long jNextLong = super.zzwA().zzzr().nextLong();
            int i = 0;
            String[] strArr = (String[]) bundleZza.keySet().toArray(new String[bundle.size()]);
            Arrays.sort(strArr);
            int length2 = strArr.length;
            int i2 = 0;
            while (i2 < length2) {
                String str4 = strArr[i2];
                Object obj = bundleZza.get(str4);
                super.zzwA();
                Bundle[] bundleArrZzC = zzckx.zzC(obj);
                if (bundleArrZzC != null) {
                    bundleZza.putInt(str4, bundleArrZzC.length);
                    int i3 = 0;
                    while (true) {
                        int i4 = i3;
                        if (i4 >= bundleArrZzC.length) {
                            break;
                        }
                        Bundle bundleZza2 = super.zzwA().zza("_ep", bundleArrZzC[i4], listSingletonList, z3, false);
                        bundleZza2.putString("_en", str2);
                        bundleZza2.putLong("_eid", jNextLong);
                        bundleZza2.putString("_gn", str4);
                        bundleZza2.putInt("_ll", bundleArrZzC.length);
                        bundleZza2.putInt("_i", i4);
                        arrayList.add(bundleZza2);
                        i3 = i4 + 1;
                    }
                    length = bundleArrZzC.length + i;
                } else {
                    length = i;
                }
                i2++;
                i = length;
            }
            if (i != 0) {
                bundleZza.putLong("_eid", jNextLong);
                bundleZza.putInt("_epc", i);
            }
            zzcfy.zzxD();
            zzcjo zzcjoVarZzzf = super.zzww().zzzf();
            if (zzcjoVarZzzf != null && !bundleZza.containsKey("_sc")) {
                zzcjoVarZzzf.zzbtW = true;
            }
            int i5 = 0;
            while (true) {
                int i6 = i5;
                if (i6 >= arrayList.size()) {
                    break;
                }
                Bundle bundle2 = (Bundle) arrayList.get(i6);
                String str5 = i6 != 0 ? "_ep" : str2;
                bundle2.putString("_o", str);
                if (!bundle2.containsKey("_sc")) {
                    zzcjl.zza(zzcjoVarZzzf, bundle2);
                }
                Bundle bundleZzB = z2 ? super.zzwA().zzB(bundle2) : bundle2;
                super.zzwE().zzyA().zze("Logging event (FE)", super.zzwz().zzdX(str2), super.zzwz().zzA(bundleZzB));
                super.zzwv().zzc(new zzcgl(str5, new zzcgi(bundleZzB), str, j), str3);
                if (!zEquals) {
                    Iterator<AppMeasurement.OnEventListener> it = this.zzbtu.iterator();
                    while (it.hasNext()) {
                        it.next().onEvent(str, str2, new Bundle(bundleZzB), j);
                    }
                }
                i5 = i6 + 1;
            }
            zzcfy.zzxD();
            if (super.zzww().zzzf() == null || !AppMeasurement.Event.APP_EXCEPTION.equals(str2)) {
                return;
            }
            super.zzwC().zzap(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzc(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        super.zzjB();
        zzkC();
        zzbr.zzu(conditionalUserProperty);
        zzbr.zzcF(conditionalUserProperty.mName);
        if (!this.zzboi.isEnabled()) {
            super.zzwE().zzyA().log("Conditional property not cleared since Firebase Analytics is disabled");
            return;
        }
        try {
            super.zzwv().zzf(new zzcfw(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, new zzcku(conditionalUserProperty.mName, 0L, null, null), conditionalUserProperty.mCreationTimestamp, conditionalUserProperty.mActive, conditionalUserProperty.mTriggerEventName, null, conditionalUserProperty.mTriggerTimeout, null, conditionalUserProperty.mTimeToLive, super.zzwA().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, conditionalUserProperty.mCreationTimestamp, true, false)));
        } catch (IllegalArgumentException e) {
        }
    }

    private final List<AppMeasurement.ConditionalUserProperty> zzl(String str, String str2, String str3) {
        if (super.zzwD().zzyK()) {
            super.zzwE().zzyv().log("Cannot get conditional user properties from analytics worker thread");
            return Collections.emptyList();
        }
        super.zzwD();
        if (zzchs.zzR()) {
            super.zzwE().zzyv().log("Cannot get conditional user properties from main thread");
            return Collections.emptyList();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzboi.zzwD().zzj(new zzcjb(this, atomicReference, str, str2, str3));
            try {
                atomicReference.wait(5000L);
            } catch (InterruptedException e) {
                super.zzwE().zzyx().zze("Interrupted waiting for get conditional user properties", str, e);
            }
        }
        List<zzcfw> list = (List) atomicReference.get();
        if (list == null) {
            super.zzwE().zzyx().zzj("Timed out waiting for get conditional user properties", str);
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (zzcfw zzcfwVar : list) {
            AppMeasurement.ConditionalUserProperty conditionalUserProperty = new AppMeasurement.ConditionalUserProperty();
            conditionalUserProperty.mAppId = str;
            conditionalUserProperty.mOrigin = str2;
            conditionalUserProperty.mCreationTimestamp = zzcfwVar.zzbpi;
            conditionalUserProperty.mName = zzcfwVar.zzbph.name;
            conditionalUserProperty.mValue = zzcfwVar.zzbph.getValue();
            conditionalUserProperty.mActive = zzcfwVar.zzbpj;
            conditionalUserProperty.mTriggerEventName = zzcfwVar.zzbpk;
            if (zzcfwVar.zzbpl != null) {
                conditionalUserProperty.mTimedOutEventName = zzcfwVar.zzbpl.name;
                if (zzcfwVar.zzbpl.zzbpQ != null) {
                    conditionalUserProperty.mTimedOutEventParams = zzcfwVar.zzbpl.zzbpQ.zzyr();
                }
            }
            conditionalUserProperty.mTriggerTimeout = zzcfwVar.zzbpm;
            if (zzcfwVar.zzbpn != null) {
                conditionalUserProperty.mTriggeredEventName = zzcfwVar.zzbpn.name;
                if (zzcfwVar.zzbpn.zzbpQ != null) {
                    conditionalUserProperty.mTriggeredEventParams = zzcfwVar.zzbpn.zzbpQ.zzyr();
                }
            }
            conditionalUserProperty.mTriggeredTimestamp = zzcfwVar.zzbph.zzbuC;
            conditionalUserProperty.mTimeToLive = zzcfwVar.zzbpo;
            if (zzcfwVar.zzbpp != null) {
                conditionalUserProperty.mExpiredEventName = zzcfwVar.zzbpp.name;
                if (zzcfwVar.zzbpp.zzbpQ != null) {
                    conditionalUserProperty.mExpiredEventParams = zzcfwVar.zzbpp.zzbpQ.zzyr();
                }
            }
            arrayList.add(conditionalUserProperty);
        }
        return arrayList;
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        super.zzwo();
        zza((String) null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        zzbr.zzcF(str);
        super.zzwn();
        zza(str, str2, str3, bundle);
    }

    public final Task<String> getAppInstanceId() {
        try {
            String strZzyF = super.zzwF().zzyF();
            return strZzyF != null ? Tasks.forResult(strZzyF) : Tasks.call(super.zzwD().zzyL(), new zzcji(this));
        } catch (Exception e) {
            super.zzwE().zzyx().log("Failed to schedule task for getAppInstanceId");
            return Tasks.forException(e);
        }
    }

    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        super.zzwo();
        return zzl(null, str, str2);
    }

    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        zzbr.zzcF(str);
        super.zzwn();
        return zzl(str, str2, str3);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        super.zzwo();
        return zzb(null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        zzbr.zzcF(str);
        super.zzwn();
        return zzb(str, str2, str3, z);
    }

    public final void registerOnMeasurementEventListener(AppMeasurement.OnEventListener onEventListener) {
        super.zzwo();
        zzkC();
        zzbr.zzu(onEventListener);
        if (this.zzbtu.add(onEventListener)) {
            return;
        }
        super.zzwE().zzyx().log("OnEventListener already registered");
    }

    public final void setConditionalUserProperty(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        zzbr.zzu(conditionalUserProperty);
        super.zzwo();
        AppMeasurement.ConditionalUserProperty conditionalUserProperty2 = new AppMeasurement.ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            super.zzwE().zzyx().log("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        zzbr.zzu(conditionalUserProperty);
        zzbr.zzcF(conditionalUserProperty.mAppId);
        super.zzwn();
        zza(new AppMeasurement.ConditionalUserProperty(conditionalUserProperty));
    }

    @WorkerThread
    public final void setEventInterceptor(AppMeasurement.EventInterceptor eventInterceptor) {
        super.zzjB();
        super.zzwo();
        zzkC();
        if (eventInterceptor != null && eventInterceptor != this.zzbtt) {
            zzbr.zza(this.zzbtt == null, "EventInterceptor already set.");
        }
        this.zzbtt = eventInterceptor;
    }

    public final void setMeasurementEnabled(boolean z) {
        zzkC();
        super.zzwo();
        super.zzwD().zzj(new zzciy(this, z));
    }

    public final void setMinimumSessionDuration(long j) {
        super.zzwo();
        super.zzwD().zzj(new zzcjd(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        super.zzwo();
        super.zzwD().zzj(new zzcje(this, j));
    }

    public final void unregisterOnMeasurementEventListener(AppMeasurement.OnEventListener onEventListener) {
        super.zzwo();
        zzkC();
        zzbr.zzu(onEventListener);
        if (this.zzbtu.remove(onEventListener)) {
            return;
        }
        super.zzwE().zzyx().log("OnEventListener had not been registered");
    }

    public final void zza(String str, String str2, Bundle bundle, long j) {
        super.zzwo();
        zza(str, str2, j, bundle, false, true, true, null);
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        super.zzwo();
        zza(str, str2, bundle, true, this.zzbtt == null || zzckx.zzey(str2), true, null);
    }

    @Nullable
    final String zzac(long j) {
        if (super.zzwD().zzyK()) {
            super.zzwE().zzyv().log("Cannot retrieve app instance id from analytics worker thread");
            return null;
        }
        super.zzwD();
        if (zzchs.zzR()) {
            super.zzwE().zzyv().log("Cannot retrieve app instance id from main thread");
            return null;
        }
        long jElapsedRealtime = super.zzkp().elapsedRealtime();
        String strZzad = zzad(120000L);
        long jElapsedRealtime2 = super.zzkp().elapsedRealtime() - jElapsedRealtime;
        return (strZzad != null || jElapsedRealtime2 >= 120000) ? strZzad : zzad(120000 - jElapsedRealtime2);
    }

    public final List<zzcku> zzao(boolean z) {
        super.zzwo();
        zzkC();
        super.zzwE().zzyA().log("Fetching user attributes (FE)");
        if (super.zzwD().zzyK()) {
            super.zzwE().zzyv().log("Cannot get all user properties from analytics worker thread");
            return Collections.emptyList();
        }
        super.zzwD();
        if (zzchs.zzR()) {
            super.zzwE().zzyv().log("Cannot get all user properties from main thread");
            return Collections.emptyList();
        }
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            this.zzboi.zzwD().zzj(new zzcjh(this, atomicReference, z));
            try {
                atomicReference.wait(5000L);
            } catch (InterruptedException e) {
                super.zzwE().zzyx().zzj("Interrupted waiting for get user properties", e);
            }
        }
        List<zzcku> list = (List) atomicReference.get();
        if (list != null) {
            return list;
        }
        super.zzwE().zzyx().log("Timed out waiting for get user properties");
        return Collections.emptyList();
    }

    public final void zzb(String str, String str2, Object obj) {
        zzbr.zzcF(str);
        long jCurrentTimeMillis = super.zzkp().currentTimeMillis();
        int iZzet = super.zzwA().zzet(str2);
        if (iZzet != 0) {
            super.zzwA();
            this.zzboi.zzwA().zza(iZzet, "_ev", zzckx.zza(str2, zzcfy.zzxh(), true), str2 != null ? str2.length() : 0);
            return;
        }
        if (obj == null) {
            zza(str, str2, jCurrentTimeMillis, (Object) null);
            return;
        }
        int iZzl = super.zzwA().zzl(str2, obj);
        if (iZzl != 0) {
            super.zzwA();
            this.zzboi.zzwA().zza(iZzl, "_ev", zzckx.zza(str2, zzcfy.zzxh(), true), ((obj instanceof String) || (obj instanceof CharSequence)) ? String.valueOf(obj).length() : 0);
        } else {
            Object objZzm = super.zzwA().zzm(str2, obj);
            if (objZzm != null) {
                zza(str, str2, jCurrentTimeMillis, objZzm);
            }
        }
    }

    public final void zzd(String str, String str2, Bundle bundle) {
        super.zzwo();
        zza(str, str2, bundle, true, this.zzbtt == null || zzckx.zzey(str2), false, null);
    }

    final void zzef(@Nullable String str) {
        this.zzbtw.set(str);
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzjB() {
        super.zzjB();
    }

    @Override // com.google.android.gms.internal.zzciv
    protected final void zzjC() {
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ com.google.android.gms.common.util.zzf zzkp() {
        return super.zzkp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckx zzwA() {
        return super.zzwA();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchr zzwB() {
        return super.zzwB();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzckm zzwC() {
        return super.zzwC();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchs zzwD() {
        return super.zzwD();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgx zzwE() {
        return super.zzwE();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzchi zzwF() {
        return super.zzwF();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfy zzwG() {
        return super.zzwG();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwn() {
        super.zzwn();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwo() {
        super.zzwo();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ void zzwp() {
        super.zzwp();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfo zzwq() {
        return super.zzwq();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfv zzwr() {
        return super.zzwr();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcix zzws() {
        return super.zzws();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgs zzwt() {
        return super.zzwt();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgf zzwu() {
        return super.zzwu();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjp zzwv() {
        return super.zzwv();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcjl zzww() {
        return super.zzww();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgt zzwx() {
        return super.zzwx();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcfz zzwy() {
        return super.zzwy();
    }

    @Override // com.google.android.gms.internal.zzciu
    public final /* bridge */ /* synthetic */ zzcgv zzwz() {
        return super.zzwz();
    }

    @Nullable
    public final String zzyF() {
        super.zzwo();
        return this.zzbtw.get();
    }
}
