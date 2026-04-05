package com.kopin.solos.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import com.kopin.solos.R;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.metrics.IntensityFactor;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Lap;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.view.ExpandableCardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class IntensityFactorZonesView extends ZonesView {
    private static int[][] stringIdValues = {new int[]{R.id.txtTitle, R.string.caps_intensity_zone}, new int[]{R.id.txtTitleData, R.string.txt_intensity_graph_key}, new int[]{R.id.txtTitleTime, R.string.txt_heartzone_graph_time}, new int[]{R.id.txtTitlePercent, R.string.txt_heartzone_graph_percent}, new int[]{R.id.txtZoneName1, R.string.intensity_zone1_name}, new int[]{R.id.txtZoneName2, R.string.intensity_zone2_name}, new int[]{R.id.txtZoneName3, R.string.intensity_zone3_name}, new int[]{R.id.txtZoneName4, R.string.intensity_zone4_name}, new int[]{R.id.txtZoneName5, R.string.intensity_zone5_name}, new int[]{R.id.txtZoneName6, R.string.intensity_zone6_name}};
    private List<String> categories;
    private double mFtp;
    private AsyncTask<IRidePartSaved, Void, Void> mLoader;
    private IRidePartSaved mRide;
    private GraphBuilder.GraphValue mValuesProvider;

    public IntensityFactorZonesView(Context context) {
        super(context);
        this.categories = new ArrayList();
        init();
    }

    public IntensityFactorZonesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.categories = new ArrayList();
        init();
    }

    public IntensityFactorZonesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.categories = new ArrayList();
        init();
    }

    private void init() {
        super.init(stringIdValues);
        this.categories.clear();
        this.categories.add("0");
        for (double d : IntensityFactor.INTENSITY_FACTOR_CATEGORIES) {
            this.categories.add("" + d);
            this.categories.add("" + d);
        }
    }

    @Override // com.kopin.solos.views.ZonesView
    public void storeData(GraphBuilder.GraphValue valuesProvider, IRidePartSaved ride) {
        this.mValuesProvider = valuesProvider;
        this.mRide = ride;
        this.mFtp = ride instanceof SavedWorkout ? ((SavedWorkout) ride).getFunctionalThresholdPower() : 0.0d;
    }

    @Override // com.kopin.solos.views.ZonesView
    public void setData(GraphBuilder.GraphValue valuesProvider, IRidePartSaved ride) {
        this.ready = false;
        if (this.mLoader != null) {
            this.mLoader.cancel(true);
            this.mLoader = null;
        }
        this.mLoader = new AsyncTask<IRidePartSaved, Void, Void>() { // from class: com.kopin.solos.views.IntensityFactorZonesView.1
            private float mPreviousTs = 0.0f;
            private float ts = 0.0f;
            private int hrZone = -1;
            SavedWorkout.foreachMetricCallback callback = new SavedWorkout.foreachMetricCallback() { // from class: com.kopin.solos.views.IntensityFactorZonesView.1.1
                @Override // com.kopin.solos.storage.SavedWorkout.foreachMetricCallback
                public boolean onMetric(long timestamp, double realValue, int intValue, long dataValidity) {
                    if (isCancelled()) {
                        return false;
                    }
                    if (IntensityFactorZonesView.this.mFtp > 1.0d) {
                        float time = timestamp - AnonymousClass1.this.mPreviousTs;
                        AnonymousClass1.this.mPreviousTs = timestamp;
                        double factor = realValue / IntensityFactorZonesView.this.mFtp;
                        AnonymousClass1.this.hrZone = IntensityFactor.getIntensityBarCategory(factor);
                        if (!IntensityFactorZonesView.this.zonesData.containsKey(Integer.valueOf(AnonymousClass1.this.hrZone))) {
                            IntensityFactorZonesView.this.zonesData.put(Integer.valueOf(AnonymousClass1.this.hrZone), new ZoneHelper(time, factor, factor));
                        } else {
                            IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mTime += time;
                            if (factor < IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMin) {
                                IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMin = factor;
                            } else if (factor > IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMax) {
                                IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(AnonymousClass1.this.hrZone)).mMax = factor;
                            }
                        }
                    }
                    return true;
                }
            };

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(IRidePartSaved... params) {
                if (!isCancelled()) {
                    IRidePartSaved partSaved = params[0];
                    boolean isLap = partSaved instanceof Lap.Saved;
                    this.mPreviousTs = partSaved.getStartTime();
                    partSaved.foreachMetric(Record.MetricData.NORMALISED_POWER, 0, this.callback);
                    if (IntensityFactorZonesView.this.zonesData.size() != 0) {
                        float rideTimeDiff = params[0].getDuration() - this.mPreviousTs;
                        if (rideTimeDiff > 0.0f) {
                            IntensityFactorZonesView.this.zonesData.get(Integer.valueOf(this.hrZone)).mTime += rideTimeDiff;
                        }
                        float totalTime = 0.0f;
                        for (Map.Entry<Integer, ZoneHelper> entry : IntensityFactorZonesView.this.zonesData.entrySet()) {
                            totalTime += entry.getValue().mTime;
                            entry.getValue().mDurationPercent = (entry.getValue().mTime * 100.0f) / params[0].getDuration();
                        }
                        if (isLap) {
                            for (Map.Entry<Integer, ZoneHelper> entry2 : IntensityFactorZonesView.this.zonesData.entrySet()) {
                                entry2.getValue().mDurationPercent = (entry2.getValue().mTime * 100.0f) / totalTime;
                            }
                        }
                        IntensityFactorZonesView.this.mGraph.setData(IntensityFactorZonesView.this.zonesData);
                        IntensityFactorZonesView.this.mGraph.getBitmap();
                    }
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Void result) {
                IntensityFactorZonesView.this.fillZonesData();
                IntensityFactorZonesView.this.fillZonesCategories(IntensityFactorZonesView.this.categories);
                IntensityFactorZonesView.this.mGraphView.setImageBitmap(IntensityFactorZonesView.this.mGraph.getBitmap());
                IntensityFactorZonesView.this.ready = true;
                if (IntensityFactorZonesView.this.mParent != null) {
                    IntensityFactorZonesView.this.mParent.onExpandableViewReady();
                }
            }
        };
        this.mLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ride);
    }

    @Override // com.kopin.solos.views.ZonesView, com.kopin.solos.view.ExpandableCardView.LoadingView
    public void onScreen() {
        setData(this.mValuesProvider, this.mRide);
    }

    @Override // com.kopin.solos.views.ZonesView, com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setParent(ExpandableCardView parent) {
        this.mParent = parent;
    }
}
