package com.ua.sdk.user.profilephoto;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoJsonParser implements JsonParser<UserProfilePhoto> {
    private Gson gson;

    public UserProfilePhotoJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override // com.ua.sdk.internal.JsonParser
    public UserProfilePhoto parse(InputStream inputStream) throws UaException {
        try {
            UserProfilePhotoTransferObject to = (UserProfilePhotoTransferObject) this.gson.fromJson(new JsonReader(new InputStreamReader(inputStream)), UserProfilePhotoTransferObject.class);
            return UserProfilePhotoTransferObject.toUserProfilePhotoImpl(to);
        } catch (JsonParseException e) {
            throw new UaException(e);
        }
    }
}
