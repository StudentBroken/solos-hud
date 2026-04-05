package com.kopin.solos.share.peloton;

import android.content.ContentValues;
import android.util.Log;
import com.kopin.peloton.ride.Lap;
import com.kopin.peloton.ride.Metric;
import com.kopin.peloton.ride.MetricStat;
import com.kopin.peloton.ride.RideRecord;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import java.util.HashMap;

/* JADX INFO: loaded from: classes4.dex */
public class PelotonConversion {
    private static final String TAG = "PelotonConversion";

    public static ContentValues getSolosLap(Lap lap) {
        ContentValues values = new ContentValues();
        if (lap != null) {
            values.put(Lap.Field.START_TIME.name(), Long.valueOf(lap.StartTime));
            values.put(Lap.Field.END_TIME.name(), Long.valueOf(lap.EndTime));
            values.put(Lap.Field.DURATION.name(), Long.valueOf(lap.Duration));
            values.put(Lap.Field.DISTANCE.name(), Integer.valueOf((int) lap.Distance));
            values.put(Lap.Field.FINAL.name(), (Integer) 1);
            values.put(Lap.Field.CALORIES.name(), Double.valueOf(lap.Stats.Calories));
            PelotonHelper.putAveMax(values, lap.Stats.Cadence, Lap.Field.AVERAGE_CADENCE.name(), Lap.Field.MAX_CADENCE.name());
            PelotonHelper.putAveMax(values, lap.Stats.HeartRate, Lap.Field.AVERAGE_HEARTRATE.name(), Lap.Field.MAX_HEARTRATE.name());
            PelotonHelper.putAveMax(values, lap.Stats.IntensityFactor, Lap.Field.AVERAGE_INTENSITY.name(), Lap.Field.MAX_INTENSITY.name());
            PelotonHelper.putAveMax(values, lap.Stats.NormalisedPower, Lap.Field.AVERAGE_NORM_POWER.name(), Lap.Field.MAX_NORM_POWER.name());
            PelotonHelper.putAveMax(values, lap.Stats.Power, Lap.Field.AVERAGE_POWER.name(), Lap.Field.MAX_POWER.name());
            PelotonHelper.putAveMax(values, lap.Stats.Speed, Lap.Field.AVERAGE_SPEED.name(), Lap.Field.MAX_SPEED.name());
            PelotonHelper.putAveMax(values, lap.Stats.Stride, Lap.Field.AVERAGE_STRIDE.name(), Lap.Field.MAX_STRIDE.name());
            PelotonHelper.putAveMin(values, lap.Stats.Oxygenation, Lap.Field.AVERAGE_OXYGEN.name(), Lap.Field.MIN_OXYGEN.name());
            values.put(Lap.Field.GAINED_ALTITUDE.name(), Double.valueOf(lap.Stats.OverallClimb != null ? lap.Stats.OverallClimb.doubleValue() : -2.147483648E9d));
            values.put(Lap.Field.MAX_ALTITUDE_DIFF.name(), Double.valueOf(lap.Stats.AltitudeRange != null ? lap.Stats.AltitudeRange.doubleValue() : -2.147483648E9d));
            Log.d(TAG, "peloton lap _ " + lap.StartTime + " : " + lap.EndTime);
        }
        return values;
    }

