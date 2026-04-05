package com.ua.sdk.gear.user;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.EntityService;
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
public class UserGearService implements EntityService<UserGear> {
    AuthenticationManager authenticationManager;
    ConnectionFactory connectionFactory;
    JsonParser<UserGear> jsonParser;
    JsonWriter<UserGear> jsonWriter;
    JsonParser<EntityList<UserGear>> listJsonParser;
    UrlBuilder urlBuilder;

    public UserGearService(ConnectionFactory connectionFactory, AuthenticationManager authenticationManager, UrlBuilder urlBuilder, JsonParser<EntityList<UserGear>> listJsonParser, JsonParser<UserGear> jsonParser, JsonWriter<UserGear> jsonWriter) {
        this.connectionFactory = connectionFactory;
        this.authenticationManager = authenticationManager;
        this.urlBuilder = urlBuilder;
        this.listJsonParser = listJsonParser;
        this.jsonParser = jsonParser;
        this.jsonWriter = jsonWriter;
    }

    @Override // com.ua.sdk.internal.EntityService
    public UserGear create(UserGear userGear) throws UaException {
        try {
            URL url = this.urlBuilder.buildCreateUserGearUrl();
            HttpsURLConnection conn = this.connectionFactory.getSslConnection(url);
            Precondition.isAuthenticated(this.authenticationManager);
            try {
                this.authenticationManager.signAsUser(conn);
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                this.jsonWriter.write(userGear, conn.getOutputStream());
                int responseCode = conn.getResponseCode();
                if (responseCode == 201) {
                    return this.jsonParser.parse(conn.getInputStream());
                }
                throw new NetworkError(responseCode, conn);
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }

    @Override // com.ua.sdk.internal.EntityService
    public void delete(Reference ref) throws UaException {
        try {
            URL url = this.urlBuilder.buildDeleteUserGearUrl(ref);
            HttpsURLConnection conn = this.connectionFactory.getSslConnection(url);
            Precondition.isAuthenticated(this.authenticationManager);
            try {
                this.authenticationManager.signAsUser(conn);
                conn.setRequestMethod(HttpRequest.METHOD_DELETE);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                int responseCode = conn.getResponseCode();
                if (responseCode != 204) {
                    throw new NetworkError(responseCode, conn);
                }
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }

    @Override // com.ua.sdk.internal.EntityService
    public UserGear patch(UserGear userGear, Reference ref) throws UaException {
        try {
            URL url = this.urlBuilder.buildPatchUserGearUrl(ref);
            HttpsURLConnection conn = this.connectionFactory.getSslConnection(url);
            Precondition.isAuthenticated(this.authenticationManager);
            try {
                this.authenticationManager.signAsUser(conn);
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.addRequestProperty("X-HTTP-Method-Override", "PATCH");
                this.jsonWriter.write(userGear, conn.getOutputStream());
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    return this.jsonParser.parse(conn.getInputStream());
                }
                throw new NetworkError(responseCode, conn);
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }

    @Override // com.ua.sdk.internal.EntityService
    public EntityList<UserGear> fetchPage(EntityListRef<UserGear> ref) throws UaException {
        try {
            if (ref != null) {
                Precondition.isAuthenticated(this.authenticationManager);
                URL url = this.urlBuilder.buildGetUserGearUrl(ref);
                HttpsURLConnection conn = this.connectionFactory.getSslConnection(url);
                Precondition.isAuthenticated(this.authenticationManager);
                try {
                    this.authenticationManager.signAsUser(conn);
                    conn.setRequestMethod(HttpRequest.METHOD_GET);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        return this.listJsonParser.parse(conn.getInputStream());
                    }
                    throw new NetworkError(responseCode, conn);
                } finally {
                    conn.disconnect();
                }
            }
            throw new UaException("The ref must not be null!");
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN, e2);
        }
    }

    @Override // com.ua.sdk.internal.EntityService
    public UserGear fetch(Reference ref) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.EntityService
    public UserGear save(UserGear entity) throws UaException {
        return null;
    }
}
