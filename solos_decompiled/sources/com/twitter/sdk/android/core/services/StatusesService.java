package com.twitter.sdk.android.core.services;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes62.dex */
public interface StatusesService {
    @POST("/1.1/statuses/destroy/{id}.json")
    @FormUrlEncoded
    void destroy(@Path("id") Long l, @Field("trim_user") Boolean bool, Callback<Tweet> callback);

    @GET("/1.1/statuses/home_timeline.json")
    void homeTimeline(@Query("count") Integer num, @Query("since_id") Long l, @Query("max_id") Long l2, @Query("trim_user") Boolean bool, @Query("exclude_replies") Boolean bool2, @Query("contributor_details") Boolean bool3, @Query("include_entities") Boolean bool4, Callback<List<Tweet>> callback);

    @GET("/1.1/statuses/lookup.json")
    void lookup(@Query("id") String str, @Query("include_entities") Boolean bool, @Query("trim_user") Boolean bool2, @Query("map") Boolean bool3, Callback<List<Tweet>> callback);

    @GET("/1.1/statuses/mentions_timeline.json")
    void mentionsTimeline(@Query("count") Integer num, @Query("since_id") Long l, @Query("max_id") Long l2, @Query("trim_user") Boolean bool, @Query("contributor_details") Boolean bool2, @Query("include_entities") Boolean bool3, Callback<List<Tweet>> callback);

    @POST("/1.1/statuses/retweet/{id}.json")
    @FormUrlEncoded
    void retweet(@Path("id") Long l, @Field("trim_user") Boolean bool, Callback<Tweet> callback);

    @GET("/1.1/statuses/retweets_of_me.json")
    void retweetsOfMe(@Query("count") Integer num, @Query("since_id") Long l, @Query("max_id") Long l2, @Query("trim_user") Boolean bool, @Query("include_entities") Boolean bool2, @Query("include_user_entities") Boolean bool3, Callback<List<Tweet>> callback);

    @GET("/1.1/statuses/show.json")
    void show(@Query("id") Long l, @Query("trim_user") Boolean bool, @Query("include_my_retweet") Boolean bool2, @Query("include_entities") Boolean bool3, Callback<Tweet> callback);

    @POST("/1.1/statuses/unretweet/{id}.json")
    @FormUrlEncoded
    void unretweet(@Path("id") Long l, @Field("trim_user") Boolean bool, Callback<Tweet> callback);

    @POST("/1.1/statuses/update.json")
    @FormUrlEncoded
    @Deprecated
    void update(@Field("status") String str, @Field("in_reply_to_status_id") Long l, @Field("possibly_sensitive") Boolean bool, @Field("lat") Double d, @Field("long") Double d2, @Field("place_id") String str2, @Field("display_cooridnates") Boolean bool2, @Field("trim_user") Boolean bool3, Callback<Tweet> callback);

    @POST("/1.1/statuses/update.json")
    @FormUrlEncoded
    void update(@Field("status") String str, @Field("in_reply_to_status_id") Long l, @Field("possibly_sensitive") Boolean bool, @Field("lat") Double d, @Field("long") Double d2, @Field("place_id") String str2, @Field("display_cooridnates") Boolean bool2, @Field("trim_user") Boolean bool3, @Field("media_ids") String str3, Callback<Tweet> callback);

    @GET("/1.1/statuses/user_timeline.json")
    void userTimeline(@Query("user_id") Long l, @Query("screen_name") String str, @Query("count") Integer num, @Query("since_id") Long l2, @Query("max_id") Long l3, @Query("trim_user") Boolean bool, @Query("exclude_replies") Boolean bool2, @Query("contributor_details") Boolean bool3, @Query("include_rts") Boolean bool4, Callback<List<Tweet>> callback);
}
