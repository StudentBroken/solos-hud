package com.ua.sdk.moderation;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.EntityRef;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public class ModerationManagerImpl implements ModerationManager {
    private final ModerationActionManager actionManager;

    public ModerationManagerImpl(ModerationActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @Override // com.ua.sdk.moderation.ModerationManager
    public ModerationAction flagEntity(EntityRef ref) throws UaException {
        return this.actionManager.flagEntity(ref);
    }

    @Override // com.ua.sdk.moderation.ModerationManager
    public Request flagEntity(EntityRef ref, CreateCallback<ModerationAction> callback) {
        return this.actionManager.flagEntity(ref, callback);
    }

    @Override // com.ua.sdk.moderation.ModerationManager
    public ModerationAction unflagEntity(EntityRef ref) throws UaException {
        return this.actionManager.unflagEntity(ref);
    }

    @Override // com.ua.sdk.moderation.ModerationManager
    public Request unflagEntity(EntityRef ref, CreateCallback<ModerationAction> callback) {
        return this.actionManager.unflagEntity(ref, callback);
    }
}
