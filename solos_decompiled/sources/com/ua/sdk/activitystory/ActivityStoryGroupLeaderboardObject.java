package com.ua.sdk.activitystory;

import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryGroupLeaderboardObject extends ActivityStoryObject {
    Date getEndTime();

    List<ActivityStoryGroupLeaderboard> getLeaderboard();

    ActivityStoryGroupLeaderboard getResult();

    Date getStartTime();
}
