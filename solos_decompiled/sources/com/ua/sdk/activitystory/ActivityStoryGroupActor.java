package com.ua.sdk.activitystory;

import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.group.Group;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.Criteria;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryGroupActor extends ActivityStoryActor {
    Criteria getCriteria();

    DataField getDataField();

    DataType getDataType();

    EntityRef<DataType> getDataTypeRef();

    Date getEndTime();

    int getGroupInviteCount();

    EntityRef<GroupInvite> getGroupInviteRef();

    EntityRef<User> getGroupOwnerRef();

    int getGroupUserCount();

    EntityRef<GroupUser> getGroupUserRef();

    String getName();

    Period getPeriod();

    EntityRef<Group> getRef();

    Date getStartTime();
}
