package com.ua.sdk.group.leaderboard;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;
import com.ua.sdk.internal.Link;

/* JADX INFO: loaded from: classes65.dex */
public class GroupLeaderboardList extends AbstractEntityList<GroupLeaderboard> {
    public static Parcelable.Creator<GroupLeaderboardList> CREATOR = new Parcelable.Creator<GroupLeaderboardList>() { // from class: com.ua.sdk.group.leaderboard.GroupLeaderboardList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupLeaderboardList createFromParcel(Parcel source) {
            return new GroupLeaderboardList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupLeaderboardList[] newArray(int size) {
            return new GroupLeaderboardList[size];
        }
    };
    private static final String LIST_KEY = "aggregates";
    private static final String NEXT_ITERATION_KEY = "next_iteration";
    private static final String PREVIOUS_ITERATION_KEY = "prev_iteration";
    transient Link nextIteration;
    transient Link previousIteration;

    public GroupLeaderboardList() {
    }

    protected GroupLeaderboardList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public Link getPreviousIteration() {
        if (this.previousIteration == null) {
            this.previousIteration = getLink(PREVIOUS_ITERATION_KEY);
        }
        return this.previousIteration;
    }

    public Link getNextIteration() {
        if (this.nextIteration == null) {
            this.nextIteration = getLink(NEXT_ITERATION_KEY);
        }
        return this.nextIteration;
    }
}