    public static com.kopin.peloton.ride.Lap getPelotonLap(Lap.Saved solosLap) {
        com.kopin.peloton.ride.Lap lap = new com.kopin.peloton.ride.Lap();
        if (solosLap != null) {
            lap.StartTime = solosLap.getStartTime();
            lap.EndTime = solosLap.getEndTime();
            lap.Duration = solosLap.getDuration();
            lap.Distance = solosLap.getDistance();
            lap.Stats.Cadence = makeMetric(solosLap.getMaxCadence(), solosLap.getAverageCadence());
            lap.Stats.HeartRate = makeMetric(solosLap.getMaxHeartrate(), solosLap.getAverageHeartrate());
            lap.Stats.IntensityFactor = makeMetric(solosLap.getMaxIntensity(), solosLap.getAverageIntensity());
            lap.Stats.NormalisedPower = makeMetric(solosLap.getMaxNormalisedPower(), solosLap.getAverageNormalisedPower());
            lap.Stats.Power = makeMetric(solosLap.getMaxPower(), solosLap.getAveragePower());
            lap.Stats.Speed = makeMetric(solosLap.getMaxSpeed(), solosLap.getAverageSpeed());
            lap.Stats.Stride = makeMetric(solosLap.getMaxStride(), solosLap.getAverageStride());
            lap.Stats.Oxygenation = makeMetric(100, solosLap.getAverageOxygen(), solosLap.getMinOxygen());
            lap.Stats.Calories = solosLap.getCalories() == Integer.MIN_VALUE ? 0.0d : solosLap.getCalories();
            if (solosLap.getGainedAltitude() != -2.14748365E9f) {
                lap.Stats.OverallClimb = Double.valueOf(solosLap.getGainedAltitude());
            }
            if (solosLap.getMaxAltitudeDiff() != -2.14748365E9f) {
                lap.Stats.AltitudeRange = Double.valueOf(solosLap.getMaxAltitudeDiff());
            }
            Log.d(TAG, "solos lap _ " + solosLap.getStartTime() + " : " + solosLap.getEndTime());
        }
        return lap;
    }

    public static MetricStat makeMetric(int max, int ave, int min) {
        if (!isValid(Integer.valueOf(max), Integer.valueOf(ave), Integer.valueOf(min))) {
            return null;
        }
        MetricStat stat = new MetricStat(max, ave);
        stat.Minimum = min;
        return stat;
    }

    public static MetricStat makeMetric(double max, double ave) {
        if (isValid(Double.valueOf(max), Double.valueOf(ave))) {
            return new MetricStat(max, ave);
        }
        return null;
    }

    public static MetricStat makeMetric(double max, double ave, double multiplier) {
        if (isValid(Double.valueOf(max), Double.valueOf(ave))) {
            return new MetricStat(max * multiplier, ave * multiplier);
        }
        return null;
    }

    public static double validOrZero(double val) {
        if (isValid(Double.valueOf(val))) {
            return val;
        }
        return 0.0d;
    }

    public enum PelotonMetric {
        Unknown,
        Cadence,
        HeartRate,
        Oxygenation,
        Power,
        Speed,
        Distance,
        CorrectedElevation,
        Stride;

        static PelotonMetric get(String name) {
            for (PelotonMetric pelotonMetric : values()) {
                if (pelotonMetric.name().equalsIgnoreCase(name)) {
                    return pelotonMetric;
                }
            }
            return Unknown;
        }

        static HashMap<Record.MetricData, String> getConversionMap() {
            HashMap<Record.MetricData, String> map = new HashMap<>();
            map.put(Record.MetricData.DISTANCE, Distance.name());
            map.put(Record.MetricData.SPEED, Speed.name());
            map.put(Record.MetricData.CADENCE, Cadence.name());
            map.put(Record.MetricData.HEARTRATE, HeartRate.name());
            map.put(Record.MetricData.POWER, Power.name());
            map.put(Record.MetricData.OXYGEN, Oxygenation.name());
            map.put(Record.MetricData.CORRECTED_ALTITUDE, CorrectedElevation.name());
            map.put(Record.MetricData.STRIDE, Stride.name());
            return map;
        }
    }

