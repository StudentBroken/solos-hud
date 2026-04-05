package com.kopin.solos.storage;

import android.location.Location;
import com.kopin.solos.common.SportType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.util.SplitHelper;
import java.util.List;

/* JADX INFO: loaded from: classes54.dex */
public interface IWorkout extends IRidePartData, SplitHelper.SplitListener {
    public static final long NONE = -1;

    SavedWorkout end(double d, double d2);

    int getCurrentHeartRateZone();

    float getElevation();

    Lap.Split getLastSplit();

    float getOverallClimb();

    double getPowerNormalised();

    long getRouteId();

    int getSplitCount();

    double getSplitDistance();

    long getSplitTime();

    List<Lap.Split> getSplits();

    SportType getSport();

    boolean isStartedAndRunning();

    void onAltitude(float f);

    void onBikePower(double d);

    void onButtonPress(Sensor.ButtonAction buttonAction);

    void onCadence(double d);

    void onHeartRate(int i);

    void onLocation(Location location);

    void onOxygen(int i);

    void onSpeed(double d);
}
