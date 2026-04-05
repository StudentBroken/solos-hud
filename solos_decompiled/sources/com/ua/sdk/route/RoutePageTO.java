package com.ua.sdk.route;

import com.google.gson.annotations.SerializedName;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.internal.ApiTransferObject;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class RoutePageTO extends ApiTransferObject {
    public static final String KEY_ROUTES = "routes";

    @SerializedName("_embedded")
    public Map<String, ArrayList<RouteTO>> routes;

    @SerializedName(EntityDatabase.LIST.COLS.TOTAL_COUNT)
    public Integer totalRoutesCount;

    private ArrayList<RouteTO> getRoutesList() {
        if (this.routes == null) {
            return null;
        }
        return this.routes.get("routes");
    }

    public static RouteList fromTransferObject(RoutePageTO to) {
        RouteList page = new RouteList();
        ArrayList<RouteTO> routeTOs = to.getRoutesList();
        for (RouteTO routeTO : routeTOs) {
            RouteImpl route = RouteTO.fromTransferObject(routeTO);
            page.add(route);
        }
        page.setLinkMap(to.getLinkMap());
        page.setTotalCount(to.totalRoutesCount.intValue());
        return page;
    }
}
