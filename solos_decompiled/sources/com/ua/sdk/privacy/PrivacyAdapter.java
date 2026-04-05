package com.ua.sdk.privacy;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class PrivacyAdapter implements JsonSerializer<Privacy>, JsonDeserializer<Privacy> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Privacy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive()) {
            return null;
        }
        int id = json.getAsJsonPrimitive().getAsNumber().intValue();
        return PrivacyHelper.getPrivacyFromId(id);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Privacy src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive((Number) Integer.valueOf(src.getLevel().id));
    }
}
