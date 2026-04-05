package com.kopin.solos.cabledfu;

/* JADX INFO: loaded from: classes38.dex */
public class DfuRange {
    final int mLower;
    final int mUpper;

    public DfuRange(int lower, int upper) {
        this.mLower = lower;
        this.mUpper = upper;
    }

    public int getLower() {
        return this.mLower;
    }

    public int getUpper() {
        return this.mUpper;
    }

    public int getSize() {
        return (this.mUpper - this.mLower) + 1;
    }

    public boolean contains(int value) {
        boolean gteLower = value >= this.mLower;
        boolean lteUpper = value <= this.mUpper;
        return gteLower && lteUpper;
    }
}
