package com.ua.sdk.remoteconnection;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionTypeJsonParser implements JsonParser<RemoteConnectionType> {
    private Gson gson;

    public RemoteConnectionTypeJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public RemoteConnectionType parse(InputStream inputStream) throws UaException {
        RemoteConnectionTypeTransferObject to = (RemoteConnectionTypeTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), RemoteConnectionTypeTransferObject.class);
        RemoteConnectionTypeImpl rc = RemoteConnectionTypeTransferObject.toRemoteConnectionTypeImpl(to);
        return rc;
    }
}
