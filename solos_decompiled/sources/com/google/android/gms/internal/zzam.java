package com.google.android.gms.internal;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

/* JADX INFO: loaded from: classes67.dex */
public final class zzam {
    public static String zza(Map<String, String> map) {
        String str = map.get("Content-Type");
        if (str != null) {
            String[] strArrSplit = str.split(";");
            for (int i = 1; i < strArrSplit.length; i++) {
                String[] strArrSplit2 = strArrSplit[i].trim().split("=");
                if (strArrSplit2.length == 2 && strArrSplit2[0].equals(HttpRequest.PARAM_CHARSET)) {
                    return strArrSplit2[1];
                }
            }
        }
        return "ISO-8859-1";
    }

    public static zzc zzb(zzn zznVar) {
        boolean z;
        boolean z2;
        long j;
        long j2;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Map<String, String> map = zznVar.zzy;
        long j3 = 0;
        long j4 = 0;
        String str = map.get(HttpRequest.HEADER_DATE);
        long jZzf = str != null ? zzf(str) : 0L;
        String str2 = map.get(HttpRequest.HEADER_CACHE_CONTROL);
        if (str2 != null) {
            String[] strArrSplit = str2.split(",");
            z = false;
            long j5 = 0;
            long j6 = 0;
            for (String str3 : strArrSplit) {
                String strTrim = str3.trim();
                if (strTrim.equals("no-cache") || strTrim.equals("no-store")) {
                    return null;
                }
                if (strTrim.startsWith("max-age=")) {
                    try {
                        j6 = Long.parseLong(strTrim.substring(8));
                    } catch (Exception e) {
                    }
                } else if (strTrim.startsWith("stale-while-revalidate=")) {
                    try {
                        j5 = Long.parseLong(strTrim.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (strTrim.equals("must-revalidate") || strTrim.equals("proxy-revalidate")) {
                    z = true;
                }
            }
            j3 = j6;
            j4 = j5;
            z2 = true;
        } else {
            z = false;
            z2 = false;
        }
        String str4 = map.get(HttpRequest.HEADER_EXPIRES);
        long jZzf2 = str4 != null ? zzf(str4) : 0L;
        String str5 = map.get(HttpRequest.HEADER_LAST_MODIFIED);
        long jZzf3 = str5 != null ? zzf(str5) : 0L;
        String str6 = map.get(HttpRequest.HEADER_ETAG);
        if (z2) {
            j2 = jCurrentTimeMillis + (1000 * j3);
            j = z ? j2 : (1000 * j4) + j2;
        } else if (jZzf <= 0 || jZzf2 < jZzf) {
            j = 0;
            j2 = 0;
        } else {
            j = (jZzf2 - jZzf) + jCurrentTimeMillis;
            j2 = j;
        }
        zzc zzcVar = new zzc();
        zzcVar.data = zznVar.data;
        zzcVar.zza = str6;
        zzcVar.zze = j2;
        zzcVar.zzd = j;
        zzcVar.zzb = jZzf;
        zzcVar.zzc = jZzf3;
        zzcVar.zzf = map;
        return zzcVar;
    }

    private static long zzf(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0L;
        }
    }
}
