package com.ua.sdk.friendship;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.EntityList;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipPageJsonParser implements JsonParser<EntityList<Friendship>> {
    private Gson gson;

    public FriendshipPageJsonParser(Gson gson) {
        this.gson = gson;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.internal.JsonParser
    public EntityList<Friendship> parse(InputStream inputStream) throws UaException {
        try {
            FriendshipPageTransferObject to = (FriendshipPageTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), FriendshipPageTransferObject.class);
            FriendshipListImpl page = FriendshipPageTransferObject.fromTransferObject(to);
            return page;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
