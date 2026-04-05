package com.ua.sdk.internal;

import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.util.Streams;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes65.dex */
public class DebugJsonWriter<T> implements JsonWriter<T> {
    private final JsonWriter<T> writer;

    public DebugJsonWriter(JsonWriter<T> writer) {
        Precondition.isNotNull(writer);
        this.writer = writer;
    }

    @Override // com.ua.sdk.internal.JsonWriter
    public void write(T entity, OutputStream out) throws Throwable {
        String json;
        String json2 = null;
        try {
            try {
                ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
                this.writer.write(entity, tempOut);
                json = new String(tempOut.toByteArray(), "UTF-8");
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            Streams.writeFully(json, out);
            UaLog.debug("request=%s", json);
        } catch (IOException e2) {
            e = e2;
            throw new UaException(e);
        } catch (Throwable th2) {
            th = th2;
            json2 = json;
            UaLog.debug("request=%s", json2);
            throw th;
        }
    }
}
