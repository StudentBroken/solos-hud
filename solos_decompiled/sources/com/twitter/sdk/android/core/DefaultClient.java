package com.twitter.sdk.android.core;

import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.client.UrlConnectionClient;

/* JADX INFO: loaded from: classes62.dex */
public class DefaultClient implements Client {
    final SSLSocketFactory sslSocketFactory;
    final Client wrappedClient;

    public DefaultClient(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        if (hasOkHttpOnClasspath()) {
            this.wrappedClient = new OkClient() { // from class: com.twitter.sdk.android.core.DefaultClient.1
                @Override // retrofit.client.OkClient, retrofit.client.UrlConnectionClient
                protected HttpURLConnection openConnection(Request request) throws IOException {
                    return DefaultClient.this.openSslConnection(super.openConnection(request));
                }
            };
        } else {
            this.wrappedClient = new UrlConnectionClient() { // from class: com.twitter.sdk.android.core.DefaultClient.2
                @Override // retrofit.client.UrlConnectionClient
                protected HttpURLConnection openConnection(Request request) throws IOException {
                    return DefaultClient.this.openSslConnection(super.openConnection(request));
                }
            };
        }
    }

    @Override // retrofit.client.Client
    public Response execute(Request request) throws IOException {
        return this.wrappedClient.execute(request);
    }

    private boolean hasOkHttpOnClasspath() {
        boolean okUrlFactory = false;
        try {
            Class.forName("com.squareup.okhttp.OkUrlFactory");
            okUrlFactory = true;
        } catch (ClassNotFoundException e) {
        }
        boolean okHttpClient = false;
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            okHttpClient = true;
        } catch (ClassNotFoundException e2) {
        }
        if (okHttpClient != okUrlFactory) {
            Fabric.getLogger().d(TwitterCore.TAG, "Retrofit detected an unsupported OkHttp on the classpath.\nTo use OkHttp with this version of Retrofit, you'll need:\n1. com.squareup.okhttp:okhttp:1.6.0 (or newer)\n2. com.squareup.okhttp:okhttp-urlconnection:1.6.0 (or newer)\nNote that OkHttp 2.0.0+ is supported!");
            return false;
        }
        return okHttpClient;
    }

    HttpURLConnection openSslConnection(HttpURLConnection connection) {
        if (this.sslSocketFactory != null && (connection instanceof HttpsURLConnection)) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.sslSocketFactory);
        }
        return connection;
    }
}
