package com.kopin.solos.virtualworkout;

import com.kopin.solos.storage.settings.Prefs;

/* JADX INFO: loaded from: classes37.dex */
public class Target {
    public final boolean hasRange;
    private final Type mType;
    public final double maxTarget;
    public final double minTarget;
    public final double threshold;

    private enum Type {
        THRESHOLD,
        THRESHOLD_ABOVE,
        THRESHOLD_BELOW,
        RANGE
    }

    public static Target createTargetAbove(double target) {
        return new Target(target, Type.THRESHOLD_ABOVE);
    }

    public static Target createTargetBelow(double target) {
        return new Target(target, Type.THRESHOLD_BELOW);
    }

    public Target(double target) {
        this(target, true);
    }

    public Target(double target, boolean applyTolerance) {
        this(target, Type.THRESHOLD, applyTolerance);
    }

    public Target(double target, Type targetType) {
        this(target, targetType, targetType == Type.RANGE);
    }

    public Target(double target, Type targetType, boolean applyTolerance) {
        this.threshold = target;
        this.hasRange = false;
        double tolerance = (applyTolerance ? Prefs.getTargetAveTolerance() : 0.0d) / 100.0d;
        double toleranceLowerWeight = 1.0d - tolerance;
        double toleranceUpperWeight = 1.0d + tolerance;
        this.minTarget = this.threshold * toleranceLowerWeight;
        this.maxTarget = this.threshold * toleranceUpperWeight;
        this.mType = targetType;
    }

    public Target(double target, double min, double max) {
        this.threshold = target;
        this.hasRange = (min == -2.147483648E9d || max == -2.147483648E9d) ? false : true;
        if (this.hasRange) {
            this.minTarget = min;
            this.maxTarget = max;
            this.mType = Type.RANGE;
        } else {
            double tolerance = ((double) Prefs.getTargetAveTolerance()) / 100.0d;
            double toleranceLowerWeight = 1.0d - tolerance;
            double toleranceUpperWeight = 1.0d + tolerance;
            this.minTarget = this.threshold * toleranceLowerWeight;
            this.maxTarget = this.threshold * toleranceUpperWeight;
            this.mType = Type.THRESHOLD;
        }
    }

    public boolean isOutOfRange(double value) {
        switch (this.mType) {
            case THRESHOLD:
            case RANGE:
                if (value < this.minTarget || value > this.maxTarget) {
                }
                break;
            case THRESHOLD_ABOVE:
                break;
            case THRESHOLD_BELOW:
                break;
        }
        return false;
    }

    public boolean isInRange(Number number) {
        return number.doubleValue() >= this.minTarget && number.doubleValue() <= this.maxTarget;
    }

    public boolean isAbove(Number number, boolean invert) {
        return invert ? number.doubleValue() < this.minTarget : number.doubleValue() > this.maxTarget;
    }

    public double getDifference(Number number) {
        if (!this.hasRange) {
            return Math.abs(number.doubleValue() - this.threshold);
        }
        if (number.doubleValue() < this.minTarget) {
            return Math.abs(number.doubleValue() - this.minTarget);
        }
        return Math.abs(number.doubleValue() - this.maxTarget);
    }
}
