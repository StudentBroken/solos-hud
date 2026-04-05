package com.kopin.solos.share.peloton;

import com.kopin.peloton.training.TargetValue;
import com.kopin.peloton.training.TrainingWorkout;
import com.kopin.solos.common.SportType;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.Training;
import com.kopin.solos.storage.TrainingSegment;
import com.kopin.solos.storage.TrainingStep;
import com.kopin.solos.storage.TrainingTarget;
import com.kopin.solos.storage.util.MetricType;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes4.dex */
public class TrainingConversion {
    private static TrainingWorkout.Metric dBToPelotonMetric(MetricType metricType) {
        switch (metricType) {
            case AVERAGE_TARGET_SPEED:
                return TrainingWorkout.Metric.Speed;
            case AVERAGE_TARGET_PACE:
                return TrainingWorkout.Metric.Pace;
            case AVERAGE_TARGET_CADENCE:
                return TrainingWorkout.Metric.Cadence;
            case AVERAGE_TARGET_STEP:
                return TrainingWorkout.Metric.Step;
            case AVERAGE_TARGET_HEARTRATE:
                return TrainingWorkout.Metric.HeartRate;
            case AVERAGE_TARGET_POWER:
                return TrainingWorkout.Metric.Power;
            case AVERAGE_TARGET_KICK:
                return TrainingWorkout.Metric.Kick;
            default:
                return TrainingWorkout.Metric.Unknown;
        }
    }

    static TrainingWorkout.Sport dBToPelotonSport(SportType sportType) {
        switch (sportType) {
            case RUN:
                return TrainingWorkout.Sport.Run;
            default:
                return TrainingWorkout.Sport.Ride;
        }
    }

    private static TrainingWorkout.Target.TargetType dBToPelotonTarget(TrainingTarget.TargetType targetType) {
        switch (targetType) {
            case ABOVE:
                return TrainingWorkout.Target.TargetType.AboveThreshold;
            case BELOW:
                return TrainingWorkout.Target.TargetType.AboveThreshold;
            default:
                return TrainingWorkout.Target.TargetType.WithinRange;
        }
    }

    private static TrainingWorkout.Segment.SegmentType dBToPelotonSegmentType(TrainingSegment.SegmentType segmentType) {
        switch (segmentType) {
            case STEP:
                return TrainingWorkout.Segment.SegmentType.Step;
            case REPEAT:
                return TrainingWorkout.Segment.SegmentType.Repeat;
            case RAMP_UP:
                return TrainingWorkout.Segment.SegmentType.RampUp;
            default:
                return TrainingWorkout.Segment.SegmentType.RampDown;
        }
    }

    private static TrainingWorkout.Step.Intensity dBToPelotonIntensity(TrainingStep.IntensityClass intensityClass) {
        switch (intensityClass) {
        }
        return TrainingWorkout.Step.Intensity.Active;
    }

    private static TrainingWorkout.Step.Trigger dBToPelotonTrigger(TrainingStep.Trigger trigger) {
        switch (trigger) {
            case TIME:
                return TrainingWorkout.Step.Trigger.Duration;
            case DISTANCE:
                return TrainingWorkout.Step.Trigger.Distance;
            case MANUAL:
                return TrainingWorkout.Step.Trigger.Indefinite;
            default:
                return TrainingWorkout.Step.Trigger.TargetAchieved;
        }
    }

    private static MetricType pelotonToDbMetric(TrainingWorkout.Metric metric) {
        switch (metric) {
            case Speed:
                return MetricType.AVERAGE_TARGET_SPEED;
            case Pace:
                return MetricType.AVERAGE_TARGET_PACE;
            case Cadence:
                return MetricType.AVERAGE_TARGET_CADENCE;
            case Step:
                return MetricType.AVERAGE_TARGET_STEP;
            case HeartRate:
                return MetricType.AVERAGE_TARGET_HEARTRATE;
            case Power:
                return MetricType.AVERAGE_TARGET_POWER;
            case Kick:
                return MetricType.AVERAGE_TARGET_KICK;
            default:
                return MetricType.NONE;
        }
    }

