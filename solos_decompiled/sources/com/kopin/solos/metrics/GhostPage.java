package com.kopin.solos.metrics;

import android.content.Context;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.virtualworkout.GhostWorkout;
import com.kopin.solos.virtualworkout.Target;
import com.kopin.solos.virtualworkout.Targets;
import com.kopin.solos.virtualworkout.VirtualCoach;
import com.kopin.solos.virtualworkout.VirtualWorkout;
import java.lang.Number;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes37.dex */
public abstract class GhostPage<T extends Number> extends Template<T> {
    protected static final String KEY_GHOST_VALUE = "ghostvalue";
    protected static final String KEY_MAX = "max";
    protected static final String KEY_MED = "med";
    protected static final String KEY_MIN = "min";
    protected static final String KEY_NORMAL_VALUE = "value";
    protected static final String TEMPLATE_GHOST = "single_metric_ghost";
    private WeakReference<Context> context;
    protected HardwareReceiverService mService;
    protected TimeHelper mTimeHelper;

    public GhostPage(AppService appService, TemplateManager.DataType dataType) {
        super(appService, dataType);
        this.context = new WeakReference<>(appService.getApplicationContext());
    }

    @Override // com.kopin.solos.metrics.Template
    public void start() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void stop() {
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceConnected(HardwareReceiverService service) {
        this.mService = service;
        this.mTimeHelper = service.getTimeHelper();
    }

    @Override // com.kopin.solos.metrics.Template
    public void onServiceDisconnected() {
        this.mService = null;
        this.mTimeHelper = null;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateTopBar(PageBoxInfo pageBoxInfo) {
        super.updateTopBar(pageBoxInfo);
        updateGhostTopBar(pageBoxInfo.page);
    }

    @Override // com.kopin.solos.metrics.Template
    protected void prepare() {
        super.prepare();
        if (this.mGraph != null) {
            this.mGraph.reset();
        }
    }

    protected void updateGraphElements(String pageId) {
        this.mAppService.updateElement(pageId, KEY_MAX, "content", String.valueOf(this.mGraph.getMaxValue()));
        this.mAppService.updateElement(pageId, KEY_MED, "content", String.valueOf(this.mGraph.getMidValue()));
        this.mAppService.updateElement(pageId, KEY_MIN, "content", String.valueOf(this.mGraph.getMinValue()));
    }

    private void updateGhostTopBar(String page) {
        String distanceLabel;
        String timeLabel;
        double distanceDiff;
        int titleColor;
        long timeDiff;
        String timeLabel2;
        if (isBound()) {
            GhostWorkout ghostWorkout = null;
            if (VirtualCoach.getVirtualWorkout() instanceof GhostWorkout) {
                ghostWorkout = (GhostWorkout) VirtualCoach.getVirtualWorkout();
            }
            int noGhostColour = this.mAppService.getColor(R.color.black);
            if (ghostWorkout == null) {
                this.mAppService.updateElement(page, "ghost_bar", "color", Integer.valueOf(noGhostColour));
                this.mAppService.updateElement(page, "ghost_time", "content", "");
                this.mAppService.updateElement(page, "ghost_distance", "content", "");
                this.mAppService.updateElement(page, "ghost_time_label", "content", "");
                this.mAppService.updateElement(page, "ghost_distance_label", "content", "");
                return;
            }
            double ghostDistance = ghostWorkout.getDistance();
            double currentDistance = this.mService.getLastDistance();
            if (ghostDistance >= currentDistance) {
                distanceLabel = this.mAppService.getResources().getString(R.string.vc_title_ghost_behind);
                timeLabel = distanceLabel;
                distanceDiff = ghostDistance - currentDistance;
                titleColor = this.mAppService.getColor(currentDistance <= ghostDistance ? R.color.behind_color : R.color.ahead_color);
            } else {
                distanceLabel = this.mAppService.getResources().getString(R.string.vc_title_ghost_ahead);
                timeLabel = distanceLabel;
                distanceDiff = currentDistance - ghostDistance;
                titleColor = this.mAppService.getColor(R.color.ahead_color);
            }
            double speed = this.mService.getLastSpeed();
            if (speed <= 0.0d) {
                speed = LiveRide.getAverageSpeed();
            }
            if (speed > 0.0d) {
                timeDiff = (long) (distanceDiff / speed);
            } else {
                timeDiff = this.mTimeHelper.getTime() / 1000;
            }
            if (timeDiff <= 60) {
                timeLabel2 = timeLabel + String.format(": 00:%02d", Long.valueOf(timeDiff));
            } else {
                int min = (int) (timeDiff / 60);
                int sec = (int) (timeDiff % 60);
                timeLabel2 = timeLabel + String.format(": %02d:%02d", Integer.valueOf(min), Integer.valueOf(sec));
            }
            String unit = Conversion.getUnitOfDistance(this.mAppService);
            boolean isMetricSystem = unit.equals(this.mAppService.getString(R.string.unit_distance_metric_short));
            double distance = isMetricSystem ? Utility.metersToKilometers(distanceDiff) : Utility.metersToMiles(distanceDiff);
            String distanceLabel2 = distanceLabel + String.format(": %.2f %s", Double.valueOf(distance), unit);
            boolean hideGhost = ghostWorkout.isLapMode() || !ghostWorkout.hasData();
            AppService appService = this.mAppService;
            if (!hideGhost) {
                noGhostColour = titleColor;
            }
            appService.updateElement(page, "ghost_bar", "color", Integer.valueOf(noGhostColour));
            AppService appService2 = this.mAppService;
            if (hideGhost) {
                timeLabel2 = "";
            }
            appService2.updateElement(page, "ghost_time", "content", timeLabel2);
            AppService appService3 = this.mAppService;
            if (hideGhost) {
                distanceLabel2 = "";
            }
            appService3.updateElement(page, "ghost_distance", "content", distanceLabel2);
            this.mAppService.updateElement(page, "ghost_time_label", "content", hideGhost ? "" : this.context.get().getResources().getString(R.string.vc_title_ghost_time));
            this.mAppService.updateElement(page, "ghost_distance_label", "content", hideGhost ? "" : this.context.get().getResources().getString(R.string.vc_title_ghost_distance));
        }
    }

    private boolean isTargetAvailable() {
        VirtualWorkout workout = VirtualCoach.getVirtualWorkout();
        for (String page : this.mPages) {
            Target target = workout.getTarget(MetricType.getMetricType(page));
            if (target != null && target.threshold != -2.147483648E9d) {
                return true;
            }
        }
        return false;
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean isAvailable(T value) {
        if (!isBound() || value.doubleValue() == -2.147483648E9d) {
            return false;
        }
        if (VirtualCoach.isActive()) {
            return isTargetAvailable();
        }
        return value.doubleValue() >= 0.0d;
    }

    @Override // com.kopin.solos.metrics.Template
    protected String updatePage(PageBoxInfo pageBoxInfo, T updateValue) {
        MetricType metricType;
        Target target;
        this.mAppService.updateElement(pageBoxInfo.page, "value", "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        VirtualWorkout virtualWorkout = VirtualCoach.getVirtualWorkout();
        if (virtualWorkout == null) {
            return null;
        }
        if (virtualWorkout instanceof GhostWorkout) {
            MetricType metricType2 = getMetricType(pageBoxInfo.metric);
            Target target2 = virtualWorkout.getTarget(metricType2);
            double liveData = virtualWorkout.getLiveData(metricType2);
            return updateGhost(pageBoxInfo, metricType2, target2, liveData);
        }
        if (!(virtualWorkout instanceof Targets) || (target = virtualWorkout.getTarget((metricType = getMetricType(pageBoxInfo.metric)))) == null) {
            return null;
        }
        double liveData2 = virtualWorkout.getLiveData(metricType);
        return updateTarget(pageBoxInfo, metricType, target, liveData2);
    }

    private String updateTarget(PageBoxInfo boxInfo, MetricType metricType, Target target, double liveData) {
        double threshold = target != null ? target.threshold : -1.0d;
        String targetStr = Utility.formatMetric(metricType, threshold);
        this.mAppService.updateElement(boxInfo.page, boxInfo.target, "content", targetStr);
        this.mAppService.updateElement(boxInfo.page, boxInfo.value, "color", Integer.valueOf(this.mAppService.getColor(target.isInRange(Double.valueOf(liveData)) ? R.color.ahead_color : R.color.behind_color)));
        return Utility.formatMetric(metricType, liveData);
    }

    private String updateGhost(PageBoxInfo boxInfo, MetricType metricType, Target target, double liveData) {
        double ghostValue = target != null ? target.threshold : -2.147483648E9d;
        String ghostStr = (ghostValue == -2.147483648E9d || !VirtualCoach.isActive()) ? "----" : Utility.formatMetric(metricType, ghostValue);
        this.mAppService.updateElement(TEMPLATE_GHOST, boxInfo.page, KEY_GHOST_VALUE, "content", ghostStr);
        if (ghostValue == -2.147483648E9d) {
            this.mAppService.updateElement(TEMPLATE_GHOST, boxInfo.page, "value", "color", Integer.valueOf(this.mAppService.getColor(R.color.single_unit_text)));
        } else {
            this.mAppService.updateElement(TEMPLATE_GHOST, boxInfo.page, "value", "color", Integer.valueOf(this.mAppService.getColor(target.isAbove(Double.valueOf(liveData), boxInfo.page.contains("pace")) ? R.color.ahead_color : R.color.behind_color)));
        }
        return Utility.formatMetric(metricType, liveData);
    }

    private MetricType getMetricType(String pageId) {
        return MetricType.getMetricType(pageId.toUpperCase());
    }
}
