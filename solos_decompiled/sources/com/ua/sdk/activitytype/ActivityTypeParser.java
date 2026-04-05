package com.ua.sdk.activitytype;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeParser implements JsonParser<ActivityType> {
    private Gson gson;

    public ActivityTypeParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public ActivityType parse(InputStream inputStream) {
        ActivityTypeTransferObject to = (ActivityTypeTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), ActivityTypeTransferObject.class);
        ActivityType activityType = ActivityTypeTransferObject.toImpl(to);
        return activityType;
    }
}
