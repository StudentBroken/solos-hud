package com.ua.sdk.page;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.page.association.PageAssociation;
import com.ua.sdk.page.association.PageAssociationRef;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface PageManager {
    Request createPageFollow(EntityRef<Page> entityRef, EntityRef<User> entityRef2, CreateCallback<PageFollow> createCallback);

    PageFollow createPageFollow(EntityRef<Page> entityRef, EntityRef<User> entityRef2) throws UaException;

    EntityList<PageFollow> createPageFollows(List<EntityRef<Page>> list, EntityRef<User> entityRef) throws UaException;

    Request createPageFollows(List<EntityRef<Page>> list, EntityRef<User> entityRef, CreateCallback<EntityList<PageFollow>> createCallback);

    Request deletePageFollow(EntityRef<PageFollow> entityRef, DeleteCallback<Reference> deleteCallback);

    void deletePageFollow(EntityRef<PageFollow> entityRef) throws UaException;

    EntityList<PageFollow> fetchIsFollowingPage(EntityRef<User> entityRef, EntityRef<Page> entityRef2) throws UaException;

    Request fetchIsFollowingPage(EntityRef<User> entityRef, EntityRef<Page> entityRef2, FetchCallback<EntityList<PageFollow>> fetchCallback);

    Request fetchPage(EntityRef<Page> entityRef, FetchCallback<Page> fetchCallback);

    Page fetchPage(EntityRef<Page> entityRef) throws UaException;

    EntityList<PageAssociation> fetchPageAssociation(PageAssociationRef pageAssociationRef) throws UaException;

    Request fetchPageAssociation(PageAssociationRef pageAssociationRef, FetchCallback<EntityList<PageAssociation>> fetchCallback);

    EntityList<PageFollow> fetchPageFollowList(EntityListRef<PageFollow> entityListRef) throws UaException;

    Request fetchPageFollowList(EntityListRef<PageFollow> entityListRef, FetchCallback<EntityList<PageFollow>> fetchCallback);

    EntityList<Page> fetchPageList(EntityListRef<Page> entityListRef) throws UaException;

    Request fetchPageList(EntityListRef<Page> entityListRef, FetchCallback<EntityList<Page>> fetchCallback);

    Request fetchPageType(EntityRef<PageType> entityRef, FetchCallback<PageType> fetchCallback);

    PageType fetchPageType(EntityRef<PageType> entityRef) throws UaException;

    EntityList<PageType> fetchPageTypeList() throws UaException;

    Request fetchPageTypeList(FetchCallback<EntityList<PageType>> fetchCallback);
}
