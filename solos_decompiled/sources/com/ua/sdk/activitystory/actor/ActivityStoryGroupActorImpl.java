package com.ua.sdk.activitystory.actor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryActor;
import com.ua.sdk.activitystory.ActivityStoryGroupActor;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.datapoint.DataField;
import com.ua.sdk.datapoint.DataType;
import com.ua.sdk.group.Group;
import com.ua.sdk.group.invite.GroupInvite;
import com.ua.sdk.group.objective.Criteria;
import com.ua.sdk.group.user.GroupUser;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Period;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryGroupActorImpl extends ApiTransferObject implements ActivityStoryGroupActor {
    public static final Parcelable.Creator<ActivityStoryGroupActorImpl> CREATOR = new Parcelable.Creator<ActivityStoryGroupActorImpl>() { // from class: com.ua.sdk.activitystory.actor.ActivityStoryGroupActorImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupActorImpl createFromParcel(Parcel source) {
            return new ActivityStoryGroupActorImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryGroupActorImpl[] newArray(int size) {
            return new ActivityStoryGroupActorImpl[size];
        }
    };
    private static final String GROUP_INVITE_LINK = "group_invites";
    private static final String GROUP_OWNER_LINK = "group_owner";
    private static final String GROUP_USER_LINK = "group_users";

    @SerializedName("criteria")
    private Criteria criteria;
    private transient DataField dataField;

    @SerializedName("data_type_field")
    private String dataFieldStr;
    private transient DataType dataType;
    private transient EntityRef<DataType> dataTypeRef;

    @SerializedName("data_type")
    private String dataTypeStr;

    @SerializedName("end_time")
    private Date endTime;
    private transient Integer groupInviteCount;
    private transient EntityRef<GroupInvite> groupInviteRef;
    private transient EntityRef<User> groupOwnerRef;
    private transient Integer groupUserCount;
    private transient EntityRef<GroupUser> groupUserRef;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("period")
    private Period period;
    private transient EntityRef<Group> selfRef;

    @SerializedName("start_time")
    private Date startTime;

    public ActivityStoryGroupActorImpl() {
    }

    private ActivityStoryGroupActorImpl(Parcel in) {
        super(in);
        this.startTime = (Date) in.readValue(Date.class.getClassLoader());
        this.endTime = (Date) in.readValue(Date.class.getClassLoader());
        this.name = in.readString();
        this.dataTypeStr = in.readString();
        this.id = in.readString();
        this.period = (Period) in.readParcelable(Period.class.getClassLoader());
        this.dataFieldStr = in.readString();
        this.criteria = (Criteria) in.readValue(Criteria.class.getClassLoader());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public Date getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public DataType getDataType() {
        if (this.dataType == null && this.dataTypeStr != null) {
            this.dataType = BaseDataTypes.ALL_BASE_TYPE_MAP.get(this.dataTypeStr);
        }
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        if (dataType != null) {
            this.dataType = BaseDataTypes.ALL_BASE_TYPE_MAP.get(dataType.getId());
            if (this.dataType != null) {
                this.dataTypeStr = this.dataType.getId();
            }
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public DataField getDataField() {
        if (this.dataFieldStr != null && this.dataField == null) {
            this.dataField = BaseDataTypes.findDataField(this.dataFieldStr, getDataType());
        }
        return this.dataField;
    }

    public void setDataField(DataField dataField) {
        if (dataField != null) {
            this.dataField = BaseDataTypes.findDataField(dataField.getId(), getDataType());
            if (this.dataField != null) {
                this.dataFieldStr = this.dataField.getId();
            }
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
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

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public ActivityStoryActor.Type getType() {
        return ActivityStoryActor.Type.GROUP;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
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

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public EntityRef<Group> getRef() {
        Link ref;
        if (this.selfRef == null && (ref = getLink("self")) != null) {
            this.selfRef = new LinkEntityRef(ref.getId(), ref.getHref());
        }
        return this.selfRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public Criteria getCriteria() {
        return this.criteria;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
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

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public EntityRef<GroupInvite> getGroupInviteRef() {
        Link link;
        if (this.groupInviteRef == null && (link = getLink(GROUP_INVITE_LINK)) != null) {
            this.groupInviteRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.groupInviteRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public EntityRef<DataType> getDataTypeRef() {
        Link link;
        if (this.dataTypeRef == null && (link = getLink("data_type")) != null) {
            this.dataTypeRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.dataTypeRef;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryGroupActor
    public EntityRef<GroupUser> getGroupUserRef() {
        Link link;
        if (this.groupUserRef == null && (link = getLink(GROUP_USER_LINK)) != null) {
            this.groupUserRef = new LinkEntityRef(link.getId(), link.getHref());
        }
        return this.groupUserRef;
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
        dest.writeString(this.dataTypeStr);
        dest.writeString(this.id);
        dest.writeParcelable(this.period, flags);
        dest.writeString(this.dataFieldStr);
        dest.writeValue(this.criteria);
    }
}
