package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.field.FieldType;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.CalorieCounter;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.TimeHelper;

/* JADX INFO: loaded from: classes54.dex */
public class Run extends Workout {
    static final String[] RUN_FIELDS = createRunFields();
    static final String[] RUN_HEADER_FIELDS = {"_id", Field.RUN_MODE.name(), Field.GHOST_OR_ROUTE_ID.name(), Field.START_TIME.name(), Field.DURATION.name(), Field.DISTANCE.name(), Field.TITLE.name(), Field.RUN_TYPE.name()};
    public static final String TABLE_NAME = "Run";
    private static final String TAG = "Run";
    protected long mTargetAveragePaceMinutesPerKm;

    public enum Field {
        RUNNER_ID(FieldType.INTEGER),
        GEAR_ID(FieldType.INTEGER),
        ROUTE_ID(FieldType.INTEGER),
        TITLE(FieldType.TEXT),
        COMMENT(FieldType.TEXT),
        RUN_TYPE(FieldType.INTEGER),
        START_TIME(FieldType.INTEGER),
        END_TIME(FieldType.INTEGER),
        DURATION(FieldType.INTEGER),
        DISTANCE(FieldType.REAL),
        CALORIES(FieldType.INTEGER),
        FTP(FieldType.REAL),
        RSS(FieldType.REAL),
        RUN_MODE(FieldType.INTEGER),
        GHOST_OR_ROUTE_ID(FieldType.INTEGER),
        OVERALL_CLIMB(FieldType.INTEGER),
        ALTITUDE_RANGE(FieldType.INTEGER),
        PEAK_HR(FieldType.INTEGER),
        AVG_PACE(FieldType.REAL),
        MAX_PACE(FieldType.REAL),
        AVG_CADENCE(FieldType.REAL),
        MAX_CADENCE(FieldType.REAL),
        AVG_HEARTRATE(FieldType.INTEGER),
        MAX_HEARTRATE(FieldType.INTEGER),
        AVG_POWER(FieldType.REAL),
        MAX_POWER(FieldType.REAL),
        AVG_NORMALISED_POWER(FieldType.REAL),
        MAX_NORMALISED_POWER(FieldType.REAL),
        AVG_IF(FieldType.REAL),
        MAX_IF(FieldType.REAL),
        AVG_OXYGEN(FieldType.REAL),
        MIN_OXYGEN(FieldType.REAL),
        AVG_STRIDE(FieldType.REAL),
        MAX_STRIDE(FieldType.REAL),
        TARGET_TIME(FieldType.INTEGER),
        TARGET_DISTANCE(FieldType.REAL),
        TARGET_PACE(FieldType.REAL),
        TARGET_CADENCE(FieldType.REAL),
        TARGET_POWER(FieldType.REAL),
        TARGET_HEARTRATE(FieldType.INTEGER),
        TARGET_STRIDE(FieldType.REAL),
        FINAL(FieldType.INTEGER);

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

    private static String[] createRunFields() {
        String[] fields = new String[Field.values().length + 1];
        fields[0] = "_id";
        for (int i = 1; i < fields.length; i++) {
            fields[i] = Field.values()[i - 1].name();
        }
        return fields;
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

    public Run(long id, long route) {
        super(id, route);
        this.mSportType = SportType.RUN;
    }

    public Run(TimeHelper timeHelper, SplitHelper splitHelper) {
        super(timeHelper, splitHelper, Workout.RideMode.NORMAL, -1L);
        this.mSportType = SportType.RUN;
    }

    public Run(TimeHelper timeHelper, SplitHelper splitHelper, Workout.RideMode mode, long ghostId) {
        super(timeHelper, splitHelper, mode, ghostId);
        this.mId = SQLHelper.addRun(this.mRouteId, mode, ghostId);
        this.mSportType = SportType.RUN;
        this.mTargetAverageCadence = Prefs.getTargetAverageStep();
        this.mTargetAveragePower = Prefs.getTargetAverageKick();
        this.mTargetAverageHeartrate = Prefs.getTargetAverageHeartrate();
        this.mTargetAverageSpeedKm = (float) Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue(), Prefs.UnitSystem.METRIC);
        this.mTargetAveragePaceMinutesPerKm = (long) (Prefs.getTargetAveragePaceValue() * 1000.0d);
        FTP ftp = SQLHelper.getLatestFTP(SportType.RUN);
        this.mFunctionalThresholdPower = ftp == null ? 0.0d : ftp.mValue;
        FTP phr = SQLHelper.getLatestPeakHR();
        this.mEffectivePeakHR = phr == null ? 0 : (int) phr.mValue;
        if (this.mEffectivePeakHR == 0) {
            this.mEffectivePeakHR = HeartRateZones.setFromAge(UserProfile.getAge());
        } else {
            HeartRateZones.setMaxHR(this.mEffectivePeakHR);
        }
        if (this.mId != -1) {
            SQLHelper.setPeakHR(SportType.RUN, this.mId, this.mEffectivePeakHR);
        }
        init();
        this.normalisedPowerCalc = new NormalisedPower();
        if (this.mId != -1) {
            SQLHelper.setRunFTP(this.mId, this.mFunctionalThresholdPower);
            SQLHelper.setPeakHR(SportType.RUN, this.mId, this.mEffectivePeakHR);
        }
    }

