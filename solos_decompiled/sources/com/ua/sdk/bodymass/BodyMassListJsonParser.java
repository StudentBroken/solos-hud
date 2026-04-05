package com.ua.sdk.bodymass;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassListJsonParser extends AbstractGsonParser<BodyMassList> {
    public BodyMassListJsonParser() {
        super(GsonFactory.newBodyMassInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public BodyMassList read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<BodyMassList>() { // from class: com.ua.sdk.bodymass.BodyMassListJsonParser.1
        }.getType();
        return (BodyMassList) gson.fromJson(reader, type);
    }
}
