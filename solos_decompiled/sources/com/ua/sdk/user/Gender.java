package com.ua.sdk.user;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kopin.solos.storage.settings.Prefs;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public enum Gender {
    FEMALE,
    MALE;

    public static class GenderAdapter implements JsonSerializer<Gender>, JsonDeserializer<Gender> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Gender deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsString();
            if (value.equals("M") || value.equals(Prefs.MODE_MANUAL)) {
                return Gender.MALE;
            }
            if (value.equals("F") || value.equals("f")) {
                return Gender.FEMALE;
            }
            return null;
        }

        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(Gender obj, Type type, JsonSerializationContext context) {
            switch (obj) {
                case FEMALE:
                    return new JsonPrimitive("F");
                case MALE:
                    return new JsonPrimitive("M");
                default:
                    return null;
            }
        }
    }
}
