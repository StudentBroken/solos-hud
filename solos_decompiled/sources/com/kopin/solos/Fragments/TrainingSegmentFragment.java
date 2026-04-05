package com.kopin.solos.Fragments;

import android.os.Bundle;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;

/* JADX INFO: loaded from: classes24.dex */
public class TrainingSegmentFragment extends BaseFragment {
    public static final String TRAINING_DB_ID_KEY = "training_id_key";
    public static final String TRAINING_SEGMENT_INDEX_KEY = "training_segment_id_key";
    public static final String WORKOUT_ID_KEY = "workout_id_key";
    private int mCurrentSegmentIdx;
    private SavedWorkout mSavedWorkout;
    private SavedTraining mTraining;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mCurrentSegmentIdx = args.getInt(TRAINING_SEGMENT_INDEX_KEY);
        long trainingId = args.getLong(TRAINING_DB_ID_KEY, -1L);
        this.mTraining = SavedTrainingWorkouts.get(trainingId);
        long workoutId = args.getLong(WORKOUT_ID_KEY, -1L);
        this.mSavedWorkout = SavedRides.getWorkout(this.mTraining.getSport(), workoutId);
    }
}
