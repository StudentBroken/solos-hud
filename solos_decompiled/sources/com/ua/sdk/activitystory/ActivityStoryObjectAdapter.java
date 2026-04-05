package com.ua.sdk.activitystory;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kopin.solos.analytics.Analytics;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryToutObject;
import com.ua.sdk.activitystory.object.ActivityStoryActigraphyObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryAdObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryCommentObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryGroupLeaderboardObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryGroupObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryHighlightImpl;
import com.ua.sdk.activitystory.object.ActivityStoryLikeObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryRepostObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryRouteObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryStatusObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryToutObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryUserObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryWorkoutObjectImpl;
import com.ua.sdk.location.Location;
import com.ua.sdk.location.LocationImpl;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryObjectAdapter implements JsonSerializer<ActivityStoryObject>, JsonDeserializer<ActivityStoryObject> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ActivityStoryObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElement = jsonObject.get(ShareConstants.MEDIA_TYPE);
        if (typeElement == null) {
            return null;
        }
        String type = typeElement.getAsString();
        Class clazz = null;
        if (Analytics.Events.WORKOUT.equals(type)) {
            clazz = ActivityStoryWorkoutObjectImpl.class;
        } else if ("user".equals(type)) {
            clazz = ActivityStoryUserObjectImpl.class;
        } else if ("status".equals(type)) {
            clazz = ActivityStoryStatusObjectImpl.class;
        } else if ("repost".equals(type)) {
            clazz = ActivityStoryRepostObjectImpl.class;
        } else if ("group".equals(type)) {
            clazz = ActivityStoryGroupObjectImpl.class;
        } else if ("group_leaderboard".equals(type)) {
            clazz = ActivityStoryGroupLeaderboardObjectImpl.class;
        } else if ("route".equals(type)) {
            clazz = ActivityStoryRouteObjectImpl.class;
        } else if ("actigraphy".equals(type)) {
            clazz = ActivityStoryActigraphyObjectImpl.class;
        } else if ("comment".equals(type)) {
            clazz = ActivityStoryCommentObjectImpl.class;
        } else {
            if ("ad".equals(type)) {
                return new ActivityStoryAdObjectImpl();
            }
            if ("like".equals(type)) {
                return new ActivityStoryLikeObjectImpl();
            }
            if ("tout".equals(type)) {
                ActivityStoryToutObject.Subtype subtype = null;
                JsonElement subtypeElement = jsonObject.get("subtype");
                if (subtypeElement != null) {
                    String subtypeString = subtypeElement.getAsString();
                    if ("find_friends".equals(subtypeString)) {
                        subtype = ActivityStoryToutObject.Subtype.FIND_FRIENDS;
                    }
                }
                return new ActivityStoryToutObjectImpl(subtype);
            }
        }
        if (clazz != null) {
            return (ActivityStoryObject) context.deserialize(json, clazz);
        }
        return null;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ActivityStoryObject src, Type typeOfSrc, JsonSerializationContext context) {
        ActivityStoryToutObject.Subtype subtype;
        JsonElement element = context.serialize(src, src.getClass());
        JsonObject jsonObject = element.getAsJsonObject();
        jsonObject.addProperty(ShareConstants.MEDIA_TYPE, src.getType().toString().toLowerCase());
        if (src.getType() == ActivityStoryObject.Type.TOUT && (subtype = ((ActivityStoryToutObject) src).getSubtype()) != null) {
            switch (subtype) {
                case FIND_FRIENDS:
                    jsonObject.addProperty("subtype", "find_friends");
                default:
                    return jsonObject;
            }
        }
        return jsonObject;
    }

    Location getLocation(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    JsonObject obj = val.getAsJsonObject();
                    String country = getString(obj, "country");
                    String region = getString(obj, "region");
                    String locality = getString(obj, "locality");
                    String address = getString(obj, "address");
                    return new LocationImpl(country, region, locality, address);
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }

    List<ActivityStoryHighlight> getHighlights(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    JsonArray array = val.getAsJsonArray();
                    int size = array.size();
                    ArrayList<ActivityStoryHighlight> highlights = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        JsonObject obj = array.get(i).getAsJsonObject();
                        ActivityStoryHighlightImpl highlight = new ActivityStoryHighlightImpl();
                        String key = getString(obj, "key");
                        highlight.setKey(key);
                        highlight.setPercentile(getDouble(obj, "percentile"));
                        highlight.setThumbnailUrl(getString(obj, "thumbnail_url"));
                        if (jsonObject.has(key)) {
                            JsonElement highlightVal = jsonObject.get(key);
                            if (highlightVal.isJsonPrimitive()) {
                                JsonPrimitive primitive = highlightVal.getAsJsonPrimitive();
                                if (primitive.isNumber()) {
                                    highlight.setValue(primitive.getAsNumber());
                                }
                            }
                        }
                        highlights.add(highlight);
                    }
                    return highlights;
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }

    String getString(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    return val.getAsString();
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }

    Privacy getPrivacy(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    return PrivacyHelper.getPrivacyFromId(val.getAsInt());
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }

    Double getDouble(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    return Double.valueOf(val.getAsDouble());
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }

    Integer getInteger(JsonObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                JsonElement val = jsonObject.get(field);
                if (!val.isJsonNull()) {
                    return Integer.valueOf(val.getAsInt());
                }
            }
        } catch (Throwable t) {
            UaLog.error("Unable to parse " + field, t);
        }
        return null;
    }
}
