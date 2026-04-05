package com.ua.sdk.group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GroupJsonParser extends AbstractGsonParser<Group> {
    public GroupJsonParser() {
        super(GsonFactory.newGroupInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public Group read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<Group>() { // from class: com.ua.sdk.group.GroupJsonParser.1
        }.getType();
        return (Group) gson.fromJson(reader, type);
    }
}
