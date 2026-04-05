package com.ua.sdk.recorder;

import com.google.gson.Gson;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class DataSourceConfigurationListWriter extends AbstractGsonWriter {
    public DataSourceConfigurationListWriter() {
        super(GsonFactory.newRecorderConfigurationInstance());
    }

    @Override // com.ua.sdk.internal.AbstractGsonWriter
    protected void write(Object entity, Gson gson, OutputStreamWriter writer) throws UaException {
        gson.toJson(entity, writer);
    }
}
