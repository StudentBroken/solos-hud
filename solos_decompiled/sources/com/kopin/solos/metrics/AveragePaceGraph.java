package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class AveragePaceGraph extends GhostPage<Long> {
    public AveragePaceGraph(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.AVERAGE_PACE_GRAPH);
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, true));
        setLabel(appService.getString(R.string.vc_title_average_pace));
        setImage(MetricResource.PACE);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_AVERAGE_PACE);
        this.mGraph.setShowAverage(true);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return isBound() && LiveRide.getAveragePace() > 0.0d;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.AVERAGE_PACE_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        this.mGraph.setAverageProvider(service.getAveragePaceProvider());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
        this.mGraph.addValue(LiveRide.getAveragePaceLocale());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.AVERAGE_PACE_GRAPH.getResource())) {
            graphUpdate(MetricType.AVERAGE_PACE_GRAPH.getResource(), "graph");
            updateGraphElements(MetricType.AVERAGE_PACE_GRAPH.getResource());
            setUnit(Conversion.getUnitOfPaceShort(this.mAppService, true).toUpperCase());
        }
        return PaceUtil.formatPace(LiveRide.getAveragePaceLocale());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfPaceShort(this.mAppService, true));
    }

    @Override // com.kopin.solos.metrics.GhostPage
    protected void updateGraphElements(String pageId) {
        this.mAppService.updateElement(pageId, "max", "content", PaceUtil.formatPace(this.mGraph.getMaxValue()));
        this.mAppService.updateElement(pageId, "med", "content", PaceUtil.formatPace(this.mGraph.getMidValue()));
        this.mAppService.updateElement(pageId, "min", "content", PaceUtil.formatPace(this.mGraph.getMinValue()));
    }
}
