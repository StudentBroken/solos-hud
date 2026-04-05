package com.ua.sdk.activitystory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.sdk.activitystory.object.ActivityStoryHighlightImpl;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryHighlightAdapter implements JsonSerializer<ActivityStoryHighlight>, JsonDeserializer<ActivityStoryHighlight> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ActivityStoryHighlight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (ActivityStoryHighlight) context.deserialize(json, ActivityStoryHighlightImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ActivityStoryHighlight src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
