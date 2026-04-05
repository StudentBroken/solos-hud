package com.ua.sdk.page.association;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationAdapter implements JsonSerializer<PageAssociation>, JsonDeserializer<PageAssociation> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public PageAssociation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PageAssociationTransferObject to = (PageAssociationTransferObject) context.deserialize(json, PageAssociationTransferObject.class);
        return PageAssociationTransferObject.toPageAssocaitionImpl(to);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(PageAssociation src, Type typeOfSrc, JsonSerializationContext context) {
        PageAssociationTransferObject to = PageAssociationTransferObject.fromPageAssocaition(src);
        return context.serialize(to, to.getClass());
    }
}
