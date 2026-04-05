package com.ua.sdk.page.follow;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.page.Page;
import com.ua.sdk.user.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowManager extends AbstractManager<PageFollow> {
    public PageFollowManager(CacheSettings cacheSettings, Cache memCache, DiskCache<PageFollow> diskCache, EntityService<PageFollow> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
    }

    public EntityList<PageFollow> fetchPageFollowList(EntityListRef<PageFollow> ref) throws UaException {
        return fetchPage(ref);
    }

    public Request fetchPageFollowList(EntityListRef<PageFollow> ref, FetchCallback<EntityList<PageFollow>> callback) {
        return fetchPage(ref, callback);
    }

    public EntityList<PageFollow> fetchIsFollowingPage(EntityRef<User> user, EntityRef<Page> page) throws UaException {
        return fetchPage(PageFollowRef.getBuilder().setUserId(user.getId()).setPageId(page.getId()).build());
    }

    public Request fetchIsFollowingPage(EntityRef<User> user, EntityRef<Page> page, FetchCallback<EntityList<PageFollow>> callback) {
        return fetchPage(PageFollowRef.getBuilder().setUserId(user.getId()).setPageId(page.getId()).build(), callback);
    }

    public void deletePageFollow(EntityRef<PageFollow> pageFollow) throws UaException {
        delete(pageFollow);
    }

    public Request deletePageFollow(EntityRef<PageFollow> pageFollow, DeleteCallback<Reference> callback) {
        return delete(pageFollow, callback);
    }

    public PageFollow createPageFollow(EntityRef<Page> pageRef, EntityRef<User> userRef) throws UaException {
        PageFollowImpl pageFollow = new PageFollowImpl();
        pageFollow.setPageRef(pageRef);
        pageFollow.setUserRef(userRef);
        return create(pageFollow);
    }

    public Request createPageFollow(EntityRef<Page> pageRef, EntityRef<User> userRef, CreateCallback<PageFollow> callback) {
        PageFollowImpl pageFollow = new PageFollowImpl();
        pageFollow.setPageRef(pageRef);
        pageFollow.setUserRef(userRef);
        return create(pageFollow, callback);
    }

    public EntityList<PageFollow> createPageFollows(List<EntityRef<Page>> pageRefs, EntityRef<User> userRef) throws UaException {
        Precondition.isNotNull(pageRefs);
        Precondition.isNotNull(userRef);
        PageFollowImpl pageFollows = new PageFollowImpl();
        for (EntityRef<Page> pageRef : pageRefs) {
            PageFollowImpl follow = new PageFollowImpl();
            follow.setPageRef(pageRef);
            follow.setUserRef(userRef);
            pageFollows.addPageFollow(follow);
        }
        return ((PageFollowService) this.service).patch(pageFollows);
    }

    public Request createPageFollows(final List<EntityRef<Page>> pageRefs, final EntityRef<User> userRef, CreateCallback<EntityList<PageFollow>> callback) {
        final CreateRequest<EntityList<PageFollow>> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.page.follow.PageFollowManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(PageFollowManager.this.createPageFollows(pageRefs, userRef), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
