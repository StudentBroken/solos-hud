package com.ua.sdk.actigraphysettings;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphySettingsRequestWriter extends AbstractGsonWriter<ActigraphySettings> {
    public ActigraphySettingsRequestWriter() {
        super(GsonFactory.newInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(ActigraphySettings entity, Gson gson, OutputStreamWriter writer) throws UaException {
        ActigraphySettingsRequestTO transferObject = ActigraphySettingsRequestTO.toTransferObject(entity);
        gson.toJson(transferObject, writer);
    }
}
