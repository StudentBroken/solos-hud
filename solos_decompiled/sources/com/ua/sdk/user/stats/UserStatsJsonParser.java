package com.ua.sdk.user.stats;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class UserStatsJsonParser extends AbstractGsonParser<UserStats> {
    public UserStatsJsonParser() {
        super(GsonFactory.newUserStatsInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public UserStats read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<UserStats>() { // from class: com.ua.sdk.user.stats.UserStatsJsonParser.1
        }.getType();
        return (UserStats) gson.fromJson(reader, type);
    }
}
