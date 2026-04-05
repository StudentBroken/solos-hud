package com.digits.sdk.android;

import android.os.Build;
import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import javax.net.ssl.SSLSocketFactory;
import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes18.dex */
class DigitsApiClient {
    private final RestAdapter restAdapter;
    private final ConcurrentHashMap<Class, Object> services;
    private final Session session;

    public interface AccountService {
        @GET("/1.1/sdk/account.json")
        void verifyAccount(Callback<VerifyAccountResponse> callback);
    }

    protected interface DeviceService {
        @POST("/1.1/device/register.json")
        @FormUrlEncoded
        void register(@Field("raw_phone_number") String str, @Field("text_key") String str2, @Field("send_numeric_pin") Boolean bool, @Field("lang") String str3, @Field("client_identifier_string") String str4, @Field("verification_type") String str5, Callback<DeviceRegistrationResponse> callback);
    }

    protected interface SdkService {
        @POST("/1.1/sdk/account.json")
        @FormUrlEncoded
        void account(@Field(DigitsClient.EXTRA_PHONE) String str, @Field("numeric_pin") String str2, Callback<DigitsUser> callback);

        @POST("/1/sdk/login")
        @FormUrlEncoded
        void auth(@Field("x_auth_phone_number") String str, @Field("verification_type") String str2, Callback<AuthResponse> callback);

        @POST("/1.1/sdk/account/email")
        void email(@Query("email_address") String str, Callback<DigitsSessionResponse> callback);

        @POST("/auth/1/xauth_challenge.json")
        @FormUrlEncoded
        void login(@Field("login_verification_request_id") String str, @Field("login_verification_user_id") long j, @Field("login_verification_challenge_response") String str2, Callback<DigitsSessionResponse> callback);

        @POST("/auth/1/xauth_pin.json")
        @FormUrlEncoded
        void verifyPin(@Field("login_verification_request_id") String str, @Field("login_verification_user_id") long j, @Field("pin") String str2, Callback<DigitsSessionResponse> callback);
    }

    DigitsApiClient(Session session) {
        this(session, TwitterCore.getInstance().getAuthConfig(), TwitterCore.getInstance().getSSLSocketFactory(), Digits.getInstance().getExecutorService(), new DigitsUserAgent(Digits.getInstance().getVersion(), Build.VERSION.RELEASE));
    }

    DigitsApiClient(Session session, TwitterAuthConfig authConfig, SSLSocketFactory sslFactory, ExecutorService executorService, DigitsUserAgent userAgent) {
        this.session = session;
        this.services = new ConcurrentHashMap<>();
        this.restAdapter = new RestAdapter.Builder().setEndpoint(new DigitsApi().getBaseHostUrl()).setRequestInterceptor(new DigitsRequestInterceptor(userAgent)).setExecutors(executorService, new MainThreadExecutor()).setClient(new AuthenticatedClient(authConfig, session, sslFactory)).build();
    }

    public Session getSession() {
        return this.session;
    }

    public SdkService getSdkService() {
        return (SdkService) getService(SdkService.class);
    }

    public DeviceService getDeviceService() {
        return (DeviceService) getService(DeviceService.class);
    }

    public AccountService getAccountService() {
        return (AccountService) getService(AccountService.class);
    }

    private <T> T getService(Class<T> cls) {
        if (!this.services.containsKey(cls)) {
            this.services.put(cls, this.restAdapter.create(cls));
        }
        return (T) this.services.get(cls);
    }
}
