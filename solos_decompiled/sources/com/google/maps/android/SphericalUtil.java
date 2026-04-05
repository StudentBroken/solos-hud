package com.google.maps.android;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/* JADX INFO: loaded from: classes69.dex */
public class SphericalUtil {
    private SphericalUtil() {
    }

    public static double computeHeading(LatLng from, LatLng to) {
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double toLat = Math.toRadians(to.latitude);
        double toLng = Math.toRadians(to.longitude);
        double dLng = toLng - fromLng;
        double heading = Math.atan2(Math.sin(dLng) * Math.cos(toLat), (Math.cos(fromLat) * Math.sin(toLat)) - ((Math.sin(fromLat) * Math.cos(toLat)) * Math.cos(dLng)));
        return MathUtil.wrap(Math.toDegrees(heading), -180.0d, 180.0d);
    }

    public static LatLng computeOffset(LatLng from, double distance, double heading) {
        double distance2 = distance / 6371009.0d;
        double heading2 = Math.toRadians(heading);
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double cosDistance = Math.cos(distance2);
        double sinDistance = Math.sin(distance2);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = (cosDistance * sinFromLat) + (sinDistance * cosFromLat * Math.cos(heading2));
        double dLng = Math.atan2(sinDistance * cosFromLat * Math.sin(heading2), cosDistance - (sinFromLat * sinLat));
        return new LatLng(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }

    public static LatLng computeOffsetOrigin(LatLng to, double distance, double heading) {
        double heading2 = Math.toRadians(heading);
        double distance2 = distance / 6371009.0d;
        double n1 = Math.cos(distance2);
        double n2 = Math.sin(distance2) * Math.cos(heading2);
        double n3 = Math.sin(distance2) * Math.sin(heading2);
        double n4 = Math.sin(Math.toRadians(to.latitude));
        double n12 = n1 * n1;
        double discriminant = (((n2 * n2) * n12) + (n12 * n12)) - ((n12 * n4) * n4);
        if (discriminant < 0.0d) {
            return null;
        }
        double b = ((n2 * n4) + Math.sqrt(discriminant)) / ((n1 * n1) + (n2 * n2));
        double a = (n4 - (n2 * b)) / n1;
        double fromLatRadians = Math.atan2(a, b);
        if (fromLatRadians < -1.5707963267948966d || fromLatRadians > 1.5707963267948966d) {
            fromLatRadians = Math.atan2(a, ((n2 * n4) - Math.sqrt(discriminant)) / ((n1 * n1) + (n2 * n2)));
        }
        if (fromLatRadians < -1.5707963267948966d || fromLatRadians > 1.5707963267948966d) {
            return null;
        }
        double fromLngRadians = Math.toRadians(to.longitude) - Math.atan2(n3, (Math.cos(fromLatRadians) * n1) - (Math.sin(fromLatRadians) * n2));
        return new LatLng(Math.toDegrees(fromLatRadians), Math.toDegrees(fromLngRadians));
    }

    public static LatLng interpolate(LatLng from, LatLng to, double fraction) {
        double fromLat = Math.toRadians(from.latitude);
        double fromLng = Math.toRadians(from.longitude);
        double toLat = Math.toRadians(to.latitude);
        double toLng = Math.toRadians(to.longitude);
        double cosFromLat = Math.cos(fromLat);
        double cosToLat = Math.cos(toLat);
        double angle = computeAngleBetween(from, to);
        double sinAngle = Math.sin(angle);
        if (sinAngle >= 1.0E-6d) {
            double a = Math.sin((1.0d - fraction) * angle) / sinAngle;
            double b = Math.sin(fraction * angle) / sinAngle;
            double x = (a * cosFromLat * Math.cos(fromLng)) + (b * cosToLat * Math.cos(toLng));
            double y = (a * cosFromLat * Math.sin(fromLng)) + (b * cosToLat * Math.sin(toLng));
            double z = (Math.sin(fromLat) * a) + (Math.sin(toLat) * b);
            double lat = Math.atan2(z, Math.sqrt((x * x) + (y * y)));
            double lng = Math.atan2(y, x);
            return new LatLng(Math.toDegrees(lat), Math.toDegrees(lng));
        }
        return from;
    }

    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return MathUtil.arcHav(MathUtil.havDistance(lat1, lat2, lng1 - lng2));
    }

    static double computeAngleBetween(LatLng from, LatLng to) {
        return distanceRadians(Math.toRadians(from.latitude), Math.toRadians(from.longitude), Math.toRadians(to.latitude), Math.toRadians(to.longitude));
    }

    public static double computeDistanceBetween(LatLng from, LatLng to) {
        return computeAngleBetween(from, to) * 6371009.0d;
    }

    public static double computeLength(List<LatLng> path) {
        if (path.size() < 2) {
            return 0.0d;
        }
        double length = 0.0d;
        LatLng prev = path.get(0);
        double prevLat = Math.toRadians(prev.latitude);
        double prevLng = Math.toRadians(prev.longitude);
        for (LatLng point : path) {
            double lat = Math.toRadians(point.latitude);
            double lng = Math.toRadians(point.longitude);
            length += distanceRadians(prevLat, prevLng, lat, lng);
            prevLat = lat;
            prevLng = lng;
        }
        return 6371009.0d * length;
    }

    public static double computeArea(List<LatLng> path) {
        return Math.abs(computeSignedArea(path));
    }

    public static double computeSignedArea(List<LatLng> path) {
        return computeSignedArea(path, 6371009.0d);
    }

    static double computeSignedArea(List<LatLng> path, double radius) {
        int size = path.size();
        if (size < 3) {
            return 0.0d;
        }
        double total = 0.0d;
        LatLng prev = path.get(size - 1);
        double prevTanLat = Math.tan((1.5707963267948966d - Math.toRadians(prev.latitude)) / 2.0d);
        double prevLng = Math.toRadians(prev.longitude);
        for (LatLng point : path) {
            double tanLat = Math.tan((1.5707963267948966d - Math.toRadians(point.latitude)) / 2.0d);
            double lng = Math.toRadians(point.longitude);
            total += polarTriangleArea(tanLat, lng, prevTanLat, prevLng);
            prevTanLat = tanLat;
            prevLng = lng;
        }
        return radius * radius * total;
    }

    private static double polarTriangleArea(double tan1, double lng1, double tan2, double lng2) {
        double deltaLng = lng1 - lng2;
        double t = tan1 * tan2;
        return 2.0d * Math.atan2(Math.sin(deltaLng) * t, 1.0d + (Math.cos(deltaLng) * t));
    }
}
