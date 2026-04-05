package com.kopin.solos.Fragments;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Config;
import com.kopin.solos.storage.Run;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.sync.SyncBarHelper;

/* JADX INFO: loaded from: classes24.dex */
public class RunHistoryFragment extends WorkoutHistoryFragment implements SyncBarHelper.UISyncRefresh {
    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getAllWorkoutHeadersCursor(boolean routePicker, boolean showIncomplete) {
        return SavedRides.getWorkoutHeadersCursor(SportType.RUN, routePicker, showIncomplete);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getImportedWorkoutHeadersCursor(boolean requireRoute) {
        return SavedRides.getImportedRunHeadersCursor(requireRoute, Config.SYNC_PROVIDER.getSharedKey());
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Cursor getWorkoutHeadersCursor(Workout.RideMode mode, boolean requireRoute) {
        return SavedRides.getRunHeadersCursor(mode, requireRoute);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected Workout.Header getWorkout(Cursor cursor) {
        return new Run.Header(cursor);
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected boolean hasGhost(Workout.Header workout) {
        return SavedRides.hasGhostRuns(workout.getId());
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected int getActivityImageRes(Workout.Header workout) {
        int runType = workout.getActivity();
        TypedArray rideTypeIcons = getResources().obtainTypedArray(R.array.sport_types_icons_run);
        int imageRes = rideTypeIcons.getResourceId(runType, R.drawable.road_practice_run);
        rideTypeIcons.recycle();
        return imageRes;
    }

    @Override // com.kopin.solos.Fragments.WorkoutHistoryFragment
    protected void setDynamicEmptyText() {
        ((TextView) getListView().getEmptyView()).setText(getString(R.string.activities_no_runs_live_ride_title));
        getListView().getEmptyView().setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        ((TextView) getListView().getEmptyView()).setGravity(17);
        ((TextView) getActivity().findViewById(R.id.empty_prompt)).setText(getString(R.string.activities_no_rides_live_run_text));
    }
}
