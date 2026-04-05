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
import com.ua.sdk.suggestedfriends.SuggestedFriends;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface FriendshipManager {
    Request approvePendingFriendRequest(Friendship friendship, SaveCallback<Friendship> saveCallback);

    Request approvePendingFriendRequest(FriendshipRef friendshipRef, SaveCallback<Friendship> saveCallback);

    Friendship approvePendingFriendRequest(Friendship friendship) throws UaException;

    Friendship approvePendingFriendRequest(FriendshipRef friendshipRef) throws UaException;

    Request createNewFriendRequest(EntityRef<User> entityRef, String str, CreateCallback<Friendship> createCallback);

    Friendship createNewFriendRequest(EntityRef<User> entityRef, String str) throws UaException;

    EntityList<Friendship> createNewFriendRequests(List<EntityRef<User>> list, String str) throws UaException;

    Request createNewFriendRequests(List<EntityRef<User>> list, String str, CreateCallback<EntityList<Friendship>> createCallback);

    Request deleteOrDenyFriendship(EntityRef<Friendship> entityRef, DeleteCallback<Reference> deleteCallback);

    void deleteOrDenyFriendship(EntityRef<Friendship> entityRef) throws UaException;

    EntityList<Friendship> fetchFriendList(EntityListRef<Friendship> entityListRef) throws UaException;

    Request fetchFriendList(EntityListRef<Friendship> entityListRef, FetchCallback<EntityList<Friendship>> fetchCallback);

    EntityList<User> fetchMutualFriendList(EntityListRef<User> entityListRef) throws UaException;

    Request fetchMutualFriendList(EntityListRef<User> entityListRef, FetchCallback<EntityList<User>> fetchCallback);

    EntityList<SuggestedFriends> fetchSuggestedFriendList(EntityListRef<SuggestedFriends> entityListRef) throws UaException;

    Request fetchSuggestedFriendList(EntityListRef<SuggestedFriends> entityListRef, FetchCallback<EntityList<SuggestedFriends>> fetchCallback);
}
