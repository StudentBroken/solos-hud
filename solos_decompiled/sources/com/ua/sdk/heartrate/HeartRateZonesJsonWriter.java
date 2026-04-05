package com.ua.sdk.heartrate;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZonesJsonWriter extends AbstractGsonWriter<HeartRateZones> {
    public HeartRateZonesJsonWriter() {
        super(GsonFactory.newHeartRateInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(HeartRateZones entity, Gson gson, OutputStreamWriter writer) throws UaException {
        gson.toJson(entity, writer);
    }
}
