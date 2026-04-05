package com.twitter.sdk.android.core;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.twitter.sdk.android.core.internal.oauth.AppAuthToken;
import com.twitter.sdk.android.core.internal.oauth.GuestAuthToken;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes62.dex */
public class AuthTokenAdapter implements JsonSerializer<AuthToken>, JsonDeserializer<AuthToken> {
    private static final String AUTH_TOKEN = "auth_token";
    private static final String AUTH_TYPE = "auth_type";
    static final Map<String, Class<? extends AuthToken>> authTypeRegistry = new HashMap();
    private final Gson gson = new Gson();

    static {
        authTypeRegistry.put("oauth1a", TwitterAuthToken.class);
        authTypeRegistry.put("oauth2", OAuth2Token.class);
        authTypeRegistry.put("guest", GuestAuthToken.class);
        authTypeRegistry.put(SettingsJsonConstants.APP_KEY, AppAuthToken.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(AuthToken src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auth_type", getAuthTypeString(src.getClass()));
        jsonObject.add(AUTH_TOKEN, this.gson.toJsonTree(src));
        return jsonObject;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public AuthToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive jsonAuthType = jsonObject.getAsJsonPrimitive("auth_type");
        String authType = jsonAuthType.getAsString();
        JsonElement jsonAuthToken = jsonObject.get(AUTH_TOKEN);
        return (AuthToken) this.gson.fromJson(jsonAuthToken, (Class) authTypeRegistry.get(authType));
    }

    static String getAuthTypeString(Class<? extends AuthToken> authTokenClass) {
        for (Map.Entry<String, Class<? extends AuthToken>> entry : authTypeRegistry.entrySet()) {
            if (entry.getValue().equals(authTokenClass)) {
                return entry.getKey();
            }
        }
        return "";
    }
}
