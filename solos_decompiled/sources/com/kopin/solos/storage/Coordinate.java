package com.kopin.solos.storage;

import android.database.Cursor;
import android.location.Location;
import android.provider.BaseColumns;
import com.google.android.gms.maps.model.LatLng;
import com.kopin.solos.storage.Record;

/* JADX INFO: loaded from: classes54.dex */
public class Coordinate implements BaseColumns {
    public static final String ALTITUDE = "coordAlt";
    public static final String LATITUDE = "coordLat";
    public static final String LONGITUDE = "coordLong";
    public static final String NAME = "Coord";
    public static final Coordinate NONE = new Coordinate();
    public static final double NO_DATA_D = -2.147483648E9d;
    public static final float NO_DATA_F = -2.14748365E9f;
    public static final String ROUTE_ID = "routeId";
    private static final long ROUTE_NONE = -1;
    static final String SQL_INSERT = "INSERT INTO Coord (routeId, coordLat, coordLong, coordAlt) VALUES (?, ?, ?, ?)";
    private float altitude;
    private boolean hasData;
    private double latitude;
    private double longitude;
    private long mId;
    private long mRouteId;
    private long timestamp;

    private Coordinate() {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = -1L;
    }

    protected Coordinate(Coordinate from) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = from.mRouteId;
        this.latitude = from.latitude;
        this.longitude = from.longitude;
        this.altitude = from.altitude;
        this.hasData = from.hasData;
        this.timestamp = from.timestamp;
    }

    public Coordinate(LatLng pos) {
        this(pos.latitude, pos.longitude);
    }

    public Coordinate(double lat, double lon) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = -1L;
        this.latitude = lat;
        this.longitude = lon;
        this.hasData = true;
    }

    public Coordinate(double lat, double lon, long coordId) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mId = coordId;
        this.mRouteId = -1L;
        this.latitude = lat;
        this.longitude = lon;
        this.hasData = true;
    }

    public Coordinate(long routeId, double lat, double lng, float alt) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = routeId;
        this.latitude = lat;
        this.longitude = lng;
        this.altitude = alt;
        this.hasData = true;
    }

    public Coordinate(long routeId, Location location) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = routeId;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        if (location.hasAltitude()) {
            this.altitude = (float) location.getAltitude();
        }
        this.hasData = true;
    }

    public Coordinate(long routeId, float alt, Location location) {
        this(routeId, location);
        this.altitude = alt;
    }

    public Coordinate(Coordinate location, long stamp) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.mRouteId = location.mRouteId;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.hasData = location.hasData;
        this.timestamp = stamp;
    }

    public Coordinate(Cursor cursor) {
        this.latitude = -2.147483648E9d;
        this.longitude = -2.147483648E9d;
        this.altitude = -2.14748365E9f;
        this.timestamp = 0L;
        this.hasData = false;
        this.hasData = true;
        int pos = cursor.getColumnIndex("_id");
        this.mId = pos != -1 ? cursor.getLong(pos) : -1L;
        int pos2 = cursor.getColumnIndex("routeId");
        this.mRouteId = pos2 != -1 ? cursor.getLong(pos2) : -1L;
        int pos3 = cursor.getColumnIndex(LATITUDE);
        this.latitude = pos3 != -1 ? cursor.getDouble(pos3) : -2.147483648E9d;
        this.hasData = this.latitude != -2.147483648E9d;
        int pos4 = cursor.getColumnIndex(LONGITUDE);
        this.longitude = pos4 != -1 ? cursor.getDouble(pos4) : -2.147483648E9d;
        this.hasData = this.hasData && this.longitude != -2.147483648E9d;
        int pos5 = cursor.getColumnIndex(ALTITUDE);
        this.altitude = pos5 != -1 ? cursor.getFloat(pos5) : -2.14748365E9f;
        int pos6 = cursor.getColumnIndex(Record.Field.TIMESTAMP.name());
        this.timestamp = pos6 != -1 ? cursor.getLong(pos6) : 0L;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public boolean hasData() {
        return this.hasData && hasData(this.longitude) && hasData(this.latitude);
    }

    private boolean hasData(double latOrLong) {
        return (latOrLong == -2.147483648E9d || latOrLong == 0.0d) ? false : true;
    }

    public boolean hasAltitude() {
        return this.altitude != -2.14748365E9f;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public float getAltitude() {
        return this.altitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public void setRouteId(long newRouteId) {
        this.mRouteId = newRouteId;
    }

    public long getRouteId() {
        return this.mRouteId;
    }

    public long getId() {
        return this.mId;
    }

    public void setLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.hasData = true;
    }

    public Location getLocation(boolean withAltitude) {
        Location loc = new Location("solos");
        if (hasData()) {
            loc.setLatitude(this.latitude);
            loc.setLongitude(this.longitude);
        }
        if (withAltitude && hasAltitude()) {
            loc.setAltitude(this.altitude);
        }
        return loc;
    }

    public void setAltitude(float alt) {
        this.altitude = alt;
    }

    public String toString() {
        return "{" + this.latitude + "/" + this.longitude + "}";
    }
}
