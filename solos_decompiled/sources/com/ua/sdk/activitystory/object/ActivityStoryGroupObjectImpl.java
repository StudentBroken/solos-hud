package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryGroupObject;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.group.Group;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.Criteria;
import com.ua.sdk.group.purpose.GroupPurpose;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.LinkListRef;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryGroupObjectImpl extends ApiTransferObject implements ActivityStoryGroupObject {
    public static final Parcelable.Creator<ActivityStoryGroupObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryGroupObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryGroupObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryGroupObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupObjectImpl[] newArray(int size) {
            return new ActivityStoryGroupObjectImpl[size];
        }
    };
    private static final String GROUP_INVITE_LINK = "group_invites";
    private static final String GROUP_OWNER_LINK = "group_owner";
    private static final String GROUP_USER_LINK = "group_users";

    @SerializedName("criteria")
    private Criteria criteria;
    private transient DataField dataField;
    private transient DataType dataType;

    @SerializedName("data_type_field")
    private String dataTypeField;
    private transient EntityRef<DataType> dataTypeRef;

    @SerializedName("end_time")
    private Date endTime;
    private transient Integer groupInviteCount;
    private transient EntityRef<User> groupOwnerRef;
    private transient Integer groupUserCount;

    @SerializedName("id")
    private String id;

    @SerializedName("invite_accepted")
    private Boolean inviteAccepted;
    private transient EntityListRef<GroupInvite> inviteRef;

    @SerializedName("name")
    private String name;

    @SerializedName("period")
    private Period period;
    private transient EntityRef<GroupPurpose> purposeRef;
    private transient EntityRef<Group> selfRef;

    @SerializedName("start_time")
    private Date startTime;
    private transient EntityListRef<GroupUser> usersRef;

    public ActivityStoryGroupObjectImpl() {
    }

    private ActivityStoryGroupObjectImpl(Parcel in) {
        super(in);
        this.startTime = (Date) in.readValue(Date.class.getClassLoader());
        this.endTime = (Date) in.readValue(Date.class.getClassLoader());
        this.name = in.readString();
        this.dataTypeField = in.readString();
        this.id = in.readString();
        this.period = (Period) in.readParcelable(Period.class.getClassLoader());
        this.inviteAccepted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.criteria = (Criteria) in.readValue(Criteria.class.getClassLoader());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public Date getStartTime() {
        return this.startTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public Date getEndTime() {
        return this.endTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setName(String name) {
        this.name = name;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public Period getPeriod() {
        return this.period;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public Boolean getInviteAccepted() {
        return this.inviteAccepted;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setInviteAccepted(Boolean inviteAccepted) {
        this.inviteAccepted = inviteAccepted;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public DataType getDataType() {
        if (this.dataType == null && getDataTypeRef() != null) {
            this.dataType = BaseDataTypes.ALL_BASE_TYPE_MAP.get(this.dataTypeRef.getId());
        }
        return this.dataType;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setDataType(DataType dataType) {
        if (dataType != null) {
            this.dataType = BaseDataTypes.ALL_BASE_TYPE_MAP.get(dataType.getId());
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public DataField getDataField() {
        if (this.dataField == null && this.dataTypeField != null) {
            this.dataField = BaseDataTypes.findDataField(this.dataTypeField, getDataType());
        }
        return this.dataField;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public void setDataField(DataField dataField) {
        if (dataField != null) {
            this.dataField = BaseDataTypes.findDataField(dataField.getId(), getDataType());
            if (this.dataField != null) {
                this.dataTypeField = this.dataField.getId();
            }
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public int getGroupInviteCount() {
        if (this.groupInviteCount == null) {
            Link link = getLink(GROUP_INVITE_LINK);
            if (link == null) {
                this.groupInviteCount = 0;
            } else {
                this.groupInviteCount = Integer.valueOf(link.getCount() != null ? link.getCount().intValue() : 0);
            }
        }
        return this.groupInviteCount.intValue();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public int getGroupUserCount() {
        if (this.groupUserCount == null) {
            Link link = getLink(GROUP_USER_LINK);
            if (link == null) {
                this.groupUserCount = 0;
            } else {
                this.groupUserCount = Integer.valueOf(link.getCount() != null ? link.getCount().intValue() : 0);
            }
        }
        return this.groupUserCount.intValue();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.GROUP;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityRef<Group> getRef() {
        Link link;
        if (this.selfRef == null && (link = getLink("self")) != null) {
            this.selfRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.selfRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityListRef<GroupInvite> getInviteRef() {
        Link link;
        if (this.inviteRef == null && (link = getLink(GROUP_INVITE_LINK)) != null) {
            this.inviteRef = new LinkListRef(link.getHref());
        }
        return this.inviteRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityRef<GroupPurpose> getPurposeRef() {
        Link link;
        if (this.purposeRef == null && (link = getLink("purpose")) != null) {
            this.purposeRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.purposeRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityRef<DataType> getDataTypeRef() {
        Link link;
        if (this.dataTypeRef == null && (link = getLink("data_type")) != null) {
            this.dataTypeRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.dataTypeRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityListRef<GroupUser> getUsersRef() {
        Link link;
        if (this.usersRef == null && (link = getLink(GROUP_USER_LINK)) != null) {
            this.usersRef = new LinkListRef(link.getHref());
        }
        return this.usersRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public Criteria getCriteria() {
        return this.criteria;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupObject
    public EntityRef<User> getGroupOwnerRef() {
        Link user;
        if (this.groupOwnerRef == null && (user = getLink(GROUP_OWNER_LINK)) != null) {
            this.groupOwnerRef = new LinkEntityRef(user.getId(), user.getHref());
        }
        return this.groupOwnerRef;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.startTime);
        dest.writeValue(this.endTime);
        dest.writeString(this.name);
        dest.writeString(this.dataTypeField);
        dest.writeString(this.id);
        dest.writeParcelable(this.period, flags);
        dest.writeValue(this.inviteAccepted);
        dest.writeValue(this.criteria);
    }
}
