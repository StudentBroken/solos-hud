package com.kopin.solos.metrics;

import android.content.Context;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.view.graphics.Bar;

/* JADX INFO: loaded from: classes37.dex */
public class IntensityFactor extends GhostPage<Double> {
    private Context context;
    private float ftp;
    private Bar mIntensityBar;
    private static final int[] INTENSITY_ZONES = {R.string.intensity_zone1_name, R.string.intensity_zone2_name, R.string.intensity_zone3_name, R.string.intensity_zone4_name, R.string.intensity_zone5_name, R.string.intensity_zone6_name};
    public static final double[] INTENSITY_FACTOR_CATEGORIES = {0.75d, 0.85d, 0.95d, 1.05d, 1.15d};

    public IntensityFactor(AppService appService) {
        super(appService, TemplateManager.DataType.DERIVED_POWER);
        this.ftp = -1.0f;
        this.context = appService.getApplicationContext();
        addPage(MetricType.INTENSITY_FACTOR);
        setUnit(appService.getString(R.string.intensity_factor_unit_vertical));
        setLabel(appService.getString(R.string.vc_title_intensity_factor));
        setImage(MetricResource.INTENSITY_FACTOR);
        this.mIntensityBar = new Bar(appService, 6, 6, Bar.DEFAULT_WIDTH, 48, true);
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public boolean isAvailable(Double value) {
        return getFunctionalThresholdPower() > 1.0f && LiveRide.getPowerNormalised() > 0.0d && value.doubleValue() != -2.147483648E9d;
    }

    @Override // com.kopin.solos.metrics.Template
    public void updateBackground(Double updateValue) {
        double intensity = getIntensityFactor();
        this.mIntensityBar.addValue(getIntensityBarCategory(intensity));
    }

    @Override // com.kopin.solos.metrics.GhostPage, com.kopin.solos.metrics.Template
    public String updatePage(PageBoxInfo pageBoxInfo, Double updateValue) {
        double intensity = getIntensityFactor();
        String categoryTitle = intensityFactorZone(this.mAppService, intensity);
        this.mAppService.addBitmap("intensity_factor_bar", this.mIntensityBar.getBitmap());
        this.mAppService.updateElement("intensity_factor", "intensity_factor_name", "content", categoryTitle);
        return String.format("%.2f", Double.valueOf(intensity));
    }

    private static float getFunctionalThresholdPower() {
        FTP functionalThresholdPower = FTPHelper.getFTP();
        if (functionalThresholdPower == null) {
            return -1.0f;
        }
        return (long) functionalThresholdPower.mValue;
    }

    private static double getIntensityFactor() {
        return LiveRide.getPowerNormalised() / ((double) getFunctionalThresholdPower());
    }

    public static int getIntensityBarCategory(double intensity) {
        int i = 1;
        for (double category : INTENSITY_FACTOR_CATEGORIES) {
            if (intensity < category) {
                break;
            }
            i++;
        }
        return i;
    }

    private static String intensityFactorZone(Context context, double intensityFactor) {
        int category = getIntensityBarCategory(intensityFactor);
        return context.getString(INTENSITY_ZONES[category - 1]);
    }

    public static boolean hasIntensityFactor() {
        return getFunctionalThresholdPower() > 1.0f && LiveRide.getPowerNormalised() > 0.0d;
    }

    public static String intensityFactorZone(Context context) {
        double intensityFactor = getIntensityFactor();
        return intensityFactorZone(context, intensityFactor);
    }
}
