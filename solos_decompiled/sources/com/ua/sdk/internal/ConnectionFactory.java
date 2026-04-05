package com.ua.sdk.internal;

import android.content.Context;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class ConnectionFactory {
    private final String clientId;
    private final Context context;

    public ConnectionFactory(Context context, String clientId) {
        this.context = (Context) Precondition.isNotNull(context, "context");
        this.clientId = (String) Precondition.isNotNull(clientId, "clientId");
    }

    public HttpsURLConnection getSslConnection(URL url) throws IOException, UaException {
        Precondition.isConnected(this.context);
        UaLog.debug("connect=%s", url.toString());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        return (HttpsURLConnection) addProperties(connection);
    }

    public HttpURLConnection getConnection(URL url) throws IOException, UaException {
        Precondition.isConnected(this.context);
        UaLog.debug("connect=%s", url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        return (HttpURLConnection) addProperties(connection);
    }

    public <T extends URLConnection> T addProperties(T conn) {
        conn.setRequestProperty("Api-Key", this.clientId);
        return conn;
    }
}
