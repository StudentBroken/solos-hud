package com.kopin.solos.navigate.geolocation;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.kopin.solos.navigate.geolocation.Heading;
import com.kopin.solos.navigate.geolocation.TurnBasedDirections;
import com.kopin.solos.navigate.helperclasses.LocationHelper;
import com.kopin.solos.storage.Coordinate;

/* JADX INFO: loaded from: classes47.dex */
public class Waypoint extends Coordinate {
    static final Waypoint NONE = new Waypoint(Coordinate.NONE);
    public int bearing;
    public int distance;
    public String instruction;
    public boolean isEnd;
    public boolean isStart;
    public String maneuver;
    public Heading nextWaypoint;
    public float[] results;
    public TurnBasedDirections.Turn turn;

    private boolean shouldBeAdded() {
        return this.isEnd || this.isStart;
    }

    public Waypoint(Coordinate theCoordsToAdd) {
        super(theCoordsToAdd);
        this.isStart = false;
        this.isEnd = false;
        this.results = new float[2];
        this.distance = 0;
        this.bearing = 0;
        this.instruction = "";
        this.maneuver = "";
        this.nextWaypoint = Heading.NONE;
        this.turn = TurnBasedDirections.Turn.STRAIGHT_ON;
    }

    public Waypoint(Waypoint x) {
        super(x);
        this.isStart = false;
        this.isEnd = false;
        this.results = new float[2];
        this.distance = 0;
        this.bearing = 0;
        this.instruction = "";
        this.maneuver = "";
        this.nextWaypoint = Heading.NONE;
        this.turn = TurnBasedDirections.Turn.STRAIGHT_ON;
        this.isEnd = x.isEnd;
        this.isStart = x.isStart;
        this.bearing = x.bearing;
        this.distance = x.distance;
        this.instruction = x.instruction;
        this.maneuver = x.maneuver;
        this.nextWaypoint = x.nextWaypoint;
    }

    public Location toLocation() {
        return LocationHelper.location(this);
    }

    @Override // com.kopin.solos.storage.Coordinate
    public String toString() {
        return "Lat/Lon: " + getLatitude() + ", " + getLongitude() + " Heading: " + this.distance + " mark " + this.bearing + " (" + Heading.Bearing.fromDirection(this.bearing) + ") Instructions: " + this.instruction + ", " + this.maneuver;
    }

    public boolean equals(Coordinate c) {
        return c.getLatitude() == getLatitude() && c.getLongitude() == getLongitude();
    }

    public boolean equals(LatLng pos) {
        return pos.latitude == getLatitude() && pos.longitude == getLongitude();
    }

    public boolean sameDirection(Waypoint other) {
        return this.turn.sameDirection(other.turn);
    }
}
