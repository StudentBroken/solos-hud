package com.ua.sdk.activitytimeseries;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTimeSeriesJsonParser extends AbstractGsonParser<ActivityTimeSeries> {
    public ActivityTimeSeriesJsonParser() {
        super(GsonFactory.newActivityTimeSeriesInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public ActivityTimeSeries read(Gson gson, JsonReader reader) throws UaException {
        return (ActivityTimeSeries) gson.fromJson(reader, ActivityTimeSeriesImpl.class);
    }
}
