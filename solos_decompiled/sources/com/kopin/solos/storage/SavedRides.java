package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Workout;
import java.util.HashMap;

/* JADX INFO: loaded from: classes54.dex */
public class SavedRides {
    private static final String TAG = "SavedRides";
    private static HashMap<Long, SavedWorkout> mRideCache = new HashMap<>();

    public static void dumpToCSV(String filename, SavedWorkout workout) {
        SQLHelper.saveRideCSV(filename, workout);
    }

    public static Cursor getWorkoutHeadersCursor(SportType forSport, boolean requireRoute, boolean showIncomplete) {
        switch (forSport) {
            case RIDE:
                return SQLHelper.getRideHeadersCursor(0L, true, requireRoute, showIncomplete);
            case RUN:
                return SQLHelper.getRunHeadersCursor(0L, true, requireRoute, showIncomplete);
            default:
                return null;
        }
    }

    public static Cursor getRunHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        return SQLHelper.getRunHeadersCursor(mode, requireRoute);
    }

    public static Cursor getRideHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        return SQLHelper.getRideHeadersCursor(mode, requireRoute);
    }

    public static Cursor getImportedRunHeadersCursor(boolean requireRoute, int excludePlatformKey) {
        return SQLHelper.getImportedRunHeadersCursor(requireRoute, excludePlatformKey);
    }

    public static Cursor getImportedRideHeadersCursor(boolean requireRoute, int excludePlatformKey) {
        return SQLHelper.getImportedRideHeadersCursor(requireRoute, excludePlatformKey);
    }

    public static Cursor getWorkoutHeaderCursor(Workout.RideMode mode) {
        return SQLHelper.getWorkoutHeaders(mode);
    }

    public static SavedWorkout getWorkout(SportType sportType, long id, boolean includeIncomplete) {
        switch (sportType) {
            case RIDE:
                return getRide(id, includeIncomplete);
            case RUN:
                return getRun(id, includeIncomplete);
            default:
                return null;
        }
    }

    public static SavedWorkout getWorkout(SportType forSport, long id) {
        return getWorkout(forSport, id, false);
    }

    private static SavedRun getRun(long id, boolean allowUnfinished) {
        SavedWorkout workout = mRideCache.get(Long.valueOf(id));
        if (workout != null) {
            if (workout instanceof SavedRun) {
                return (SavedRun) workout;
            }
            return null;
        }
        SavedRun savedRun = SQLHelper.getRun(id, allowUnfinished);
        if (savedRun != null && savedRun.isComplete()) {
            mRideCache.put(Long.valueOf(id), savedRun);
        }
        return savedRun;
    }

    private static SavedRide getRide(long id, boolean allowUnfinished) {
        SavedWorkout workout = mRideCache.get(Long.valueOf(id));
        if (workout != null) {
            if (workout instanceof SavedRide) {
                return (SavedRide) workout;
            }
            return null;
        }
        SavedRide ride = SQLHelper.getRide(id, allowUnfinished);
        if (ride != null && ride.isComplete()) {
            mRideCache.put(Long.valueOf(id), ride);
        }
        return ride;
    }

    public static void prepareDeleteWorkout(SportType sportType, long id) {
        SQLHelper.setTitle(id, "", sportType);
        switch (sportType) {
            case RIDE:
                SQLHelper.setRideFinished(id, false);
                break;
            case RUN:
                SQLHelper.setRunFinished(id, false);
                break;
        }
        mRideCache.remove(Long.valueOf(id));
    }

    public static void removeFromCache(long id) {
        mRideCache.remove(Long.valueOf(id));
    }

    public static void deleteWorkout(SportType sportType, long id) {
        if (sportType == SportType.RIDE) {
            SQLHelper.removeRide(id);
        } else {
            SQLHelper.removeRun(id);
        }
        mRideCache.remove(Long.valueOf(id));
    }

    public static void deleteWorkout(SavedWorkout ride) {
        if (ride.getSportType() == SportType.RIDE) {
            SQLHelper.removeRide(ride);
        } else {
            SQLHelper.removeRun(ride);
        }
        mRideCache.remove(Long.valueOf(ride.getId()));
        if (ride.mMode == Workout.RideMode.TRAINING && ride.getVirtualWorkoutId() != -1) {
            SavedTrainingWorkouts.remove(ride.getVirtualWorkoutId());
        }
    }

    public static void deleteRoute(long routeId) {
    }

    public static void updateWorkout(SavedWorkout workout) {
        if (workout instanceof SavedRide) {
            SQLHelper.updateRide(workout);
        } else if (workout instanceof SavedRun) {
            SQLHelper.updateRun(workout);
        }
    }

    static void saveWorkout(SavedWorkout workout) {
        if (workout instanceof SavedRide) {
            SQLHelper.updateRide(workout);
        } else if (workout instanceof SavedRun) {
            SQLHelper.updateRun(workout);
        }
        if (!workout.hasLocations()) {
            SQLHelper.removeRoute(workout.getRouteId());
        }
        mRideCache.put(Long.valueOf(workout.getId()), workout);
    }

    public static OverallStats getOverallStatsForCurrentSport(long since, int syncPlatformKey, boolean processTSS) {
        return LiveRide.getCurrentSport() == SportType.RUN ? getOverallStatsForRun(since, syncPlatformKey) : getOverallStatsForRide(since, syncPlatformKey, processTSS);
    }

    private static OverallStats getOverallStatsForRun(long since, final int syncPlatformKey) {
        final OverallStats stats = new OverallStats();
        SQLHelper.foreachRunCursor(since, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.SavedRides.1
            @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
            public boolean onWorkoutCursor(Cursor cursor) {
                if (!SQLHelper.wasImportedExceptPlatform(SavedRun.getIdFromCursor(cursor), syncPlatformKey)) {
                    stats.addDistance(SavedRun.getDistanceFromCursor(cursor));
                    stats.addDuration(SavedRun.getDurationFromCursor(cursor));
                    stats.addOverallClimb(SavedRun.getOverallClimbFromCursor(cursor));
                    return true;
                }
                return true;
            }
        });
        return stats;
    }

    private static OverallStats getOverallStatsForRide(long since, final int syncPlatformKey, final boolean processTSS) {
        final OverallStats stats = new OverallStats();
        SQLHelper.foreachRideCursor(since, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.SavedRides.2
            @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
            public boolean onWorkoutCursor(Cursor cursor) {
                if (!SQLHelper.wasImportedExceptPlatform(SavedRide.getIdFromCursor(cursor), syncPlatformKey)) {
                    stats.addDistance(SavedRide.getDistanceFromCursor(cursor));
                    stats.addDuration(SavedRide.getDurationFromCursor(cursor));
                    stats.addOverallClimb(SavedRide.getOverallClimbFromCursor(cursor));
                    if (processTSS) {
                        stats.addTSS(SavedRide.getTSS(cursor));
                        return true;
                    }
                    return true;
                }
                return true;
            }
        });
        return stats;
    }

    public static void clearCache() {
        mRideCache.clear();
    }

    public static Cursor getWorkoutCursor(SportType sportType, long id) {
        if (sportType == SportType.RIDE) {
            return SQLHelper.getRideCursor(id);
        }
        return SQLHelper.getRunCursor(id);
    }

    public static void foreachWorkout(SportType sportType, long timestamp, SQLHelper.foreachWorkoutCallback cb) {
        if (sportType == SportType.RIDE) {
            SQLHelper.foreachRide(timestamp, cb);
        } else {
            SQLHelper.foreachRun(timestamp, cb);
        }
    }

    public static void foreachWorkout(SportType sportType, long timestamp, SQLHelper.foreachWorkoutCursorCallback cb) {
        if (sportType == SportType.RIDE) {
            SQLHelper.foreachRideCursor(timestamp, cb);
        } else {
            SQLHelper.foreachRunCursor(timestamp, cb);
        }
    }

    public static long getLatestIncompleteWorkoutId(SportType sportType) {
        if (sportType == SportType.RIDE) {
            return SQLHelper.getLastIncompleteRideId();
        }
        return SQLHelper.getLastIncompleteRunId();
    }

    public static boolean hasGhostRides(long rideId) {
        return SQLHelper.hasGhostRides(rideId);
    }

    public static boolean hasGhostRuns(long runId) {
        return SQLHelper.hasGhostRuns(runId);
    }

    public static SavedRide getRide(long rideId) {
        return SQLHelper.getRide(rideId, false);
    }

    public static SavedRun getRun(long runId) {
        return SQLHelper.getRun(runId, false);
    }
}
