package com.ua.sdk.heartrate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class HeartRateZonesListJsonParser extends AbstractGsonParser<HeartRateZonesList> {
    public HeartRateZonesListJsonParser() {
        super(GsonFactory.newHeartRateInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public HeartRateZonesList read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<HeartRateZonesList>() { // from class: com.ua.sdk.heartrate.HeartRateZonesListJsonParser.1
        }.getType();
        return (HeartRateZonesList) gson.fromJson(reader, type);
    }
}
