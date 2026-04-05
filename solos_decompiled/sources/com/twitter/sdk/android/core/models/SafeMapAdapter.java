package com.twitter.sdk.android.core.models;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class SafeMapAdapter implements TypeAdapterFactory {
    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> tokenType) {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, tokenType);
        return new TypeAdapter<T>() { // from class: com.twitter.sdk.android.core.models.SafeMapAdapter.1
            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override // com.google.gson.TypeAdapter
            /* JADX INFO: renamed from: read */
            public T read2(JsonReader jsonReader) throws IOException {
                T t = (T) delegate.read2(jsonReader);
                if (Map.class.isAssignableFrom(tokenType.getRawType())) {
                    if (t == null) {
                        return (T) Collections.EMPTY_MAP;
                    }
                    return (T) Collections.unmodifiableMap((Map) t);
                }
                return t;
            }
        };
    }
}
