package com.ua.sdk.user;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface UserSharing extends Parcelable {
    Boolean getFacebook();

    Boolean getTwitter();

    boolean isFacebook();

    boolean isTwitter();

    void setFacebook(Boolean bool);

    void setTwitter(Boolean bool);
}
