package com.google.android.gms.internal;

import android.os.SystemClock;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

/* JADX INFO: loaded from: classes67.dex */
public final class zzad implements zzk {
    private static boolean DEBUG = zzab.DEBUG;
    private static int zzam = 3000;
    private static int zzan = 4096;
    private zzan zzao;
    private zzae zzap;

    public zzad(zzan zzanVar) {
        this(zzanVar, new zzae(zzan));
    }

    private zzad(zzan zzanVar, zzae zzaeVar) {
        this.zzao = zzanVar;
        this.zzap = zzaeVar;
    }

    private static Map<String, String> zza(Header[] headerArr) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private static void zza(String str, zzp<?> zzpVar, zzaa zzaaVar) throws zzaa {
        zzx zzxVarZzj = zzpVar.zzj();
        int iZzi = zzpVar.zzi();
        try {
            zzxVarZzj.zza(zzaaVar);
            zzpVar.zzb(String.format("%s-retry [timeout=%s]", str, Integer.valueOf(iZzi)));
        } catch (zzaa e) {
            zzpVar.zzb(String.format("%s-timeout-giveup [timeout=%s]", str, Integer.valueOf(iZzi)));
            throw e;
        }
    }

    private final byte[] zza(HttpEntity httpEntity) throws zzy, IOException {
        zzaq zzaqVar = new zzaq(this.zzap, (int) httpEntity.getContentLength());
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new zzy();
            }
            byte[] bArrZzb = this.zzap.zzb(1024);
            while (true) {
                int i = content.read(bArrZzb);
                if (i == -1) {
                    break;
                }
                zzaqVar.write(bArrZzb, 0, i);
            }
            byte[] byteArray = zzaqVar.toByteArray();
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                zzab.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzap.zza(bArrZzb);
            zzaqVar.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                httpEntity.consumeContent();
            } catch (IOException e2) {
                zzab.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzap.zza(null);
            zzaqVar.close();
            throw th;
        }
    }

    @Override // com.google.android.gms.internal.zzk
    public final zzn zza(zzp<?> zzpVar) throws zzaa {
        byte[] bArrZza;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        while (true) {
            HttpResponse httpResponse = null;
            Map<String, String> mapEmptyMap = Collections.emptyMap();
            try {
                try {
                    HashMap map = new HashMap();
                    zzc zzcVarZze = zzpVar.zze();
                    if (zzcVarZze != null) {
                        if (zzcVarZze.zza != null) {
                            map.put(HttpRequest.HEADER_IF_NONE_MATCH, zzcVarZze.zza);
                        }
                        if (zzcVarZze.zzc > 0) {
                            map.put("If-Modified-Since", DateUtils.formatDate(new Date(zzcVarZze.zzc)));
                        }
                    }
                    HttpResponse httpResponseZza = this.zzao.zza(zzpVar, map);
                    try {
                        StatusLine statusLine = httpResponseZza.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        mapEmptyMap = zza(httpResponseZza.getAllHeaders());
                        if (statusCode == 304) {
                            zzc zzcVarZze2 = zzpVar.zze();
                            if (zzcVarZze2 == null) {
                                return new zzn(304, null, mapEmptyMap, true, SystemClock.elapsedRealtime() - jElapsedRealtime);
                            }
                            zzcVarZze2.zzf.putAll(mapEmptyMap);
                            return new zzn(304, zzcVarZze2.data, zzcVarZze2.zzf, true, SystemClock.elapsedRealtime() - jElapsedRealtime);
                        }
                        bArrZza = httpResponseZza.getEntity() != null ? zza(httpResponseZza.getEntity()) : new byte[0];
                        try {
                            long jElapsedRealtime2 = SystemClock.elapsedRealtime() - jElapsedRealtime;
                            if (DEBUG || jElapsedRealtime2 > zzam) {
                                Object[] objArr = new Object[5];
                                objArr[0] = zzpVar;
                                objArr[1] = Long.valueOf(jElapsedRealtime2);
                                objArr[2] = bArrZza != null ? Integer.valueOf(bArrZza.length) : "null";
                                objArr[3] = Integer.valueOf(statusLine.getStatusCode());
                                objArr[4] = Integer.valueOf(zzpVar.zzj().zzb());
                                zzab.zzb("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", objArr);
                            }
                            if (statusCode < 200 || statusCode > 299) {
                                throw new IOException();
                            }
                            return new zzn(statusCode, bArrZza, mapEmptyMap, false, SystemClock.elapsedRealtime() - jElapsedRealtime);
                        } catch (IOException e) {
                            e = e;
                            httpResponse = httpResponseZza;
                            if (httpResponse == null) {
                                throw new zzo(e);
                            }
                            int statusCode2 = httpResponse.getStatusLine().getStatusCode();
                            zzab.zzc("Unexpected response code %d for %s", Integer.valueOf(statusCode2), zzpVar.getUrl());
                            if (bArrZza != null) {
                                zzn zznVar = new zzn(statusCode2, bArrZza, mapEmptyMap, false, SystemClock.elapsedRealtime() - jElapsedRealtime);
                                if (statusCode2 != 401 && statusCode2 != 403) {
                                    if (statusCode2 >= 400 && statusCode2 <= 499) {
                                        throw new zzf(zznVar);
                                    }
                                    if (statusCode2 < 500 || statusCode2 > 599) {
                                        throw new zzy(zznVar);
                                    }
                                    throw new zzy(zznVar);
                                }
                                zza("auth", zzpVar, new zza(zznVar));
                            } else {
                                zza("network", zzpVar, new zzm());
                            }
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bArrZza = null;
                        httpResponse = httpResponseZza;
                    }
                } catch (IOException e3) {
                    e = e3;
                    bArrZza = null;
                }
            } catch (MalformedURLException e4) {
                String strValueOf = String.valueOf(zzpVar.getUrl());
                throw new RuntimeException(strValueOf.length() != 0 ? "Bad URL ".concat(strValueOf) : new String("Bad URL "), e4);
            } catch (SocketTimeoutException e5) {
                zza("socket", zzpVar, new zzz());
            } catch (ConnectTimeoutException e6) {
                zza("connection", zzpVar, new zzz());
            }
        }
    }
}
