package com.ua.sdk.group;

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
public interface GroupManager {
    Request createGroup(Group group, CreateCallback<Group> createCallback);

    Group createGroup(Group group) throws UaException;

    Request deleteGroup(EntityRef<Group> entityRef, DeleteCallback<EntityRef<Group>> deleteCallback);

    void deleteGroup(EntityRef<Group> entityRef) throws UaException;

    Request endGroupChallenge(EntityRef<Group> entityRef, SaveCallback<Group> saveCallback);

    Request endGroupChallenge(Group group, SaveCallback<Group> saveCallback);

    Group endGroupChallenge(EntityRef<Group> entityRef) throws UaException;

    Group endGroupChallenge(Group group) throws UaException;

    Request fetchGroup(EntityRef<Group> entityRef, FetchCallback<Group> fetchCallback);

    Group fetchGroup(EntityRef<Group> entityRef) throws UaException;

    EntityList<Group> fetchGroupList(EntityListRef<Group> entityListRef) throws UaException;

    Request fetchGroupList(EntityListRef<Group> entityListRef, FetchCallback<EntityList<Group>> fetchCallback);

    Request updateGroup(Group group, EntityRef<Group> entityRef, CreateCallback<Group> createCallback);

    Group updateGroup(Group group, EntityRef<Group> entityRef) throws UaException;
}
