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
public class Cadence extends GhostPage<Double> {
    public Cadence(AppService appService) {
        super(appService, TemplateManager.DataType.CADENCE);
        addPage(MetricType.CADENCE);
        addPage(MetricType.CADENCE_GRAPH);
        setImage(MetricResource.CADENCE);
        setLabel(appService.getString(R.string.vc_title_cadence));
        this.mAppService = appService;
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_CADENCE);
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
        setUnit(this.mAppService.getString(LiveRide.isGhostWorkout() ? R.string.cadence_unit_vert : R.string.cadence_unit_caps));
        this.mGraph.reset();
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.CADENCE_GRAPH.getResource());
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
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.CADENCE_GRAPH.getResource())) {
            addGraphBitmap();
        }
        return String.valueOf(lastCadence);
    }

    private void addGraphBitmap() {
        setUnit(this.mAppService.getString(R.string.cadence_unit_vert));
        graphUpdate(MetricType.CADENCE_GRAPH.getResource(), "graph");
        updateGraphElements(MetricType.CADENCE_GRAPH.getResource());
    }
}
