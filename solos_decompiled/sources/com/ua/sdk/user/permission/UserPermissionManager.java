package com.ua.sdk.user.permission;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface UserPermissionManager {
    EntityList<UserPermission> fetchUserPermission(EntityRef entityRef) throws UaException;

    Request fetchUserPermission(EntityRef entityRef, FetchCallback<EntityList<UserPermission>> fetchCallback);

    EntityList<UserPermission> fetchUserPermissionList() throws UaException;

    Request fetchUserPermissionList(FetchCallback<EntityList<UserPermission>> fetchCallback);
}
