package com.digits.sdk.android;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.AuthTokenAdapter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.persistence.SerializationStrategy;
import java.util.List;
import retrofit.client.Header;

/* JADX INFO: loaded from: classes18.dex */
public class DigitsSession extends Session<AuthToken> {
    public static final Email DEFAULT_EMAIL = new Email("", false);
    public static final String DEFAULT_PHONE_NUMBER = "";
    public static final long LOGGED_OUT_USER_ID = 0;
    static final String SECRET_HEADER = "x-twitter-new-account-oauth-secret";
    static final String TOKEN_HEADER = "x-twitter-new-account-oauth-access-token";
    public static final long UNKNOWN_USER_ID = -1;

    @SerializedName("email")
    private final Email email;

    @SerializedName(DigitsClient.EXTRA_PHONE)
    private final String phoneNumber;

    public DigitsSession(AuthToken authToken, long id) {
        this(authToken, id, "", DEFAULT_EMAIL);
    }

    public DigitsSession(AuthToken authToken, long id, String phoneNumber, Email email) {
        super(authToken, id);
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public DigitsSession(OAuth2Token token) {
        this(token, 0L, "", DEFAULT_EMAIL);
    }

    public boolean isLoggedOutUser() {
        return getId() == 0;
    }

    public boolean isValidUser() {
        return isValidUserId(getId()) && isValidUserToken(getAuthToken());
    }

    private boolean isValidUserId(long id) {
        return (isLoggedOutUser() || id == -1) ? false : true;
    }

    private boolean isValidUserToken(AuthToken token) {
        return (!(token instanceof TwitterAuthToken) || ((TwitterAuthToken) token).secret == null || ((TwitterAuthToken) token).token == null) ? false : true;
    }

    static DigitsSession create(Result<DigitsUser> result, String phoneNumber) {
        if (result == null) {
            throw new NullPointerException("result must not be null");
        }
        if (result.data == null) {
            throw new NullPointerException("result.data must not be null");
        }
        if (result.response == null) {
            throw new NullPointerException("result.response must not be null");
        }
        if (phoneNumber == null) {
            throw new NullPointerException("phoneNumber must not be null");
        }
        List<Header> headers = result.response.getHeaders();
        String token = "";
        String secret = "";
        for (Header header : headers) {
            if (TOKEN_HEADER.equals(header.getName())) {
                token = header.getValue();
            } else if (SECRET_HEADER.equals(header.getName())) {
                secret = header.getValue();
            }
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(secret)) {
                break;
            }
        }
        return new DigitsSession(new TwitterAuthToken(token, secret), result.data.id, phoneNumber, DEFAULT_EMAIL);
    }

    static DigitsSession create(DigitsSessionResponse result, String phoneNumber) {
        if (result == null) {
            throw new NullPointerException("result must not be null");
        }
        if (phoneNumber == null) {
            throw new NullPointerException("phoneNumber must not be null");
        }
        return new DigitsSession(new TwitterAuthToken(result.token, result.secret), result.userId, phoneNumber, DEFAULT_EMAIL);
    }

    public static DigitsSession create(VerifyAccountResponse verifyAccountResponse) {
        if (verifyAccountResponse == null) {
            throw new NullPointerException("verifyAccountResponse must not be null");
        }
        return new DigitsSession(verifyAccountResponse.token, verifyAccountResponse.userId, verifyAccountResponse.phoneNumber, verifyAccountResponse.email != null ? verifyAccountResponse.email : DEFAULT_EMAIL);
    }

    public Email getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public static class Serializer implements SerializationStrategy<DigitsSession> {
        private static final String TAG = "Digits";
        private final Gson gson = new GsonBuilder().registerTypeAdapter(AuthToken.class, new AuthTokenAdapter()).create();

        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public String serialize(DigitsSession session) {
            if (session != null && session.getAuthToken() != null) {
                try {
                    return this.gson.toJson(session);
                } catch (Exception e) {
                    Fabric.getLogger().d("Digits", e.getMessage());
                }
            }
            return "";
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // io.fabric.sdk.android.services.persistence.SerializationStrategy
        public DigitsSession deserialize(String serializedSession) {
            if (!TextUtils.isEmpty(serializedSession)) {
                try {
                    DigitsSession deserializeSession = (DigitsSession) this.gson.fromJson(serializedSession, DigitsSession.class);
                    return new DigitsSession(deserializeSession.getAuthToken(), deserializeSession.getId(), deserializeSession.phoneNumber == null ? "" : deserializeSession.phoneNumber, deserializeSession.email == null ? DigitsSession.DEFAULT_EMAIL : deserializeSession.email);
                } catch (Exception e) {
                    Fabric.getLogger().d("Digits", e.getMessage());
                }
            }
            return null;
        }
    }

    @Override // com.twitter.sdk.android.core.Session
    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }
        DigitsSession session = (DigitsSession) o;
        if (this.phoneNumber != null) {
            if (!this.phoneNumber.equals(session.phoneNumber)) {
                return false;
            }
        } else if (session.phoneNumber != null) {
            return false;
        }
        if (this.email == null ? session.email != null : !this.email.equals(session.email)) {
            z = false;
        }
        return z;
    }

    @Override // com.twitter.sdk.android.core.Session
    public int hashCode() {
        int result = super.hashCode();
        return (((result * 31) + (this.phoneNumber != null ? this.phoneNumber.hashCode() : 0)) * 31) + (this.email != null ? this.email.hashCode() : 0);
    }
}