    public Run(Cursor cursor) {
        this.mTargetAverageCadence = Prefs.getTargetAverageStep();
        this.mTargetAveragePower = Prefs.getTargetAverageKick();
        this.mTargetAverageHeartrate = Prefs.getTargetAverageHeartrate();
        this.mTargetAverageSpeedKm = (float) Conversion.speedForLocale(Prefs.getTargetAverageSpeedValue(), Prefs.UnitSystem.METRIC);
        this.mTargetAveragePaceMinutesPerKm = (long) (Prefs.getTargetAveragePaceValue() * 1000.0d);
        int pos = cursor.getColumnIndex("_id");
        this.mId = pos != -1 ? cursor.getLong(pos) : 0L;
        int pos2 = cursor.getColumnIndex(Field.ROUTE_ID.name());
        this.mRouteId = pos2 != -1 ? cursor.getLong(pos2) : -1L;
        int pos3 = cursor.getColumnIndex(Field.GHOST_OR_ROUTE_ID.name());
        this.mGhostId = pos3 != -1 ? cursor.getLong(pos3) : -1L;
        int pos4 = cursor.getColumnIndex(Field.FTP.name());
        this.mFunctionalThresholdPower = pos4 != -1 ? cursor.getDouble(pos4) : 0.0d;
        int pos5 = cursor.getColumnIndex(Field.PEAK_HR.name());
        this.mEffectivePeakHR = pos5 != -1 ? cursor.getInt(pos5) : 0;
        this.normalisedPowerCalc = new NormalisedPower();
    }

    @Override // com.kopin.solos.storage.Trackable, com.kopin.solos.storage.DataListener
    public void onStride(double stride) {
        if (shouldAddMetric()) {
            super.onStride(stride);
            if (this.mFinalLap != null) {
                this.mFinalLap.onStride(stride);
            }
        }
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.IRidePartData
    public int getCalories() {
        return CalorieCounter.calculate(getDistance(), this.mRiderWeight);
    }

    @Override // com.kopin.solos.storage.util.SplitHelper.SplitListener
    public boolean isLastLap() {
        return false;
    }

    @Override // com.kopin.solos.storage.Workout, com.kopin.solos.storage.util.SplitHelper.SplitListener
    public void onSplit(long startTime, double splitDistance, long splitTime, boolean isEnd) {
        super.onSplit(startTime, splitDistance, splitTime, isEnd);
        Lap lap = this.mFinalLap;
        this.mFinalLap = new Lap(this, this.mSplitHelper.getType(), this.mTimeHelper);
        int calories = Integer.MIN_VALUE;
        if (splitDistance > 0.0d) {
            calories = CalorieCounter.calculate(splitDistance, this.mRiderWeight);
        }
        Lap.Split split = lap.end(startTime, splitDistance, splitTime, calories);
        this.mSplits.add(split);
    }

    @Override // com.kopin.solos.storage.Workout
    public SavedRun end() {
        SavedRun saved = new SavedRun(this);
        SavedRides.saveWorkout(saved);
        return saved;
    }

    public double getFunctionalThresholdPower() {
        return this.mFunctionalThresholdPower;
    }

    public float getTargetAveragePaceMinutesPerKm() {
        return this.mTargetAveragePaceMinutesPerKm;
    }

    public static class Header extends Workout.Header {
        public Header(Cursor cursor) {
            super(cursor);
        }

        @Override // com.kopin.solos.storage.Workout.Header
        public SportType getSportType() {
            return SportType.RUN;
        }
    }
}
