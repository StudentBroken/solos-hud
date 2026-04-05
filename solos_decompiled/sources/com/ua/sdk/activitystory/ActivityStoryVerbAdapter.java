package com.ua.sdk.activitystory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryVerbAdapter implements JsonSerializer<ActivityStoryVerb>, JsonDeserializer<ActivityStoryVerb> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ActivityStoryVerb deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String val = json.getAsString();
        if (val == null) {
            return null;
        }
        try {
            return ActivityStoryVerb.valueOf(val.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ActivityStoryVerb src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString().toLowerCase(), String.class);
    }
}
