package com.ua.sdk.gear.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearJsonParser extends AbstractGsonParser<UserGear> {
    public UserGearJsonParser() {
        super(GsonFactory.newGearInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public UserGear read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<UserGear>() { // from class: com.ua.sdk.gear.user.UserGearJsonParser.1
        }.getType();
        return (UserGear) gson.fromJson(reader, type);
    }
}
