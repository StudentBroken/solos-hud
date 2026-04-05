package com.ua.sdk.internal;

import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.util.Streams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes65.dex */
public class DebugJsonParser<T> implements JsonParser<T> {
    private final JsonParser<T> parser;

    public DebugJsonParser(JsonParser<T> parser) {
        Precondition.isNotNull(parser);
        this.parser = parser;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public T parse(InputStream inputStream) throws UaException {
        String json = null;
        try {
            try {
                json = Streams.readFully(inputStream);
                return this.parser.parse(new ByteArrayInputStream(json.getBytes("UTF-8")));
            } catch (IOException e) {
                throw new UaException(e);
            }
        } finally {
            UaLog.debug("response=%s", json);
        }
    }
}
