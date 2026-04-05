package com.kopin.solos.navigate.helperclasses;

import com.kopin.solos.storage.Coordinate;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class CoordinateHelper {
    public static List<Coordinate> decodePoly(String encoded) {
        int index;
        int b;
        List<Coordinate> poly = new ArrayList<>();
        int index2 = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;
        while (index2 < len) {
            int shift = 0;
            int result = 0;
            while (true) {
                index = index2 + 1;
                int b2 = encoded.charAt(index2) - '?';
                result |= (b2 & 31) << shift;
                shift += 5;
                if (b2 < 32) {
                    break;
                }
                index2 = index;
            }
            int dlat = (result & 1) != 0 ? (result >> 1) ^ (-1) : result >> 1;
            lat += dlat;
            int shift2 = 0;
            int result2 = 0;
            do {
                int index3 = index;
                index = index3 + 1;
                b = encoded.charAt(index3) - '?';
                result2 |= (b & 31) << shift2;
                shift2 += 5;
            } while (b >= 32);
            int dlng = (result2 & 1) != 0 ? (result2 >> 1) ^ (-1) : result2 >> 1;
            lng += dlng;
            Coordinate p = new Coordinate(((double) lat) / 100000.0d, ((double) lng) / 100000.0d);
            poly.add(p);
            index2 = index;
        }
        return poly;
    }
}
