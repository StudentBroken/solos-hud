package com.ua.sdk.user.permission;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;

/* JADX INFO: loaded from: classes65.dex */
public class UserPermissionJsonParser extends AbstractGsonParser<UserPermission> {
    public UserPermissionJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public UserPermission read(Gson gson, JsonReader reader) throws UaException {
        UserPermissionTO to = (UserPermissionTO) gson.fromJson(reader, UserPermissionTO.class);
        return UserPermissionTO.fromTransferObject(to);
    }
}
