package com.ua.sdk.activitytimeseries;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesJsonWriter extends AbstractGsonWriter<ActivityTimeSeries> {
    public ActivityTimeSeriesJsonWriter() {
        super(GsonFactory.newActivityTimeSeriesInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(ActivityTimeSeries entity, Gson gson, OutputStreamWriter writer) throws UaException {
        gson.toJson(entity, entity.getClass(), writer);
    }
}
