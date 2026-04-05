package com.ua.sdk.internal;

import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.NetworkError;
import com.ua.sdk.Reference;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.net.UrlBuilder;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.concurrent.Callable;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AbstractResourceService<T extends Resource> implements EntityService<T> {
    protected final AuthenticationManager authManager;
    protected final ConnectionFactory connFactory;
    protected final JsonParser<? extends EntityList<T>> jsonPageParser;
    protected final JsonParser<T> jsonParser;
    protected final JsonWriter<T> jsonWriter;
    protected final UrlBuilder urlBuilder;

    protected abstract URL getCreateUrl();

    protected abstract URL getDeleteUrl(Reference reference);

    protected abstract URL getFetchPageUrl(EntityListRef<T> entityListRef);

    protected abstract URL getFetchUrl(Reference reference);

    protected abstract URL getPatchUrl(Reference reference);

    protected abstract URL getSaveUrl(T t);

    public AbstractResourceService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<T> jsonParser, JsonWriter<T> jsonWriter, JsonParser<? extends EntityList<T>> jsonPageParser) {
        this.connFactory = (ConnectionFactory) Precondition.isNotNull(connFactory);
        this.urlBuilder = (UrlBuilder) Precondition.isNotNull(urlBuilder);
        this.jsonParser = jsonParser;
        this.jsonWriter = jsonWriter;
        this.authManager = authManager;
        this.jsonPageParser = jsonPageParser;
    }

    protected final <R> R call(Callable<R> callable) throws UaException {
        return (R) call(callable, true);
    }

    private <R> R call(Callable<R> callable, boolean z) throws UaException {
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        try {
            return callable.call();
        } catch (NetworkError e) {
            if (e.getResponseCode() == 401) {
                if (z) {
                    this.authManager.refreshToken(jElapsedRealtime);
                    return (R) call(callable, false);
                }
                throw new UaException(UaException.Code.NOT_AUTHENTICATED, e);
            }
            throw e;
        } catch (UaException e2) {
            throw e2;
        } catch (InterruptedIOException e3) {
            throw new UaException(UaException.Code.CANCELED, e3);
        } catch (IOException e4) {
            throw new UaException(UaException.Code.UNKNOWN, e4);
        } catch (Exception e5) {
            if (Build.VERSION.SDK_INT >= 11 && (e5 instanceof NetworkOnMainThreadException)) {
                throw new UaException(UaException.Code.NETWORK_ON_MAIN_THREAD, e5);
            }
            throw new UaException(UaException.Code.UNKNOWN, e5);
        }
    }

    protected final void checkAuthentication(AuthenticationManager.AuthenticationType type) throws UaException {
        if (type == AuthenticationManager.AuthenticationType.USER) {
            Precondition.isAuthenticated(this.authManager);
        }
    }

    @Override // com.ua.sdk.internal.EntityService
    public T create(T resource) throws UaException {
        try {
            checkAuthentication(getCreateAuthenticationType());
            Callable<T> createCallable = getCreateCallable(resource);
            return (T) call(createCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected Callable<T> getCreateCallable(final T t) throws UaException {
        Precondition.isNotNull(this.jsonWriter, "jsonWriter");
        Precondition.isNotNull(this.jsonParser, "jsonParser");
        return (Callable<T>) new Callable<T>() { // from class: com.ua.sdk.internal.AbstractResourceService.1
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Precondition.isNotNull(t, "resource");
                HttpsURLConnection sslConnection = AbstractResourceService.this.connFactory.getSslConnection(AbstractResourceService.this.getCreateUrl());
                AbstractResourceService.this.authManager.sign(sslConnection, AbstractResourceService.this.getCreateAuthenticationType());
                sslConnection.setRequestMethod(HttpRequest.METHOD_POST);
                sslConnection.setDoOutput(true);
                sslConnection.setUseCaches(false);
                AbstractResourceService.this.jsonWriter.write((T) t, sslConnection.getOutputStream());
                Precondition.isExpectedResponse(sslConnection, 201);
                return AbstractResourceService.this.jsonParser.parse(sslConnection.getInputStream());
            }
        };
    }

    protected AuthenticationManager.AuthenticationType getCreateAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }

    @Override // com.ua.sdk.internal.EntityService
    public T fetch(Reference ref) throws UaException {
        try {
            checkAuthentication(getFetchAuthenticationType());
            Callable<T> fetchCallable = getFetchCallable(ref);
            return (T) call(fetchCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected Callable<T> getFetchCallable(final Reference reference) throws UaException {
        Precondition.isNotNull(this.jsonParser, "jsonParser");
        return (Callable<T>) new Callable<T>() { // from class: com.ua.sdk.internal.AbstractResourceService.2
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Precondition.isNotNull(reference, "ref");
                URL url = AbstractResourceService.this.getFetchUrl(reference);
                HttpsURLConnection conn = AbstractResourceService.this.connFactory.getSslConnection(url);
                try {
                    AbstractResourceService.this.authManager.sign(conn, AbstractResourceService.this.getFetchAuthenticationType());
                    conn.setRequestMethod(HttpRequest.METHOD_GET);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    Precondition.isExpectedResponse(conn, 200);
                    return AbstractResourceService.this.jsonParser.parse(conn.getInputStream());
                } finally {
                    conn.disconnect();
                }
            }
        };
    }

    protected AuthenticationManager.AuthenticationType getFetchAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }

    @Override // com.ua.sdk.internal.EntityService
    public T save(T resource) throws UaException {
        try {
            checkAuthentication(getSaveAuthenticationType());
            Callable<T> saveCallable = getSaveCallable(resource);
            return (T) call(saveCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected Callable<T> getSaveCallable(final T t) throws UaException {
        Precondition.isNotNull(this.jsonWriter, "jsonWriter");
        Precondition.isNotNull(this.jsonParser, "jsonParser");
        return (Callable<T>) new Callable<T>() { // from class: com.ua.sdk.internal.AbstractResourceService.3
            /* JADX WARN: Type inference incomplete: some casts might be missing */
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                Precondition.isNotNull(t, "resource");
                HttpsURLConnection sslConnection = AbstractResourceService.this.connFactory.getSslConnection(AbstractResourceService.this.getSaveUrl(t));
                try {
                    AbstractResourceService.this.authManager.sign(sslConnection, AbstractResourceService.this.getSaveAuthenticationType());
                    sslConnection.setRequestMethod(HttpRequest.METHOD_PUT);
                    sslConnection.setDoOutput(true);
                    sslConnection.setUseCaches(false);
                    AbstractResourceService.this.jsonWriter.write((T) t, sslConnection.getOutputStream());
                    Precondition.isExpectedResponse(sslConnection, 200);
                    return AbstractResourceService.this.jsonParser.parse(sslConnection.getInputStream());
                } finally {
                    sslConnection.disconnect();
                }
            }
        };
    }

    protected AuthenticationManager.AuthenticationType getSaveAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }

    @Override // com.ua.sdk.internal.EntityService
    public void delete(Reference ref) throws UaException {
        try {
            checkAuthentication(getDeleteAuthenticationType());
            Callable<Void> deleteCallable = getDeleteCallable(ref);
            call(deleteCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected AuthenticationManager.AuthenticationType getDeleteAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }

    private Callable<Void> getDeleteCallable(final Reference ref) throws UaException {
        Precondition.isNotNull(ref, "ref");
        return new Callable<Void>() { // from class: com.ua.sdk.internal.AbstractResourceService.4
            @Override // java.util.concurrent.Callable
            public Void call() throws Exception {
                URL url = AbstractResourceService.this.getDeleteUrl(ref);
                HttpsURLConnection conn = AbstractResourceService.this.connFactory.getSslConnection(url);
                try {
                    AbstractResourceService.this.authManager.sign(conn, AbstractResourceService.this.getDeleteAuthenticationType());
                    conn.setRequestMethod(HttpRequest.METHOD_DELETE);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    Precondition.isExpectedResponse(conn, 204);
                    conn.disconnect();
                    return null;
                } catch (Throwable th) {
                    conn.disconnect();
                    throw th;
                }
            }
        };
    }

    @Override // com.ua.sdk.internal.EntityService
    public T patch(T resource, Reference ref) throws UaException {
        try {
            checkAuthentication(getPatchAuthenticationType());
            Callable<T> patchCallable = getPatchCallable(resource, ref);
            return (T) call(patchCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected Callable<T> getPatchCallable(final T t, final Reference reference) throws UaException {
        Precondition.isNotNull(this.jsonWriter, "jsonWriter");
        Precondition.isNotNull(this.jsonParser, "jsonParser");
        Precondition.isNotNull(t, "resource");
        return (Callable<T>) new Callable<T>() { // from class: com.ua.sdk.internal.AbstractResourceService.5
            @Override // java.util.concurrent.Callable
            public T call() throws Exception {
                HttpsURLConnection sslConnection = AbstractResourceService.this.connFactory.getSslConnection(AbstractResourceService.this.getPatchUrl(reference));
                try {
                    AbstractResourceService.this.authManager.sign(sslConnection, AbstractResourceService.this.getPatchAuthenticationType());
                    sslConnection.setRequestMethod(HttpRequest.METHOD_POST);
                    sslConnection.setDoOutput(true);
                    sslConnection.setUseCaches(false);
                    sslConnection.addRequestProperty("X-HTTP-Method-Override", "PATCH");
                    AbstractResourceService.this.jsonWriter.write((T) t, sslConnection.getOutputStream());
                    Precondition.isResponseSuccess(sslConnection);
                    return AbstractResourceService.this.jsonParser.parse(sslConnection.getInputStream());
                } finally {
                    sslConnection.disconnect();
                }
            }
        };
    }

    protected AuthenticationManager.AuthenticationType getPatchAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }

    @Override // com.ua.sdk.internal.EntityService
    public EntityList<T> fetchPage(EntityListRef<T> ref) throws UaException {
        try {
            checkAuthentication(getFetchPageAuthenticationType());
            Callable<EntityList<T>> fetchPageCallable = getFetchPageCallable(ref);
            return (EntityList) call(fetchPageCallable);
        } catch (UaException e) {
            throw e;
        } catch (Exception e2) {
            throw new UaException(e2);
        }
    }

    protected Callable<EntityList<T>> getFetchPageCallable(final EntityListRef<T> entityListRef) throws UaException {
        Precondition.isNotNull(this.jsonPageParser, "jsonPageParser");
        Precondition.isNotNull(entityListRef, "ref");
        return (Callable<EntityList<T>>) new Callable<EntityList<T>>() { // from class: com.ua.sdk.internal.AbstractResourceService.6
            @Override // java.util.concurrent.Callable
            public EntityList<T> call() throws Exception {
                URL url = AbstractResourceService.this.getFetchPageUrl(entityListRef);
                HttpsURLConnection conn = AbstractResourceService.this.connFactory.getSslConnection(url);
                try {
                    AbstractResourceService.this.authManager.sign(conn, AbstractResourceService.this.getFetchPageAuthenticationType());
                    conn.setRequestMethod(HttpRequest.METHOD_GET);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    Precondition.isExpectedResponse(conn, 200);
                    return AbstractResourceService.this.jsonPageParser.parse(conn.getInputStream());
                } finally {
                    conn.disconnect();
                }
            }
        };
    }

    protected AuthenticationManager.AuthenticationType getFetchPageAuthenticationType() {
        return AuthenticationManager.AuthenticationType.USER;
    }
}
