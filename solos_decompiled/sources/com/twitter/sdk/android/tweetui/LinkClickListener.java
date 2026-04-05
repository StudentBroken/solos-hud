package com.twitter.sdk.android.tweetui;

import com.twitter.sdk.android.core.models.MediaEntity;

/* JADX INFO: loaded from: classes9.dex */
interface LinkClickListener {
    void onPhotoClicked(MediaEntity mediaEntity);

    void onUrlClicked(String str);
}
