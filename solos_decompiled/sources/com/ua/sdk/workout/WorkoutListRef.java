package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.oss.org.codehaus.jackson.map.util.Iso8601DateFormat;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutListRef implements EntityListRef<Workout> {
    public static final Parcelable.Creator<WorkoutListRef> CREATOR = new Parcelable.Creator<WorkoutListRef>() { // from class: com.ua.sdk.workout.WorkoutListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutListRef createFromParcel(Parcel source) {
            return new WorkoutListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutListRef[] newArray(int size) {
            return new WorkoutListRef[size];
        }
    };
    private String href;

    public enum WorkoutOrder {
        CHRONOLOGICAL,
        LATEST_FIRST
    }

    public WorkoutListRef(String href) {
        this.href = href;
    }

    private WorkoutListRef(Builder init) {
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private static final String KEY_ACTIVITY_TYPE = "activity_type";
        private static final String KEY_ORDER_BY = "order_by";
        private static final String KEY_STARTED_AFTER = "started_after";
        private static final String KEY_STARTED_BEFORE = "started_before";
        private static final String KEY_USER = "user";
        private Date startedAfter;
        private Date startedBefore;
        private EntityRef<User> user;

        private Builder() {
            super(UrlBuilderImpl.GET_WORKOUTS_URL);
        }

        public Builder setActivityTypes(EntityRef<ActivityType>... activityTypes) {
            if (activityTypes != null) {
                String[] activityTypeIds = new String[activityTypes.length];
                for (int i = 0; i < activityTypes.length; i++) {
                    activityTypeIds[i] = activityTypes[i].getId();
                }
                setParams(KEY_ACTIVITY_TYPE, activityTypeIds);
            } else {
                setParams(KEY_ACTIVITY_TYPE, (String[]) null);
            }
            return this;
        }

        public Builder setStartedAfter(Date startedAfter) {
            if (startedAfter != null) {
                setParam(KEY_STARTED_AFTER, Iso8601DateFormat.format(startedAfter));
                this.startedAfter = startedAfter;
            }
            return this;
        }

        public Builder setStartedBefore(Date startedBefore) {
            if (startedBefore != null) {
                setParam(KEY_STARTED_BEFORE, Iso8601DateFormat.format(startedBefore));
                this.startedBefore = startedBefore;
            }
            return this;
        }

        public Builder setUser(EntityRef<User> user) {
            if (user != null) {
                this.user = user;
                setParam("user", user.getId());
            } else {
                setParam("user", (String) null);
            }
            return this;
        }

        public Builder setWorkoutOrder(WorkoutOrder order) {
            if (order == WorkoutOrder.LATEST_FIRST) {
                setParam(KEY_ORDER_BY, "-start_datetime");
            }
            return this;
        }

        public WorkoutListRef build() {
            if (this.user == null) {
                throw new IllegalArgumentException("Must call setUser before building.");
            }
            if (this.startedAfter != null && this.startedBefore != null) {
                Precondition.check(this.startedAfter.getTime() < this.startedBefore.getTime(), "Started after not should be less than started before date");
            }
            return new WorkoutListRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    private WorkoutListRef(Parcel in) {
        this.href = in.readString();
    }
}
