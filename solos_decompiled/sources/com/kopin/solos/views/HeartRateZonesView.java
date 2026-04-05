package com.kopin.solos.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import com.kopin.solos.R;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.util.HeartRateZones;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class HeartRateZonesView extends ZonesView {
    private static int[][] stringIdValues = {new int[]{R.id.txtTitle, R.string.caps_heart_rate_zone}, new int[]{R.id.txtTitleData, R.string.txt_heartzone_graph_bpm}, new int[]{R.id.txtTitleTime, R.string.txt_heartzone_graph_time}, new int[]{R.id.txtTitlePercent, R.string.txt_heartzone_graph_percent}, new int[]{R.id.txtZoneName1, R.string.hr_zone1_name}, new int[]{R.id.txtZoneName2, R.string.hr_zone2_name}, new int[]{R.id.txtZoneName3, R.string.hr_zone3_name}, new int[]{R.id.txtZoneName4, R.string.hr_zone4_name}, new int[]{R.id.txtZoneName5, R.string.hr_zone5_name}, new int[]{R.id.txtZoneName6, R.string.hr_zone6_name}};
    private AsyncTask<IRidePartSaved, Void, Void> mLoader;

    public HeartRateZonesView(Context context) {
        super(context);
        init();
    }

    public HeartRateZonesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartRateZonesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        super.init(stringIdValues);
    }

    @Override // com.kopin.solos.views.ZonesView
    public void setData(final GraphBuilder.GraphValue valuesProvider, IRidePartSaved ride) {
        this.ready = false;
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
            this.mLoader = null;
        }
        this.mLoader = new AsyncTask<IRidePartSaved, Void, Void>() { // from class: com.kopin.solos.views.HeartRateZonesView.1
            private float mPreviousTs = 0.0f;
            private float ts = 0.0f;
            private int hrZone = -1;
            SavedWorkout.foreachRecordCallback callback = new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.views.HeartRateZonesView.1.1
                @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
                public boolean onRecord(Record record) {
                    if (isCancelled()) {
                        return false;
                    }
                    if (valuesProvider.hasData(record)) {
                        AnonymousClass1.this.ts = valuesProvider.getX(record);
                        int bpm = (int) valuesProvider.getY(record);
                        float time = AnonymousClass1.this.ts - AnonymousClass1.this.mPreviousTs;
                        AnonymousClass1.this.mPreviousTs = AnonymousClass1.this.ts;
                        AnonymousClass1.this.hrZone = HeartRateZones.computeHeartRateZone(bpm);
                        if (!HeartRateZonesView.this.zonesData.containsKey(Integer.valueOf(AnonymousClass1.this.hrZone))) {
                            HeartRateZonesView.this.zonesData.put(Integer.valueOf(AnonymousClass1.this.hrZone), new ZoneHelper(time, bpm, bpm));
                        } else {
                            HeartRateZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mTime += time;
                            if (bpm < HeartRateZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMin) {
                                HeartRateZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMin = bpm;
                            } else if (bpm > HeartRateZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMax) {
                                HeartRateZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMax = bpm;
                            }
                        }
                    }
                    return true;
                }
            };

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(IRidePartSaved... params) {
                boolean isLap = params[0] instanceof Lap.Saved;
                processLap(params[0]);
                if (!isCancelled() && HeartRateZonesView.this.zonesData.size() != 0) {
                    float rideTimeDiff = params[0].getDuration() - this.mPreviousTs;
                    if (rideTimeDiff > 0.0f) {
                        HeartRateZonesView.this.zonesData.get(Integer.valueOf(this.hrZone)).mTime += rideTimeDiff;
                    }
                    float totalTime = 0.0f;
                    for (Map.Entry<Integer, ZoneHelper> entry : HeartRateZonesView.this.zonesData.entrySet()) {
                        totalTime += entry.getValue().mTime;
                        entry.getValue().mDurationPercent = (entry.getValue().mTime * 100.0f) / params[0].getDuration();
                    }
                    if (isLap) {
                        for (Map.Entry<Integer, ZoneHelper> entry2 : HeartRateZonesView.this.zonesData.entrySet()) {
                            entry2.getValue().mDurationPercent = (entry2.getValue().mTime * 100.0f) / totalTime;
                        }
                    }
                    HeartRateZonesView.this.mGraph.setData(HeartRateZonesView.this.zonesData);
                    HeartRateZonesView.this.mGraph.getBitmap();
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void result) {
                HeartRateZonesView.this.fillZonesData();
                List<String> zones = new ArrayList<>();
                List<Integer> hrZoneVals = HeartRateZones.getHeartRateZones();
                zones.add(String.valueOf(hrZoneVals.get(0)));
                for (int i = 1; i < hrZoneVals.size(); i++) {
                    zones.add(String.valueOf(hrZoneVals.get(i)));
                    zones.add(String.valueOf(hrZoneVals.get(i).intValue() + 1));
                }
                HeartRateZonesView.this.fillZonesCategories(zones);
                HeartRateZonesView.this.mGraphView.setImageBitmap(HeartRateZonesView.this.mGraph.getBitmap());
                HeartRateZonesView.this.ready = true;
                HeartRateZonesView.this.mParent.onExpandableViewReady();
            }

            private boolean processLap(IRidePartSaved lap) {
                if (isCancelled()) {
                    return false;
                }
                this.mPreviousTs = lap.getStartTime();
                lap.foreachRecord(600, this.callback);
                return true;
            }
        };
        this.mLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ride);
    }
}
