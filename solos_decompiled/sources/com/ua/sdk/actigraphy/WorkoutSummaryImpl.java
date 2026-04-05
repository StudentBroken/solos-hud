package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.workout.WorkoutSummary;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutSummaryImpl extends ApiTransferObject implements WorkoutSummary, Parcelable {
    public static Parcelable.Creator<WorkoutSummaryImpl> CREATOR = new Parcelable.Creator<WorkoutSummaryImpl>() { // from class: com.ua.sdk.actigraphy.WorkoutSummaryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutSummaryImpl createFromParcel(Parcel source) {
            return new WorkoutSummaryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutSummaryImpl[] newArray(int size) {
            return new WorkoutSummaryImpl[size];
        }
    };
    private ActigraphyAggregatesImpl mActigraphyAggregates;
    private int mActivityTypeId;
    private Date mEndDateTime;
    private String mName;
    private Date mStartDateTime;

    protected WorkoutSummaryImpl() {
    }

    protected WorkoutSummaryImpl(int activityTypeId, String name, ActigraphyAggregatesImpl actigraphyAggregates, Date startDateTime, Date endDateTime) {
        this.mActivityTypeId = activityTypeId;
        this.mName = name;
        this.mActigraphyAggregates = actigraphyAggregates;
        this.mStartDateTime = startDateTime;
        this.mEndDateTime = endDateTime;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<WorkoutSummary> getRef() {
        return null;
    }

    public void setActivityTypeId(int activityTypeId) {
        this.mActivityTypeId = activityTypeId;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setWorkoutAggregates(ActigraphyAggregatesImpl actigraphyAggregatesImpl) {
        this.mActigraphyAggregates = actigraphyAggregatesImpl;
    }

    public void setStartDateTime(Date startDateTime) {
        this.mStartDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.mEndDateTime = endDateTime;
    }

    @Override // com.ua.sdk.workout.WorkoutSummary
    public int getActivityTypeId() {
        return this.mActivityTypeId;
    }

    @Override // com.ua.sdk.workout.WorkoutSummary
    public String getName() {
        return this.mName;
    }

    @Override // com.ua.sdk.workout.WorkoutSummary
    public ActigraphyAggregatesImpl getWorkoutAggregates() {
        return this.mActigraphyAggregates;
    }

    @Override // com.ua.sdk.workout.WorkoutSummary
    public Date getStartDateTime() {
        return this.mStartDateTime;
    }

    @Override // com.ua.sdk.workout.WorkoutSummary
    public Date getEndDateTime() {
        return this.mEndDateTime;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mActivityTypeId);
        dest.writeString(this.mName);
        dest.writeParcelable(this.mActigraphyAggregates, flags);
        dest.writeLong(this.mStartDateTime == null ? -1L : this.mStartDateTime.getTime());
        dest.writeLong(this.mEndDateTime != null ? this.mEndDateTime.getTime() : -1L);
    }

    private WorkoutSummaryImpl(Parcel in) {
        super(in);
        this.mActivityTypeId = in.readInt();
        this.mName = in.readString();
        this.mActigraphyAggregates = (ActigraphyAggregatesImpl) in.readParcelable(ActigraphyAggregatesImpl.class.getClassLoader());
        Long tmpStartDateTime = Long.valueOf(in.readLong());
        this.mStartDateTime = tmpStartDateTime.longValue() == -1 ? null : new Date(tmpStartDateTime.longValue());
        Long tmpEndDateTime = Long.valueOf(in.readLong());
        this.mEndDateTime = tmpEndDateTime.longValue() != -1 ? new Date(tmpEndDateTime.longValue()) : null;
    }
}
