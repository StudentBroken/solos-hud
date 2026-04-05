package com.ua.sdk.internal;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public abstract class AbstractGsonParser<T> implements JsonParser<T> {
    protected Gson mGson;

    protected abstract T read(Gson gson, JsonReader jsonReader) throws UaException;

    public AbstractGsonParser(Gson gson) {
        Precondition.isNotNull(gson);
        this.mGson = gson;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public T parse(InputStream inputStream) throws UaException {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            T entity = read(this.mGson, reader);
            return entity;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
