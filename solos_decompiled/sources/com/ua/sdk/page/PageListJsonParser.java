package com.ua.sdk.page;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class PageListJsonParser implements JsonParser<EntityList<Page>> {
    private Gson gson;

    public PageListJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<Page> parse(InputStream inputStream) throws UaException {
        try {
            PagePageTO to = (PagePageTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), PagePageTO.class);
            return PagePageTO.toPage(to);
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
