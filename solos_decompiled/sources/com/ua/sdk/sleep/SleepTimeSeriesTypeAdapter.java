package com.ua.sdk.sleep;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.ua.sdk.sleep.SleepMetric;
import com.ua.sdk.sleep.SleepMetricImpl;
import java.io.IOException;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes65.dex */
public class SleepTimeSeriesTypeAdapter extends TypeAdapter<SleepMetricImpl.TimeSeries> {
    public static final int BUFFER_SIZE = 256;

    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, SleepMetricImpl.TimeSeries value) throws IOException {
        if (value != null) {
            out.beginObject();
            out.name("sleep");
            out.beginObject();
            out.name("values");
            out.beginArray();
            ArrayList<SleepStateEntry> events = value.events;
            int size = events.size();
            for (int i = 0; i < size; i++) {
                SleepStateEntry entry = events.get(i);
                out.beginArray();
                out.value(entry.epoch);
                out.value(entry.state.value);
                out.endArray();
            }
            out.endArray();
            out.endObject();
            out.endObject();
            return;
        }
        out.nullValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    /* JADX INFO: renamed from: read */
    public SleepMetricImpl.TimeSeries read2(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.skipValue();
            return null;
        }
        SleepMetricImpl.TimeSeries timeSeries = new SleepMetricImpl.TimeSeries();
        ArrayList<SleepStateEntry> events = timeSeries.events;
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("sleep".equals(name)) {
                in.beginObject();
                while (in.hasNext()) {
                    String name2 = in.nextName();
                    if ("values".equals(name2)) {
                        in.beginArray();
                        while (in.hasNext()) {
                            in.beginArray();
                            long epoch = in.nextLong();
                            int stateVal = in.nextInt();
                            SleepMetric.State state = SleepMetric.State.getState(stateVal);
                            events.add(new SleepStateEntry(epoch, state));
                            in.endArray();
                        }
                        in.endArray();
                    } else {
                        in.skipValue();
                    }
                }
                in.endObject();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return timeSeries;
    }
}
