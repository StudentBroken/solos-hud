package com.kopin.solos.storage;

import android.database.Cursor;
import android.location.Location;
import android.provider.BaseColumns;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes54.dex */
public class Record implements BaseColumns {
    static final String IS_BREAK = "recIsBreak";
    private static final long MAX_TIME_DIFF = 2000;
    static final String NAME = "Record";
    private static final Coordinate NO_DATA_L = Coordinate.NONE;
    static final String SQL_INSERT = "INSERT INTO Record (" + Field.RIDE_ID + ", " + Field.TIMESTAMP + ", " + Field.COORD_ID + ", " + Field.IS_BREAK + ", " + Field.HEARTRATE + ", " + Field.POWER + ", " + Field.SPEED + ", " + Field.CADENCE + ", " + Field.DISTANCE + ", " + Field.OXYGEN + ", " + Field.STRIDE + ", " + Field.RESOLUTION + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String TAG = "Record";
    private double cadence;
    private long coordId;
    private double distance;
    private int heartrate;
    private boolean isBreak;
    protected Coordinate location;
    private int oxygen;
    private double power;
    private int resolution;
    private long rideId;
    private double speed;
    private double stride;
    private long timestamp;

    public enum MetricData {
        TIME,
        DISTANCE,
        SPEED,
        CADENCE,
        HEARTRATE,
        POWER,
        OXYGEN,
        PACE,
        STEP,
        KICK,
        STRIDE,
        NORMALISED_POWER,
        INTENSITY_FACTOR,
        ALTITUDE,
        LOCATION,
        CORRECTED_ALTITUDE
    }

    enum Field {
        TIMESTAMP(FieldType.INTEGER),
        RESOLUTION(FieldType.INTEGER),
        CADENCE(FieldType.REAL),
        DISTANCE(FieldType.REAL),
        HEARTRATE(FieldType.INTEGER),
        POWER(FieldType.REAL),
        SPEED(FieldType.REAL),
        RIDE_ID(FieldType.INTEGER),
        IS_BREAK(FieldType.INTEGER),
        COORD_ID(FieldType.INTEGER),
        OXYGEN(FieldType.INTEGER),
        STRIDE(FieldType.REAL),
        coordAlt(FieldType.REAL, true),
        CORRECTED_ALTITUDE(FieldType.REAL, true);

        public FieldType fieldType;
        private final boolean pseudoEntry;

        Field(FieldType field) {
            this(field, false);
        }

        Field(FieldType field, boolean external) {
            this.fieldType = field;
            this.pseudoEntry = external;
        }

        @Override // java.lang.Enum
        public String toString() {
            return name();
        }

        public String fqname() {
            return "Record." + name();
        }

        public int getInt(Cursor cursor) {
            return FieldHelper.getInt(cursor, name());
        }

        public long getLong(Cursor cursor) {
            return FieldHelper.getLong(cursor, name());
        }

        public double getDouble(Cursor cursor) {
            return FieldHelper.getDouble(cursor, name());
        }

        public String getString(Cursor cursor) {
            return FieldHelper.getString(cursor, name());
        }

        static Field getFieldForMetric(MetricData type) {
            switch (type) {
                case TIME:
                    return TIMESTAMP;
                case DISTANCE:
                    return DISTANCE;
                case SPEED:
                    return SPEED;
                case CADENCE:
                    return CADENCE;
                case HEARTRATE:
                    return HEARTRATE;
                case POWER:
                    return POWER;
                case OXYGEN:
                    return OXYGEN;
                case PACE:
                    return SPEED;
                case STEP:
                    return CADENCE;
                case KICK:
                    return POWER;
                case STRIDE:
                    return STRIDE;
                case ALTITUDE:
                    return coordAlt;
                case CORRECTED_ALTITUDE:
                    return CORRECTED_ALTITUDE;
                case NORMALISED_POWER:
                    return POWER;
                default:
                    return null;
            }
        }
    }

    public Record(long timestamp) {
        this(timestamp, 0);
    }

