package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class RouteList extends AbstractEntityList<Route> {
    public static final Parcelable.Creator<RouteList> CREATOR = new Parcelable.Creator<RouteList>() { // from class: com.ua.sdk.route.RouteList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteList createFromParcel(Parcel source) {
            return new RouteList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteList[] newArray(int size) {
            return new RouteList[size];
        }
    };
    public static final String LIST_KEY = "routes";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "routes";
    }

    public RouteList() {
    }

    private RouteList(Parcel in) {
    }
}
