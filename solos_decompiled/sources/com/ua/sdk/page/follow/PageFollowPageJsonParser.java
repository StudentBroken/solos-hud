package com.ua.sdk.page.follow;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowPageJsonParser implements JsonParser<EntityList<PageFollow>> {
    private Gson gson;

    public PageFollowPageJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<PageFollow> parse(InputStream inputStream) throws UaException {
        try {
            PageFollowPageTransferObject to = (PageFollowPageTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), PageFollowPageTransferObject.class);
            PageFollowsListImpl page = PageFollowPageTransferObject.toPage(to);
            return page;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
