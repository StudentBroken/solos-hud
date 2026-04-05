package com.ua.sdk.group.invite;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteJsonParser extends AbstractGsonParser<GroupInvite> {
    public GroupInviteJsonParser() {
        super(GsonFactory.newGroupInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public GroupInvite read(Gson gson, JsonReader reader) throws UaException {
        return (GroupInvite) gson.fromJson(reader, GroupInvite.class);
    }
}
