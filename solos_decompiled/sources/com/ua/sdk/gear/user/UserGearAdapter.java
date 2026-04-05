package com.ua.sdk.gear.user;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.cache.EntityDatabase;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearAdapter implements JsonDeserializer<UserGear>, JsonSerializer<UserGear> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public UserGear deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject embedded = jsonObject.getAsJsonObject("_embedded");
        if (embedded != null) {
            JsonObject gear = embedded.getAsJsonObject("gear");
            JsonObject links = embedded.getAsJsonObject(EntityDatabase.LINKS.TABLE_SUFFIX);
            gear.add(EntityDatabase.LINKS.TABLE_SUFFIX, links);
            jsonObject.add("gear", gear);
        }
        return (UserGear) context.deserialize(jsonObject, UserGearImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(UserGear src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src, src.getClass());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (src.getGear() != null && src.getGear().getRef() != null) {
            String gearRef = src.getGear().getRef().getHref();
            jsonObject.add("gear", context.serialize(gearRef));
        }
        if (src.getDefaultActivities() != null) {
            jsonObject.remove(EntityDatabase.LINKS.TABLE_SUFFIX);
            List<EntityRef<ActivityType>> activityTypes = src.getDefaultActivities();
            JsonArray jsonArray = new JsonArray();
            for (EntityRef<ActivityType> activityType : activityTypes) {
                String activityRef = activityType.getHref();
                jsonArray.add(context.serialize(activityRef));
            }
            jsonObject.add("default_activities", jsonArray);
        }
        return jsonObject;
    }
}
