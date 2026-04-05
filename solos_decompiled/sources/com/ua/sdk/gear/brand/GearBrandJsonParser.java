package com.ua.sdk.gear.brand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GearBrandJsonParser extends AbstractGsonParser<GearBrand> {
    public GearBrandJsonParser() {
        super(GsonFactory.newGearInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public GearBrand read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<GearBrand>() { // from class: com.ua.sdk.gear.brand.GearBrandJsonParser.1
        }.getType();
        return (GearBrand) gson.fromJson(reader, type);
    }
}