    Record(long timestamp, int res) {
        this.resolution = 0;
        this.heartrate = Integer.MIN_VALUE;
        this.oxygen = Integer.MIN_VALUE;
        this.cadence = -2.147483648E9d;
        this.distance = -2.147483648E9d;
        this.power = -2.147483648E9d;
        this.speed = -2.147483648E9d;
        this.stride = -2.147483648E9d;
        this.location = NO_DATA_L;
        this.isBreak = false;
        this.timestamp = timestamp;
        this.resolution = res;
    }

    public Record(Cursor cursor) {
        this.resolution = 0;
        this.heartrate = Integer.MIN_VALUE;
        this.oxygen = Integer.MIN_VALUE;
        this.cadence = -2.147483648E9d;
        this.distance = -2.147483648E9d;
        this.power = -2.147483648E9d;
        this.speed = -2.147483648E9d;
        this.stride = -2.147483648E9d;
        this.location = NO_DATA_L;
        this.isBreak = false;
        fromCursor(cursor);
    }

    public Record(long timestamp, int heartrate, double cadence, double distance, double power, double speed, float altitude, long route, Location location, int oxygen, double stride) {
        this.resolution = 0;
        this.heartrate = Integer.MIN_VALUE;
        this.oxygen = Integer.MIN_VALUE;
        this.cadence = -2.147483648E9d;
        this.distance = -2.147483648E9d;
        this.power = -2.147483648E9d;
        this.speed = -2.147483648E9d;
        this.stride = -2.147483648E9d;
        this.location = NO_DATA_L;
        this.isBreak = false;
        this.timestamp = timestamp;
        this.heartrate = heartrate;
        this.cadence = cadence;
        this.distance = distance;
        this.power = power;
        this.speed = speed;
        this.oxygen = oxygen;
        this.stride = stride;
        if (location != null) {
            this.location = new Coordinate(route, altitude, location);
        }
    }

    public static Record createBreak(long timestamp) {
        Record record = new Record(timestamp);
        record.isBreak = true;
        return record;
    }

    public final void fromCursor(Cursor cursor) {
        Cursor cursor2;
        this.timestamp = Field.TIMESTAMP.getLong(cursor);
        this.resolution = Field.RESOLUTION.getInt(cursor);
        FieldHelper.setDefault(Integer.MIN_VALUE);
        this.heartrate = Field.HEARTRATE.getInt(cursor);
        this.cadence = Field.CADENCE.getDouble(cursor);
        this.distance = Field.DISTANCE.getDouble(cursor);
        this.power = Field.POWER.getDouble(cursor);
        this.speed = Field.SPEED.getDouble(cursor);
        this.oxygen = Field.OXYGEN.getInt(cursor);
        this.stride = Field.STRIDE.getDouble(cursor);
        FieldHelper.setDefault(-1);
        this.coordId = Field.COORD_ID.getLong(cursor);
        if (this.coordId != -1 && (cursor2 = SQLHelper.getCoordCursor(this.coordId)) != null) {
            if (cursor2.moveToFirst()) {
                this.location = new Coordinate(cursor2);
            }
            cursor2.close();
        }
        if (this.location == null) {
            this.location = Coordinate.NONE;
        }
        FieldHelper.setDefault(0);
        this.isBreak = Field.IS_BREAK.getInt(cursor) != 0;
        this.rideId = Field.RIDE_ID.getLong(cursor);
    }

    public boolean acceptsAltitude(long time) {
        return !hasAltitude() && checkTime(time);
    }

    public boolean acceptsCadence(long time) {
        return !hasCadence() && checkTime(time);
    }

    public boolean acceptsHeartrate(long time) {
        return !hasHeartrate() && checkTime(time);
    }

    public boolean acceptsPower(long time) {
        return !hasPower() && checkTime(time);
    }

    public boolean acceptsSpeed(long time) {
        return !hasSpeed() && checkTime(time);
    }

