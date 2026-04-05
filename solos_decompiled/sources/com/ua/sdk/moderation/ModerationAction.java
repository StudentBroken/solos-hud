package com.ua.sdk.moderation;

import com.ua.sdk.Entity;

/* JADX INFO: loaded from: classes65.dex */
public interface ModerationAction extends Entity {
    String getAction();

    String getResource();

    void setAction(String str);

    void setResource(String str);
}
