package com.ua.sdk.sleep;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class SleepMetricListJsonParser extends AbstractGsonParser<SleepMetricList> {
    public SleepMetricListJsonParser() {
        super(GsonFactory.newSleepInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public SleepMetricList read(Gson gson, JsonReader reader) throws UaException {
        return (SleepMetricList) gson.fromJson(reader, SleepMetricList.class);
    }
}
