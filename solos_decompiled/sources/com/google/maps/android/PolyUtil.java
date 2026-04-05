package com.google.maps.android;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* JADX INFO: loaded from: classes69.dex */
public class PolyUtil {
    private static final double DEFAULT_TOLERANCE = 0.1d;

    private PolyUtil() {
    }

    private static double tanLatGC(double lat1, double lat2, double lng2, double lng3) {
        return ((Math.tan(lat1) * Math.sin(lng2 - lng3)) + (Math.tan(lat2) * Math.sin(lng3))) / Math.sin(lng2);
    }

    private static double mercatorLatRhumb(double lat1, double lat2, double lng2, double lng3) {
        return ((MathUtil.mercator(lat1) * (lng2 - lng3)) + (MathUtil.mercator(lat2) * lng3)) / lng2;
    }

    private static boolean intersects(double lat1, double lat2, double lng2, double lat3, double lng3, boolean geodesic) {
        if ((lng3 >= 0.0d && lng3 >= lng2) || ((lng3 < 0.0d && lng3 < lng2) || lat3 <= -1.5707963267948966d || lat1 <= -1.5707963267948966d || lat2 <= -1.5707963267948966d || lat1 >= 1.5707963267948966d || lat2 >= 1.5707963267948966d || lng2 <= -3.141592653589793d)) {
            return false;
        }
        double linearLat = (((lng2 - lng3) * lat1) + (lat2 * lng3)) / lng2;
        if (lat1 >= 0.0d && lat2 >= 0.0d && lat3 < linearLat) {
            return false;
        }
        if ((lat1 > 0.0d || lat2 > 0.0d || lat3 < linearLat) && lat3 < 1.5707963267948966d) {
            return geodesic ? Math.tan(lat3) >= tanLatGC(lat1, lat2, lng2, lng3) : MathUtil.mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3);
        }
        return true;
    }

    public static boolean containsLocation(LatLng point, List<LatLng> polygon, boolean geodesic) {
        int size = polygon.size();
        if (size == 0) {
            return false;
        }
        double lat3 = Math.toRadians(point.latitude);
        double lng3 = Math.toRadians(point.longitude);
        LatLng prev = polygon.get(size - 1);
        double lat1 = Math.toRadians(prev.latitude);
        double lng1 = Math.toRadians(prev.longitude);
        int nIntersect = 0;
        for (LatLng point2 : polygon) {
            double dLng3 = MathUtil.wrap(lng3 - lng1, -3.141592653589793d, 3.141592653589793d);
            if (lat3 == lat1 && dLng3 == 0.0d) {
                return true;
            }
            double lat2 = Math.toRadians(point2.latitude);
            double lng2 = Math.toRadians(point2.longitude);
            if (intersects(lat1, lat2, MathUtil.wrap(lng2 - lng1, -3.141592653589793d, 3.141592653589793d), lat3, dLng3, geodesic)) {
                nIntersect++;
            }
            lat1 = lat2;
            lng1 = lng2;
        }
        return (nIntersect & 1) != 0;
    }

    public static boolean isLocationOnEdge(LatLng point, List<LatLng> polygon, boolean geodesic, double tolerance) {
        return isLocationOnEdgeOrPath(point, polygon, true, geodesic, tolerance);
    }

    public static boolean isLocationOnEdge(LatLng point, List<LatLng> polygon, boolean geodesic) {
        return isLocationOnEdge(point, polygon, geodesic, DEFAULT_TOLERANCE);
    }

    public static boolean isLocationOnPath(LatLng point, List<LatLng> polyline, boolean geodesic, double tolerance) {
        return isLocationOnEdgeOrPath(point, polyline, false, geodesic, tolerance);
    }

    public static boolean isLocationOnPath(LatLng point, List<LatLng> polyline, boolean geodesic) {
        return isLocationOnPath(point, polyline, geodesic, DEFAULT_TOLERANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0158, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static boolean isLocationOnEdgeOrPath(com.google.android.gms.maps.model.LatLng r60, java.util.List<com.google.android.gms.maps.model.LatLng> r61, boolean r62, boolean r63, double r64) {
        /*
            Method dump skipped, instruction units count: 354
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.maps.android.PolyUtil.isLocationOnEdgeOrPath(com.google.android.gms.maps.model.LatLng, java.util.List, boolean, boolean, double):boolean");
    }

    private static double sinDeltaBearing(double lat1, double lng1, double lat2, double lng2, double lat3, double lng3) {
        double sinLat1 = Math.sin(lat1);
        double cosLat2 = Math.cos(lat2);
        double cosLat3 = Math.cos(lat3);
        double lat31 = lat3 - lat1;
        double lng31 = lng3 - lng1;
        double lat21 = lat2 - lat1;
        double lng21 = lng2 - lng1;
        double a = Math.sin(lng31) * cosLat3;
        double c = Math.sin(lng21) * cosLat2;
        double b = Math.sin(lat31) + (2.0d * sinLat1 * cosLat3 * MathUtil.hav(lng31));
        double d = Math.sin(lat21) + (2.0d * sinLat1 * cosLat2 * MathUtil.hav(lng21));
        double denom = ((a * a) + (b * b)) * ((c * c) + (d * d));
        if (denom <= 0.0d) {
            return 1.0d;
        }
        return ((a * d) - (b * c)) / Math.sqrt(denom);
    }

    private static boolean isOnSegmentGC(double lat1, double lng1, double lat2, double lng2, double lat3, double lng3, double havTolerance) {
        double havDist13 = MathUtil.havDistance(lat1, lat3, lng1 - lng3);
        if (havDist13 <= havTolerance) {
            return true;
        }
        double havDist23 = MathUtil.havDistance(lat2, lat3, lng2 - lng3);
        if (havDist23 <= havTolerance) {
            return true;
        }
        double sinBearing = sinDeltaBearing(lat1, lng1, lat2, lng2, lat3, lng3);
        double sinDist13 = MathUtil.sinFromHav(havDist13);
        double havCrossTrack = MathUtil.havFromSin(sinDist13 * sinBearing);
        if (havCrossTrack > havTolerance) {
            return false;
        }
        double havDist12 = MathUtil.havDistance(lat1, lat2, lng1 - lng2);
        double term = havDist12 + ((1.0d - (2.0d * havDist12)) * havCrossTrack);
        if (havDist13 > term || havDist23 > term) {
            return false;
        }
        if (havDist12 < 0.74d) {
            return true;
        }
        double cosCrossTrack = 1.0d - (2.0d * havCrossTrack);
        double havAlongTrack13 = (havDist13 - havCrossTrack) / cosCrossTrack;
        double havAlongTrack23 = (havDist23 - havCrossTrack) / cosCrossTrack;
        double sinSumAlongTrack = MathUtil.sinSumFromHav(havAlongTrack13, havAlongTrack23);
        return sinSumAlongTrack > 0.0d;
    }

    public static List<LatLng> simplify(List<LatLng> poly, double tolerance) {
        int n = poly.size();
        if (n < 1) {
            throw new IllegalArgumentException("Polyline must have at least 1 point");
        }
        if (tolerance <= 0.0d) {
            throw new IllegalArgumentException("Tolerance must be greater than zero");
        }
        boolean closedPolygon = isClosedPolygon(poly);
        LatLng lastPoint = null;
        if (closedPolygon) {
            LatLng lastPoint2 = poly.get(poly.size() - 1);
            lastPoint = lastPoint2;
            poly.remove(poly.size() - 1);
            poly.add(new LatLng(lastPoint.latitude + 1.0E-11d, lastPoint.longitude + 1.0E-11d));
        }
        int maxIdx = 0;
        Stack<int[]> stack = new Stack<>();
        double[] dists = new double[n];
        dists[0] = 1.0d;
        dists[n - 1] = 1.0d;
        if (n > 2) {
            int[] stackVal = {0, n - 1};
            stack.push(stackVal);
            while (stack.size() > 0) {
                int[] current = stack.pop();
                double maxDist = 0.0d;
                for (int idx = current[0] + 1; idx < current[1]; idx++) {
                    double dist = distanceToLine(poly.get(idx), poly.get(current[0]), poly.get(current[1]));
                    if (dist > maxDist) {
                        maxDist = dist;
                        maxIdx = idx;
                    }
                }
                if (maxDist > tolerance) {
                    dists[maxIdx] = maxDist;
                    int[] stackValCurMax = {current[0], maxIdx};
                    stack.push(stackValCurMax);
                    int[] stackValMaxCur = {maxIdx, current[1]};
                    stack.push(stackValMaxCur);
                }
            }
        }
        if (closedPolygon) {
            poly.remove(poly.size() - 1);
            poly.add(lastPoint);
        }
        int idx2 = 0;
        ArrayList<LatLng> simplifiedLine = new ArrayList<>();
        for (LatLng l : poly) {
            if (dists[idx2] != 0.0d) {
                simplifiedLine.add(l);
            }
            idx2++;
        }
        return simplifiedLine;
    }

    public static boolean isClosedPolygon(List<LatLng> poly) {
        LatLng firstPoint = poly.get(0);
        LatLng lastPoint = poly.get(poly.size() - 1);
        return firstPoint.equals(lastPoint);
    }

    public static double distanceToLine(LatLng p, LatLng start, LatLng end) {
        if (start.equals(end)) {
            SphericalUtil.computeDistanceBetween(end, p);
        }
        double s0lat = Math.toRadians(p.latitude);
        double s0lng = Math.toRadians(p.longitude);
        double s1lat = Math.toRadians(start.latitude);
        double s1lng = Math.toRadians(start.longitude);
        double s2lat = Math.toRadians(end.latitude);
        double s2lng = Math.toRadians(end.longitude);
        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        double u = (((s0lat - s1lat) * s2s1lat) + ((s0lng - s1lng) * s2s1lng)) / ((s2s1lat * s2s1lat) + (s2s1lng * s2s1lng));
        if (u <= 0.0d) {
            return SphericalUtil.computeDistanceBetween(p, start);
        }
        if (u >= 1.0d) {
            return SphericalUtil.computeDistanceBetween(p, end);
        }
        LatLng sa = new LatLng(p.latitude - start.latitude, p.longitude - start.longitude);
        LatLng sb = new LatLng((end.latitude - start.latitude) * u, (end.longitude - start.longitude) * u);
        return SphericalUtil.computeDistanceBetween(sa, sb);
    }

    public static List<LatLng> decode(String encodedPath) {
        int index;
        int b;
        int len = encodedPath.length();
        List<LatLng> path = new ArrayList<>();
        int index2 = 0;
        int lat = 0;
        int lng = 0;
        while (index2 < len) {
            int result = 1;
            int shift = 0;
            while (true) {
                index = index2 + 1;
                int b2 = (encodedPath.charAt(index2) - '?') - 1;
                result += b2 << shift;
                shift += 5;
                if (b2 < 31) {
                    break;
                }
                index2 = index;
            }
            lat += (result & 1) != 0 ? (result >> 1) ^ (-1) : result >> 1;
            int result2 = 1;
            int shift2 = 0;
            do {
                index++;
                b = (encodedPath.charAt(r3) - '?') - 1;
                result2 += b << shift2;
                shift2 += 5;
            } while (b >= 31);
            lng += (result2 & 1) != 0 ? (result2 >> 1) ^ (-1) : result2 >> 1;
            path.add(new LatLng(((double) lat) * 1.0E-5d, ((double) lng) * 1.0E-5d));
            index2 = index;
        }
        return path;
    }

    public static String encode(List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;
        StringBuffer result = new StringBuffer();
        for (LatLng point : path) {
            long lat = Math.round(point.latitude * 100000.0d);
            long lng = Math.round(point.longitude * 100000.0d);
            long dLat = lat - lastLat;
            long dLng = lng - lastLng;
            encode(dLat, result);
            encode(dLng, result);
            lastLat = lat;
            lastLng = lng;
        }
        return result.toString();
    }

    private static void encode(long v, StringBuffer result) {
        long v2 = v < 0 ? (v << 1) ^ (-1) : v << 1;
        while (v2 >= 32) {
            result.append(Character.toChars((int) (((31 & v2) | 32) + 63)));
            v2 >>= 5;
        }
        result.append(Character.toChars((int) (v2 + 63)));
    }
}
