package org.jstrava.entities.segment;

/* JADX INFO: loaded from: classes68.dex */
public class Bound {
    private double northeastLatitude;
    private double northeastlongitude;
    private double southwestLatitude;
    private double southwestlongitude;

    public Bound(double southwestLatitude, double southwestlongitude, double northeastLatitude, double northeastlongitude) {
        this.southwestLatitude = southwestLatitude;
        this.southwestlongitude = southwestlongitude;
        this.northeastLatitude = northeastLatitude;
        this.northeastlongitude = northeastlongitude;
    }

    public double getSouthwestLatitude() {
        return this.southwestLatitude;
    }

    public void setSouthwestLatitude(double southwestLatitude) {
        this.southwestLatitude = southwestLatitude;
    }

    public double getSouthwestlongitude() {
        return this.southwestlongitude;
    }

    public void setSouthwestlongitude(double southwestlongitude) {
        this.southwestlongitude = southwestlongitude;
    }

    public double getNortheastLatitude() {
        return this.northeastLatitude;
    }

    public void setNortheastLatitude(double northeastLatitude) {
        this.northeastLatitude = northeastLatitude;
    }

    public double getNortheastlongitude() {
        return this.northeastlongitude;
    }

    public void setNortheastlongitude(double northeastlongitude) {
        this.northeastlongitude = northeastlongitude;
    }

    public String toString() {
        return this.southwestLatitude + "," + this.southwestlongitude + "," + this.northeastLatitude + "," + this.northeastlongitude;
    }
}
