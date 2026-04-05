package com.ua.sdk.user.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class UserStatsImpl extends ApiTransferObject implements UserStats {
    public static Parcelable.Creator<UserStatsImpl> CREATOR = new Parcelable.Creator<UserStatsImpl>() { // from class: com.ua.sdk.user.stats.UserStatsImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserStatsImpl createFromParcel(Parcel source) {
            return new UserStatsImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserStatsImpl[] newArray(int size) {
            return new UserStatsImpl[size];
        }
    };

    @SerializedName("_embedded")
    Map<String, ArrayList<Stats>> embeddedStats;
    transient ArrayList<Stats> stats;
    transient ArrayList<Stats> summaryStats;

    public UserStatsImpl() {
    }

    @Override // com.ua.sdk.user.stats.UserStats
    public EntityRef<User> getUserRef() {
        Link link = getLink("user");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.user.stats.UserStats
    public List<Stats> getStats() {
        if (this.embeddedStats == null) {
            return null;
        }
        if (this.stats == null) {
            this.stats = this.embeddedStats.get("stats");
        }
        return this.stats;
    }

    @Override // com.ua.sdk.user.stats.UserStats
    public boolean hasSummaryStats() {
        if (this.embeddedStats == null) {
            return false;
        }
        return this.embeddedStats.containsKey("summary_stats");
    }

    @Override // com.ua.sdk.user.stats.UserStats
    public List<Stats> getSummaryStats() {
        if (!hasSummaryStats()) {
            return null;
        }
        if (this.summaryStats == null) {
            this.summaryStats = this.embeddedStats.get("summary_stats");
        }
        return this.summaryStats;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.stats != null ? this.stats : new ArrayList<>(0));
        dest.writeList(this.summaryStats != null ? this.summaryStats : new ArrayList<>(0));
    }

    private UserStatsImpl(Parcel in) {
        this.stats = new ArrayList<>();
        in.readList(this.stats, Stats.class.getClassLoader());
        this.stats = this.stats.size() == 0 ? null : this.stats;
        this.summaryStats = new ArrayList<>();
        in.readList(this.summaryStats, Stats.class.getClassLoader());
        this.summaryStats = this.summaryStats.size() == 0 ? null : this.summaryStats;
        this.embeddedStats = this.stats != null ? new HashMap() : null;
        if (this.embeddedStats != null) {
            this.embeddedStats.put("stats", this.stats);
            if (this.summaryStats != null) {
                this.embeddedStats.put("summary_stats", this.summaryStats);
            }
        }
    }
}
