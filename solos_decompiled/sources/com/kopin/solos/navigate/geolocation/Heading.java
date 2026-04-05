package com.kopin.solos.navigate.geolocation;

/* JADX INFO: loaded from: classes47.dex */
public class Heading {
    public static final Heading NONE = new Heading(0.0d, 0.0d);
    private double bearing;
    private double distance;

    public enum Bearing {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        public static Bearing fromDirection(double dir) {
            double bearing = rationalise(dir);
            double sectorAngle = 360 / values().length;
            double sectorStart = -(sectorAngle / 2.0d);
            for (Bearing b : values()) {
                if (bearing <= sectorStart || bearing >= sectorStart + sectorAngle) {
                    sectorStart += sectorAngle;
                } else {
                    return b;
                }
            }
            Bearing b2 = NORTH;
            return b2;
        }

        public static double normalise(double bearing) {
            if (bearing < -180.0d) {
                return bearing + 360.0d;
            }
            if (bearing > 180.0d) {
                return 360.0d - bearing;
            }
            return bearing;
        }

        public static double rationalise(double bearing) {
            if (bearing < 0.0d) {
                return bearing + 360.0d;
            }
            return bearing;
        }
    }

    Heading(double dist, double dir) {
        this.distance = dist;
        this.bearing = dir;
    }

    public boolean sameDirection(Heading other) {
        return this.bearing < -170.0d ? other.bearing < -170.0d : this.bearing < -10.0d ? other.bearing < -10.0d && other.bearing > -170.0d : this.bearing > 170.0d ? other.bearing > 170.0d : this.bearing > 10.0d ? other.bearing > 10.0d && other.bearing < 170.0d : other.bearing < 10.0d && other.bearing > -10.0d;
    }
}
