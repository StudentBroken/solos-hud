package com.ua.sdk.authentication;

import com.ua.sdk.UaException;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class InternalTokenCredentialService {
    private AuthenticationManager authManager;
    private ConnectionFactory connFactory;
    private JsonWriter<InternalTokenCredential> internalTokenCredentialWriter;
    private JsonParser<OAuth2Credentials> oauth2Parser;
    private UrlBuilder urlBuilder;

    public void init(ConnectionFactory connFactory, UrlBuilder urlBuilder, AuthenticationManager authManager, JsonParser<OAuth2Credentials> oauth2Parser, JsonWriter<InternalTokenCredential> internalTokenCredentialWriter) {
        this.connFactory = connFactory;
        this.urlBuilder = urlBuilder;
        this.authManager = authManager;
        this.oauth2Parser = oauth2Parser;
        this.internalTokenCredentialWriter = internalTokenCredentialWriter;
    }

    public OAuth2Credentials save(InternalTokenCredential internalTokenCredential, boolean authAsUser) throws UaException {
        Precondition.isNotNull(internalTokenCredential, "internalTokenCredential");
        try {
            URL url = this.urlBuilder.buildCreateInternalTokenCredentialUrl();
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                if (authAsUser) {
                    this.authManager.signAsUser(conn);
                } else {
                    this.authManager.signAsClient(conn);
                }
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                this.internalTokenCredentialWriter.write(internalTokenCredential, conn.getOutputStream());
                Precondition.isResponseSuccess(conn);
                return this.oauth2Parser.parse(conn.getInputStream());
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }
}
