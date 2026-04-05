package com.ua.sdk.user.permission;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.NetworkError;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionService {
    private final AuthenticationManager authManager;
    private final ConnectionFactory connFactory;
    private final JsonParser<? extends EntityList<UserPermission>> jsonPageParser;
    private final UrlBuilder urlBuilder;

    public UserPermissionService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<? extends EntityList<UserPermission>> jsonPageParser) {
        this.connFactory = connFactory;
        this.authManager = authManager;
        this.urlBuilder = urlBuilder;
        this.jsonPageParser = jsonPageParser;
    }

    public EntityList<UserPermission> fetchUserPermission(EntityRef ref) throws UaException {
        try {
            URL url = this.urlBuilder.buildGetUserPermissionUrl(ref);
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.sign(conn, AuthenticationManager.AuthenticationType.USER);
                conn.setRequestMethod(HttpRequest.METHOD_GET);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                Precondition.isExpectedResponse(conn, 200);
                return this.jsonPageParser.parse(conn.getInputStream());
            } finally {
                conn.disconnect();
            }
        } catch (NetworkError e) {
            if (e.getResponseCode() == 401) {
                throw new UaException(UaException.Code.NOT_AUTHENTICATED, e);
            }
            throw e;
        } catch (InterruptedIOException e2) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e3) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }

    public EntityList<UserPermission> fetchUserPermissions() throws UaException {
        try {
            URL url = this.urlBuilder.buildGetUserPermissionsUrl(null);
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.sign(conn, AuthenticationManager.AuthenticationType.USER);
                conn.setRequestMethod(HttpRequest.METHOD_GET);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                Precondition.isExpectedResponse(conn, 200);
                return this.jsonPageParser.parse(conn.getInputStream());
            } finally {
                conn.disconnect();
            }
        } catch (NetworkError e) {
            if (e.getResponseCode() == 401) {
                throw new UaException(UaException.Code.NOT_AUTHENTICATED, e);
            }
            throw e;
        } catch (InterruptedIOException e2) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e3) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }
}
