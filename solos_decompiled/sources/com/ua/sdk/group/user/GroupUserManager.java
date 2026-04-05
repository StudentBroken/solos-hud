package com.ua.sdk.group.user;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupUserManager {
    Request createGroupUser(GroupUser groupUser, CreateCallback<GroupUser> createCallback);

    GroupUser createGroupUser(GroupUser groupUser) throws UaException;

    EntityRef<GroupUser> deleteGroupUser(EntityRef<GroupUser> entityRef) throws UaException;

    Request deleteGroupUser(EntityRef<GroupUser> entityRef, DeleteCallback<EntityRef<GroupUser>> deleteCallback);

    EntityList<GroupUser> fetchGroupUserList(EntityListRef<GroupUser> entityListRef) throws UaException;

    Request fetchGroupUserList(EntityListRef<GroupUser> entityListRef, FetchCallback<EntityList<GroupUser>> fetchCallback);
}
