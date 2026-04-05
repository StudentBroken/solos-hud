package com.kopin.peloton.ride;

/* JADX INFO: loaded from: classes61.dex */
public class Coordinate {
    public double Altitude;
    public double Latitude;
    public double Longitude;

    public Coordinate(double lat, double lon) {
        this.Latitude = -2.147483648E9d;
        this.Longitude = -2.147483648E9d;
        this.Altitude = -2.147483648E9d;
        this.Latitude = lat;
        this.Longitude = lon;
    }

    public Coordinate(double lat, double lon, double alt) {
        this.Latitude = -2.147483648E9d;
        this.Longitude = -2.147483648E9d;
        this.Altitude = -2.147483648E9d;
        this.Latitude = lat;
        this.Longitude = lon;
        this.Altitude = alt;
    }

    public Coordinate(double alt) {
        this.Latitude = -2.147483648E9d;
        this.Longitude = -2.147483648E9d;
        this.Altitude = -2.147483648E9d;
        this.Altitude = alt;
    }
}
