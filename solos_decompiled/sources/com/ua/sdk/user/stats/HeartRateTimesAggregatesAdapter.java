package com.ua.sdk.user.stats;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateTimesAggregatesAdapter implements JsonDeserializer<HeartRateTimesAggregate>, JsonSerializer<HeartRateTimesAggregate> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public HeartRateTimesAggregate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (HeartRateTimesAggregate) context.deserialize(json, HeartRateTimesAggregateImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(HeartRateTimesAggregate src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
