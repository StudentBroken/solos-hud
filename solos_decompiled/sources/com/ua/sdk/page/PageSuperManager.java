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
import com.ua.sdk.page.follow.PageFollowManager;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class PageSuperManager implements PageManager {
    private final PageFollowManager pageFollowManager;
    private final PageManagerImpl pageManager;

    public PageSuperManager(PageManagerImpl pageManager, PageFollowManager pageFollowManager) {
        this.pageManager = pageManager;
        this.pageFollowManager = pageFollowManager;
    }

    @Override // com.ua.sdk.page.PageManager
    public Page fetchPage(EntityRef<Page> ref) throws UaException {
        return this.pageManager.fetchPage(ref);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPage(EntityRef<Page> ref, FetchCallback<Page> callback) {
        return this.pageManager.fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<PageType> fetchPageTypeList() throws UaException {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPageTypeList(FetchCallback<EntityList<PageType>> callback) {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public PageType fetchPageType(EntityRef<PageType> ref) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPageType(EntityRef<PageType> ref, FetchCallback<PageType> callback) {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<PageAssociation> fetchPageAssociation(PageAssociationRef ref) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPageAssociation(PageAssociationRef ref, FetchCallback<EntityList<PageAssociation>> callback) {
        return null;
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<PageFollow> fetchPageFollowList(EntityListRef<PageFollow> ref) throws UaException {
        return this.pageFollowManager.fetchPageFollowList(ref);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPageFollowList(EntityListRef<PageFollow> ref, FetchCallback<EntityList<PageFollow>> callback) {
        return this.pageFollowManager.fetchPageFollowList(ref, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<PageFollow> fetchIsFollowingPage(EntityRef<User> user, EntityRef<Page> page) throws UaException {
        return this.pageFollowManager.fetchIsFollowingPage(user, page);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchIsFollowingPage(EntityRef<User> user, EntityRef<Page> page, FetchCallback<EntityList<PageFollow>> callback) {
        return this.pageFollowManager.fetchIsFollowingPage(user, page, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public void deletePageFollow(EntityRef<PageFollow> pageFollowRef) throws UaException {
        this.pageFollowManager.deletePageFollow(pageFollowRef);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request deletePageFollow(EntityRef<PageFollow> pageFollowRef, DeleteCallback<Reference> callback) {
        return this.pageFollowManager.deletePageFollow(pageFollowRef, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public PageFollow createPageFollow(EntityRef<Page> pageRef, EntityRef<User> userRef) throws UaException {
        return this.pageFollowManager.createPageFollow(pageRef, userRef);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request createPageFollow(EntityRef<Page> pageRef, EntityRef<User> userRef, CreateCallback<PageFollow> callback) {
        return this.pageFollowManager.createPageFollow(pageRef, userRef, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<PageFollow> createPageFollows(List<EntityRef<Page>> pageRefs, EntityRef<User> userRef) throws UaException {
        return this.pageFollowManager.createPageFollows(pageRefs, userRef);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request createPageFollows(List<EntityRef<Page>> pageRefs, EntityRef<User> userRef, CreateCallback<EntityList<PageFollow>> callback) {
        return this.pageFollowManager.createPageFollows(pageRefs, userRef, callback);
    }

    @Override // com.ua.sdk.page.PageManager
    public EntityList<Page> fetchPageList(EntityListRef<Page> ref) throws UaException {
        return this.pageManager.fetchPageList(ref);
    }

    @Override // com.ua.sdk.page.PageManager
    public Request fetchPageList(EntityListRef<Page> ref, FetchCallback<EntityList<Page>> callback) {
        return this.pageManager.fetchPageList(ref, callback);
    }
}
