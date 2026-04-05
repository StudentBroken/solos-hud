package com.ua.sdk.activitystory;

import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface Attachment extends Parcelable {

    public enum Status {
        UNKNOWN,
        PENDING,
        PROCESSING,
        READY
    }

    public enum Type {
        PHOTO,
        VIDEO
    }

    Date getPublished();

    Status getStatus();

    Type getType();

    String getUri();
}
