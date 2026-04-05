package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.Bar;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class HeartRate extends GhostPage<Integer> {
    private Bar mHeartRateBar;
    private String unit;
    private String unitVertical;

    public HeartRate(AppService appService) {
        super(appService, TemplateManager.DataType.HEART_RATE);
        addPage(MetricType.HEARTRATE);
        addPage(MetricType.HEARTRATE_GRAPH);
        addPage(MetricType.HEARTRATE_ZONES);
        this.unit = appService.getString(R.string.caps_bpm);
        this.unitVertical = appService.getString(R.string.caps_bpm_vertical);
        setUnit(this.unit);
        setImage(MetricResource.HEARTRATE);
        setLabel(appService.getString(R.string.vc_title_heartrate));
        this.mHeartRateBar = new Bar(appService, 6, 6, Bar.DEFAULT_WIDTH, 48, true);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_HEARTRATE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Integer value) {
        return isBound() && value.intValue() > 0;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Integer updateValue) {
        this.mGraph.addValue(updateValue.intValue());
        this.mHeartRateBar.addValue(HeartRateZones.computeHeartRateZone(updateValue.intValue()));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Integer updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        boolean unitVert = LiveRide.isGhostWorkout();
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.HEARTRATE_ZONES.getResource())) {
            unitVert = true;
            updateHeartRateZones();
        } else if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.HEARTRATE_GRAPH.getResource())) {
            graphUpdate(MetricType.HEARTRATE_GRAPH.getResource(), "graph");
            this.mAppService.updateElement(MetricType.HEARTRATE_GRAPH.getResource(), "max", "content", String.valueOf(this.mGraph.getMaxValue()));
            this.mAppService.updateElement(MetricType.HEARTRATE_GRAPH.getResource(), "med", "content", String.valueOf(this.mGraph.getMidValue()));
            this.mAppService.updateElement(MetricType.HEARTRATE_GRAPH.getResource(), "min", "content", String.valueOf(this.mGraph.getMinValue()));
            unitVert = true;
        }
        setUnit(unitVert ? this.unitVertical : this.unit);
        return String.valueOf(updateValue);
    }

    private void updateHeartRateZones() {
        this.mAppService.addBitmap("heart_rate_bar", this.mHeartRateBar.getBitmap());
        this.mAppService.updateElement(MetricType.HEARTRATE_ZONES.getResource(), "heart_rate_zone_name", "content", HeartRateZones.getHeartRateZoneName(LiveRide.getCurrentHeartRateZone()));
    }
}
