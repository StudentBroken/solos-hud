package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzn;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/* JADX INFO: loaded from: classes67.dex */
public final class DynamiteModule {
    private static Boolean zzaSJ;
    private static zzj zzaSK;
    private static zzl zzaSL;
    private static String zzaSM;
    private static final ThreadLocal<zza> zzaSN = new ThreadLocal<>();
    private static final zzh zzaSO = new com.google.android.gms.dynamite.zza();
    public static final zzd zzaSP = new com.google.android.gms.dynamite.zzb();
    private static zzd zzaSQ = new com.google.android.gms.dynamite.zzc();
    public static final zzd zzaSR = new com.google.android.gms.dynamite.zzd();
    public static final zzd zzaSS = new zze();
    public static final zzd zzaST = new zzf();
    private final Context zzaSU;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    static class zza {
        public Cursor zzaSV;

        private zza() {
        }

        /* synthetic */ zza(com.google.android.gms.dynamite.zza zzaVar) {
            this();
        }
    }

    static class zzb implements zzh {
        private final int zzaSW;
        private final int zzaSX = 0;

        public zzb(int i, int i2) {
            this.zzaSW = i;
        }

        @Override // com.google.android.gms.dynamite.zzh
        public final int zzF(Context context, String str) {
            return this.zzaSW;
        }

        @Override // com.google.android.gms.dynamite.zzh
        public final int zzb(Context context, String str, boolean z) {
            return 0;
        }
    }

    public static class zzc extends Exception {
        private zzc(String str) {
            super(str);
        }

        /* synthetic */ zzc(String str, com.google.android.gms.dynamite.zza zzaVar) {
            this(str);
        }

        private zzc(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ zzc(String str, Throwable th, com.google.android.gms.dynamite.zza zzaVar) {
            this(str, th);
        }
    }

    public interface zzd {
        zzi zza(Context context, String str, zzh zzhVar) throws zzc;
    }

    private DynamiteModule(Context context) {
        this.zzaSU = (Context) zzbr.zzu(context);
    }

    public static int zzF(Context context, String str) {
        int i;
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            String strValueOf = String.valueOf("com.google.android.gms.dynamite.descriptors.");
            String strValueOf2 = String.valueOf("ModuleDescriptor");
            Class<?> clsLoadClass = classLoader.loadClass(new StringBuilder(String.valueOf(strValueOf).length() + 1 + String.valueOf(str).length() + String.valueOf(strValueOf2).length()).append(strValueOf).append(str).append(".").append(strValueOf2).toString());
            Field declaredField = clsLoadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = clsLoadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                i = declaredField2.getInt(null);
            } else {
                String strValueOf3 = String.valueOf(declaredField.get(null));
                Log.e("DynamiteModule", new StringBuilder(String.valueOf(strValueOf3).length() + 51 + String.valueOf(str).length()).append("Module descriptor id '").append(strValueOf3).append("' didn't match expected id '").append(str).append("'").toString());
                i = 0;
            }
            return i;
        } catch (ClassNotFoundException e) {
            Log.w("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 45).append("Local module descriptor class for ").append(str).append(" not found.").toString());
            return 0;
        } catch (Exception e2) {
            String strValueOf4 = String.valueOf(e2.getMessage());
            Log.e("DynamiteModule", strValueOf4.length() != 0 ? "Failed to load module descriptor class: ".concat(strValueOf4) : new String("Failed to load module descriptor class: "));
            return 0;
        }
    }

    public static int zzG(Context context, String str) {
        return zzb(context, str, false);
    }

    private static DynamiteModule zzH(Context context, String str) {
        String strValueOf = String.valueOf(str);
        Log.i("DynamiteModule", strValueOf.length() != 0 ? "Selected local version of ".concat(strValueOf) : new String("Selected local version of "));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, zzl zzlVar) {
        try {
            return (Context) zzn.zzE(zzlVar.zza(zzn.zzw(context), str, i, zzn.zzw(cursor)));
        } catch (Exception e) {
            String strValueOf = String.valueOf(e.toString());
            Log.e("DynamiteModule", strValueOf.length() != 0 ? "Failed to load DynamiteLoader: ".concat(strValueOf) : new String("Failed to load DynamiteLoader: "));
            return null;
        }
    }

