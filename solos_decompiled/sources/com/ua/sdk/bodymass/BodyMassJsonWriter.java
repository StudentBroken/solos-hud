package com.ua.sdk.bodymass;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassJsonWriter extends AbstractGsonWriter<BodyMass> {
    public BodyMassJsonWriter() {
        super(GsonFactory.newBodyMassInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(BodyMass entity, Gson gson, OutputStreamWriter writer) throws UaException {
        gson.toJson(entity, writer);
    }
}