    public boolean acceptsDistance(long time) {
        return !hasDistance() && checkTime(time);
    }

    public boolean acceptsStride(long time) {
        return !hasStride() && checkTime(time);
    }

    public boolean acceptsOxygen(long time) {
        return !hasOxygen() && checkTime(time);
    }

    public boolean acceptsLocation(long time) {
        return !hasLocation() && checkTime(time);
    }

    private boolean checkTime(long time) {
        return time == 0 || time - 2000 < this.timestamp;
    }

    int getResolution() {
        return this.resolution;
    }

    public long getDataValidityTime() {
        return (this.resolution + 1) * 4 * 1000;
    }

    public long getCoordId() {
        return this.coordId;
    }

    public void setCoordId(long coord) {
        this.coordId = coord;
    }

    public float getAltitude() {
        return this.location.getAltitude();
    }

    public double getCadence() {
        return this.cadence;
    }

    public double getDistance() {
        return this.distance;
    }

    public int getHeartrate() {
        return this.heartrate;
    }

    public double getPower() {
        return this.power;
    }

    public double getStride() {
        return this.stride;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getOxygen() {
        return this.oxygen;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public double getLongitude() {
        return this.location.getLongitude();
    }

    public double getLatitude() {
        return this.location.getLatitude();
    }

    public double getSpeedForLocale() {
        return Conversion.speedForLocale(this.speed);
    }

    public double getDistanceForLocale() {
        return Utility.convertToUserUnits(1, this.distance);
    }

    public double getAltitudeForLocale() {
        return Utility.convertToUserUnits(0, this.location.getAltitude());
    }

    public double getStrideForLocale() {
        return Conversion.strideForLocale(this.stride);
    }

    public long getRideId() {
        return this.rideId;
    }

    public boolean hasAltitude() {
        return this.location.hasAltitude();
    }

    public boolean hasCadence() {
        return this.cadence != -2.147483648E9d;
    }

    public boolean hasHeartrate() {
        return this.heartrate != Integer.MIN_VALUE;
    }

    public boolean hasPower() {
        return this.power != -2.147483648E9d;
    }

    public boolean hasSpeed() {
        return this.speed != -2.147483648E9d;
    }

    public boolean hasStride() {
        return this.stride != -2.147483648E9d;
    }

    public boolean hasOxygen() {
        return this.oxygen != Integer.MIN_VALUE;
    }

    public boolean hasDistance() {
        return this.distance != -2.147483648E9d;
    }

    public boolean hasLocation() {
        return this.location.hasData();
    }

    public boolean isBreak() {
        return this.isBreak;
    }

    public void setAltitude(float altitude) {
        this.location.setAltitude(altitude);
    }

    public void setCadence(double cadence) {
        this.cadence = cadence;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setHeartrate(int heartrate) {
        this.heartrate = heartrate;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setStride(double stride) {
        this.stride = stride;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    public String toString() {
        if (this.isBreak) {
            return "{ break }";
        }
        return "{ timestamp: " + this.timestamp + " (resolution: " + this.resolution + "); cadence: " + (hasCadence() ? Double.valueOf(this.cadence) : "--") + "; distance: " + (hasDistance() ? Double.valueOf(this.distance) : "--") + "; heartrate: " + (hasHeartrate() ? Integer.valueOf(this.heartrate) : "--") + "; power: " + (hasPower() ? Double.valueOf(this.power) : "--") + "; oxygen: " + (hasOxygen() ? Integer.valueOf(this.oxygen) : "--") + "; speed: " + (hasSpeed() ? Double.valueOf(this.speed) : "--") + "; location: " + (hasLocation() ? this.location : "--") + " }";
    }

    public static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            if (!field.pseudoEntry) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(field.name()).append(" ").append(field.fieldType);
            }
        }
        return builder.toString();
    }

    public Coordinate getTimestampedCoordinate() {
        if (this.location == NO_DATA_L) {
            return null;
        }
        return new Coordinate(this.location, this.timestamp);
    }
}
