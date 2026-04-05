package com.ua.sdk.page.follow;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.page.Page;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public interface PageFollow extends Entity {
    EntityRef<Page> getPageReference();

    @Override // com.ua.sdk.Entity, com.ua.sdk.Resource
    EntityRef<PageFollow> getRef();

    EntityRef<User> getUserReference();
}
