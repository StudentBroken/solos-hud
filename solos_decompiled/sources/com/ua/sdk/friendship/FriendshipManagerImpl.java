package com.ua.sdk.friendship;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.suggestedfriends.SuggestedFriends;
import com.ua.sdk.suggestedfriends.SuggestedFriendsManager;
import com.ua.sdk.user.User;
import com.ua.sdk.user.UserManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipManagerImpl extends AbstractManager<Friendship> implements FriendshipManager {
    private SuggestedFriendsManager suggestedFriendsManager;
    private UserManager userManager;

    public FriendshipManagerImpl(UserManager userManager, SuggestedFriendsManager suggestedFriendsManager, CacheSettings cacheSettings, Cache memCache, DiskCache<Friendship> diskCache, EntityService<Friendship> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.userManager = (UserManager) Precondition.isNotNull(userManager, "userManager");
        this.suggestedFriendsManager = (SuggestedFriendsManager) Precondition.isNotNull(suggestedFriendsManager, "suggestedFriendsManager");
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public EntityList<Friendship> fetchFriendList(EntityListRef<Friendship> listRef) throws UaException {
        return fetchPage(listRef);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request fetchFriendList(EntityListRef<Friendship> listRef, FetchCallback<EntityList<Friendship>> callback) {
        return fetchPage(listRef, callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Friendship approvePendingFriendRequest(Friendship friendship) throws UaException {
        ((FriendshipImpl) friendship).setFriendshipStatus(FriendshipStatus.ACTIVE);
        return update(friendship);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request approvePendingFriendRequest(Friendship friendship, SaveCallback<Friendship> callback) {
        ((FriendshipImpl) friendship).setFriendshipStatus(FriendshipStatus.ACTIVE);
        return update(friendship, callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Friendship approvePendingFriendRequest(FriendshipRef friendshipRef) throws UaException {
        FriendshipImpl friendship = new FriendshipImpl(friendshipRef);
        friendship.setFriendshipStatus(FriendshipStatus.ACTIVE);
        return update(friendship);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request approvePendingFriendRequest(FriendshipRef friendshipRef, SaveCallback<Friendship> callback) {
        FriendshipImpl friendship = new FriendshipImpl(friendshipRef);
        friendship.setFriendshipStatus(FriendshipStatus.ACTIVE);
        return update(friendship, callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public void deleteOrDenyFriendship(EntityRef<Friendship> friendship) throws UaException {
        delete(friendship);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request deleteOrDenyFriendship(EntityRef<Friendship> friendship, DeleteCallback<Reference> callback) {
        return delete(friendship, callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Friendship createNewFriendRequest(EntityRef<User> toUser, String message) throws UaException {
        return create(getFriendshipBody(toUser, message));
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request createNewFriendRequest(EntityRef<User> toUser, String message, CreateCallback<Friendship> callback) {
        return create(getFriendshipBody(toUser, message), callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public EntityList<Friendship> createNewFriendRequests(List<EntityRef<User>> toUsers, String message) throws UaException {
        return ((FriendshipService) this.service).patch(getFriendships(toUsers, message));
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request createNewFriendRequests(final List<EntityRef<User>> toUsers, final String message, CreateCallback<EntityList<Friendship>> callback) {
        final CreateRequest<EntityList<Friendship>> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.friendship.FriendshipManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    request.done(FriendshipManagerImpl.this.createNewFriendRequests(toUsers, message), null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    private FriendshipImpl getFriendships(List<EntityRef<User>> toUsers, String message) {
        FriendshipImpl impl = new FriendshipImpl();
        for (EntityRef<User> user : toUsers) {
            impl.addFriendship(getFriendshipBody(user, message));
        }
        return impl;
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public EntityList<SuggestedFriends> fetchSuggestedFriendList(EntityListRef<SuggestedFriends> listRef) throws UaException {
        return this.suggestedFriendsManager.fetchSuggestedFriendList(listRef);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request fetchSuggestedFriendList(EntityListRef<SuggestedFriends> listRef, FetchCallback<EntityList<SuggestedFriends>> callback) {
        return this.suggestedFriendsManager.fetchSuggestedFriendList(listRef, callback);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public EntityList<User> fetchMutualFriendList(EntityListRef<User> listRef) throws UaException {
        return this.userManager.fetchUserList(listRef);
    }

    @Override // com.ua.sdk.friendship.FriendshipManager
    public Request fetchMutualFriendList(EntityListRef<User> listRef, FetchCallback<EntityList<User>> callback) {
        return this.userManager.fetchUserList(listRef, callback);
    }

    private FriendshipImpl getFriendshipBody(EntityRef<User> toUser, String message) {
        FriendshipImpl friendship = new FriendshipImpl();
        EntityRef<User> fromUser = (EntityRef) Precondition.isNotNull(this.userManager.getCurrentUserRef());
        EntityRef<User> toUser2 = (EntityRef) Precondition.isNotNull(toUser);
        ArrayList<Link> to_user = new ArrayList<>();
        to_user.add(new Link(toUser2.getHref(), toUser2.getId()));
        friendship.setLinksForRelation(FriendshipImpl.ARG_TO_USER, to_user);
        ArrayList<Link> from_user = new ArrayList<>();
        from_user.add(new Link(fromUser.getHref(), fromUser.getId()));
        friendship.setLinksForRelation(FriendshipImpl.ARG_FROM_USER, from_user);
        friendship.setMessage(message);
        return friendship;
    }
}
