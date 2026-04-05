package com.ua.sdk.activitytype;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeListJsonParser extends AbstractGsonParser<ActivityTypeList> {
    public ActivityTypeListJsonParser() {
        super(GsonFactory.newActivityTypeInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public ActivityTypeList read(Gson gson, JsonReader reader) throws UaException {
        return (ActivityTypeList) gson.fromJson(reader, ActivityTypeList.class);
    }
}
