package com.ua.sdk.group.leaderboard;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.group.Group;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class GroupLeaderboardListRef implements EntityListRef<GroupLeaderboard> {
    public static Parcelable.Creator<GroupLeaderboardListRef> CREATOR = new Parcelable.Creator<GroupLeaderboardListRef>() { // from class: com.ua.sdk.group.leaderboard.GroupLeaderboardListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupLeaderboardListRef createFromParcel(Parcel source) {
            return new GroupLeaderboardListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupLeaderboardListRef[] newArray(int size) {
            return new GroupLeaderboardListRef[size];
        }
    };
    private final String href;

    private GroupLeaderboardListRef(Parcel in) {
        this.href = in.readString();
    }

    private GroupLeaderboardListRef(Builder init) {
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public static class Builder extends BaseReferenceBuilder {
        private static final String END_DATE_KEY = "end_datetime";
        private static final String GROUP_ID_KEY = "group_id";
        private static final String ITERATION_KEY = "iteration";
        private static final String START_DATE_KEY = "start_datetime";
        Date endDate;
        String groupId;
        Integer iteration;
        Date startDate;

        public Builder() {
            super(UrlBuilderImpl.GET_GROUP_LEADERBOARD_LIST_URL);
        }

        public Builder setStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setGroup(EntityRef<Group> groupRef) {
            this.groupId = groupRef.getId();
            return this;
        }

        public Builder setIteration(int iteration) {
            this.iteration = Integer.valueOf(iteration);
            return this;
        }

        public GroupLeaderboardListRef build() {
            Precondition.isNotNull(this.groupId);
            setParam("group_id", this.groupId);
            if (this.iteration != null) {
                setParam(ITERATION_KEY, this.iteration.intValue());
                return new GroupLeaderboardListRef(this);
            }
            Precondition.isNotNull(this.startDate);
            Precondition.isNotNull(this.endDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            setParam(START_DATE_KEY, dateFormat.format(this.startDate));
            setParam(END_DATE_KEY, dateFormat.format(this.endDate));
            return new GroupLeaderboardListRef(this);
        }
    }
}
