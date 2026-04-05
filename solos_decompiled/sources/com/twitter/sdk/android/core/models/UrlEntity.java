package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes62.dex */
public class UrlEntity extends Entity {

    @SerializedName("display_url")
    public final String displayUrl;

    @SerializedName("expanded_url")
    public final String expandedUrl;

    @SerializedName("url")
    public final String url;

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getEnd() {
        return super.getEnd();
    }

    @Override // com.twitter.sdk.android.core.models.Entity
    public /* bridge */ /* synthetic */ int getStart() {
        return super.getStart();
    }

    public UrlEntity(String url, String expandedUrl, String displayUrl, int start, int end) {
        super(start, end);
        this.url = url;
        this.expandedUrl = expandedUrl;
        this.displayUrl = displayUrl;
    }
}
