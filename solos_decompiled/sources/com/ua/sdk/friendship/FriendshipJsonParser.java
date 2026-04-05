package com.ua.sdk.friendship;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipJsonParser implements JsonParser<Friendship> {
    private Gson gson;

    public FriendshipJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public Friendship parse(InputStream inputStream) throws UaException {
        try {
            FriendshipTransferObject to = (FriendshipTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), FriendshipTransferObject.class);
            FriendshipImpl friendship = FriendshipTransferObject.toFriendship(to);
            return friendship;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
