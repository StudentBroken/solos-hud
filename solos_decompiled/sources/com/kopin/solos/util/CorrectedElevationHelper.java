package com.kopin.solos.util;

import com.kopin.solos.navigate.helperclasses.ElevationCorrecter;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.CorrectedElevation;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Metrics;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class CorrectedElevationHelper {

    public interface CorrectedElevationCallback {
        void onComplete(boolean z);
    }

    public static void getCorrectedElevation(final SavedWorkout mRide, final CorrectedElevationCallback callback) {
        if (mRide != null && mRide.hasRoute() && !mRide.hasCorrectedElevation()) {
            List<Coordinate> route = mRide.getRouteDetails();
            final List<Lap.Saved> laps = mRide.getLaps();
            ElevationCorrecter.getCorrectedElevation(route, new ElevationCorrecter.CorrectedElevationCallback() { // from class: com.kopin.solos.util.CorrectedElevationHelper.1
                @Override // com.kopin.solos.navigate.helperclasses.ElevationCorrecter.CorrectedElevationCallback
                public void onResults(List<CorrectedElevation> results) {
                    if (results == null || results.isEmpty()) {
                        if (callback != null) {
                            callback.onComplete(false);
                            return;
                        }
                        return;
                    }
                    SQLHelper.addElevation(results, null);
                    Metrics.FloatMetric correctedElevLap = new Metrics.FloatMetric();
                    Metrics.FloatMetric correctedElevation = new Metrics.FloatMetric();
                    for (CorrectedElevation res : results) {
                        float elev = (float) res.getAltitude();
                        long stamp = res.getTimestamp();
                        correctedElevation.addValue(Float.valueOf(elev));
                        if (laps.size() > 0 && stamp > ((Lap.Saved) laps.get(0)).getEndTime()) {
                            Lap.Saved lap = (Lap.Saved) laps.get(0);
                            lap.setMetricStats(Record.MetricData.ALTITUDE, correctedElevLap);
                            SQLHelper.updateLap(lap);
                            laps.remove(0);
                            correctedElevLap = new Metrics.FloatMetric();
                        }
                        correctedElevLap.addValue(Float.valueOf(elev));
                    }
                    if (laps.size() > 0) {
                        Lap.Saved lap2 = (Lap.Saved) laps.get(0);
                        lap2.setMetricStats(Record.MetricData.ALTITUDE, correctedElevLap);
                        SQLHelper.updateLap(lap2);
                    }
                    mRide.setMetricStats(Record.MetricData.ALTITUDE, correctedElevation);
                    SavedRides.updateWorkout(mRide);
                    if (callback != null) {
                        callback.onComplete(true);
                    }
                }
            });
        }
    }
}
