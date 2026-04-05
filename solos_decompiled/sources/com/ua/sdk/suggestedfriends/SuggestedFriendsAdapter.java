package com.ua.sdk.suggestedfriends;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsAdapter implements JsonSerializer<SuggestedFriends>, JsonDeserializer<SuggestedFriends> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public SuggestedFriends deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SuggestedFriends suggestedFriends = (SuggestedFriends) context.deserialize(json, SuggestedFriendsImpl.class);
        return suggestedFriends;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(SuggestedFriends src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
