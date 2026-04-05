package com.ua.sdk.group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GroupJsonWriter extends AbstractGsonWriter<Group> {
    public GroupJsonWriter() {
        super(GsonFactory.newGroupInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(Group entity, Gson gson, OutputStreamWriter writer) throws UaException {
        Type type = new TypeToken<Group>() { // from class: com.ua.sdk.group.GroupJsonWriter.1
        }.getType();
        gson.toJson(entity, type, writer);
    }
}
