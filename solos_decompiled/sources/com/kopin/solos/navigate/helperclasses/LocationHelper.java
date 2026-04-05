package com.kopin.solos.navigate.helperclasses;

import android.location.Location;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleLatLng;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.storage.Coordinate;

/* JADX INFO: loaded from: classes47.dex */
public class LocationHelper {
    public static Location location(double lat, double lng) {
        Location l = new Location("");
        l.setLatitude(lat);
        l.setLongitude(lng);
        return l;
    }

    public static Location location(Coordinate lL) {
        if (lL == null) {
            return null;
        }
        return location(lL.getLatitude(), lL.getLongitude());
    }

    public static Location location(Waypoint tC) {
        if (tC == null) {
            return null;
        }
        return location(tC.getLatitude(), tC.getLongitude());
    }

    public static Location location(GoogleLatLng lL) {
        return location(lL.gLat, lL.gLng);
    }
}
