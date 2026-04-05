package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.graphics.ElevationGraphWithBars;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class Elevation extends Template<Float> {
    private ElevationGraphWithBars mElevationSplitGraph;
    private HardwareReceiverService mService;

    public Elevation(AppService appService) {
        super(appService, TemplateManager.DataType.ELEVATION);
        addPage(MetricType.ELEVATION);
        addPage(MetricType.ELEVATION_GRAPH);
        addPage(MetricType.ELEVATION_LAP_GRAPH);
        setUnit(Conversion.getUnitOfLength(this.mAppService).toUpperCase());
        setLabel(appService.getString(R.string.vc_title_elevation));
        this.mAppService = appService;
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_ELEVATION);
        this.mElevationSplitGraph = new ElevationGraphWithBars();
        setImage(MetricResource.ELEVATION);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(Float value) {
        return isBound() && value.floatValue() != -2.14748365E9f;
    }

    @Override // com.kopin.solos.metrics.Template
    public void prepare() {
        super.prepare();
        this.mGraph.reset();
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        String max = Utility.increaseDecimalsIfSameValue(this.mGraph.getMaxValueDouble(), this.mGraph.getMinValueDouble());
        String min = Utility.increaseDecimalsIfSameValue(this.mGraph.getMinValueDouble(), this.mGraph.getMaxValueDouble());
        this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "max", "content", max);
        this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "med", "content", String.valueOf(this.mGraph.getMidValue()));
        this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "min", "content", min);
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mService = service;
        this.mElevationSplitGraph.setService(service);
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Float updateValue) {
        double relativeElevation = updateValue.floatValue() - this.mService.getInitialElevation();
        this.mGraph.addValue(Utility.convertToUserUnits(0, relativeElevation));
        this.mElevationSplitGraph.onDataChanged();
    }

    @Override // com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Float updateValue) {
        if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.ELEVATION_GRAPH.getResource())) {
            setUnit(Conversion.getUnitOfLength(this.mAppService, true).toUpperCase());
            graphUpdate(MetricType.ELEVATION_GRAPH.getResource(), "graph");
            String max = Utility.increaseDecimalsIfSameValue(this.mGraph.getMaxValueDouble(), this.mGraph.getMinValueDouble());
            String min = Utility.increaseDecimalsIfSameValue(this.mGraph.getMinValueDouble(), this.mGraph.getMaxValueDouble());
            this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "max", "content", max);
            this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "med", "content", String.valueOf(this.mGraph.getMidValue()));
            this.mAppService.updateElement(MetricType.ELEVATION_GRAPH.getResource(), "min", "content", min);
        } else if (pageBoxInfo.metric.equalsIgnoreCase(MetricType.ELEVATION_LAP_GRAPH.getResource())) {
            setUnit(Conversion.getUnitOfLength(this.mAppService, true).toUpperCase());
            this.mAppService.addBitmap("graph", this.mElevationSplitGraph.getBitmap());
            String max2 = Utility.increaseDecimalsIfSameValue(this.mElevationSplitGraph.getMaxValueForLocaleDouble(), this.mElevationSplitGraph.getMinValueForLocaleDouble());
            String min2 = Utility.increaseDecimalsIfSameValue(this.mElevationSplitGraph.getMinValueForLocaleDouble(), this.mElevationSplitGraph.getMaxValueForLocaleDouble());
            this.mAppService.updateElement(MetricType.ELEVATION_LAP_GRAPH.getResource(), "max", "content", max2);
            this.mAppService.updateElement(MetricType.ELEVATION_LAP_GRAPH.getResource(), "med", "content", String.valueOf(this.mElevationSplitGraph.getMidValue()));
            this.mAppService.updateElement(MetricType.ELEVATION_LAP_GRAPH.getResource(), "min", "content", min2);
            this.mAppService.updateElement(pageBoxInfo.metric, "lap_count", "content", this.mAppService.getString(R.string.lap_nr, new Object[]{Integer.valueOf(this.mElevationSplitGraph.getSplitCount())}));
        }
        double elevationVal = Conversion.elevationForLocale(updateValue.floatValue());
        return String.valueOf((int) elevationVal);
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfLength(this.mAppService).toUpperCase());
    }
}
