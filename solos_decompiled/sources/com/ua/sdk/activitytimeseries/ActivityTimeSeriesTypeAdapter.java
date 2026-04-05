package com.ua.sdk.activitytimeseries;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ua.sdk.activitytimeseries.ActivityTimeSeriesImpl;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.util.DoubleList;
import com.ua.sdk.util.IntList;
import com.ua.sdk.util.LongList;
import java.io.IOException;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesTypeAdapter extends TypeAdapter<ActivityTimeSeriesImpl.TimeSeries> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, ActivityTimeSeriesImpl.TimeSeries value) throws IOException {
        if (value != null) {
            out.beginObject();
            long[] epochs = value.stepEpochs;
            int[] values = value.stepValues;
            writeIntValues(out, BaseDataTypes.ID_STEPS, epochs, values);
            long[] epochs2 = value.distanceEpochs;
            double[] values2 = value.distanceValues;
            writeDoubleValues(out, BaseDataTypes.ID_DISTANCE, epochs2, values2);
            long[] epochs3 = value.calorieEpochs;
            double[] values3 = value.calorieValues;
            writeDoubleValues(out, "calories", epochs3, values3);
            out.endObject();
            return;
        }
        out.nullValue();
    }

    private void writeIntValues(JsonWriter out, String type, long[] epochs, int[] values) throws IOException {
        if (epochs != null) {
            out.name(type);
            out.beginObject();
            out.name("values");
            out.beginArray();
            int size = epochs.length;
            for (int i = 0; i < size; i++) {
                out.beginArray();
                out.value(epochs[i]);
                out.value(values[i]);
                out.endArray();
            }
            out.endArray();
            out.endObject();
        }
    }

    private void writeDoubleValues(JsonWriter out, String type, long[] epochs, double[] values) throws IOException {
        if (epochs != null) {
            out.name(type);
            out.beginObject();
            out.name("values");
            out.beginArray();
            int size = epochs.length;
            for (int i = 0; i < size; i++) {
                out.beginArray();
                out.value(epochs[i]);
                out.value(values[i]);
                out.endArray();
            }
            out.endArray();
            out.endObject();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    /* JADX INFO: renamed from: read */
    public ActivityTimeSeriesImpl.TimeSeries read2(JsonReader in) throws IOException {
        ActivityTimeSeriesImpl.TimeSeries timeSeries = new ActivityTimeSeriesImpl.TimeSeries();
        LongList epochs = null;
        IntList intValues = null;
        DoubleList doubleValues = null;
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (BaseDataTypes.ID_STEPS.equals(name)) {
                if (epochs == null) {
                    epochs = new LongList();
                } else {
                    epochs.clear();
                }
                if (intValues == null) {
                    intValues = new IntList();
                } else {
                    intValues.clear();
                }
                readIntValues(in, epochs, intValues);
                timeSeries.stepEpochs = epochs.toArray();
                timeSeries.stepValues = intValues.toArray();
            } else if (BaseDataTypes.ID_DISTANCE.equals(name)) {
                if (epochs == null) {
                    epochs = new LongList();
                } else {
                    epochs.clear();
                }
                if (doubleValues == null) {
                    doubleValues = new DoubleList();
                } else {
                    doubleValues.clear();
                }
                readDoubleValues(in, epochs, doubleValues);
                timeSeries.distanceEpochs = epochs.toArray();
                timeSeries.distanceValues = doubleValues.toArray();
            } else if ("calories".equals(name)) {
                if (epochs == null) {
                    epochs = new LongList();
                } else {
                    epochs.clear();
                }
                if (doubleValues == null) {
                    doubleValues = new DoubleList();
                } else {
                    doubleValues.clear();
                }
                readDoubleValues(in, epochs, doubleValues);
                timeSeries.calorieEpochs = epochs.toArray();
                timeSeries.calorieValues = doubleValues.toArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
        return timeSeries;
    }

    private void readIntValues(JsonReader in, LongList epochs, IntList values) throws IOException {
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("values".equals(name)) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    long epoch = in.nextLong();
                    int value = in.nextInt();
                    epochs.add(epoch);
                    values.add(value);
                    in.endArray();
                }
                in.endArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
    }

    private void readDoubleValues(JsonReader in, LongList epochs, DoubleList values) throws IOException {
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if ("values".equals(name)) {
                in.beginArray();
                while (in.hasNext()) {
                    in.beginArray();
                    long epoch = in.nextLong();
                    double value = in.nextDouble();
                    epochs.add(epoch);
                    values.add(value);
                    in.endArray();
                }
                in.endArray();
            } else {
                in.skipValue();
            }
        }
        in.endObject();
    }
}
