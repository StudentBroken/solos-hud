package com.ua.sdk.group.leaderboard;

import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.UaException;

/* JADX INFO: loaded from: classes65.dex */
public interface GroupLeaderboardManager {
    EntityList<GroupLeaderboard> fetchGroupLeaderboardList(EntityListRef<GroupLeaderboard> entityListRef) throws UaException;

    Request fetchGroupLeaderboardList(EntityListRef<GroupLeaderboard> entityListRef, FetchCallback<EntityList<GroupLeaderboard>> fetchCallback);
}
