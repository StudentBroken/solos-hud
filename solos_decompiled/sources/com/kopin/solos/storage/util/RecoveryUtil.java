package com.kopin.solos.storage.util;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.IncompleteRide;
import com.kopin.solos.storage.IncompleteRun;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Rider;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.SplitHelper;

/* JADX INFO: loaded from: classes54.dex */
public class RecoveryUtil {
    private static RideRecoveryTask rideRecoverTask;

    public interface RideRecoveryComplete {
        void onRecoveryComplete();
    }

    public static boolean recoverRide(SplitHelper.SplitType splitType, long rideId, int activityId, RideRecoveryComplete rideRecoveryComplete) {
        rideRecoverTask = new RideRecoveryTask(splitType, rideId, activityId, rideRecoveryComplete);
        rideRecoverTask.execute(new Void[0]);
        return false;
    }

    public static void cancelRecovery() {
        if (rideRecoverTask != null) {
            rideRecoverTask.cancel(true);
        }
    }

    private static class RideRecoveryTask extends AsyncTask<Void, Void, Void> {
        int mActivityId;
        RideRecoveryComplete mRecoveryComplete;
        long mRideId;
        SplitHelper.SplitType mSplitType;

        RideRecoveryTask(SplitHelper.SplitType splitType, long rideId, int activityId, RideRecoveryComplete rideRecoveryComplete) {
            this.mSplitType = splitType;
            this.mRideId = rideId;
            this.mActivityId = activityId;
            this.mRecoveryComplete = rideRecoveryComplete;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            Log.d("RecoveryTask", "Workout Id: " + this.mRideId);
            SportType sportType = LiveRide.getCurrentSport();
            Cursor cursor = SavedRides.getWorkoutCursor(sportType, this.mRideId);
            if (cursor != null && cursor.moveToNext()) {
                if (sportType == SportType.RIDE) {
                    IncompleteRide ride = new IncompleteRide(this.mSplitType, cursor);
                    ride.recoverMetrics();
                    SavedRide savedRide = ride.end();
                    if (savedRide != null) {
                        if (this.mActivityId >= 0) {
                            savedRide.setActivity(this.mActivityId);
                        }
                        savedRide.setBikeId(Prefs.getChosenBikeId());
                        Rider rider = SQLHelper.getLatestRider();
                        savedRide.setRiderId(rider != null ? rider.getId() : 0L);
                    }
                } else {
                    IncompleteRun run = new IncompleteRun(this.mSplitType, cursor);
                    run.recoverMetrics();
                    SavedRun savedRun = run.end();
                    if (savedRun != null && this.mActivityId >= 0) {
                        savedRun.setActivity(this.mActivityId);
                    }
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            this.mRecoveryComplete.onRecoveryComplete();
        }
    }

    public static long getLastIncompleteWorkoutId() {
        return SavedRides.getLatestIncompleteWorkoutId(LiveRide.getCurrentSport());
    }

    public static void discardIncompleteWorkout(long id) {
        SavedRides.deleteWorkout(LiveRide.getCurrentSport(), id);
        SQLHelper.removeIncompleteWorkouts();
    }

    public static boolean hasInCompleteRide() {
        return (LiveRide.isActiveRide() || getLastIncompleteWorkoutId() == -1) ? false : true;
    }
}
