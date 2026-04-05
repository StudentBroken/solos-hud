package com.ua.sdk.page.association;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.page.Page;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationManagerImpl extends AbstractManager<PageAssociation> implements PageAssociationManager {
    public PageAssociationManagerImpl(CacheSettings cacheSettings, Cache memCache, DiskCache<PageAssociation> diskCache, EntityService<PageAssociation> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public EntityList<PageAssociation> fetchPageAssociationList(EntityListRef<PageAssociation> pageAssociationRef) throws UaException {
        return fetchPage(pageAssociationRef);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public Request fetchPageAssociationList(EntityListRef<PageAssociation> pageAssociationRef, FetchCallback<EntityList<PageAssociation>> callback) {
        return fetchPage(pageAssociationRef, callback);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public PageAssociation fetchPageAssociation(EntityRef<PageAssociation> ref) throws UaException {
        return fetch(ref);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public Request fetchPageAssociation(EntityRef<PageAssociation> ref, FetchCallback<PageAssociation> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public PageAssociation createPageAssociation(EntityRef<Page> fromPage, EntityRef<Page> toPage) throws UaException {
        PageAssociation pageAssociation = buildPageAssociation(fromPage, toPage);
        return (PageAssociation) this.service.create(pageAssociation);
    }

    @Override // com.ua.sdk.page.association.PageAssociationManager
    public Request createPageAssociation(EntityRef<Page> fromPage, EntityRef<Page> toPage, CreateCallback<Page> callback) {
        return null;
    }

    private PageAssociation buildPageAssociation(EntityRef<Page> fromPage, EntityRef<Page> toPage) {
        PageAssociationImpl pageAssociation = new PageAssociationImpl();
        Link from_page = new Link(((LinkEntityRef) fromPage).getHref(), fromPage.getId());
        pageAssociation.setLink("from_page", from_page);
        Link to_page = new Link(((LinkEntityRef) toPage).getHref(), toPage.getId());
        pageAssociation.setLink("to_page", to_page);
        return pageAssociation;
    }
}
