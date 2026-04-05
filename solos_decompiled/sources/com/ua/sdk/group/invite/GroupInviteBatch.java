package com.ua.sdk.group.invite;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteBatch extends GroupInviteImpl {

    @SerializedName("group_invites")
    List<GroupInvite> groupInvites;

    public void setGroupInvites(List<GroupInvite> groupInvites) {
        if (this.groupInvites == null) {
            this.groupInvites = new ArrayList();
        }
        this.groupInvites.clear();
        this.groupInvites.addAll(groupInvites);
    }
}
