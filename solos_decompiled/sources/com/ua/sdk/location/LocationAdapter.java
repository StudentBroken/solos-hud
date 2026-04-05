package com.ua.sdk.location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            return null;
        }
        JsonObject jsonObject = json.getAsJsonObject();
        String country = getString(jsonObject, "country");
        String region = getString(jsonObject, "region");
        String locality = getString(jsonObject, "locality");
        String address = getString(jsonObject, "address");
        return new LocationImpl(country, region, locality, address);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("country", src.getCountry());
        jsonObject.addProperty("region", src.getRegion());
        jsonObject.addProperty("locality", src.getLocality());
        jsonObject.addProperty("address", src.getAddress());
        return jsonObject;
    }

    private String getString(JsonObject json, String key) {
        JsonElement element = json.get(key);
        if (element != null) {
            return element.getAsString();
        }
        return null;
    }
}
