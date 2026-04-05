package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.internal.TwitterCollection;
import retrofit.http.GET;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes62.dex */
public interface CollectionService {
    @GET("/1.1/collections/entries.json")
    void collection(@Query("id") String str, @Query("count") Integer num, @Query("max_position") Long l, @Query("min_position") Long l2, Callback<TwitterCollection> callback);
}
