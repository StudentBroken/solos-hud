package com.nuance.android.vocalizer;

/* JADX INFO: loaded from: classes16.dex */
public class VocalizerVersion {
    public int mBuild;
    public int mBuildDay;
    public String mBuildInfo;
    public int mBuildMonth;
    public int mBuildYear;
    public int mMajor;
    public int mMinor;

    public String toString() {
        return "Vocalizer Expressive v" + this.mMajor + "." + this.mMinor + "." + this.mBuild + " (" + this.mBuildYear + "/" + this.mBuildMonth + "/" + this.mBuildDay + ") " + ((this.mBuildInfo == null || this.mBuildInfo.length() <= 0) ? "" : this.mBuildInfo);
    }
}
