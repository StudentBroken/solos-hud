package com.ua.sdk.route;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.JsonWriter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class RouteJsonWriter implements JsonWriter<Route> {
    private Gson gson;

    public RouteJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(Route entity, OutputStream out) throws UaException {
        RouteTO to = RouteTO.toTransferObject(entity);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(to, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush RouteJsonWriter during write.");
            throw new UaException(e2);
        }
    }
}
