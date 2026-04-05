package com.kopin.solos.storage;

/* JADX INFO: loaded from: classes54.dex */
class AveragedRecord extends Trackable {
    static final int INITIAL_RESOLUTION = 5;
    static final int SECOND_RESOLUTION = 13;
    private int mResolution;
    private long mStartStamp;

    static int NEXT_RESOLUTION(int nMinus1, int nMinus2) {
        return (nMinus1 * 2) + (nMinus1 - nMinus2);
    }

    AveragedRecord(int resolution, long timestamp) {
        this.mResolution = resolution;
        this.mStartStamp = timestamp;
    }

    void setResolution(int res) {
        this.mResolution = res;
    }

    int getResolution() {
        return this.mResolution;
    }

    Record save(long now, Trackable wholeRide) {
        long avgStamp = this.mStartStamp + ((now - this.mStartStamp) / 2);
        Record ret = new Record(avgStamp, this.mResolution);
        ret.setHeartrate(wholeRide.getMaxHeartRate() == getMaxHeartRate() ? getMaxHeartRate() : getAverageHeartrate());
        ret.setCadence(wholeRide.getMaxCadence() == getMaxCadence() ? getMaxCadence() : getAverageCadence());
        ret.setPower(wholeRide.getMaxPower() == getMaxPower() ? getMaxPower() : getAveragePower());
        ret.setSpeed(wholeRide.getMaxSpeed() == getMaxSpeed() ? getMaxSpeed() : getAverageSpeed());
        ret.setStride(wholeRide.getMaxStride() == getMaxStride() ? getMaxStride() : getAverageStride());
        ret.setOxygen(wholeRide.getMinOxygen() == getMinOxygen() ? getMinOxygen() : getAverageOxygen());
        return ret;
    }
}
