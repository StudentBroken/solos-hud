package com.ua.sdk.group.invite;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.group.Group;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class GroupInviteImpl extends ApiTransferObject implements GroupInvite {
    public static Parcelable.Creator<GroupInviteImpl> CREATOR = new Parcelable.Creator<GroupInviteImpl>() { // from class: com.ua.sdk.group.invite.GroupInviteImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupInviteImpl createFromParcel(Parcel source) {
            return new GroupInviteImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupInviteImpl[] newArray(int size) {
            return new GroupInviteImpl[size];
        }
    };
    private static final String GROUP_KEY = "group";
    private static final String USER_KEY = "user";

    @SerializedName("email")
    String email;
    transient EntityRef<Group> groupRef;
    transient EntityRef<User> userRef;

    public GroupInviteImpl() {
    }

    private GroupInviteImpl(Parcel in) {
        super(in);
        this.email = in.readString();
    }

    @Override // com.ua.sdk.group.invite.GroupInvite
    public String getEmail() {
        return this.email;
    }

    @Override // com.ua.sdk.group.invite.GroupInvite
    public EntityRef<User> getUserRef() {
        Link user;
        if (this.userRef == null && (user = getLink("user")) != null) {
            this.userRef = new LinkEntityRef(user.getId(), user.getHref());
        }
        return this.userRef;
    }

    @Override // com.ua.sdk.group.invite.GroupInvite
    public EntityRef<Group> getGroupRef() {
        Link group;
        if (this.groupRef == null && (group = getLink(GROUP_KEY)) != null) {
            this.groupRef = new LinkEntityRef(group.getId(), group.getHref());
        }
        return this.groupRef;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroupRef(EntityRef<Group> groupRef) {
        if (groupRef != null && groupRef.getHref() != null) {
            this.groupRef = groupRef;
            addLink(GROUP_KEY, new Link(groupRef.getHref()));
        }
    }

    public void setUserRef(EntityRef<User> userRef) {
        if (userRef != null && userRef.getHref() != null) {
            this.userRef = userRef;
            addLink("user", new Link(userRef.getHref()));
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<GroupInvite> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.email);
    }
}
