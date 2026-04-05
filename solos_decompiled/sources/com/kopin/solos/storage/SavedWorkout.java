package com.kopin.solos.storage;

import android.database.Cursor;
import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public abstract class SavedWorkout extends StatsSheet implements IRidePartSaved, ISyncable {
    private static final String PREFIX_RIDE = "ride_";
    private static final String PREFIX_RUN = "run_";
    public static final int RIDE_ALL_RECORDS = 0;
    public static final int RIDE_RESOLUTION_ALL = -1;
    public static final int RIDE_RESOLUTION_DEFAULT = 0;
    private static final String TAG = SavedWorkout.class.getSimpleName();
    protected int mActivity;
    protected long mActualStartTime;
    protected long mBikeId;
    protected int mCalories;
    protected String mComment;
    protected int mComplete;
    protected double mDistance;
    protected long mDuration;
    protected int mEffectivePeakHR;
    protected long mEndTime;
    protected double mFunctionalThresholdPower;
    protected float mGainedAltitude;
    protected long mGearId;
    protected long mGhostId;
    protected boolean mHasLocations;
    protected long mId;
    protected float mMaxAltitudeDiff;
    protected Workout.RideMode mMode;
    protected long mRiderId;
    protected long mRouteId;
    protected long mStartTime;
    protected int mTargetAverageCadence;
    protected int mTargetAverageHeartrate;
    protected int mTargetAveragePower;
    protected double mTargetAverageSpeedKm;
    protected String mTitle;
    protected double mTrainingStress;

    public interface ForeachExtendedRecordCallback {
        boolean onRecordValues(long j, HashMap<String, Number> map, Coordinate coordinate);
    }

    public interface foreachLapCallback {
        boolean onLap(Lap.Saved saved);
    }

    public interface foreachMetricCallback {
        boolean onMetric(long j, double d, int i, long j2);
    }

    public interface foreachRecordCallback {
        boolean onRecord(Record record);
    }

    public abstract SportType getSportType();

    public abstract Shared toShared(int i, String str, String str2);

    public SavedWorkout(Workout workout) {
        super(workout);
        this.mId = -2147483648L;
        this.mRouteId = -1L;
        this.mGhostId = -1L;
        this.mStartTime = -2147483648L;
        this.mDuration = -2147483648L;
        this.mEndTime = -2147483648L;
        this.mActualStartTime = -2147483648L;
        this.mDistance = -2.147483648E9d;
        this.mCalories = Integer.MIN_VALUE;
        this.mMaxAltitudeDiff = -2.14748365E9f;
        this.mGainedAltitude = -2.14748365E9f;
        this.mTitle = null;
        this.mComment = null;
        this.mFunctionalThresholdPower = -2.147483648E9d;
        this.mTrainingStress = -2.147483648E9d;
        this.mEffectivePeakHR = Integer.MIN_VALUE;
        this.mHasLocations = false;
    }

    public SavedWorkout() {
        this.mId = -2147483648L;
        this.mRouteId = -1L;
        this.mGhostId = -1L;
        this.mStartTime = -2147483648L;
        this.mDuration = -2147483648L;
        this.mEndTime = -2147483648L;
        this.mActualStartTime = -2147483648L;
        this.mDistance = -2.147483648E9d;
        this.mCalories = Integer.MIN_VALUE;
        this.mMaxAltitudeDiff = -2.14748365E9f;
        this.mGainedAltitude = -2.14748365E9f;
        this.mTitle = null;
        this.mComment = null;
        this.mFunctionalThresholdPower = -2.147483648E9d;
        this.mTrainingStress = -2.147483648E9d;
        this.mEffectivePeakHR = Integer.MIN_VALUE;
        this.mHasLocations = false;
    }

    public long getId() {
        return this.mId;
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public long getRideId() {
        return this.mId;
    }

    public long getRouteId() {
        return this.mRouteId;
    }

    public long getGhostRideId() {
        if (this.mMode == Workout.RideMode.GHOST_RIDE) {
            return this.mGhostId;
        }
        return -1L;
    }

    public long getGhostRouteId() {
        if (this.mMode == Workout.RideMode.ROUTE) {
            return this.mGhostId;
        }
        return -1L;
    }

    public long getVirtualWorkoutId() {
        return this.mGhostId;
    }

    public Workout.RideMode getWorkoutMode() {
        return this.mMode;
    }

    @Override // com.kopin.solos.storage.IRidePartData
    public long getStartTime() {
        return this.mStartTime;
    }

    @Override // com.kopin.solos.storage.IRidePartData
    public long getActualStartTime() {
        return this.mActualStartTime;
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
    public float getGainedAltitude() {
        return this.mGainedAltitude;
    }

    public float getMaxAltitudeDiff() {
        return this.mMaxAltitudeDiff;
    }

    @Override // com.kopin.solos.storage.IRidePartData
    public int getCalories() {
        return this.mCalories;
    }

    public double getFunctionalThresholdPower() {
        return this.mFunctionalThresholdPower;
    }

    public double getTrainingStress() {
        return this.mTrainingStress;
    }

    public String getComment() {
        return this.mComment;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getOrGenerateTitle() {
        String prefix;
        if (this instanceof SavedRun) {
            prefix = PREFIX_RUN;
        } else {
            prefix = PREFIX_RIDE;
        }
        return (this.mTitle == null || this.mTitle.trim().isEmpty()) ? prefix + Utility.formatDateAndTimeCompact(this.mActualStartTime) : this.mTitle;
    }

    public boolean isGeneratedTitle() {
        return isGeneratedTitle(getOrGenerateTitle());
    }

    public static boolean isGeneratedTitle(String title) {
        int i = 0;
        if (title != null && ((title.startsWith(PREFIX_RIDE) || title.startsWith(PREFIX_RUN)) && (title.length() == 16 || title.length() == 17))) {
            try {
                i = Integer.parseInt(title.substring(6));
            } catch (NumberFormatException e) {
            }
        }
        return i > 0;
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

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getMaxSpeedForLocale() {
        return Conversion.speedForLocale(getMaxSpeed());
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getDistanceForLocale() {
        return Utility.convertToUserUnits(1, this.mDistance / 1000.0d);
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public float getGainedAltitudeForLocale() {
        if (this.mGainedAltitude == -2.14748365E9f) {
            return -2.14748365E9f;
        }
        return (float) Utility.convertToUserUnits(0, this.mGainedAltitude);
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getMaxAltitudeDiffForLocale() {
        return Utility.convertToUserUnits(0, this.mMaxAltitudeDiff);
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getAverageStrideForLocale() {
        return Conversion.strideForLocale(getAverageStride());
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getMaxStrideForLocale() {
        return Conversion.strideForLocale(getMaxStride());
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getAveragePaceForLocale() {
        return Conversion.paceForLocale(getAveragePace());
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public double getMaxPaceForLocale() {
        return Conversion.paceForLocale(getMaxPace());
    }

    public boolean hasComment() {
        return (this.mComment == null || this.mComment.trim().isEmpty()) ? false : true;
    }

    public boolean hasTitle() {
        return (this.mTitle == null || this.mTitle.trim().isEmpty()) ? false : true;
    }

    public boolean isComplete() {
        return this.mComplete != 0;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setBikeId(long localBikeId) {
        this.mBikeId = localBikeId;
    }

    public long getBikeId() {
        return this.mBikeId;
    }

    public void setBikeType(int bikeType) {
        this.mBikeId = bikeType;
    }

    public void setGearId(long localGearId) {
        this.mGearId = localGearId;
    }

    public long getGearId() {
        return this.mGearId;
    }

    public void setRiderId(long riderId) {
        this.mRiderId = riderId;
    }

    public long getRiderId() {
        return this.mRiderId;
    }

    public void setActivity(int rideType) {
        this.mActivity = rideType;
    }

    public int getActivity() {
        return this.mActivity;
    }

    public int getTargetAverageCadence() {
        return this.mTargetAverageCadence;
    }

    public int getTargetAveragePower() {
        return this.mTargetAveragePower;
    }

    public int getTargetAverageHeartrate() {
        return this.mTargetAverageHeartrate;
    }

    public double getTargetAverageSpeedKm() {
        return this.mTargetAverageSpeedKm;
    }

    public int getEffectivePeakHR() {
        return this.mEffectivePeakHR;
    }

    @Override // com.kopin.solos.storage.StatsSheet
    public String toString() {
        return "{ id: " + this.mId + ", start time: " + this.mStartTime + ", duration: " + this.mDuration + ", end time: " + this.mEndTime + ", average speed: " + getAverageSpeed() + " true avg: " + getTrueAverageSpeed() + ", distance: " + this.mDistance + ", calories: " + this.mCalories + ", max[altitudeDiff: " + this.mMaxAltitudeDiff + ", cadence: " + getMaxCadence() + ", heartRate: " + getMaxHeartrate() + ", speed: " + getMaxSpeed() + ", power: , average oxygen: " + getAverageOxygen() + ", max oxygen: " + getMaxOxygen() + ", min oxygen: " + getMinOxygen() + ", power: " + getMaxPower() + "], title: " + this.mTitle + ", bike: N/A, rider: N/Aride: " + this.mActivity + ",comment: " + this.mComment + " }";
    }

    public boolean hasRoute() {
        return SQLHelper.hasLocations(getSportType(), this.mId);
    }

    public boolean hasCorrectedElevation() {
        return SQLHelper.hasCorrectedElevation(this.mId);
    }

    public void setRouteId(long routeId) {
        this.mRouteId = routeId;
    }

    public boolean hasLocations() {
        return this.mHasLocations;
    }

    public int numLaps() {
        return SQLHelper.countLaps(this.mId, true);
    }

    public Lap.Saved getLap(int lap) {
        return SQLHelper.getLap(this.mId, lap);
    }

    public List<Lap.Saved> getLaps(int start, int count) {
        return SQLHelper.getLaps(this.mId, start, count);
    }

    public ArrayList<Lap.Saved> getLaps() {
        ArrayList<Lap.Saved> laps = new ArrayList<>();
        Cursor c = SQLHelper.getLapCursor(this.mId);
        while (c.moveToNext()) {
            laps.add(new Lap.Saved(c));
        }
        c.close();
        return laps;
    }

    public void setMetricStats(Record.MetricData type, Metrics.FloatMetric values) {
        switch (type) {
            case ALTITUDE:
                this.mGainedAltitude = values.getGain().floatValue();
                this.mMaxAltitudeDiff = values.getRange().floatValue();
                break;
        }
    }

    public static long getIdFromCursor(Cursor cursor) {
        int pos = cursor.getColumnIndex("_id");
        if (pos != -1) {
            return cursor.getLong(pos);
        }
        return -1L;
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public void foreachRecord(int numberOfPoints, foreachRecordCallback cb) {
        int rideRes = findBestResolution(this.mId, -1L, -1L, numberOfPoints);
        Cursor c = SQLHelper.getRecordsCursor(this.mId, rideRes);
        foreachRecord(c, numberOfPoints, cb);
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public void foreachMetric(Record.MetricData type, int numberOfPoints, foreachMetricCallback cb) {
        int rideRes = 0;
        switch (type) {
            case ALTITUDE:
            case CORRECTED_ALTITUDE:
                break;
            case NORMALISED_POWER:
                cb = normalisedPowerCalculator(cb);
                break;
            default:
                rideRes = findBestResolution(this.mId, -1L, -1L, numberOfPoints);
                break;
        }
        Cursor c = SQLHelper.getMetricsCursor(this.mId, -1L, -1L, type, rideRes);
        foreachMetric(c, type, numberOfPoints, cb);
    }

    static int findBestResolution(long rideId, long start, long end, int targetPoints) {
        if (targetPoints == 0) {
            return 0;
        }
        Log.d(TAG, "find best ride resolution to generate " + targetPoints + " points");
        int closestRes = 0;
        int diff = -1;
        HashMap<Integer, Integer> resMap = SQLHelper.getRecordResolutions(rideId, start, end);
        for (Integer res : resMap.keySet()) {
            int dataPoints = resMap.get(res).intValue();
            Log.d(TAG, "  resolution " + res + " has " + dataPoints + " data points");
            if (diff == -1 || Math.abs(dataPoints - targetPoints) < diff) {
                diff = Math.abs(dataPoints - targetPoints);
                closestRes = res.intValue();
            }
        }
        Log.d(TAG, " returning resolution: " + closestRes);
        return closestRes;
    }

    public void foreachRecord(long periodStart, long periodEnd, foreachRecordCallback cb) {
        Cursor c = SQLHelper.getRecordsCursor(this.mId, periodStart, periodEnd, 0);
        foreachRecord(c, 0, cb);
    }

    public void foreachRecord(foreachRecordCallback cb) {
        Cursor c = SQLHelper.getRecordsCursor(this.mId, 0);
        foreachRecord(c, 0, cb);
    }

    static void foreachRecord(Cursor c, int targetPoints, foreachRecordCallback cb) {
        if (c != null && cb != null && SQLHelper.isReady()) {
            boolean cont = true;
            int skip = targetPoints == 0 ? 0 : c.getCount() / targetPoints;
            int count = 0;
            while (c.moveToNext() && cont) {
                try {
                    if (count == 0) {
                        count = skip;
                        Record record = new Record(c);
                        cont = cb.onRecord(record);
                    } else {
                        count--;
                    }
                } catch (IllegalStateException ise) {
                    Log.e(TAG, "Abandoning foreachRecord due to exception: " + ise.getMessage());
                }
            }
            c.close();
        }
    }

    static void foreachMetric(Cursor c, Record.MetricData type, int targetPoints, foreachMetricCallback cb) {
        if (c != null && cb != null && SQLHelper.isReady()) {
            int skip = targetPoints == 0 ? 0 : c.getCount() / targetPoints;
            int count = 0;
            while (c.moveToNext() && 1 != 0) {
                try {
                    if (count == 0) {
                        count = skip;
                        Record.Field metric = Record.Field.getFieldForMetric(type);
                        if (metric != null) {
                            long timestamp = Record.Field.TIMESTAMP.getLong(c);
                            double realVal = metric.getDouble(c);
                            if (Ride.hasData(realVal)) {
                                int intVal = metric.getInt(c);
                                switch (type) {
                                    case ALTITUDE:
                                    case CORRECTED_ALTITUDE:
                                        realVal = Utility.convertToUserUnits(0, realVal);
                                        intVal = (int) realVal;
                                        break;
                                    case SPEED:
                                        realVal = Conversion.speedForLocale(realVal);
                                        intVal = (int) realVal;
                                        break;
                                    case STRIDE:
                                        realVal = Conversion.strideForLocale(realVal);
                                        intVal = (int) realVal;
                                        break;
                                    case PACE:
                                        realVal = Conversion.speedToPaceForLocale(realVal);
                                        intVal = (int) realVal;
                                        break;
                                }
                                int resolution = Record.Field.RESOLUTION.getInt(c);
                                cb.onMetric(timestamp, realVal, intVal, (skip + 1) * (resolution + 1) * 4 * 1000);
                            }
                        }
                    } else {
                        count--;
                    }
                } catch (IllegalStateException ise) {
                    Log.e(TAG, "Abandoning foreachMetric due to exception: " + ise.getMessage());
                }
            }
            c.close();
        }
    }

    @Override // com.kopin.solos.storage.IRidePartSaved
    public List<Coordinate> getRouteDetails() {
        List<Coordinate> returnList = new ArrayList<>();
        if (SQLHelper.isReady()) {
            Cursor c = SQLHelper.getCoordsCursor(this.mId, -1L, -1L);
            while (c.moveToNext()) {
                Coordinate coord = new Coordinate(c);
                if (coord.hasData() && coord.getLatitude() != 0.0d && coord.getLongitude() != 0.0d) {
                    returnList.add(coord);
                }
            }
            c.close();
        }
        return returnList;
    }

    public void foreachExtendedRecord(HashMap<Record.MetricData, String> keyMap, ForeachExtendedRecordCallback cb) {
        Cursor c;
        if (cb != null && SQLHelper.isReady() && (c = SQLHelper.getExtendedRecordsCursor(this.mId, 0)) != null) {
            boolean cont = true;
            FieldHelper.setDefault(Integer.MIN_VALUE);
            try {
                HashMap<String, Number> values = new HashMap<>();
                while (c.moveToNext() && cont) {
                    long timestamp = Record.Field.TIMESTAMP.getLong(c);
                    for (Record.MetricData metric : keyMap.keySet()) {
                        Record.Field field = Record.Field.getFieldForMetric(metric);
                        if (field != null) {
                            switch (field.fieldType) {
                                case INTEGER:
                                    int intVal = field.getInt(c);
                                    if (intVal != Integer.MIN_VALUE) {
                                        values.put(keyMap.get(metric), Integer.valueOf(intVal));
                                    }
                                    break;
                                case REAL:
                                    double realVal = field.getDouble(c);
                                    if (realVal != -2.147483648E9d) {
                                        values.put(keyMap.get(metric), Double.valueOf(realVal));
                                    }
                                    break;
                            }
                        }
                    }
                    Coordinate position = new Coordinate(c);
                    cont = cb.onRecordValues(timestamp, values, position);
                    values.clear();
                }
            } catch (IllegalStateException ise) {
                Log.e(TAG, "Abandoning foreachRecord due to exception: " + ise.getMessage());
            }
            FieldHelper.setDefault(0);
            c.close();
        }
    }

    static foreachMetricCallback normalisedPowerCalculator(final foreachMetricCallback chainedCb) {
        return new foreachMetricCallback() { // from class: com.kopin.solos.storage.SavedWorkout.1
            NormalisedPower mNormalisedPowerCalc = new NormalisedPower();

            @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
            public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                if (timestamp >= 0) {
                    double np = this.mNormalisedPowerCalc.calculateNormalisedPower(realValue, timestamp);
                    if (Ride.hasData(np)) {
                        return chainedCb.onMetric(timestamp, np, (int) np, dataValidity);
                    }
                }
                return true;
            }
        };
    }
}
