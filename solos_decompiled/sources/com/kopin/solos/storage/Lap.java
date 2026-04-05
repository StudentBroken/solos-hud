package com.kopin.solos.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class Lap extends Trackable {
    public static final String NAME = "Lap";
    private static final String TAG = "Lap";
    public static final String TRIGGER_DISABLED = "Disabled";
    public static final String TRIGGER_DISTANCE = "Distance";
    public static final String TRIGGER_MANUAL = "Manual";
    public static final String TRIGGER_TIME = "Time";
    private long mId;
    private IWorkout mRide;
    protected SplitHelper.SplitType mSplitType;
    private TimeHelper mTimeHelper;

    public enum Field {
        RIDE_ID(FieldType.INTEGER),
        START_TIME(FieldType.INTEGER),
        DURATION(FieldType.INTEGER),
        END_TIME(FieldType.INTEGER),
        AVERAGE_SPEED(FieldType.INTEGER),
        DISTANCE(FieldType.REAL),
        AVERAGE_CADENCE(FieldType.REAL),
        AVERAGE_HEARTRATE(FieldType.INTEGER),
        AVERAGE_POWER(FieldType.REAL),
        MAX_ALTITUDE_DIFF(FieldType.REAL),
        MAX_CADENCE(FieldType.REAL),
        MAX_HEARTRATE(FieldType.INTEGER),
        MAX_POWER(FieldType.REAL),
        MAX_SPEED(FieldType.REAL),
        FINAL(FieldType.INTEGER),
        TRIGGER(FieldType.TEXT),
        CALORIES(FieldType.INTEGER),
        GAINED_ALTITUDE(FieldType.REAL),
        AVERAGE_OXYGEN(FieldType.INTEGER),
        MIN_OXYGEN(FieldType.INTEGER),
        AVERAGE_NORM_POWER(FieldType.REAL),
        MAX_NORM_POWER(FieldType.REAL),
        AVERAGE_INTENSITY(FieldType.REAL),
        MAX_INTENSITY(FieldType.REAL),
        AVERAGE_PACE(FieldType.REAL),
        MAX_PACE(FieldType.REAL),
        AVERAGE_STRIDE(FieldType.REAL),
        MAX_STRIDE(FieldType.REAL);

        public FieldType fieldType;

        Field(FieldType field) {
            this.fieldType = field;
        }

        @Override // java.lang.Enum
        public String toString() {
            return name();
        }

        public int getInt(Cursor cursor) {
            return FieldHelper.getInt(cursor, name());
        }

        public long getLong(Cursor cursor) {
            return FieldHelper.getLong(cursor, name());
        }

        public float getFloat(Cursor cursor) {
            return FieldHelper.getFloat(cursor, name());
        }

        public double getDouble(Cursor cursor) {
            return FieldHelper.getDouble(cursor, name());
        }

        public String getString(Cursor cursor) {
            return FieldHelper.getString(cursor, name());
        }
    }

    public Lap(long id) {
        this.mId = id;
    }

    public Lap(IWorkout ride, SplitHelper.SplitType type, TimeHelper timeHelper) {
        this.mRide = ride;
        if (ride == null) {
            throw new NullPointerException("Null Ride");
        }
        this.mTimeHelper = timeHelper;
        if (timeHelper == null) {
            throw new NullPointerException("Null TimeHelper");
        }
        this.mId = SQLHelper.addLap(ride.getId(), timeHelper.getTime());
        this.mSplitType = type;
    }

    public Lap(long rideId, SplitHelper.SplitType type, TimeHelper timeHelper) {
        this.mTimeHelper = timeHelper;
        if (timeHelper == null) {
            throw new NullPointerException("Null TimeHelper");
        }
        this.mId = SQLHelper.addLap(rideId, timeHelper.getTime());
        this.mSplitType = type;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public Split end(long startTime, double splitDistance, long splitTime, int calories) {
        int elevation = this.mAltitude.getDiff().intValue();
        Saved saved = new Saved(this, startTime, this.mTimeHelper.getTime(), splitDistance, splitTime, calories, elevation);
        SQLHelper.updateLap(saved, this.mRide.getStartTime());
        LiveRide.setLastLap(saved);
        return new Split(splitTime, splitDistance, elevation);
    }

    public static class Split {
        public double distance;
        public int elevation;
        public long time;

        public Split(long time, double distance, int elevation) {
            this.time = time;
            this.distance = distance;
            this.elevation = elevation;
        }
    }

    public static class Saved extends StatsSheet implements IRidePartSaved {
        private int mCalories;
        private double mDistance;
        private long mDuration;
        private long mEndTime;
        private float mGainedAltitude;
        private long mId;
        private float mMaxAltitudeDiff;
        private long mRideId;
        private SplitHelper.SplitType mSplitType;
        private long mStartTime;

        public Saved(long rideId, long startTime, long endTime) {
            this.mId = -2147483648L;
            this.mRideId = -2147483648L;
            this.mStartTime = -2147483648L;
            this.mDuration = -2147483648L;
            this.mEndTime = -2147483648L;
            this.mDistance = -2.147483648E9d;
            this.mCalories = Integer.MIN_VALUE;
            this.mMaxAltitudeDiff = -2.14748365E9f;
            this.mGainedAltitude = -2.14748365E9f;
            this.mRideId = rideId;
            this.mStartTime = startTime;
            this.mEndTime = endTime;
        }

        public Saved(Lap lap, long startTime, long endTime, double splitDistance, long splitTime, int calories, float gainedAltitude) {
            super(lap);
            this.mId = -2147483648L;
            this.mRideId = -2147483648L;
            this.mStartTime = -2147483648L;
            this.mDuration = -2147483648L;
            this.mEndTime = -2147483648L;
            this.mDistance = -2.147483648E9d;
            this.mCalories = Integer.MIN_VALUE;
            this.mMaxAltitudeDiff = -2.14748365E9f;
            this.mGainedAltitude = -2.14748365E9f;
            this.mId = lap.mId;
            this.mStartTime = startTime;
            this.mDuration = splitTime;
            this.mEndTime = endTime;
            Log.d("Lap", "Avg speed stats: " + getAverageSpeed() + ", from splithelper: " + Utility.calculateSpeedInKph(splitDistance, splitTime));
            this.mDistance = splitDistance;
            this.mGainedAltitude = gainedAltitude;
            Log.d("Lap", "saved calories " + calories);
            this.mCalories = calories;
            this.mMaxAltitudeDiff = lap.mAltitude.getRange().floatValue();
            this.mSplitType = lap.mSplitType;
        }

        public Saved(Cursor cursor) {
            this.mId = -2147483648L;
            this.mRideId = -2147483648L;
            this.mStartTime = -2147483648L;
            this.mDuration = -2147483648L;
            this.mEndTime = -2147483648L;
            this.mDistance = -2.147483648E9d;
            this.mCalories = Integer.MIN_VALUE;
            this.mMaxAltitudeDiff = -2.14748365E9f;
            this.mGainedAltitude = -2.14748365E9f;
            this.mId = FieldHelper.getLong(cursor, "_id");
            this.mRideId = Field.RIDE_ID.getLong(cursor);
            FieldHelper.setDefault(Integer.MIN_VALUE);
            this.mStartTime = Field.START_TIME.getInt(cursor);
            this.mDuration = Field.DURATION.getLong(cursor);
            this.mEndTime = Field.END_TIME.getLong(cursor);
            setSpeed(cursor, null, Field.MAX_SPEED.name(), Field.AVERAGE_SPEED.name());
            setCadence(cursor, null, Field.MAX_CADENCE.name(), Field.AVERAGE_CADENCE.name());
            setPower(cursor, null, Field.MAX_POWER.name(), Field.AVERAGE_POWER.name());
            setHeartRate(cursor, null, Field.MAX_HEARTRATE.name(), Field.AVERAGE_HEARTRATE.name());
            setOxygen(cursor, null, Field.MIN_OXYGEN.name(), Field.AVERAGE_OXYGEN.name());
            setNormPower(cursor, null, Field.MAX_NORM_POWER.name(), Field.AVERAGE_NORM_POWER.name());
            setIntensity(cursor, null, Field.MAX_INTENSITY.name(), Field.AVERAGE_INTENSITY.name());
            setPace(cursor, null, Field.MAX_PACE.name(), Field.AVERAGE_PACE.name());
            setStride(cursor, null, Field.MAX_STRIDE.name(), Field.AVERAGE_STRIDE.name());
            this.mDistance = Field.DISTANCE.getDouble(cursor);
            this.mGainedAltitude = Field.GAINED_ALTITUDE.getFloat(cursor);
            this.mMaxAltitudeDiff = Field.MAX_ALTITUDE_DIFF.getFloat(cursor);
            this.mSplitType = parseSplitTrigger(Field.TRIGGER.getString(cursor));
            this.mCalories = Field.CALORIES.getInt(cursor);
            FieldHelper.setDefault(0);
            setPace();
        }

        public static Saved changeIds(Saved lap, long id, long rideId) {
            lap.mId = id;
            lap.mRideId = rideId;
            return lap;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public long getId() {
            return this.mId;
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public long getRideId() {
            return this.mRideId;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public long getStartTime() {
            return this.mStartTime;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public long getActualStartTime() {
            return this.mStartTime;
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public long getDuration() {
            return this.mDuration;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public long getEndTime() {
            return this.mEndTime;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public double getDistance() {
            return this.mDistance;
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getDistanceForLocale() {
            return Utility.convertToUserUnits(1, this.mDistance / 1000.0d);
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public float getGainedAltitude() {
            return this.mGainedAltitude;
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public float getGainedAltitudeForLocale() {
            return (float) Utility.convertToUserUnits(0, this.mGainedAltitude);
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getMaxAltitudeDiffForLocale() {
            return Utility.convertToUserUnits(0, this.mMaxAltitudeDiff);
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getMaxSpeedForLocale() {
            return Conversion.speedForLocale(getMaxSpeed());
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getAveragePaceForLocale() {
            return Conversion.paceForLocale(getAveragePace());
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getMaxPaceForLocale() {
            return Conversion.paceForLocale(getMaxPace());
        }

        public float getMaxAltitudeDiff() {
            return this.mMaxAltitudeDiff;
        }

        public SplitHelper.SplitType getSplitType() {
            return this.mSplitType;
        }

        @Override // com.kopin.solos.storage.IRidePartData
        public int getCalories() {
            return this.mCalories;
        }

        @Override // com.kopin.solos.storage.StatsSheet, com.kopin.solos.storage.IRidePartData
        public double getAverageSpeed() {
            if (this.mDuration > 1000) {
                return (this.mDistance / this.mDuration) * 1000.0d;
            }
            return 0.0d;
        }

        public double getTrueAverageSpeed() {
            return super.getAverageSpeed();
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getAverageSpeedForLocale() {
            return Conversion.speedForLocale(getAverageSpeed());
        }

        public String getSplitTrigger() {
            switch (this.mSplitType) {
                case DISTANCE:
                    return Lap.TRIGGER_DISTANCE;
                case MANUAL:
                    return Lap.TRIGGER_MANUAL;
                case TIME:
                    return Lap.TRIGGER_TIME;
                case DISABLED:
                    return Lap.TRIGGER_DISABLED;
                default:
                    throw new InvalidParameterException("Someone forgot to do something...");
            }
        }

        private SplitHelper.SplitType parseSplitTrigger(String trigger) {
            if (trigger == null) {
                return SplitHelper.SplitType.MANUAL;
            }
            switch (trigger) {
            }
            return SplitHelper.SplitType.MANUAL;
        }

        @Override // com.kopin.solos.storage.StatsSheet
        public String toString() {
            return "{ id: " + this.mId + ", rideId: " + this.mRideId + ", start time: " + this.mStartTime + ", duration: " + this.mDuration + ", end time: " + this.mEndTime + ", average speed: " + getAverageSpeed() + " true avg: " + getTrueAverageSpeed() + ", distance: " + this.mDistance + ", maxAltitudeDiff: " + this.mMaxAltitudeDiff + ", cadence: " + getMaxCadence() + ", heartRate: " + getMaxHeartrate() + ", speed: " + getMaxSpeed() + ", power: " + getMaxPower() + ", calories: " + this.mCalories + ", averageOxygen: " + getAverageOxygen() + ", minOxygen: " + getMinOxygen() + ", maxOxygen: " + getMaxOxygen() + "] }";
        }

        public void setCadence(ContentValues contentValues) {
            contentValues.put(Field.AVERAGE_CADENCE.name(), Double.valueOf(getAverageCadence()));
            contentValues.put(Field.MAX_CADENCE.name(), Double.valueOf(getMaxCadence()));
        }

        public void setHeartrate(ContentValues contentValues) {
            contentValues.put(Field.AVERAGE_HEARTRATE.name(), Integer.valueOf(getAverageHeartrate()));
            contentValues.put(Field.MAX_HEARTRATE.name(), Integer.valueOf(getMaxHeartrate()));
        }

        public void setPower(ContentValues contentValues) {
            contentValues.put(Field.AVERAGE_POWER.name(), Double.valueOf(getAveragePower()));
            contentValues.put(Field.MAX_POWER.name(), Double.valueOf(getMaxPower()));
        }

        public void setSpeed(ContentValues contentValues) {
            Log.d("Lap", "setSpeed ave " + getAverageSpeed());
            contentValues.put(Field.AVERAGE_SPEED.name(), Double.valueOf(getAverageSpeed()));
            contentValues.put(Field.MAX_SPEED.name(), Double.valueOf(getMaxSpeed()));
        }

        public void setPace(ContentValues contentValues) {
            Log.d("Lap", "setSpeed ave " + getAverageSpeed());
            contentValues.put(Field.AVERAGE_PACE.name(), Double.valueOf(getAveragePace()));
            contentValues.put(Field.MAX_PACE.name(), Double.valueOf(getMaxPace()));
        }

        public void setStride(ContentValues contentValues) {
            contentValues.put(Field.AVERAGE_STRIDE.name(), Double.valueOf(getAverageStride()));
            contentValues.put(Field.MAX_STRIDE.name(), Double.valueOf(getMaxStride()));
        }

        public void setBasicMetrics(ContentValues contentValues) {
            setCadence(contentValues);
            setHeartrate(contentValues);
            setPower(contentValues);
            setSpeed(contentValues);
            setPace(contentValues);
            setStride(contentValues);
        }

        public void setMetricStats(Record.MetricData type, Metrics.FloatMetric values) {
            switch (type) {
                case ALTITUDE:
                    this.mGainedAltitude = values.getGain().floatValue();
                    this.mMaxAltitudeDiff = values.getRange().floatValue();
                    break;
            }
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public void foreachRecord(int numberOfPoints, SavedWorkout.foreachRecordCallback cb) {
            int rideRes = SavedRide.findBestResolution(this.mRideId, this.mStartTime, this.mEndTime, numberOfPoints);
            Cursor c = SQLHelper.getRecordsCursor(this.mRideId, this.mStartTime, this.mEndTime, rideRes);
            SavedRide.foreachRecord(c, numberOfPoints, cb);
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public void foreachMetric(Record.MetricData type, int numberOfPoints, SavedWorkout.foreachMetricCallback cb) {
            int rideRes = 0;
            switch (type) {
                case ALTITUDE:
                case CORRECTED_ALTITUDE:
                    break;
                case NORMALISED_POWER:
                    cb = SavedRide.normalisedPowerCalculator(cb);
                    break;
                default:
                    rideRes = SavedRide.findBestResolution(this.mRideId, this.mStartTime, this.mEndTime, numberOfPoints);
                    break;
            }
            Cursor c = SQLHelper.getMetricsCursor(this.mRideId, this.mStartTime, this.mEndTime, type, rideRes);
            SavedRide.foreachMetric(c, type, numberOfPoints, cb);
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public List<Coordinate> getRouteDetails() {
            List<Coordinate> returnList = new ArrayList<>();
            if (SQLHelper.isReady()) {
                Cursor c = SQLHelper.getCoordsCursor(this.mRideId, this.mStartTime, this.mEndTime);
                while (c.moveToNext()) {
                    Coordinate coord = new Coordinate(c);
                    returnList.add(coord);
                }
                c.close();
            }
            return returnList;
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getAverageStrideForLocale() {
            return Conversion.strideForLocale(getAverageStride());
        }

        @Override // com.kopin.solos.storage.IRidePartSaved
        public double getMaxStrideForLocale() {
            return Conversion.strideForLocale(getMaxStride());
        }

        public Cursor getRecordsCursor() {
            return SQLHelper.getRecordsCursor(this.mRideId, this.mStartTime, this.mEndTime, 0);
        }
    }

    public static class LapData extends Saved {
        public ArrayList<Record> records;

        public LapData(Cursor cursor) {
            super(cursor);
            this.records = new ArrayList<>();
        }

        public void addRecord(Record record) {
            this.records.add(record);
        }
    }

    public static String getCreateFieldsQuery() {
        StringBuilder builder = new StringBuilder();
        for (Field field : Field.values()) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(field.name()).append(" ").append(field.fieldType);
        }
        return builder.toString();
    }

    public static Cursor getCursor(long rideId) {
        return SQLHelper.getLapCursor(rideId);
    }
}
