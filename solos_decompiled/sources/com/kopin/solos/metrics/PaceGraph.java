package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class PaceGraph extends GhostPage<Double> {
    public PaceGraph(AppService appService) {
        super(appService, TemplateManager.DataType.PACE);
        addPage(MetricType.PACE_GRAPH);
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, true).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_pace));
        setImage(MetricResource.PACE);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_PACE);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        this.mService = service;
        this.mGraph.setAverageProvider(service.getAveragePaceProvider());
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.PACE_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
        super.onServiceDisconnected();
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        return isBound() && value.doubleValue() > 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
        this.mGraph.addValue(Conversion.paceForLocale(updateValue.doubleValue()));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.PACE_GRAPH.getResource())) {
            graphUpdate(MetricType.PACE_GRAPH.getResource(), "graph");
            updateGraphElements(MetricType.PACE_GRAPH.getResource());
            setUnit(Conversion.getUnitOfPaceShort(this.mAppService, true).toUpperCase());
        }
        return PaceUtil.formatPace(Conversion.paceForLocale(updateValue.doubleValue()));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService).toUpperCase());
    }

    @Override // com.kopin.solos.metrics.GhostPage
    protected void updateGraphElements(String pageId) {
        this.mAppService.updateElement(pageId, "max", "content", PaceUtil.formatPace(this.mGraph.getMaxValue()));
        this.mAppService.updateElement(pageId, "med", "content", PaceUtil.formatPace(this.mGraph.getMidValue()));
        this.mAppService.updateElement(pageId, "min", "content", PaceUtil.formatPace(this.mGraph.getMinValue()));
    }
}
