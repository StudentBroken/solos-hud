package com.twitter.sdk.android.core.internal.oauth;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApi;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.UrlUtils;
import javax.net.ssl.SSLSocketFactory;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/* JADX INFO: loaded from: classes62.dex */
public class OAuth2Service extends OAuthService {
    OAuth2Api api;

    interface OAuth2Api {
        @POST("/oauth2/token")
        @Headers({"Content-Type: application/x-www-form-urlencoded;charset=UTF-8"})
        @FormUrlEncoded
        void getAppAuthToken(@Header("Authorization") String str, @Field(OAuthConstants.PARAM_GRANT_TYPE) String str2, Callback<AppAuthToken> callback);

        @POST("/1.1/guest/activate.json")
        void getGuestToken(@Header("Authorization") String str, Callback<GuestTokenResponse> callback);
    }

    public OAuth2Service(TwitterCore twitterCore, SSLSocketFactory sslSocketFactory, TwitterApi api) {
        super(twitterCore, sslSocketFactory, api);
        this.api = (OAuth2Api) getApiAdapter().create(OAuth2Api.class);
    }

    public void requestGuestAuthToken(final Callback<OAuth2Token> callback) {
        Callback<AppAuthToken> appAuthCallback = new Callback<AppAuthToken>() { // from class: com.twitter.sdk.android.core.internal.oauth.OAuth2Service.1
            @Override // com.twitter.sdk.android.core.Callback
            public void success(Result<AppAuthToken> result) {
                final OAuth2Token appAuthToken = result.data;
                Callback<GuestTokenResponse> guestTokenCallback = new Callback<GuestTokenResponse>() { // from class: com.twitter.sdk.android.core.internal.oauth.OAuth2Service.1.1
                    @Override // com.twitter.sdk.android.core.Callback
                    public void success(Result<GuestTokenResponse> result2) {
                        GuestAuthToken guestAuthToken = new GuestAuthToken(appAuthToken.getTokenType(), appAuthToken.getAccessToken(), result2.data.guestToken);
                        callback.success(new Result(guestAuthToken, null));
                    }

                    @Override // com.twitter.sdk.android.core.Callback
                    public void failure(TwitterException error) {
                        Fabric.getLogger().e(TwitterCore.TAG, "Your app may not allow guest auth. Please talk to us regarding upgrading your consumer key.", error);
                        callback.failure(error);
                    }
                };
                OAuth2Service.this.requestGuestToken(guestTokenCallback, appAuthToken);
            }

            @Override // com.twitter.sdk.android.core.Callback
            public void failure(TwitterException error) {
                Fabric.getLogger().e(TwitterCore.TAG, "Failed to get app auth token", error);
                if (callback != null) {
                    callback.failure(error);
                }
            }
        };
        requestAppAuthToken(appAuthCallback);
    }

    public void requestAppAuthToken(Callback<AppAuthToken> callback) {
        this.api.getAppAuthToken(getAuthHeader(), OAuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS, callback);
    }

    public void requestGuestToken(Callback<GuestTokenResponse> callback, OAuth2Token appAuthToken) {
        this.api.getGuestToken(getAuthorizationHeader(appAuthToken), callback);
    }

    public static String getAuthorizationHeader(OAuth2Token token) {
        return "Bearer " + token.getAccessToken();
    }

    private String getAuthHeader() {
        TwitterAuthConfig authConfig = getTwitterCore().getAuthConfig();
        return "Basic " + HttpRequest.Base64.encode(UrlUtils.percentEncode(authConfig.getConsumerKey()) + ":" + UrlUtils.percentEncode(authConfig.getConsumerSecret()));
    }
}
