package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes62.dex */
public interface FavoriteService {
    @POST("/1.1/favorites/create.json")
    @FormUrlEncoded
    void create(@Field("id") Long l, @Field("include_entities") Boolean bool, Callback<Tweet> callback);

    @POST("/1.1/favorites/destroy.json")
    @FormUrlEncoded
    void destroy(@Field("id") Long l, @Field("include_entities") Boolean bool, Callback<Tweet> callback);

    @GET("/1.1/favorites/list.json")
    void list(@Query("user_id") Long l, @Query("screen_name") String str, @Query("count") Integer num, @Query("since_id") String str2, @Query("max_id") String str3, @Query("include_entities") Boolean bool, Callback<List<Tweet>> callback);
}
