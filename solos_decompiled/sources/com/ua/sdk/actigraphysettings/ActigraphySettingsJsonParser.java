package com.ua.sdk.actigraphysettings;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphySettingsJsonParser implements JsonParser<ActigraphySettings> {
    private Gson gson;

    public ActigraphySettingsJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public ActigraphySettings parse(InputStream inputStream) throws UaException {
        ActigraphySettingsTO to = (ActigraphySettingsTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), ActigraphySettingsTO.class);
        ActigraphySettingsImpl actigraphySettings = ActigraphySettingsTO.fromTransferObject(to);
        return actigraphySettings;
    }
}
