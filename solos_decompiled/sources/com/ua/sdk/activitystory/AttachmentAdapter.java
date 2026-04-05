package com.ua.sdk.activitystory;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class AttachmentAdapter implements JsonSerializer<Attachment>, JsonDeserializer<Attachment> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Attachment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement typeJson;
        JsonObject obj = json.getAsJsonObject();
        JsonElement objectJson = obj.get("object");
        if (objectJson != null && (typeJson = objectJson.getAsJsonObject().get(ShareConstants.MEDIA_TYPE)) != null) {
            String type = typeJson.getAsString();
            if ("photo".equals(type)) {
                return (Attachment) context.deserialize(json, PhotoAttachmentImpl.class);
            }
            if ("video".equals(type)) {
                return (Attachment) context.deserialize(json, VideoAttachmentImpl.class);
            }
        }
        return null;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Attachment src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
