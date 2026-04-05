package com.ua.sdk.user.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ua.sdk.EntityRef;
import com.ua.sdk.LocalDate;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class UserStatsRef implements EntityRef<UserStats> {
    public static Parcelable.Creator<UserStatsRef> CREATOR = new Parcelable.Creator<UserStatsRef>() { // from class: com.ua.sdk.user.stats.UserStatsRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserStatsRef createFromParcel(Parcel source) {
            return new UserStatsRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserStatsRef[] newArray(int size) {
            return new UserStatsRef[size];
        }
    };
    private String href;

    private UserStatsRef(Builder init) {
        this.href = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public static final class Builder extends BaseReferenceBuilder {
        private Builder() {
            super("v7.0/user_stats/{id}/");
        }

        public Builder setUser(EntityRef<User> user) {
            setParam("id", user.getId());
            return this;
        }

        public Builder setAggregatePeriodUserStats(AggregatePeriodUserStats aggregatePeriodUserStats) {
            setParam("aggregate_by_period", aggregatePeriodUserStats.toString());
            return this;
        }

        public Builder setStartDate(LocalDate startDate) {
            setParam(FirebaseAnalytics.Param.START_DATE, startDate.toString());
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            setParam(FirebaseAnalytics.Param.END_DATE, endDate.toString());
            return this;
        }

        public Builder setIncludeSummaries(boolean includeSummaries) {
            setParam("include_summary_stats", Boolean.toString(includeSummaries));
            return this;
        }

        public UserStatsRef build() {
            UserStatsRef userStatsRef;
            synchronized (UserStatsRef.class) {
                Precondition.isNotNull(getParam("user"));
                userStatsRef = new UserStatsRef(this);
            }
            return userStatsRef;
        }
    }

    public enum AggregatePeriodUserStats {
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        YEAR("year"),
        LIFETIME("lifetime");

        private String value;

        AggregatePeriodUserStats(String value) {
            this.value = value;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.value;
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

    private UserStatsRef(Parcel in) {
        this.href = in.readString();
    }
}
