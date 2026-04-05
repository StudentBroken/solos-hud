package com.ua.sdk.moderation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ModerationActionWriter extends AbstractGsonWriter<ModerationAction> {
    public ModerationActionWriter() {
        super(GsonFactory.newModerationInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(ModerationAction entity, Gson gson, OutputStreamWriter writer) throws UaException {
        Type type = new TypeToken<ModerationAction>() { // from class: com.ua.sdk.moderation.ModerationActionWriter.1
        }.getType();
        gson.toJson(entity, type, writer);
    }
}
