package com.ua.sdk.remoteconnection;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.UaException;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.LinkListRef;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeService extends AbstractResourceService<RemoteConnectionType> {
    public EntityListRef<RemoteConnectionType> PAGE_REF;
    public Reference REF;

    public RemoteConnectionTypeService(ConnectionFactory connFactory, UrlBuilder urlBuilder, AuthenticationManager authManager, JsonParser<RemoteConnectionType> parser, JsonParser<EntityList<RemoteConnectionType>> pageJsonParser) {
        super(connFactory, authManager, urlBuilder, parser, null, pageJsonParser);
        this.REF = new LinkEntityRef(null, null);
        this.PAGE_REF = new LinkListRef((String) null);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        throw new UnsupportedOperationException("Create RemooteConnectionType is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService, com.ua.sdk.internal.EntityService
    public RemoteConnectionType fetch(Reference ref) throws UaException {
        if (ref == null) {
            ref = this.REF;
        }
        return (RemoteConnectionType) super.fetch(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetRemoteConnectionTypeUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService, com.ua.sdk.internal.EntityService
    public EntityList<RemoteConnectionType> fetchPage(EntityListRef<RemoteConnectionType> ref) throws UaException {
        if (ref == null) {
            ref = this.PAGE_REF;
        }
        return super.fetchPage(ref);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<RemoteConnectionType> ref) {
        URL url = this.urlBuilder.buildGetRemoteConnectionTypeUrl(ref);
        return url;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(RemoteConnectionType resource) {
        throw new UnsupportedOperationException("Save RemooteConnectionType is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Delete RemooteConnectionType is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch RemooteConnectionType is not supported.");
    }
}
