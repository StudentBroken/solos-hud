package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.Callback;

/* JADX INFO: loaded from: classes9.dex */
public interface Timeline<T> {
    void next(Long l, Callback<TimelineResult<T>> callback);

    void previous(Long l, Callback<TimelineResult<T>> callback);
}
