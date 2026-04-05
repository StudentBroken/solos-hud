package com.ua.sdk.user.permission;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionListJsonParser extends AbstractGsonParser<UserPermissionList> {
    public UserPermissionListJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public UserPermissionList read(Gson gson, JsonReader reader) throws UaException {
        UserPermissionPagedTO to = (UserPermissionPagedTO) gson.fromJson(reader, UserPermissionPagedTO.class);
        return UserPermissionPagedTO.toPage(to);
    }
}
