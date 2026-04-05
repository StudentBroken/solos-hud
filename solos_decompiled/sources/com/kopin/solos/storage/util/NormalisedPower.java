package com.kopin.solos.storage.util;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes54.dex */
public class NormalisedPower {
    private static final long RANGE_IN_MILLIS = 30000;
    private ArrayList<PowerData> mPowerDataSet = new ArrayList<>();
    private double mRaisedPowerTotal = 0.0d;
    private int mRaisedPowerCount = 0;
    private double mRollingPowerTotal = 0.0d;
    private double mNormalisedPower = -2.147483648E9d;

    private class PowerData {
        public double power;
        public double rollingAvgPowerRaisedTo4;
        public long timestamp;

        public PowerData(double power, double rollingAvgPowerRaisedTo4, long ts) {
            this.power = power;
            this.rollingAvgPowerRaisedTo4 = rollingAvgPowerRaisedTo4;
            this.timestamp = ts;
        }
    }

    public void reset() {
        this.mPowerDataSet.clear();
        this.mRaisedPowerTotal = 0.0d;
        this.mRaisedPowerCount = 0;
        this.mRollingPowerTotal = 0.0d;
        this.mNormalisedPower = -2.147483648E9d;
    }

    public double calculateNormalisedPower(double power, long timestamp) {
        int removeCount = 0;
        for (PowerData powerData : this.mPowerDataSet) {
            if (timestamp - powerData.timestamp > RANGE_IN_MILLIS) {
                removeCount++;
                this.mRollingPowerTotal -= powerData.power;
            }
        }
        while (removeCount > 0) {
            this.mPowerDataSet.remove(0);
            removeCount--;
        }
        this.mRollingPowerTotal += power;
        double rollingAvgPower = this.mRollingPowerTotal / ((double) (this.mPowerDataSet.size() + 1));
        double rollingAvgPowerRaisedTo4 = Math.pow(rollingAvgPower, 4.0d);
        this.mRaisedPowerTotal += rollingAvgPowerRaisedTo4;
        this.mRaisedPowerCount++;
        if (timestamp > RANGE_IN_MILLIS) {
            double avgRaisedPowers = this.mRaisedPowerTotal / ((double) this.mRaisedPowerCount);
            this.mNormalisedPower = Math.pow(avgRaisedPowers, 0.25d);
        }
        this.mPowerDataSet.add(new PowerData(power, rollingAvgPowerRaisedTo4, timestamp));
        return this.mNormalisedPower;
    }

    public double lastNormalisedPower() {
        return this.mNormalisedPower;
    }
}
