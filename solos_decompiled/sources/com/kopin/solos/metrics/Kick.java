package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.Bar;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class Kick extends GhostPage<Double> {
    private Bar mPowerBar;

    public Kick(AppService appService) {
        super(appService, TemplateManager.DataType.KICK);
        addPage(MetricType.KICK);
        addPage(MetricType.KICK_BAR);
        addPage(MetricType.KICK_GRAPH);
        setUnit(appService.getString(R.string.power_unit_abbrev));
        setLabel(appService.getString(R.string.vc_title_power));
        setImage(MetricResource.KICK);
        this.mPowerBar = new Bar(appService, 9, 300, Bar.DEFAULT_WIDTH, 48, false);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_POWER);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        return value.doubleValue() > 0.0d;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
        this.mPowerBar.addValue(updateValue.intValue());
        this.mGraph.addValue(updateValue.doubleValue());
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.KICK_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.KICK_BAR.getResource())) {
            this.mAppService.addBitmap("power_bar", this.mPowerBar.getBitmap());
        } else if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.KICK_GRAPH.getResource())) {
            graphUpdate(MetricType.KICK_GRAPH.getResource(), "graph");
            updateGraphElements(MetricType.KICK_GRAPH.getResource());
        }
        return String.valueOf(updateValue.intValue());
    }
}
