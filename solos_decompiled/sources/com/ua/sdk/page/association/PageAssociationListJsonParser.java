package com.ua.sdk.page.association;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationListJsonParser extends AbstractGsonParser<PageAssociationList> {
    public PageAssociationListJsonParser() {
        super(GsonFactory.newPageAssociationInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public PageAssociationList read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<PageAssociationList>() { // from class: com.ua.sdk.page.association.PageAssociationListJsonParser.1
        }.getType();
        return (PageAssociationList) gson.fromJson(reader, type);
    }
}
