package com.ua.sdk.group;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.GroupObjective;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public interface Group extends Entity {
    EntityRef<ActivityStory> getActivityFeedRef();

    String getDescription();

    EntityRef<GroupInvite> getGroupInviteRef();

    GroupObjective getGroupObjective();

    EntityRef<User> getGroupOwnerRef();

    EntityRef<GroupPurpose> getGroupPurposeRef();

    EntityRef<GroupUser> getGroupUserRef();

    String getInvitationCode();

    Boolean getInvitationRequired();

    int getMaxUsers();

    int getMemberCount();

    String getName();

    Boolean getPublicGroup();
}
