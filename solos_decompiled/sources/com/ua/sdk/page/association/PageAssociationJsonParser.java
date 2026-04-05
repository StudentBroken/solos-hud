package com.ua.sdk.page.association;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationJsonParser extends AbstractGsonParser<PageAssociation> {
    public PageAssociationJsonParser() {
        super(GsonFactory.newPageAssociationInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public PageAssociation read(Gson gson, JsonReader reader) throws UaException {
        return (PageAssociation) gson.fromJson(reader, PageAssociationImpl.class);
    }
}
