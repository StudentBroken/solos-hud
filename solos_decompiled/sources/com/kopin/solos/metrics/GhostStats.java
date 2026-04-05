package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.virtualworkout.GhostWorkout;
import com.kopin.solos.virtualworkout.VirtualCoach;

/* JADX INFO: loaded from: classes37.dex */
public class GhostStats extends GhostPage<Long> {
    public GhostStats(AppService appService) {
        super(appService, TemplateManager.DataType.TIME);
        addPage(MetricType.GHOST_STATS);
    }

    @Override // com.kopin.solos.metrics.Template
    public boolean before(Long value) {
        return false;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Long value) {
        return (VirtualCoach.isActive() && VirtualCoach.isWorkoutComplete()) || LiveRide.lastRide() != null;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Long updateValue) {
    }

    private String updateWinner(PageBoxInfo pageBoxInfo, boolean winner) {
        String message = this.mAppService.getString(winner ? R.string.vc_title_ghost_result_winner : R.string.vc_title_ghost_result_loser);
        updateElement(pageBoxInfo.page, "ghost_result_message", "content", message);
        updateElement(pageBoxInfo.page, "timeLabel", "content", this.mAppService.getString(winner ? R.string.vc_title_ghost_result_time_ahead : R.string.vc_title_ghost_result_time_behind));
        updateElement(pageBoxInfo.page, "distanceLabel", "content", this.mAppService.getString(winner ? R.string.vc_title_ghost_result_distance_ahead : R.string.vc_title_ghost_result_distance_behind));
        updateElement(pageBoxInfo.page, "timeValue", "color", Integer.valueOf(this.mAppService.getColor(winner ? R.color.ahead_color : R.color.behind_color)));
        updateElement(pageBoxInfo.page, "distanceValue", "color", Integer.valueOf(this.mAppService.getColor(winner ? R.color.ahead_color : R.color.behind_color)));
        this.mAppService.addBitmapFromResource("ghost_result_icon", winner ? R.drawable.ic_winner_green : R.drawable.ic_loser_red);
        return message;
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Long updateValue) {
        String timeLabel;
        GhostWorkout ghostWorkout = null;
        if (VirtualCoach.getVirtualWorkout() instanceof GhostWorkout) {
            ghostWorkout = (GhostWorkout) VirtualCoach.getVirtualWorkout();
        }
        if (ghostWorkout == null || ghostWorkout.hasData()) {
            return "-";
        }
        String strUpdateWinner = updateWinner(pageBoxInfo, ghostWorkout.isAhead());
        double distanceDiff = Math.abs(ghostWorkout.getDistanceDiff());
        if (distanceDiff > 250.0d) {
            String distStr = String.format("%.2f", Double.valueOf(Conversion.distanceForLocale(distanceDiff)));
            updateElement(pageBoxInfo.page, "distanceValue", "content", distStr);
            updateElement(pageBoxInfo.page, "distanceUnits", "content", Conversion.getUnitOfDistance(this.mAppService, true));
        } else {
            String distStr2 = String.format("%.0f", Double.valueOf(Conversion.distanceSmallForLocale(distanceDiff)));
            updateElement(pageBoxInfo.page, "distanceValue", "content", distStr2);
            updateElement(pageBoxInfo.page, "distanceUnits", "content", Conversion.getUnitOfDistanceSmall(this.mAppService, true));
        }
        long timeDiff = Math.abs(ghostWorkout.getTimeDiff());
        if (timeDiff <= 60) {
            timeLabel = String.format("00:%02d", Long.valueOf(timeDiff));
        } else {
            int min = (int) (timeDiff / 60);
            int sec = (int) (timeDiff % 60);
            timeLabel = String.format("%02d:%02d", Integer.valueOf(min), Integer.valueOf(sec));
        }
        updateElement(pageBoxInfo.page, "timeValue", "content", timeLabel);
        return strUpdateWinner;
    }
}
