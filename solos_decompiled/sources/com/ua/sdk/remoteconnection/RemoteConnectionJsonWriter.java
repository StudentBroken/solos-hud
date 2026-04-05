package com.ua.sdk.remoteconnection;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.JsonWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class RemoteConnectionJsonWriter implements JsonWriter<RemoteConnection> {
    private Gson gson;

    public RemoteConnectionJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(RemoteConnection rc, OutputStream out) throws UaException {
        RemoteConnectionTransferObject rcto = RemoteConnectionTransferObject.fromRemoteConnection(rc);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(rcto, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush RemoteConnectionJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
