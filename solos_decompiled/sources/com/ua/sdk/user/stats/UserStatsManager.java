package com.ua.sdk.user.stats;

import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface UserStatsManager {
    Request fetchUserStats(UserStatsRef userStatsRef, FetchCallback<UserStats> fetchCallback);

    UserStats fetchUserStats(UserStatsRef userStatsRef) throws UaException;
}
