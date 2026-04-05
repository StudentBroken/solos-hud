package com.ua.sdk.role;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class RoleService implements EntityService<Role> {
    private final AuthenticationManager authManager;
    private final ConnectionFactory connFactory;
    private final JsonParser<? extends EntityList<Role>> jsonPageParser;
    private final JsonParser<Role> jsonParser;
    private final UrlBuilder urlBuilder;

    public RoleService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<Role> jsonParser, JsonParser<? extends EntityList<Role>> jsonPageParser) {
        this.connFactory = connFactory;
        this.authManager = authManager;
        this.urlBuilder = urlBuilder;
        this.jsonParser = jsonParser;
        this.jsonPageParser = jsonPageParser;
    }

    @Override // com.ua.sdk.internal.EntityService
    public Role create(Role entity) throws UaException {
        throw new UnsupportedOperationException();
    }

    @Override // com.ua.sdk.internal.EntityService
    public Role fetch(Reference ref) throws UaException {
        try {
            URL url = this.urlBuilder.buildGetRoleUrl(ref);
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.sign(conn, AuthenticationManager.AuthenticationType.USER);
                conn.setRequestMethod(HttpRequest.METHOD_GET);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
                Precondition.isExpectedResponse(conn, 200);
                return this.jsonParser.parse(conn.getInputStream());
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

    @Override // com.ua.sdk.internal.EntityService
    public Role save(Role entity) throws UaException {
        throw new UnsupportedOperationException();
    }

    @Override // com.ua.sdk.internal.EntityService
    public void delete(Reference ref) throws UaException {
        throw new UnsupportedOperationException();
    }

    @Override // com.ua.sdk.internal.EntityService
    public Role patch(Role entity, Reference ref) throws UaException {
        throw new UnsupportedOperationException();
    }

    @Override // com.ua.sdk.internal.EntityService
    public EntityList<Role> fetchPage(EntityListRef<Role> ref) throws UaException {
        try {
            URL url = this.urlBuilder.buildGetRolesUrl(null);
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
