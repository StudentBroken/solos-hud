package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzbr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes3.dex */
public abstract class zzbhu {
    /* JADX WARN: Multi-variable type inference failed */
    protected static <O, I> I zza(zzbhv<I, O> zzbhvVar, Object obj) {
        return ((zzbhv) zzbhvVar).zzaIS != null ? zzbhvVar.convertBack(obj) : obj;
    }

    private static void zza(StringBuilder sb, zzbhv zzbhvVar, Object obj) {
        if (zzbhvVar.zzaIJ == 11) {
            sb.append(zzbhvVar.zzaIP.cast(obj).toString());
        } else {
            if (zzbhvVar.zzaIJ != 7) {
                sb.append(obj);
                return;
            }
            sb.append("\"");
            sb.append(com.google.android.gms.common.util.zzq.zzcK((String) obj));
            sb.append("\"");
        }
    }

    private static void zza(StringBuilder sb, zzbhv zzbhvVar, ArrayList<Object> arrayList) {
        sb.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object obj = arrayList.get(i);
            if (obj != null) {
                zza(sb, zzbhvVar, obj);
            }
        }
        sb.append("]");
    }

    public String toString() {
        Map<String, zzbhv<?, ?>> mapZzrK = zzrK();
        StringBuilder sb = new StringBuilder(100);
        for (String str : mapZzrK.keySet()) {
            zzbhv<?, ?> zzbhvVar = mapZzrK.get(str);
            if (zza(zzbhvVar)) {
                Object objZza = zza(zzbhvVar, zzb(zzbhvVar));
                if (sb.length() == 0) {
                    sb.append("{");
                } else {
                    sb.append(",");
                }
                sb.append("\"").append(str).append("\":");
                if (objZza != null) {
                    switch (zzbhvVar.zzaIL) {
                        case 8:
                            sb.append("\"").append(com.google.android.gms.common.util.zzd.zzg((byte[]) objZza)).append("\"");
                            break;
                        case 9:
                            sb.append("\"").append(com.google.android.gms.common.util.zzd.zzh((byte[]) objZza)).append("\"");
                            break;
                        case 10:
                            com.google.android.gms.common.util.zzr.zza(sb, (HashMap) objZza);
                            break;
                        default:
                            if (zzbhvVar.zzaIK) {
                                zza(sb, (zzbhv) zzbhvVar, (ArrayList<Object>) objZza);
                            } else {
                                zza(sb, zzbhvVar, objZza);
                            }
                            break;
                    }
                } else {
                    sb.append("null");
                }
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        } else {
            sb.append("{}");
        }
        return sb.toString();
    }

    protected boolean zza(zzbhv zzbhvVar) {
        if (zzbhvVar.zzaIL != 11) {
            return zzcI(zzbhvVar.zzaIN);
        }
        if (zzbhvVar.zzaIM) {
            String str = zzbhvVar.zzaIN;
            throw new UnsupportedOperationException("Concrete type arrays not supported");
        }
        String str2 = zzbhvVar.zzaIN;
        throw new UnsupportedOperationException("Concrete types not supported");
    }

    protected Object zzb(zzbhv zzbhvVar) {
        String str = zzbhvVar.zzaIN;
        if (zzbhvVar.zzaIP == null) {
            return zzcH(zzbhvVar.zzaIN);
        }
        zzcH(zzbhvVar.zzaIN);
        zzbr.zza(true, "Concrete field shouldn't be value object: %s", zzbhvVar.zzaIN);
        boolean z = zzbhvVar.zzaIM;
        try {
            char upperCase = Character.toUpperCase(str.charAt(0));
            String strValueOf = String.valueOf(str.substring(1));
            return getClass().getMethod(new StringBuilder(String.valueOf(strValueOf).length() + 4).append("get").append(upperCase).append(strValueOf).toString(), new Class[0]).invoke(this, new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object zzcH(String str);

    protected abstract boolean zzcI(String str);

    public abstract Map<String, zzbhv<?, ?>> zzrK();
}
