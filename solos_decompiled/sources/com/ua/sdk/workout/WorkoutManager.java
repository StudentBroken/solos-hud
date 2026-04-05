package com.ua.sdk.workout;

import android.net.Uri;
import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.MultipleCreateCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.UploadCallback;
import java.io.IOException;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public interface WorkoutManager {
    void cancelUpload() throws IOException;

    Request createWorkout(Workout workout, CreateCallback<Workout> createCallback);

    Workout createWorkout(Workout workout) throws UaException;

    Request deleteWorkout(WorkoutRef workoutRef, DeleteCallback<WorkoutRef> deleteCallback);

    void deleteWorkout(WorkoutRef workoutRef) throws UaException;

    List<Workout> fetchUnsyncedCreatedWorkouts();

    Request fetchWorkout(WorkoutRef workoutRef, boolean z, FetchCallback<Workout> fetchCallback);

    Workout fetchWorkout(WorkoutRef workoutRef, boolean z) throws UaException;

    EntityList<Workout> fetchWorkoutList(WorkoutListRef workoutListRef) throws UaException;

    Request fetchWorkoutList(WorkoutListRef workoutListRef, FetchCallback<EntityList<Workout>> fetchCallback);

    WorkoutBuilder getWorkoutBuilderCreate();

    WorkoutBuilder getWorkoutBuilderUpdate(Workout workout, boolean z);

    WorkoutNameGenerator getWorkoutNameGenerator();

    Request syncAllUnsyncedWorkouts(MultipleCreateCallback<Workout> multipleCreateCallback) throws UaException;

    Request updateWorkout(Workout workout, SaveCallback<Workout> saveCallback);

    Workout updateWorkout(Workout workout) throws UaException;

    Request uploadImage(Workout workout, Uri uri, UploadCallback<Workout> uploadCallback);

    Request uploadVideo(Workout workout, Uri uri, UploadCallback<Workout> uploadCallback);
}
