package com.ua.sdk.sleep;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class SleepMetricAdapter implements JsonDeserializer<SleepMetric>, JsonSerializer<SleepMetric> {
    @Override // com.google.gson.JsonDeserializer
    public SleepMetric deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (SleepMetricImpl) context.deserialize(json, SleepMetricImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(SleepMetric src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