    private static SportType pelotonToDbSport(TrainingWorkout.Sport sport) {
        switch (sport) {
            case Run:
                return SportType.RUN;
            default:
                return SportType.RIDE;
        }
    }

    private static TrainingSegment.SegmentType pelotonToDbSegmentType(TrainingWorkout.Segment.SegmentType segmentType) {
        switch (segmentType) {
            case RampUp:
                return TrainingSegment.SegmentType.RAMP_UP;
            case RampDown:
                return TrainingSegment.SegmentType.RAMP_DOWN;
            case Repeat:
                return TrainingSegment.SegmentType.REPEAT;
            default:
                return TrainingSegment.SegmentType.STEP;
        }
    }

    private static TrainingStep.IntensityClass pelotonToDbIntensityClass(TrainingWorkout.Step.Intensity intensity) {
        switch (intensity) {
            case Recover:
                return TrainingStep.IntensityClass.RECOVER;
            case WarmUp:
                return TrainingStep.IntensityClass.WARM_UP;
            case CoolDown:
                return TrainingStep.IntensityClass.COOL_DOWN;
            default:
                return TrainingStep.IntensityClass.ACTIVE;
        }
    }

    private static TrainingStep.Trigger pelotonToDbTrigger(TrainingWorkout.Step.Trigger trigger) {
        switch (trigger) {
            case Distance:
                return TrainingStep.Trigger.DISTANCE;
            case Duration:
                return TrainingStep.Trigger.TIME;
            case TargetAchieved:
                return TrainingStep.Trigger.TARGET_ACHIEVED;
            default:
                return TrainingStep.Trigger.MANUAL;
        }
    }

    static TrainingWorkout savedToPelotonTraining(SavedTraining training) {
        ArrayList<TrainingWorkout.Segment> pelotonSegments = new ArrayList<>();
        for (SavedTraining.Segment segment : training.getSegments()) {
            ArrayList<TrainingWorkout.Step> pelotonSteps = new ArrayList<>();
            for (SavedTraining.Step step : segment.getSteps()) {
                ArrayList<TrainingWorkout.Target> pelotonTargets = new ArrayList<>();
                for (SavedTraining.Target target : step.getTargets()) {
                    TargetValue threshold = null;
                    TargetValue min = null;
                    TargetValue max = null;
                    if (target.getType() == TrainingTarget.TargetType.RANGE) {
                        min = new TargetValue(Double.valueOf(target.getMinTarget()));
                        max = new TargetValue(Double.valueOf(target.getMaxTarget()));
                    } else {
                        threshold = new TargetValue(Double.valueOf(target.getThresholdTarget()));
                    }
                    TrainingWorkout.Target pelotonTarget = new TrainingWorkout.Target(dBToPelotonMetric(target.getMetric()), dBToPelotonTarget(target.getType()), threshold, min, max);
                    pelotonTargets.add(pelotonTarget);
                }
                TrainingWorkout.Step pelotonStep = new TrainingWorkout.Step(step.getTitle(), step.getNotes(), step.getDuration() * 1000, step.getDistance(), dBToPelotonIntensity(step.getIntensityClass()), dBToPelotonTrigger(step.getTrigger()), pelotonTargets);
                pelotonSteps.add(pelotonStep);
            }
            TrainingWorkout.Segment pelotonSegment = new TrainingWorkout.Segment(dBToPelotonSegmentType(segment.getType()), segment.getLoopCount(), pelotonSteps);
            pelotonSegments.add(pelotonSegment);
        }
        TrainingWorkout pelotonTraining = new TrainingWorkout(training.getTitle(), training.getDescription(), training.getDistance(), training.getDuration() * 1000, dBToPelotonSport(training.getSport()), dBToPelotonMetric(training.getTrainingType()), pelotonSegments);
        return pelotonTraining;
    }

