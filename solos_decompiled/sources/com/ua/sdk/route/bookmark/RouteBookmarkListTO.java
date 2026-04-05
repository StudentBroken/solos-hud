package com.ua.sdk.route.bookmark;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.cache.EntityDatabase;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkListTO {
    private static final String LIST_KEY = "route_bookmarks";

    @SerializedName("_embedded")
    Map<String, List<RouteBookmarkTO>> embedded;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    Integer totalCount;

    public List<RouteBookmarkTO> getRouteBookmarks() {
        return this.embedded.get(LIST_KEY);
    }

    public static RouteBookmarkList fromTransferObject(RouteBookmarkListTO to) {
        RouteBookmarkList list = null;
        List<RouteBookmarkTO> toList = to.getRouteBookmarks();
        if (toList != null) {
            list = new RouteBookmarkList();
            for (RouteBookmarkTO obj : toList) {
                list.add(RouteBookmarkTO.fromTransferObject(obj));
            }
        }
        return list;
    }
}
