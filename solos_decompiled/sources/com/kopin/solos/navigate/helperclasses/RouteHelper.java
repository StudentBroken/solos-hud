package com.kopin.solos.navigate.helperclasses;

import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.storage.Coordinate;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class RouteHelper {
    private static final double AVERAGED_BEARING_MAX_TOLERANCE = 10.0d;
    private static final int SHORT_TRIP_MAX_TOLERANCE = 50;
    private static final int SHORT_TRIP_MIN_TOLERANCE = 2;

    public static double diffBearing(double a, double b) {
        if (a < 0.0d) {
            a += 360.0d;
        }
        if (b < 0.0d) {
            b += 360.0d;
        }
        double diff = a - b;
        if (diff < -180.0d) {
            return diff + 360.0d;
        }
        if (diff > 180.0d) {
            return 360.0d - diff;
        }
        return diff;
    }

    public static boolean checkBearing(double bearing, double limit) {
        return bearing > limit && bearing < 360.0d - limit;
    }

    public static List<Coordinate> smoothCoords(List<Coordinate> coords) {
        List<Coordinate> smoothed = new ArrayList<>();
        if (coords.size() >= 5) {
            smoothed.add(coords.get(0));
            Coordinate prev = coords.get(0);
            for (int i = 0; i < coords.size(); i++) {
                Coordinate check = coords.get(i);
                double runningLat = 0.0d;
                double runningLong = 0.0d;
                int count = 0;
                for (int j = -1; j < 2; j++) {
                    int idx = i + j;
                    if (idx < 0) {
                        idx = 0;
                    }
                    if (idx >= coords.size()) {
                        idx = coords.size() - 1;
                    }
                    Coordinate next = coords.get(idx);
                    double dist = CoordCalc.GetDistance(check, next);
                    if (dist < 50.0d) {
                        runningLat += next.getLatitude();
                        runningLong += next.getLongitude();
                        count++;
                    }
                }
                Coordinate avg = new Coordinate(runningLat / ((double) count), runningLong / ((double) count));
                if (prev != null) {
                    double dist2 = CoordCalc.GetDistance(avg, prev);
                    if (dist2 > 2.0d) {
                        smoothed.add(avg);
                    }
                }
                prev = avg;
            }
        }
        return smoothed;
    }

    public static List<Waypoint> createWaypoints(List<Coordinate> coords) {
        List<Waypoint> waypoints = new ArrayList<>();
        if (coords.size() >= 5) {
            Coordinate prev = coords.get(0);
            double distanceTravelled = 0.0d;
            Waypoint first = new Waypoint(prev);
            first.isStart = true;
            waypoints.add(first);
            Waypoint last = first;
            for (int i = 1; i < coords.size(); i++) {
                double prevBearing = CoordCalc.getAverageBearingBackwards(coords, i, 3);
                double afterBearing = CoordCalc.getAverageBearingForwards(coords, i, 3);
                Coordinate to = coords.get(i);
                distanceTravelled += (double) CoordCalc.GetDistance(prev, to);
                double deltaHeading = Math.abs(prevBearing - afterBearing);
                if (checkBearing(deltaHeading, 10.0d)) {
                    Waypoint next = new Waypoint(to);
                    next.distance = (int) distanceTravelled;
                    next.bearing = (int) CoordCalc.GetBearing(last, to);
                    waypoints.add(next);
                    distanceTravelled = 0.0d;
                    last = next;
                }
                prev = to;
            }
            Waypoint last2 = new Waypoint(prev);
            last2.isEnd = true;
            waypoints.add(last2);
        }
        return waypoints;
    }
}
