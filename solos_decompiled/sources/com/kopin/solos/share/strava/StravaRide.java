package com.kopin.solos.share.strava;

import android.content.ContentValues;
import android.location.Location;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Statistics;
import com.kopin.solos.storage.StatsSheet;
import com.kopin.solos.storage.util.Conversion;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.jstrava.entities.activity.Activity;
import org.jstrava.entities.stream.Stream;

/* JADX INFO: loaded from: classes4.dex */
public class StravaRide extends StatsSheet {
    private HashMap<Integer, Double> altitudes;
    private HashMap<Integer, Double> cadences;
    private HashMap<Integer, Double> distances;
    private HashMap<Integer, Double> heartRates;
    private HashMap<Integer, Location> locationList;
    private long mActualStartTime;
    private int mCalories;
    public String mComment;
    private double mDistance;
    private long mDuration;
    private float mGainedAltitude;
    private long mGhostRideId;
    private ImportState mImportState;
    private float mMaxAltitudeDiff;
    private long mSolosId;
    private long mStravaId;
    public String mTitle;
    public String name;
    private HashMap<Integer, Double> power;
    private HashMap<Integer, Double> times;

    public enum ImportState {
        None,
        Importing,
        Imported
    }

    public interface StravaRecordCallback {
        boolean onRecord(long j, double d, int i, double d2, double d3, double d4, float f, Location location);
    }

    public StravaRide(long stravaId, String name, long uploadId) {
        this(stravaId, name, name, uploadId);
    }

    private StravaRide(long stravaId, String name, String title, long localId) {
        this.mSolosId = -1L;
        this.mGhostRideId = -2147483648L;
        this.mDuration = -2147483648L;
        this.mActualStartTime = -2147483648L;
        this.mDistance = -2.147483648E9d;
        this.mCalories = Integer.MIN_VALUE;
        this.mMaxAltitudeDiff = -2.14748365E9f;
        this.mGainedAltitude = -2.14748365E9f;
        this.mTitle = "";
        this.mComment = "";
        this.locationList = new HashMap<>();
        this.times = new HashMap<>();
        this.distances = new HashMap<>();
        this.altitudes = new HashMap<>();
        this.cadences = new HashMap<>();
        this.heartRates = new HashMap<>();
        this.power = new HashMap<>();
        this.mImportState = ImportState.None;
        this.mStravaId = stravaId;
        this.name = name;
        this.mTitle = title;
        this.mSolosId = localId;
        this.mImportState = this.mSolosId != -1 ? ImportState.Imported : ImportState.None;
    }

