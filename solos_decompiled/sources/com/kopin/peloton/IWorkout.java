package com.kopin.peloton;

import com.kopin.peloton.ride.Lap;
import com.kopin.peloton.ride.RideRecord;
import java.util.List;

/* JADX INFO: loaded from: classes61.dex */
public interface IWorkout {
    List<Lap> getLaps();

    List<RideRecord> getRecords();
}
