package com.kopin.peloton.training;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes61.dex */
public class TrainingSegment {
    public int LoopCount;
    public ArrayList<TrainingStep> Steps;
    public String Type;

    public enum SegmentType {
        Step,
        RampUp,
        RampDown,
        Repeat
    }

    public TrainingSegment() {
        this.Steps = new ArrayList<>();
    }

    public TrainingSegment(SegmentType type, int loopCount, ArrayList<TrainingStep> steps) {
        this.Steps = new ArrayList<>();
        this.Type = type.name();
        this.LoopCount = loopCount;
        this.Steps = steps;
    }
}
