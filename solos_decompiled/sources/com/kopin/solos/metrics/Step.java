package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class Step extends GhostPage<Double> {
    public Step(AppService appService) {
        super(appService, TemplateManager.DataType.STEP);
        addPage(MetricType.STEP);
        addPage(MetricType.STEP_GRAPH);
        setImage(MetricResource.STEP);
        setLabel(appService.getString(R.string.vc_title_cadence));
        this.mAppService = appService;
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_STEP);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public boolean before(Double value) {
        if (!isBound()) {
        }
        return false;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    protected void prepare() {
        super.prepare();
        setUnit(this.mAppService.getString(LiveRide.isGhostWorkout() ? R.string.unit_cadence_run_short_caps_vert : R.string.unit_cadence_run_short_upper));
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.STEP_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        before(value);
        return value.doubleValue() != -2.147483648E9d;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
        this.mGraph.addValue(updateValue.doubleValue());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        int lastCadence = updateValue.intValue();
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.STEP_GRAPH.getResource())) {
            setUnit(this.mAppService.getString(R.string.unit_cadence_run_short_caps_vert));
            graphUpdate(MetricType.STEP_GRAPH.getResource(), "graph");
            updateGraphElements(MetricType.STEP_GRAPH.getResource());
        }
        return String.valueOf(lastCadence);
    }
}
