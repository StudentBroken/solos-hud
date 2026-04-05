package com.kopin.solos.navigate.geolocation;

import com.kopin.solos.storage.Coordinate;

/* JADX INFO: loaded from: classes47.dex */
public class RouteMarker {
    private boolean isOnRoute;
    private final NavigationRoute mRoute;
    private double curLat = 0.0d;
    private double curLong = 0.0d;
    private int nearestCoordIdx = -1;
    private int lastWaypointIdx = -1;
    private int nextWaypointIdx = 0;

    public RouteMarker(NavigationRoute route) {
        this.mRoute = route;
    }

    public void setLocation(Coordinate coord) {
        this.curLat = coord.getLatitude();
        this.curLong = coord.getLongitude();
    }

    public boolean isOnRoute() {
        return this.isOnRoute;
    }
}
