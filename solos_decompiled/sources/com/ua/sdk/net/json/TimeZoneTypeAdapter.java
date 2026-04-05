package com.ua.sdk.net.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class TimeZoneTypeAdapter extends TypeAdapter<TimeZone> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, TimeZone value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getID());
        }
    }

    @Override // com.google.gson.TypeAdapter
    /* JADX INFO: renamed from: read, reason: avoid collision after fix types in other method */
    public TimeZone read2(JsonReader in) throws IOException {
        String id = in.nextString();
        if (id == null) {
            return null;
        }
        return TimeZone.getTimeZone(id);
    }
}
