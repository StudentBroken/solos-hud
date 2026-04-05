package com.ua.sdk.group.invite;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteJsonWriter extends AbstractGsonWriter<GroupInvite> {
    public GroupInviteJsonWriter() {
        super(GsonFactory.newGroupInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(GroupInvite entity, Gson gson, OutputStreamWriter writer) throws UaException {
        gson.toJson(entity, writer);
    }
}
