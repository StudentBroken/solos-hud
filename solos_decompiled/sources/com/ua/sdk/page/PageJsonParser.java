package com.ua.sdk.page;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class PageJsonParser implements JsonParser<Page> {
    private Gson gson;

    public PageJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public Page parse(InputStream inputStream) throws UaException {
        try {
            PageTO to = (PageTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), PageTO.class);
            PageImpl page = PageTO.toPageImpl(to);
            return page;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
