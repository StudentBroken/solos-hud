package com.ua.sdk.authentication;

import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class FilemobileCredentialService {
    private AuthenticationManager authManager;
    private ConnectionFactory connFactory;
    private JsonParser<FilemobileCredential> jsonParser;
    private UrlBuilder urlBuilder;

    public void init(ConnectionFactory connFactory, UrlBuilder urlBuilder, JsonParser<FilemobileCredential> jsonParser, AuthenticationManager authManager) {
        this.connFactory = connFactory;
        this.urlBuilder = urlBuilder;
        this.jsonParser = jsonParser;
        this.authManager = authManager;
    }

    public FilemobileCredential fetchCredentials() throws UaException {
        return fetchCredentials(0);
    }

    public FilemobileCredential fetchCredentials(int attempts) throws UaException {
        FilemobileCredential filemobileCredentialRetryFetchCredentials;
        if (attempts > 2) {
            UaLog.debug("Tried fetching Filemobile credentials three(3) times.  Was not able to get a token.");
            throw new UaException("Unable to fetch Filemobile credentials.");
        }
        try {
            URL url = this.urlBuilder.buildCreateFilemobileTokenCredentialUrl();
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.signAsUser(conn);
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                Precondition.isResponseSuccess(conn);
                if (conn.getResponseCode() == 202) {
                    filemobileCredentialRetryFetchCredentials = retryFetchCredentials(attempts);
                } else {
                    filemobileCredentialRetryFetchCredentials = this.jsonParser.parse(conn.getInputStream());
                }
                return filemobileCredentialRetryFetchCredentials;
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Filemobile fetch credentials cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable t) {
            UaLog.error("Unable to fetch Filemobile credentials.", t);
            throw new UaException("Unable to fetch Filemobile credentials.", t);
        }
    }

    private FilemobileCredential retryFetchCredentials(int attempts) throws InterruptedException, UaException {
        UaLog.debug("Retrying in three(3) seconds.");
        Thread.sleep(3000L);
        UaLog.debug("Fetching Filemobile credentials attempt: " + (attempts + 1));
        return fetchCredentials(attempts + 1);
    }
}
