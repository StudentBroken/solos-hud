package com.ua.sdk.group;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.GroupObjective;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class GroupImpl extends ApiTransferObject implements Group, Parcelable {
    public static final Parcelable.Creator<GroupImpl> CREATOR = new Parcelable.Creator<GroupImpl>() { // from class: com.ua.sdk.group.GroupImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupImpl createFromParcel(Parcel source) {
            return new GroupImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupImpl[] newArray(int size) {
            return new GroupImpl[size];
        }
    };
    private static final String REF_SELF = "self";
    private transient EntityRef<ActivityStory> activityFeedRef;

    @SerializedName("description")
    private String description;
    private transient EntityRef<GroupInvite> groupInviteRef;

    @SerializedName("group_objective")
    private GroupObjective groupObjective;
    private transient EntityRef<User> groupOwnerRef;
    private transient EntityRef<GroupPurpose> groupPurposeRef;
    private transient EntityRef<GroupUser> groupUserRef;

    @SerializedName("invitation_code")
    private String invitationCode;

    @SerializedName("invitation_required")
    private Boolean invitationRequired;

    @SerializedName("max_users")
    private Integer maxUsers;
    private transient int memberCount;

    @SerializedName("name")
    private String name;

    @SerializedName("is_public")
    private Boolean publicGroup;

    public GroupImpl() {
    }

    @Override // com.ua.sdk.group.Group
    public Boolean getPublicGroup() {
        return this.publicGroup;
    }

    public void setPublicGroup(Boolean publicGroup) {
        this.publicGroup = publicGroup;
    }

    @Override // com.ua.sdk.group.Group
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // com.ua.sdk.group.Group
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override // com.ua.sdk.group.Group
    public Boolean getInvitationRequired() {
        return this.invitationRequired;
    }

    public void setInvitationRequired(Boolean invitationRequired) {
        this.invitationRequired = invitationRequired;
    }

    @Override // com.ua.sdk.group.Group
    public String getInvitationCode() {
        return this.invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    @Override // com.ua.sdk.group.Group
    public int getMemberCount() {
        Link userRef;
        if (this.memberCount == 0 && (userRef = getLink("users")) != null) {
            this.memberCount = userRef.getCount().intValue();
        }
        return this.memberCount;
    }

    @Override // com.ua.sdk.group.Group
    public int getMaxUsers() {
        if (this.maxUsers == null) {
            return 0;
        }
        return this.maxUsers.intValue();
    }

    public void setMaxUsers(int count) {
        this.maxUsers = Integer.valueOf(count);
    }

    @Override // com.ua.sdk.group.Group
    public GroupObjective getGroupObjective() {
        return this.groupObjective;
    }

    public void setGroupObjective(GroupObjective groupObjective) {
        this.groupObjective = groupObjective;
    }

    @Override // com.ua.sdk.group.Group
    public EntityRef<GroupUser> getGroupUserRef() {
        List<Link> links;
        if (this.groupUserRef == null && (links = getLinks("group_user")) != null) {
            this.groupUserRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.groupUserRef;
    }

    @Override // com.ua.sdk.group.Group
    public EntityRef<ActivityStory> getActivityFeedRef() {
        List<Link> links;
        if (this.activityFeedRef == null && (links = getLinks("activity_feed")) != null) {
            this.activityFeedRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.activityFeedRef;
    }

    @Override // com.ua.sdk.group.Group
    public EntityRef<GroupPurpose> getGroupPurposeRef() {
        List<Link> links;
        if (this.groupPurposeRef == null && (links = getLinks("group_purpose")) != null) {
            this.groupPurposeRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.groupPurposeRef;
    }

    public void setGroupPurposeRef(EntityRef<GroupPurpose> ref) {
        this.groupPurposeRef = ref;
        addLink("group_purpose", new Link(ref.getHref(), ref.getId()));
    }

    @Override // com.ua.sdk.group.Group
    public EntityRef<GroupInvite> getGroupInviteRef() {
        List<Link> links;
        if (this.groupInviteRef == null && (links = getLinks("group_invite")) != null) {
            this.groupInviteRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.groupInviteRef;
    }

    @Override // com.ua.sdk.group.Group
    public EntityRef<User> getGroupOwnerRef() {
        Link owner;
        if (this.groupOwnerRef == null && (owner = getLink("group_owner")) != null) {
            this.groupOwnerRef = new LinkEntityRef(owner.getId(), owner.getHref());
        }
        return this.groupOwnerRef;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<GroupImpl> getRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.publicGroup);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeValue(this.invitationRequired);
        dest.writeString(this.invitationCode);
        dest.writeValue(this.groupObjective);
        dest.writeValue(this.maxUsers);
    }

    private GroupImpl(Parcel in) {
        super(in);
        this.publicGroup = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.invitationRequired = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.invitationCode = in.readString();
        this.groupObjective = (GroupObjective) in.readValue(GroupObjective.class.getClassLoader());
        this.maxUsers = (Integer) in.readValue(Integer.class.getClassLoader());
    }
}
