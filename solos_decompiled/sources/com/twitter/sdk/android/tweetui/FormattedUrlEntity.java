package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.models.UrlEntity;

/* JADX INFO: loaded from: classes9.dex */
class FormattedUrlEntity {
    final String displayUrl;
    int end;
    int start;
    final String url;

    FormattedUrlEntity(UrlEntity entity) {
        this.start = entity.getStart();
        this.end = entity.getEnd();
        this.displayUrl = entity.displayUrl;
        this.url = entity.url;
    }
}
