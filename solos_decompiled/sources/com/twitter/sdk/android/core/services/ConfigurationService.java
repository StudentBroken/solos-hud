package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Configuration;
import retrofit.http.GET;

/* JADX INFO: loaded from: classes62.dex */
public interface ConfigurationService {
    @GET("/1.1/help/configuration.json")
    void configuration(Callback<Configuration> callback);
}
