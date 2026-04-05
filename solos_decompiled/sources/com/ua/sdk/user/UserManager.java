package com.ua.sdk.user;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.cache.CachePolicy;

/* JADX INFO: loaded from: classes65.dex */
public interface UserManager {
    Request fetchUser(EntityRef<User> entityRef, FetchCallback<User> fetchCallback);

    Request fetchUser(EntityRef<User> entityRef, CachePolicy cachePolicy, FetchCallback<User> fetchCallback);

    User fetchUser(EntityRef<User> entityRef) throws UaException;

    EntityList<User> fetchUserList(EntityListRef<User> entityListRef) throws UaException;

    Request fetchUserList(EntityListRef<User> entityListRef, FetchCallback<EntityList<User>> fetchCallback);

    Request fetchUserList(EntityListRef<User> entityListRef, CachePolicy cachePolicy, FetchCallback<EntityList<User>> fetchCallback);

    User getCurrentUser() throws UaException;

    EntityRef<User> getCurrentUserRef();

    User newUser();

    void onLogout();

    Request updateUser(User user, SaveCallback<User> saveCallback);

    User updateUser(User user) throws UaException;
}
