package com.ua.sdk.user.permission;

import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionList extends AbstractEntityList<UserPermission> {
    private static final String LIST_KEY = "user_permissions";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
