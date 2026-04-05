package com.ua.sdk.group.invite;

import com.ua.sdk.EntityRef;
import com.ua.sdk.group.Group;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteBuilder {
    String email;
    List<GroupInvite> groupInvites;
    EntityRef<Group> groupRef;
    EntityRef<User> userRef;

    public GroupInviteBuilder setGroup(EntityRef<Group> groupRef) {
        this.groupRef = groupRef;
        return this;
    }

    public GroupInviteBuilder setUser(EntityRef<User> userRef) {
        this.userRef = userRef;
        return this;
    }

    public GroupInviteBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public GroupInviteBuilder addGroupInvite(EntityRef<User> user, EntityRef<Group> group) {
        GroupInvite userInvite = new GroupInviteBuilder().setUser(user).setGroup(group).build();
        return addGroupInvite(userInvite);
    }

    public GroupInviteBuilder addGroupInvite(String email, EntityRef<Group> group) {
        GroupInvite emailInvite = new GroupInviteBuilder().setEmail(email).setGroup(group).build();
        return addGroupInvite(emailInvite);
    }

    public GroupInvite build() {
        if (this.groupInvites != null && !this.groupInvites.isEmpty()) {
            GroupInviteBatch impl = new GroupInviteBatch();
            impl.setGroupInvites(this.groupInvites);
            return impl;
        }
        Precondition.isNotNull(this.groupRef);
        if (this.email == null) {
            Precondition.isNotNull(this.userRef);
        }
        if (this.userRef == null) {
            Precondition.isNotNull(this.email);
        }
        GroupInviteImpl impl2 = new GroupInviteImpl();
        impl2.setUserRef(this.userRef);
        impl2.setGroupRef(this.groupRef);
        impl2.setEmail(this.email);
        return impl2;
    }

    private GroupInviteBuilder addGroupInvite(GroupInvite groupInvite) {
        if (this.groupInvites == null) {
            this.groupInvites = new ArrayList();
        }
        this.groupInvites.add(groupInvite);
        return this;
    }
}
