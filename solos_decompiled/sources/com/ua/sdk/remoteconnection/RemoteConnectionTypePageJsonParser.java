package com.ua.sdk.remoteconnection;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypePageJsonParser implements JsonParser<EntityList<RemoteConnectionType>> {
    private final Gson gson;

    public RemoteConnectionTypePageJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<RemoteConnectionType> parse(InputStream inputStream) throws UaException {
        try {
            RemoteConnectionTypePageTO to = (RemoteConnectionTypePageTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), RemoteConnectionTypePageTO.class);
            return RemoteConnectionTypePageTO.fromTransferObject(to);
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
