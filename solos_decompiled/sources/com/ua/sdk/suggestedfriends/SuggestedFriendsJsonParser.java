package com.ua.sdk.suggestedfriends;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.AbstractGsonParser;

/* JADX INFO: loaded from: classes65.dex */
public class SuggestedFriendsJsonParser extends AbstractGsonParser<SuggestedFriends> {
    public SuggestedFriendsJsonParser(Gson gson) {
        super(gson);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.AbstractGsonParser
    public SuggestedFriends read(Gson gson, JsonReader reader) throws UaException {
        return (SuggestedFriends) gson.fromJson(reader, SuggestedFriendsImpl.class);
    }
}
