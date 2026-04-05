package com.ua.sdk.activitystory;

import com.facebook.share.internal.ShareConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class AttachmentDest {

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_HREF)
    String href;

    @SerializedName(FirebaseAnalytics.Param.INDEX)
    int index;

    @SerializedName("rel")
    String rel;
    transient String userId;

    public AttachmentDest(String href, String rel, int index) {
        this(href, rel, index, null);
    }

    public AttachmentDest(String href, String rel, int index, String userId) {
        this.href = href;
        this.rel = rel;
        this.index = index;
        this.userId = userId;
    }

    public String getHref() {
        return this.href;
    }

    public String getRel() {
        return this.rel;
    }

    public int getIndex() {
        return this.index;
    }

    public String getUserId() {
        return this.userId;
    }

    public String toString() {
        return "{\"href\":\"" + this.href + "\",\"rel\":\"" + this.rel + "\",\"index\":" + this.index + '}';
    }
}
