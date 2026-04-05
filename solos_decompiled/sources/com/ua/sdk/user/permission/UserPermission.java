package com.ua.sdk.user.permission;

import com.ua.sdk.Resource;

/* JADX INFO: loaded from: classes65.dex */
public interface UserPermission extends Resource {
    String getPermission();

    String getResource();

    void setPermission(String str);

    void setResource(String str);
}
