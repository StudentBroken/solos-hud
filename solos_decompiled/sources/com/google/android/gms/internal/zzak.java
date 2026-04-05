package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* JADX INFO: loaded from: classes67.dex */
public final class zzak implements zzan {
    private HttpClient zzaB;

    public zzak(HttpClient httpClient) {
        this.zzaB = httpClient;
    }

    private static void zza(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, zzp<?> zzpVar) throws zza {
        byte[] bArrZzg = zzpVar.zzg();
        if (bArrZzg != null) {
            httpEntityEnclosingRequestBase.setEntity(new ByteArrayEntity(bArrZzg));
        }
    }

    private static void zza(HttpUriRequest httpUriRequest, Map<String, String> map) {
        for (String str : map.keySet()) {
            httpUriRequest.setHeader(str, map.get(str));
        }
    }

    @Override // com.google.android.gms.internal.zzan
    public final HttpResponse zza(zzp<?> zzpVar, Map<String, String> map) throws zza, IOException {
        HttpUriRequest httpTrace;
        switch (zzpVar.getMethod()) {
            case -1:
                httpTrace = new HttpGet(zzpVar.getUrl());
                break;
            case 0:
                httpTrace = new HttpGet(zzpVar.getUrl());
                break;
            case 1:
                HttpPost httpPost = new HttpPost(zzpVar.getUrl());
                httpPost.addHeader("Content-Type", zzp.zzf());
                zza(httpPost, zzpVar);
                httpTrace = httpPost;
                break;
            case 2:
                HttpPut httpPut = new HttpPut(zzpVar.getUrl());
                httpPut.addHeader("Content-Type", zzp.zzf());
                zza(httpPut, zzpVar);
                httpTrace = httpPut;
                break;
            case 3:
                httpTrace = new HttpDelete(zzpVar.getUrl());
                break;
            case 4:
                httpTrace = new HttpHead(zzpVar.getUrl());
                break;
            case 5:
                httpTrace = new HttpOptions(zzpVar.getUrl());
                break;
            case 6:
                httpTrace = new HttpTrace(zzpVar.getUrl());
                break;
            case 7:
                zzal zzalVar = new zzal(zzpVar.getUrl());
                zzalVar.addHeader("Content-Type", zzp.zzf());
                zza(zzalVar, zzpVar);
                httpTrace = zzalVar;
                break;
            default:
                throw new IllegalStateException("Unknown request method.");
        }
        zza(httpTrace, map);
        zza(httpTrace, zzpVar.getHeaders());
        HttpParams params = httpTrace.getParams();
        int iZzi = zzpVar.zzi();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, iZzi);
        return this.zzaB.execute(httpTrace);
    }
}
