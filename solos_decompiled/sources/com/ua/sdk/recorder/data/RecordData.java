package com.ua.sdk.recorder.data;

/* JADX INFO: loaded from: classes65.dex */
public interface RecordData {
    double getBearing();

    int getBearingIndex();

    double getBearingTimestamp();

    double getCadence();

    double getCadenceAvg();

    int getCadenceIndex();

    double getCadenceMax();

    double getCadenceMin();

    double getCadenceTimestamp();

    double getDistance();

    int getDistanceIndex();

    double getDistanceTimestamp();

    double getElapsedTime();

    double getElevation();

    double getElevationAccuracy();

    double getElevationAvg();

    double getElevationGain();

    int getElevationIndex();

    double getElevationMax();

    double getElevationMin();

    double getElevationTimestamp();

    double getHeartRate();

    double getHeartRateAvg();

    int getHeartRateIndex();

    double getHeartRateMax();

    double getHeartRateMin();

    double getHeartRateTimestamp();

    double getIntensity();

    int getIntensityIndex();

    double getIntensityTimestamp();

    double getLocationAccuracy();

    int getLocationIndex();

    double getLocationLatitude();

    double getLocationLongitude();

    double getLocationTimestamp();

    double getMetabolicEnergy();

    int getMetabolicEnergyIndex();

    double getMetabolicEnergyTimestamp();

    double getPower();

    double getPowerAvg();

    int getPowerIndex();

    double getPowerMax();

    double getPowerMin();

    double getPowerTimestamp();

    double getSegmentStartTimestamp();

    double getSpeed();

    double getSpeedAvg();

    int getSpeedIndex();

    double getSpeedMax();

    double getSpeedMin();

    double getSpeedTimestamp();

    double getTorque();

    int getTorqueIndex();

    double getTorqueTimestamp();

    double getWillPower();

    int getWillPowerIndex();

    double getWillPowerTimestamp();

    boolean isSegmentStarted();
}
