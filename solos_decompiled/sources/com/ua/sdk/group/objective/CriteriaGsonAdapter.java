package com.ua.sdk.group.objective;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class CriteriaGsonAdapter implements JsonDeserializer<Criteria>, JsonSerializer<Criteria> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Criteria deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CriteriaItem item;
        JsonObject jsonObject = json.getAsJsonObject();
        Criteria criteria = new CriteriaImpl();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String name = entry.getKey();
            if (CriteriaTypes.ACTIVITY_TYPE.equals(name)) {
                item = new ActivityTypeCriteriaItem();
                item.setValue(Integer.valueOf(entry.getValue().getAsInt()));
            } else if (CriteriaTypes.SORT.equals(name)) {
                item = new SortCriteriaItem();
                item.setValue(entry.getValue().getAsString());
            } else {
                item = new CriteriaItemImpl();
                ((CriteriaItemImpl) item).setValue(entry.getValue().toString());
                ((CriteriaItemImpl) item).name = name;
                item.setValue(entry.getValue().toString());
            }
            criteria.addCriteriaItem(item);
        }
        return criteria;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Criteria src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for (String key : ((CriteriaImpl) src).criteria.keySet()) {
            if (CriteriaTypes.ACTIVITY_TYPE.equals(key)) {
                json.add(key, new JsonPrimitive((Number) ((CriteriaImpl) src).criteria.get(key).getValue()));
            } else if (CriteriaTypes.SORT.equals(key)) {
                json.add(key, new JsonPrimitive((String) ((CriteriaImpl) src).criteria.get(key).getValue()));
            } else {
                json.add(key, new JsonPrimitive((String) ((CriteriaImpl) src).criteria.get(key).getValue()));
            }
        }
        return json;
    }
}
