package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;
import retrofit.http.GET;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes62.dex */
public interface ListService {
    @GET("/1.1/lists/statuses.json")
    void statuses(@Query("list_id") Long l, @Query("slug") String str, @Query("owner_screen_name") String str2, @Query("owner_id") Long l2, @Query("since_id") Long l3, @Query("max_id") Long l4, @Query("count") Integer num, @Query("include_entities") Boolean bool, @Query("include_rts") Boolean bool2, Callback<List<Tweet>> callback);
}
