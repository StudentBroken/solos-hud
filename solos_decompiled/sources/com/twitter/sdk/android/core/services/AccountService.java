package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;
import retrofit.http.GET;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes62.dex */
public interface AccountService {
    @GET("/1.1/account/verify_credentials.json")
    User verifyCredentials(@Query("include_entities") Boolean bool, @Query("skip_status") Boolean bool2);

    @GET("/1.1/account/verify_credentials.json")
    void verifyCredentials(@Query("include_entities") Boolean bool, @Query("skip_status") Boolean bool2, Callback<User> callback);
}