    static long addPelotonTrainingToDb(TrainingWorkout pelotonTraining) {
        Training training = pelotonToDbTraining(pelotonTraining);
        long trainingId = training.addToBb();
        int position = 0;
        for (TrainingWorkout.Segment pelotonSegment : pelotonTraining.Segments) {
            TrainingSegment segment = pelotonToDbSegment(pelotonSegment);
            segment.setTrainingId(trainingId);
            long segmentId = segment.addToDb();
            for (TrainingWorkout.Step pelotonStep : pelotonSegment.Steps) {
                int position2 = position + 1;
                TrainingStep step = pelotonToDbStep(pelotonStep, position);
                step.setSegmentId(segmentId);
                step.setTrainingId(trainingId);
                long stepId = step.addToDb();
                for (TrainingWorkout.Target pelotonTarget : pelotonStep.Targets) {
                    TrainingTarget target = pelotonToDbTarget(pelotonTarget);
                    target.setStepId(stepId);
                    target.setTrainingId(trainingId);
                    target.addToDb();
                }
                position = position2;
            }
        }
        return trainingId;
    }

    private static Training pelotonToDbTraining(TrainingWorkout pelotonTraining) {
        MetricType primaryMetric = pelotonToDbMetric(pelotonTraining.getTrainingType());
        SportType sportType = pelotonToDbSport(pelotonTraining.getSportType());
        return new Training(-1L, pelotonTraining.Name, pelotonTraining.Description, pelotonTraining.Duration / 1000, pelotonTraining.Distance, sportType, primaryMetric);
    }

    private static TrainingSegment pelotonToDbSegment(TrainingWorkout.Segment pelotonSegment) {
        TrainingSegment.SegmentType segmentType = pelotonToDbSegmentType(pelotonSegment.getType());
        return new TrainingSegment(pelotonSegment.Steps.size(), pelotonSegment.LoopCount, segmentType);
    }

    private static TrainingStep pelotonToDbStep(TrainingWorkout.Step pelotonStep, int position) {
        TrainingStep.IntensityClass intensityClass = pelotonToDbIntensityClass(pelotonStep.getIntensityClass());
        TrainingStep.Trigger trigger = pelotonToDbTrigger(pelotonStep.getTriggerType());
        return new TrainingStep(pelotonStep.Name, intensityClass, position, pelotonStep.Duration / 1000, pelotonStep.Distance, trigger, pelotonStep.Notes);
    }

    private static TrainingTarget pelotonToDbTarget(TrainingWorkout.Target pelotonTarget) {
        TrainingTarget.TargetType targetType;
        double min = -2.147483648E9d;
        double max = -2.147483648E9d;
        double threshold = -2.147483648E9d;
        switch (pelotonTarget.getType()) {
            case WithinRange:
                targetType = TrainingTarget.TargetType.RANGE;
                if (pelotonTarget.Minimum != null) {
                    min = pelotonTarget.Minimum.ValueDouble.doubleValue();
                }
                if (pelotonTarget.Maximum != null) {
                    max = pelotonTarget.Maximum.ValueDouble.doubleValue();
                }
                break;
            case BelowThreshold:
                targetType = TrainingTarget.TargetType.BELOW;
                if (pelotonTarget.Threshold != null) {
                    threshold = pelotonTarget.Threshold.ValueDouble.doubleValue();
                }
                break;
            default:
                targetType = TrainingTarget.TargetType.ABOVE;
                if (pelotonTarget.Threshold != null) {
                    threshold = pelotonTarget.Threshold.ValueDouble.doubleValue();
                }
                break;
        }
        MetricType metricType = pelotonToDbMetric(pelotonTarget.getMetric());
        TrainingTarget target = new TrainingTarget(metricType, targetType, threshold, min, max);
        return target;
    }
}
