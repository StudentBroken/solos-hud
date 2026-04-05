package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes42.dex */
public class Finalizer implements Runnable {
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    private static final Field zzbWS = zzEq();
    private final WeakReference<Class<?>> zzbWP;
    private final PhantomReference<Object> zzbWQ;
    private final ReferenceQueue<Object> zzbWR;

    private Finalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        this.zzbWR = referenceQueue;
        this.zzbWP = new WeakReference<>(cls);
        this.zzbWQ = phantomReference;
    }

    public static void startFinalizer(Class<?> cls, ReferenceQueue<Object> referenceQueue, PhantomReference<Object> phantomReference) {
        if (!cls.getName().equals("com.google.common.base.FinalizableReference")) {
            throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
        }
        Thread thread = new Thread(new Finalizer(cls, referenceQueue, phantomReference));
        thread.setName(Finalizer.class.getName());
        thread.setDaemon(true);
        try {
            if (zzbWS != null) {
                zzbWS.set(thread, null);
            }
        } catch (Throwable th) {
            logger.logp(Level.INFO, "com.google.common.base.internal.Finalizer", "startFinalizer", "Failed to clear thread local values inherited by reference finalizer thread.", th);
        }
        thread.start();
    }

    private final Method zzEp() {
        Class<?> cls = this.zzbWP.get();
        if (cls == null) {
            return null;
        }
        try {
            return cls.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private static Field zzEq() {
        try {
            Field declaredField = Thread.class.getDeclaredField("inheritableThreadLocals");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            logger.logp(Level.INFO, "com.google.common.base.internal.Finalizer", "getInheritableThreadLocalsField", "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }

    private final boolean zza(Reference<?> reference) {
        Method methodZzEp = zzEp();
        if (methodZzEp == null) {
            return false;
        }
        do {
            reference.clear();
            if (reference == this.zzbWQ) {
                return false;
            }
            try {
                methodZzEp.invoke(reference, new Object[0]);
            } catch (Throwable th) {
                logger.logp(Level.SEVERE, "com.google.common.base.internal.Finalizer", "cleanUp", "Error cleaning up after reference.", th);
            }
            reference = this.zzbWR.poll();
        } while (reference != null);
        return true;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (zza(this.zzbWR.remove())) {
        }
    }
}
