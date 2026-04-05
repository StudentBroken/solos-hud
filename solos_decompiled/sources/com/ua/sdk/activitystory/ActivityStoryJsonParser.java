package com.ua.sdk.activitystory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryJsonParser extends AbstractGsonParser<ActivityStory> {
    public ActivityStoryJsonParser() {
        super(GsonFactory.newActivityStoryInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public ActivityStory read(Gson gson, JsonReader reader) throws UaException {
        return (ActivityStory) gson.fromJson(reader, ActivityStory.class);
    }
}
