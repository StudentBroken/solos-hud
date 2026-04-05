package com.ua.sdk.activitystory;

import com.ua.sdk.ImageUrl;
import com.ua.sdk.activitystory.Attachment;

/* JADX INFO: loaded from: classes65.dex */
public interface VideoAttachment extends Attachment {

    public enum Provider {
        OOYALA,
        UNKNOWN
    }

    Provider getProvider();

    String getProviderId();

    String getProviderString();

    ImageUrl getThumbnailUrl();

    @Override // com.ua.sdk.activitystory.Attachment
    Attachment.Type getType();
}
