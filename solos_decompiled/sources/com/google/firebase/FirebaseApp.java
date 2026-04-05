package com.google.firebase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbh;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.util.zzt;
import com.google.android.gms.internal.aer;
import com.google.android.gms.internal.aes;
import com.google.android.gms.internal.aet;
import com.google.android.gms.internal.aeu;
import com.google.android.gms.internal.zzbci;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.GetTokenResult;
import com.ua.sdk.cache.EntityDatabase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: loaded from: classes42.dex */
public class FirebaseApp {
    public static final String DEFAULT_APP_NAME = "[DEFAULT]";
    private final Context mApplicationContext;
    private final String mName;
    private final FirebaseOptions zzbWY;
    private aet zzbXe;
    private static final List<String> zzbWT = Arrays.asList("com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId");
    private static final List<String> zzbWU = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
    private static final List<String> zzbWV = Arrays.asList("com.google.android.gms.measurement.AppMeasurement");
    private static final List<String> zzbWW = Arrays.asList(new String[0]);
    private static final Set<String> zzbWX = Collections.emptySet();
    private static final Object zzuI = new Object();
    static final Map<String, FirebaseApp> zzbgU = new ArrayMap();
    private final AtomicBoolean zzbWZ = new AtomicBoolean(false);
    private final AtomicBoolean zzbXa = new AtomicBoolean();
    private final List<zza> zzbXb = new CopyOnWriteArrayList();
    private final List<zzc> zzbXc = new CopyOnWriteArrayList();
    private final List<Object> zzbXd = new CopyOnWriteArrayList();
    private zzb zzbXf = new aer();

    public interface zza {
        void zzb(@NonNull aeu aeuVar);
    }

    public interface zzb {
    }

    public interface zzc {
        void zzac(boolean z);
    }

    @TargetApi(24)
    static class zzd extends BroadcastReceiver {
        private static AtomicReference<zzd> zzbXg = new AtomicReference<>();
        private final Context mApplicationContext;

