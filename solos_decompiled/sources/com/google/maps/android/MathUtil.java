package com.google.maps.android;

/* JADX INFO: loaded from: classes69.dex */
class MathUtil {
    static final double EARTH_RADIUS = 6371009.0d;

    MathUtil() {
    }

    static double clamp(double x, double low, double high) {
        return x < low ? low : x > high ? high : x;
    }

    static double wrap(double n, double min, double max) {
        return (n < min || n >= max) ? mod(n - min, max - min) + min : n;
    }

    static double mod(double x, double m) {
        return ((x % m) + m) % m;
    }

    static double mercator(double lat) {
        return Math.log(Math.tan((0.5d * lat) + 0.7853981633974483d));
    }

    static double inverseMercator(double y) {
        return (2.0d * Math.atan(Math.exp(y))) - 1.5707963267948966d;
    }

    static double hav(double x) {
        double sinHalf = Math.sin(0.5d * x);
        return sinHalf * sinHalf;
    }

    static double arcHav(double x) {
        return 2.0d * Math.asin(Math.sqrt(x));
    }

    static double sinFromHav(double h) {
        return 2.0d * Math.sqrt((1.0d - h) * h);
    }

    static double havFromSin(double x) {
        double x2 = x * x;
        return (x2 / (Math.sqrt(1.0d - x2) + 1.0d)) * 0.5d;
    }

    static double sinSumFromHav(double x, double y) {
        double a = Math.sqrt((1.0d - x) * x);
        double b = Math.sqrt((1.0d - y) * y);
        return ((a + b) - (((a * y) + (b * x)) * 2.0d)) * 2.0d;
    }

    static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + (hav(dLng) * Math.cos(lat1) * Math.cos(lat2));
    }
}
