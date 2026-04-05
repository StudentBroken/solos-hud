package com.kopin.solos.navigate.helperclasses;

import com.kopin.solos.navigate.apimodels.googleelevation.ElevationPoint;
import com.kopin.solos.navigate.communication.tasks.ElevationTask;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.CorrectedElevation;
import com.kopin.solos.storage.Route;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class ElevationCorrecter {
    private static final int MINIMUM_DISTANCE = 0;

    public interface CorrectedElevationCallback {
        void onResults(List<CorrectedElevation> list);
    }

    public static void getCorrectedElevation(Route.Saved route, CorrectedElevationCallback cb) {
        List<Coordinate> routeCoords = route.getRouteCoordinates();
        new ElevationCorrecterThread(routeCoords, cb).start();
    }

    public static void getCorrectedElevation(List<Coordinate> routeCoords, CorrectedElevationCallback cb) {
        new ElevationCorrecterThread(routeCoords, cb).start();
    }

    private static class ElevationCorrecterThread extends Thread implements ElevationTask.ElevationTaskResults {
        private final CorrectedElevationCallback mCb;
        private final List<Coordinate> mCoords;
        private final List<CorrectedElevation> mResults;

        ElevationCorrecterThread(List<Coordinate> coords, CorrectedElevationCallback cb) {
            super("ElevationCorrecter");
            this.mResults = new ArrayList();
            this.mCb = cb;
            this.mCoords = coords;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            List<Coordinate> thisReq = new ArrayList<>();
            for (Coordinate c : this.mCoords) {
                thisReq.add(c);
                if (thisReq.size() >= 200) {
                    requestResults(thisReq);
                    thisReq.clear();
                }
            }
            if (thisReq.size() > 0) {
                requestResults(thisReq);
            }
            this.mCb.onResults(this.mResults);
        }

        private void requestResults(List<Coordinate> coords) {
            List<ElevationPoint> results = ElevationTask.getElevationResults(coords);
            if (results.size() != coords.size()) {
                for (ElevationPoint elev : results) {
                    this.mResults.add(new CorrectedElevation(0L, -1L, elev.elevation));
                }
                return;
            }
            for (int i = 0; i < coords.size(); i++) {
                Coordinate coord = coords.get(i);
                ElevationPoint elev2 = results.get(i);
                this.mResults.add(new CorrectedElevation(coord.getTimestamp(), coord.getId(), elev2.elevation));
            }
        }

        private void requestResultsAndWait(List<Coordinate> coords) {
            synchronized (this.mResults) {
                try {
                    ElevationTask.createTaskFor(coords, this).execute(new Object[0]);
                    this.mResults.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // com.kopin.solos.navigate.communication.tasks.ElevationTask.ElevationTaskResults
        public void onResults(List<ElevationPoint> results) {
            synchronized (this.mResults) {
                for (ElevationPoint elev : results) {
                    this.mResults.add(new CorrectedElevation(0L, -1L, elev.elevation));
                }
                this.mResults.notify();
            }
        }
    }
}
