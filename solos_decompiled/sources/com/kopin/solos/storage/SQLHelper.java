package com.kopin.solos.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.CorrectedElevation;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Training;
import com.kopin.solos.storage.TrainingSegment;
import com.kopin.solos.storage.TrainingStep;
import com.kopin.solos.storage.TrainingTarget;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.field.FieldHelper;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.ICancellable;
import com.kopin.solos.storage.util.Utility;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes54.dex */
public class SQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RidesDB";
    private static final int DATABASE_VERSION = 18;
    private static final String ID_FIELD = "_id";
    private static final String INTEGER = "INTEGER";
    private static final long MONTH_MILLIS = 2678400000L;
    private static final String PKEY = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ";
    private static final String REAL = "REAL";
    private static final String TAG = "SQLHelper";
    private static final String TEXT = "TEXT";
    private static String currentUsername;
    private static SQLHelper self;
    private final SQLiteDatabase mDb;
    private Handler mHandler;
    private final Resources mResources;
    private long mThreadId;
    private static long runRideId = 0;
    private static boolean ready = false;
    private static String[] RIDE_HEADER_FIELDS = {"_id", Ride.RIDE_MODE, Ride.GHOST_RIDE_ID, Ride.START_TIME_ACTUAL, Ride.DURATION, Ride.DISTANCE, Ride.TITLE, Ride.ACTIVITY};
    private static String[] RIDE_FIELDS = {"_id", Ride.GHOST_RIDE_ID, "ftp", Ride.START_TIME_ACTUAL, Ride.START_TIME, Ride.END_TIME, Ride.DURATION, Ride.AVERAGE_SPEED, Ride.DISTANCE, Ride.AVERAGE_CADENCE, Ride.AVERAGE_HEARTRATE, Ride.AVERAGE_POWER, Ride.MAX_ALTITUDE_DIFF, Ride.MAX_CADENCE, Ride.MAX_HEARTRATE, Ride.MAX_POWER, Ride.MAX_SPEED, Ride.CALORIES, Ride.GAINED_ALTITUDE, Ride.TITLE, Ride.COMMENT, Ride.ACTIVITY, Ride.BIKE_ID, Ride.TARGET_AVERAGE_CADENCE, Ride.TARGET_AVERAGE_HEARTRATE, Ride.TARGET_AVERAGE_POWER, Ride.TARGET_AVERAGE_SPEED, Ride.FINAL, Ride.RIDE_MODE, "routeId", Ride.RIDER_ID, Ride.AVERAGE_OXYGEN, Ride.MIN_OXYGEN, Ride.AVERAGE_NORM_POWER, Ride.MAX_NORM_POWER, Ride.AVERAGE_INTENSITY, Ride.MAX_INTENSITY, Ride.TSS, Ride.PEAK_HR};
    private static String[] ROUTE_FIELDS = {"_id", Route.TIME_TO_BEAT, Route.DISTANCE, Route.TITLE};
    private static String[] COORD_FIELDS = {"_id", Coordinate.LATITUDE, Coordinate.LONGITUDE, Coordinate.ALTITUDE};
    private static String[] EXTENDED_RECORD_FIELDS = {Record.Field.TIMESTAMP.name(), Record.Field.RESOLUTION.name(), Record.Field.SPEED.name(), Record.Field.DISTANCE.name(), Record.Field.CADENCE.name(), Record.Field.HEARTRATE.name(), Record.Field.POWER.name(), Record.Field.STRIDE.name(), Record.Field.COORD_ID.fqname() + " AS " + Record.Field.COORD_ID.name(), Record.Field.CORRECTED_ALTITUDE.name(), Coordinate.ALTITUDE, Coordinate.LATITUDE, Coordinate.LONGITUDE};

    public interface AddListener {
        void onAdded(long j);
    }

    public interface DatabaseWorkCallback {
        void onComplete();
    }

    public interface foreachCoordCallback {
        boolean onCoordinate(Coordinate coordinate);
    }

    public interface foreachWorkoutCallback {
        boolean onWorkout(SavedWorkout savedWorkout);
    }

    public interface foreachWorkoutCursorCallback {
        boolean onWorkoutCursor(Cursor cursor);
    }

    private SQLHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 18);
        ready = false;
        HandlerThread ht = new HandlerThread("database save thread");
        ht.start();
        this.mHandler = new Handler(ht.getLooper());
        this.mThreadId = ht.getLooper().getThread().getId();
        this.mDb = getWritableDatabase();
        ready = true;
        this.mResources = context.getResources();
    }

    protected void finalize() throws Throwable {
        this.mDb.close();
        super.finalize();
    }

    public static void init(Context context) {
        self = new SQLHelper(context);
        setRunRideId();
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    static String getString(int resId) {
        return self.mResources.getString(resId);
    }

    static boolean isReady() {
        return ready && self != null && self.mDb != null && self.mDb.isOpen();
    }

    public static boolean isActive(ICancellable cancellable) {
        return ready && self != null && (cancellable == null || cancellable.isActive());
    }

    public static long getRunRideId() {
        Log.d(TAG, "old runride id " + runRideId);
        long j = runRideId + 1;
        runRideId = j;
        return j;
    }

    public static void setRunRideId() {
        Cursor rideCursor = getMaxCursor("Ride");
        long rideId = rideCursor.moveToFirst() ? rideCursor.getLong(0) : 0L;
        Cursor runCursor = getMaxCursor(Run.TABLE_NAME);
        long runId = runCursor.moveToFirst() ? runCursor.getLong(0) : 0L;
        if (rideId > 0 || runId > 0) {
            runRideId = Math.max(rideId, runId);
        } else {
            runRideId = 0L;
        }
    }

    private String addColumn(String name, String fieldType) {
        return ", " + name + " " + fieldType;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase db) {
        ready = false;
        createRideTable(db, "Ride", false);
        createRunTable(db);
        createLapTable(db);
        createRecordTable(db);
        createRouteTable(db);
        createCoordinatesTable(db);
        createShareTable(db);
        createBikeTable(db);
        createGearTable(db);
        createRiderTable(db);
        createFtpTable(db);
        createElevationTable(db);
        createTrainingTable(db);
        createTrainingSegmentTable(db);
        createTrainingStepTable(db);
        createTrainingTargetTable(db);
        runRideId = 0L;
        ready = true;
    }

    private void createRunTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE Run (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Run.getCreateFieldsQuery() + " );";
        db.execSQL(CREATE_TABLE);
    }

    private void createFtpTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE FTP (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + FTP.getCreateFieldsQuery() + ");";
        db.execSQL(CREATE_TABLE);
    }

    private void createRiderTable(SQLiteDatabase db) {
        String CREATE_RIDER_TABLE = "CREATE TABLE Rider (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Rider.getCreateFieldsQuery() + " );";
        db.execSQL(CREATE_RIDER_TABLE);
    }

    private void createBikeTable(SQLiteDatabase db) {
        String CREATE_BIKE_TABLE = "CREATE TABLE Bike (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Bike.getCreateFieldsQuery() + ");";
        db.execSQL(CREATE_BIKE_TABLE);
    }

    private void createGearTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE Gear (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Gear.getCreateFieldsQuery() + ");";
        db.execSQL(CREATE_TABLE);
    }

    private void createElevationTable(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS CorrectedElevation (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + CorrectedElevation.getCreateFieldsQuery() + ");";
        db.execSQL(CREATE_TABLE);
    }

    private void createCoordinatesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Coord (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , routeId INTEGER, coordLat REAL, coordLong REAL, coordAlt REAL, recIsBreak INTEGER,  FOREIGN KEY (routeId) REFERENCES Route (_id));");
    }

    private void createRouteTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Route (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , routeDistance REAL, routeTime INTEGER, routeName TEXT);");
    }

    private void createRecordTable(SQLiteDatabase db) {
        String CREATE_RECORD_TABLE = "CREATE TABLE Record (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Record.getCreateFieldsQuery() + ", FOREIGN KEY (" + Record.Field.RIDE_ID.name() + ") REFERENCES Ride (_id));";
        db.execSQL(CREATE_RECORD_TABLE);
    }

    private void createLapTable(SQLiteDatabase db) {
        String CREATE_LAP_TABLE = "CREATE TABLE Lap (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Lap.getCreateFieldsQuery() + " , FOREIGN KEY (" + Lap.Field.RIDE_ID.name() + ") REFERENCES Ride (_id));";
        db.execSQL(CREATE_LAP_TABLE);
    }

    private void createTrainingTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS Training (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + Training.getCreateFieldsQuery() + ");";
        db.execSQL(query);
    }

    private void createTrainingSegmentTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS TrainingSegment (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + TrainingSegment.getCreateFieldsQuery() + ", FOREIGN KEY (" + TrainingSegment.Field.TRAINING_ID.name() + ") REFERENCES " + Training.NAME + " (_id));";
        db.execSQL(query);
    }

    private void createTrainingStepTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS TrainingStep (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + TrainingStep.getCreateFieldsQuery() + ", FOREIGN KEY (" + TrainingStep.Field.SEGMENT_ID.name() + ") REFERENCES TrainingSegment (_id));";
        db.execSQL(query);
    }

    private void createTrainingTargetTable(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS TrainingTarget (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , " + TrainingTarget.getCreateFieldsQuery() + ", FOREIGN KEY (" + TrainingTarget.Field.STEP_ID.name() + ") REFERENCES TrainingStep (_id));";
        db.execSQL(query);
    }

    private void createRideTable(SQLiteDatabase db, String name, boolean temp) {
        String CREATE_RIDE_TABLE = temp ? "CREATE TEMPORARY TABLE " : "CREATE TABLE ";
        StringBuffer buffer = new StringBuffer(CREATE_RIDE_TABLE + name + " (_id" + PKEY + ", " + Ride.GHOST_RIDE_ID + " " + INTEGER + " DEFAULT -1, routeId " + INTEGER + " DEFAULT -1, ftp " + REAL + " DEFAULT 0, " + Ride.PEAK_HR + " " + INTEGER + " DEFAULT 0, " + Ride.START_TIME_ACTUAL + " " + INTEGER + ", " + Ride.START_TIME + " " + INTEGER + ", " + Ride.FINAL + " " + INTEGER + ", " + Ride.END_TIME + " " + INTEGER + ", " + Ride.DURATION + " " + INTEGER + ", " + Ride.AVERAGE_SPEED + " " + REAL + ", " + Ride.DISTANCE + " " + REAL + ", " + Ride.AVERAGE_CADENCE + " " + REAL + ", " + Ride.AVERAGE_HEARTRATE + " " + INTEGER + ", " + Ride.AVERAGE_POWER + " " + REAL + ", " + Ride.MAX_ALTITUDE_DIFF + " " + REAL + ", " + Ride.MAX_CADENCE + " " + REAL + ", " + Ride.MAX_HEARTRATE + " " + INTEGER + ", " + Ride.MAX_POWER + " " + REAL + ", " + Ride.MAX_SPEED + " " + REAL + ", " + Ride.RIDE_MODE + " " + INTEGER);
        buffer.append(addColumn(Ride.CALORIES, INTEGER));
        buffer.append(addColumn(Ride.GAINED_ALTITUDE, REAL));
        buffer.append(addColumn(Ride.TITLE, TEXT));
        buffer.append(addColumn(Ride.COMMENT, TEXT));
        buffer.append(addColumn(Ride.ACTIVITY, INTEGER));
        buffer.append(addColumn(Ride.BIKE_ID, INTEGER));
        buffer.append(addColumn(Ride.TARGET_AVERAGE_CADENCE, INTEGER));
        buffer.append(addColumn(Ride.TARGET_AVERAGE_HEARTRATE, INTEGER));
        buffer.append(addColumn(Ride.TARGET_AVERAGE_POWER, INTEGER));
        buffer.append(addColumn(Ride.TARGET_AVERAGE_SPEED, REAL));
        buffer.append(addColumn(Ride.RIDER_ID, INTEGER));
        buffer.append(addColumn(Ride.AVERAGE_OXYGEN, INTEGER));
        buffer.append(addColumn(Ride.MIN_OXYGEN, INTEGER));
        buffer.append(addColumn(Ride.AVERAGE_NORM_POWER, REAL));
        buffer.append(addColumn(Ride.MAX_NORM_POWER, REAL));
        buffer.append(addColumn(Ride.AVERAGE_INTENSITY, REAL));
        buffer.append(addColumn(Ride.MAX_INTENSITY, REAL));
        buffer.append(addColumn(Ride.TSS, REAL));
        buffer.append(");");
        db.execSQL(buffer.toString());
    }

    private void createShareTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Shared (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , itemId INTEGER, sProId INTEGER, sUser TEXT, sWasImported INTEGER, sExternalId TEXT, Type INTEGER DEFAULT 0,UpdatedTime INTEGER DEFAULT 0);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ready = false;
        switch (oldVersion) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                db.execSQL("DROP TABLE IF EXISTS Ride");
                db.execSQL("DROP TABLE IF EXISTS Run");
                db.execSQL("DROP TABLE IF EXISTS Lap");
                db.execSQL("DROP TABLE IF EXISTS Record");
                db.execSQL("DROP TABLE IF EXISTS Route");
                db.execSQL("DROP TABLE IF EXISTS Shared");
                db.execSQL("DROP TABLE IF EXISTS Bike");
                db.execSQL("DROP TABLE IF EXISTS Rider");
                db.execSQL("DROP TABLE IF EXISTS FTP");
                db.execSQL("DROP TABLE IF EXISTS Gear");
                onCreate(db);
                return;
            case 14:
                addColumn(db, "Record", Record.Field.RESOLUTION.name(), Record.Field.RESOLUTION.fieldType.toString());
                createElevationTable(db);
                ready = true;
                return;
            case 15:
                createTrainingTable(db);
                createTrainingSegmentTable(db);
                createTrainingStepTable(db);
                createTrainingTargetTable(db);
                ready = true;
                return;
            case 16:
                addColumn(db, Training.NAME, Training.Field.TRAINING_TYPE.name(), Training.Field.TRAINING_TYPE.fieldType.toString());
                ready = true;
                return;
            case 17:
                addColumn(db, FTP.NAME, FTP.Field.THRESHOLD.name(), FTP.Field.THRESHOLD.fieldType.toString(), FTP.ThresholdType.FTP.name());
                addColumn(db, Run.TABLE_NAME, Run.Field.FTP.name(), Run.Field.FTP.fieldType.toString(), "0");
                addColumn(db, Run.TABLE_NAME, Run.Field.RSS.name(), Run.Field.RSS.fieldType.toString(), "0");
                addColumn(db, "Ride", Ride.PEAK_HR, INTEGER);
                addColumn(db, Run.TABLE_NAME, Run.Field.PEAK_HR.name(), Run.Field.PEAK_HR.fieldType.toString());
                break;
        }
        ready = true;
    }

    private void addColumn(SQLiteDatabase db, String table, String name, String type) {
        addColumn(db, table, name, type, null);
    }

    private void addColumn(SQLiteDatabase db, String table, String name, String type, String defaultValue) {
        StringBuffer buffer = new StringBuffer("ALTER TABLE ").append(table).append(" ADD COLUMN ");
        buffer.append(name).append(" ").append(type);
        db.execSQL(buffer.toString());
        if (defaultValue != null && !defaultValue.isEmpty()) {
            StringBuffer buffer2 = new StringBuffer("UPDATE ").append(table);
            buffer2.append(" SET ").append(name);
            buffer2.append(" = '").append(defaultValue).append("'");
            db.execSQL(buffer2.toString());
        }
    }

    private void removeColumnRideTargetIsMetric(SQLiteDatabase db) {
        StringBuffer rideColumns = new StringBuffer();
        for (String column : RIDE_FIELDS) {
            if (rideColumns.length() > 0) {
                rideColumns.append(", ");
            }
            rideColumns.append(column);
        }
        db.beginTransaction();
        try {
            createRideTable(db, "ride_temp", true);
            db.execSQL("INSERT INTO ride_temp SELECT " + ((Object) rideColumns) + " FROM Ride");
            db.execSQL("DROP TABLE Ride");
            createRideTable(db, "Ride", false);
            db.execSQL("INSERT INTO Ride SELECT " + ((Object) rideColumns) + " FROM ride_temp");
            db.execSQL("DROP TABLE ride_temp");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static long addRide(long routeId, Workout.RideMode mode, long ghostId) {
        return self.doAddRide(ghostId, routeId, mode, System.currentTimeMillis(), false);
    }

    public static long addRun(long routeId, Workout.RideMode mode, long ghostId) {
        return self.doAddRun(ghostId, routeId, mode, System.currentTimeMillis(), false);
    }

    private long doAddRide(long ghostId, long routeId, Workout.RideMode mode, long actualStartTime, boolean finished) {
        long id;
        if (!isReady()) {
            return 0L;
        }
        ContentValues values = new ContentValues();
        values.put(Ride.START_TIME_ACTUAL, Long.valueOf(actualStartTime));
        values.put(Ride.START_TIME, Long.valueOf(Utility.getTimeMilliseconds()));
        values.put(Ride.FINAL, Integer.valueOf(finished ? 1 : 0));
        values.put("routeId", Long.valueOf(routeId));
        values.put(Ride.RIDE_MODE, Integer.valueOf(mode.ordinal()));
        values.put(Ride.GHOST_RIDE_ID, Long.valueOf(ghostId));
        values.put("_id", Long.valueOf(getRunRideId()));
        synchronized (this.mDb) {
            id = this.mDb.insert("Ride", null, values);
            Log.d(TAG, "doAddRide " + id + ", start = " + actualStartTime);
        }
        return id;
    }

    private long doAddRun(long ghostId, long routeId, Workout.RideMode mode, long actualStartTime, boolean finished) {
        long id;
        ContentValues values = new ContentValues();
        values.put(Run.Field.START_TIME.name(), Long.valueOf(actualStartTime));
        values.put(Run.Field.RUN_MODE.name(), Integer.valueOf(mode.ordinal()));
        values.put(Run.Field.FINAL.name(), Integer.valueOf(finished ? 1 : 0));
        values.put(Run.Field.ROUTE_ID.name(), Long.valueOf(routeId));
        values.put(Run.Field.GHOST_OR_ROUTE_ID.name(), Long.valueOf(ghostId));
        values.put("_id", Long.valueOf(getRunRideId()));
        synchronized (this.mDb) {
            id = this.mDb.insert(Run.TABLE_NAME, null, values);
            Log.d(TAG, "doAddRun " + id + ", start = " + actualStartTime);
        }
        return id;
    }

    public static long addDownloadedRide(String externalId, ContentValues rideInfo, int platformId, String username) {
        return self.doAddRide(externalId, rideInfo, platformId, username);
    }

    public static long addDownloadedRun(String externalId, ContentValues runInfo, int platformId, String username) {
        return self.doAddRun(externalId, runInfo, platformId, username);
    }

    private long doAddRide(String externalId, ContentValues rideInfo, int platformKey, String username) {
        long id;
        if (!isReady()) {
            return 0L;
        }
        synchronized (this.mDb) {
            rideInfo.put("_id", Long.valueOf(getRunRideId()));
            id = this.mDb.insert("Ride", null, rideInfo);
            Log.d(TAG, " _doAddRide id " + id + ", " + rideInfo.toString());
            Shared sharedHeader = new Shared(id, platformKey, username, "", true, Shared.ShareType.RIDE, System.currentTimeMillis());
            Shared sharedData = new Shared(id, platformKey, username, "", true, Shared.ShareType.RIDE_DATA, System.currentTimeMillis());
            Log.d(TAG, "doAddRide SHARE " + sharedHeader + ", " + sharedData);
            doAddShare(sharedHeader, null, null);
            doAddShare(sharedData, null, null);
        }
        return id;
    }

    private long doAddRun(String externalId, ContentValues runInfo, int platformKey, String username) {
        long id;
        if (!isReady()) {
            return 0L;
        }
        synchronized (this.mDb) {
            runInfo.put("_id", Long.valueOf(getRunRideId()));
            id = this.mDb.insert(Run.TABLE_NAME, null, runInfo);
            Log.d(TAG, " _doAddRun id " + id + ", " + runInfo.toString());
            Shared sharedHeader = new Shared(id, platformKey, username, "", true, Shared.ShareType.RUN, System.currentTimeMillis());
            Shared sharedData = new Shared(id, platformKey, username, "", true, Shared.ShareType.RUN_DATA, System.currentTimeMillis());
            Log.d(TAG, "doAddRide SHARE " + sharedHeader + ", " + sharedData);
            doAddShare(sharedHeader, null, null);
            doAddShare(sharedData, null, null);
        }
        return id;
    }

    public static void setRideFTP(long rideId, double ftp) {
        self.doSetRideFTP(SportType.RIDE, rideId, ftp);
    }

    public static void setRunFTP(long runId, double ftp) {
        self.doSetRideFTP(SportType.RUN, runId, ftp);
    }

    private void doSetRideFTP(SportType sport, long rideId, double ftp) {
        if (isReady()) {
            ContentValues values = new ContentValues();
            if (sport == SportType.RIDE) {
                values.put("ftp", Double.valueOf(ftp));
                synchronized (this.mDb) {
                    this.mDb.update("Ride", values, "_id = ?", new String[]{String.valueOf(rideId)});
                }
                return;
            }
            if (sport == SportType.RUN) {
                values.put(Run.Field.FTP.name(), Double.valueOf(ftp));
                synchronized (this.mDb) {
                    this.mDb.update(Run.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(rideId)});
                }
            }
        }
    }

    public static void setPeakHR(SportType sport, long rideId, int peakHR) {
        self.doSetPeakHR(sport, rideId, peakHR);
    }

    private void doSetPeakHR(SportType sport, long rideId, int peakHR) {
        if (isReady()) {
            ContentValues values = new ContentValues();
            synchronized (this.mDb) {
                switch (sport) {
                    case RIDE:
                        values.put(Ride.PEAK_HR, Integer.valueOf(peakHR));
                        this.mDb.update("Ride", values, "_id = ?", new String[]{String.valueOf(rideId)});
                        break;
                    case RUN:
                        values.put(Run.Field.PEAK_HR.name(), Integer.valueOf(peakHR));
                        this.mDb.update(Run.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(rideId)});
                        break;
                }
            }
        }
    }

    public static long addRoute() {
        return self.doAddRoute();
    }

    private long doAddRoute() {
        long jInsert;
        if (!isReady()) {
            return 0L;
        }
        ContentValues values = new ContentValues();
        values.put(Route.DISTANCE, Double.valueOf(0.0d));
        values.put(Route.TIME_TO_BEAT, (Integer) 0);
        synchronized (this.mDb) {
            jInsert = this.mDb.insert(Route.NAME, null, values);
        }
        return jInsert;
    }

    private void doRemoveRoute(long routeId) {
        if (isReady()) {
            synchronized (this.mDb) {
                this.mDb.delete(Route.NAME, "_id = ?", new String[]{String.valueOf(routeId)});
            }
            ContentValues values = new ContentValues();
            values.put("routeId", (Long) (-1L));
            synchronized (this.mDb) {
                this.mDb.update("Ride", values, "routeId = ?", new String[]{String.valueOf(routeId)});
            }
        }
    }

    public static void removeRide(long rideId) {
        long trainingId = getRideVirtualWorkoutId(rideId, Workout.RideMode.TRAINING.ordinal());
        if (trainingId != -1) {
            removeTraining(trainingId);
        }
        self.doRemoveRide(rideId);
    }

    public static void removeRun(long runId) {
        long trainingId = getRunVirtualWorkoutId(runId, Workout.RideMode.TRAINING.ordinal());
        if (trainingId != -1) {
            removeTraining(trainingId);
        }
        self.doRemoveRun(runId);
    }

    public static void removeRide(SavedWorkout ride) {
        self.doRemoveRoute(ride.getRouteId());
        self.doRemoveRide(ride.getId());
    }

    public static void removeRun(SavedWorkout run) {
        self.doRemoveRoute(run.getRouteId());
        self.doRemoveRun(run.getId());
    }

    private void doRemoveRide(long rideId) {
        if (isReady()) {
            setRideFinished(rideId, false);
            removeWorkoutDependencies(rideId);
        }
    }

    private void doRemoveRun(long runId) {
        if (isReady()) {
            setRunFinished(runId, false);
            removeWorkoutDependencies(runId);
        }
    }

    private void removeWorkoutDependencies(final long workoutId) {
        synchronized (this.mDb) {
            this.mDb.delete(Shared.NAME, "itemId = ? AND Type = ?", new String[]{String.valueOf(workoutId), String.valueOf(Shared.ShareType.RIDE.getId())});
            this.mDb.delete(Shared.NAME, "itemId = ? AND Type = ?", new String[]{String.valueOf(workoutId), String.valueOf(Shared.ShareType.RIDE_DATA.getId())});
        }
        runOnDbThread("RemoveRide", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.1
            @Override // java.lang.Runnable
            public void run() {
                SQLHelper.this.mDb.delete("Record", Record.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(workoutId)});
                SQLHelper.this.mDb.delete(Lap.NAME, Lap.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(workoutId)});
            }
        });
    }

    public static void removeRoute(long routeId) {
        self.doRemoveRoute(routeId);
    }

    public static void updateRideTSS(long rideId, double tss) {
        self.doUpdateRideProperty("Ride", rideId, Ride.TSS, Double.valueOf(tss));
    }

    public static void updateRunRSS(long rideId, double tss) {
        self.doUpdateRideProperty(Run.TABLE_NAME, rideId, Run.Field.RSS.name(), Double.valueOf(tss));
    }

    public static void updateRideRoute(long rideId, long routeId) {
        self.doUpdateRideProperty("Ride", rideId, "routeId", Long.valueOf(routeId));
    }

    public static void updateRunRoute(long runId, long routeId) {
        self.doUpdateRideProperty(Run.TABLE_NAME, runId, Run.Field.ROUTE_ID.name(), Long.valueOf(routeId));
    }

    static void updateRide(SavedWorkout r) {
        self.doUpdateRide(r, "Ride", true);
    }

    static void updateRun(SavedWorkout r) {
        self.doUpdateRun(r, true);
    }

    public static void setTitle(long id, String title, SportType sportType) {
        switch (sportType) {
            case RIDE:
                self.doUpdateRideProperty("Ride", id, Ride.TITLE, title);
                break;
            case RUN:
                self.doUpdateRideProperty(Run.TABLE_NAME, id, Run.Field.TITLE.name(), title);
                break;
        }
    }

    public static void setRideFinished(long id, boolean finished) {
        self.doUpdateRideProperty("Ride", id, Ride.FINAL, Integer.valueOf(finished ? 1 : 0));
    }

    public static void setRunFinished(long id, boolean finished) {
        self.doUpdateRideProperty(Run.TABLE_NAME, id, Run.Field.FINAL.name(), Integer.valueOf(finished ? 1 : 0));
    }

    private void doUpdateRide(final SavedWorkout r, final String table, final boolean finished) {
        runOnDbThread("UpdateRide", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.2
            @Override // java.lang.Runnable
            public void run() {
                ContentValues values = new ContentValues();
                values.put(Ride.AVERAGE_SPEED, Double.valueOf(r.getAverageSpeed()));
                values.put(Ride.AVERAGE_CADENCE, Double.valueOf(r.getAverageCadence()));
                values.put(Ride.AVERAGE_HEARTRATE, Integer.valueOf(r.getAverageHeartrate()));
                values.put(Ride.AVERAGE_POWER, Double.valueOf(r.getAveragePower()));
                values.put(Ride.DISTANCE, Integer.valueOf((int) r.getDistance()));
                values.put(Ride.GAINED_ALTITUDE, Float.valueOf(r.getGainedAltitude()));
                values.put(Ride.START_TIME_ACTUAL, Long.valueOf(r.getActualStartTime()));
                values.put(Ride.START_TIME, Long.valueOf(r.getStartTime()));
                values.put(Ride.DURATION, Long.valueOf(r.getDuration()));
                values.put(Ride.END_TIME, Long.valueOf(r.getEndTime()));
                values.put(Ride.MAX_ALTITUDE_DIFF, Float.valueOf(r.getMaxAltitudeDiff()));
                values.put(Ride.MAX_CADENCE, Double.valueOf(r.getMaxCadence()));
                values.put(Ride.MAX_HEARTRATE, Integer.valueOf(r.getMaxHeartrate()));
                values.put(Ride.MAX_POWER, Double.valueOf(r.getMaxPower()));
                values.put(Ride.MAX_SPEED, Double.valueOf(r.getMaxSpeed()));
                values.put(Ride.CALORIES, Integer.valueOf(r.getCalories()));
                values.put(Ride.TITLE, r.getTitle());
                values.put(Ride.COMMENT, r.getComment());
                values.put(Ride.ACTIVITY, Integer.valueOf(r.getActivity()));
                values.put(Ride.BIKE_ID, Long.valueOf(r.getBikeId() > 0 ? r.getBikeId() : Prefs.getChosenBikeId()));
                values.put(Ride.RIDER_ID, Long.valueOf(r.getRiderId()));
                values.put("routeId", Long.valueOf(r.getRouteId()));
                values.put(Ride.TARGET_AVERAGE_CADENCE, Integer.valueOf(r.getTargetAverageCadence()));
                values.put(Ride.TARGET_AVERAGE_HEARTRATE, Integer.valueOf(r.getTargetAverageHeartrate()));
                values.put(Ride.TARGET_AVERAGE_POWER, Integer.valueOf(r.getTargetAveragePower()));
                values.put(Ride.TARGET_AVERAGE_SPEED, Double.valueOf(r.getTargetAverageSpeedKm()));
                values.put(Ride.AVERAGE_OXYGEN, Integer.valueOf(r.getAverageOxygen()));
                values.put(Ride.MIN_OXYGEN, Integer.valueOf(r.getMinOxygen()));
                values.put(Ride.AVERAGE_NORM_POWER, Double.valueOf(r.getAverageNormalisedPower()));
                values.put(Ride.MAX_NORM_POWER, Double.valueOf(r.getMaxNormalisedPower()));
                values.put(Ride.AVERAGE_INTENSITY, Double.valueOf(r.getAverageIntensity()));
                values.put(Ride.MAX_INTENSITY, Double.valueOf(r.getMaxIntensity()));
                values.put(Ride.TSS, Double.valueOf(r.getTrainingStress()));
                values.put(Ride.GHOST_RIDE_ID, Long.valueOf(r.getVirtualWorkoutId()));
                values.put(Ride.FINAL, Integer.valueOf(finished ? 1 : 0));
                if (finished && !r.hasLocations()) {
                    values.put("routeId", (Long) (-1L));
                }
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(table, values, "_id = ?", new String[]{String.valueOf(r.getId())});
                    }
                    Log.d(SQLHelper.TAG, "udpateRide " + r.getId() + ", " + r.getTitle());
                }
            }
        });
    }

    private void doUpdateRun(final SavedWorkout r, final boolean finished) {
        runOnDbThread("UpdateRide", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.3
            @Override // java.lang.Runnable
            public void run() {
                ContentValues values = new ContentValues();
                values.put(Run.Field.TITLE.name(), r.getTitle());
                values.put(Run.Field.COMMENT.name(), r.getComment());
                values.put(Run.Field.RUN_TYPE.name(), Integer.valueOf(r.getActivity()));
                values.put(Run.Field.RUNNER_ID.name(), Long.valueOf(r.getRiderId()));
                values.put(Run.Field.ROUTE_ID.name(), Long.valueOf(r.getRouteId()));
                values.put(Run.Field.GHOST_OR_ROUTE_ID.name(), Long.valueOf(r.getVirtualWorkoutId()));
                values.put(Run.Field.START_TIME.name(), Long.valueOf(r.getActualStartTime()));
                values.put(Run.Field.END_TIME.name(), Long.valueOf(r.getEndTime()));
                values.put(Run.Field.DURATION.name(), Long.valueOf(r.getDuration()));
                values.put(Run.Field.DISTANCE.name(), Double.valueOf(r.getDistance()));
                values.put(Run.Field.CALORIES.name(), Integer.valueOf(r.getCalories()));
                values.put(Run.Field.MAX_PACE.name(), Double.valueOf(r.getMaxPace()));
                values.put(Run.Field.AVG_PACE.name(), Double.valueOf(r.getAveragePace()));
                values.put(Run.Field.AVG_CADENCE.name(), Double.valueOf(r.getAverageCadence()));
                values.put(Run.Field.MAX_CADENCE.name(), Double.valueOf(r.getMaxCadence()));
                values.put(Run.Field.AVG_HEARTRATE.name(), Integer.valueOf(r.getAverageHeartrate()));
                values.put(Run.Field.MAX_HEARTRATE.name(), Integer.valueOf(r.getMaxHeartrate()));
                values.put(Run.Field.AVG_POWER.name(), Double.valueOf(r.getAveragePower()));
                values.put(Run.Field.MAX_POWER.name(), Double.valueOf(r.getMaxPower()));
                values.put(Run.Field.AVG_STRIDE.name(), Double.valueOf(r.getAverageStride()));
                values.put(Run.Field.MAX_STRIDE.name(), Double.valueOf(r.getMaxStride()));
                values.put(Run.Field.AVG_NORMALISED_POWER.name(), Double.valueOf(r.getAverageNormalisedPower()));
                values.put(Run.Field.MAX_NORMALISED_POWER.name(), Double.valueOf(r.getMaxNormalisedPower()));
                values.put(Run.Field.AVG_IF.name(), Double.valueOf(r.getAverageIntensity()));
                values.put(Run.Field.MAX_IF.name(), Double.valueOf(r.getMaxIntensity()));
                values.put(Run.Field.AVG_OXYGEN.name(), Integer.valueOf(r.getAverageOxygen()));
                values.put(Run.Field.MIN_OXYGEN.name(), Integer.valueOf(r.getMinOxygen()));
                values.put(Run.Field.RSS.name(), Double.valueOf(r.getTrainingStress()));
                values.put(Run.Field.TARGET_CADENCE.name(), Integer.valueOf(r.getTargetAverageCadence()));
                values.put(Run.Field.TARGET_HEARTRATE.name(), Integer.valueOf(r.getTargetAverageHeartrate()));
                values.put(Run.Field.TARGET_POWER.name(), Integer.valueOf(r.getTargetAveragePower()));
                values.put(Run.Field.TARGET_PACE.name(), Double.valueOf(((SavedRun) r).getTargetAveragePaceMinutesPerKm()));
                values.put(Run.Field.OVERALL_CLIMB.name(), Float.valueOf(r.getGainedAltitude()));
                values.put(Run.Field.ALTITUDE_RANGE.name(), Float.valueOf(r.getMaxAltitudeDiff()));
                values.put(Run.Field.FINAL.name(), Integer.valueOf(finished ? 1 : 0));
                if (finished && !r.hasLocations()) {
                    values.put(Run.Field.ROUTE_ID.name(), (Long) (-1L));
                }
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Run.TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(r.getId())});
                    }
                    Log.d(SQLHelper.TAG, "udpateRide " + r.getId() + ", " + r.getTitle());
                }
            }
        });
    }

    private void doUpdateRideProperty(final String table, final long id, final String key, final Object value) {
        runOnDbThread("UpdateRideProperty", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.4
            @Override // java.lang.Runnable
            public void run() {
                ContentValues values = new ContentValues();
                if (value instanceof String) {
                    values.put(key, (String) value);
                } else if (value instanceof Double) {
                    values.put(key, Double.valueOf(((Double) value).doubleValue()));
                } else if (value instanceof Float) {
                    values.put(key, Float.valueOf(((Float) value).floatValue()));
                } else if (value instanceof Long) {
                    values.put(key, Long.valueOf(((Long) value).longValue()));
                } else if (value instanceof Integer) {
                    values.put(key, Integer.valueOf(((Integer) value).intValue()));
                } else if (value instanceof Boolean) {
                    values.put(key, Boolean.valueOf(((Boolean) value).booleanValue()));
                }
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(table, values, "_id = ?", new String[]{String.valueOf(id)});
                    }
                }
            }
        });
    }

    public static void updateRide(long id, ContentValues values) {
        self.doUpdateRide(id, values, "Ride");
    }

    public static void updateRun(long id, ContentValues values) {
        self.doUpdateRide(id, values, Run.TABLE_NAME);
    }

    private void doUpdateRide(final long id, final ContentValues values, final String table) {
        runOnDbThread("UpdateRide", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.5
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(table, values, "_id = ?", new String[]{String.valueOf(id)});
                    }
                }
            }
        });
    }

    public static void updateRoute(long routeId, long duration, double distance, String routeName) {
        self.doUpdateRoute(routeId, duration, distance, routeName);
    }

    public static void updateRoute(long routeId, ContentValues values) {
        self.doUpdateRoute(routeId, values);
    }

    private void doUpdateRoute(final long routeId, final ContentValues values) {
        if (isReady()) {
            runOnDbThread("UpdateRoute", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.6
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Route.NAME, values, "_id = ?", new String[]{String.valueOf(routeId)});
                    }
                }
            });
        }
    }

    private void doUpdateRoute(final long routeId, final long duration, final double distance, final String routeName) {
        if (isReady()) {
            runOnDbThread("UpdateRoute", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.7
                @Override // java.lang.Runnable
                public void run() {
                    long timeToBeat = 0;
                    Cursor routes = SQLHelper.this.getRouteCursor(routeId);
                    while (routes.moveToNext()) {
                        timeToBeat = routes.getLong(routes.getColumnIndex(Route.TIME_TO_BEAT));
                    }
                    if (timeToBeat == 0 || timeToBeat > duration) {
                        timeToBeat = duration;
                    }
                    ContentValues values = new ContentValues();
                    values.put(Route.DISTANCE, Double.valueOf(distance));
                    values.put(Route.TITLE, routeName);
                    values.put(Route.TIME_TO_BEAT, Long.valueOf(timeToBeat));
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Route.NAME, values, "_id = ?", new String[]{String.valueOf(routeId)});
                        routes.close();
                    }
                }
            });
        }
    }

    public static long addLap(long rideId) {
        return self.doAddLap(rideId, 0L);
    }

    public static long addLap(long rideId, long startTime) {
        return self.doAddLap(rideId, startTime);
    }

    private long doAddLap(long rideId, long startTime) {
        long id;
        if (!isReady()) {
            return 0L;
        }
        ContentValues values = new ContentValues();
        values.put(Lap.Field.FINAL.name(), (Integer) 0);
        values.put(Lap.Field.RIDE_ID.name(), Long.valueOf(rideId));
        values.put(Lap.Field.START_TIME.name(), Long.valueOf(startTime));
        synchronized (this.mDb) {
            id = this.mDb.insert(Lap.NAME, null, values);
            Log.d(TAG, "add lap " + id + ", ride = " + rideId);
        }
        return id;
    }

    public static void updateLap(long lapId, ContentValues lap) {
        self.doUpdateLap(lap, lapId);
    }

    public static void updateLap(Lap.Saved lap) {
        self.doUpdateLap(lap, 0L);
    }

    public static void updateLap(Lap.Saved lap, long offset) {
        self.doUpdateLap(lap, offset);
    }

    private void doUpdateLap(Lap.Saved lap, long offset) {
        doUpdateLap(lap, lap.getId(), offset);
    }

    private void doUpdateLap(final Lap.Saved lap, final long lapId, final long offset) {
        runOnDbThread("UpdateLap", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.8
            @Override // java.lang.Runnable
            public void run() {
                ContentValues values = new ContentValues();
                lap.setBasicMetrics(values);
                values.put(Lap.Field.DISTANCE.name(), Integer.valueOf((int) lap.getDistance()));
                values.put(Lap.Field.GAINED_ALTITUDE.name(), Float.valueOf(lap.getGainedAltitude()));
                values.put(Lap.Field.DURATION.name(), Long.valueOf(lap.getDuration()));
                values.put(Lap.Field.END_TIME.name(), Long.valueOf(lap.getEndTime()));
                values.put(Lap.Field.MAX_ALTITUDE_DIFF.name(), Float.valueOf(lap.getMaxAltitudeDiff()));
                values.put(Lap.Field.CALORIES.name(), Integer.valueOf(lap.getCalories()));
                values.put(Lap.Field.FINAL.name(), (Integer) 1);
                values.put(Lap.Field.TRIGGER.name(), lap.getSplitTrigger());
                values.put(Lap.Field.AVERAGE_OXYGEN.name(), Integer.valueOf(lap.getAverageOxygen()));
                values.put(Lap.Field.MIN_OXYGEN.name(), Integer.valueOf(lap.getMinOxygen()));
                values.put(Lap.Field.AVERAGE_NORM_POWER.name(), Double.valueOf(lap.getAverageNormalisedPower()));
                values.put(Lap.Field.MAX_NORM_POWER.name(), Double.valueOf(lap.getMaxNormalisedPower()));
                values.put(Lap.Field.AVERAGE_INTENSITY.name(), Double.valueOf(lap.getAverageIntensity()));
                values.put(Lap.Field.MAX_INTENSITY.name(), Double.valueOf(lap.getMaxIntensity()));
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Lap.NAME, values, "_id = ?", new String[]{String.valueOf(lapId)});
                        Log.d(SQLHelper.TAG, "update lap " + lapId + ", start = " + (lap.getStartTime() - offset) + ", " + lap.getEndTime());
                    }
                }
            }
        });
    }

    private void doUpdateLap(final ContentValues values, final long lapId) {
        if (isReady()) {
            runOnDbThread("UpdateLap", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.9
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Lap.NAME, values, "_id = ?", new String[]{String.valueOf(lapId)});
                        Log.d(SQLHelper.TAG, "doUpdateLap " + lapId + ", " + values.toString());
                    }
                }
            });
        }
    }

    public static void updateCoordList(List<Coordinate> list, long routeId) {
        for (Coordinate c : list) {
            c.setRouteId(routeId);
            self.doUpdateCoord(c);
        }
    }

    private void doUpdateCoord(final Coordinate coordinate) {
        if (isReady()) {
            runOnDbThread("UpdateCoord", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.10
                @Override // java.lang.Runnable
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put("routeId", Long.valueOf(coordinate.getRouteId()));
                    values.put(Coordinate.ALTITUDE, Float.valueOf(coordinate.getAltitude()));
                    values.put(Coordinate.LONGITUDE, Double.valueOf(coordinate.getLongitude()));
                    values.put(Coordinate.LATITUDE, Double.valueOf(coordinate.getLatitude()));
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Coordinate.NAME, values, "_id = ?", new String[]{String.valueOf(coordinate.getId())});
                    }
                }
            });
        }
    }

    public static void addCoordList(final List<Coordinate> list, final long routeId) {
        self.runOnDbThread("AddCoords", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.11
            @Override // java.lang.Runnable
            public void run() {
                SQLiteStatement insertCoord = SQLHelper.self.mDb.compileStatement("INSERT INTO Coord (routeId, coordLat, coordLong, coordAlt) VALUES (?, ?, ?, ?)");
                synchronized (SQLHelper.self.mDb) {
                    SQLHelper.self.mDb.beginTransaction();
                    for (Coordinate coord : list) {
                        if (coord != null) {
                            insertCoord.clearBindings();
                            insertCoord.bindLong(1, routeId);
                            insertCoord.bindDouble(2, coord.getLatitude());
                            insertCoord.bindDouble(3, coord.getLongitude());
                            insertCoord.bindDouble(4, coord.getAltitude());
                            insertCoord.executeInsert();
                        }
                    }
                    SQLHelper.self.mDb.setTransactionSuccessful();
                    SQLHelper.self.mDb.endTransaction();
                }
            }
        });
    }

    private static void addLocationList(final List<Location> list, final long routeId) {
        self.runOnDbThread("AddCoords", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.12
            @Override // java.lang.Runnable
            public void run() {
                SQLiteStatement insertCoord = SQLHelper.self.mDb.compileStatement("INSERT INTO Coord (routeId, coordLat, coordLong, coordAlt) VALUES (?, ?, ?, ?)");
                synchronized (SQLHelper.self.mDb) {
                    SQLHelper.self.mDb.beginTransaction();
                    for (Location coord : list) {
                        if (coord != null) {
                            insertCoord.clearBindings();
                            insertCoord.bindLong(1, routeId);
                            insertCoord.bindDouble(2, coord.getLatitude());
                            insertCoord.bindDouble(3, coord.getLongitude());
                            insertCoord.bindDouble(4, coord.getAltitude());
                            insertCoord.executeInsert();
                        }
                    }
                    SQLHelper.self.mDb.setTransactionSuccessful();
                    SQLHelper.self.mDb.endTransaction();
                }
            }
        });
    }

    public static boolean hasLocations(long routeId) {
        if (routeId <= 0 || !ready) {
            return false;
        }
        Cursor c = self.mDb.query(Coordinate.NAME, COORD_FIELDS, "routeId = ?", new String[]{routeId + ""}, null, null, "_id ASC");
        boolean multiplePoints = c.getCount() >= 2;
        c.close();
        return multiplePoints;
    }

    public static boolean hasLocations(SportType type, long id) {
        Cursor c;
        if (id <= 0 || !ready) {
            return false;
        }
        switch (type) {
            case RIDE:
                c = self.mDb.query("Record", new String[]{"_id", Record.Field.COORD_ID.name()}, Record.Field.RIDE_ID + " = ?", new String[]{id + ""}, null, null, "_id ASC");
                break;
            case RUN:
                c = self.mDb.query("Record", new String[]{"_id", Record.Field.COORD_ID.name()}, Record.Field.RIDE_ID + " = ?", new String[]{id + ""}, null, null, "_id ASC");
                break;
            default:
                return false;
        }
        boolean multiplePoints = c.getCount() >= 2;
        c.close();
        return multiplePoints;
    }

    public static void addRecords(long rideId, List<Record> records, long routeId) {
        addRecords(rideId, records, routeId, false);
    }

    public static void addRecords(long rideId, List<Record> records, long routeId, boolean waitTillFinish) {
        addRecords(rideId, records, routeId, waitTillFinish, null);
    }

    public static void addRecords(final long rideId, final List<Record> records, final long routeId, boolean waitTillFinish, final DatabaseWorkCallback databaseWorkCallback) {
        Log.d(TAG, "addRecords to ride " + rideId + " record count = " + records.size() + " " + (waitTillFinish ? "WAIT" : "ASYNC"));
        Runnable runnable = new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.13
            @Override // java.lang.Runnable
            public void run() {
                Log.d(SQLHelper.TAG, "  running...");
                if (SQLHelper.ready) {
                    SQLiteStatement insertRecord = SQLHelper.self.mDb.compileStatement(Record.SQL_INSERT);
                    SQLiteStatement insertCoord = SQLHelper.self.mDb.compileStatement("INSERT INTO Coord (routeId, coordLat, coordLong, coordAlt) VALUES (?, ?, ?, ?)");
                    synchronized (SQLHelper.self.mDb) {
                        SQLHelper.self.mDb.beginTransaction();
                        for (Record record : records) {
                            if (record != null) {
                                if (record.hasLocation()) {
                                    insertCoord.clearBindings();
                                    insertCoord.bindLong(1, routeId);
                                    insertCoord.bindDouble(2, record.getLatitude());
                                    insertCoord.bindDouble(3, record.getLongitude());
                                    insertCoord.bindDouble(4, record.getAltitude());
                                    record.setCoordId(insertCoord.executeInsert());
                                } else {
                                    record.setCoordId(-1L);
                                }
                            }
                        }
                        SQLHelper.self.mDb.setTransactionSuccessful();
                        SQLHelper.self.mDb.endTransaction();
                    }
                    synchronized (SQLHelper.self.mDb) {
                        SQLHelper.self.mDb.beginTransaction();
                        for (Record record2 : records) {
                            if (record2 != null) {
                                Log.v(SQLHelper.TAG, "   record = " + record2);
                                insertRecord.clearBindings();
                                insertRecord.bindLong(1, rideId);
                                insertRecord.bindLong(2, record2.getTimestamp());
                                insertRecord.bindLong(12, record2.getResolution());
                                insertRecord.bindLong(3, record2.getCoordId());
                                insertRecord.bindLong(4, record2.isBreak() ? 1L : 0L);
                                insertRecord.bindLong(5, record2.getHeartrate());
                                insertRecord.bindDouble(6, record2.getPower());
                                insertRecord.bindDouble(7, record2.getSpeed());
                                insertRecord.bindDouble(8, record2.getCadence());
                                insertRecord.bindDouble(9, record2.getDistance());
                                insertRecord.bindLong(10, record2.getOxygen());
                                insertRecord.bindDouble(11, record2.getStride());
                                insertRecord.executeInsert();
                            }
                        }
                        SQLHelper.self.mDb.setTransactionSuccessful();
                        SQLHelper.self.mDb.endTransaction();
                    }
                }
                Log.d(SQLHelper.TAG, "  processed all data for ride " + rideId);
                if (databaseWorkCallback != null) {
                    databaseWorkCallback.onComplete();
                }
                synchronized (this) {
                    notify();
                }
            }
        };
        synchronized (runnable) {
            self.runOnDbThread("AddRecords", runnable);
            if (waitTillFinish) {
                try {
                    runnable.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addElevation(final List<CorrectedElevation> elevations, final DatabaseWorkCallback databaseWorkCallback) {
        boolean waitTillFinish = databaseWorkCallback == null;
        Log.d(TAG, "addElevations to ride count = " + elevations.size() + " " + (waitTillFinish ? "WAIT" : "ASYNC"));
        Runnable runnable = new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.14
            @Override // java.lang.Runnable
            public void run() {
                Log.d(SQLHelper.TAG, "  running...");
                if (SQLHelper.ready) {
                    SQLiteStatement statement = SQLHelper.self.mDb.compileStatement(CorrectedElevation.SQL_INSERT);
                    synchronized (SQLHelper.self.mDb) {
                        SQLHelper.self.mDb.beginTransaction();
                        for (CorrectedElevation elevation : elevations) {
                            if (elevation != null) {
                                elevation.bind(statement);
                                statement.executeInsert();
                            }
                        }
                        SQLHelper.self.mDb.setTransactionSuccessful();
                        SQLHelper.self.mDb.endTransaction();
                    }
                }
                Log.d(SQLHelper.TAG, "  processed all data for ride elevations ");
                if (databaseWorkCallback != null) {
                    databaseWorkCallback.onComplete();
                }
                synchronized (this) {
                    notify();
                }
            }
        };
        synchronized (runnable) {
            self.runOnDbThread("AddElevations", runnable);
            if (waitTillFinish) {
                try {
                    runnable.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void insert(SQLiteStatement statement, Object... data) {
        statement.clearBindings();
        int i = 0;
        for (Object object : data) {
            i++;
            if ((object instanceof Long) || (object instanceof Integer)) {
                statement.bindLong(i, ((Long) object).longValue());
            } else if (object instanceof String) {
                statement.bindString(i, (String) object);
            } else if ((object instanceof Double) || (object instanceof Float)) {
                statement.bindDouble(i, ((Double) object).doubleValue());
            }
        }
        statement.executeInsert();
    }

    public static void addShare(Shared share) {
        self.doAddShare(share, null, null);
    }

    public static void addShare(Shared share, AddListener sharedListener) {
        self.doAddShare(share, sharedListener, null);
    }

    public static void addShare(Shared share, ICancellable cancellable) {
        self.doAddShare(share, null, cancellable);
    }

    private void doAddShare(final Shared share, final AddListener sharedListener, ICancellable cancellable) {
        if (isActive(cancellable)) {
            runOnDbThread("AddShare", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.15
                @Override // java.lang.Runnable
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put(Shared.ITEM_ID, Long.valueOf(share.getId()));
                    values.put(Shared.PROVIDER_ID, Integer.valueOf(share.getProviderId()));
                    values.put(Shared.USERNAME, share.getUserName());
                    values.put(Shared.EXTERNAL_ID, share.getImportedFromId());
                    values.put(Shared.TYPE, Integer.valueOf(share.shareType.getId()));
                    values.put(Shared.UPDATED_TIME, Long.valueOf(share.updatedTime));
                    values.put(Shared.WAS_IMPORTED, Integer.valueOf(share.wasImported() ? 1 : 0));
                    synchronized (SQLHelper.this.mDb) {
                        Log.d(SQLHelper.TAG, "addSharerow " + share.toString());
                        long rowId = SQLHelper.this.mDb.insert(Shared.NAME, null, values);
                        if (sharedListener != null) {
                            sharedListener.onAdded(rowId);
                        }
                    }
                }
            });
        }
    }

    public static void markRideFinishedShared(long id, Shared rideHeaderShared, Shared rideDataShared) {
        self.doMarkRideFinishedShared(id, rideHeaderShared, rideDataShared);
    }

    private void doMarkRideFinishedShared(final long id, final Shared share, final Shared rideDataShared) {
        runOnDbThread("UpdateShareFinishRide", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.16
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.self.mDb) {
                        ContentValues values = new ContentValues();
                        values.put(Shared.EXTERNAL_ID, share.getImportedFromId());
                        values.put(Shared.UPDATED_TIME, Long.valueOf(share.updatedTime));
                        ContentValues values2 = new ContentValues();
                        values2.put(Shared.EXTERNAL_ID, rideDataShared.getImportedFromId());
                        values2.put(Shared.UPDATED_TIME, Long.valueOf(rideDataShared.updatedTime));
                        String table = "";
                        String fieldFinal = "";
                        switch (AnonymousClass30.$SwitchMap$com$kopin$solos$storage$Shared$ShareType[share.shareType.ordinal()]) {
                            case 1:
                            case 2:
                                table = "Ride";
                                fieldFinal = Ride.FINAL;
                                break;
                            case 3:
                            case 4:
                                table = Run.TABLE_NAME;
                                fieldFinal = Run.Field.FINAL.name();
                                break;
                        }
                        ContentValues values3 = new ContentValues();
                        values3.put(fieldFinal, (Integer) 1);
                        SQLHelper.self.mDb.beginTransaction();
                        SQLHelper.this.mDb.update(Shared.NAME, values, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ? ", new String[]{String.valueOf(share.getId()), String.valueOf(share.getProviderId()), share.getUserName(), String.valueOf(share.shareType.getId())});
                        SQLHelper.this.mDb.update(Shared.NAME, values2, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ? ", new String[]{String.valueOf(rideDataShared.getId()), String.valueOf(rideDataShared.getProviderId()), rideDataShared.getUserName(), String.valueOf(rideDataShared.shareType.getId())});
                        SQLHelper.this.mDb.update(table, values3, "_id = ?", new String[]{String.valueOf(id)});
                        SQLHelper.self.mDb.setTransactionSuccessful();
                        SQLHelper.self.mDb.endTransaction();
                    }
                }
            }
        });
    }

    public static void updateShare(Shared share) {
        self.doUpdateShare(share);
    }

    private void doUpdateShare(final Shared share) {
        runOnDbThread("UpdateShare", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.17
            @Override // java.lang.Runnable
            public void run() {
                ContentValues values = new ContentValues();
                values.put(Shared.EXTERNAL_ID, share.getImportedFromId());
                values.put(Shared.UPDATED_TIME, Long.valueOf(share.updatedTime));
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(Shared.NAME, values, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ? ", new String[]{String.valueOf(share.getId()), String.valueOf(share.getProviderId()), share.getUserName(), String.valueOf(share.shareType.getId())});
                    }
                }
            }
        });
    }

    public static void removeShare(Shared share) {
        if (ready) {
            synchronized (self.mDb) {
                self.mDb.delete(Shared.NAME, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ? ", new String[]{String.valueOf(share.getId()), String.valueOf(share.getProviderId()), share.getUserName(), String.valueOf(share.shareType.getId())});
            }
        }
    }

    public static Shared getShare(long rideId, int providerId, Shared.ShareType type) {
        if (!isReady()) {
            return null;
        }
        Cursor c = self.mDb.query(Shared.NAME, null, "itemId = ? AND sProId = ? AND Type = ?", new String[]{String.valueOf(rideId), String.valueOf(providerId), String.valueOf(type.getId())}, null, null, null);
        Shared share = null;
        if (c.moveToNext()) {
            share = new Shared(c);
        }
        c.close();
        return share;
    }

    private boolean isShared(long rideId, int providerId, String username, Shared.ShareType shareType) {
        if (!isReady()) {
            return false;
        }
        Cursor c = this.mDb.query(Shared.NAME, new String[]{Shared.USERNAME, Shared.EXTERNAL_ID}, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ?", new String[]{String.valueOf(rideId), String.valueOf(providerId), username, String.valueOf(shareType.getId())}, null, null, null);
        boolean zMoveToNext = c.moveToNext();
        c.close();
        return zMoveToNext;
    }

    public static boolean isItemSynced(String externalId, int providerId, String username, Shared.ShareType shareType) {
        if (username == null || externalId == null || !ready) {
            return false;
        }
        return self.isSynced(externalId, providerId, username.trim(), shareType);
    }

    private boolean isSynced(String externalId, int providerId, String username, Shared.ShareType shareType) {
        if (!isReady()) {
            return false;
        }
        Cursor c = this.mDb.query(Shared.NAME, new String[]{Shared.USERNAME, Shared.EXTERNAL_ID}, "sExternalId = ? AND sProId = ? AND sUser = ? AND Type = ?", new String[]{externalId, String.valueOf(providerId), username, String.valueOf(shareType.getId())}, null, null, null);
        boolean zMoveToNext = c.moveToNext();
        c.close();
        return zMoveToNext;
    }

    public static String getExternalId(long internalId, int providerId, String username, Shared.ShareType shareType) {
        String id = null;
        if (ready) {
            Cursor c = self.mDb.query(Shared.NAME, new String[]{Shared.USERNAME, Shared.EXTERNAL_ID}, "itemId = ? AND sProId = ? AND sUser = ? AND Type = ?", new String[]{String.valueOf(internalId), String.valueOf(providerId), username, String.valueOf(shareType.getId())}, null, null, null);
            if (c.moveToNext()) {
                id = c.getString(1);
            }
            c.close();
        }
        return id;
    }

    public static long getLocalId(String externalId, int providerId, String username, Shared.ShareType shareType) {
        long id = 0;
        if (ready) {
            Cursor c = self.mDb.query(Shared.NAME, new String[]{Shared.ITEM_ID}, "sExternalId = ? AND sProId = ? AND sUser = ? AND Type = ?", new String[]{externalId, String.valueOf(providerId), username, String.valueOf(shareType.getId())}, null, null, null);
            if (c.moveToNext()) {
                id = c.getLong(0);
                Log.d(TAG, "getLocalId id = " + id + ", external " + externalId);
            }
            c.close();
        }
        return id;
    }

    public static HashSet<String> getImportedRideIds(long platformSharedKey) {
        HashSet<String> list = new HashSet<>();
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sProId = ? AND sWasImported != 0", new String[]{String.valueOf(platformSharedKey)}, null, null, null);
            while (cursor.moveToNext()) {
                Shared shared = new Shared(cursor);
                list.add(shared.getImportedFromId());
            }
            cursor.close();
        }
        return list;
    }

    public static HashMap<Long, String> getImportShareMap(long platformSharedKey) {
        HashMap<Long, String> map = new HashMap<>();
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sProId = ? AND sWasImported != 0", new String[]{String.valueOf(platformSharedKey)}, null, null, null);
            while (cursor.moveToNext()) {
                Shared shared = new Shared(cursor);
                map.put(Long.valueOf(shared.getId()), shared.mExternalId);
            }
            cursor.close();
        }
        return map;
    }

    public static boolean wasImported(String id, long platformSharedKey) {
        boolean imported = false;
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sProId = ? AND sExternalId = ?", new String[]{"" + platformSharedKey, id}, null, null, null);
            if (cursor.moveToNext()) {
                imported = true;
            }
            cursor.close();
        }
        return imported;
    }

    public static boolean wasImported(long rideId, int platformSharedKey) {
        boolean imported = false;
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sExternalId IS NOT NULL AND sExternalId != \"\" AND sProId = ? AND itemId = ?  AND sWasImported != 0", new String[]{String.valueOf(platformSharedKey), String.valueOf(rideId)}, null, null, null);
            if (cursor.moveToNext()) {
                imported = true;
            }
            cursor.close();
        }
        return imported;
    }

    public static boolean wasImportedExceptPlatform(long rideId, int ignorePlatfromId) {
        boolean imported = false;
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sExternalId IS NOT NULL AND sExternalId != \"\" AND sProId IS NOT NULL AND itemId = ?  AND sWasImported != 0 AND sProId != ?", new String[]{String.valueOf(rideId), String.valueOf(ignorePlatfromId)}, null, null, null);
            if (cursor.moveToNext()) {
                imported = true;
            }
            cursor.close();
        }
        return imported;
    }

    public static boolean wasImported(long rideId) {
        boolean imported = false;
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sExternalId IS NOT NULL AND sExternalId != \"\" AND sProId IS NOT NULL AND itemId = ?  AND sWasImported != 0", new String[]{String.valueOf(rideId)}, null, null, null);
            if (cursor.moveToNext()) {
                imported = true;
            }
            cursor.close();
        }
        return imported;
    }

    public static boolean isRideShared(long rideId, int providerId, String username) {
        return isItemShared(rideId, providerId, username, Shared.ShareType.RIDE) || isItemShared(rideId, providerId, username, Shared.ShareType.RUN);
    }

    public static boolean isItemShared(long rideId, int providerId, String username, Shared.ShareType shareType) {
        if (username == null || !ready) {
            return false;
        }
        return self.isShared(rideId, providerId, username.trim(), shareType);
    }

    public static boolean isItemShared(Shared shared) {
        if (shared.getUserName() == null || !ready) {
            return false;
        }
        return self.isShared(shared.getId(), shared.getProviderId(), shared.getUserName().trim(), shared.shareType);
    }

    public static Cursor getRoutesCursor(boolean reverse) {
        String[] args = {"0", ""};
        return self.mDb.query(Route.NAME, ROUTE_FIELDS, "routeDistance > ? AND routeName IS NOT NULL AND routeName != ?", args, null, null, "_id" + (reverse ? " DESC" : " ASC"));
    }

    public static Cursor getAllRoutesCursor(boolean reverse) {
        String[] args = {"0"};
        return self.mDb.query(Route.NAME, ROUTE_FIELDS, "routeDistance > ?", args, null, null, "_id" + (reverse ? " DESC" : " ASC"));
    }

    public static Cursor getRoutesCursor(boolean reverse, String search) {
        return self.mDb.query(Route.NAME, ROUTE_FIELDS, "routeName LIKE ?", new String[]{"%" + search + "%"}, null, null, "_id" + (reverse ? " DESC" : " ASC"));
    }

    public static List<Coordinate> getRouteDetails(boolean reverse, long routeId) {
        List<Coordinate> returnList = new ArrayList<>();
        if (ready) {
            Cursor c = self.mDb.query(Coordinate.NAME, COORD_FIELDS, "routeId = ?", new String[]{routeId + ""}, null, null, "_id" + (reverse ? " DESC" : " ASC"));
            while (c.moveToNext()) {
                Coordinate coord = new Coordinate(c);
                returnList.add(coord);
            }
            c.close();
        }
        return returnList;
    }

    public static boolean isSavedRoute(long routeId) {
        if (!ready) {
            return false;
        }
        Cursor c = self.mDb.query(Route.NAME, new String[]{Route.TITLE}, "_id = ? AND routeName IS NOT NULL AND routeName != ?", new String[]{String.valueOf(routeId), ""}, null, null, null);
        boolean zMoveToNext = c.moveToNext();
        c.close();
        return zMoveToNext;
    }

    public static void unSaveRoute(long routeId) {
        ContentValues values = new ContentValues();
        values.put(Route.TITLE, "");
        updateRoute(routeId, values);
    }

    private static Cursor getMaxCursor(String table) {
        return self.mDb.rawQuery("SELECT MAX(_id) FROM " + table, null);
    }

    static Cursor getRideHeadersCursor(long fromTs, boolean reverse, boolean requireRoute, boolean showInComplete) {
        SQLiteDatabase sQLiteDatabase = self.mDb;
        String[] strArr = RIDE_HEADER_FIELDS;
        String str = "rFinal = ? AND rActualStartTime >= ?" + (requireRoute ? " AND routeId > 0" : "");
        String[] strArr2 = new String[2];
        strArr2[0] = showInComplete ? "0" : AppEventsConstants.EVENT_PARAM_VALUE_YES;
        strArr2[1] = String.valueOf(fromTs);
        return sQLiteDatabase.query("Ride", strArr, str, strArr2, null, null, Ride.START_TIME_ACTUAL + (reverse ? " DESC" : " ASC"));
    }

    private static Cursor getRidesCursor(long fromTs, boolean reverse, boolean requireRoute) {
        return self.mDb.query("Ride", RIDE_FIELDS, "rFinal = ? AND rActualStartTime >= ?" + (requireRoute ? " AND routeId > 0" : ""), new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(fromTs)}, null, null, Ride.START_TIME_ACTUAL + (reverse ? " DESC" : " ASC"));
    }

    static Cursor getImportedRideHeadersCursor(boolean requireRoute, int excludePlatform) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (").append("SELECT ").append(TextUtils.join(", ", RIDE_HEADER_FIELDS)).append(" FROM ").append("Ride").append(" WHERE ").append(Ride.FINAL).append("=1");
        if (requireRoute) {
            sb.append(" AND ").append("routeId").append(" > 0 ");
        }
        sb.append(" ORDER BY ").append(Ride.START_TIME_ACTUAL).append(" DESC ").append(")").append(" AS T1").append(" WHERE EXISTS ( SELECT 1 FROM ").append(Shared.NAME).append(" WHERE ").append(Shared.WAS_IMPORTED).append("=1").append(" AND ").append(Shared.EXTERNAL_ID).append(" IS NOT NULL AND ").append(Shared.EXTERNAL_ID).append(" != \"\" AND ").append(Shared.PROVIDER_ID).append(" !=").append(String.valueOf(excludePlatform)).append(" AND ").append(Shared.ITEM_ID).append("=").append("T1.").append("_id").append(")");
        return self.mDb.rawQuery(sb.toString(), null);
    }

    static Cursor getImportedRunHeadersCursor(boolean requireRoute, int excludePlatform) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (").append("SELECT ").append(TextUtils.join(", ", Run.RUN_HEADER_FIELDS)).append(" FROM ").append(Run.TABLE_NAME).append(" WHERE ").append(Run.Field.FINAL.name()).append("=1");
        if (requireRoute) {
            sb.append(" AND ").append(Run.Field.ROUTE_ID.name()).append(" > 0 ");
        }
        sb.append(" ORDER BY ").append(Run.Field.START_TIME.name()).append(" DESC ").append(")").append(" AS T1").append(" WHERE EXISTS ( SELECT 1 FROM ").append(Shared.NAME).append(" WHERE ").append(Shared.WAS_IMPORTED).append("=1").append(" AND ").append(Shared.EXTERNAL_ID).append(" IS NOT NULL AND ").append(Shared.EXTERNAL_ID).append(" != \"\" AND ").append(Shared.PROVIDER_ID).append("!=").append(String.valueOf(excludePlatform)).append(" AND ").append(Shared.ITEM_ID).append("=").append("T1.").append("_id").append(")");
        return self.mDb.rawQuery(sb.toString(), null);
    }

    static Cursor getRideHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        String virtualIdConditionRide = "";
        String virtualTableName = null;
        String additionMode = "";
        switch (mode) {
            case TRAINING:
                virtualIdConditionRide = " AND ghostRideId != -1";
                virtualTableName = Training.NAME;
                break;
            case GHOST_RIDE:
                virtualIdConditionRide = " AND ghostRideId != -1";
                break;
            case ROUTE:
                virtualIdConditionRide = " AND routeId != -1";
                break;
            case TARGETS:
                additionMode = " OR rType=" + Workout.RideMode.NORMAL.ordinal() + " AND (" + Ride.TARGET_AVERAGE_CADENCE + " > 0 OR " + Ride.TARGET_AVERAGE_HEARTRATE + " > 0 OR " + Ride.TARGET_AVERAGE_POWER + " > 0 OR " + Ride.TARGET_AVERAGE_SPEED + " > 0 )";
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (").append("SELECT ").append(TextUtils.join(", ", RIDE_HEADER_FIELDS)).append(" FROM ").append("Ride").append(" WHERE ").append("(").append(Ride.RIDE_MODE).append("=").append(mode.ordinal()).append(additionMode).append(")").append(" AND ").append(Ride.FINAL).append("=1").append(virtualIdConditionRide);
        if (requireRoute) {
            sb.append(" AND ").append("routeId").append(" > 0 ");
        }
        sb.append(" ORDER BY ").append(Ride.START_TIME_ACTUAL).append(" DESC ").append(")");
        if (virtualTableName != null) {
            sb.append(" WHERE EXISTS ( SELECT 1 FROM ").append(virtualTableName).append(" WHERE ").append("_id").append("=").append(Ride.GHOST_RIDE_ID).append(")");
        }
        return self.mDb.rawQuery(sb.toString(), null);
    }

    static Cursor getRunHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        String virtualIdConditionRun = "";
        String virtualTableName = null;
        String additionMode = "";
        switch (mode) {
            case TRAINING:
                virtualIdConditionRun = " AND " + Run.Field.GHOST_OR_ROUTE_ID.name() + " != -1";
                virtualTableName = Training.NAME;
                break;
            case GHOST_RIDE:
                virtualIdConditionRun = " AND " + Run.Field.GHOST_OR_ROUTE_ID.name() + " != -1";
                break;
            case ROUTE:
                virtualIdConditionRun = " AND " + Run.Field.ROUTE_ID.name() + " != -1";
                break;
            case TARGETS:
                additionMode = " OR " + Run.Field.RUN_MODE.name() + "=" + Workout.RideMode.NORMAL.ordinal() + " AND (" + Run.Field.TARGET_CADENCE.name() + " > 0 OR " + Run.Field.TARGET_HEARTRATE.name() + " > 0 OR " + Run.Field.TARGET_POWER.name() + " > 0 OR " + Run.Field.TARGET_STRIDE.name() + " > 0 OR " + Run.Field.TARGET_PACE.name() + " > 0 )";
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (").append("SELECT ").append(TextUtils.join(", ", Run.RUN_HEADER_FIELDS)).append(" FROM ").append(Run.TABLE_NAME).append(" WHERE ").append("(").append(Run.Field.RUN_MODE.name()).append("=").append(mode.ordinal()).append(additionMode).append(")").append(" AND ").append(Run.Field.FINAL.name()).append("=1").append(virtualIdConditionRun);
        if (requireRoute) {
            sb.append(" AND ").append(Run.Field.ROUTE_ID.name()).append(" > 0 ");
        }
        sb.append(" ORDER BY ").append(Run.Field.START_TIME).append(" DESC ").append(")");
        if (virtualTableName != null) {
            sb.append(" WHERE EXISTS ( SELECT 1 FROM ").append(virtualTableName).append(" WHERE ").append("_id").append("=").append(Run.Field.GHOST_OR_ROUTE_ID.name()).append(")");
        }
        return self.mDb.rawQuery(sb.toString(), null);
    }

    static Cursor getWorkoutHeaders(Workout.RideMode mode) {
        String virtualIdConditionRide = "";
        String virtualIdConditionRun = "";
        String virtualTableName = null;
        switch (mode) {
            case TRAINING:
                virtualIdConditionRide = " AND ghostRideId != -1";
                virtualIdConditionRun = " AND " + Run.Field.GHOST_OR_ROUTE_ID.name() + " != -1";
                virtualTableName = Training.NAME;
                break;
            case GHOST_RIDE:
                virtualIdConditionRide = " AND ghostRideId != -1";
                virtualIdConditionRun = " AND " + Run.Field.GHOST_OR_ROUTE_ID.name() + " != -1";
                break;
            case ROUTE:
                virtualIdConditionRide = " AND routeId != -1";
                virtualIdConditionRun = " AND " + Run.Field.ROUTE_ID.name() + " != -1";
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM (").append("SELECT ").append(TextUtils.join(", ", RIDE_HEADER_FIELDS)).append(" FROM ").append("Ride").append(" WHERE ").append(Ride.RIDE_MODE).append("=").append(mode.ordinal()).append(" AND ").append(Ride.FINAL).append("=1").append(virtualIdConditionRide).append(" UNION ").append("SELECT ").append(TextUtils.join(", ", Run.RUN_HEADER_FIELDS)).append(" FROM ").append(Run.TABLE_NAME).append(" WHERE ").append(Run.Field.RUN_MODE.name()).append("=").append(mode.ordinal()).append(" AND ").append(Run.Field.FINAL.name()).append("=1").append(virtualIdConditionRun).append(" ORDER BY ").append(Ride.START_TIME_ACTUAL).append(" DESC ").append(")");
        if (virtualTableName != null) {
            sb.append(" WHERE EXISTS ( SELECT 1 FROM ").append(virtualTableName).append(" WHERE ").append("_id").append("=").append(Ride.GHOST_RIDE_ID).append(")");
        }
        return self.mDb.rawQuery(sb.toString(), null);
    }

    static int getVirtualWorkoutCount(Workout.RideMode mode, long virtualWorkoutId) {
        if (!ready) {
            return -1;
        }
        String query = "SELECT  _id FROM Ride WHERE rType=" + String.valueOf(mode.ordinal()) + " AND " + Ride.FINAL + "=1 AND " + Ride.GHOST_RIDE_ID + "=" + String.valueOf(virtualWorkoutId) + " UNION SELECT  _id FROM " + Run.TABLE_NAME + " WHERE " + Run.Field.RUN_MODE.name() + "=" + String.valueOf(mode.ordinal()) + " AND " + Run.Field.FINAL.name() + "=1 AND " + Run.Field.GHOST_OR_ROUTE_ID + "=" + String.valueOf(virtualWorkoutId);
        Cursor cursor = self.mDb.rawQuery(query, null);
        if (cursor.moveToNext()) {
            return cursor.getCount();
        }
        return -1;
    }

    private static long getRideVirtualWorkoutId(long rideId, int rideMode) {
        Cursor cursor = self.mDb.query("Ride", new String[]{Ride.GHOST_RIDE_ID}, "_id = ? AND rType = ?", new String[]{String.valueOf(rideId), String.valueOf(rideMode)}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    private static long getRunVirtualWorkoutId(long runId, int runMode) {
        Cursor cursor = self.mDb.query(Run.TABLE_NAME, new String[]{Run.Field.RUN_MODE.name()}, "_id = ? AND " + Run.Field.RUN_MODE.name() + " = ?", new String[]{String.valueOf(runId), String.valueOf(runMode)}, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return -1L;
    }

    public static double getRunAvgSpeed(long since) {
        String query = "SELECT ( SUM(" + Run.Field.DISTANCE.name() + ") /  SUM(" + Run.Field.DURATION.name() + ") * 1000 ) FROM " + Run.TABLE_NAME + " WHERE " + Run.Field.START_TIME + " >= " + since + " AND " + Run.Field.FINAL.name() + "=1";
        Cursor cursor = self.mDb.rawQuery(query, null);
        if (cursor.moveToNext()) {
            return cursor.getDouble(0);
        }
        return -2.147483648E9d;
    }

    public static double getRideAverageSpeed(long since) {
        String query = "SELECT ( SUM(rDistance) /  SUM(rDuration) * 1000 ) FROM Ride WHERE rActualStartTime >= " + since + " AND " + Ride.FINAL + "=1";
        Cursor cursor = self.mDb.rawQuery(query, null);
        if (cursor.moveToNext()) {
            return cursor.getDouble(0);
        }
        return -2.147483648E9d;
    }

    private static Cursor getRidesCursor(long periodStart, long periodEnd, String table) {
        return self.mDb.query(table, RIDE_FIELDS, "rFinal = ? AND rActualStartTime >= ? AND rActualStartTime <= ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(periodStart), String.valueOf(periodEnd)}, null, null, "rActualStartTime DESC");
    }

    static void foreachRide(long timestamp, foreachWorkoutCallback cb) {
        if (cb != null && ready) {
            Cursor c = getRidesCursor(timestamp, false, false);
            boolean cont = true;
            while (c.moveToNext() && cont) {
                SavedRide ride = new SavedRide(c);
                cont = cb.onWorkout(ride);
            }
            c.close();
        }
    }

    static void foreachRideCursor(long timestamp, foreachWorkoutCursorCallback cb) {
        if (cb != null && ready) {
            Cursor c = getRidesCursor(timestamp, false, false);
            boolean cont = true;
            while (c.moveToNext() && cont) {
                cont = cb.onWorkoutCursor(c);
            }
            c.close();
        }
    }

    public static List<Double> getDistancesForPeriods(List<Long> startRidePeriods, String table, int allowImportedPlatform, SportType sportType) {
        List<Double> distances = new ArrayList<>();
        Long startRide = startRidePeriods.get(0);
        if (ready) {
            int i = 0;
            for (Long period : startRidePeriods) {
                if (i > 0) {
                    double distance = 0.0d;
                    Cursor c = SportType.RUN == sportType ? getRunsCursor(startRide.longValue(), period.longValue()) : getRidesCursor(startRide.longValue(), period.longValue(), table);
                    while (c.moveToNext() && 1 != 0) {
                        if (SportType.RUN == sportType && !wasImportedExceptPlatform(SavedRun.getIdFromCursor(c), allowImportedPlatform)) {
                            distance += SavedRun.getDistanceFromCursor(c);
                        } else if (!wasImportedExceptPlatform(SavedRide.getIdFromCursor(c), allowImportedPlatform)) {
                            distance += SavedRide.getDistanceFromCursor(c);
                        }
                    }
                    c.close();
                    distances.add(Double.valueOf(distance));
                    startRide = period;
                }
                i++;
            }
        }
        return distances;
    }

    static Cursor getRideCursor(long rideId) {
        if (ready) {
            return self.mDb.query("Ride", RIDE_FIELDS, "_id = ?", new String[]{String.valueOf(rideId)}, null, null, "rActualStartTime DESC");
        }
        return null;
    }

    static SavedRide getRide(long rideId, boolean allowUnfinished) {
        SavedRide ride = null;
        if (ready) {
            Cursor data = getRideCursor(rideId, allowUnfinished);
            if (data.moveToNext()) {
                ride = new SavedRide(data);
                Log.d(TAG, "getRide " + rideId + ", " + ride.getTitle());
            }
            data.close();
        }
        return ride;
    }

    static Cursor getRideCursor(long rideId, boolean allowUnfinished) {
        if (allowUnfinished) {
            Cursor data = self.mDb.query("Ride", RIDE_FIELDS, "_id = ?", new String[]{String.valueOf(rideId)}, null, null, "rActualStartTime DESC");
            return data;
        }
        Cursor data2 = self.mDb.query("Ride", RIDE_FIELDS, "rFinal = ? AND _id = ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(rideId)}, null, null, "rActualStartTime DESC");
        return data2;
    }

    public static Lap.Saved getLap(long rideId, int lapNum) {
        if (self.mDb == null || !ready) {
            return null;
        }
        Cursor c = getLapCursor(rideId, lapNum, 1, false);
        Lap.Saved lap = null;
        if (c.moveToNext()) {
            lap = new Lap.Saved(c);
        }
        c.close();
        Log.d(TAG, "getLap " + lapNum + ", " + rideId);
        return lap;
    }

    public static List<Lap.Saved> getLaps(long rideId, int offset, int limit) {
        if (self.mDb == null || !ready) {
            return null;
        }
        Cursor c = getLapCursor(rideId, offset, limit, false);
        ArrayList<Lap.Saved> laps = new ArrayList<>();
        while (c.moveToNext()) {
            laps.add(new Lap.Saved(c));
        }
        c.close();
        return laps;
    }

    static long getLastIncompleteRideId() {
        long rideId = -1;
        Cursor cursor = self.mDb.query("Ride", RIDE_FIELDS, "rFinal = ? AND rTitle is null", new String[]{"0"}, null, null, "rActualStartTime DESC");
        if (cursor.moveToNext()) {
            rideId = cursor.getLong(cursor.getColumnIndex("_id"));
            Log.d("IncompleteRide", "Get Id:" + rideId + " name:" + cursor.getString(cursor.getColumnIndex(Ride.TITLE)));
        }
        cursor.close();
        return rideId;
    }

    public static void removeIncompleteWorkouts() {
        self.runOnDbThread("RemoveIncompleteRides", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.18
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.ready) {
                    synchronized (SQLHelper.self.mDb) {
                        long rows = SQLHelper.self.mDb.delete("Ride", "rFinal = ? AND rTitle is null", new String[]{"0"});
                        long rows1 = SQLHelper.self.mDb.delete(Run.TABLE_NAME, Run.Field.FINAL.name() + " = ? AND " + Run.Field.TITLE.name() + " is null", new String[]{"0"});
                        Log.d("IncompleteRide", "Removed rides: " + rows);
                        Log.d("IncompleteRuns", "Removed runs: " + rows1);
                    }
                }
            }
        });
    }

    static SavedRun getRun(long runId, boolean allowUnfinished) {
        SavedRun savedRun = null;
        if (ready) {
            Cursor data = getRunCursor(runId, allowUnfinished);
            if (data.moveToNext()) {
                savedRun = new SavedRun(data);
                Log.d(TAG, "getRun " + runId + ", " + savedRun.getTitle());
            }
            data.close();
        }
        return savedRun;
    }

    public static Cursor getRunHeadersCursor(long fromTs, boolean reverse, boolean requireRoute, boolean showIncomplete) {
        SQLiteDatabase sQLiteDatabase = self.mDb;
        String[] strArr = Run.RUN_HEADER_FIELDS;
        String str = Run.Field.FINAL.name() + " = ? AND " + Run.Field.START_TIME.name() + " >= ?" + (requireRoute ? " AND " + Run.Field.ROUTE_ID.name() + " > 0" : "");
        String[] strArr2 = new String[2];
        strArr2[0] = showIncomplete ? "0" : AppEventsConstants.EVENT_PARAM_VALUE_YES;
        strArr2[1] = String.valueOf(fromTs);
        return sQLiteDatabase.query(Run.TABLE_NAME, strArr, str, strArr2, null, null, Run.Field.START_TIME.name() + (reverse ? " DESC" : " ASC"));
    }

    static Cursor getRunCursor(long rideId) {
        if (ready) {
            return self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, "_id = ?", new String[]{String.valueOf(rideId)}, null, null, Run.Field.START_TIME.name() + " DESC");
        }
        return null;
    }

    static Cursor getRunCursor(long runId, boolean allowIncomplete) {
        if (allowIncomplete) {
            Cursor data = self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, "_id = ?", new String[]{String.valueOf(runId)}, null, null, Run.Field.START_TIME.name() + " DESC");
            return data;
        }
        Cursor data2 = self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, Run.Field.FINAL.name() + " = ? AND _id = ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(runId)}, null, null, Run.Field.START_TIME.name() + " DESC");
        return data2;
    }

    private static Cursor getRunsCursor(long periodStart, long periodEnd) {
        return self.mDb.query(Run.TABLE_NAME, null, Run.Field.FINAL.name() + " = ? AND " + Run.Field.START_TIME.name() + " >= ? AND " + Run.Field.START_TIME.name() + " <= ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(periodStart), String.valueOf(periodEnd)}, null, null, Run.Field.START_TIME.name() + " DESC");
    }

    private static Cursor getRunsCursor(long fromTs, boolean reverse, boolean requireRoute) {
        return self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, Run.Field.FINAL.name() + " = ? AND " + Run.Field.START_TIME.name() + " >= ?" + (requireRoute ? " AND " + Run.Field.ROUTE_ID.name() + " > 0" : ""), new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, String.valueOf(fromTs)}, null, null, Run.Field.START_TIME.name() + (reverse ? " DESC" : " ASC"));
    }

    public static Cursor getIncompleteRunsCursor() {
        return null;
    }

    public static long getLastIncompleteRunId() {
        Cursor cursor = self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, Run.Field.FINAL.name() + " = ? AND " + Run.Field.TITLE.name() + " is null", new String[]{"0"}, null, null, Run.Field.START_TIME.name() + " DESC");
        if (!cursor.moveToNext()) {
            return -1L;
        }
        long runId = cursor.getLong(cursor.getColumnIndex("_id"));
        Log.d("IncompleteRun", "Get Id:" + runId + " name:" + cursor.getString(cursor.getColumnIndex(Run.Field.TITLE.name())));
        return runId;
    }

    public static void removeIncompleteRuns() {
    }

    static void foreachRun(long timestamp, foreachWorkoutCallback cb) {
        if (cb != null && ready) {
            Cursor c = getRunsCursor(timestamp, false, false);
            boolean cont = true;
            while (c.moveToNext() && cont) {
                SavedRun ride = new SavedRun(c);
                cont = cb.onWorkout(ride);
            }
            c.close();
        }
    }

    static void foreachRunCursor(long timestamp, foreachWorkoutCursorCallback cb) {
        if (cb != null && ready) {
            Cursor c = getRunsCursor(timestamp, false, false);
            boolean cont = true;
            while (c.moveToNext() && cont) {
                cont = cb.onWorkoutCursor(c);
            }
            c.close();
        }
    }

    static boolean hasGhostRuns(long rideId) {
        if (!ready) {
            return false;
        }
        Cursor cursor = getGhostRuns(rideId);
        boolean zMoveToNext = cursor.moveToNext();
        cursor.close();
        return zMoveToNext;
    }

    private static Cursor getGhostRuns(long id) {
        String ghostMode = String.valueOf(Workout.RideMode.GHOST_RIDE.ordinal());
        return self.mDb.query(Run.TABLE_NAME, Run.RUN_FIELDS, Run.Field.FINAL.name() + " = ? AND " + Run.Field.RUN_MODE.name() + " = ? AND " + Run.Field.GHOST_OR_ROUTE_ID.name() + " = ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, ghostMode, String.valueOf(id)}, null, null, null);
    }

    static Cursor getLapCursor(long rideId) {
        return self.mDb.query(Lap.NAME, null, Lap.Field.RIDE_ID.name() + " = ? AND " + Lap.Field.FINAL.name() + " = ?", new String[]{String.valueOf(rideId), AppEventsConstants.EVENT_PARAM_VALUE_YES}, null, null, null);
    }

    static Cursor getLapCursor(long rideId, int offset, int limit, boolean includeIncomplete) {
        String limitFormat = offset + "," + limit;
        if (includeIncomplete) {
            return self.mDb.query(Lap.NAME, null, Lap.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(rideId)}, null, null, null, limitFormat);
        }
        return self.mDb.query(Lap.NAME, null, Lap.Field.RIDE_ID.name() + " = ? AND " + Lap.Field.FINAL.name() + " = ?", new String[]{String.valueOf(rideId), AppEventsConstants.EVENT_PARAM_VALUE_YES}, null, null, null, limitFormat);
    }

    private static Cursor getAllLapCursor(long rideId) {
        return self.mDb.query(Lap.NAME, null, Lap.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(rideId)}, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Cursor getRouteCursor(long routeId) {
        return self.mDb.query(Route.NAME, ROUTE_FIELDS, "_id = ?", new String[]{String.valueOf(routeId)}, null, null, null);
    }

    public static void foreachLap(long rideId, boolean isFinal, SavedWorkout.foreachLapCallback cb) {
        if (cb != null && self.mDb != null && ready) {
            Cursor c = isFinal ? getLapCursor(rideId) : getAllLapCursor(rideId);
            boolean cont = true;
            while (c.moveToNext() && cont && ready) {
                Lap.Saved lap = new Lap.Saved(c);
                cont = cb.onLap(lap);
            }
            c.close();
        }
    }

    public static int countLaps(long rideId, boolean isFinal) {
        int count = 1;
        if (ready) {
            Cursor c = isFinal ? getLapCursor(rideId) : getAllLapCursor(rideId);
            if (c.moveToNext()) {
                count = c.getCount();
            }
            c.close();
        }
        return count;
    }

    static HashMap<Integer, Integer> getRecordResolutions(long rideId, long start, long end) {
        Cursor c;
        HashMap<Integer, Integer> results = new HashMap<>();
        if (ready) {
            if (end == -1) {
                c = self.mDb.query("Record", new String[]{Record.Field.RESOLUTION.name(), "COUNT(" + Record.Field.RESOLUTION.name() + ")"}, Record.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(rideId)}, Record.Field.RESOLUTION.name(), null, null);
            } else {
                c = self.mDb.query("Record", new String[]{Record.Field.RESOLUTION.name(), "COUNT(" + Record.Field.RESOLUTION.name() + ")"}, Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.TIMESTAMP.name() + " >= ? AND " + Record.Field.TIMESTAMP.name() + " < ?", new String[]{String.valueOf(rideId), String.valueOf(start), String.valueOf(end)}, Record.Field.RESOLUTION.name(), null, null);
            }
            while (c.moveToNext()) {
                int res = c.getInt(0);
                int count = c.getInt(1);
                results.put(Integer.valueOf(res), Integer.valueOf(count));
            }
            c.close();
        }
        return results;
    }

    static Cursor getRecordsCursor(long rideId, int resolution) {
        return self.mDb.query("Record", null, Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.RESOLUTION.name() + " = ?", new String[]{String.valueOf(rideId), String.valueOf(resolution)}, null, null, Record.Field.TIMESTAMP.name() + " ASC");
    }

    static Cursor getRecordsCursor(long rideId, long start, long end, int resolution) {
        return self.mDb.query("Record", null, Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.RESOLUTION.name() + " = ? AND " + Record.Field.TIMESTAMP.name() + " >= ? AND " + Record.Field.TIMESTAMP.name() + " < ?", new String[]{String.valueOf(rideId), String.valueOf(resolution), String.valueOf(start), String.valueOf(end)}, null, null, Record.Field.TIMESTAMP.name() + " ASC");
    }

    static Cursor getExtendedRecordsCursor(long rideId, int resolution) {
        String table = "Record LEFT JOIN CorrectedElevation ON Record." + Record.Field.COORD_ID.name() + " = CorrectedElevation." + CorrectedElevation.Field.COORD_ID.name() + " LEFT JOIN " + Coordinate.NAME + " ON Record." + Record.Field.COORD_ID.name() + " = " + Coordinate.NAME + "._id";
        return self.mDb.query(table, EXTENDED_RECORD_FIELDS, Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.RESOLUTION.fqname() + " = ?", new String[]{String.valueOf(rideId), String.valueOf(resolution)}, null, null, Record.Field.TIMESTAMP.fqname() + " ASC");
    }

    static Cursor getMetricsCursor(long rideId, long start, long end, Record.MetricData metric, int resolution) {
        String table;
        String[] select;
        String where;
        String[] whereArgs;
        Log.d(TAG, "Get metrics cursor for metric " + metric + " in ride " + rideId + " at resolution " + resolution);
        if (end != -1) {
            Log.d(TAG, "  between timestamp " + start + " and " + end);
        }
        switch (metric) {
            case ALTITUDE:
            case LOCATION:
                table = "Record INNER JOIN Coord ON Record." + Record.Field.COORD_ID.name() + " = " + Coordinate.NAME + "._id";
                break;
            case CORRECTED_ALTITUDE:
                table = "Record INNER JOIN CorrectedElevation ON Record." + Record.Field.COORD_ID.name() + " = CorrectedElevation." + CorrectedElevation.Field.COORD_ID.name();
                break;
            default:
                table = "Record";
                break;
        }
        switch (metric) {
            case ALTITUDE:
                select = new String[]{Record.Field.TIMESTAMP.name(), "Coord.coordAlt"};
                break;
            case LOCATION:
                select = new String[]{Record.Field.TIMESTAMP.name(), Coordinate.LATITUDE, Coordinate.LONGITUDE};
                break;
            case CORRECTED_ALTITUDE:
                select = new String[]{"Record." + Record.Field.TIMESTAMP.name(), "CorrectedElevation." + CorrectedElevation.Field.CORRECTED_ALTITUDE.name()};
                break;
            case NORMALISED_POWER:
                select = new String[]{Record.Field.TIMESTAMP.name(), Record.Field.POWER.name(), Record.Field.RESOLUTION.name()};
                break;
            case PACE:
                select = new String[]{Record.Field.TIMESTAMP.name(), Record.Field.SPEED.name(), Record.Field.RESOLUTION.name()};
                break;
            case STEP:
                select = new String[]{Record.Field.TIMESTAMP.name(), Record.Field.CADENCE.name(), Record.Field.RESOLUTION.name()};
                break;
            case KICK:
                select = new String[]{Record.Field.TIMESTAMP.name(), Record.Field.POWER.name(), Record.Field.RESOLUTION.name()};
                break;
            default:
                select = new String[]{Record.Field.TIMESTAMP.name(), metric.name(), Record.Field.RESOLUTION.name()};
                break;
        }
        if (end == -1) {
            where = Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.RESOLUTION.fqname() + " = ?";
            whereArgs = new String[]{String.valueOf(rideId), String.valueOf(resolution)};
        } else {
            where = Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.RESOLUTION.fqname() + " = ? AND " + Record.Field.TIMESTAMP.fqname() + " >= ? AND " + Record.Field.TIMESTAMP.fqname() + " < ?";
            whereArgs = new String[]{String.valueOf(rideId), String.valueOf(resolution), String.valueOf(start), String.valueOf(end)};
        }
        return self.mDb.query(table, select, where, whereArgs, null, null, Record.Field.TIMESTAMP.fqname() + " ASC");
    }

    /* JADX INFO: renamed from: com.kopin.solos.storage.SQLHelper$30, reason: invalid class name */
    static /* synthetic */ class AnonymousClass30 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$storage$Shared$ShareType;

        static {
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.ALTITUDE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.LOCATION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.CORRECTED_ALTITUDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.NORMALISED_POWER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.PACE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.STEP.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Record$MetricData[Record.MetricData.KICK.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$com$kopin$solos$storage$Workout$RideMode = new int[Workout.RideMode.values().length];
            try {
                $SwitchMap$com$kopin$solos$storage$Workout$RideMode[Workout.RideMode.TRAINING.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Workout$RideMode[Workout.RideMode.GHOST_RIDE.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Workout$RideMode[Workout.RideMode.ROUTE.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Workout$RideMode[Workout.RideMode.TARGETS.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
            $SwitchMap$com$kopin$solos$storage$Shared$ShareType = new int[Shared.ShareType.values().length];
            try {
                $SwitchMap$com$kopin$solos$storage$Shared$ShareType[Shared.ShareType.RIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Shared$ShareType[Shared.ShareType.RIDE_DATA.ordinal()] = 2;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Shared$ShareType[Shared.ShareType.RUN.ordinal()] = 3;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$kopin$solos$storage$Shared$ShareType[Shared.ShareType.RUN_DATA.ordinal()] = 4;
            } catch (NoSuchFieldError e15) {
            }
            $SwitchMap$com$kopin$solos$common$SportType = new int[SportType.values().length];
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$kopin$solos$common$SportType[SportType.RUN.ordinal()] = 2;
            } catch (NoSuchFieldError e17) {
            }
        }
    }

    static Cursor getAllRecordsCursor(long rideId) {
        return self.mDb.query("Record", null, Record.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(rideId)}, null, null, Record.Field.RESOLUTION.name() + " ASC, " + Record.Field.TIMESTAMP.name() + " ASC");
    }

    static Cursor getCoordsCursor(long rideId, long start, long end) {
        String table = "Record INNER JOIN Coord ON Record." + Record.Field.COORD_ID.name() + " = " + Coordinate.NAME + "._id";
        String[] select = {Record.Field.TIMESTAMP.name(), "Coord._id", "Coord.coordAlt", "Coord.coordLat", "Coord.coordLong"};
        if (end == -1) {
            return self.mDb.query(table, select, Record.Field.RIDE_ID.name() + " = ?", new String[]{String.valueOf(rideId)}, null, null, Record.Field.TIMESTAMP.name() + " ASC");
        }
        return self.mDb.query(table, select, Record.Field.RIDE_ID.name() + " = ? AND " + Record.Field.TIMESTAMP.name() + " >= ? AND " + Record.Field.TIMESTAMP.name() + " < ?", new String[]{String.valueOf(rideId), String.valueOf(start), String.valueOf(end)}, null, null, Record.Field.TIMESTAMP.name() + " ASC");
    }

    public static boolean hasCorrectedElevation(long rideId) {
        Cursor c = getMetricsCursor(rideId, -1L, -1L, Record.MetricData.CORRECTED_ALTITUDE, 0);
        boolean has = c.getCount() > 0;
        c.close();
        return has;
    }

    static Cursor getCoordCursor(long coordId) {
        return self.mDb.query(Coordinate.NAME, new String[]{"_id", "routeId", Coordinate.LATITUDE, Coordinate.LONGITUDE, Coordinate.ALTITUDE}, "_id = ?", new String[]{String.valueOf(coordId)}, null, null, null);
    }

    static Cursor getCoordsCursor(long routeId) {
        return self.mDb.query(Coordinate.NAME, COORD_FIELDS, "routeId = ?", new String[]{String.valueOf(routeId)}, null, null, null);
    }

    public static void foreachCoord(long routeId, foreachCoordCallback cb) {
        if (cb != null && ready) {
            Cursor c = getCoordsCursor(routeId);
            boolean cont = true;
            while (c.moveToNext() && cont && ready) {
                Coordinate coord = new Coordinate(c);
                cont = cb.onCoordinate(coord);
            }
            c.close();
        }
    }

    static boolean hasGhostRides(long rideId) {
        if (!ready) {
            return false;
        }
        Cursor cursor = getGhostRides(rideId);
        boolean zMoveToNext = cursor.moveToNext();
        cursor.close();
        return zMoveToNext;
    }

    private static Cursor getGhostRides(long id) {
        String ghostMode = String.valueOf(Workout.RideMode.GHOST_RIDE.ordinal());
        return self.mDb.query("Ride", RIDE_FIELDS, "rFinal = ? AND rType = ? AND ghostRideId = ?", new String[]{AppEventsConstants.EVENT_PARAM_VALUE_YES, ghostMode, String.valueOf(id)}, null, null, null);
    }

    public static void saveRideCSV(String destination, SavedWorkout ride) {
        self.saveRideToCSV(destination, ride);
    }

    private void saveRideToCSV(final String destination, final SavedWorkout ride) {
        if (isReady()) {
            runOnDbThread("SaveRideToCSV", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.19
                @Override // java.lang.Runnable
                public void run() {
                    FieldHelper.setDefault(Integer.MIN_VALUE);
                    try {
                        Writer out = new BufferedWriter(new FileWriter(destination, false), 1024);
                        HashMap<Integer, Integer> resolutions = SQLHelper.getRecordResolutions(ride.getId(), -1L, -1L);
                        for (Integer res : resolutions.keySet()) {
                            out.write("Resolution: " + res + ", record count: " + resolutions.get(res) + "\n");
                            out.write("timestamp, resolution, heartrate, cadence, distance, power, speed/pace, stride, oxygen, corrected_elevation, altitude, latitude, longitude, norm_power, intensity\n");
                            Cursor recordsCursor = SQLHelper.getExtendedRecordsCursor(ride.getId(), res.intValue());
                            while (recordsCursor.moveToNext()) {
                                StringBuffer buffer = new StringBuffer();
                                Record record = new Record(recordsCursor);
                                buffer.append(record.getTimestamp()).append(", ").append(record.getResolution()).append(", ");
                                buffer.append(record.hasHeartrate() ? record.getHeartrate() + "," : "NO_DATA,");
                                buffer.append(record.hasCadence() ? String.format("%.2f", Double.valueOf(record.getCadence())) + "," : "NO_DATA,");
                                buffer.append(record.hasDistance() ? String.format("%.2f", Double.valueOf(record.getDistance())) + "," : "NO_DATA,");
                                buffer.append(record.hasPower() ? String.format("%.2f", Double.valueOf(record.getPower())) + "," : "NO_DATA,");
                                buffer.append(record.hasSpeed() ? String.format("%.2f", Double.valueOf(record.getSpeed())) + "," : "NO_DATA,");
                                buffer.append(record.hasStride() ? String.format("%.2f", Double.valueOf(record.getStride())) + "," : "NO_DATA,");
                                buffer.append(record.hasOxygen() ? record.getOxygen() + "," : "NO_DATA,");
                                FieldHelper.setDefault(Integer.MIN_VALUE);
                                double elev = CorrectedElevation.Field.CORRECTED_ALTITUDE.getDouble(recordsCursor);
                                buffer.append(elev == -2.147483648E9d ? "NO_DATA" : String.format("%.2f", Double.valueOf(elev))).append(",");
                                FieldHelper.setDefault(0);
                                buffer.append(record.hasAltitude() ? record.getAltitude() + "," : "NO_DATA,");
                                buffer.append(record.hasLocation() ? record.getLatitude() + "," + record.getLongitude() + "," : "NO_DATA,NO_DATA,");
                                buffer.append("\n");
                                out.write(buffer.toString());
                                out.flush();
                            }
                            recordsCursor.close();
                            if (res.intValue() == 0) {
                                StringBuffer buffer2 = new StringBuffer().append("average, -, ").append(ride.getAverageHeartrate()).append(", ").append(ride.getAverageCadence()).append(", ").append("-, ").append(ride.getAveragePower()).append(", ").append(ride.getAverageSpeed()).append(", ").append("-, ").append("-, ").append(ride.getMaxAltitudeDiff()).append(", -, ").append("-, -, ").append(ride.getAverageNormalisedPower()).append(", ").append(ride.getAverageIntensity()).append(", ").append("\n");
                                out.write(buffer2.toString());
                                StringBuffer buffer3 = new StringBuffer().append("maximum, -, ").append(ride.getMaxHeartrate()).append(", ").append(ride.getMaxCadence()).append(", ").append("-, ").append(ride.getMaxPower()).append(", ").append(ride.getMaxSpeed()).append(", ").append("-, ").append("-, ").append(ride.getGainedAltitude()).append(", -, ").append("-, -, ").append(ride.getMaxNormalisedPower()).append(", ").append(ride.getMaxIntensity()).append(", ").append("\n");
                                out.write(buffer3.toString());
                                if (ride instanceof SavedRide) {
                                    out.write("calories, " + ride.getCalories() + ", FTP, " + ((SavedRide) ride).getFunctionalThresholdPower() + ", TSS, " + ((SavedRide) ride).getTrainingStress() + "\n");
                                } else {
                                    out.write("calories, " + ride.getCalories() + "\n");
                                }
                                out.flush();
                            }
                            out.write("\n");
                            out.flush();
                        }
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FieldHelper.setDefault(0);
                }
            });
        }
    }

    public static void addBike(Bike bike) {
        self.doAddBike(bike, false, null);
    }

    public static void addBike(Bike bike, AddListener addListener) {
        self.doAddBike(bike, false, addListener);
    }

    public static void updateBike(Bike bike) {
        self.doAddBike(bike, true, null);
    }

    private void doAddBike(final Bike bike, final boolean update, final AddListener addListener) {
        if (isReady()) {
            runOnDbThread("AddBike", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.20
                @Override // java.lang.Runnable
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put(Bike.Field.NAME.name(), bike.getName());
                    values.put(Bike.Field.TYPE.name(), Integer.valueOf(bike.getType().ordinal()));
                    values.put(Bike.Field.WHEEL_SIZE.name(), Integer.valueOf(bike.getWheelSize()));
                    values.put(Bike.Field.WEIGHT.name(), Double.valueOf(bike.getWeight()));
                    values.put(Bike.Field.ACTIVE.name(), Integer.valueOf(bike.isActive() ? 1 : 0));
                    synchronized (SQLHelper.this.mDb) {
                        if (!update) {
                            long id = SQLHelper.this.mDb.insert(Bike.TABLE, null, values);
                            if (addListener != null) {
                                addListener.onAdded(id);
                            }
                        } else {
                            long id2 = SQLHelper.this.mDb.update(Bike.TABLE, values, "_id = ?", new String[]{String.valueOf(bike.getId())});
                            if (addListener != null) {
                                addListener.onAdded(id2);
                            }
                        }
                    }
                }
            });
        }
    }

    public static void hideBike(Bike bike) {
        bike.setActive(false);
        updateBike(bike);
    }

    public static List<Bike> getBikes() {
        List<Bike> bikes = new ArrayList<>();
        if (ready) {
            Cursor cursor = self.mDb.query(Bike.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                bikes.add(new Bike(cursor));
            }
            cursor.close();
        }
        return bikes;
    }

    public static Bike getActiveBike(long bikeId) {
        Cursor cursor = self.mDb.query(Bike.TABLE, null, "_id = ? AND " + Bike.Field.ACTIVE.name() + " = ?", new String[]{String.valueOf(bikeId), String.valueOf(1)}, null, null, null);
        if (cursor.moveToNext()) {
            Bike bike = new Bike(cursor);
            cursor.close();
            return bike;
        }
        cursor.close();
        return null;
    }

    static List<Bike> getActiveBikes() {
        List<Bike> bikes = new ArrayList<>();
        if (ready) {
            Cursor cursor = self.mDb.query(Bike.TABLE, null, Bike.Field.ACTIVE.name() + " = ?", new String[]{String.valueOf(1)}, null, null, null);
            while (cursor.moveToNext()) {
                bikes.add(new Bike(cursor));
            }
            cursor.close();
        }
        return bikes;
    }

    public static Map<String, Bike> getSyncedBikes(int platformKey, String username) {
        Map<String, Bike> bikeMap = new HashMap<>();
        if (ready) {
            List<Bike> activeBikes = getBikes();
            for (Bike bike : activeBikes) {
                String id = getExternalId(bike.getId(), platformKey, username, Shared.ShareType.BIKE);
                if (id != null && !id.isEmpty()) {
                    bikeMap.put(id, bike);
                }
            }
        }
        return bikeMap;
    }

    public static List<Bike> getUnSyncedBikes(int platformKey, String username, ICancellable cancellable) {
        String id;
        List<Bike> bikes = new ArrayList<>();
        if (isActive(cancellable)) {
            List<Bike> localBikes = getActiveBikes();
            for (Bike bike : localBikes) {
                if (isActive(cancellable) && ((id = getExternalId(bike.getId(), platformKey, username, Shared.ShareType.BIKE)) == null || id.isEmpty())) {
                    bikes.add(bike);
                }
            }
        }
        return bikes;
    }

    public static List<FTP> getFTPs() {
        List<FTP> ftps = new ArrayList<>();
        if (ready) {
            Cursor cursor = self.mDb.query(FTP.NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ftps.add(new FTP(cursor));
            }
            cursor.close();
        }
        return ftps;
    }

    public static List<FTP> getUnSyncedFTPs(int platformKey, String username, ICancellable cancellable) {
        String id;
        List<FTP> ftps = new ArrayList<>();
        if (isActive(cancellable)) {
            List<FTP> localFTPs = getFTPs();
            for (FTP ftp : localFTPs) {
                if (isActive(cancellable) && ((id = getExternalId(ftp.mId, platformKey, username, Shared.ShareType.FTP)) == null || id.isEmpty())) {
                    ftps.add(ftp);
                }
            }
        }
        return ftps;
    }

    public static List<Route.Saved> getRoutes() {
        List<Route.Saved> routes = new ArrayList<>();
        if (ready) {
            Cursor cursor = self.mDb.query(Route.NAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                routes.add(new Route.Saved(cursor));
            }
            cursor.close();
        }
        return routes;
    }

    public static List<Route.Saved> getUnSyncedRoutes(int platformKey, String username) {
        return getUnSyncedRoutes(platformKey, username, null);
    }

    public static List<Route.Saved> getUnSyncedRoutes(int platformKey, String username, ICancellable cancellable) {
        String id;
        List<Route.Saved> routes = new ArrayList<>();
        if (isActive(cancellable)) {
            List<Route.Saved> localRoutes = getRoutes();
            for (Route.Saved r : localRoutes) {
                if (r.isValidRouteName() && isActive(cancellable) && ((id = getExternalId(r.getId(), platformKey, username, Shared.ShareType.ROUTE)) == null || id.isEmpty())) {
                    routes.add(r);
                }
            }
        }
        return routes;
    }

    private static Cursor getSharedIds(String tableName, int platformKey, String userName, Shared.ShareType shareType, boolean synced) {
        String whereClause = synced ? " WHERE " : " WHERE NOT EXISTS ";
        String rawQuery = "SELECT " + tableName + "._id FROM " + tableName + whereClause + "(SELECT 1 FROM " + Shared.NAME + " WHERE " + tableName + "._id = " + Shared.NAME + "." + Shared.ITEM_ID + " AND " + Shared.NAME + "." + Shared.PROVIDER_ID + " = ? AND " + Shared.NAME + "." + Shared.USERNAME + " = ? AND " + Shared.NAME + "." + Shared.TYPE + " = ? AND (" + Shared.NAME + "." + Shared.EXTERNAL_ID + " IS NOT NULL AND " + Shared.NAME + "." + Shared.EXTERNAL_ID + " != ?))";
        String[] selectionArgs = {String.valueOf(platformKey), userName, String.valueOf(shareType.getId()), ""};
        return self.mDb.rawQuery(rawQuery, selectionArgs);
    }

    private static Cursor getUnsynced(String tableName, int platformKey, String userName, Shared.ShareType shareType) {
        return getSharedIds(tableName, platformKey, userName, shareType, false);
    }

    private static Cursor getSynced(String tableName, int platformKey, String userName, Shared.ShareType shareType) {
        return getSharedIds(tableName, platformKey, userName, shareType, true);
    }

    public static Cursor getUnsyncedTrainingIdsCursor(int platformKey, String username) {
        return getSharedTrainingCursor(platformKey, username, false, null);
    }

    public static List<Long> getUnsyncedTrainingIds(int platformKey, String username, ICancellable cancellable) {
        List<Long> trainingIds = new ArrayList<>();
        Cursor cursor = getSharedTrainingCursor(platformKey, username, false, cancellable);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (isActive(cancellable)) {
                    trainingIds.add(Long.valueOf(FieldHelper.getLong(cursor, "_id")));
                }
            }
        }
        return trainingIds;
    }

    public static Cursor getSyncedTrainingIdsCursor(int platformKey, String username) {
        return getSharedTrainingCursor(platformKey, username, true, null);
    }

    private static Cursor getSharedTrainingCursor(int platformKey, String username, boolean synced, ICancellable cancellable) {
        if (isActive(cancellable)) {
            return getSharedIds(Training.NAME, platformKey, username, Shared.ShareType.TRAINING, synced);
        }
        return null;
    }

    public static String lookupSharedId(String id, int platformSharedKey, Shared.ShareType shareType) {
        String shareId = "";
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, new String[]{Shared.ITEM_ID}, "sProId = ? AND sExternalId = ? AND Type = ?", new String[]{String.valueOf(platformSharedKey), id, String.valueOf(shareType.getId())}, null, null, null);
            if (cursor.moveToNext()) {
                shareId = cursor.getString(0);
            }
            cursor.close();
        }
        return shareId;
    }

    public static boolean isTrainingCompleted(String userId, int platformId, String externalId) {
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, null, "sProId = ? AND sExternalId = ? AND Type = ?", new String[]{String.valueOf(platformId), externalId, String.valueOf(Shared.ShareType.TRAINING.getId())}, null, null, null);
            if (cursor.moveToNext()) {
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static long getSharedDate(long id, int platformSharedKey, Shared.ShareType shareType) {
        long date = 0;
        if (ready) {
            Cursor cursor = self.mDb.query(Shared.NAME, new String[]{Shared.UPDATED_TIME}, "sProId = ? AND itemId = ? AND Type = ?", new String[]{String.valueOf(platformSharedKey), String.valueOf(id), String.valueOf(shareType.getId())}, null, null, null);
            if (cursor.moveToNext()) {
                date = cursor.getLong(0);
            }
            cursor.close();
        }
        return date;
    }

    public static void removeShare(String externalId, Shared.ShareType shareType) {
        if (ready) {
            synchronized (self.mDb) {
                self.mDb.delete(Shared.NAME, "sExternalId = ? AND Type = ?", new String[]{externalId, String.valueOf(shareType.getId())});
            }
        }
    }

    public static Cursor getActiveBikesCursor() {
        return self.mDb.query(Bike.TABLE, null, Bike.Field.ACTIVE.name() + " = ?", new String[]{String.valueOf(1)}, null, null, null);
    }

    public static void removeAllBikes() {
        if (ready) {
            synchronized (self.mDb) {
                self.mDb.delete(Bike.TABLE, null, null);
            }
        }
    }

    public static void clear() {
        self.doClear(null);
    }

    public static void clear(DatabaseWorkCallback databaseWorkCallback) {
        self.doClear(databaseWorkCallback);
    }

    private void doClear(final DatabaseWorkCallback databaseWorkCallback) {
        runOnDbThread("ClearDb", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.21
            @Override // java.lang.Runnable
            public void run() {
                try {
                    synchronized (SQLHelper.self.mDb) {
                        boolean unused = SQLHelper.ready = false;
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Ride");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Run");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Lap");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Record");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Route");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Coord");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Shared");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Bike");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Gear");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Rider");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS FTP");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS CorrectedElevation");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS Training");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS TrainingSegment");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS TrainingStep");
                        SQLHelper.self.mDb.execSQL("DROP TABLE IF EXISTS TrainingTarget");
                        SQLHelper.self.onCreate(SQLHelper.self.mDb);
                    }
                } finally {
                    if (databaseWorkCallback != null) {
                        databaseWorkCallback.onComplete();
                    }
                }
            }
        });
    }

    public static Bike getBike(long bikeId) {
        if (ready) {
            Cursor cursor = self.mDb.query(Bike.TABLE, null, "_id = ?", new String[]{String.valueOf(bikeId)}, null, null, null);
            if (cursor.moveToNext()) {
                Bike bike = new Bike(cursor);
                cursor.close();
                return bike;
            }
            cursor.close();
        }
        return null;
    }

    public static void addFTP(double ftp, FTP.ThresholdType type, AddListener addListener) {
        addFTP(ftp, type, System.currentTimeMillis(), addListener);
    }

    public static void addFTP(double ftp, FTP.ThresholdType type, long date, AddListener addListener) {
        self.doAddFTP(ftp, type.name(), date, addListener);
    }

    private void doAddFTP(final double ftp, final String type, final long date, final AddListener addListener) {
        if (ready) {
            runOnDbThread("AddFTP", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.22
                @Override // java.lang.Runnable
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put(FTP.Field.DATE.name(), Long.valueOf(date));
                    values.put(FTP.Field.VALUE.name(), Double.valueOf(ftp));
                    values.put(FTP.Field.THRESHOLD.name(), type);
                    synchronized (SQLHelper.this.mDb) {
                        long id = SQLHelper.this.mDb.insert(FTP.NAME, null, values);
                        if (addListener != null) {
                            addListener.onAdded(id);
                        }
                    }
                }
            });
        }
    }

    public static boolean syncRoute(int platformId, String serverId, String username, String name, long duration, double distance, long date, List<Location> list) {
        if (!isSyncReady(username) || isItemSynced(serverId, platformId, username, Shared.ShareType.ROUTE)) {
            return false;
        }
        long rowId = addRoute();
        updateRoute(rowId, duration, distance, name);
        addLocationList(list, rowId);
        Shared shared = new Shared(rowId, platformId, username, serverId, true, Shared.ShareType.ROUTE, date);
        addShare(shared);
        Log.i(TAG, "sync route added share to " + rowId);
        return true;
    }

    public static boolean canSyncTraining(String userName, String externalId, int platformId) {
        return isSyncReady(userName) && !isItemSynced(externalId, platformId, userName, Shared.ShareType.TRAINING);
    }

    public static boolean syncFTP(final int platformId, final String serverId, final String username, double ftp, String type, final long date) {
        if (!isSyncReady(username) || isItemSynced(serverId, platformId, username, Shared.ShareType.FTP)) {
            return false;
        }
        self.doAddFTP(ftp, type, date, new AddListener() { // from class: com.kopin.solos.storage.SQLHelper.23
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                Shared shared = new Shared(rowId, platformId, username, serverId, true, Shared.ShareType.FTP, date);
                SQLHelper.addShare(shared);
            }
        });
        return true;
    }

    private static boolean isSyncReady(String username) {
        boolean syncReady = (currentUsername == null || currentUsername.isEmpty() || !currentUsername.equalsIgnoreCase(username)) ? false : true;
        if (!syncReady) {
            Log.w(TAG, "sync username null/changed, reject database writes. \ncurrentUser " + currentUsername + ", username " + username);
        }
        return ready && syncReady;
    }

    public static boolean syncBike(final int platformId, final String serverId, final String username, Bike bike, final long date) {
        Log.d(TAG, "syncBike to db, username " + username);
        if (!isSyncReady(username) || isItemSynced(serverId, platformId, username, Shared.ShareType.BIKE)) {
            return false;
        }
        addBike(bike, new AddListener() { // from class: com.kopin.solos.storage.SQLHelper.24
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                Shared shared = new Shared(rowId, platformId, username, serverId, true, Shared.ShareType.BIKE, date);
                SQLHelper.addShare(shared);
            }
        });
        return true;
    }

    public static void setProfile(final int platformId, final String username, final String externalId, Rider rider, final long date, final AddListener readyToShareListener) {
        addRider(rider, username, new AddListener() { // from class: com.kopin.solos.storage.SQLHelper.25
            @Override // com.kopin.solos.storage.SQLHelper.AddListener
            public void onAdded(long rowId) {
                SQLHelper.addShare(new Shared(rowId, platformId, username, externalId, false, Shared.ShareType.PROFILE, date), readyToShareListener);
            }
        });
    }

    public static void addRider(Rider rider, String username, AddListener addListener) {
        self.doAddRider(rider, username, addListener);
    }

    private void doAddRider(final Rider rider, String username, final AddListener addListener) {
        if (isReady() && isSyncReady(username)) {
            runOnDbThread("AddRider", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.26
                @Override // java.lang.Runnable
                public void run() {
                    ContentValues values = new ContentValues();
                    values.put(Rider.Field.AGE.name(), Integer.valueOf(rider.getAge()));
                    values.put(Rider.Field.WEIGHT.name(), Double.valueOf(rider.getWeight()));
                    values.put(Rider.Field.USER_NAME.name(), rider.name);
                    values.put(Rider.Field.GENDER.name(), Integer.valueOf(rider.gender.ordinal()));
                    synchronized (SQLHelper.this.mDb) {
                        long rowId = SQLHelper.this.mDb.insert(Rider.TABLE_NAME, null, values);
                        if (addListener != null) {
                            addListener.onAdded(rowId);
                        }
                    }
                }
            });
        }
    }

    public static Rider getRider(long riderId) {
        if (ready) {
            Cursor cursor = self.mDb.query(Rider.TABLE_NAME, null, "_id = ?", new String[]{String.valueOf(riderId)}, null, null, null);
            if (cursor.moveToNext()) {
                Rider rider = new Rider(cursor);
                cursor.close();
                return rider;
            }
            cursor.close();
        }
        return null;
    }

    public static Rider getLatestRider() {
        if (self == null || !ready) {
            return null;
        }
        Cursor cursor = self.mDb.query(Rider.TABLE_NAME, null, null, null, null, null, "_id DESC", AppEventsConstants.EVENT_PARAM_VALUE_YES);
        if (cursor != null && cursor.moveToNext()) {
            Rider rider = new Rider(cursor);
            cursor.close();
            return rider;
        }
        if (cursor == null) {
            return null;
        }
        cursor.close();
        return null;
    }

    public static FTP getLatestFTP(SportType forSport) {
        return getLatestThreshold(forSport == SportType.RUN ? FTP.ThresholdType.RUN_FTP : FTP.ThresholdType.FTP);
    }

    public static FTP getLatestPeakHR() {
        return getLatestThreshold(FTP.ThresholdType.PEAK_HR);
    }

    private static FTP getLatestThreshold(FTP.ThresholdType type) {
        FTP ftp = null;
        if (ready) {
            Cursor cursor = self.mDb.query(FTP.NAME, null, FTP.Field.THRESHOLD.name() + " LIKE ?", new String[]{type.name()}, null, null, FTP.Field.DATE.name() + " DESC", AppEventsConstants.EVENT_PARAM_VALUE_YES);
            if (cursor != null && cursor.moveToNext()) {
                ftp = new FTP(cursor);
            }
            cursor.close();
        }
        return ftp;
    }

    static boolean removeTraining(final long trainingId) {
        if (getVirtualWorkoutCount(Workout.RideMode.TRAINING, trainingId) > 1) {
            Log.d(TAG, "RemoveTraining: Cannot Remove as there are more than one dependent workouts");
            return false;
        }
        self.runOnDbThread("RemoveTraining", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.27
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.ready) {
                    synchronized (SQLHelper.self.mDb) {
                        String id = String.valueOf(trainingId);
                        String[] args = {id};
                        int rows = SQLHelper.self.mDb.delete(Training.NAME, String.format("_id = ?", id), args);
                        Log.d(SQLHelper.TAG, "RemoveTraining: " + rows);
                        String whereClause = String.format(TrainingSegment.Field.TRAINING_ID.name() + " = ?", id);
                        int rows2 = SQLHelper.self.mDb.delete("TrainingSegment", whereClause, args);
                        Log.d(SQLHelper.TAG, "RemoveTrainingSegment: " + rows2);
                        int rows3 = SQLHelper.self.mDb.delete("TrainingStep", whereClause, args);
                        Log.d(SQLHelper.TAG, "RemoveTrainingSteps: " + rows3);
                        int rows4 = SQLHelper.self.mDb.delete("TrainingTarget", whereClause, args);
                        Log.d(SQLHelper.TAG, "RemoveTrainingTargets: " + rows4);
                    }
                }
            }
        });
        return true;
    }

    static void removeTrainingWorkouts(String[] trainingIds) {
        self.doRemoveTrainingWorkouts(trainingIds);
    }

    private void doRemoveTrainingWorkouts(final String[] trainingIds) {
        self.runOnDbThread("RemoveTrainingWorkouts", new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.28
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.ready) {
                    synchronized (SQLHelper.self.mDb) {
                        Object[] clauseArgs = {TextUtils.join(",", Collections.nCopies(trainingIds.length, "?"))};
                        int rows = SQLHelper.this.mDb.delete(Training.NAME, String.format("_id in (%s)", clauseArgs), trainingIds);
                        Log.d(SQLHelper.TAG, "RemoveTrainingWorkouts: " + rows);
                        String whereClause = String.format(TrainingSegment.Field.TRAINING_ID.name() + " in (%s)", clauseArgs);
                        int rows2 = SQLHelper.this.mDb.delete("TrainingSegment", whereClause, trainingIds);
                        Log.d(SQLHelper.TAG, "RemoveTrainingSegments: " + rows2);
                        int rows3 = SQLHelper.this.mDb.delete("TrainingStep", whereClause, trainingIds);
                        Log.d(SQLHelper.TAG, "RemoveTrainingSteps: " + rows3);
                        int rows4 = SQLHelper.this.mDb.delete("TrainingTarget", whereClause, trainingIds);
                        Log.d(SQLHelper.TAG, "RemoveTrainingTargets: " + rows4);
                    }
                }
            }
        });
    }

    static Cursor getTrainingCursor(long id) {
        return self.mDb.query(Training.NAME, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    static Cursor getTrainingSegmentCursor(long trainingId) {
        return self.mDb.query("TrainingSegment", null, TrainingSegment.Field.TRAINING_ID.name() + " = ?", new String[]{String.valueOf(trainingId)}, null, null, null);
    }

    static Cursor getTrainingStepsCursor(long segmentId) {
        return self.mDb.query("TrainingStep", null, TrainingStep.Field.SEGMENT_ID.name() + " = ?", new String[]{String.valueOf(segmentId)}, null, null, "_id ASC");
    }

    static Cursor getTrainingTargetCursor(long stepId) {
        return self.mDb.query("TrainingTarget", null, TrainingTarget.Field.STEP_ID.name() + " = ?", new String[]{String.valueOf(stepId)}, null, null, null);
    }

    public static long addRow(String table, ContentValues values) {
        return self.doAddRow(table, values);
    }

    private long doAddRow(String table, ContentValues values) {
        long id;
        if (!isReady()) {
            return 0L;
        }
        synchronized (this.mDb) {
            id = this.mDb.insert(table, null, values);
            Log.d(TAG, "Add " + table + ": " + id);
        }
        return id;
    }

    public static void updateRow(String table, long rowId, ContentValues values) {
        self.doUpdateRow(table, rowId, values);
    }

    private void doUpdateRow(final String table, final long rowId, final ContentValues values) {
        runOnDbThread("Update" + table, new Runnable() { // from class: com.kopin.solos.storage.SQLHelper.29
            @Override // java.lang.Runnable
            public void run() {
                if (SQLHelper.isReady()) {
                    synchronized (SQLHelper.this.mDb) {
                        SQLHelper.this.mDb.update(table, values, "_id = ?", new String[]{String.valueOf(rowId)});
                    }
                    Log.d(SQLHelper.TAG, "Update " + table + " Row id: " + rowId);
                }
            }
        });
    }

    private void runOnDbThread(String tag, Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            Log.d(TAG, "Queueing operation: " + tag);
            this.mHandler.post(new SQLOperationRunnable(tag, runnable));
        } else {
            Log.d(TAG, "Running operation: " + tag + "...");
            runnable.run();
            Log.d(TAG, "  completed " + tag);
        }
    }

    private static class SQLOperationRunnable implements Runnable {
        private final Runnable mOperation;
        private final String mTag;

        public SQLOperationRunnable(String name, Runnable op) {
            this.mTag = name;
            this.mOperation = op;
        }

        @Override // java.lang.Runnable
        public void run() {
            Log.d(SQLHelper.TAG, "Running operation: " + this.mTag + "...");
            this.mOperation.run();
            Log.d(SQLHelper.TAG, "  completed " + this.mTag);
        }
    }
}
