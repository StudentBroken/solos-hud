package com.ua.sdk.route;

import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes65.dex */
public interface RouteBuilder extends Parcelable {
    Route build();

    RouteBuilder setDescription(String str);

    RouteBuilder setName(String str);

    RouteBuilder setPoints(ArrayList<Point> arrayList);

    RouteBuilder setPostalCode(String str);

    RouteBuilder setStartPointType(String str);
}
