package com.ua.sdk.workout;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutJsonParser extends AbstractGsonParser<Workout> {
    public WorkoutJsonParser() {
        super(GsonFactory.newWorkoutInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public Workout read(Gson gson, JsonReader reader) throws UaException {
        return (Workout) gson.fromJson(reader, WorkoutImpl.class);
    }
}
