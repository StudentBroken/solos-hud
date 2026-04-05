package com.ua.sdk.gear.user;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface UserGearManager {
    UserGear createEmptyUserGearObject();

    Request createUserGear(UserGear userGear, CreateCallback<UserGear> createCallback);

    UserGear createUserGear(UserGear userGear) throws UaException;

    Request deleteUserGear(EntityRef<UserGear> entityRef, DeleteCallback deleteCallback);

    void deleteUserGear(EntityRef<UserGear> entityRef) throws Exception;

    EntityList<UserGear> fetchUserGear(EntityListRef<UserGear> entityListRef) throws UaException;

    Request fetchUserGear(EntityListRef<UserGear> entityListRef, FetchCallback<EntityList<UserGear>> fetchCallback);

    Request updateUserGear(EntityRef<UserGear> entityRef, UserGear userGear, CreateCallback<UserGear> createCallback);

    UserGear updateUserGear(EntityRef<UserGear> entityRef, UserGear userGear) throws UaException;
}
