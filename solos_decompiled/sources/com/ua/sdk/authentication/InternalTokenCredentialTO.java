package com.ua.sdk.authentication;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class InternalTokenCredentialTO {

    @SerializedName("secret")
    String secret;

    @SerializedName("token")
    String token;

    @SerializedName("token_type")
    String tokenType;

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static InternalTokenCredentialTO toTransferObject(InternalTokenCredential from) {
        InternalTokenCredentialTO answer = new InternalTokenCredentialTO();
        answer.setTokenType(from.getTokenType());
        answer.setToken(from.getToken());
        answer.setSecret(from.getSecret());
        return answer;
    }
}
