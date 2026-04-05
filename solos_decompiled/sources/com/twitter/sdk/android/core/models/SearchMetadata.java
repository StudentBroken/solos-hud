package com.twitter.sdk.android.core.models;

import com.google.android.gms.actions.SearchIntents;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class SearchMetadata {

    @SerializedName("completed_in")
    public final double completedIn;

    @SerializedName("count")
    public final long count;

    @SerializedName("max_id")
    public final long maxId;

    @SerializedName("max_id_str")
    public final String maxIdStr;

    @SerializedName("next_results")
    public final String nextResults;

    @SerializedName(SearchIntents.EXTRA_QUERY)
    public final String query;

    @SerializedName("refresh_url")
    public final String refreshUrl;

    @SerializedName("since_id")
    public final long sinceId;

    @SerializedName("since_id_str")
    public final String sinceIdStr;

    public SearchMetadata(int maxId, int sinceId, String refreshUrl, String nextResults, int count, double completedIn, String sinceIdStr, String query, String maxIdStr) {
        this.maxId = maxId;
        this.sinceId = sinceId;
        this.refreshUrl = refreshUrl;
        this.nextResults = nextResults;
        this.count = count;
        this.completedIn = completedIn;
        this.sinceIdStr = sinceIdStr;
        this.query = query;
        this.maxIdStr = maxIdStr;
    }
}
