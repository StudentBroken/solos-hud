package com.ua.sdk.group.invite;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteAdapter implements JsonDeserializer<GroupInvite>, JsonSerializer<GroupInvite> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public GroupInvite deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (GroupInvite) context.deserialize(json, GroupInviteImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(GroupInvite src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
