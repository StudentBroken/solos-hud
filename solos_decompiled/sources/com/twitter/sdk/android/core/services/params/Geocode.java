package com.twitter.sdk.android.core.services.params;

/* JADX INFO: loaded from: classes62.dex */
public class Geocode {
    public final Distance distance;
    public final double latitude;
    public final double longitude;
    public final int radius;

    public enum Distance {
        MILES("mi"),
        KILOMETERS("km");

        public final String identifier;

        Distance(String identifier) {
            this.identifier = identifier;
        }
    }

    public Geocode(double latitude, double longitude, int radius, Distance distance) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.distance = distance;
    }

    public String toString() {
        return this.latitude + "," + this.longitude + "," + this.radius + this.distance.identifier;
    }
}
