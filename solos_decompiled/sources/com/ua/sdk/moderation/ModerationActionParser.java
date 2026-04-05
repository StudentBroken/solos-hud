package com.ua.sdk.moderation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class ModerationActionParser extends AbstractGsonParser<ModerationAction> {
    public ModerationActionParser() {
        super(GsonFactory.newModerationInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public ModerationAction read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<ModerationAction>() { // from class: com.ua.sdk.moderation.ModerationActionParser.1
        }.getType();
        return (ModerationAction) gson.fromJson(reader, type);
    }
}
