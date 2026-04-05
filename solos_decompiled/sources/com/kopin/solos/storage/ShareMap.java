package com.kopin.solos.storage;

import android.database.Cursor;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.ICancellable;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class ShareMap {
    public static List<Long> getUnSyncedRideIds(final int platformKey, String username, final ICancellable cancellable) {
        final List<Long> rides = new ArrayList<>();
        if (SQLHelper.isReady()) {
            SQLHelper.foreachRideCursor(0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.ShareMap.1
                @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
                public boolean onWorkoutCursor(Cursor cursor) {
                    long rideId = SavedRide.getIdFromCursor(cursor);
                    String title = SavedRide.getTitleFromCursor(cursor);
                    Shared rideHeader = SQLHelper.getShare(rideId, platformKey, Shared.ShareType.RIDE);
                    Shared rideData = SQLHelper.getShare(rideId, platformKey, Shared.ShareType.RIDE_DATA);
                    if (title != null && !title.isEmpty() && (rideHeader == null || rideHeader.isUnsynced() || rideData == null || rideData.isUnsynced())) {
                        int pos = cursor.getColumnIndex("_id");
                        long mId = pos != -1 ? cursor.getLong(pos) : 0L;
                        rides.add(Long.valueOf(mId));
                    }
                    return SQLHelper.isActive(cancellable);
                }
            });
            SQLHelper.foreachRunCursor(0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.ShareMap.2
                @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
                public boolean onWorkoutCursor(Cursor cursor) {
                    long runId = SavedRun.getIdFromCursor(cursor);
                    String title = SavedRun.getTitleFromCursor(cursor);
                    Shared runHeader = SQLHelper.getShare(runId, platformKey, Shared.ShareType.RUN);
                    Shared runData = SQLHelper.getShare(runId, platformKey, Shared.ShareType.RUN_DATA);
                    if (title != null && !title.isEmpty() && (runHeader == null || runHeader.isUnsynced() || runData == null || runData.isUnsynced())) {
                        int pos = cursor.getColumnIndex("_id");
                        long mId = pos != -1 ? cursor.getLong(pos) : 0L;
                        rides.add(Long.valueOf(mId));
                    }
                    return SQLHelper.isActive(cancellable);
                }
            });
        }
        return rides;
    }

    public static List<String> getSyncedRideIds(final int platformKey, final String username) {
        final List<String> ids = new ArrayList<>();
        if (SQLHelper.isReady()) {
            SQLHelper.foreachRideCursor(0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.ShareMap.3
                @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
                public boolean onWorkoutCursor(Cursor cursor) {
                    String id = SQLHelper.getExternalId(SavedRide.getIdFromCursor(cursor), platformKey, username, Shared.ShareType.RIDE);
                    if (id != null && !id.isEmpty()) {
                        ids.add(id);
                        return true;
                    }
                    return true;
                }
            });
        }
        return ids;
    }

    public static List<SavedWorkout> getUnSyncedRides(final int platformKey, String username, final ICancellable cancellable) {
        final List<SavedWorkout> workouts = new ArrayList<>();
        if (SQLHelper.isReady()) {
            SQLHelper.foreachRideCursor(0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.ShareMap.4
                @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
                public boolean onWorkoutCursor(Cursor cursor) {
                    Shared rideHeader;
                    Shared rideData;
                    long rideId = SavedRide.getIdFromCursor(cursor);
                    String title = SavedRide.getTitleFromCursor(cursor);
                    if (rideId != -1 && title != null && !title.isEmpty() && ((rideHeader = SQLHelper.getShare(rideId, platformKey, Shared.ShareType.RIDE)) == null || rideHeader.isUnsynced() || (rideData = SQLHelper.getShare(rideId, platformKey, Shared.ShareType.RIDE_DATA)) == null || rideData.isUnsynced())) {
                        workouts.add(new SavedRide(cursor));
                    }
                    return SQLHelper.isActive(cancellable);
                }
            });
            if (SQLHelper.isReady()) {
                SQLHelper.foreachRunCursor(0L, new SQLHelper.foreachWorkoutCursorCallback() { // from class: com.kopin.solos.storage.ShareMap.5
                    @Override // com.kopin.solos.storage.SQLHelper.foreachWorkoutCursorCallback
                    public boolean onWorkoutCursor(Cursor cursor) {
                        Shared header;
                        Shared data;
                        long runId = SavedRun.getIdFromCursor(cursor);
                        String title = SavedRun.getTitleFromCursor(cursor);
                        if (runId != -1 && title != null && !title.isEmpty() && ((header = SQLHelper.getShare(runId, platformKey, Shared.ShareType.RUN)) == null || header.isUnsynced() || (data = SQLHelper.getShare(runId, platformKey, Shared.ShareType.RUN_DATA)) == null || data.isUnsynced())) {
                            workouts.add(new SavedRun(cursor));
                        }
                        return SQLHelper.isActive(cancellable);
                    }
                });
            }
        }
        return workouts;
    }

    public static void addTraining(SavedTraining training, String userId, int platformId) {
        String newExternalId = training.getExternalId() + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + training.getScheduledDate();
        SQLHelper.addShare(new Shared(training.getId(), platformId, userId, newExternalId, false, Shared.ShareType.TRAINING, 0L));
    }

    public static void removeTraining(long trainingId, String userId, int platformId) {
        int count = SQLHelper.getVirtualWorkoutCount(Workout.RideMode.TRAINING, trainingId);
        if (count == 0) {
            Shared shared = new Shared(trainingId, platformId, userId, null, false, Shared.ShareType.TRAINING);
            SQLHelper.removeShare(shared);
        }
    }

    public static boolean isTrainingCompleted(String userId, int platformId, long externalId, long scheduledDate) {
        String newExternalId = externalId + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + scheduledDate;
        return SQLHelper.isTrainingCompleted(userId, platformId, newExternalId);
    }
}
