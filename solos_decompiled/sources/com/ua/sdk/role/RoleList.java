package com.ua.sdk.role;

import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class RoleList extends AbstractEntityList<Role> {
    private static final String LIST_KEY = "roles";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
