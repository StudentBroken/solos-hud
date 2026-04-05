package com.twitter.sdk.android.tweetcomposer;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/* JADX INFO: loaded from: classes29.dex */
public interface StatusesService {
    @POST("/1.1/statuses/update.json")
    @FormUrlEncoded
    void update(@Field("status") String str, @Field("card_uri") String str2, Callback<Tweet> callback);
}
