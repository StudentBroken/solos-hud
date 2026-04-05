package com.ua.sdk.actigraphy;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyService extends AbstractResourceService<Actigraphy> {
    public ActigraphyService(ConnectionFactory connectionFactory, AuthenticationManager authenticationManager, UrlBuilder urlBuilder, JsonParser<EntityList<Actigraphy>> jsonParser) {
        super(connectionFactory, authenticationManager, urlBuilder, null, null, jsonParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        throw new UnsupportedOperationException("Create Actigraphy is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        throw new UnsupportedOperationException("Fetch Actigraphy is not supported.");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(Actigraphy resource) {
        throw new UnsupportedOperationException("Save Actigraphy is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        throw new UnsupportedOperationException("Delete Actigraphy is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<Actigraphy> ref) {
        if (!(ref instanceof ActigraphyListRef)) {
            throw new IllegalArgumentException("Use ActigraphyListRef.");
        }
        URL url = this.urlBuilder.buildGetActigraphyUrl((ActigraphyListRef) ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Patch Actigraphy is not supported.");
    }
}
