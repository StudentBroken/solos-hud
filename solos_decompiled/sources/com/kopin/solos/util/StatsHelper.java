package com.kopin.solos.util;

import android.os.AsyncTask;
import android.util.Log;
import com.kopin.solos.core.R;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.share.Config;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes37.dex */
public class StatsHelper {
    public static final double HOURS_MILLISECONDS = 3600000.0d;
    private static AsyncTask<Period, Void, Void> sStatsProcessingTask;

    public static class Stats {
        public int mRideCount = 0;
        public double mTotalDistance = 0.0d;
        public long mDuration = 0;
        public float mElevation = 0.0f;
        public int mCalories = 0;
        public AvgAccumulator mAvgSpeed = new AvgAccumulator();
        public AvgAccumulator mAvgPower = new AvgAccumulator();
        public AvgAccumulator mAvgCadence = new AvgAccumulator();
        public AvgAccumulator mAvgHeartRate = new AvgAccumulator();
        public AvgAccumulator mAvgOxygen = new AvgAccumulator();
        public AvgAccumulator mAvgStride = new AvgAccumulator();
    }

    public interface StatsResultCallback {
        void onStatsFetchCancelled();

        void onStatsProgressUpdate(List<MetricStat> list);

        void onStatsResult(List<MetricStat> list);
    }

    private static class AvgAccumulator {
        private double mTotal = 0.0d;
        private long mTime = 0;

        public void addTotal(double total, long time) {
            this.mTime += time;
            this.mTotal += total;
        }

        public void addAverage(double avgVal, long time) {
            this.mTime += time;
            this.mTotal += time * avgVal;
        }

        public double getAverage() {
            if (this.mTime == 0) {
                return 0.0d;
            }
            return this.mTotal / this.mTime;
        }
    }

    public static class MetricStat {
        public String stat;
        public int titleRes;
        public int unitRes;

        MetricStat(int titleRes, String stat, int unitRes) {
            this.titleRes = titleRes;
            this.stat = stat;
            this.unitRes = unitRes;
        }
    }

    public enum Period {
        DAY(-1, R.string.stats_interval_last_24_h),
        WEEK(-7, R.string.stats_interval_last_7_days),
        MONTH(-30, R.string.stats_interval_last_30_days),
        QUARTER(-90, R.string.stats_interval_last_90_days),
        YEAR(-365, R.string.stats_interval_last_365_days),
        FOREVER(-365000, R.string.stats_interval_all_time);

        private int mLabel;
        private int mTime;

        Period(int time, int labelRes) {
            this.mTime = 0;
            this.mTime = time;
            this.mLabel = labelRes;
        }

        public int getLabel() {
            return this.mLabel;
        }

        public long getDate() {
            Calendar calendar = Calendar.getInstance();
            if (Config.STATS_CAPPED_TO_MIDNIGHT) {
                if (this != DAY) {
                    calendar.add(5, this.mTime + 1);
                }
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
            } else {
                calendar.add(5, this.mTime);
            }
            return calendar.getTimeInMillis();
        }
    }

    private static class StatsTask extends AsyncTask<Period, Void, Void> {
        private static final String TAG = StatsTask.class.getSimpleName();
        private StatsResultCallback mResultCallback;
        List<MetricStat> metricStats = new ArrayList();

