package com.kopin.solos.storage;

import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.util.NormalisedPower;
import com.kopin.solos.storage.util.SplitHelper;

/* JADX INFO: loaded from: classes54.dex */
public class IncompleteLap extends Lap {
    long mEndTime;
    long mRideId;
    double mSplitDistance;
    long mStartTime;
    NormalisedPower normalisedPowerCalc;

    public IncompleteLap(Lap.Saved saved, SplitHelper.SplitType splitType) {
        super(saved.getId());
        this.normalisedPowerCalc = new NormalisedPower();
        this.mRideId = saved.getRideId();
        this.mStartTime = saved.getStartTime();
        this.mSplitType = splitType;
    }

    public void addDistance(double distance) {
        this.mSplitDistance += distance;
    }

    public double getDistance() {
        return this.mSplitDistance;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public long getSplitTime() {
        return this.mEndTime - this.mStartTime;
    }

    public void end(int calories, long endTime) {
        this.mEndTime = endTime;
        long splitTime = endTime - this.mStartTime;
        Lap.Saved saved = new Lap.Saved(this, this.mStartTime, this.mEndTime, this.mSplitDistance, splitTime, calories, this.mAltitude.getRange().floatValue());
        SQLHelper.updateLap(saved, 0L);
    }

    protected void addNormalisedPower(double power, long duration, double ftp) {
        if (duration > 0) {
            double normalisePower = this.normalisedPowerCalc.calculateNormalisedPower(power, duration);
            if (normalisePower > 0.0d) {
                this.mNormPower.addValue(Double.valueOf(normalisePower));
                double intensityF = normalisePower / ftp;
                this.mIntensity.addValue(Double.valueOf(intensityF));
            }
        }
    }
}
