package com.ua.sdk.actigraphy;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyJsonParser implements JsonParser<EntityList<Actigraphy>> {
    private Gson gson;

    public ActigraphyJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<Actigraphy> parse(InputStream inputStream) throws UaException {
        ActigraphyTransferObject to = (ActigraphyTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), ActigraphyTransferObject.class);
        return ActigraphyTransferObject.toList(to);
    }
}