        public StatsTask(StatsResultCallback resultCallback) {
            this.mResultCallback = resultCallback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Period... params) {
            final Stats stats = new Stats();
            SavedRides.foreachWorkout(LiveRide.getCurrentSport(), params[0].getDate(), new SQLHelper.foreachWorkoutCallback() { // from class: com.kopin.solos.util.StatsHelper.StatsTask.1
                @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCallback
                public boolean onWorkout(SavedWorkout workout) {
                    if (!SQLHelper.wasImportedExceptPlatform(workout.getId(), Config.SYNC_PROVIDER.getSharedKey())) {
                        long rideDuration = workout.getDuration();
                        if (Ride.hasData(workout.getDistance())) {
                            stats.mTotalDistance += workout.getDistance();
                            stats.mAvgSpeed.addTotal(workout.getDistance(), rideDuration);
                        }
                        if (rideDuration > 0) {
                            stats.mDuration += rideDuration;
                        }
                        if (workout.getGainedAltitude() > 0.0f) {
                            stats.mElevation += workout.getGainedAltitudeForLocale();
                        }
                        if (Workout.hasData(workout.getAveragePower())) {
                            stats.mAvgPower.addAverage(workout.getAveragePower(), rideDuration);
                        }
                        if (Workout.hasData(workout.getAverageCadence())) {
                            stats.mAvgCadence.addAverage(workout.getAverageCadence(), rideDuration);
                        }
                        if (Workout.hasData(workout.getAverageHeartrate())) {
                            stats.mAvgHeartRate.addAverage(workout.getAverageHeartrate(), rideDuration);
                        }
                        if (Workout.hasData(workout.getCalories())) {
                            stats.mCalories += workout.getCalories();
                        }
                        if (workout.getAverageOxygen() > 0) {
                            stats.mAvgOxygen.addAverage(workout.getAverageOxygen(), rideDuration);
                        }
                        if (Workout.hasData(workout.getAverageStride())) {
                            stats.mAvgStride.addAverage(workout.getAverageStrideForLocale(), rideDuration);
                        }
                        stats.mRideCount++;
                        StatsTask.this.metricStats = StatsTask.this.processStats(stats);
                        StatsTask.this.publishProgress(new Void[0]);
                        return true;
                    }
                    return true;
                }
            });
            if (stats.mRideCount == 0) {
                this.metricStats = processStats(stats);
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List<MetricStat> processStats(Stats stats) {
            ArrayList<MetricStat> metricStats = new ArrayList<>();
            double distanceForLocale = Conversion.distanceForLocale(stats.mTotalDistance);
            metricStats.add(new MetricStat(R.string.stats_text_distance, StatsHelper.formatDouble(distanceForLocale, distanceForLocale > 1.0d ? 1 : 2), Utility.getUnitOfDistanceRes()));
            metricStats.add(new MetricStat(R.string.stats_text_time, Utility.formatTime(stats.mDuration), 0));
            long j = stats.mDuration / 1000;
            if (stats.mDuration == 0) {
                stats.mDuration = 1L;
            }
            double avgSpeedInMPS = stats.mAvgSpeed.getAverage() * 1000.0d;
            double avgSpeed = Conversion.speedForLocale(avgSpeedInMPS);
            if (SensorsConnector.isAllowedType(Sensor.DataType.SPEED)) {
                metricStats.add(new MetricStat(R.string.stats_text_avg_speed, StatsHelper.formatDouble(avgSpeed, 1), Utility.getUnitOfSpeedRes()));
            }
            if (SensorsConnector.isAllowedType(Sensor.DataType.PACE)) {
                metricStats.add(new MetricStat(R.string.stats_text_avg_pace, PaceUtil.formatPace(Conversion.speedToPaceForLocale(avgSpeedInMPS)), Utility.getUnitOfPaceRes()));
            }
            metricStats.add(new MetricStat(R.string.stats_text_total_elevation, StatsHelper.formatDouble(stats.mElevation, 0), Utility.getUnitOfElevationRes()));
            if (SensorsConnector.isAllowedType(Sensor.DataType.STRIDE)) {
                metricStats.add(new MetricStat(R.string.stats_text_average_stride, StatsHelper.formatDouble(stats.mAvgStride.getAverage(), 0), Utility.getUnitOfStrideRes()));
            }
            if (SensorsConnector.isAllowedType(Sensor.DataType.POWER)) {
                metricStats.add(new MetricStat(R.string.stats_text_average_power, StatsHelper.formatDouble(stats.mAvgPower.getAverage(), 0), R.string.power_unit));
            }
            if (SensorsConnector.isAllowedType(Sensor.DataType.KICK)) {
                metricStats.add(new MetricStat(R.string.stats_text_average_power, StatsHelper.formatDouble(stats.mAvgPower.getAverage(), 0), R.string.power_unit));
            }
            if (SensorsConnector.isAllowedType(Sensor.DataType.CADENCE)) {
                metricStats.add(new MetricStat(R.string.stats_text_average_cadence, StatsHelper.formatDouble(stats.mAvgCadence.getAverage(), 0), R.string.cadence_unit));
            }
            if (SensorsConnector.isAllowedType(Sensor.DataType.STEP)) {
                metricStats.add(new MetricStat(R.string.stats_text_average_cadence, StatsHelper.formatDouble(stats.mAvgCadence.getAverage(), 0), R.string.unit_cadence_run_short));
            }
            metricStats.add(new MetricStat(R.string.stats_text_average_heart_rate, StatsHelper.formatDouble(stats.mAvgHeartRate.getAverage(), 0), R.string.heart_unit));
            metricStats.add(new MetricStat(R.string.stats_text_total_calories, StatsHelper.formatDouble(stats.mCalories, 0), R.string.calories_unit));
            if (SensorsConnector.isAllowedType(Sensor.DataType.OXYGEN)) {
                metricStats.add(new MetricStat(R.string.stats_text_oxygen, StatsHelper.formatDouble(stats.mAvgOxygen.getAverage(), 0), R.string.oxygen_unit));
            }
            Log.d(TAG, "processing complete");
            return metricStats;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate((Object[]) values);
            this.mResultCallback.onStatsProgressUpdate(this.metricStats);
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            super.onCancelled();
            this.mResultCallback.onStatsFetchCancelled();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.mResultCallback.onStatsResult(this.metricStats);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatDouble(double value, int decimalPlaces) {
        if (value % 1.0d == 0.0d) {
            decimalPlaces = 0;
        }
        String format = "%." + decimalPlaces + "f";
        return String.format(Locale.US, format, Double.valueOf(value));
    }

    public static void fetchStatsForPeriod(Period period, StatsResultCallback callback) {
        cancelStatsFetch();
        sStatsProcessingTask = new StatsTask(callback);
        sStatsProcessingTask.execute(period);
    }

    public static void cancelStatsFetch() {
        if (sStatsProcessingTask != null) {
            sStatsProcessingTask.cancel(true);
        }
    }
}
