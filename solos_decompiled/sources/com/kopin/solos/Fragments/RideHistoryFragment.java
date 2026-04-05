package com.kopin.solos.Fragments;

import android.content.res.TypedArray;
import android.database.Cursor;
import com.kopin.solos.R;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Config;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.sync.SyncBarHelper;

/* JADX INFO: loaded from: classes24.dex */
public class RideHistoryFragment extends WorkoutHistoryFragment implements SyncBarHelper.UISyncRefresh {
    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getAllWorkoutHeadersCursor(boolean routePicker, boolean showIncomplete) {
        return SavedRides.getWorkoutHeadersCursor(SportType.RIDE, routePicker, showIncomplete);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getImportedWorkoutHeadersCursor(boolean requireRoute) {
        return SavedRides.getImportedRideHeadersCursor(requireRoute, Config.SYNC_PROVIDER.getSharedKey());
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getWorkoutHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        return SavedRides.getRideHeadersCursor(mode, requireRoute);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Workout.Header getWorkout(Cursor cursor) {
        return new Ride.Header(cursor);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected boolean hasGhost(Workout.Header workout) {
        return SavedRides.hasGhostRides(workout.getId());
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected int getActivityImageRes(Workout.Header workout) {
        int rideType = workout.getActivity();
        TypedArray rideTypeIcons = getResources().obtainTypedArray(R.array.sport_types_icons);
        int imageRes = rideTypeIcons.getResourceId(rideType, R.drawable.road_practice);
        rideTypeIcons.recycle();
        return imageRes;
    }
}