    public static DynamiteModule zza(Context context, zzd zzdVar, String str) throws zzc {
        zza zzaVar = zzaSN.get();
        zza zzaVar2 = new zza(null);
        zzaSN.set(zzaVar2);
        try {
            zzi zziVarZza = zzdVar.zza(context, str, zzaSO);
            Log.i("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 68 + String.valueOf(str).length()).append("Considering local module ").append(str).append(":").append(zziVarZza.zzaSY).append(" and remote module ").append(str).append(":").append(zziVarZza.zzaSZ).toString());
            if (zziVarZza.zzaTa == 0 || ((zziVarZza.zzaTa == -1 && zziVarZza.zzaSY == 0) || (zziVarZza.zzaTa == 1 && zziVarZza.zzaSZ == 0))) {
                throw new zzc(new StringBuilder(91).append("No acceptable module found. Local version is ").append(zziVarZza.zzaSY).append(" and remote version is ").append(zziVarZza.zzaSZ).append(".").toString(), (com.google.android.gms.dynamite.zza) null);
            }
            if (zziVarZza.zzaTa == -1) {
                DynamiteModule dynamiteModuleZzH = zzH(context, str);
                if (zzaVar2.zzaSV != null) {
                    zzaVar2.zzaSV.close();
                }
                zzaSN.set(zzaVar);
                return dynamiteModuleZzH;
            }
            if (zziVarZza.zzaTa != 1) {
                throw new zzc(new StringBuilder(47).append("VersionPolicy returned invalid code:").append(zziVarZza.zzaTa).toString(), (com.google.android.gms.dynamite.zza) null);
            }
            try {
                DynamiteModule dynamiteModuleZza = zza(context, str, zziVarZza.zzaSZ);
                if (zzaVar2.zzaSV != null) {
                    zzaVar2.zzaSV.close();
                }
                zzaSN.set(zzaVar);
                return dynamiteModuleZza;
            } catch (zzc e) {
                String strValueOf = String.valueOf(e.getMessage());
                Log.w("DynamiteModule", strValueOf.length() != 0 ? "Failed to load remote module: ".concat(strValueOf) : new String("Failed to load remote module: "));
                if (zziVarZza.zzaSY == 0 || zzdVar.zza(context, str, new zzb(zziVarZza.zzaSY, 0)).zzaTa != -1) {
                    throw new zzc("Remote load failed. No local fallback found.", e, null);
                }
                DynamiteModule dynamiteModuleZzH2 = zzH(context, str);
                if (zzaVar2.zzaSV != null) {
                    zzaVar2.zzaSV.close();
                }
                zzaSN.set(zzaVar);
                return dynamiteModuleZzH2;
            }
        } catch (Throwable th) {
            if (zzaVar2.zzaSV != null) {
                zzaVar2.zzaSV.close();
            }
            zzaSN.set(zzaVar);
            throw th;
        }
    }

    private static DynamiteModule zza(Context context, String str, int i) throws zzc {
        Boolean bool;
        synchronized (DynamiteModule.class) {
            bool = zzaSJ;
        }
        if (bool == null) {
            throw new zzc("Failed to determine which loading route to use.", (com.google.android.gms.dynamite.zza) null);
        }
        return bool.booleanValue() ? zzc(context, str, i) : zzb(context, str, i);
    }

    private static void zza(ClassLoader classLoader) throws zzc {
        zzl zzmVar;
        com.google.android.gms.dynamite.zza zzaVar = null;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzmVar = null;
            } else {
                IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                zzmVar = iInterfaceQueryLocalInterface instanceof zzl ? (zzl) iInterfaceQueryLocalInterface : new zzm(iBinder);
            }
            zzaSL = zzmVar;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new zzc("Failed to instantiate dynamite loader", e, zzaVar);
        }
    }

    private static zzj zzaT(Context context) {
        zzj zzkVar;
        synchronized (DynamiteModule.class) {
            if (zzaSK != null) {
                return zzaSK;
            }
            if (com.google.android.gms.common.zze.zzoU().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            }
            try {
                IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                if (iBinder == null) {
                    zzkVar = null;
                } else {
                    IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                    zzkVar = iInterfaceQueryLocalInterface instanceof zzj ? (zzj) iInterfaceQueryLocalInterface : new zzk(iBinder);
                }
                if (zzkVar != null) {
                    zzaSK = zzkVar;
                    return zzkVar;
                }
            } catch (Exception e) {
                String strValueOf = String.valueOf(e.getMessage());
                Log.e("DynamiteModule", strValueOf.length() != 0 ? "Failed to load IDynamiteLoader from GmsCore: ".concat(strValueOf) : new String("Failed to load IDynamiteLoader from GmsCore: "));
            }
            return null;
        }
    }

    public static int zzb(Context context, String str, boolean z) {
        Class<?> clsLoadClass;
        Field declaredField;
        synchronized (DynamiteModule.class) {
            Boolean bool = zzaSJ;
            if (bool == null) {
                try {
                    clsLoadClass = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName());
                    declaredField = clsLoadClass.getDeclaredField("sClassLoader");
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
                    String strValueOf = String.valueOf(e);
                    Log.w("DynamiteModule", new StringBuilder(String.valueOf(strValueOf).length() + 30).append("Failed to load module via V2: ").append(strValueOf).toString());
                    bool = Boolean.FALSE;
                }
                synchronized (clsLoadClass) {
                    ClassLoader classLoader = (ClassLoader) declaredField.get(null);
                    if (classLoader != null) {
                        if (classLoader == ClassLoader.getSystemClassLoader()) {
                            bool = Boolean.FALSE;
                        } else {
                            try {
                                zza(classLoader);
                            } catch (zzc e2) {
                            }
                            bool = Boolean.TRUE;
                        }
                    } else if ("com.google.android.gms".equals(context.getApplicationContext().getPackageName())) {
                        declaredField.set(null, ClassLoader.getSystemClassLoader());
                        bool = Boolean.FALSE;
                    } else {
                        try {
                            int iZzd = zzd(context, str, z);
                            if (zzaSM == null || zzaSM.isEmpty()) {
                                return iZzd;
                            }
                            zzg zzgVar = new zzg(zzaSM, ClassLoader.getSystemClassLoader());
                            zza(zzgVar);
                            declaredField.set(null, zzgVar);
                            zzaSJ = Boolean.TRUE;
                            return iZzd;
                        } catch (zzc e3) {
                            declaredField.set(null, ClassLoader.getSystemClassLoader());
                            bool = Boolean.FALSE;
                        }
                    }
                    zzaSJ = bool;
                }
            }
            if (!bool.booleanValue()) {
                return zzc(context, str, z);
            }
            try {
                return zzd(context, str, z);
            } catch (zzc e4) {
                String strValueOf2 = String.valueOf(e4.getMessage());
                Log.w("DynamiteModule", strValueOf2.length() != 0 ? "Failed to retrieve remote module version: ".concat(strValueOf2) : new String("Failed to retrieve remote module version: "));
                return 0;
            }
        }
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws zzc {
        com.google.android.gms.dynamite.zza zzaVar = null;
        Log.i("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 51).append("Selected remote version of ").append(str).append(", version >= ").append(i).toString());
        zzj zzjVarZzaT = zzaT(context);
        if (zzjVarZzaT == null) {
            throw new zzc("Failed to create IDynamiteLoader.", zzaVar);
        }
        try {
            IObjectWrapper iObjectWrapperZza = zzjVarZzaT.zza(zzn.zzw(context), str, i);
            if (zzn.zzE(iObjectWrapperZza) == null) {
                throw new zzc("Failed to load remote module.", zzaVar);
            }
            return new DynamiteModule((Context) zzn.zzE(iObjectWrapperZza));
        } catch (RemoteException e) {
            throw new zzc("Failed to load remote module.", e, zzaVar);
        }
    }

    private static int zzc(Context context, String str, boolean z) {
        zzj zzjVarZzaT = zzaT(context);
        if (zzjVarZzaT == null) {
            return 0;
        }
        try {
            return zzjVarZzaT.zza(zzn.zzw(context), str, z);
        } catch (RemoteException e) {
            String strValueOf = String.valueOf(e.getMessage());
            Log.w("DynamiteModule", strValueOf.length() != 0 ? "Failed to retrieve remote module version: ".concat(strValueOf) : new String("Failed to retrieve remote module version: "));
            return 0;
        }
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws zzc {
        zzl zzlVar;
        com.google.android.gms.dynamite.zza zzaVar = null;
        Log.i("DynamiteModule", new StringBuilder(String.valueOf(str).length() + 51).append("Selected remote version of ").append(str).append(", version >= ").append(i).toString());
        synchronized (DynamiteModule.class) {
            zzlVar = zzaSL;
        }
        if (zzlVar == null) {
            throw new zzc("DynamiteLoaderV2 was not cached.", zzaVar);
        }
        zza zzaVar2 = zzaSN.get();
        if (zzaVar2 == null || zzaVar2.zzaSV == null) {
            throw new zzc("No result cursor", zzaVar);
        }
        Context contextZza = zza(context.getApplicationContext(), str, i, zzaVar2.zzaSV, zzlVar);
        if (contextZza == null) {
            throw new zzc("Failed to get module context", zzaVar);
        }
        return new DynamiteModule(contextZza);
    }

    private static int zzd(Context context, String str, boolean z) throws Throwable {
        String str2;
        Cursor cursor = null;
        try {
            str2 = z ? "api_force_staging" : "api";
        } catch (Throwable th) {
            th = th;
        }
        try {
            String strValueOf = String.valueOf("content://com.google.android.gms.chimera/");
            Cursor cursorQuery = context.getContentResolver().query(Uri.parse(new StringBuilder(String.valueOf(strValueOf).length() + 1 + String.valueOf(str2).length() + String.valueOf(str).length()).append(strValueOf).append(str2).append("/").append(str).toString()), null, null, null, null);
            if (cursorQuery != null) {
                try {
                    if (cursorQuery.moveToFirst()) {
                        int i = cursorQuery.getInt(0);
                        if (i > 0) {
                            synchronized (DynamiteModule.class) {
                                zzaSM = cursorQuery.getString(2);
                            }
                            zza zzaVar = zzaSN.get();
                            if (zzaVar != null && zzaVar.zzaSV == null) {
                                zzaVar.zzaSV = cursorQuery;
                                cursorQuery = null;
                            }
                        }
                        if (cursorQuery != null) {
                            cursorQuery.close();
                        }
                        return i;
                    }
                } catch (Exception e) {
                    e = e;
                    if (e instanceof zzc) {
                        throw e;
                    }
                    throw new zzc("V2 version check failed", e, null);
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new zzc("Failed to connect to dynamite module ContentResolver.", (com.google.android.gms.dynamite.zza) null);
        } catch (Exception e2) {
            e = e2;
        } catch (Throwable th2) {
            th = th2;
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final IBinder zzcW(String str) throws zzc {
        try {
            return (IBinder) this.zzaSU.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String strValueOf = String.valueOf(str);
            throw new zzc(strValueOf.length() != 0 ? "Failed to instantiate module class: ".concat(strValueOf) : new String("Failed to instantiate module class: "), e, null);
        }
    }

    public final Context zztB() {
        return this.zzaSU;
    }
}
