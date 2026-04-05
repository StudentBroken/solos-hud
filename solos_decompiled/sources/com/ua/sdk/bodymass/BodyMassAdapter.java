package com.ua.sdk.bodymass;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassAdapter implements JsonDeserializer<BodyMass>, JsonSerializer<BodyMass> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public BodyMass deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (BodyMass) context.deserialize(json, BodyMassImpl.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(BodyMass src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }
}
