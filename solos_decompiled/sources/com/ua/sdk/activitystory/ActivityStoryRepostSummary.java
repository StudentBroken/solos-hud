package com.ua.sdk.activitystory;

import android.os.Parcelable;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryRepostSummary extends Parcelable {
    List<ActivityStory> getItems();

    int getTotalCount();

    boolean isReposted();
}
