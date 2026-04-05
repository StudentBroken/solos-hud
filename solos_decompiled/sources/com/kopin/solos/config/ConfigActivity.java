package com.kopin.solos.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.peloton.Peloton;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.StartActivity;
import com.kopin.solos.common.BaseActivity;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.metrics.MetricResource;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.ConfigPrefs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class ConfigActivity extends BaseActivity {
    CheckBox chkCloud;
    CheckBox chkCloudHttps;
    CheckBox chkCloudLive;
    CheckBox chkDebug;
    CheckBox chkDebugMenu;
    CheckBox chkFakeData;
    CheckBox chkFirmwareServerLive;
    CheckBox chkForceMulti;
    CheckBox chkMirror;
    CheckBox chkRun;
    CheckBox chkShortFTP;
    CheckBox chkVocon;
    EditText edDayOffset;
    EditText edSyncFullPeriod;
    EditText edSyncUploadPeriod;
    public static final MetricResource[] metricResources = {MetricResource.CADENCE, MetricResource.HEARTRATE, MetricResource.OXYGENATION, MetricResource.POWER, MetricResource.SPEED, MetricResource.STRIDE, MetricResource.PACE, MetricResource.STEP, MetricResource.KICK};
    private static final int[] metricCheckIds = {R.id.chkCadence, R.id.chkHeart, R.id.chkOxygen, R.id.chkPower, R.id.chkSpeed, R.id.chkStride};
    private static final int[] metricEditMinIds = {R.id.edMinCadence, R.id.edMinHeart, R.id.edMinOxygen, R.id.edMinPower, R.id.edMinSpeed, R.id.edMinStride};
    private static final int[] metricEditMaxIds = {R.id.edMaxCadence, R.id.edMaxHeart, R.id.edMaxOxygen, R.id.edMaxPower, R.id.edMaxSpeed, R.id.edMaxStride};
    private static final int[] metricConfigMaster = {R.id.chkCadenceMast, R.id.chkCadenceAveMast, R.id.chkCaloriesMast, R.id.chkDistanceMast, R.id.chkElevationMast, R.id.chkElevationChangeMast, R.id.chkFTPMast, R.id.chkHeartRateMast, R.id.chkHeartRateZoneMast, R.id.chkIntensityMast, R.id.chkNormPowerMast, R.id.chkOverallClimbMast, R.id.chkOxygenMast, R.id.chkPowerMast, R.id.chkPowerAveMast, R.id.chkPowerbarMast, R.id.chkSpeedMast, R.id.chkSpeedAveMast, R.id.chkStridegenMast, R.id.chkPaceMast, R.id.chkAvgPaceMast, R.id.chkStepMast, R.id.chkAvgStepMast, R.id.chkKickMast, R.id.chkKickAveMast, R.id.chkKickbarMast, R.id.chkTssMast, R.id.chkTargetPaceMast, R.id.chkTargetCadenceMast, R.id.chkTargetHeartRateMast, R.id.chkTargetPowerMast, R.id.chkTargetSpeedMast, R.id.chkTargetStepMast, R.id.chkTargetKickMast};
    private static final int[] metricConfigHeadset = {R.id.chkCadenceHead, R.id.chkCadenceAveHead, R.id.chkCaloriesHead, R.id.chkDistanceHead, R.id.chkElevationHead, R.id.chkElevationChangeHead, R.id.chkFTPHead, R.id.chkHeartRateHead, R.id.chkHeartRateZoneHead, R.id.chkIntensityHead, R.id.chkNormPowerHead, R.id.chkOverallClimbHead, R.id.chkOxygenHead, R.id.chkPowerHead, R.id.chkPowerAveHead, R.id.chkPowerbarHead, R.id.chkSpeedHead, R.id.chkSpeedAveHead, R.id.chkStrideHead, R.id.chkPaceHead, R.id.chkAvgPaceHead, R.id.chkStepHead, R.id.chkAvgStepHead, R.id.chkKickHead, R.id.chkKickAveHead, R.id.chkKickbarHead, R.id.chkTssHead, R.id.chkTargetCadenceHead, R.id.chkTargetHeartRateHead, R.id.chkTargetPowerHead, R.id.chkTargetSpeedHead, R.id.chkTargetPaceHead, R.id.chkTargetStepHead, R.id.chkTargetKickHead};
    private static final int[] metricConfigRideScreen = {R.id.chkCadenceRide, R.id.chkCadenceAveRide, R.id.chkCaloriesRide, R.id.chkDistanceRide, R.id.chkElevationRide, R.id.chkElevationChangeRide, R.id.chkFTPRide, R.id.chkHeartRateRide, R.id.chkHeartRateZoneRide, R.id.chkIntensityRide, R.id.chkNormPowerRide, R.id.chkOverallClimbRide, R.id.chkOxygenRide, R.id.chkPowerRide, R.id.chkPowerAveRide, R.id.chkPowerbarRide, R.id.chkSpeedRide, R.id.chkSpeedAveRide, R.id.chkStrideRide, R.id.chkPaceRide, R.id.chkAvgPaceRide, R.id.chkStepRide, R.id.chkAvgStepRide, R.id.chkKickRide, R.id.chkKickAveRide, R.id.chkKickbarRide, R.id.chkTssRide, R.id.chkTargetCadenceRide, R.id.chkTargetHeartRateRide, R.id.chkTargetPowerRide, R.id.chkTargetSpeedRide, R.id.chkTargetPaceRide, R.id.chkTargetStepRide, R.id.chkTargetKickRide};
    private static final Map<MetricResource, MetricItem> metricItems = new HashMap();
    List<CheckBox> checkBoxListMaster = new ArrayList();
    List<CheckBox> checkBoxListHeadset = new ArrayList();
    List<CheckBox> checkBoxListRideScreen = new ArrayList();

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_config);
        ConfigHelper.init(this);
        setupMetrics();
        setupGeneral();
        setupMetricConfig();
        final boolean fromStartup = getIntent().getBooleanExtra("startup", false);
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.config.ConfigActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ConfigActivity.this.apply();
                if (fromStartup) {
                    ConfigActivity.this.startActivity(new Intent(ConfigActivity.this, (Class<?>) StartActivity.class).putExtra("config", false).setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM));
                }
                ConfigActivity.this.finish();
            }
        });
    }

    private void setupMetrics() {
        AppConfig.metricItems.clear();
        int i = 0;
        for (int id : metricCheckIds) {
            CheckBox checkBox = (CheckBox) findViewById(id);
            if (checkBox != null) {
                AppConfig.metricItems.put(metricResources[i], new MetricItem(this, metricResources[i], (CheckBox) findViewById(id), (EditText) findViewById(metricEditMinIds[i]), (EditText) findViewById(metricEditMaxIds[i])));
            }
            i++;
        }
    }

    private void setupMetricConfig() {
        this.checkBoxListMaster.clear();
        this.checkBoxListHeadset.clear();
        this.checkBoxListRideScreen.clear();
        for (int id : metricConfigMaster) {
            CheckBox checkBox = (CheckBox) findViewById(id);
            switch (id) {
                case R.id.chkCadenceMast /* 2131951723 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.CADENCE);
                    break;
                case R.id.chkCadenceAveMast /* 2131951726 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_CADENCE);
                    break;
                case R.id.chkCaloriesMast /* 2131951729 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.CALORIES);
                    break;
                case R.id.chkDistanceMast /* 2131951732 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.DISTANCE);
                    break;
                case R.id.chkElevationMast /* 2131951735 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.ELEVATION);
                    break;
                case R.id.chkElevationChangeMast /* 2131951738 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.ELEVATION_CHANGE);
                    break;
                case R.id.chkFTPMast /* 2131951741 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.FUNCTIONAL_THRESHOLD_POWER);
                    break;
                case R.id.chkHeartRateMast /* 2131951744 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.HEART_RATE);
                    break;
                case R.id.chkHeartRateZoneMast /* 2131951747 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.HEART_RATE_ZONE);
                    break;
                case R.id.chkIntensityMast /* 2131951750 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.INTENSITY_FACTOR);
                    break;
                case R.id.chkNormPowerMast /* 2131951753 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.NORMALISED_POWER);
                    break;
                case R.id.chkOverallClimbMast /* 2131951756 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.OVERALL_CLIMB);
                    break;
                case R.id.chkOxygenMast /* 2131951759 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.OXYGEN);
                    break;
                case R.id.chkPowerMast /* 2131951762 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.POWER);
                    break;
                case R.id.chkPowerAveMast /* 2131951765 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_POWER);
                    break;
                case R.id.chkPowerbarMast /* 2131951768 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.POWER_BAR);
                    break;
                case R.id.chkSpeedMast /* 2131951771 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.SPEED);
                    break;
                case R.id.chkSpeedAveMast /* 2131951774 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_SPEED);
                    break;
                case R.id.chkStridegenMast /* 2131951777 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.STRIDE);
                    break;
                case R.id.chkPaceMast /* 2131951780 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.PACE);
                    break;
                case R.id.chkAvgPaceMast /* 2131951783 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_PACE);
                    break;
                case R.id.chkStepMast /* 2131951786 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.STEP);
                    break;
                case R.id.chkAvgStepMast /* 2131951789 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_STEP);
                    break;
                case R.id.chkKickMast /* 2131951792 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.KICK);
                    break;
                case R.id.chkKickAveMast /* 2131951795 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.AVG_KICK);
                    break;
                case R.id.chkKickbarMast /* 2131951798 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.KICK_BAR);
                    break;
                case R.id.chkTssMast /* 2131951801 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TRAINING_STRESS_SCORE);
                    break;
                case R.id.chkTargetCadenceMast /* 2131951804 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_CADENCE);
                    break;
                case R.id.chkTargetHeartRateMast /* 2131951807 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_HEART_RATE);
                    break;
                case R.id.chkTargetPowerMast /* 2131951810 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_POWER);
                    break;
                case R.id.chkTargetSpeedMast /* 2131951813 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_SPEED);
                    break;
                case R.id.chkTargetPaceMast /* 2131951816 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_PACE);
                    break;
                case R.id.chkTargetStepMast /* 2131951819 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_STEP);
                    break;
                case R.id.chkTargetKickMast /* 2131951822 */:
                    setupConfigCheckBoxMaster(checkBox, MetricDataType.TARGET_AVERAGE_KICK);
                    break;
            }
        }
        for (int id2 : metricConfigHeadset) {
            CheckBox checkBox2 = (CheckBox) findViewById(id2);
            switch (id2) {
                case R.id.chkCadenceHead /* 2131951724 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.CADENCE);
                    break;
                case R.id.chkCadenceAveHead /* 2131951727 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_CADENCE);
                    break;
                case R.id.chkCaloriesHead /* 2131951730 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.CALORIES);
                    break;
                case R.id.chkDistanceHead /* 2131951733 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.DISTANCE);
                    break;
                case R.id.chkElevationHead /* 2131951736 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.ELEVATION);
                    break;
                case R.id.chkElevationChangeHead /* 2131951739 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.ELEVATION_CHANGE);
                    break;
                case R.id.chkFTPHead /* 2131951742 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.FUNCTIONAL_THRESHOLD_POWER);
                    break;
                case R.id.chkHeartRateHead /* 2131951745 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.HEART_RATE);
                    break;
                case R.id.chkHeartRateZoneHead /* 2131951748 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.HEART_RATE_ZONE);
                    break;
                case R.id.chkIntensityHead /* 2131951751 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.INTENSITY_FACTOR);
                    break;
                case R.id.chkNormPowerHead /* 2131951754 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.NORMALISED_POWER);
                    break;
                case R.id.chkOverallClimbHead /* 2131951757 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.OVERALL_CLIMB);
                    break;
                case R.id.chkOxygenHead /* 2131951760 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.OXYGEN);
                    break;
                case R.id.chkPowerHead /* 2131951763 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.POWER);
                    break;
                case R.id.chkPowerAveHead /* 2131951766 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_POWER);
                    break;
                case R.id.chkPowerbarHead /* 2131951769 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.POWER_BAR);
                    break;
                case R.id.chkSpeedHead /* 2131951772 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.SPEED);
                    break;
                case R.id.chkSpeedAveHead /* 2131951775 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_SPEED);
                    break;
                case R.id.chkStrideHead /* 2131951778 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.STRIDE);
                    break;
                case R.id.chkPaceHead /* 2131951781 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.PACE);
                    break;
                case R.id.chkAvgPaceHead /* 2131951784 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_PACE);
                    break;
                case R.id.chkStepHead /* 2131951787 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.STEP);
                    break;
                case R.id.chkAvgStepHead /* 2131951790 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_STEP);
                    break;
                case R.id.chkKickHead /* 2131951793 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.KICK);
                    break;
                case R.id.chkKickAveHead /* 2131951796 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.AVG_KICK);
                    break;
                case R.id.chkKickbarHead /* 2131951799 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.KICK_BAR);
                    break;
                case R.id.chkTssHead /* 2131951802 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TRAINING_STRESS_SCORE);
                    break;
                case R.id.chkTargetCadenceHead /* 2131951805 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_CADENCE);
                    break;
                case R.id.chkTargetHeartRateHead /* 2131951808 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_HEART_RATE);
                    break;
                case R.id.chkTargetPowerHead /* 2131951811 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_POWER);
                    break;
                case R.id.chkTargetSpeedHead /* 2131951814 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_SPEED);
                    break;
                case R.id.chkTargetPaceHead /* 2131951817 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_PACE);
                    break;
                case R.id.chkTargetStepHead /* 2131951820 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_STEP);
                    break;
                case R.id.chkTargetKickHead /* 2131951823 */:
                    setupConfigCheckBoxHeadset(checkBox2, MetricDataType.TARGET_AVERAGE_KICK);
                    break;
            }
        }
        for (int id3 : metricConfigRideScreen) {
            CheckBox checkBox3 = (CheckBox) findViewById(id3);
            switch (id3) {
                case R.id.chkCadenceRide /* 2131951725 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.CADENCE);
                    break;
                case R.id.chkCadenceAveRide /* 2131951728 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_CADENCE);
                    break;
                case R.id.chkCaloriesRide /* 2131951731 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.CALORIES);
                    break;
                case R.id.chkDistanceRide /* 2131951734 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.DISTANCE);
                    break;
                case R.id.chkElevationRide /* 2131951737 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.ELEVATION);
                    break;
                case R.id.chkElevationChangeRide /* 2131951740 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.ELEVATION_CHANGE);
                    break;
                case R.id.chkFTPRide /* 2131951743 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.FUNCTIONAL_THRESHOLD_POWER);
                    break;
                case R.id.chkHeartRateRide /* 2131951746 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.HEART_RATE);
                    break;
                case R.id.chkHeartRateZoneRide /* 2131951749 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.HEART_RATE_ZONE);
                    break;
                case R.id.chkIntensityRide /* 2131951752 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.INTENSITY_FACTOR);
                    break;
                case R.id.chkNormPowerRide /* 2131951755 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.NORMALISED_POWER);
                    break;
                case R.id.chkOverallClimbRide /* 2131951758 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.OVERALL_CLIMB);
                    break;
                case R.id.chkOxygenRide /* 2131951761 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.OXYGEN);
                    break;
                case R.id.chkPowerRide /* 2131951764 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.POWER);
                    break;
                case R.id.chkPowerAveRide /* 2131951767 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_POWER);
                    break;
                case R.id.chkPowerbarRide /* 2131951770 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.POWER_BAR);
                    break;
                case R.id.chkSpeedRide /* 2131951773 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.SPEED);
                    break;
                case R.id.chkSpeedAveRide /* 2131951776 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_SPEED);
                    break;
                case R.id.chkStrideRide /* 2131951779 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.STRIDE);
                    break;
                case R.id.chkPaceRide /* 2131951782 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.PACE);
                    break;
                case R.id.chkAvgPaceRide /* 2131951785 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_PACE);
                    break;
                case R.id.chkStepRide /* 2131951788 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.STEP);
                    break;
                case R.id.chkAvgStepRide /* 2131951791 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_STEP);
                    break;
                case R.id.chkKickRide /* 2131951794 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.KICK);
                    break;
                case R.id.chkKickAveRide /* 2131951797 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.AVG_KICK);
                    break;
                case R.id.chkKickbarRide /* 2131951800 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.KICK_BAR);
                    break;
                case R.id.chkTssRide /* 2131951803 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TRAINING_STRESS_SCORE);
                    break;
                case R.id.chkTargetCadenceRide /* 2131951806 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_CADENCE);
                    break;
                case R.id.chkTargetHeartRateRide /* 2131951809 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_HEART_RATE);
                    break;
                case R.id.chkTargetPowerRide /* 2131951812 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_POWER);
                    break;
                case R.id.chkTargetSpeedRide /* 2131951815 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_SPEED);
                    break;
                case R.id.chkTargetPaceRide /* 2131951818 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_PACE);
                    break;
                case R.id.chkTargetStepRide /* 2131951821 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_STEP);
                    break;
                case R.id.chkTargetKickRide /* 2131951824 */:
                    setupConfigCheckBoxRide(checkBox3, MetricDataType.TARGET_AVERAGE_KICK);
                    break;
            }
        }
    }

    private void setupConfigCheckBoxMaster(CheckBox checkBox, MetricDataType metricType) {
        checkBox.setChecked(ConfigPrefs.isMetricConfigMaster(metricType));
        checkBox.setTag(metricType);
        this.checkBoxListMaster.add(checkBox);
    }

    private void setupConfigCheckBoxHeadset(CheckBox checkBox, MetricDataType metricType) {
        checkBox.setChecked(ConfigPrefs.isMetricConfigHeadset(metricType));
        checkBox.setTag(metricType);
        this.checkBoxListHeadset.add(checkBox);
    }

    private void setupConfigCheckBoxRide(CheckBox checkBox, MetricDataType metricType) {
        checkBox.setChecked(ConfigPrefs.isMetricConfigRideScreen(metricType));
        checkBox.setTag(metricType);
        this.checkBoxListRideScreen.add(checkBox);
    }

    private void setupGeneral() {
        this.chkShortFTP = (CheckBox) findViewById(R.id.chkShortFTP);
        this.chkShortFTP.setChecked(Config.SHORT_FTP);
        this.chkDebug = (CheckBox) findViewById(R.id.chkDebug);
        this.chkDebug.setChecked(Config.DEBUG);
        this.chkDebugMenu = (CheckBox) findViewById(R.id.chkDebugMenu);
        this.chkDebugMenu.setChecked(MainActivity.SHOW_DEBUG_MENUS);
        this.chkVocon = (CheckBox) findViewById(R.id.chkVocon);
        this.chkVocon.setChecked(Config.VOCON_ENABLED);
        this.chkRun = (CheckBox) findViewById(R.id.chkRun);
        this.chkRun.setChecked(Config.MULTI_SPORT_ENABLED);
        this.chkFakeData = (CheckBox) findViewById(R.id.chkFakeData);
        this.chkFakeData.setChecked(Config.FAKE_DATA);
        this.chkMirror = (CheckBox) findViewById(R.id.chkMirror);
        this.chkMirror.setChecked(Config.SHOW_GLASSES_MIRROR);
        this.chkForceMulti = (CheckBox) findViewById(R.id.chkForceMulti);
        this.chkForceMulti.setChecked(Config.FORCE_MULTI_TIME_ELAPSED);
        this.chkCloud = (CheckBox) findViewById(R.id.chkCloud);
        this.chkCloud.setChecked(Config.SYNC_PROVIDER != Platforms.None);
        this.chkCloudLive = (CheckBox) findViewById(R.id.chkCloudLive);
        this.chkCloudLive.setChecked(Config.CLOUD_LIVE);
        this.chkCloudHttps = (CheckBox) findViewById(R.id.chkCloudHttps);
        this.chkCloudHttps.setChecked(Config.CLOUD_HTTPS);
        this.chkFirmwareServerLive = (CheckBox) findViewById(R.id.chkFirmWareServer);
        this.chkFirmwareServerLive.setChecked(Config.IS_RELEASE_FIRMWARE_SERVER);
        this.edSyncUploadPeriod = (EditText) findViewById(R.id.edSyncUploadPeriod);
        this.edSyncFullPeriod = (EditText) findViewById(R.id.edSyncFullPeriod);
        this.edDayOffset = (EditText) findViewById(R.id.edDayOffset);
        try {
            this.edSyncUploadPeriod.setText(String.valueOf(Config.CLOUD_SYNC_UPLOAD_PERIOD));
        } catch (Exception e) {
        }
        try {
            this.edSyncFullPeriod.setText(String.valueOf(Config.CLOUD_SYNC_FULL_PERIOD));
        } catch (Exception e2) {
        }
        try {
            this.edDayOffset.setText(String.valueOf(Config.START_DAY_OFFSET));
        } catch (Exception e3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void apply() {
        for (MetricItem metricItem : AppConfig.metricItems.values()) {
            metricItem.serialize();
        }
        applyMetricConfigs();
        try {
            long dayOffset = Long.parseLong(this.edDayOffset.getText().toString());
            Config.START_DAY_OFFSET = dayOffset;
        } catch (Exception e) {
        }
        try {
            long upload = Long.parseLong(this.edSyncUploadPeriod.getText().toString());
            Config.CLOUD_SYNC_UPLOAD_PERIOD = upload;
        } catch (Exception e2) {
        }
        try {
            long full = Long.parseLong(this.edSyncFullPeriod.getText().toString());
            Config.CLOUD_SYNC_UPLOAD_PERIOD = full;
        } catch (Exception e3) {
        }
        Config.MULTI_SPORT_ENABLED = this.chkRun.isChecked();
        Config.DEBUG = this.chkDebug.isChecked();
        MainActivity.SHOW_DEBUG_MENUS = this.chkDebugMenu.isChecked();
        Config.VOCON_ENABLED = this.chkVocon.isChecked();
        Config.FAKE_DATA = this.chkFakeData.isChecked();
        Config.SHORT_FTP = this.chkShortFTP.isChecked();
        Config.SHOW_GLASSES_MIRROR = this.chkMirror.isChecked();
        Config.FORCE_MULTI_TIME_ELAPSED = this.chkForceMulti.isChecked();
        Config.SYNC_PROVIDER = this.chkCloud.isChecked() ? Platforms.Peloton : Platforms.None;
        Config.CLOUD_HTTPS = this.chkCloudHttps.isChecked();
        Config.CLOUD_LIVE = this.chkCloudLive.isChecked();
        Config.IS_RELEASE_FIRMWARE_SERVER = this.chkFirmwareServerLive.isChecked();
        Peloton.configure(Config.CLOUD_LIVE, Config.CLOUD_HTTPS);
        ConfigHelper.apply();
    }

    private void applyMetricConfigs() {
        for (CheckBox checkBox : this.checkBoxListMaster) {
            ConfigPrefs.setMetricConfigMaster((MetricDataType) checkBox.getTag(), checkBox.isChecked());
        }
        for (CheckBox checkBox2 : this.checkBoxListHeadset) {
            ConfigPrefs.setMetricConfigHeadset((MetricDataType) checkBox2.getTag(), checkBox2.isChecked());
        }
        for (CheckBox checkBox3 : this.checkBoxListRideScreen) {
            ConfigPrefs.setMetricConfigRide((MetricDataType) checkBox3.getTag(), checkBox3.isChecked());
        }
        ConfigMetrics.init();
    }
}
