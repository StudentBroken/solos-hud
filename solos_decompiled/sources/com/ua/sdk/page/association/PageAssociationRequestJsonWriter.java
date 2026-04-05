package com.ua.sdk.page.association;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationRequestJsonWriter extends AbstractGsonWriter<PageAssociation> {
    public PageAssociationRequestJsonWriter() {
        super(GsonFactory.newInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(PageAssociation entity, Gson gson, OutputStreamWriter writer) throws UaException {
        PageAssociationRequestTransferObject transferObject = PageAssociationRequestTransferObject.fromPageAssociation(entity);
        gson.toJson(transferObject, writer);
    }
}
