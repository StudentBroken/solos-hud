package com.google.protobuf;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

/* JADX INFO: loaded from: classes67.dex */
final class zze {
    private static final Logger logger = Logger.getLogger(zze.class.getName());
    private static final Unsafe zzcuB = zzLL();
    private static final Class<?> zzcuC = zzio("libcore.io.Memory");
    private static final boolean zzcuD;
    private static final boolean zzcuE;
    private static final boolean zzcuF;
    private static final zzd zzcuG;
    private static final boolean zzcuH;
    private static final boolean zzcuI;
    private static final long zzcuJ;
    private static final boolean zzcuK;
    private static final boolean zzcuu;
    private static final long zzcuv;

    static final class zza extends zzd {
        zza(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static final class zzb extends zzd {
        zzb(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static final class zzc extends zzd {
        zzc(Unsafe unsafe) {
            super(unsafe);
        }
    }

    static abstract class zzd {
        Unsafe zzcuL;

        zzd(Unsafe unsafe) {
            this.zzcuL = unsafe;
        }
    }

    static {
        Field fieldZza;
        zzcuD = zzio("org.robolectric.Robolectric") != null;
        zzcuE = zzg(Long.TYPE);
        zzcuF = zzg(Integer.TYPE);
        zzcuG = zzcuB == null ? null : zzLP() ? zzcuE ? new zzb(zzcuB) : zzcuF ? new zza(zzcuB) : null : new zzc(zzcuB);
        zzcuH = zzLO();
        zzcuu = zzLM();
        zzcuI = zzLN();
        zzcuv = zzcuu ? zzcuG.zzcuL.arrayBaseOffset(byte[].class) : -1;
        if (!zzLP() || (fieldZza = zza(Buffer.class, "effectiveDirectAddress")) == null) {
            fieldZza = zza(Buffer.class, "address");
        }
        zzcuJ = (fieldZza == null || zzcuG == null) ? -1L : zzcuG.zzcuL.objectFieldOffset(fieldZza);
        zzcuK = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    }

    private zze() {
    }

    static boolean zzLJ() {
        return zzcuu;
    }

    static long zzLK() {
        return zzcuv;
    }

    private static Unsafe zzLL() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzf());
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzLM() {
        if (zzcuB == null) {
            return false;
        }
        try {
            Class<?> cls = zzcuB.getClass();
            cls.getMethod("objectFieldOffset", Field.class);
            cls.getMethod("arrayBaseOffset", Class.class);
            cls.getMethod("getInt", Object.class, Long.TYPE);
            cls.getMethod("putInt", Object.class, Long.TYPE, Integer.TYPE);
            cls.getMethod("getLong", Object.class, Long.TYPE);
            cls.getMethod("putLong", Object.class, Long.TYPE, Long.TYPE);
            cls.getMethod("getObject", Object.class, Long.TYPE);
            cls.getMethod("putObject", Object.class, Long.TYPE, Object.class);
            if (zzLP()) {
                return true;
            }
            cls.getMethod("getByte", Object.class, Long.TYPE);
            cls.getMethod("putByte", Object.class, Long.TYPE, Byte.TYPE);
            cls.getMethod("getBoolean", Object.class, Long.TYPE);
            cls.getMethod("putBoolean", Object.class, Long.TYPE, Boolean.TYPE);
            cls.getMethod("getFloat", Object.class, Long.TYPE);
            cls.getMethod("putFloat", Object.class, Long.TYPE, Float.TYPE);
            cls.getMethod("getDouble", Object.class, Long.TYPE);
            cls.getMethod("putDouble", Object.class, Long.TYPE, Double.TYPE);
            return true;
        } catch (Throwable th) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            String strValueOf = String.valueOf(th);
            logger2.logp(level, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", new StringBuilder(String.valueOf(strValueOf).length() + 71).append("platform method missing - proto runtime falling back to safer methods: ").append(strValueOf).toString());
            return false;
        }
    }

    private static boolean zzLN() {
        if (zzcuB == null) {
            return false;
        }
        try {
            zzcuB.getClass().getMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
            return true;
        } catch (Throwable th) {
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeCopyMemory", "copyMemory is missing from platform - proto runtime falling back to safer methods.");
            return false;
        }
    }

    private static boolean zzLO() {
        if (zzcuB == null) {
            return false;
        }
        try {
            Class<?> cls = zzcuB.getClass();
            cls.getMethod("objectFieldOffset", Field.class);
            cls.getMethod("getLong", Object.class, Long.TYPE);
            if (zzLP()) {
                return true;
            }
            cls.getMethod("getByte", Long.TYPE);
            cls.getMethod("putByte", Long.TYPE, Byte.TYPE);
            cls.getMethod("getInt", Long.TYPE);
            cls.getMethod("putInt", Long.TYPE, Integer.TYPE);
            cls.getMethod("getLong", Long.TYPE);
            cls.getMethod("putLong", Long.TYPE, Long.TYPE);
            cls.getMethod("copyMemory", Long.TYPE, Long.TYPE, Long.TYPE);
            return true;
        } catch (Throwable th) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            String strValueOf = String.valueOf(th);
            logger2.logp(level, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", new StringBuilder(String.valueOf(strValueOf).length() + 71).append("platform method missing - proto runtime falling back to safer methods: ").append(strValueOf).toString());
            return false;
        }
    }

    private static boolean zzLP() {
        return (zzcuC == null || zzcuD) ? false : true;
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzg(Class<?> cls) {
        if (!zzLP()) {
            return false;
        }
        try {
            Class<?> cls2 = zzcuC;
            cls2.getMethod("peekLong", cls, Boolean.TYPE);
            cls2.getMethod("pokeLong", cls, Long.TYPE, Boolean.TYPE);
            cls2.getMethod("pokeInt", cls, Integer.TYPE, Boolean.TYPE);
            cls2.getMethod("peekInt", cls, Boolean.TYPE);
            cls2.getMethod("pokeByte", cls, Byte.TYPE);
            cls2.getMethod("peekByte", cls);
            cls2.getMethod("pokeByteArray", cls, byte[].class, Integer.TYPE, Integer.TYPE);
            cls2.getMethod("peekByteArray", cls, byte[].class, Integer.TYPE, Integer.TYPE);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static <T> Class<T> zzio(String str) {
        try {
            return (Class<T>) Class.forName(str);
        } catch (Throwable th) {
            return null;
        }
    }
}
