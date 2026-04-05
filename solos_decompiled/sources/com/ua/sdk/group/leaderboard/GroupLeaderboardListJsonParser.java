package com.ua.sdk.group.leaderboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import com.ua.sdk.net.json.GsonFactory;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class GroupLeaderboardListJsonParser extends AbstractGsonParser<GroupLeaderboardList> {
    public GroupLeaderboardListJsonParser() {
        super(GsonFactory.newGroupInstance());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public GroupLeaderboardList read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<GroupLeaderboardList>() { // from class: com.ua.sdk.group.leaderboard.GroupLeaderboardListJsonParser.1
        }.getType();
        return (GroupLeaderboardList) gson.fromJson(reader, type);
    }
}
