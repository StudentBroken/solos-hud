package com.kopin.solos.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.graphics.HeartRateZonesGraph;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.ExpandableCardView;
import java.util.List;
import java.util.TreeMap;

/* JADX INFO: loaded from: classes24.dex */
public class ZonesView extends FrameLayout implements ExpandableCardView.LoadingView {
    protected HeartRateZonesGraph mGraph;
    protected ImageView mGraphView;
    protected ExpandableCardView mParent;
    protected View mView;
    protected boolean ready;
    protected TreeMap<Integer, ZoneHelper> zonesData;

    public ZonesView(Context context) {
        super(context);
        this.zonesData = new TreeMap<>();
        this.ready = false;
    }

    public ZonesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zonesData = new TreeMap<>();
        this.ready = false;
    }

    public ZonesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.zonesData = new TreeMap<>();
        this.ready = false;
    }

    protected void init(int[][] stringIdValues) {
        this.mView = View.inflate(getContext(), R.layout.heartrate_zone_graph_view, this);
        this.mGraphView = (ImageView) this.mView.findViewById(R.id.graph_image);
        for (int[] idResource : stringIdValues) {
            TextView textView = (TextView) this.mView.findViewById(idResource[0]);
            if (textView != null) {
                textView.setText(idResource[1]);
            }
        }
        int width = (int) getResources().getDimension(R.dimen.hrzone_graph_width);
        int height = (int) getResources().getDimension(R.dimen.hrzone_graph_height);
        this.mGraph = new HeartRateZonesGraph(getContext(), width, height);
    }

    protected void fillZonesCategories(List<String> zones) {
        ((TextView) findViewById(R.id.hr_zone1_bpm_min)).setText(zones.get(0).toString());
        ((TextView) findViewById(R.id.hr_zone1_bpm_max)).setText(zones.get(1).toString());
        ((TextView) findViewById(R.id.hr_zone2_bpm_min)).setText(zones.get(2).toString());
        ((TextView) findViewById(R.id.hr_zone2_bpm_max)).setText(zones.get(3).toString());
        ((TextView) findViewById(R.id.hr_zone3_bpm_min)).setText(zones.get(4).toString());
        ((TextView) findViewById(R.id.hr_zone3_bpm_max)).setText(zones.get(5).toString());
        ((TextView) findViewById(R.id.hr_zone4_bpm_min)).setText(zones.get(6).toString());
        ((TextView) findViewById(R.id.hr_zone4_bpm_max)).setText(zones.get(7).toString());
        ((TextView) findViewById(R.id.hr_zone5_bpm_min)).setText(zones.get(8).toString());
        ((TextView) findViewById(R.id.hr_zone5_bpm_max)).setText(zones.get(9).toString());
        ((TextView) findViewById(R.id.hr_zone6_bpm_min)).setText(zones.get(10).toString());
        ((TextView) findViewById(R.id.hr_zone6_bpm_max)).setText("+");
    }

    protected void fillZonesData() {
        if (this.zonesData.containsKey(1)) {
            ((TextView) findViewById(R.id.hr_zone1_time)).setText(Utility.formatTime1((long) this.zonesData.get(1).mTime));
            ((TextView) findViewById(R.id.hr_zone1_procent)).setText(Utility.formatDecimal(Float.valueOf(this.zonesData.get(1).mDurationPercent), 2, true));
        }
        if (this.zonesData.containsKey(2)) {
            ((TextView) findViewById(R.id.hr_zone2_time)).setText(Utility.formatTime1((long) this.zonesData.get(2).mTime));
            ((TextView) findViewById(R.id.hr_zone2_procent)).setText(Utility.formatDecimal(Float.valueOf(this.zonesData.get(2).mDurationPercent), 2, true));
        }
        if (this.zonesData.containsKey(3)) {
            ((TextView) findViewById(R.id.hr_zone3_time)).setText(Utility.formatTime1((long) this.zonesData.get(3).mTime));
            ((TextView) findViewById(R.id.hr_zone3_procent)).setText(Utility.formatDecimal(Float.valueOf(this.zonesData.get(3).mDurationPercent), 2, true));
        }
        if (this.zonesData.containsKey(4)) {
            ((TextView) findViewById(R.id.hr_zone4_time)).setText(Utility.formatTime1((long) this.zonesData.get(4).mTime));
            float percent = this.zonesData.get(4).mDurationPercent;
            ((TextView) findViewById(R.id.hr_zone4_procent)).setText(Utility.formatDecimal(Float.valueOf(percent), percent > 0.0f, 2, 0));
        }
        if (this.zonesData.containsKey(5)) {
            ((TextView) findViewById(R.id.hr_zone5_time)).setText(Utility.formatTime1((long) this.zonesData.get(5).mTime));
            float percent2 = this.zonesData.get(5).mDurationPercent;
            ((TextView) findViewById(R.id.hr_zone5_procent)).setText(Utility.formatDecimal(Float.valueOf(percent2), percent2 > 0.0f, 2, 0));
        }
        if (this.zonesData.containsKey(6)) {
            ((TextView) findViewById(R.id.hr_zone6_time)).setText(Utility.formatTime1((long) this.zonesData.get(6).mTime));
            ((TextView) findViewById(R.id.hr_zone6_procent)).setText(Utility.formatDecimal(Float.valueOf(this.zonesData.get(6).mDurationPercent), 2, true));
        }
        ((TextView) findViewById(R.id.hr_zone6_bpm_max)).setText("+");
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public View getView() {
        return this;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public boolean isReady() {
        return this.ready;
    }

    public void onScreen() {
    }

    public void setParent(ExpandableCardView parent) {
        this.mParent = parent;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setTextColor(int color) {
        Utility.setTextColor((ViewGroup) this, color);
        this.mGraph.setLinesColour(color);
    }

    public void storeData(GraphBuilder.GraphValue valuesProvider, IRidePartSaved ride) {
    }

    public void setData(GraphBuilder.GraphValue valuesProvider, IRidePartSaved ride) {
    }
}
