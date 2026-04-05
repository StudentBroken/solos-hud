package com.kopin.solos.storage;

import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Shared;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class Route implements BaseColumns {
    public static final String DISTANCE = "routeDistance";
    public static final String NAME = "Route";
    public static final long NONE = -1;
    public static final String TIME_TO_BEAT = "routeTime";
    public static final String TITLE = "routeName";

    public static class Saved implements ISyncable {
        private double mDistance;
        private long routeId;
        private String routeName;
        private long timeToBeat;

        public Saved(long id, String name, long duration, double distance) {
            this.routeId = id;
            this.routeName = name;
            this.timeToBeat = duration;
            this.mDistance = distance;
        }

        public Saved(Cursor cursor) {
            int pos = cursor.getColumnIndex("_id");
            this.routeId = pos != -1 ? cursor.getLong(pos) : -1L;
            int pos2 = cursor.getColumnIndex(Route.TITLE);
            this.routeName = pos2 != -1 ? cursor.getString(pos2) : "";
            int pos3 = cursor.getColumnIndex(Route.DISTANCE);
            this.mDistance = pos3 != -1 ? cursor.getDouble(pos3) : 0.0d;
            int pos4 = cursor.getColumnIndex(Route.TIME_TO_BEAT);
            this.timeToBeat = pos4 != -1 ? cursor.getLong(pos4) : 0L;
        }

        public long getId() {
            return this.routeId;
        }

        public double getDistance() {
            return this.mDistance;
        }

        public long getTimeToBeat() {
            return this.timeToBeat;
        }

        public String getRouteName() {
            return (this.routeName == null || this.routeName.isEmpty()) ? "Untitled" : this.routeName;
        }

        public boolean isValidRouteName() {
            return (this.routeName == null || this.routeName.isEmpty()) ? false : true;
        }

        @Override // com.kopin.solos.storage.ISyncable
        public Shared toShared(int providerKey, String userName, String externalId) {
            return new Shared(this.routeId, providerKey, userName, externalId, false, Shared.ShareType.ROUTE, System.currentTimeMillis());
        }

        public List<Coordinate> getRouteCoordinates() {
            return SQLHelper.getRouteDetails(false, this.routeId);
        }
    }

    public static long getRouteIdForRide(long rideId) {
        long routeId = -1;
        Cursor c = SQLHelper.getRideCursor(rideId, false);
        if (c.moveToNext()) {
            routeId = c.getLong(c.getColumnIndex("routeId"));
        }
        c.close();
        return routeId;
    }

    public static List<Coordinate> getRouteCoordinates(long routeId) {
        if (routeId == -1) {
            return new ArrayList();
        }
        final ArrayList<Coordinate> coords = new ArrayList<>();
        SQLHelper.foreachCoord(routeId, new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.storage.Route.1
            @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
            public boolean onCoordinate(Coordinate coord) {
                coords.add(coord);
                return true;
            }
        });
        return coords;
    }

    public static boolean isNavRoute(long routeId) {
        return SQLHelper.isSavedRoute(routeId);
    }
}
