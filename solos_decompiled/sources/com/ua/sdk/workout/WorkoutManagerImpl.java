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
import com.ua.sdk.activitystory.AttachmentDest;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.concurrent.MultipleCreateRequest;
import com.ua.sdk.concurrent.UploadRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.MediaService;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.user.UserManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutManagerImpl extends AbstractManager<Workout> implements WorkoutManager {
    private final MediaService<Workout> mediaService;
    private final UserManager userManager;
    private WorkoutDatabase workoutDatabase;

    public WorkoutManagerImpl(UserManager userManager, CacheSettings cacheSettings, Cache memCache, WorkoutDatabase diskCache, EntityService<Workout> entityService, ExecutorService executorService, MediaService<Workout> mediaService) {
        super(cacheSettings, memCache, diskCache, entityService, executorService);
        this.userManager = (UserManager) Precondition.isNotNull(userManager, "userManager");
        this.mediaService = (MediaService) Precondition.isNotNull(mediaService, "imageService");
        this.workoutDatabase = diskCache;
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public WorkoutBuilder getWorkoutBuilderCreate() {
        return new WorkoutBuilderImpl();
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public WorkoutBuilder getWorkoutBuilderUpdate(Workout workout, boolean invalidateTimeSeries) {
        return new WorkoutBuilderImpl(workout, invalidateTimeSeries);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public WorkoutNameGenerator getWorkoutNameGenerator() {
        return new WorkoutNameGeneratorImpl();
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Workout createWorkout(Workout workout) throws UaException {
        return create(workout);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request createWorkout(Workout workout, CreateCallback<Workout> callback) {
        return create(workout, callback);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request uploadImage(final Workout workout, final Uri image, final UploadCallback<Workout> callback) {
        final UploadRequest<Workout> request = new UploadRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.workout.WorkoutManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String href = workout.getRef().getHref();
                    AttachmentDest dest = new AttachmentDest(href, "attachments", 0);
                    WorkoutManagerImpl.this.mediaService.uploadImage(image, dest, callback);
                    request.done(workout, null);
                } catch (UaException error) {
                    request.done(workout, error);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request uploadVideo(final Workout workout, final Uri video, final UploadCallback<Workout> callback) {
        final UploadRequest<Workout> request = new UploadRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.workout.WorkoutManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String href = workout.getRef().getHref();
                    AttachmentDest dest = new AttachmentDest(href, "attachments", 0, WorkoutManagerImpl.this.userManager.getCurrentUserRef().getId());
                    WorkoutManagerImpl.this.mediaService.uploadVideo(video, dest, callback);
                    request.done(workout, null);
                } catch (UaException error) {
                    request.done(workout, error);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request deleteWorkout(WorkoutRef ref, DeleteCallback<WorkoutRef> callback) {
        return delete(ref, callback);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public void deleteWorkout(WorkoutRef ref) throws UaException {
        delete(ref);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Workout fetchWorkout(WorkoutRef ref, boolean setTimeSeriesField) throws UaException {
        return setTimeSeriesField ? fetch(WorkoutRef.getFieldBuilder(ref).setTimeSeriesField(true).build()) : fetch(ref);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request fetchWorkout(WorkoutRef ref, boolean setTimeSeriesField, FetchCallback<Workout> callback) {
        return setTimeSeriesField ? fetch(WorkoutRef.getFieldBuilder(ref).setTimeSeriesField(true).build(), callback) : fetch(ref, callback);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public EntityList<Workout> fetchWorkoutList(WorkoutListRef ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request fetchWorkoutList(WorkoutListRef ref, FetchCallback<EntityList<Workout>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Workout updateWorkout(Workout workout) throws UaException {
        return update(workout);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request updateWorkout(Workout workout, SaveCallback<Workout> callback) {
        return update(workout, callback);
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public void cancelUpload() throws IOException {
        this.mediaService.close();
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public List<Workout> fetchUnsyncedCreatedWorkouts() {
        return this.workoutDatabase.fetchUnsyncedCreatedWorkouts();
    }

    @Override // com.ua.sdk.workout.WorkoutManager
    public Request syncAllUnsyncedWorkouts(MultipleCreateCallback<Workout> callback) throws UaException {
        final MultipleCreateRequest<Workout> request = new MultipleCreateRequest<>(callback);
        final List<Workout> syncedWorkouts = new ArrayList<>();
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.workout.WorkoutManagerImpl.3
            @Override // java.lang.Runnable
            public void run() {
                List<Workout> workoutList = WorkoutManagerImpl.this.fetchUnsyncedCreatedWorkouts();
                try {
                    for (Workout w : workoutList) {
                        WorkoutManagerImpl.this.createWorkout(w);
                        syncedWorkouts.add(w);
                    }
                    request.done(syncedWorkouts, null);
                } catch (UaException e) {
                    request.done(syncedWorkouts, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }
}
