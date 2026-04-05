package com.ua.sdk.activitystory;

import com.google.gson.Gson;
import com.ua.sdk.internal.AbstractGsonWriter;
import com.ua.sdk.net.json.GsonFactory;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryJsonWriter extends AbstractGsonWriter<ActivityStory> {
    public ActivityStoryJsonWriter() {
        super(GsonFactory.newActivityStoryInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractGsonWriter
    public void write(ActivityStory entity, Gson gson, OutputStreamWriter writer) {
        gson.toJson(entity, writer);
    }
}
