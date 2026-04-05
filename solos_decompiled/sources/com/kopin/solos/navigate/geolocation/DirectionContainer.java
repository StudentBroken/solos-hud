package com.kopin.solos.navigate.geolocation;

import android.location.Location;
import com.kopin.solos.storage.Coordinate;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionContainer {
    public int bearing;
    public String initialDistance;
    public String instruction;
    public int theDistance;

    public static DirectionContainer CurrentStep(Waypoint t, Coordinate c) {
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), t.results);
        DirectionContainer newDC = new DirectionContainer();
        newDC.initialDistance = ((int) t.results[0]) + "";
        newDC.theDistance = (int) t.results[0];
        newDC.bearing = (int) t.results[1];
        return newDC;
    }

    public static DirectionContainer CurrentStep(Waypoint t, Location loc) {
        Location.distanceBetween(loc.getLatitude(), loc.getLongitude(), t.getLatitude(), t.getLongitude(), t.results);
        DirectionContainer newDC = new DirectionContainer();
        newDC.initialDistance = ((int) t.results[0]) + "";
        newDC.theDistance = (int) t.results[0];
        newDC.bearing = (int) t.results[1];
        return newDC;
    }
}
