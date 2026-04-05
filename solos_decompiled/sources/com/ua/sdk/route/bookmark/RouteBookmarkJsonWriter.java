package com.ua.sdk.route.bookmark;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.internal.JsonWriter;
import com.ua.sdk.route.RouteBookmark;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkJsonWriter implements JsonWriter<RouteBookmark> {
    private Gson gson;

    public RouteBookmarkJsonWriter(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(RouteBookmark entity, OutputStream out) throws UaException {
        RouteBookmarkTO to = RouteBookmarkTO.toTransferObject(entity);
        OutputStreamWriter writer = new OutputStreamWriter(out);
        this.gson.toJson(to, writer);
        try {
            writer.flush();
        } catch (InterruptedIOException e) {
            throw new UaException(UaException.Code.CANCELED);
        } catch (IOException e2) {
            UaLog.error("Unable to flush json writer during write.", (Throwable) e2);
            throw new UaException(e2);
        }
    }
}
