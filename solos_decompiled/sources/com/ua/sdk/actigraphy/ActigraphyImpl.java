package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyImpl extends ApiTransferObject implements Actigraphy, Parcelable {
    public static Parcelable.Creator<ActigraphyImpl> CREATOR = new Parcelable.Creator<ActigraphyImpl>() { // from class: com.ua.sdk.actigraphy.ActigraphyImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyImpl createFromParcel(Parcel source) {
            return new ActigraphyImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyImpl[] newArray(int size) {
            return new ActigraphyImpl[size];
        }
    };
    protected static final String REF_USER = "user";
    private ActigraphyAggregatesImpl mActigraphyAggregatesImpl;
    private ActigraphyMetricsImpl mActigraphyMetricsImpl;
    private LocalDate mDate;
    private Date mEndDateTime;
    private Date mStartDateTime;
    private TimeZone mTimeZone;
    private WorkoutSummaryImpl[] mWorkoutSummaries;

    protected ActigraphyImpl() {
    }

    public void setAggregates(ActigraphyAggregatesImpl aggregates) {
        this.mActigraphyAggregatesImpl = aggregates;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Actigraphy> getRef() {
        if (this.mStartDateTime != null) {
            return new LinkEntityRef(String.valueOf(this.mStartDateTime.getTime()), null);
        }
        return null;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public EntityRef<User> getUserRef() {
        List<Link> userLinks = getLinks("user");
        if (userLinks == null || userLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(userLinks.get(0).getId(), userLinks.get(0).getHref());
    }

    public void setMetrics(ActigraphyMetricsImpl metrics) {
        this.mActigraphyMetricsImpl = metrics;
    }

    public void setWorkoutSummaries(WorkoutSummaryImpl[] workoutSummaries) {
        this.mWorkoutSummaries = workoutSummaries;
    }

    public void setDate(LocalDate date) {
        this.mDate = date;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.mTimeZone = timeZone;
    }

    public void setStartDateTime(Date startDateTime) {
        this.mStartDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.mEndDateTime = endDateTime;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public ActigraphyAggregatesImpl getActigraphyAggregates() {
        return this.mActigraphyAggregatesImpl;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public ActigraphyMetricsImpl getMetrics() {
        return this.mActigraphyMetricsImpl;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public WorkoutSummaryImpl[] getWorkoutSummaries() {
        return this.mWorkoutSummaries;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public LocalDate getDate() {
        return this.mDate;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
    public Date getStartDateTime() {
        return this.mStartDateTime;
    }

    @Override // com.ua.sdk.actigraphy.Actigraphy
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
        dest.writeParcelable(this.mActigraphyAggregatesImpl, flags);
        dest.writeParcelable(this.mActigraphyMetricsImpl, flags);
        dest.writeParcelableArray(this.mWorkoutSummaries, flags);
        dest.writeParcelable(this.mDate, flags);
        dest.writeString(this.mTimeZone != null ? this.mTimeZone.getID() : null);
        dest.writeLong(this.mStartDateTime != null ? this.mStartDateTime.getTime() : -1L);
        dest.writeLong(this.mEndDateTime != null ? this.mEndDateTime.getTime() : -1L);
    }

    private ActigraphyImpl(Parcel in) {
        super(in);
        this.mActigraphyAggregatesImpl = (ActigraphyAggregatesImpl) in.readParcelable(ActigraphyAggregatesImpl.class.getClassLoader());
        this.mActigraphyMetricsImpl = (ActigraphyMetricsImpl) in.readParcelable(ActigraphyMetricsImpl.class.getClassLoader());
        Parcelable[] tempWorkoutSummaries = in.readParcelableArray(WorkoutSummaryImpl.class.getClassLoader());
        if (tempWorkoutSummaries != null) {
            this.mWorkoutSummaries = new WorkoutSummaryImpl[tempWorkoutSummaries.length];
            System.arraycopy(tempWorkoutSummaries, 0, this.mWorkoutSummaries, 0, tempWorkoutSummaries.length);
        } else {
            this.mWorkoutSummaries = null;
        }
        this.mDate = (LocalDate) in.readParcelable(LocalDate.class.getClassLoader());
        String timeZone = in.readString();
        this.mTimeZone = (timeZone == null || timeZone.length() == 0) ? null : TimeZone.getTimeZone(timeZone);
        Long tmpStartDateTime = Long.valueOf(in.readLong());
        this.mStartDateTime = tmpStartDateTime.longValue() == -1 ? null : new Date(tmpStartDateTime.longValue());
        Long tmpEndDateTime = Long.valueOf(in.readLong());
        this.mEndDateTime = tmpEndDateTime.longValue() != -1 ? new Date(tmpEndDateTime.longValue()) : null;
    }
}