    public void setSolosId(long id) {
        this.mSolosId = id;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getComment() {
        return this.mComment;
    }

    public void setData(List<Stream> streams) {
        for (Stream stream : streams) {
            setData(stream.getType(), stream.getData());
        }
    }

    private void setData(String streamType, List<Object> data) {
        if (streamType.equals("time")) {
            fillDataDouble(this.times, data, 1000);
        }
        if (streamType.equals("latlng")) {
            fillDataLocation(data);
        }
        if (streamType.equals(BaseDataTypes.ID_DISTANCE)) {
            fillDataDouble(this.distances, data);
        }
        if (streamType.equals("altitude")) {
            fillDataDouble(this.altitudes, data);
        }
        if (streamType.equals("heartrate")) {
            fillDataDouble(this.heartRates, data);
        }
        if (streamType.equals("cadence")) {
            fillDataDouble(this.cadences, data);
        }
        if (streamType.equals("watts")) {
            fillDataDouble(this.power, data);
        }
    }

    public double getEndTime() {
        return ((Double) Collections.max(this.times.values())).doubleValue();
    }

    private void fillDataDouble(HashMap<Integer, Double> listFloat, List<Object> listObject) {
        fillDataDouble(listFloat, listObject, 1);
    }

    private void fillDataDouble(HashMap<Integer, Double> listFloat, List<Object> listObject, int multiplier) {
        listFloat.clear();
        for (int i = 0; i < listObject.size(); i++) {
            Object o = listObject.get(i);
            if (o instanceof Double) {
                listFloat.put(Integer.valueOf(i), Double.valueOf(((Double) o).doubleValue() * ((double) multiplier)));
            }
        }
    }

    private void fillDataInt(List<Integer> listOut, List<Object> listIn) {
        listOut.clear();
        for (Object o : listIn) {
            listOut.add((Integer) o);
        }
    }

    private void fillDataLocation(List<Object> listObject) {
        this.locationList.clear();
        for (int i = 0; i < listObject.size(); i++) {
            Object o = listObject.get(i);
            if (o != null) {
                String s = ("" + o).replace("[", "").replace("]", "");
                int comma = s.indexOf(",");
                if (comma > 0) {
                    String s1 = s.substring(0, comma);
                    String s2 = s.substring(comma + 1);
                    Location location = new Location("strava");
                    location.setLatitude(Double.parseDouble(s1.trim()));
                    location.setLongitude(Double.parseDouble(s2.trim()));
                    this.locationList.put(Integer.valueOf(i), location);
                }
            }
        }
    }

    void foreachRecord(StravaRecordCallback cb) {
        long lastTime = -1;
        double lastDistance = -1.0d;
        for (int index = 0; index < this.times.size(); index++) {
            long time = Math.round(this.times.get(Integer.valueOf(index)).doubleValue());
            double distance = this.distances.get(Integer.valueOf(index)).doubleValue();
            double speed = -2.147483648E9d;
            if (lastTime > -1 && lastDistance > -1.0d && time > lastTime && distance > lastDistance) {
                speed = ((distance - lastDistance) / (time - lastTime)) * 1000.0d;
            }
            lastTime = time;
            lastDistance = distance;
            boolean cont = cb.onRecord(time, this.distances.containsKey(Integer.valueOf(index)) ? this.distances.get(Integer.valueOf(index)).doubleValue() : -2.147483648E9d, (int) Math.round(this.heartRates.containsKey(Integer.valueOf(index)) ? this.heartRates.get(Integer.valueOf(index)).doubleValue() : -2.147483648E9d), speed, this.cadences.containsKey(Integer.valueOf(index)) ? this.cadences.get(Integer.valueOf(index)).doubleValue() : -2.147483648E9d, this.power.containsKey(Integer.valueOf(index)) ? this.power.get(Integer.valueOf(index)).doubleValue() : -2.147483648E9d, (float) (this.altitudes.containsKey(Integer.valueOf(index)) ? this.altitudes.get(Integer.valueOf(index)).doubleValue() : -2.147483648E9d), this.locationList.containsKey(Integer.valueOf(index)) ? this.locationList.get(Integer.valueOf(index)) : null);
            if (!cont) {
                return;
            }
        }
    }

    public void setStats(Activity stravaActivity) {
        setDistance(stravaActivity.getDistance());
        setDuration(stravaActivity.getElapsed_time());
        this.mGainedAltitude = stravaActivity.getTotal_elevation_gain();
        setCalories(stravaActivity.getCalories());
        setSpeed(new Statistics.DoubleStatistic(Double.valueOf(0.0d), Double.valueOf(stravaActivity.getMax_speed()), Double.valueOf(stravaActivity.getAverage_speed())));
    }

    void setStats(Statistics.DoubleStatistic speed, Statistics.DoubleStatistic cadence, Statistics.IntegerStatistic hr, Statistics.DoubleStatistic power, Statistics.DoubleStatistic normPower, float elevRange, float overallClimb) {
        if (getAverageSpeed() == -2.147483648E9d) {
            setSpeed(speed);
        }
        setCadence(cadence);
        setHeartRate(hr);
        setPower(power);
        setNormPower(normPower);
        this.mMaxAltitudeDiff = elevRange;
        if (this.mGainedAltitude == -2.14748365E9f || this.mGainedAltitude == 0.0f) {
            this.mGainedAltitude = overallClimb;
        }
    }

    public long getId() {
        return this.mStravaId;
    }

    public long getSolosId() {
        return this.mSolosId;
    }

    public double getDistance() {
        return this.mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public float getGainedAltitude() {
        return this.mGainedAltitude;
    }

    float getMaxAltitudeDiff() {
        return this.mMaxAltitudeDiff;
    }

    public int getCalories() {
        return this.mCalories;
    }

    public void setCalories(float mCalories) {
        if (mCalories > 0.0d) {
            this.mCalories = (int) mCalories;
        }
    }

    public void setDuration(long time) {
        this.mDuration = 1000 * time;
    }

    public boolean isImported() {
        return this.mImportState == ImportState.Imported;
    }

    public boolean isImporting() {
        return this.mImportState == ImportState.Importing;
    }

    public void setImportState(ImportState state) {
        this.mImportState = state;
    }

    public void setActualStartTime(long startTime) {
        this.mActualStartTime = startTime;
    }

    public long getActualStartTime() {
        return this.mActualStartTime;
    }

    public boolean hasLocations() {
        return this.locationList.size() >= 2;
    }

    void saveRide() {
        ContentValues values = new ContentValues();
        values.put(Ride.AVERAGE_SPEED, Double.valueOf(getAverageSpeed()));
        values.put(Ride.AVERAGE_CADENCE, Double.valueOf(getAverageCadence()));
        values.put(Ride.AVERAGE_HEARTRATE, Integer.valueOf(getAverageHeartrate()));
        values.put(Ride.AVERAGE_POWER, Double.valueOf(getAveragePower()));
        values.put(Ride.AVERAGE_NORM_POWER, Double.valueOf(getAverageNormalisedPower()));
        values.put(Ride.DISTANCE, Integer.valueOf((int) getDistance()));
        values.put(Ride.GAINED_ALTITUDE, Float.valueOf(getGainedAltitude()));
        values.put(Ride.START_TIME_ACTUAL, Long.valueOf(getActualStartTime()));
        values.put(Ride.START_TIME, Double.valueOf(0.0d));
        values.put(Ride.DURATION, Long.valueOf(getDuration()));
        values.put(Ride.END_TIME, Double.valueOf(getEndTime()));
        values.put(Ride.MAX_ALTITUDE_DIFF, Float.valueOf(getMaxAltitudeDiff()));
        values.put(Ride.MAX_CADENCE, Double.valueOf(getMaxCadence()));
        values.put(Ride.MAX_HEARTRATE, Integer.valueOf(getMaxHeartrate()));
        values.put(Ride.MAX_POWER, Double.valueOf(getMaxPower()));
        values.put(Ride.MAX_NORM_POWER, Double.valueOf(getMaxNormalisedPower()));
        values.put(Ride.MAX_SPEED, Double.valueOf(getMaxSpeed()));
        values.put(Ride.CALORIES, Integer.valueOf(getCalories()));
        values.put(Ride.TITLE, getTitle());
        values.put(Ride.COMMENT, getComment());
        values.put(Ride.ACTIVITY, (Integer) 0);
        values.put(Ride.BIKE_ID, (Integer) 0);
        values.put(Ride.RIDER_ID, (Integer) 0);
        values.put(Ride.AVERAGE_OXYGEN, (Integer) Integer.MIN_VALUE);
        values.put(Ride.MIN_OXYGEN, (Integer) Integer.MIN_VALUE);
        values.put(Ride.TARGET_AVERAGE_CADENCE, (Integer) Integer.MIN_VALUE);
        values.put(Ride.TARGET_AVERAGE_SPEED, Double.valueOf(-2.147483648E9d));
        values.put(Ride.TARGET_AVERAGE_POWER, (Integer) Integer.MIN_VALUE);
        values.put(Ride.TARGET_AVERAGE_HEARTRATE, (Integer) Integer.MIN_VALUE);
        values.put(Ride.AVERAGE_INTENSITY, Double.valueOf(-2.147483648E9d));
        values.put(Ride.MAX_INTENSITY, Double.valueOf(-2.147483648E9d));
        values.put(Ride.TSS, Double.valueOf(-2.147483648E9d));
        values.put(Ride.FINAL, (Integer) 0);
        SQLHelper.updateRide(getSolosId(), values);
    }

    void saveRun() {
        ContentValues values = new ContentValues();
        values.put(Run.Field.AVG_PACE.name(), Double.valueOf(Conversion.speedToPace(getAverageSpeed())));
        values.put(Run.Field.AVG_CADENCE.name(), Double.valueOf(getAverageCadence()));
        values.put(Run.Field.AVG_HEARTRATE.name(), Integer.valueOf(getAverageHeartrate()));
        values.put(Run.Field.AVG_POWER.name(), Double.valueOf(getAveragePower()));
        values.put(Run.Field.AVG_NORMALISED_POWER.name(), Double.valueOf(getAverageNormalisedPower()));
        values.put(Run.Field.DISTANCE.name(), Integer.valueOf((int) getDistance()));
        values.put(Run.Field.OVERALL_CLIMB.name(), Float.valueOf(getGainedAltitude()));
        values.put(Run.Field.START_TIME.name(), Long.valueOf(getActualStartTime()));
        values.put(Run.Field.DURATION.name(), Long.valueOf(getDuration()));
        values.put(Run.Field.END_TIME.name(), Double.valueOf(getEndTime()));
        values.put(Run.Field.ALTITUDE_RANGE.name(), Float.valueOf(getMaxAltitudeDiff()));
        values.put(Run.Field.MAX_CADENCE.name(), Double.valueOf(getMaxCadence()));
        values.put(Run.Field.MAX_HEARTRATE.name(), Integer.valueOf(getMaxHeartrate()));
        values.put(Run.Field.MAX_POWER.name(), Double.valueOf(getMaxPower()));
        values.put(Run.Field.MAX_NORMALISED_POWER.name(), Double.valueOf(getMaxNormalisedPower()));
        values.put(Run.Field.MAX_PACE.name(), Double.valueOf(Conversion.speedToPace(getMaxSpeed())));
        values.put(Run.Field.CALORIES.name(), Integer.valueOf(getCalories()));
        values.put(Run.Field.TITLE.name(), getTitle());
        values.put(Run.Field.COMMENT.name(), getComment());
        values.put(Run.Field.GEAR_ID.name(), (Integer) 0);
        values.put(Run.Field.RUNNER_ID.name(), (Integer) 0);
        values.put(Run.Field.AVG_OXYGEN.name(), (Integer) Integer.MIN_VALUE);
        values.put(Run.Field.MIN_OXYGEN.name(), (Integer) Integer.MIN_VALUE);
        values.put(Run.Field.TARGET_CADENCE.name(), (Integer) Integer.MIN_VALUE);
        values.put(Run.Field.TARGET_PACE.name(), Double.valueOf(-2.147483648E9d));
        values.put(Run.Field.TARGET_POWER.name(), (Integer) Integer.MIN_VALUE);
        values.put(Run.Field.TARGET_HEARTRATE.name(), (Integer) Integer.MIN_VALUE);
        values.put(Run.Field.AVG_IF.name(), Double.valueOf(-2.147483648E9d));
        values.put(Run.Field.MAX_IF.name(), Double.valueOf(-2.147483648E9d));
        values.put(Run.Field.AVG_STRIDE.name(), Double.valueOf(-2.147483648E9d));
        values.put(Run.Field.MAX_STRIDE.name(), Double.valueOf(-2.147483648E9d));
        values.put(Run.Field.FINAL.name(), (Integer) 0);
        SQLHelper.updateRun(getSolosId(), values);
    }
}
