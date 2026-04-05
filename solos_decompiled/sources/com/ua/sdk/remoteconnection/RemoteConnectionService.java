package com.ua.sdk.remoteconnection;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.LinkListRef;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionService extends AbstractResourceService<RemoteConnection> {
    public EntityListRef<RemoteConnection> PAGE_REF;

    public RemoteConnectionService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<RemoteConnection> remoteConnectionParser, JsonParser<EntityList<RemoteConnection>> remoteConnectionPageParser) {
        super(connFactory, authManager, urlBuilder, remoteConnectionParser, null, remoteConnectionPageParser);
        this.PAGE_REF = new LinkListRef((String) null);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        throw new UnsupportedOperationException("Create RemoteConnection is unsupported.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(RemoteConnection resource) {
        throw new UnsupportedOperationException("Save RemoteConnection is unsupported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        URL url = this.urlBuilder.buildDeleteRemoteConnectionUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetRemoteConnectionUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService, com.ua.sdk.internal.EntityService
    public EntityList<RemoteConnection> fetchPage(EntityListRef<RemoteConnection> ref) throws UaException {
        if (ref == null) {
            ref = this.PAGE_REF;
        }
        return super.fetchPage(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<RemoteConnection> ref) {
        URL url = this.urlBuilder.buildGetRemoteConnectionUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch RemoteConnection is unsupported.");
    }
}
