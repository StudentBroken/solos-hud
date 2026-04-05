package com.ua.sdk.page.association;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.page.Page;

/* JADX INFO: loaded from: classes65.dex */
public interface PageAssociationManager {
    Request createPageAssociation(EntityRef<Page> entityRef, EntityRef<Page> entityRef2, CreateCallback<Page> createCallback);

    PageAssociation createPageAssociation(EntityRef<Page> entityRef, EntityRef<Page> entityRef2) throws UaException;

    Request fetchPageAssociation(EntityRef<PageAssociation> entityRef, FetchCallback<PageAssociation> fetchCallback);

    PageAssociation fetchPageAssociation(EntityRef<PageAssociation> entityRef) throws UaException;

    EntityList<PageAssociation> fetchPageAssociationList(EntityListRef<PageAssociation> entityListRef) throws UaException;

    Request fetchPageAssociationList(EntityListRef<PageAssociation> entityListRef, FetchCallback<EntityList<PageAssociation>> fetchCallback);
}
