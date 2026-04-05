package com.kopin.peloton.ride;

/* JADX INFO: loaded from: classes61.dex */
public class WayPoint extends DataPoint {
    public String NavInstruction = "";

    public WayPoint() {
    }

    public WayPoint(Coordinate coordinate) {
        this.Position = coordinate;
    }

    public WayPoint(double lat, double lon) {
        this.Position = new Coordinate(lat, lon);
    }

    public WayPoint(double lat, double lon, double alt) {
        this.Position = new Coordinate(lat, lon, alt);
    }
}
