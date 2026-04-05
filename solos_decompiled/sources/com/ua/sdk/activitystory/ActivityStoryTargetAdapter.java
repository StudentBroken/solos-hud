package com.ua.sdk.activitystory;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.sdk.activitystory.target.ActivityStoryGroupTarget;
import com.ua.sdk.activitystory.target.ActivityStoryUnknownTarget;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryTargetAdapter implements JsonSerializer<ActivityStoryTarget>, JsonDeserializer<ActivityStoryTarget> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ActivityStoryTarget deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElement = jsonObject.get(ShareConstants.MEDIA_TYPE);
        if (typeElement != null) {
            String type = typeElement.getAsString();
            if ("group".equals(type)) {
                return (ActivityStoryTarget) context.deserialize(json, ActivityStoryGroupTarget.class);
            }
        }
        return (ActivityStoryTarget) context.deserialize(json, ActivityStoryUnknownTarget.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ActivityStoryTarget src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