    public static Record getSolosRecord(RideRecord pelotonRecord) {
        Record record = new Record(pelotonRecord.Timestamp);
        if (pelotonRecord.Position != null) {
            record.setLocation(new Coordinate(pelotonRecord.Position.Latitude, pelotonRecord.Position.Longitude));
            record.setAltitude((float) pelotonRecord.Position.Altitude);
        }
        for (Metric metric : pelotonRecord.Values) {
            if (metric.Name != null && (metric.ValueDouble != null || metric.ValueInteger != null)) {
                double d = metric.ValueDouble != null ? metric.ValueDouble.doubleValue() : metric.ValueInteger.intValue();
                switch (PelotonMetric.get(metric.Name)) {
                    case Cadence:
                        record.setCadence(d);
                        break;
                    case HeartRate:
                        record.setHeartrate((int) d);
                        break;
                    case Oxygenation:
                        record.setOxygen((int) d);
                        break;
                    case Power:
                        record.setPower(d);
                        break;
                    case Speed:
                        record.setSpeed(d);
                        break;
                }
            }
        }
        Log.v(TAG, "getSolosRecord _ " + pelotonRecord.Timestamp);
        return record;
    }

    public static RideRecord getPelotonRecord(Record solosRecord) {
        RideRecord record = new RideRecord();
        record.Timestamp = solosRecord.getTimestamp();
        Log.v(TAG, "getPelotonRecord _ " + solosRecord.getTimestamp());
        if (solosRecord.hasLocation()) {
            record.Position = solosRecord.hasAltitude() ? new com.kopin.peloton.ride.Coordinate(solosRecord.getLatitude(), solosRecord.getLongitude(), solosRecord.getAltitude()) : new com.kopin.peloton.ride.Coordinate(solosRecord.getLatitude(), solosRecord.getLongitude());
        }
        addRecordValue(record, PelotonMetric.Cadence.name(), solosRecord.getCadence());
        addRecordValue(record, PelotonMetric.HeartRate.name(), solosRecord.getHeartrate());
        addRecordValue(record, PelotonMetric.Oxygenation.name(), solosRecord.getOxygen());
        addRecordValue(record, PelotonMetric.Power.name(), solosRecord.getPower());
        addRecordValue(record, PelotonMetric.Speed.name(), solosRecord.getSpeed());
        return record;
    }

    static RideRecord getPelotonRecord(long timestamp, HashMap<String, Number> values, Coordinate position) {
        RideRecord record = new RideRecord();
        record.Timestamp = timestamp;
        if (position != null && position.hasData()) {
            record.Position = position.hasAltitude() ? new com.kopin.peloton.ride.Coordinate(position.getLatitude(), position.getLongitude(), position.getAltitude()) : new com.kopin.peloton.ride.Coordinate(position.getLatitude(), position.getLongitude());
        }
        for (String name : values.keySet()) {
            Number value = values.get(name);
            if (value instanceof Integer) {
                addRecordValue(record, name, value.intValue());
            } else if (value instanceof Double) {
                addRecordValue(record, name, value.doubleValue());
            }
        }
        return record;
    }

    private static void addRecordValue(RideRecord record, String key, int value) {
        if (isValid(Integer.valueOf(value))) {
            record.Values.add(new Metric(key, value));
        }
    }

    private static void addRecordValue(RideRecord record, String key, double value) {
        if (isValid(Double.valueOf(value))) {
            record.Values.add(new Metric(key, value));
        }
    }

    private static void addRecordValue(RideRecord record, PelotonMetric key, double value, double multiplier) {
        if (isValid(Double.valueOf(value))) {
            record.Values.add(new Metric(key.name(), value * multiplier));
        }
    }

    private static boolean isValid(Integer... ints) {
        for (Integer i : ints) {
            if (i == null || i.intValue() == Integer.MIN_VALUE) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(Float... floats) {
        for (Float f : floats) {
            if (f == null || f.floatValue() == -2.14748365E9f || Float.isInfinite(f.floatValue()) || Float.isNaN(f.floatValue())) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValid(Double... doubles) {
        for (Double d : doubles) {
            if (d == null || d.doubleValue() == -2.147483648E9d || Double.isInfinite(d.doubleValue()) || Double.isNaN(d.doubleValue())) {
                return false;
            }
        }
        return true;
    }
}
