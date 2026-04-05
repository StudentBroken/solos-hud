package com.ua.sdk.role;

import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;
import com.ua.sdk.role.Role;

/* JADX INFO: loaded from: classes65.dex */
public interface RoleManager {
    Role fetchRole(Role.Type type);

    EntityList<Role> fetchRoleList() throws UaException;

    Request fetchRoleList(FetchCallback<EntityList<Role>> fetchCallback);
}
