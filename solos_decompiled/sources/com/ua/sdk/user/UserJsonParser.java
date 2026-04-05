package com.ua.sdk.user;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class UserJsonParser implements JsonParser<User> {
    private Gson gson;

    public UserJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public User parse(InputStream inputStream) throws UaException {
        try {
            UserTO to = (UserTO) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), UserTO.class);
            UserImpl user = UserTO.fromTransferObject(to);
            return user;
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
