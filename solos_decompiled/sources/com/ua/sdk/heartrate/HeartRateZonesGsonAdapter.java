package com.ua.sdk.heartrate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZonesGsonAdapter implements JsonDeserializer<HeartRateZones>, JsonSerializer<HeartRateZones> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public HeartRateZones deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (HeartRateZones) context.deserialize(json, HeartRateZonesImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(HeartRateZones src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
