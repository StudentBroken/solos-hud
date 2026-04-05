package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.Attachments;
import com.ua.sdk.activitystory.SocialSettings;
import com.ua.sdk.activitytype.ActivityTypeRef;
import com.ua.sdk.gear.user.UserGearRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;
import com.ua.sdk.route.RouteRef;
import com.ua.sdk.user.User;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutImpl extends ApiTransferObject implements Workout {
    public static final Parcelable.Creator<WorkoutImpl> CREATOR = new Parcelable.Creator<WorkoutImpl>() { // from class: com.ua.sdk.workout.WorkoutImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutImpl createFromParcel(Parcel source) {
            return new WorkoutImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutImpl[] newArray(int size) {
            return new WorkoutImpl[size];
        }
    };
    private static final String KEY_ACTIVITY_TYPE = "activity_type";
    private static final String KEY_PRIVACY = "privacy";
    private static final String KEY_ROUTE = "route";
    private static final String KEY_SELF = "self";
    private static final String KEY_USER = "user";
    private static final String KEY_USERGEAR = "usergear";

    @SerializedName("attachments")
    Attachments attachments;

    @SerializedName("created_datetime")
    Date createdTime;

    @SerializedName("has_time_series")
    Boolean hasTimeSeries;

    @SerializedName("is_verified")
    Boolean isVerified;

    @SerializedName("name")
    String name;

    @SerializedName("notes")
    String notes;

    @SerializedName("reference_key")
    String referenceKey;

    @SerializedName("sharing")
    SocialSettings socialSettings;

    @SerializedName("source")
    String source;

    @SerializedName("start_datetime")
    Date startTime;

    @SerializedName("time_series")
    TimeSeriesData timeSeries;

    @SerializedName("start_locale_timezone")
    TimeZone timeZone;

    @SerializedName("updated_datetime")
    Date updateTime;

    @SerializedName("aggregates")
    WorkoutAggregates workoutAggregates;

    protected WorkoutImpl(WorkoutBuilderImpl builder) {
        this.startTime = builder.startTime;
        this.updateTime = builder.updateTime;
        this.createdTime = builder.createdTime;
        this.name = builder.name;
        this.notes = builder.notes;
        this.timeZone = builder.timeZone;
        this.source = builder.source;
        this.referenceKey = builder.referenceKey;
        this.workoutAggregates = builder.workoutAggregates;
        this.timeSeries = builder.timeSeries;
        this.hasTimeSeries = Boolean.valueOf(builder.hasTimeSeries);
        this.socialSettings = builder.socialSettings;
        this.attachments = builder.attachments;
        if (builder.userGearEntityRef != null) {
            setLink(KEY_USERGEAR, new Link(builder.userGearEntityRef.getHref(), builder.userGearEntityRef.getId()));
        }
        if (builder.activityTypeEntityRef != null) {
            setLink(KEY_ACTIVITY_TYPE, new Link(builder.activityTypeEntityRef.getHref(), builder.activityTypeEntityRef.getId()));
        }
        if (builder.privacy != null) {
            setLink("privacy", PrivacyHelper.toLink(builder.privacy));
        }
        if (builder.workoutRef != null) {
            setLink("self", new Link(builder.workoutRef.getHref(), builder.workoutRef.getId()));
        }
        if (builder.routeRef != null) {
            setLink(KEY_ROUTE, new Link(builder.routeRef.getHref(), builder.routeRef.getId()));
        }
        if (builder.userRef != null) {
            setLink("user", new Link(builder.userRef.getHref(), builder.userRef.getId()));
        }
    }

    @Override // com.ua.sdk.workout.Workout
    public Date getStartTime() {
        return this.startTime;
    }

    @Override // com.ua.sdk.workout.Workout
    public Date getUpdatedTime() {
        return this.updateTime;
    }

    @Override // com.ua.sdk.workout.Workout
    public Date getCreatedTime() {
        return this.createdTime;
    }

    @Override // com.ua.sdk.workout.Workout
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.workout.Workout
    public String getNotes() {
        return this.notes;
    }

    @Override // com.ua.sdk.workout.Workout
    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    @Override // com.ua.sdk.workout.Workout
    public String getSource() {
        return this.source;
    }

    @Override // com.ua.sdk.workout.Workout
    public RouteRef getRouteRef() {
        Link link = getLink(KEY_ROUTE);
        if (link == null) {
            return null;
        }
        return new RouteRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.workout.Workout
    public EntityRef<User> getUserRef() {
        Link link = getLink("user");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.workout.Workout
    public ActivityTypeRef getActivityTypeRef() {
        Link link = getLink(KEY_ACTIVITY_TYPE);
        if (link == null) {
            return null;
        }
        return ActivityTypeRef.getBuilder().setActivityTypeId(link.getId()).build();
    }

    @Override // com.ua.sdk.workout.Workout
    public String getReferenceKey() {
        return this.referenceKey;
    }

    @Override // com.ua.sdk.workout.Workout
    public Boolean hasTimeSeries() {
        return this.hasTimeSeries;
    }

    @Override // com.ua.sdk.workout.Workout
    public Boolean isVerified() {
        return this.isVerified;
    }

    @Override // com.ua.sdk.workout.Workout
    public UserGearRef getUserGearRef() {
        Link link = getLink(KEY_USERGEAR);
        if (link == null) {
            return null;
        }
        return new UserGearRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.workout.Workout
    public Privacy getPrivacy() {
        Link link = getLink("privacy");
        if (link == null) {
            return null;
        }
        try {
            int id = Integer.parseInt(link.getId());
            return PrivacyHelper.getPrivacyFromId(id);
        } catch (NumberFormatException e) {
            UaLog.error("Unable to get privacy", (Throwable) e);
            return null;
        }
    }

    @Override // com.ua.sdk.Resource
    public WorkoutRef getRef() {
        Link link = getLink("self");
        if (link != null) {
            return new WorkoutRef(link.getId(), this.mLocalId, link.getHref());
        }
        if (this.mLocalId == -1) {
            return new WorkoutRef((String) null);
        }
        return new WorkoutRef(null, this.mLocalId, null);
    }

    @Override // com.ua.sdk.workout.Workout
    public WorkoutAggregates getAggregates() {
        return this.workoutAggregates;
    }

    @Override // com.ua.sdk.workout.Workout
    public TimeSeriesData getTimeSeriesData() {
        return this.timeSeries;
    }

    @Override // com.ua.sdk.workout.Workout
    public SocialSettings getSocialSettings() {
        return this.socialSettings;
    }

    @Override // com.ua.sdk.workout.Workout
    public int getAttachmentCount() {
        if (this.attachments == null) {
            return 0;
        }
        return this.attachments.getCount();
    }

    @Override // com.ua.sdk.workout.Workout
    public Attachment getAttachment(int index) throws IndexOutOfBoundsException {
        if (this.attachments == null) {
            throw new IndexOutOfBoundsException("Workout does not have any attachments.");
        }
        return this.attachments.getAttachment(index);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.startTime != null ? this.startTime.getTime() : -1L);
        dest.writeLong(this.updateTime != null ? this.updateTime.getTime() : -1L);
        dest.writeLong(this.createdTime != null ? this.createdTime.getTime() : -1L);
        dest.writeString(this.name);
        dest.writeString(this.notes);
        dest.writeSerializable(this.timeZone);
        dest.writeString(this.source);
        dest.writeString(this.referenceKey);
        dest.writeValue(this.hasTimeSeries);
        dest.writeValue(this.isVerified);
        dest.writeParcelable(this.workoutAggregates, flags);
        dest.writeParcelable(this.timeSeries, flags);
        dest.writeParcelable(this.socialSettings, flags);
        dest.writeParcelable(this.attachments, flags);
    }

    public WorkoutImpl() {
    }

    private WorkoutImpl(Parcel in) {
        super(in);
        long tmpStartTime = in.readLong();
        this.startTime = tmpStartTime == -1 ? null : new Date(tmpStartTime);
        long tmpUpdateTime = in.readLong();
        this.updateTime = tmpUpdateTime == -1 ? null : new Date(tmpUpdateTime);
        long tmpCreatedTime = in.readLong();
        this.createdTime = tmpCreatedTime != -1 ? new Date(tmpCreatedTime) : null;
        this.name = in.readString();
        this.notes = in.readString();
        this.timeZone = (TimeZone) in.readSerializable();
        this.source = in.readString();
        this.referenceKey = in.readString();
        this.hasTimeSeries = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.isVerified = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.workoutAggregates = (WorkoutAggregates) in.readParcelable(WorkoutAggregates.class.getClassLoader());
        this.timeSeries = (TimeSeriesData) in.readParcelable(TimeSeries.class.getClassLoader());
        this.socialSettings = (SocialSettings) in.readParcelable(SocialSettings.class.getClassLoader());
        this.attachments = (Attachments) in.readParcelable(Attachments.class.getClassLoader());
    }
}
