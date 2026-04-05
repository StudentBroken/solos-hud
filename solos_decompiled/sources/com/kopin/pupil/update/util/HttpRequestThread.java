package com.kopin.pupil.update.util;

import android.content.Context;
import android.util.Log;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: loaded from: classes13.dex */
public abstract class HttpRequestThread extends Thread {
    private final String TAG = "HttpRequest";
    private final Context mContext;
    private final URL mURL;
    private final SSLSocketFactory socketFactory;

    protected abstract ArrayList<NameValuePair> getHeaders();

    protected abstract void processResponse(String str, Map<String, List<String>> map);

    protected abstract boolean processStream(InputStream inputStream, Map<String, List<String>> map);

    protected HttpRequestThread(Context context, String endPoint) {
        this.mContext = context;
        URL url = null;
        try {
            URL url2 = new URL(endPoint);
            url = url2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.mURL = url;
        this.socketFactory = null;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        InputStream stream;
        Log.d("HttpRequest", this.mURL.toString());
        try {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) this.mURL.openConnection();
            try {
                httpsURLConnection.setRequestMethod(HttpRequest.METHOD_GET);
                httpsURLConnection.setUseCaches(false);
                httpsURLConnection.setDoOutput(false);
                if (this.socketFactory != null) {
                    httpsURLConnection.setSSLSocketFactory(this.socketFactory);
                }
                httpsURLConnection.setInstanceFollowRedirects(true);
                httpsURLConnection.addRequestProperty("Connection", "Keep-Alive");
                httpsURLConnection.setRequestProperty("Accept", "application/json,application/octet-stream");
                httpsURLConnection.setRequestProperty("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
                ArrayList<NameValuePair> headers = getHeaders();
                if (headers != null) {
                    for (NameValuePair nvp : headers) {
                        httpsURLConnection.addRequestProperty(nvp.getName(), nvp.getValue());
                    }
                }
            } catch (ProtocolException e) {
                Log.e("HttpRequest", e.getMessage());
            }
            InputStream stream2 = null;
            int responseCode = -1;
            try {
                stream = new BufferedInputStream(httpsURLConnection.getInputStream());
            } catch (IOException e2) {
                e = e2;
            }
            try {
                responseCode = httpsURLConnection.getResponseCode();
                stream2 = stream;
            } catch (IOException e3) {
                e = e3;
                stream2 = stream;
                e.printStackTrace();
            }
            if (responseCode == 200 && !processStream(stream2, httpsURLConnection.getHeaderFields())) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream2));
                    StringBuilder builder = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line != null) {
                            builder.append(line);
                            builder.append("\n");
                        } else {
                            processResponse(builder.toString(), httpsURLConnection.getHeaderFields());
                            return;
                        }
                    }
                } catch (IOException e4) {
                }
            }
        } catch (IOException e5) {
            Log.e("HttpRequest", e5.getMessage());
        }
    }

    public static class FileDownloadThread extends HttpRequestThread {
        protected FileDownloadThread(Context context, String endPoint) {
            super(context, endPoint);
        }

        @Override // com.kopin.pupil.update.util.HttpRequestThread
        protected boolean processStream(InputStream stream, Map<String, List<String>> headers) {
            return false;
        }

        @Override // com.kopin.pupil.update.util.HttpRequestThread
        protected ArrayList<NameValuePair> getHeaders() {
            return null;
        }

        @Override // com.kopin.pupil.update.util.HttpRequestThread
        protected void processResponse(String resp, Map<String, List<String>> headers) {
        }
    }
}
