package com.twitter.sdk.android.tweetcomposer.internal;

import com.twitter.sdk.android.core.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/* JADX INFO: loaded from: classes29.dex */
public interface CardService {
    @POST("/v2/cards/create.json")
    @FormUrlEncoded
    void create(@Field("card_data") CardData cardData, Callback<CardCreate> callback);
}
