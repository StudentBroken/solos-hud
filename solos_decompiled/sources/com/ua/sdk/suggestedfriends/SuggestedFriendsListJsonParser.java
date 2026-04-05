package com.ua.sdk.suggestedfriends;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsListJsonParser extends AbstractGsonParser<SuggestedFriendsListImpl> {
    public SuggestedFriendsListJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public SuggestedFriendsListImpl read(Gson gson, JsonReader reader) throws UaException {
        Type type = new TypeToken<SuggestedFriendsListImpl>() { // from class: com.ua.sdk.suggestedfriends.SuggestedFriendsListJsonParser.1
        }.getType();
        return (SuggestedFriendsListImpl) gson.fromJson(reader, type);
    }
}
