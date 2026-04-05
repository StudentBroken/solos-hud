package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.graphics.GraphWithBars;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.SplitHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.Graph;

/* JADX INFO: loaded from: classes37.dex */
public class Speed extends GhostPage<Double> {
    private GraphWithBars mSplitGraph;

    public Speed(AppService appService) {
        super(appService, TemplateManager.DataType.SPEED);
        addPage(MetricType.SPEED);
        addPage(MetricType.SPEED_GRAPH);
        addPage(MetricType.LAP_GRAPH);
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
        setImage(MetricResource.SPEED);
        setLabel(appService.getString(R.string.vc_title_speed));
        this.mGraph = new Graph(appService, GraphRenderer.GraphDataSet.GraphType.HEADSET_SPEED);
        this.mGraph.setShowAverage(false);
        this.mSplitGraph = new GraphWithBars();
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean before(Double value) {
        if (!isBound()) {
        }
        return false;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        before(value);
        return value.doubleValue() != -2.147483648E9d;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void prepare() {
        super.prepare();
        this.mGraph.reset();
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        super.onServiceConnected(service);
        this.mService = service;
        this.mTimeHelper = service.getTimeHelper();
        this.mSplitGraph.setService(service);
        this.mGraph.setAverageProvider(service.getAverageSpeedProvider());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
        super.onServiceDisconnected();
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
        this.mGraph.addValue(Conversion.speedForLocale(updateValue.doubleValue()));
        this.mSplitGraph.onDataChanged();
    }

    @Override // com.kopin.solos.metrics.Template
    protected void graphAdjust() {
        super.graphAdjust();
        updateGraphElements(MetricType.SPEED_GRAPH.getResource());
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        super.updatePage(pageBoxInfo, updateValue);
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
        double lastSpeed = Utility.roundToOneDecimalPlaces(Conversion.speedForLocale(updateValue.doubleValue()));
        if (pageBoxInfo.isMatch(MetricType.SPEED_GRAPH)) {
            setUnit(Conversion.getUnitOfSpeed(this.mAppService, true).toUpperCase());
            graphUpdate(MetricType.SPEED_GRAPH.getResource(), "graph");
            updateGraphElements(MetricType.SPEED_GRAPH.getResource());
        } else if (pageBoxInfo.isMatch(MetricType.LAP_GRAPH)) {
            setUnit(Conversion.getUnitOfSpeed(this.mAppService, true).toUpperCase());
            this.mAppService.addBitmap("lap_graph", this.mSplitGraph.getBitmap());
            this.mAppService.updateElement(pageBoxInfo.metric, "lap_count", "content", this.mAppService.getString(R.string.lap_nr, new Object[]{Integer.valueOf(this.mSplitGraph.getSplitNr())}));
            this.mAppService.updateElement(pageBoxInfo.metric, "lap_mode", "content", SplitHelper.getSplitMode(this.mAppService) + " " + this.mAppService.getString(R.string.caps_lap));
            String title = SplitHelper.getSplitUnit(this.mAppService) == SplitHelper.SplitUnit.ELEVATION ? this.mAppService.getString(R.string.elevation) : this.mAppService.getString(R.string.speed);
            this.mAppService.updateElement(pageBoxInfo.metric, "lap_graph_title", "content", this.mAppService.getString(R.string.lap_graph_title, new Object[]{title.toUpperCase()}));
        } else {
            if (pageBoxInfo.isMatch(MetricType.AVERAGE_SPEED)) {
                double averageSpeed = this.mService.getAverageSpeedForLocale();
                this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.target, "content", Double.valueOf(averageSpeed));
                return String.format("%.1f", Double.valueOf(averageSpeed));
            }
            this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.target, "content", String.valueOf(lastSpeed));
        }
        return String.valueOf(lastSpeed);
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        setUnit(Conversion.getUnitOfSpeed(this.mAppService, LiveRide.isGhostWorkout()).toUpperCase());
    }
}
