package com.ua.sdk.group;

import com.ua.sdk.EntityRef;
import com.ua.sdk.group.objective.GroupObjective;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class GroupBuilder {
    String description;
    GroupObjective groupObjective;
    EntityRef<GroupPurpose> groupPurposeRef;
    String invitationCode;
    Boolean invitationRequired;
    Boolean isPublic;
    String name;

    public GroupBuilder setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public GroupBuilder setInvitationRequired(Boolean invitationRequired) {
        this.invitationRequired = invitationRequired;
        return this;
    }

    public GroupBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public GroupBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public GroupBuilder setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
        return this;
    }

    public GroupBuilder setGroupObjective(GroupObjective groupObjective) {
        this.groupObjective = groupObjective;
        return this;
    }

    public GroupBuilder setGroupPurpose(GroupPurpose purpose) {
        Precondition.isNotNull(purpose);
        this.groupPurposeRef = purpose.getRef();
        return this;
    }

    public Group build() {
        Precondition.isNotNull(this.name, "name");
        Precondition.isNotNull(this.groupObjective, "groupObjective");
        if (this.groupPurposeRef == null) {
            this.groupPurposeRef = new LinkEntityRef(UrlBuilderImpl.GET_GROUP_PURPOSE_CHALLENGE_URL);
        }
        GroupImpl impl = new GroupImpl();
        impl.setPublicGroup(this.isPublic);
        impl.setInvitationRequired(this.invitationRequired);
        impl.setName(this.name);
        impl.setDescription(this.description);
        impl.setInvitationCode(this.invitationCode);
        impl.setGroupObjective(this.groupObjective);
        impl.setGroupPurposeRef(this.groupPurposeRef);
        return impl;
    }
}
