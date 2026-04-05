package com.kopin.solos.storage;

import android.content.Context;
import android.database.Cursor;
import android.util.Pair;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.TrainingSegment;
import com.kopin.solos.storage.TrainingStep;
import com.kopin.solos.storage.TrainingTarget;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public class SavedTraining implements ISyncable {
    private boolean mCompleted;
    private String mDescription;
    private double mDistance;
    private long mDuration;
    private String mExternalId;
    private long mId;
    private long mScheduledDate;
    private MetricType mSecondaryMetric;
    private List<Pair<Integer, Integer>> mSegmentMap;
    private List<Segment> mSegments;
    private SportType mSportType;
    private List<Step> mSteps;
    private String mTitle;
    private MetricType mTrainingType;

    public SavedTraining(String title, String description, long duration, double distance, SportType sport, MetricType trainingType, long externalId, long scheduledDate, boolean completed, List<Segment> segments) {
        this.mSegments = new ArrayList();
        this.mSegmentMap = new ArrayList();
        this.mTitle = title;
        this.mDescription = description;
        this.mDuration = duration;
        this.mDistance = distance;
        this.mSportType = sport;
        this.mTrainingType = trainingType;
        this.mSegments = segments;
        this.mExternalId = String.valueOf(externalId);
        this.mScheduledDate = scheduledDate;
        this.mCompleted = completed;
        setStepFlatList();
    }

    public static SavedTraining getNewInstance(long id) {
        Cursor cursor = SQLHelper.getTrainingCursor(id);
        if (cursor.moveToNext()) {
            return new SavedTraining(new Training(cursor));
        }
        return null;
    }

    public SavedTraining(Training training) {
        this.mSegments = new ArrayList();
        this.mSegmentMap = new ArrayList();
        this.mId = training.mId;
        this.mTitle = training.mTitle;
        this.mDescription = training.mDescription;
        this.mDuration = training.mDuration;
        this.mSportType = training.mSportType;
        this.mTrainingType = training.mTrainingType;
        Cursor cursor = SQLHelper.getTrainingSegmentCursor(this.mId);
        while (cursor.moveToNext()) {
            this.mSegments.add(new Segment(cursor));
        }
        cursor.close();
        setStepFlatList();
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public long getDuration() {
        return this.mDuration;
    }

    public double getDistance() {
        return this.mDistance;
    }

    public List<Segment> getSegments() {
        return this.mSegments;
    }

    public List<Step> getStepFlatList() {
        return this.mSteps;
    }

    public List<Pair<Integer, Integer>> getSegmentMap() {
        return this.mSegmentMap;
    }

    private void setStepFlatList() {
        this.mSteps = new ArrayList();
        int flatPosition = 0;
        int segmentIndex = 0;
        int flatSegmentIndex = 0;
        for (Segment segment : this.mSegments) {
            segment.mFlatStepListPosition = flatPosition;
            for (int i = 0; i < segment.getLoopCount(); i++) {
                this.mSegmentMap.add(new Pair<>(Integer.valueOf(segmentIndex), Integer.valueOf(i)));
                flatSegmentIndex++;
                if (this.mSecondaryMetric == null || segment.mSecondaryMetric == null) {
                    Iterator it = segment.mSteps.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            Step step = (Step) it.next();
                            if (step.getTargets().size() > 1) {
                                this.mSecondaryMetric = step.getTargets().get(1).mMetricType;
                                segment.mSecondaryMetric = this.mSecondaryMetric;
                                break;
                            }
                        }
                    }
                }
                this.mSteps.addAll(segment.mSteps);
                flatPosition += segment.mSteps.size();
            }
            segmentIndex++;
        }
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public SportType getSport() {
        return this.mSportType;
    }

    public MetricType getTrainingType() {
        return this.mTrainingType;
    }

    public MetricType getSecondaryMetric() {
        return this.mSecondaryMetric;
    }

    public int getPrimaryMetricImageRes() {
        switch (this.mSportType) {
            case RIDE:
                switch (this.mTrainingType) {
                    case AVERAGE_TARGET_HEARTRATE:
                        return R.drawable.ic_ride_hr;
                    case AVERAGE_TARGET_POWER:
                        return R.drawable.ic_ride_power;
                    case AVERAGE_TARGET_SPEED:
                        return R.drawable.ic_ride_speed;
                    default:
                        return R.drawable.ic_ride_power;
                }
            case RUN:
                switch (this.mTrainingType) {
                    case AVERAGE_TARGET_HEARTRATE:
                        return R.drawable.ic_run_hr;
                    case AVERAGE_TARGET_POWER:
                    case AVERAGE_TARGET_SPEED:
                    default:
                        return R.drawable.ic_run_power;
                    case AVERAGE_TARGET_KICK:
                        return R.drawable.ic_run_power;
                    case AVERAGE_TARGET_PACE:
                        return R.drawable.ic_run_pace;
                }
            default:
                return 0;
        }
    }

    public String getExternalId() {
        return this.mExternalId;
    }

    public long getScheduledDate() {
        return this.mScheduledDate;
    }

    long addToDb() {
        this.mId = new Training(-1L, this.mTitle, this.mDescription, this.mDuration, this.mDistance, this.mSportType, this.mTrainingType).addToBb();
        for (Segment segment : this.mSegments) {
            segment.save(this.mId);
        }
        return this.mId;
    }

    public boolean isCompleted() {
        return this.mCompleted;
    }

    @Override // com.kopin.solos.storage.ISyncable
    public Shared toShared(int providerKey, String userName, String externalId) {
        return new Shared(this.mId, providerKey, userName, externalId, false, Shared.ShareType.TRAINING, System.currentTimeMillis());
    }

    public static class Segment {
        private double mApproxDistance;
        private long mApproxDuration;
        private int mFlatStepListPosition;
        private long mId;
        private int mLoopCount;
        private TrainingStep.Trigger mPrimaryTrigger;
        private MetricType mSecondaryMetric;
        private int mStepCount;
        private List<Step> mSteps;
        private long mTrainingId;
        private TrainingSegment.SegmentType mType;

        public Segment(int stepCount, int loopCount, TrainingSegment.SegmentType type, long approxDuration, double approxDistance, TrainingStep.Trigger primaryTrigger, List<Step> steps) {
            this.mPrimaryTrigger = TrainingStep.Trigger.TIME;
            this.mSteps = new ArrayList();
            this.mStepCount = stepCount;
            this.mLoopCount = loopCount;
            this.mType = type;
            this.mSteps = steps;
            this.mApproxDistance = approxDistance;
            this.mApproxDuration = approxDuration;
            this.mPrimaryTrigger = primaryTrigger;
        }

        Segment(Cursor cursor) {
            this.mPrimaryTrigger = TrainingStep.Trigger.TIME;
            this.mSteps = new ArrayList();
            TrainingSegment segment = new TrainingSegment(cursor);
            this.mId = segment.mId;
            this.mLoopCount = segment.mLoopCount;
            this.mStepCount = segment.mStepCount;
            this.mType = segment.mType;
            Cursor stepsCursor = SQLHelper.getTrainingStepsCursor(this.mId);
            while (stepsCursor.moveToNext()) {
                this.mSteps.add(new Step(stepsCursor));
            }
            stepsCursor.close();
        }

        public String toString(Context context) {
            switch (this.mType) {
                case STEP:
                    Step step = this.mSteps.get(0);
                    return (step.mTitle == null || step.mTitle.isEmpty()) ? step.mIntensityClass.toString() : step.mTitle;
                case REPEAT:
                    return context.getString(R.string.repeat) + " " + this.mLoopCount + " " + context.getString(R.string.times);
                default:
                    return "N/A";
            }
        }

        public List<Step> getSteps() {
            return this.mSteps;
        }

        public int getFlatStepListPosition() {
            return this.mFlatStepListPosition;
        }

        public int getLoopCount() {
            return this.mLoopCount;
        }

        public int getStepCount() {
            return this.mStepCount;
        }

        public TrainingSegment.SegmentType getType() {
            return this.mType;
        }

        void save(long trainingId) {
            TrainingSegment trainingSegment = new TrainingSegment(this.mStepCount, this.mLoopCount, this.mType);
            trainingSegment.mTrainingId = trainingId;
            long segmentId = trainingSegment.addToDb();
            for (Step step : this.mSteps) {
                step.save(trainingId, segmentId);
            }
        }

        public long getApproxDuration() {
            return this.mApproxDuration;
        }

        public double getApproxDistance() {
            return this.mApproxDistance;
        }

        public TrainingStep.Trigger getPrimaryTrigger() {
            return this.mPrimaryTrigger;
        }

        public MetricType getSecondaryMetric() {
            return this.mSecondaryMetric;
        }
    }

    public static class Step {
        private double distance;
        private long mDuration;
        private long mId;
        private TrainingStep.IntensityClass mIntensityClass;
        private String mNotes;
        private long mSegmentId;
        private long mSequence;
        private List<Target> mTargets;
        private String mTitle;
        private long mTrainingId;
        private TrainingStep.Trigger mTrigger;

        public Step(String title, TrainingStep.IntensityClass intensityClass, long sequence, long duration, double distance, TrainingStep.Trigger trigger, String notes, List<Target> targets) {
            this.mTargets = new ArrayList();
            this.mTitle = title;
            this.mDuration = duration;
            this.distance = distance;
            this.mNotes = notes;
            this.mSequence = sequence;
            this.mTrigger = trigger;
            this.mIntensityClass = intensityClass;
            this.mTargets = targets;
        }

        public Step(Cursor cursor) {
            this.mTargets = new ArrayList();
            TrainingStep step = new TrainingStep(cursor);
            this.mId = step.mId;
            this.mSegmentId = step.mSegmentId;
            this.mTrainingId = step.mTrainingId;
            this.mTitle = step.mTitle;
            this.mSequence = step.mSequence;
            this.distance = step.mDistance;
            this.mDuration = step.mDuration;
            this.mTrigger = step.mTrigger;
            this.mNotes = step.mNotes;
            this.mIntensityClass = step.mIntensityClass;
            Cursor targetsCursor = SQLHelper.getTrainingTargetCursor(this.mId);
            while (targetsCursor.moveToNext()) {
                this.mTargets.add(new Target(targetsCursor));
            }
            targetsCursor.close();
        }

        public String toString(Context context) {
            StringBuilder sb = new StringBuilder();
            if (this.mTitle != null && !this.mTitle.isEmpty()) {
                sb.append(this.mTitle);
            } else {
                sb.append(this.mIntensityClass.name());
            }
            sb.append('\n').append("    ");
            if (this.distance > 0.0d) {
                double distanceForLocale = Conversion.distanceForLocale(this.distance);
                String distStr = Utility.trimDecimalPlaces(distanceForLocale, distanceForLocale < 1.0d ? 2 : 1, true);
                sb.append(distStr).append(' ').append(Conversion.getUnitOfDistance(context));
            } else if (this.mDuration > 0) {
                sb.append(Utility.formatTime(this.mDuration * 1000, true));
            }
            sb.append(' ');
            for (Target target : this.mTargets) {
                sb.append('@').append(' ');
                sb.append(target.toString(context));
                sb.append('\n').append("    ");
            }
            return sb.toString().trim();
        }

        public SplitHelper.SplitType getSplitType() {
            switch (this.mTrigger) {
                case TIME:
                    return SplitHelper.SplitType.TIME;
                case DISTANCE:
                    return SplitHelper.SplitType.DISTANCE;
                default:
                    return SplitHelper.SplitType.MANUAL;
            }
        }

        public TrainingStep.Trigger getTrigger() {
            return this.mTrigger;
        }

        public boolean isManualLap() {
            return getSplitType() == SplitHelper.SplitType.MANUAL;
        }

        public long getDuration() {
            return this.mDuration;
        }

        public List<Target> getTargets() {
            return this.mTargets;
        }

        public double getDistance() {
            return this.distance;
        }

        public long getSequence() {
            return this.mSequence;
        }

        public String getTitle() {
            return this.mTitle;
        }

        public String getNotes() {
            return this.mNotes;
        }

        public TrainingStep.IntensityClass getIntensityClass() {
            return this.mIntensityClass;
        }

        void save(long trainingId, long segmentId) {
            TrainingStep step = new TrainingStep(this.mTitle, this.mIntensityClass, this.mSequence, this.mDuration, this.distance, this.mTrigger, this.mNotes);
            step.mTrainingId = trainingId;
            step.mSegmentId = segmentId;
            long stepId = step.addToDb();
            for (Target target : this.mTargets) {
                target.save(trainingId, stepId);
            }
        }
    }

    public static class Target {
        private long mId;
        private double mMaximum;
        private MetricType mMetricType;
        private double mMinimum;
        private long mStepId;
        private double mThreshold;
        private long mTrainingId;
        private TrainingTarget.TargetType mType;

        public Target(MetricType metricType, TrainingTarget.TargetType targetType, double threshold, double minTarget, double maxTarget) {
            this.mMetricType = metricType;
            this.mType = targetType;
            this.mThreshold = threshold;
            this.mMinimum = minTarget;
            this.mMaximum = maxTarget;
        }

        public Target(Cursor cursor) {
            TrainingTarget target = new TrainingTarget(cursor);
            this.mId = target.mId;
            this.mStepId = target.mStepId;
            this.mMetricType = target.mMetricType;
            this.mType = target.mType;
            this.mThreshold = target.mThresholdTarget;
            this.mMaximum = target.mMaxTarget;
            this.mMinimum = target.mMinTarget;
        }

        public String toString(Context context) {
            switch (this.mType) {
                case ABOVE:
                case BELOW:
                    return Utility.formatMetricWithUnit(context, this.mMetricType, this.mThreshold);
                case RANGE:
                    return Utility.formatMetricRange(context, this.mMetricType, this.mMinimum, this.mMaximum);
                default:
                    return null;
            }
        }

        public MetricType getMetric() {
            return this.mMetricType;
        }

        public double getThresholdTarget() {
            return this.mThreshold;
        }

        public double getMinTarget() {
            return this.mMinimum;
        }

        public double getMaxTarget() {
            return this.mMaximum;
        }

        public TrainingTarget.TargetType getType() {
            return this.mType;
        }

        void save(long trainingId, long stepId) {
            TrainingTarget target = new TrainingTarget(this.mMetricType, this.mType, this.mThreshold, this.mMinimum, this.mMaximum);
            target.mTrainingId = trainingId;
            target.mStepId = stepId;
            target.addToDb();
        }
    }
}
