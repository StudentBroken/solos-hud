package com.ua.sdk.group.purpose;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupPurpose extends Entity<EntityRef<GroupPurpose>> {
    String getPurpose();

    Boolean isRestricted();
}
