package com.ua.sdk.recorder;

import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutNameGenerator;

/* JADX INFO: loaded from: classes65.dex */
public interface RecorderWorkoutConverter {
    Workout generateWorkout(WorkoutNameGenerator workoutNameGenerator);

    Workout generateWorkout(String str);
}