        private zzd(Context context) {
            this.mApplicationContext = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void zzbB(Context context) {
            if (zzbXg.get() == null) {
                zzd zzdVar = new zzd(context);
                if (zzbXg.compareAndSet(null, zzdVar)) {
                    context.registerReceiver(zzdVar, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            synchronized (FirebaseApp.zzuI) {
                Iterator<FirebaseApp> it = FirebaseApp.zzbgU.values().iterator();
                while (it.hasNext()) {
                    it.next().zzEv();
                }
            }
            this.mApplicationContext.unregisterReceiver(this);
        }
    }

    private FirebaseApp(Context context, String str, FirebaseOptions firebaseOptions) {
        this.mApplicationContext = (Context) zzbr.zzu(context);
        this.mName = zzbr.zzcF(str);
        this.zzbWY = (FirebaseOptions) zzbr.zzu(firebaseOptions);
    }

    public static List<FirebaseApp> getApps(Context context) {
        ArrayList arrayList;
        aes.zzbL(context);
        synchronized (zzuI) {
            arrayList = new ArrayList(zzbgU.values());
            aes.zzKo();
            Set<String> setZzKp = aes.zzKp();
            setZzKp.removeAll(zzbgU.keySet());
            for (String str : setZzKp) {
                aes.zzhP(str);
                arrayList.add(initializeApp(context, null, str));
            }
        }
        return arrayList;
    }

    @Nullable
    public static FirebaseApp getInstance() {
        FirebaseApp firebaseApp;
        synchronized (zzuI) {
            firebaseApp = zzbgU.get(DEFAULT_APP_NAME);
            if (firebaseApp == null) {
                String strValueOf = String.valueOf(zzt.zzse());
                throw new IllegalStateException(new StringBuilder(String.valueOf(strValueOf).length() + 116).append("Default FirebaseApp is not initialized in this process ").append(strValueOf).append(". Make sure to call FirebaseApp.initializeApp(Context) first.").toString());
            }
        }
        return firebaseApp;
    }

    public static FirebaseApp getInstance(@NonNull String str) {
        FirebaseApp firebaseApp;
        String strConcat;
        synchronized (zzuI) {
            firebaseApp = zzbgU.get(str.trim());
            if (firebaseApp == null) {
                List<String> listZzEu = zzEu();
                if (listZzEu.isEmpty()) {
                    strConcat = "";
                } else {
                    String strValueOf = String.valueOf(TextUtils.join(", ", listZzEu));
                    strConcat = strValueOf.length() != 0 ? "Available app names: ".concat(strValueOf) : new String("Available app names: ");
                }
                throw new IllegalStateException(String.format("FirebaseApp with name %s doesn't exist. %s", str, strConcat));
            }
        }
        return firebaseApp;
    }

    @Nullable
    public static FirebaseApp initializeApp(Context context) {
        FirebaseApp firebaseAppInitializeApp;
        synchronized (zzuI) {
            if (zzbgU.containsKey(DEFAULT_APP_NAME)) {
                firebaseAppInitializeApp = getInstance();
            } else {
                FirebaseOptions firebaseOptionsFromResource = FirebaseOptions.fromResource(context);
                firebaseAppInitializeApp = firebaseOptionsFromResource == null ? null : initializeApp(context, firebaseOptionsFromResource);
            }
        }
        return firebaseAppInitializeApp;
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, DEFAULT_APP_NAME);
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String str) {
        FirebaseApp firebaseApp;
        aes.zzbL(context);
        if (context.getApplicationContext() instanceof Application) {
            zzbci.zza((Application) context.getApplicationContext());
            zzbci.zzpt().zza(new com.google.firebase.zza());
        }
        String strTrim = str.trim();
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        synchronized (zzuI) {
            zzbr.zza(!zzbgU.containsKey(strTrim), new StringBuilder(String.valueOf(strTrim).length() + 33).append("FirebaseApp name ").append(strTrim).append(" already exists!").toString());
            zzbr.zzb(context, "Application context cannot be null.");
            firebaseApp = new FirebaseApp(context, strTrim, firebaseOptions);
            zzbgU.put(strTrim, firebaseApp);
        }
        aes.zze(firebaseApp);
        firebaseApp.zza(FirebaseApp.class, firebaseApp, zzbWT);
        if (firebaseApp.zzEs()) {
            firebaseApp.zza(FirebaseApp.class, firebaseApp, zzbWU);
            firebaseApp.zza(Context.class, firebaseApp.getApplicationContext(), zzbWV);
        }
        return firebaseApp;
    }

    private final void zzEr() {
        zzbr.zza(!this.zzbXa.get(), "FirebaseApp was deleted");
    }

    private static List<String> zzEu() {
        com.google.android.gms.common.util.zzb zzbVar = new com.google.android.gms.common.util.zzb();
        synchronized (zzuI) {
            Iterator<FirebaseApp> it = zzbgU.values().iterator();
            while (it.hasNext()) {
                zzbVar.add(it.next().getName());
            }
            if (aes.zzKo() != null) {
                zzbVar.addAll(aes.zzKp());
            }
        }
        ArrayList arrayList = new ArrayList(zzbVar);
        Collections.sort(arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzEv() {
        zza(FirebaseApp.class, this, zzbWT);
        if (zzEs()) {
            zza(FirebaseApp.class, this, zzbWU);
            zza(Context.class, this.mApplicationContext, zzbWV);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final <T> void zza(Class<T> cls, T t, Iterable<String> iterable) {
        boolean zIsDeviceProtectedStorage = ContextCompat.isDeviceProtectedStorage(this.mApplicationContext);
        if (zIsDeviceProtectedStorage) {
            zzd.zzbB(this.mApplicationContext);
        }
        for (String str : iterable) {
            if (zIsDeviceProtectedStorage) {
                try {
                } catch (ClassNotFoundException e) {
                    if (zzbWX.contains(str)) {
                        throw new IllegalStateException(String.valueOf(str).concat(" is missing, but is required. Check if it has been removed by Proguard."));
                    }
                    Log.d("FirebaseApp", String.valueOf(str).concat(" is not linked. Skipping initialization."));
                } catch (IllegalAccessException e2) {
                    String strValueOf = String.valueOf(str);
                    Log.wtf("FirebaseApp", strValueOf.length() != 0 ? "Failed to initialize ".concat(strValueOf) : new String("Failed to initialize "), e2);
                } catch (NoSuchMethodException e3) {
                    throw new IllegalStateException(String.valueOf(str).concat("#getInstance has been removed by Proguard. Add keep rule to prevent it."));
                } catch (InvocationTargetException e4) {
                    Log.wtf("FirebaseApp", "Firebase API initialization failure.", e4);
                }
                if (zzbWW.contains(str)) {
                }
            }
            Method method = Class.forName(str).getMethod("getInstance", cls);
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                method.invoke(null, t);
            }
        }
    }

    public static void zzac(boolean z) {
        synchronized (zzuI) {
            ArrayList arrayList = new ArrayList(zzbgU.values());
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                FirebaseApp firebaseApp = (FirebaseApp) obj;
                if (firebaseApp.zzbWZ.get()) {
                    firebaseApp.zzav(z);
                }
            }
        }
    }

    private final void zzav(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        Iterator<zzc> it = this.zzbXc.iterator();
        while (it.hasNext()) {
            it.next().zzac(z);
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof FirebaseApp) {
            return this.mName.equals(((FirebaseApp) obj).getName());
        }
        return false;
    }

    @NonNull
    public Context getApplicationContext() {
        zzEr();
        return this.mApplicationContext;
    }

    @NonNull
    public String getName() {
        zzEr();
        return this.mName;
    }

    @NonNull
    public FirebaseOptions getOptions() {
        zzEr();
        return this.zzbWY;
    }

    public final Task<GetTokenResult> getToken(boolean z) {
        zzEr();
        return this.zzbXe == null ? Tasks.forException(new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.")) : this.zzbXe.zzaw(z);
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public void setAutomaticResourceManagementEnabled(boolean z) {
        zzEr();
        if (this.zzbWZ.compareAndSet(!z, z)) {
            boolean zZzpu = zzbci.zzpt().zzpu();
            if (z && zZzpu) {
                zzav(true);
            } else {
                if (z || !zZzpu) {
                    return;
                }
                zzav(false);
            }
        }
    }

    public String toString() {
        return zzbh.zzt(this).zzg("name", this.mName).zzg(EntityDatabase.META.COLS.OPTIONS, this.zzbWY).toString();
    }

    public final boolean zzEs() {
        return DEFAULT_APP_NAME.equals(getName());
    }

    public final String zzEt() {
        String strValueOf = String.valueOf(com.google.android.gms.common.util.zzd.zzi(getName().getBytes()));
        String strValueOf2 = String.valueOf(com.google.android.gms.common.util.zzd.zzi(getOptions().getApplicationId().getBytes()));
        return new StringBuilder(String.valueOf(strValueOf).length() + 1 + String.valueOf(strValueOf2).length()).append(strValueOf).append("+").append(strValueOf2).toString();
    }

    public final void zza(@NonNull aet aetVar) {
        this.zzbXe = (aet) zzbr.zzu(aetVar);
    }

    @UiThread
    public final void zza(@NonNull aeu aeuVar) {
        Log.d("FirebaseApp", "Notifying auth state listeners.");
        Iterator<zza> it = this.zzbXb.iterator();
        int i = 0;
        while (it.hasNext()) {
            it.next().zzb(aeuVar);
            i++;
        }
        Log.d("FirebaseApp", String.format("Notified %d auth state listeners.", Integer.valueOf(i)));
    }

    public final void zza(@NonNull zza zzaVar) {
        zzEr();
        zzbr.zzu(zzaVar);
        this.zzbXb.add(zzaVar);
        this.zzbXb.size();
    }

    public final void zza(zzc zzcVar) {
        zzEr();
        if (this.zzbWZ.get() && zzbci.zzpt().zzpu()) {
            zzcVar.zzac(true);
        }
        this.zzbXc.add(zzcVar);
    }
}
