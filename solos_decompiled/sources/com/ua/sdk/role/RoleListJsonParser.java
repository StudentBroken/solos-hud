package com.ua.sdk.role;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;

/* JADX INFO: loaded from: classes65.dex */
public class RoleListJsonParser extends AbstractGsonParser<RoleList> {
    public RoleListJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public RoleList read(Gson gson, JsonReader reader) throws UaException {
        RolePagedTO to = (RolePagedTO) gson.fromJson(reader, RolePagedTO.class);
        return RolePagedTO.toPage(to);
    }
}
