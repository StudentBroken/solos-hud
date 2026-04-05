package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes40.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class Lifecycling {
    private static Map<Class, Constructor<? extends GenericLifecycleObserver>> sCallbackCache;
    private static Constructor<? extends GenericLifecycleObserver> sREFLECTIVE;

    Lifecycling() {
    }

    static {
        try {
            sREFLECTIVE = ReflectiveGenericLifecycleObserver.class.getDeclaredConstructor(Object.class);
        } catch (NoSuchMethodException e) {
        }
        sCallbackCache = new HashMap();
    }

    @NonNull
    static GenericLifecycleObserver getCallback(Object object) {
        if (object instanceof GenericLifecycleObserver) {
            return (GenericLifecycleObserver) object;
        }
        try {
            Class<?> klass = object.getClass();
            Constructor<? extends GenericLifecycleObserver> cachedConstructor = sCallbackCache.get(klass);
            if (cachedConstructor != null) {
                return cachedConstructor.newInstance(object);
            }
            Constructor<? extends GenericLifecycleObserver> cachedConstructor2 = getGeneratedAdapterConstructor(klass);
            if (cachedConstructor2 != null) {
                if (!cachedConstructor2.isAccessible()) {
                    cachedConstructor2.setAccessible(true);
                }
            } else {
                cachedConstructor2 = sREFLECTIVE;
            }
            sCallbackCache.put(klass, cachedConstructor2);
            return cachedConstructor2.newInstance(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    @Nullable
    private static Constructor<? extends GenericLifecycleObserver> getGeneratedAdapterConstructor(Class<?> klass) {
        Package aPackage = klass.getPackage();
        String fullPackage = aPackage != null ? aPackage.getName() : "";
        String name = klass.getCanonicalName();
        if (name == null) {
            return null;
        }
        if (!fullPackage.isEmpty()) {
            name = name.substring(fullPackage.length() + 1);
        }
        String adapterName = getAdapterName(name);
        try {
            if (!fullPackage.isEmpty()) {
                adapterName = fullPackage + "." + adapterName;
            }
            return Class.forName(adapterName).getDeclaredConstructor(klass);
        } catch (ClassNotFoundException e) {
            Class<?> superclass = klass.getSuperclass();
            if (superclass != null) {
                return getGeneratedAdapterConstructor(superclass);
            }
            return null;
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        }
    }

    static String getAdapterName(String className) {
        return className.replace(".", EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR) + "_LifecycleAdapter";
    }
}
