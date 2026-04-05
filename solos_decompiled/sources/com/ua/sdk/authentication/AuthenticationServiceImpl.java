package com.ua.sdk.authentication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.digits.sdk.vcard.VCardConfig;
import com.ua.sdk.UaException;
import com.ua.sdk.internal.ConnectionFactory;
import com.ua.sdk.internal.JsonParser;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.UrlBuilder;
import com.ua.sdk.util.Streams;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: loaded from: classes65.dex */
public class AuthenticationServiceImpl implements AuthenticationService {
    private final String clientId;
    private final String clientSecret;
    private final ConnectionFactory connFactory;
    private final Context context;
    private final JsonParser<OAuth2Credentials> oauth2Parser;
    private final UrlBuilder urlBuilder;

    public AuthenticationServiceImpl(String clientId, String clientSecret, ConnectionFactory connFactory, UrlBuilder urlBuilder, JsonParser<OAuth2Credentials> oauth2Parser, Context context) {
        this.connFactory = (ConnectionFactory) Precondition.isNotNull(connFactory);
        this.urlBuilder = (UrlBuilder) Precondition.isNotNull(urlBuilder);
        this.oauth2Parser = (JsonParser) Precondition.isNotNull(oauth2Parser);
        this.clientId = (String) Precondition.isNotNull(clientId);
        this.clientSecret = (String) Precondition.isNotNull(clientSecret);
        this.context = (Context) Precondition.isNotNull(context);
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public void requestUserAuthorization(String redirectUri) {
        Uri uri = Uri.parse(this.urlBuilder.buildOAuth2AuthorizationUrl(this.clientId, redirectUri).toString());
        Intent i = new Intent("android.intent.action.VIEW", uri);
        i.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        this.context.startActivity(i);
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public String getUserAuthorizationUrl(String redirectUri) {
        return this.urlBuilder.buildOAuth2AuthorizationUrl(this.clientId, redirectUri).toString();
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public OAuth2Credentials authenticateUser(CharSequence username, CharSequence password) throws UaException {
        try {
            StringBuilder formParams = new StringBuilder();
            formParams.append("grant_type=password");
            formParams.append("&client_id=").append(this.clientId);
            formParams.append("&client_secret=").append(this.clientSecret);
            formParams.append("&username=").append(URLEncoder.encode(username.toString(), "UTF-8"));
            formParams.append("&password=").append(URLEncoder.encode(password.toString(), "UTF-8"));
            return requestToken(formParams);
        } catch (UnsupportedEncodingException e) {
            throw new UaException();
        }
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public OAuth2Credentials authenticateUser(String authorizationCode) throws UaException {
        StringBuilder formParams = new StringBuilder();
        formParams.append("grant_type=authorization_code");
        formParams.append("&client_id=").append(this.clientId);
        formParams.append("&client_secret=").append(this.clientSecret);
        formParams.append("&code=").append(authorizationCode);
        return requestToken(formParams);
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public OAuth2Credentials authenticateClient() throws UaException {
        StringBuilder formParams = new StringBuilder();
        formParams.append("grant_type=client_credentials");
        formParams.append("&client_id=").append(this.clientId);
        formParams.append("&client_secret=").append(this.clientSecret);
        return requestToken(formParams);
    }

    @Override // com.ua.sdk.authentication.AuthenticationService
    public OAuth2Credentials refreshAuthentication(OAuth2Credentials token) throws UaException {
        StringBuilder formParams = new StringBuilder();
        formParams.append("grant_type=refresh_token");
        formParams.append("&client_id=").append(this.clientId);
        formParams.append("&client_secret=").append(this.clientSecret);
        formParams.append("&refresh_token=").append(token.getRefreshToken());
        return requestToken(formParams);
    }

    private OAuth2Credentials requestToken(CharSequence formData) throws UaException {
        try {
            URL url = this.urlBuilder.buildGetAuthenticationToken();
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", HttpRequest.CONTENT_TYPE_FORM);
                Streams.writeFully(formData, conn.getOutputStream());
                Precondition.isResponseHttpOk(conn);
                OAuth2Credentials token = this.oauth2Parser.parse(conn.getInputStream());
                return token;
            } finally {
                conn.disconnect();
            }
        } catch (UaException e) {
            throw e;
        } catch (InterruptedIOException e2) {
            throw new UaException(UaException.Code.CANCELED, e2);
        } catch (Throwable e3) {
            throw new UaException(e3);
        }
    }
}
