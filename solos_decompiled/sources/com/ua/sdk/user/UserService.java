package com.ua.sdk.user;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import com.ua.sdk.util.Streams;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class UserService extends AbstractResourceService<User> {
    public UserService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<User> userParser, JsonWriter<User> userWriter, JsonParser<EntityList<User>> userPageParser) {
        super(connFactory, authManager, urlBuilder, userParser, userWriter, userPageParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        return this.urlBuilder.buildCreateUserUrl();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public AuthenticationManager.AuthenticationType getCreateAuthenticationType() {
        return AuthenticationManager.AuthenticationType.CLIENT;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        if (ref instanceof CurrentUserRef) {
            URL url = this.urlBuilder.buildGetCurrentUserUrl();
            return url;
        }
        URL url2 = this.urlBuilder.buildGetUserUrl(ref);
        return url2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(User resource) {
        return this.urlBuilder.buildSaveUserUrl(resource.getRef());
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("User patch is not supported.");
    }

    public void resetPassword(String email) throws UaException {
        Precondition.isNotNull(email);
        try {
            URL url = this.urlBuilder.buildResetPasswordUrl();
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.signAsClient(conn);
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                String body = "{\"email\":\"" + email + "\"}";
                Streams.writeFully(body, conn.getOutputStream());
                Precondition.isResponseSuccess(conn);
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            throw new UaException(UaException.Code.UNKNOWN);
        }
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<User> ref) {
        return this.urlBuilder.buildGetUserPageUrl(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("User delete is not supported.");
    }
}
