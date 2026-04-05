package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/* JADX INFO: loaded from: classes62.dex */
public class Search {

    @SerializedName("search_metadata")
    public final SearchMetadata searchMetadata;

    @SerializedName("statuses")
    public final List<Tweet> tweets;

    public Search(List<Tweet> tweets, SearchMetadata searchMetadata) {
        this.tweets = tweets;
        this.searchMetadata = searchMetadata;
    }
}
