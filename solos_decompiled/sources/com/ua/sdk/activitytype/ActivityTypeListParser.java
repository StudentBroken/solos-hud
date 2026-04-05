package com.ua.sdk.activitytype;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeListParser implements JsonParser<List<ActivityTypeImpl>> {
    private Gson gson;

    public ActivityTypeListParser(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public List<ActivityTypeImpl> parse(InputStream inputStream) {
        ActivityTypeListTransferObject to = (ActivityTypeListTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), ActivityTypeListTransferObject.class);
        List<ActivityTypeImpl> list = ActivityTypeListTransferObject.toImplList(to);
        return list;
    }
}
