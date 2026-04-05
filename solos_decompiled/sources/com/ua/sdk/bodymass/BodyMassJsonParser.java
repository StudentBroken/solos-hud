package com.ua.sdk.bodymass;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassJsonParser extends AbstractGsonParser<BodyMass> {
    public BodyMassJsonParser() {
        super(GsonFactory.newBodyMassInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public BodyMass read(Gson gson, JsonReader reader) throws UaException {
        return (BodyMass) gson.fromJson(reader, BodyMass.class);
    }
}
