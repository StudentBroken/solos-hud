package com.ua.sdk.activitystory;

import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.group.Group;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.Criteria;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryGroupObject extends ActivityStoryObject {
    Criteria getCriteria();

    DataField getDataField();

    DataType getDataType();

    EntityRef<DataType> getDataTypeRef();

    Date getEndTime();

    int getGroupInviteCount();

    EntityRef<User> getGroupOwnerRef();

    int getGroupUserCount();

    String getId();

    Boolean getInviteAccepted();

    EntityListRef<GroupInvite> getInviteRef();

    String getName();

    Period getPeriod();

    EntityRef<GroupPurpose> getPurposeRef();

    EntityRef<Group> getRef();

    Date getStartTime();

    EntityListRef<GroupUser> getUsersRef();

    void setDataField(DataField dataField);

    void setDataType(DataType dataType);

    void setEndTime(Date date);

    void setId(String str);

    void setInviteAccepted(Boolean bool);

    void setName(String str);

    void setPeriod(Period period);

    void setStartTime(Date date);
}
