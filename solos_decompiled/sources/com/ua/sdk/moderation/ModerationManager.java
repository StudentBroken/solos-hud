package com.ua.sdk.moderation;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface ModerationManager {
    Request flagEntity(EntityRef entityRef, CreateCallback<ModerationAction> createCallback);

    ModerationAction flagEntity(EntityRef entityRef) throws UaException;

    Request unflagEntity(EntityRef entityRef, CreateCallback<ModerationAction> createCallback);

    ModerationAction unflagEntity(EntityRef entityRef) throws UaException;
}
