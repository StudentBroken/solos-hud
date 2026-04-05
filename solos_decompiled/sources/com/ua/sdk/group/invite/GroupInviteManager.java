package com.ua.sdk.group.invite;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupInviteManager {
    Request createGroupInvite(GroupInvite groupInvite, CreateCallback<GroupInvite> createCallback);

    GroupInvite createGroupInvite(GroupInvite groupInvite) throws UaException;

    EntityRef<GroupInvite> deleteGroupInvite(EntityRef<GroupInvite> entityRef) throws UaException;

    Request deleteGroupInvite(EntityRef<GroupInvite> entityRef, DeleteCallback<EntityRef<GroupInvite>> deleteCallback);

    EntityList<GroupInvite> fetchGroupInviteList(EntityListRef<GroupInvite> entityListRef) throws UaException;

    Request fetchGroupInviteList(EntityListRef<GroupInvite> entityListRef, FetchCallback<EntityList<GroupInvite>> fetchCallback);

    EntityList<GroupInvite> patchGroupInvite(GroupInvite groupInvite) throws UaException;

    Request patchGroupInvite(GroupInvite groupInvite, CreateCallback<EntityList<GroupInvite>> createCallback);
}
