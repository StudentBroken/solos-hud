package com.kopin.solos.metrics;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class AverageSpeed extends GhostPage<Long> {
    public AverageSpeed(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.AVERAGE_SPEED_GRAPH);
        addPage(MetricType.AVERAGE_SPEED);
        setUnit(Conversion.getUnitOfSpeed(appService).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_average_speed_abbrev));
        setImage(MetricResource.SPEED);
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_AVERAGE_SPEED);
        this.mGraph.setShowAverage(true);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return isBound() && this.mService.getAverageSpeedForLocale() > 0.1d;
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.AVERAGE_SPEED_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        this.mGraph.setAverageProvider(service.getAverageSpeedProvider());
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
        this.mGraph.addValue(this.mService.getAverageSpeedForLocale());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        this.mAppService.updateElement(MetricType.GHOST_AVERAGE_SPEED.getResource(), FirebaseAnalytics.Param.VALUE, "color", Integer.valueOf(this.mAppService.getResources().getColor(R.color.single_unit_text)));
        double avgSpeed = this.mService.getAverageSpeedForLocale();
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.AVERAGE_SPEED_GRAPH.getResource())) {
            graphUpdate(MetricType.AVERAGE_SPEED_GRAPH.getResource(), "graph");
            setUnit(Conversion.getUnitOfSpeed(this.mAppService, true));
            updateGraphElements(MetricType.AVERAGE_SPEED_GRAPH.getResource());
        } else if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.AVERAGE_SPEED.getResource())) {
            this.mAppService.updateElement(MetricType.AVERAGE_SPEED.getResource(), FirebaseAnalytics.Param.VALUE, "content", Double.valueOf(avgSpeed));
        }
        return String.format("%2.1f", Double.valueOf(avgSpeed));
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, true));
    }
}
