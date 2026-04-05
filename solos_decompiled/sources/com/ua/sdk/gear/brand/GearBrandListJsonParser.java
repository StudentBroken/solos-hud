package com.ua.sdk.gear.brand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandListJsonParser extends AbstractGsonParser<EntityList<GearBrand>> {
    public GearBrandListJsonParser() {
        super(GsonFactory.newGearInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public EntityList<GearBrand> read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<GearBrandList>() { // from class: com.ua.sdk.gear.brand.GearBrandListJsonParser.1
        }.getType();
        return (EntityList) gson.fromJson(reader, type);
    }
}
