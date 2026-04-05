package com.ua.sdk.recorder;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class DataSourceConfigurationListParser extends AbstractGsonParser<DataSourceConfigurationList> {
    public DataSourceConfigurationListParser() {
        super(GsonFactory.newRecorderConfigurationInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public DataSourceConfigurationList read(Gson gson, JsonReader reader) throws UaException {
        return (DataSourceConfigurationList) gson.fromJson(reader, DataSourceConfigurationList.class);
    }
}
