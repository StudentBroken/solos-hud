package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.pupil.ui.PageHelper;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyListRef implements EntityListRef<Actigraphy> {
    public static Parcelable.Creator<ActigraphyListRef> CREATOR = new Parcelable.Creator<ActigraphyListRef>() { // from class: com.ua.sdk.actigraphy.ActigraphyListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyListRef createFromParcel(Parcel source) {
            return new ActigraphyListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyListRef[] newArray(int size) {
            return new ActigraphyListRef[size];
        }
    };
    private String href;

    public enum TimeInterval {
        DEFAULT("0"),
        FIFTEEN_MINUTES("900"),
        THIRTY_MINUTES("1800"),
        ONE_HOUR("3600");

        private String value;

        TimeInterval(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private ActigraphyListRef(Builder init) {
        this.href = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    public static class Builder extends BaseReferenceBuilder {
        private static final String DATE_FORMAT = "yyyy-MM-dd";
        private static final int DEFAULT_FETCH_LIMIT = 40;
        private static final int DEFAULT_OFFSET = 0;
        private Date endDate;
        private int limit;
        private int offset;
        private Date startDate;
        private TimeInterval timeInterval;
        private TimeZone timeZone;
        private EntityRef<User> user;

        private Builder() {
            super(null);
            this.timeInterval = TimeInterval.DEFAULT;
            this.limit = 40;
            this.offset = 0;
        }

        public Builder setStartDate(Date startDate) {
            Precondition.isNotNull(startDate, "startDate");
            this.startDate = startDate;
            validateTimeLine();
            return this;
        }

        public Builder setEndDate(Date endDate) {
            this.endDate = endDate;
            if (endDate != null) {
                validateTimeLine();
            }
            return this;
        }

        public Builder setTimeZone(TimeZone timeZone) {
            Precondition.isNotNull(timeZone, "timeZone");
            this.timeZone = timeZone;
            return this;
        }

        public Builder setTimeZone(String timeZone) {
            Precondition.isNotNull(timeZone, "timeZone");
            return setTimeZone(TimeZone.getTimeZone(timeZone));
        }

        public Builder setTimeInterval(TimeInterval timeInterval) {
            Precondition.isNotNull(timeInterval, "timeInterval");
            this.timeInterval = timeInterval;
            return this;
        }

        public Builder setUserRef(EntityRef<User> user) {
            Precondition.isNotNull(user, "user");
            this.user = user;
            return this;
        }

        public Builder setLimit(int limit) {
            Precondition.isNotNegative(limit, "limit");
            this.limit = limit;
            return this;
        }

        public Builder setOffset(int offset) {
            Precondition.isNotNegative(offset, PageHelper.OFFSET_ATTRIBUTE);
            this.offset = offset;
            return this;
        }

        public ActigraphyListRef build() {
            Precondition.isNotNull(this.startDate, "startDate");
            if (this.timeZone == null) {
                setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
            simpleDateFormat.setTimeZone(this.timeZone);
            String start = simpleDateFormat.format(this.startDate);
            String end = null;
            if (this.endDate != null) {
                end = simpleDateFormat.format(this.endDate);
            }
            setParam(FirebaseAnalytics.Param.START_DATE, start);
            if (end != null) {
                start = end;
            }
            setParam(FirebaseAnalytics.Param.END_DATE, start);
            if (this.user != null) {
                setParam("user", this.user.getId());
            }
            if (this.timeInterval != TimeInterval.DEFAULT) {
                setParam("time_series_interval", this.timeInterval.getValue());
            }
            setParam("limit", Integer.toString(this.limit));
            setParam(PageHelper.OFFSET_ATTRIBUTE, Integer.toString(this.offset));
            return new ActigraphyListRef(this);
        }

        private void validateTimeLine() {
            if (this.startDate != null && this.endDate != null) {
                long startTime = this.startDate.getTime();
                long endTime = this.endDate.getTime();
                if (startTime > endTime) {
                    throw new IllegalStateException("the start date can not be greater than the end date.");
                }
            }
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

    private ActigraphyListRef(Parcel in) {
        this.href = in.readString();
    }
}
