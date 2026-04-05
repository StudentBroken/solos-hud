package com.kopin.solos.Fragments;

import android.os.Bundle;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes24.dex */
public class WorkoutFragment extends SafeFragment {
    protected SavedWorkout mWorkout;

    public void setArguments(SportType sportType, long workoutId) {
        Bundle bundle = new Bundle();
        bundle.putLong("ride_id", workoutId);
        bundle.getString(ThemeActivity.EXTRA_WORKOUT_TYPE, sportType.name());
        setArguments(bundle);
    }

    public void setArguments(SavedWorkout workout) {
        setArguments(workout.getSportType(), workout.getId());
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mWorkout = getWorkout(getArguments());
    }

    public static SavedWorkout getWorkout(Bundle arguments) {
        if (arguments == null) {
            return null;
        }
        SportType type = SportType.valueOf(arguments.getString(ThemeActivity.EXTRA_WORKOUT_TYPE, LiveRide.getCurrentSport().name()));
        SavedWorkout savedWorkout = SavedRides.getWorkout(type, arguments.getLong("ride_id", -1L));
        return savedWorkout;
    }
}
