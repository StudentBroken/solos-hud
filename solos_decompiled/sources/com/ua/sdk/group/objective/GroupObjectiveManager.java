package com.ua.sdk.group.objective;

import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupObjectiveManager {
    Request createGroupObjective(GroupObjective groupObjective, CreateCallback<GroupObjective> createCallback);

    GroupObjective createGroupObjective(GroupObjective groupObjective) throws UaException;

    EntityRef<GroupObjective> deleteGroupObjective(EntityRef<GroupObjective> entityRef) throws UaException;

    Request deleteGroupObjective(EntityRef<GroupObjective> entityRef, DeleteCallback<EntityRef<GroupObjective>> deleteCallback);

    Request fetchGroupObjective(EntityRef<GroupObjective> entityRef, FetchCallback<GroupObjective> fetchCallback);

    GroupObjective fetchGroupObjective(EntityRef<GroupObjective> entityRef) throws UaException;

    EntityList<GroupObjective> fetchGroupObjectiveList(EntityListRef<GroupObjective> entityListRef) throws UaException;

    Request fetchGroupObjectiveList(EntityListRef<GroupObjective> entityListRef, FetchCallback<EntityList<GroupObjective>> fetchCallback);

    Request updateGroupObjective(GroupObjective groupObjective, SaveCallback<GroupObjective> saveCallback);

    GroupObjective updateGroupObjective(GroupObjective groupObjective) throws UaException;
}
