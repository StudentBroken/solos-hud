package com.ua.sdk.user.role;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface UserRoleManager {
    Request createUserRole(UserRole userRole, CreateCallback<UserRole> createCallback);

    UserRole createUserRole(UserRole userRole) throws UaException;

    Request fetchUserRole(EntityRef entityRef, FetchCallback<UserRole> fetchCallback);

    UserRole fetchUserRole(EntityRef entityRef) throws UaException;
}
