package com.ua.sdk.workout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kopin.solos.analytics.Analytics;
import com.twitter.sdk.android.BuildConfig;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitystory.SocialSettings;
import com.ua.sdk.cache.EntityDatabase;
import com.ua.sdk.cache.database.definition.BooleanColumnDefinition;
import com.ua.sdk.cache.database.definition.ColumnDefinition;
import com.ua.sdk.cache.database.definition.DateColumnDefinition;
import com.ua.sdk.cache.database.definition.DoubleColumnDefinition;
import com.ua.sdk.cache.database.definition.IntegerColumnDefinition;
import com.ua.sdk.cache.database.definition.LocalIdColumnDefinition;
import com.ua.sdk.cache.database.definition.StringColumnDefinition;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.internal.AbstractEntityList;
import com.ua.sdk.internal.Link;
import com.ua.sdk.util.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutDatabase extends EntityDatabase<Workout> {
    private static final String ENTITY_NAME = "workout";
    protected static final String WORKOUT_DATABASE_NAME = "uasdk_workout";
    private static final int WORKOUT_DATABASE_VERSION = 2;
    private static final String WORKOUT_DIR_PATH = "workout";
    private static WorkoutDatabase instance = null;
    private static WorkoutTimeSeriesDataAdapter adapter = null;
    public static final ColumnDefinition<Long> LOCAL_ID = new LocalIdColumnDefinition(0, "_id");
    public static final ColumnDefinition<String> REMOTE_ID = new StringColumnDefinition(1, EntityDatabase.LIST.COLS.REMOTE_ID);
    public static final ColumnDefinition<String> NAME = new StringColumnDefinition(2, "name");
    public static final ColumnDefinition<Date> START_DATETIME = new DateColumnDefinition(3, "start_datetime");
    public static final ColumnDefinition<String> START_LOCALE_TIMEZONE = new StringColumnDefinition(4, "start_locale_timezone");
    public static final ColumnDefinition<Date> CREATED_DATETIME = new DateColumnDefinition(5, "created_datetime");
    public static final ColumnDefinition<Date> UPDATED_DATETIME = new DateColumnDefinition(6, "updated_datetime");
    public static final ColumnDefinition<String> REFERENCE_KEY = new StringColumnDefinition(7, "reference_key");
    public static final ColumnDefinition<String> SOURCE = new StringColumnDefinition(8, "source");
    public static final ColumnDefinition<String> NOTES = new StringColumnDefinition(9, "notes");
    public static final ColumnDefinition<Double> DISTANCE_TOTAL = new DoubleColumnDefinition(10, "distance_total");
    public static final ColumnDefinition<Double> METABOLIC_ENERGY_TOTAL = new DoubleColumnDefinition(11, "metabolic_energy_total");
    public static final ColumnDefinition<Double> ACTIVE_TIME_TOTAL = new DoubleColumnDefinition(12, "active_time_total");
    public static final ColumnDefinition<Double> ELAPSED_TIME_TOTAL = new DoubleColumnDefinition(13, "elapsed_time_total");
    public static final ColumnDefinition<Integer> STEPS_TOTAL = new IntegerColumnDefinition(14, "steps_total");
    public static final ColumnDefinition<Integer> HEART_RATE_MIN = new IntegerColumnDefinition(15, "heartrate_min");
    public static final ColumnDefinition<Integer> HEART_RATE_MAX = new IntegerColumnDefinition(16, "heartrate_max");
    public static final ColumnDefinition<Integer> HEART_RATE_AVG = new IntegerColumnDefinition(17, "heartrate_avg");
    public static final ColumnDefinition<Double> SPEED_MIN = new DoubleColumnDefinition(18, "speed_min");
    public static final ColumnDefinition<Double> SPEED_MAX = new DoubleColumnDefinition(19, "speed_max");
    public static final ColumnDefinition<Double> SPEED_AVG = new DoubleColumnDefinition(20, "speed_avg");
    public static final ColumnDefinition<Integer> CADENCE_MIN = new IntegerColumnDefinition(21, "cadence_min");
    public static final ColumnDefinition<Integer> CADENCE_MAX = new IntegerColumnDefinition(22, "cadence_max");
    public static final ColumnDefinition<Integer> CADENCE_AVG = new IntegerColumnDefinition(23, "cadence_avg");
    public static final ColumnDefinition<Double> POWER_MIN = new DoubleColumnDefinition(24, "power_min");
    public static final ColumnDefinition<Double> POWER_MAX = new DoubleColumnDefinition(25, "power_max");
    public static final ColumnDefinition<Double> POWER_AVG = new DoubleColumnDefinition(26, "power_avg");
    public static final ColumnDefinition<Double> TORQUE_MIN = new DoubleColumnDefinition(27, "torque_min");
    public static final ColumnDefinition<Double> TORQUE_MAX = new DoubleColumnDefinition(28, "torque_max");
    public static final ColumnDefinition<Double> TORQUE_AVG = new DoubleColumnDefinition(29, "torque_avg");
    public static final ColumnDefinition<Boolean> HAS_TIME_SERIES = new BooleanColumnDefinition(30, "has_time_series");
    public static final ColumnDefinition<Double> WILLPOWER = new DoubleColumnDefinition(31, BaseDataTypes.ID_WILLPOWER);
    public static final ColumnDefinition<Boolean> IS_VERIFIED = new BooleanColumnDefinition(32, "is_verified");
    public static final ColumnDefinition<Boolean> FACEBOOK = new BooleanColumnDefinition(33, "facebook");
    public static final ColumnDefinition<Boolean> TWITTER = new BooleanColumnDefinition(34, BuildConfig.ARTIFACT_ID);
    private static final ColumnDefinition[] ALL_COLUMNS = {LOCAL_ID, REMOTE_ID, NAME, START_DATETIME, START_LOCALE_TIMEZONE, CREATED_DATETIME, UPDATED_DATETIME, REFERENCE_KEY, SOURCE, NOTES, DISTANCE_TOTAL, METABOLIC_ENERGY_TOTAL, ACTIVE_TIME_TOTAL, ELAPSED_TIME_TOTAL, STEPS_TOTAL, HEART_RATE_MIN, HEART_RATE_MAX, HEART_RATE_AVG, SPEED_MIN, SPEED_MAX, SPEED_AVG, CADENCE_MIN, CADENCE_MAX, CADENCE_AVG, POWER_MIN, POWER_MAX, POWER_AVG, TORQUE_MIN, TORQUE_MAX, TORQUE_AVG, HAS_TIME_SERIES, WILLPOWER, IS_VERIFIED, FACEBOOK, TWITTER};

    protected WorkoutDatabase(Context context) {
        super(context, Analytics.Events.WORKOUT, WORKOUT_DATABASE_NAME, buildColumnNames(ALL_COLUMNS), REMOTE_ID.getColumnName(), 2);
    }

    public static WorkoutDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new WorkoutDatabase(context.getApplicationContext());
        }
        return instance;
    }

    @Override // com.ua.sdk.cache.EntityDatabase
    protected void createEntityTable(SQLiteDatabase db) {
        executeSqlScript(db, "cache/workout/1_workout_create_table.sql", String.format("Fatal error, unable to initialize entity tables for %s database.", Analytics.Events.WORKOUT));
    }

    @Override // com.ua.sdk.cache.EntityDatabase
    public void onEntityUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 0:
                db.execSQL("DROP TABLE IF EXISTS " + this.mEntityTable);
                createEntityTable(db);
                break;
            case 1:
                break;
            default:
                return;
        }
        deleteEntitiesWithNullRemoteId(db);
        deleteAllLists(db);
    }

    @Override // com.ua.sdk.cache.EntityDatabase
    protected AbstractEntityList<Workout> createEntityList(long localId, String remoteId, int totalCount) {
        WorkoutListImpl list = new WorkoutListImpl();
        list.setTotalCount(totalCount);
        list.setLocalId(localId);
        list.setLink("self", new Link(remoteId));
        return list;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.EntityDatabase
    public WorkoutImpl getEntityFromCursor(Cursor c) {
        WorkoutImpl workout = new WorkoutImpl();
        workout.setLocalId(LOCAL_ID.read(c).longValue());
        workout.startTime = START_DATETIME.read(c);
        workout.updateTime = UPDATED_DATETIME.read(c);
        workout.createdTime = CREATED_DATETIME.read(c);
        workout.name = NAME.read(c);
        workout.notes = NOTES.read(c);
        workout.timeZone = TimeZone.getTimeZone(START_LOCALE_TIMEZONE.read(c));
        workout.source = SOURCE.read(c);
        workout.referenceKey = REFERENCE_KEY.read(c);
        workout.hasTimeSeries = HAS_TIME_SERIES.read(c);
        WorkoutAggregatesImpl workoutAggregates = new WorkoutAggregatesImpl();
        workoutAggregates.heartRateMin = HEART_RATE_MIN.read(c);
        workoutAggregates.heartRateMax = HEART_RATE_MAX.read(c);
        workoutAggregates.heartRateAvg = HEART_RATE_AVG.read(c);
        workoutAggregates.speedMin = SPEED_MIN.read(c);
        workoutAggregates.speedMax = SPEED_MAX.read(c);
        workoutAggregates.speedAvg = SPEED_AVG.read(c);
        workoutAggregates.cadenceMin = CADENCE_MIN.read(c);
        workoutAggregates.cadenceMax = CADENCE_MAX.read(c);
        workoutAggregates.cadenceAvg = CADENCE_AVG.read(c);
        workoutAggregates.powerMin = POWER_MIN.read(c);
        workoutAggregates.powerMax = POWER_MAX.read(c);
        workoutAggregates.powerAvg = POWER_AVG.read(c);
        workoutAggregates.torqueMin = TORQUE_MIN.read(c);
        workoutAggregates.torqueMax = TORQUE_MAX.read(c);
        workoutAggregates.torqueAvg = TORQUE_AVG.read(c);
        workoutAggregates.willPower = WILLPOWER.read(c);
        workoutAggregates.distanceTotal = DISTANCE_TOTAL.read(c);
        workoutAggregates.metabolicEnergyTotal = METABOLIC_ENERGY_TOTAL.read(c);
        workoutAggregates.activeTimeTotal = ACTIVE_TIME_TOTAL.read(c);
        workoutAggregates.elapsedTimeTotal = ELAPSED_TIME_TOTAL.read(c);
        workoutAggregates.stepsTotal = STEPS_TOTAL.read(c);
        workout.workoutAggregates = workoutAggregates;
        if (workout.hasTimeSeries != null && workout.hasTimeSeries.booleanValue()) {
            workout.timeSeries = fetchTimeSeriesData(workout.getLocalId());
        }
        Boolean fb = FACEBOOK.read(c);
        Boolean twitter = TWITTER.read(c);
        if (fb != null || twitter != null) {
            SocialSettings socialSettings = new SocialSettings();
            socialSettings.setFacebook(fb);
            socialSettings.setTwitter(twitter);
            workout.socialSettings = socialSettings;
        }
        return workout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.EntityDatabase
    public ContentValues getContentValuesFromEntity(Workout workout) {
        ContentValues cv = new ContentValues();
        START_DATETIME.write(workout.getStartTime(), cv);
        UPDATED_DATETIME.write(workout.getUpdatedTime(), cv);
        CREATED_DATETIME.write(workout.getCreatedTime(), cv);
        NAME.write(workout.getName(), cv);
        NOTES.write(workout.getNotes(), cv);
        START_LOCALE_TIMEZONE.write(workout.getTimeZone().getID(), cv);
        SOURCE.write(workout.getSource(), cv);
        REFERENCE_KEY.write(workout.getReferenceKey(), cv);
        HAS_TIME_SERIES.write(workout.hasTimeSeries(), cv);
        WorkoutAggregates agg = workout.getAggregates();
        HEART_RATE_MAX.write(agg.getHeartRateMax(), cv);
        HEART_RATE_MIN.write(agg.getHeartRateMin(), cv);
        HEART_RATE_AVG.write(agg.getHeartRateAvg(), cv);
        SPEED_MAX.write(agg.getSpeedMax(), cv);
        SPEED_MIN.write(agg.getSpeedMin(), cv);
        SPEED_AVG.write(agg.getSpeedAvg(), cv);
        CADENCE_MAX.write(agg.getCadenceMax(), cv);
        CADENCE_MIN.write(agg.getCadenceMin(), cv);
        CADENCE_AVG.write(agg.getCadenceAvg(), cv);
        POWER_MAX.write(agg.getPowerMax(), cv);
        POWER_MIN.write(agg.getPowerMin(), cv);
        POWER_AVG.write(agg.getPowerAvg(), cv);
        TORQUE_MAX.write(agg.getTorqueMax(), cv);
        TORQUE_MIN.write(agg.getTorqueMin(), cv);
        TORQUE_AVG.write(agg.getTorqueAvg(), cv);
        WILLPOWER.write(agg.getWillPower(), cv);
        DISTANCE_TOTAL.write(agg.getDistanceTotal(), cv);
        METABOLIC_ENERGY_TOTAL.write(agg.getMetabolicEnergyTotal(), cv);
        ACTIVE_TIME_TOTAL.write(agg.getActiveTimeTotal(), cv);
        ELAPSED_TIME_TOTAL.write(agg.getElapsedTimeTotal(), cv);
        STEPS_TOTAL.write(agg.getStepsTotal(), cv);
        SocialSettings socialSettings = workout.getSocialSettings();
        FACEBOOK.write(socialSettings != null ? socialSettings.getFacebook() : null, cv);
        TWITTER.write(socialSettings != null ? socialSettings.getTwitter() : null, cv);
        return cv;
    }

    private List<Workout> fetchUnsyncedWorkouts(Integer state) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sb = new StringBuilder("SELECT ");
        for (int i = 0; i < ALL_COLUMNS.length; i++) {
            sb.append("w.").append(ALL_COLUMNS[i].getColumnName());
            if (i < ALL_COLUMNS.length - 1) {
                sb.append(",");
            }
            sb.append(" ");
        }
        sb.append("FROM workout_entity w JOIN workout_meta wm ON w._id=wm.entity_id WHERE wm.pending_operation = ? AND wm.entity_list_id IS NULL;");
        Cursor c = db.rawQuery(sb.toString(), new String[]{String.valueOf(state)});
        List<Workout> result = new ArrayList<>();
        while (c.moveToNext()) {
            WorkoutImpl w = getEntityFromCursor(c);
            long localId = c.getLong(0);
            w.setLinkMap(getLinkMap(db, "entity_id", localId));
            result.add(w);
        }
        return result;
    }

    public List<Workout> fetchUnsyncedCreatedWorkouts() {
        return fetchUnsyncedWorkouts(STATE_CREATED);
    }

    private void overwriteWorkoutTimeSeriesJson(long localId, Workout workout) throws Throwable {
        OutputStreamWriter out;
        if (workout.getTimeSeriesData() != null) {
            if (adapter == null) {
                adapter = new WorkoutTimeSeriesDataAdapter();
            }
            OutputStreamWriter out2 = null;
            try {
                try {
                    out = new OutputStreamWriter(FileUtil.openFileOutput(this.mContext, Analytics.Events.WORKOUT, localId + ".json"));
                } catch (Throwable th) {
                    th = th;
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e2) {
            }
            try {
                adapter.write(new JsonWriter(out), (WorkoutTimeSeriesImpl) workout.getTimeSeriesData());
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                        UaLog.error("Caught exception closing out", (Throwable) e3);
                    }
                }
            } catch (FileNotFoundException e4) {
                String msg = String.format("Fatal error! Unable to open file to write JSON for workout with localId=%s.", Long.valueOf(localId));
                throw new RuntimeException(msg);
            } catch (IOException e5) {
                String msg2 = String.format("Fatal error! Unable to write JSON for workout with localId=%s.", Long.valueOf(localId));
                throw new RuntimeException(msg2);
            } catch (Throwable th2) {
                th = th2;
                out2 = out;
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e6) {
                        UaLog.error("Caught exception closing out", (Throwable) e6);
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.EntityDatabase
    public void postSaveEntity(long localId, Workout workout) throws Throwable {
        overwriteWorkoutTimeSeriesJson(localId, workout);
    }

    @Override // com.ua.sdk.cache.EntityDatabase
    protected void preDeleteEntity(long localId) {
        deleteTimeSeriesData(localId);
    }

    @Override // com.ua.sdk.cache.EntityDatabase
    protected void preDeleteAll(SQLiteDatabase db) {
        Cursor c = db.query(this.mEntityTable, new String[]{"_id"}, null, null, null, null, null);
        ArrayList<Long> localIds = new ArrayList<>();
        while (c.moveToNext()) {
            localIds.add(Long.valueOf(c.getLong(0)));
        }
        c.close();
        Iterator<Long> it = localIds.iterator();
        while (it.hasNext()) {
            long localId = it.next().longValue();
            deleteTimeSeriesData(localId);
        }
    }

    public TimeSeriesData fetchTimeSeriesData(long localId) throws Throwable {
        WorkoutTimeSeriesImpl workoutTimeSeriesImpl;
        InputStreamReader in;
        if (adapter == null) {
            adapter = new WorkoutTimeSeriesDataAdapter();
        }
        InputStreamReader in2 = null;
        try {
            try {
                in = new InputStreamReader(FileUtil.openFileInput(this.mContext, Analytics.Events.WORKOUT, localId + ".json"));
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
        }
        try {
            workoutTimeSeriesImpl = adapter.read2(new JsonReader(in));
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    UaLog.error("Caught exception closing in", (Throwable) e3);
                }
            }
        } catch (FileNotFoundException e4) {
            in2 = in;
            UaLog.debug("Didn't find time series for workout with localId=" + localId);
            workoutTimeSeriesImpl = null;
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e5) {
                    UaLog.error("Caught exception closing in", (Throwable) e5);
                }
            }
        } catch (IOException e6) {
            String msg = String.format("Fatal error! Unable to read JSON for workout with localId=" + localId, new Object[0]);
            throw new RuntimeException(msg);
        } catch (Throwable th2) {
            th = th2;
            in2 = in;
            if (in2 != null) {
                try {
                    in2.close();
                } catch (IOException e7) {
                    UaLog.error("Caught exception closing in", (Throwable) e7);
                }
            }
            throw th;
        }
        return workoutTimeSeriesImpl;
    }

    private boolean deleteTimeSeriesData(long localId) {
        try {
            File f = FileUtil.getFile(this.mContext, Analytics.Events.WORKOUT, localId + ".json");
            return f.delete();
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
