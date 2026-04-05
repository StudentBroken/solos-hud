package com.ua.sdk.page.follow;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowJsonParser extends AbstractGsonParser<PageFollow> {
    public PageFollowJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public PageFollow read(Gson gson, JsonReader reader) throws UaException {
        return null;
    }

    @Override // com.ua.sdk.internal.AbstractGsonParser, com.ua.sdk.internal.JsonParser
    public PageFollow parse(InputStream inputStream) throws UaException {
        try {
            PageFollowTransferObject to = (PageFollowTransferObject) this.mGson.fromJson(new JsonReader(new InputStreamReader(inputStream)), PageFollowTransferObject.class);
            PageFollowImpl pageFollow = PageFollowTransferObject.toPageFollowImpl(to);
            return pageFollow;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
