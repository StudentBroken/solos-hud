package com.ua.sdk.activitystory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.object.ActivityStoryHighlightImpl;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryAttachmentTypeAdapter implements JsonSerializer<Attachment.Type>, JsonDeserializer<Attachment.Type> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Attachment.Type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (Attachment.Type) context.deserialize(json, ActivityStoryHighlightImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Attachment.Type src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
