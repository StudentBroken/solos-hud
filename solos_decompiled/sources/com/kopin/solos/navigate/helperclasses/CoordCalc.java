package com.kopin.solos.navigate.helperclasses;

import android.location.Location;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.storage.Coordinate;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class CoordCalc {
    public static final double DEG2RAD = 0.017453292519943295d;
    private static final double MAX_AVERAGING_DISTANCE = 20.0d;
    public static final int RADIUS_OF_EARTH = 6378137;

    public static Coordinate GetNewPoint(Coordinate here, double bearing, double distance) {
        double lat1 = Math.toRadians(here.getLatitude());
        double lng1 = Math.toRadians(here.getLongitude());
        double brng = Math.toRadians(bearing);
        double lat2 = Math.asin((Math.sin(lat1) * Math.cos(distance / 6378137.0d)) + (Math.cos(lat1) * Math.sin(distance / 6378137.0d) * Math.cos(brng)));
        double lng2 = ((3.141592653589793d + (lng1 + Math.atan2((Math.sin(brng) * Math.sin(distance / 6378137.0d)) * Math.cos(lat1), Math.cos(distance / 6378137.0d) - (Math.sin(lat1) * Math.sin(lat2))))) % 6.283185307179586d) - 3.141592653589793d;
        Coordinate newLocation = new Coordinate(0.0d, 0.0d);
        if (lat2 == 0.0d || lng2 == 0.0d) {
            newLocation.setLatitude(0.0d);
            newLocation.setLongitude(0.0d);
        } else {
            newLocation.setLatitude(Math.toDegrees(lat2));
            newLocation.setLongitude(Math.toDegrees(lng2));
        }
        return newLocation;
    }

    private static float normaliseBearing(float bearing) {
        if (bearing < -180.0f) {
            return bearing + 360.0f;
        }
        if (bearing > 180.0f) {
            return 360.0f - bearing;
        }
        return bearing;
    }

    public static float GetBearing(Coordinate t, Location location) {
        float[] results = new float[2];
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        return results[1];
    }

    public static float GetDistance(Location t, Location c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        return results[0];
    }

    public static float GetBearing(Waypoint t, Location location) {
        Location.distanceBetween(location.getLatitude(), location.getLongitude(), t.getLatitude(), t.getLongitude(), t.results);
        return t.results[1];
    }

    public static float GetBearing(Waypoint t, Coordinate c) {
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), t.results);
        return normaliseBearing(t.results[1]);
    }

    public static float GetDistance(Waypoint t, Waypoint c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        return results[0];
    }

    public static float GetDistance(Coordinate t, Waypoint c) {
        return GetDistance(t, c);
    }

    public static float GetDistance(Coordinate t, Coordinate c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        return results[0];
    }

    public static float GetDistance(Location t, Coordinate c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        return results[0];
    }

    public static float[] GetDetails(Location t, Location c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        int theBearing = (int) results[1];
        if (theBearing < 0) {
            theBearing += 360;
        }
        int theBearing2 = theBearing - 90;
        if (theBearing2 < 0) {
            int i = theBearing2 + 360;
        }
        return results;
    }

    public static int[] getDistanceResults(Coordinate to, Coordinate from) {
        float[] results = new float[3];
        Location.distanceBetween(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude(), results);
        int theBearing = (int) results[1];
        int[] returner = {(int) results[0], theBearing};
        return returner;
    }

    public static int[] GetDistanceResults(Location t, Location c) {
        float[] results = new float[3];
        Location.distanceBetween(c.getLatitude(), c.getLongitude(), t.getLatitude(), t.getLongitude(), results);
        int theBearing = (int) results[1];
        int[] returner = {(int) results[0], theBearing};
        return returner;
    }

    public static int findClosestCoordinate(Location from, List<Coordinate> coords) {
        if (coords == null || coords.isEmpty()) {
            return -1;
        }
        int closest = -1;
        double lastDist = 0.0d;
        for (int i = 0; i < coords.size(); i++) {
            Coordinate coord = coords.get(i);
            double dist = GetDistance(from, coord);
            if (lastDist == 0.0d || dist < lastDist) {
                closest = i;
                lastDist = dist;
            }
        }
        return closest;
    }

    public static double getAverageBearingForwards(List<Coordinate> coords, int start, int count) {
        double bearing = 0.0d;
        double distance = 0.0d;
        if (coords.size() <= start + 1) {
            return 0.0d;
        }
        Coordinate prev = coords.get(start);
        int i = start + 1;
        while (i < coords.size() && i < start + count) {
            Coordinate coord = coords.get(i);
            int[] heading = getDistanceResults(prev, coord);
            distance += (double) heading[0];
            if (distance > MAX_AVERAGING_DISTANCE) {
                break;
            }
            bearing += (double) heading[1];
            i++;
        }
        return bearing / ((double) (i - start));
    }

    public static double getAverageBearingBackwards(List<Coordinate> coords, int start, int count) {
        double bearing = 0.0d;
        double distance = 0.0d;
        if (coords.size() <= start) {
            return 0.0d;
        }
        Coordinate prev = coords.get(start);
        int i = start - 1;
        while (i >= 0 && i > start - count) {
            Coordinate coord = coords.get(i);
            int[] heading = getDistanceResults(prev, coord);
            distance += (double) heading[0];
            if (distance > MAX_AVERAGING_DISTANCE) {
                break;
            }
            bearing += (double) heading[1];
            i--;
        }
        return bearing / ((double) (start - i));
    }
}
