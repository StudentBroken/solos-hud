package com.ua.sdk.user;

import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class CurrentUserRef extends LinkEntityRef<User> {
    public CurrentUserRef() {
        super("self", null);
    }

    @Override // com.ua.sdk.internal.LinkEntityRef
    public boolean checkCache() {
        return false;
    }
}
