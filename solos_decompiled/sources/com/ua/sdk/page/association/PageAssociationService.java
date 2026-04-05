package com.ua.sdk.page.association;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.Reference;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.internal.AbstractResourceService;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.internal.net.UrlBuilder;
import java.net.URL;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationService extends AbstractResourceService<PageAssociation> {
    public PageAssociationService(ConnectionFactory connFactory, AuthenticationManager authManager, UrlBuilder urlBuilder, JsonParser<PageAssociation> pageAssociationParser, JsonParser<? extends EntityList<PageAssociation>> pageAssociationListParser, JsonWriter<PageAssociation> pageAssociationRequestWriter) {
        super(connFactory, authManager, urlBuilder, pageAssociationParser, pageAssociationRequestWriter, pageAssociationListParser);
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getCreateUrl() {
        URL url = this.urlBuilder.buildCreatePageAssociationUrl();
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchUrl(Reference ref) {
        URL url = this.urlBuilder.buildGetPageAssociationUrl(ref);
        return url;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractResourceService
    public URL getSaveUrl(PageAssociation resource) {
        throw new UnsupportedOperationException("Save PageAssociation is not supported.");
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getDeleteUrl(Reference ref) {
        URL url = this.urlBuilder.buildDeletePageAssociationUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getFetchPageUrl(EntityListRef<PageAssociation> ref) {
        URL url = this.urlBuilder.buildGetPageAssociationsUrl(ref);
        return url;
    }

    @Override // com.ua.sdk.internal.AbstractResourceService
    protected URL getPatchUrl(Reference ref) {
        throw new UnsupportedOperationException("Save PageAssociation is not supported.");
    }
}
