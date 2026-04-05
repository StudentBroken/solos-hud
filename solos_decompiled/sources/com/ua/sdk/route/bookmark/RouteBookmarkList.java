package com.ua.sdk.route.bookmark;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;
import com.ua.sdk.route.RouteBookmark;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkList extends AbstractEntityList<RouteBookmark> {
    public static Parcelable.Creator<RouteBookmarkList> CREATOR = new Parcelable.Creator<RouteBookmarkList>() { // from class: com.ua.sdk.route.bookmark.RouteBookmarkList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteBookmarkList createFromParcel(Parcel source) {
            return new RouteBookmarkList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteBookmarkList[] newArray(int size) {
            return new RouteBookmarkList[size];
        }
    };
    private static final String LIST_KEY = "route_bookmarks";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }

    public RouteBookmarkList() {
    }

    private RouteBookmarkList(Parcel in) {
        super(in);
    }
}
