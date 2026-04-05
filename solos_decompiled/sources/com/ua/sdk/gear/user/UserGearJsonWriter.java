package com.ua.sdk.gear.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class UserGearJsonWriter extends AbstractGsonWriter<UserGear> {
    public UserGearJsonWriter() {
        super(GsonFactory.newGearInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(UserGear entity, Gson gson, OutputStreamWriter writer) throws UaException {
        Type type = new TypeToken<UserGear>() { // from class: com.ua.sdk.gear.user.UserGearJsonWriter.1
        }.getType();
        gson.toJson(entity, type, writer);
    }
}
