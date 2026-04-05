package com.ua.sdk.recorder.save;

import com.ua.sdk.datapoint.DataFrame;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.recorder.RecorderContext;
import com.ua.sdk.recorder.RecorderWorkoutConverter;
import com.ua.sdk.workout.Workout;
import com.ua.sdk.workout.WorkoutNameGenerator;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RecorderWorkoutConverterImpl implements RecorderWorkoutConverter {
    private RecorderContext context;
    private List<DataFrame> recordData;

    public RecorderWorkoutConverterImpl(List<DataFrame> recordData, RecorderContext context) {
        this.recordData = (List) Precondition.isNotNull(recordData, "record data");
        this.context = (RecorderContext) Precondition.isNotNull(context, "recorder context");
    }

    @Override // com.ua.sdk.recorder.RecorderWorkoutConverter
    public Workout generateWorkout(String workoutName) {
        return new RecordWorkoutV7Converter(this.recordData, this.context).buildWorkout(workoutName);
    }

    @Override // com.ua.sdk.recorder.RecorderWorkoutConverter
    public Workout generateWorkout(WorkoutNameGenerator workoutNameGenerator) {
        return new RecordWorkoutV7Converter(this.recordData, this.context).buildWorkout(workoutNameGenerator);
    }
}
