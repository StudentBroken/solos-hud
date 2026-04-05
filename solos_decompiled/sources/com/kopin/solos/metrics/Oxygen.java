package com.kopin.solos.metrics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class Oxygen extends GhostPage<Integer> {
    private String labelNormal;

    public Oxygen(AppService appService) {
        super(appService, TemplateManager.DataType.OXYGEN);
        addPage(MetricType.OXYGENATION);
        addPage(MetricType.OXYGENATION_GRAPH);
        setUnit(appService.getString(R.string.oxygen_unit));
        this.labelNormal = appService.getString(R.string.vc_title_oxygen);
        setImage(MetricResource.OXYGENATION);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_OXYGENATION);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Integer value) {
        return isBound() && value.intValue() != Integer.MIN_VALUE;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void prepare() {
        super.prepare();
        this.mGraph.reset();
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.OXYGENATION_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Integer updateValue) {
        this.mGraph.addValue(updateValue.intValue());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Integer updateValue) {
        int lastOxygen = updateValue.intValue();
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        setLabel(this.labelNormal);
        if (pageBoxInfo.isMatch(MetricType.OXYGENATION_GRAPH)) {
            graphUpdate(MetricType.OXYGENATION.getResource(), "graph");
            updateGraphElements(MetricType.OXYGENATION_GRAPH.getResource());
        } else if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.OXYGENATION.getResource())) {
            this.mAppService.updateElement(MetricType.OXYGENATION.getResource(), FirebaseAnalytics.Param.VALUE, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
            this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        } else {
            this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.target, "content", String.valueOf(lastOxygen));
        }
        return String.valueOf(lastOxygen);
    }
}
