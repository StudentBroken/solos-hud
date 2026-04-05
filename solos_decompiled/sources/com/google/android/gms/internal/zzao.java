package com.google.android.gms.internal;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/* JADX INFO: loaded from: classes67.dex */
public final class zzao implements zzan {
    private final zzap zzaC;
    private final SSLSocketFactory zzaD;

    public zzao() {
        this(null);
    }

    private zzao(zzap zzapVar) {
        this(null, null);
    }

    private zzao(zzap zzapVar, SSLSocketFactory sSLSocketFactory) {
        this.zzaC = null;
        this.zzaD = null;
    }

    private static HttpEntity zza(HttpURLConnection httpURLConnection) {
        InputStream errorStream;
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        try {
            errorStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            errorStream = httpURLConnection.getErrorStream();
        }
        basicHttpEntity.setContent(errorStream);
        basicHttpEntity.setContentLength(httpURLConnection.getContentLength());
        basicHttpEntity.setContentEncoding(httpURLConnection.getContentEncoding());
        basicHttpEntity.setContentType(httpURLConnection.getContentType());
        return basicHttpEntity;
    }

    private static void zza(HttpURLConnection httpURLConnection, zzp<?> zzpVar) throws zza, IOException {
        byte[] bArrZzg = zzpVar.zzg();
        if (bArrZzg != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", zzp.zzf());
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(bArrZzg);
            dataOutputStream.close();
        }
    }

    @Override // com.google.android.gms.internal.zzan
    public final HttpResponse zza(zzp<?> zzpVar, Map<String, String> map) throws zza, IOException {
        String strZzg;
        String url = zzpVar.getUrl();
        HashMap map2 = new HashMap();
        map2.putAll(zzpVar.getHeaders());
        map2.putAll(map);
        if (this.zzaC != null) {
            strZzg = this.zzaC.zzg(url);
            if (strZzg == null) {
                String strValueOf = String.valueOf(url);
                throw new IOException(strValueOf.length() != 0 ? "URL blocked by rewriter: ".concat(strValueOf) : new String("URL blocked by rewriter: "));
            }
        } else {
            strZzg = url;
        }
        URL url2 = new URL(strZzg);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
        httpURLConnection.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());
        int iZzi = zzpVar.zzi();
        httpURLConnection.setConnectTimeout(iZzi);
        httpURLConnection.setReadTimeout(iZzi);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        "https".equals(url2.getProtocol());
        for (String str : map2.keySet()) {
            httpURLConnection.addRequestProperty(str, (String) map2.get(str));
        }
        switch (zzpVar.getMethod()) {
            case -1:
                break;
            case 0:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                break;
            case 1:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_POST);
                zza(httpURLConnection, zzpVar);
                break;
            case 2:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_PUT);
                zza(httpURLConnection, zzpVar);
                break;
            case 3:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_DELETE);
                break;
            case 4:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_HEAD);
                break;
            case 5:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_OPTIONS);
                break;
            case 6:
                httpURLConnection.setRequestMethod(HttpRequest.METHOD_TRACE);
                break;
            case 7:
                httpURLConnection.setRequestMethod("PATCH");
                zza(httpURLConnection, zzpVar);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        if (httpURLConnection.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicStatusLine basicStatusLine = new BasicStatusLine(protocolVersion, httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(basicStatusLine);
        int method = zzpVar.getMethod();
        int statusCode = basicStatusLine.getStatusCode();
        if ((method == 4 || (100 <= statusCode && statusCode < 200) || statusCode == 204 || statusCode == 304) ? false : true) {
            basicHttpResponse.setEntity(zza(httpURLConnection));
        }
        for (Map.Entry<String, List<String>> entry : httpURLConnection.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                basicHttpResponse.addHeader(new BasicHeader(entry.getKey(), entry.getValue().get(0)));
            }
        }
        return basicHttpResponse;
    }
}
