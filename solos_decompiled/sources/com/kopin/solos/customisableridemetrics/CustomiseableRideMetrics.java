package com.kopin.solos.customisableridemetrics;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import com.kopin.solos.R;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.view.PagerContainer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes24.dex */
public class CustomiseableRideMetrics implements PagerContainer.PagerInitialised {
    private static final String CONTAINER_PREF_PREFIX = "RIDE_METRIC_";
    private static final boolean PAGER_SELECTION_EXCLUSIVE = true;
    private static final float PAGE_PROPORTION_WIDTH = 0.5f;
    private static final boolean SINGLE_PAGER_ENABLED = true;
    private PagerContainer mContainer1;
    private PagerContainer mContainer2;
    private PagerContainer mContainer3;
    private WeakReference<Context> mContext;
    private Vibrator vib;
    private static final int NUM_RIDE_SCREEN_METRIC_PAGES = RideScreenMetric.values().length;
    private static MultiLabelManager multiRideMetricLabelManager = new MultiLabelManager();
    private static Map<Integer, String> cachedValues = new HashMap();
    private List<Integer> allItems = new ArrayList();
    private List<Integer> items = new ArrayList();
    private boolean allowVibrate = false;

    public enum RideScreenMetric {
        DISTANCE(R.string.distance, R.drawable.ic_ic_distance_icon),
        AVG_SPEED(R.string.stats_text_avg_speed, R.drawable.ic_ic_speed_icon, Sensor.DataType.SPEED, true),
        ELEVATION_CHANGE(R.string.vc_title_elevation_change, R.drawable.ic_elevation),
        OVERALL_CLIMB(R.string.vc_title_overall_climb, R.drawable.ic_elevation),
        CADENCE(R.string.vc_title_cadence, R.string.cadence_unit, R.drawable.ic_cadence_small, Sensor.DataType.CADENCE, false),
        AVG_CADENCE(R.string.vc_title_average_cadence, R.string.cadence_unit, R.drawable.ic_cadence_small, Sensor.DataType.CADENCE, false),
        CALORIES(R.string.vc_title_calories, R.string.calories_unit, R.drawable.ic_calories_small),
        HEART_RATE(R.string.vc_title_heartrate, R.string.heart_unit, R.drawable.ic_heart_rate_small, Sensor.DataType.HEARTRATE),
        AVG_HEART_RATE(R.string.vc_title_avg_heartrate, R.string.heart_unit, R.drawable.ic_heart_rate_small, Sensor.DataType.HEARTRATE),
        POWER(R.string.vc_title_power, R.string.power_unit_abbrev, R.drawable.ic_power_small, Sensor.DataType.POWER),
        AVG_POWER(R.string.vc_title_average_power, R.string.power_unit_abbrev, R.drawable.ic_power_small, Sensor.DataType.POWER),
        SPEED(R.string.vc_title_speed, R.drawable.ic_ic_speed_icon, Sensor.DataType.SPEED, true),
        OXYGEN(R.string.vc_title_oxygen, R.string.oxygen_unit, R.drawable.ic_lungs_ride_screen_small, Sensor.DataType.OXYGEN),
        PACE(R.string.vc_title_pace, R.drawable.ic_ic_speed_icon, Sensor.DataType.PACE, false),
        AVG_PACE(R.string.vc_title_avg_pace, R.drawable.ic_ic_speed_icon, Sensor.DataType.PACE, false),
        STRIDE(R.string.vc_title_stride, R.drawable.ic_stride_small, Sensor.DataType.STRIDE, true),
        STEP(R.string.vc_title_cadence, R.string.unit_cadence_run_short, R.drawable.ic_cadence_run, Sensor.DataType.STEP, false),
        AVG_STEP(R.string.vc_title_average_cadence, R.string.unit_cadence_run_short, R.drawable.ic_cadence_run, Sensor.DataType.STEP, false),
        KICK(R.string.vc_title_power, R.string.power_unit_abbrev, R.drawable.ic_run_power_small, Sensor.DataType.KICK),
        AVG_KICK(R.string.vc_title_average_power, R.string.power_unit_abbrev, R.drawable.ic_run_power_small, Sensor.DataType.KICK);

        private boolean floatType;
        private int iconResource;
        private Sensor.DataType sensorDataType;
        private int titleResource;
        private int unitResource;

        RideScreenMetric(int titleResourceId, int iconResourceId) {
            this(titleResourceId, 0, iconResourceId, Sensor.DataType.UNKOWN, false);
        }

        RideScreenMetric(int titleResourceId, int unitResourceId, int iconResourceId) {
            this(titleResourceId, unitResourceId, iconResourceId, Sensor.DataType.UNKOWN, false);
        }

        RideScreenMetric(int titleResourceId, int unitResourceId, int iconResourceId, Sensor.DataType sensorType) {
            this(titleResourceId, unitResourceId, iconResourceId, sensorType, false);
        }

        RideScreenMetric(int titleResourceId, int iconResourceId, Sensor.DataType sensorType, boolean floatType) {
            this(titleResourceId, 0, iconResourceId, sensorType, floatType);
        }

        RideScreenMetric(int titleResourceId, int unitResourceId, int iconResourceId, Sensor.DataType dataType, boolean floatType) {
            this.titleResource = 0;
            this.unitResource = 0;
            this.iconResource = 0;
            this.floatType = false;
            this.titleResource = titleResourceId;
            this.unitResource = unitResourceId;
            this.iconResource = iconResourceId;
            this.sensorDataType = dataType;
            this.floatType = floatType;
        }

        public int getId() {
            return ordinal();
        }

        public int getTitleLabelId() {
            return ordinal() + 100;
        }

        public static int getTitleLabelId(int ordinal) {
            return values()[ordinal].getTitleLabelId();
        }

        public int getUnitLabelId() {
            return ordinal() + 1000;
        }

        public static int getUnitLabelId(int ordinal) {
            return values()[ordinal].getUnitLabelId();
        }

        public int getTitleResource() {
            return this.titleResource;
        }

        public static int getTitleResource(int ordinal) {
            return values()[ordinal].getTitleResource();
        }

        public int getUnitResource() {
            return this.unitResource;
        }

        public int getIconResource() {
            return this.iconResource;
        }

        public static int getIconResource(int ordinal) {
            return values()[ordinal].getIconResource();
        }

        public boolean isSupported() {
            return this.sensorDataType == Sensor.DataType.UNKOWN || SensorsConnector.isAllowedType(this.sensorDataType);
        }

        public static boolean isSupported(int ordinal) {
            return values()[ordinal].isSupported();
        }

        public boolean isFloatType() {
            return this.floatType;
        }

        public static boolean isFloatType(int ordinal) {
            return values()[ordinal].floatType;
        }

        public MetricDataType forDataType() {
            switch (this) {
                case CADENCE:
                    return MetricDataType.CADENCE;
                case AVG_CADENCE:
                    return MetricDataType.AVG_CADENCE;
                case CALORIES:
                    return MetricDataType.CALORIES;
                case DISTANCE:
                    return MetricDataType.DISTANCE;
                case ELEVATION_CHANGE:
                    return MetricDataType.ELEVATION_CHANGE;
                case AVG_HEART_RATE:
                case HEART_RATE:
                    return MetricDataType.HEART_RATE;
                case OVERALL_CLIMB:
                    return MetricDataType.OVERALL_CLIMB;
                case OXYGEN:
                    return MetricDataType.OXYGEN;
                case POWER:
                    return MetricDataType.POWER;
                case AVG_POWER:
                    return MetricDataType.AVG_POWER;
                case SPEED:
                    return MetricDataType.SPEED;
                case AVG_SPEED:
                    return MetricDataType.AVG_SPEED;
                case STRIDE:
                    return MetricDataType.STRIDE;
                case STEP:
                    return MetricDataType.STEP;
                case AVG_STEP:
                    return MetricDataType.AVG_STEP;
                case PACE:
                    return MetricDataType.PACE;
                case AVG_PACE:
                    return MetricDataType.AVG_PACE;
                case KICK:
                    return MetricDataType.KICK;
                case AVG_KICK:
                    return MetricDataType.AVG_KICK;
                default:
                    return null;
            }
        }
    }

    public CustomiseableRideMetrics(Context context) {
        this.mContext = new WeakReference<>(context.getApplicationContext());
        this.vib = (Vibrator) context.getSystemService("vibrator");
    }

    public void init(PagerContainer pagerContainer1, PagerContainer pagerContainer2, PagerContainer pagerContainer3, String prefSportKey, List<RideScreenMetric> defaultSet) {
        for (int i = 0; i < NUM_RIDE_SCREEN_METRIC_PAGES; i++) {
            if (RideScreenMetric.isSupported(i) && ConfigMetrics.isRideScreenMetricEnabled(RideScreenMetric.values()[i].forDataType())) {
                this.allItems.add(Integer.valueOf(i));
            }
        }
        this.mContainer1 = pagerContainer1;
        this.mContainer1.setPrefKey(CONTAINER_PREF_PREFIX + prefSportKey + 1, defaultSet.get(0).ordinal());
        initContainer(this.mContainer1, 0.5d);
        this.mContainer1.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.kopin.solos.customisableridemetrics.CustomiseableRideMetrics.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float v, int i1) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                if (CustomiseableRideMetrics.this.mContainer1.isEnabled()) {
                    CustomiseableRideMetrics.this.mContainer1.currentPageId = ((Integer) CustomiseableRideMetrics.this.items.get(i2 >= CustomiseableRideMetrics.this.items.size() ? i2 - CustomiseableRideMetrics.this.items.size() : i2)).intValue();
                    CustomiseableRideMetrics.this.mContainer1.getViewPager().setCurrentItem(CustomiseableRideMetrics.this.mContainer1.resetPosition(i2, CustomiseableRideMetrics.this.items.size()), false);
                    CustomiseableRideMetrics.this.mContainer1.setSelectedId(CustomiseableRideMetrics.this.mContainer1.currentPageId);
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }
        });
        this.mContainer2 = pagerContainer2;
        this.mContainer2.setPrefKey(CONTAINER_PREF_PREFIX + prefSportKey + 2, defaultSet.get(1).ordinal());
        initContainer(this.mContainer2, 0.5d);
        this.mContainer2.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.kopin.solos.customisableridemetrics.CustomiseableRideMetrics.2
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float v, int i1) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                if (CustomiseableRideMetrics.this.mContainer2.isEnabled()) {
                    CustomiseableRideMetrics.this.mContainer2.currentPageId = ((Integer) CustomiseableRideMetrics.this.items.get(i2 >= CustomiseableRideMetrics.this.items.size() ? i2 - CustomiseableRideMetrics.this.items.size() : i2)).intValue();
                    CustomiseableRideMetrics.this.mContainer2.getViewPager().setCurrentItem(CustomiseableRideMetrics.this.mContainer2.resetPosition(i2, CustomiseableRideMetrics.this.items.size()), false);
                    CustomiseableRideMetrics.this.mContainer2.setSelectedId(CustomiseableRideMetrics.this.mContainer2.currentPageId);
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }
        });
        this.mContainer3 = pagerContainer3;
        this.mContainer3.setPrefKey(CONTAINER_PREF_PREFIX + prefSportKey + 3, defaultSet.get(2).ordinal());
        initContainer(this.mContainer3, 0.5d);
        this.mContainer3.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.kopin.solos.customisableridemetrics.CustomiseableRideMetrics.3
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float v, int i1) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                if (CustomiseableRideMetrics.this.mContainer3.isEnabled()) {
                    CustomiseableRideMetrics.this.mContainer3.currentPageId = ((Integer) CustomiseableRideMetrics.this.items.get(i2 >= CustomiseableRideMetrics.this.items.size() ? i2 - CustomiseableRideMetrics.this.items.size() : i2)).intValue();
                    CustomiseableRideMetrics.this.mContainer3.getViewPager().setCurrentItem(CustomiseableRideMetrics.this.mContainer3.resetPosition(i2, CustomiseableRideMetrics.this.items.size()), false);
                    CustomiseableRideMetrics.this.mContainer3.setSelectedId(CustomiseableRideMetrics.this.mContainer3.currentPageId);
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }
        });
        setLabels();
        updateUnitSystem();
        this.allowVibrate = true;
    }

    private void initContainer(PagerContainer pagerContainer, double pageProportionWidth) {
        pagerContainer.setPagerInitCallback(this);
        pagerContainer.setPageWidthProportion(pageProportionWidth);
        pagerContainer.getViewPager().setAdapter(new MetricsPagerAdapter(this.mContext.get(), multiRideMetricLabelManager, Integer.valueOf(pagerContainer.currentPageId)));
        pagerContainer.getViewPager().setOffscreenPageLimit(this.allItems.size());
        pagerContainer.getViewPager().setClipChildren(false);
        setLabels();
        updateUnitSystem();
    }

    public void setMetricValue(String text, RideScreenMetric metric) {
        multiRideMetricLabelManager.setText(text, metric.getId());
        cachedValues.put(Integer.valueOf(metric.getId()), text);
    }

    public void resetMetricValues() {
        cachedValues.clear();
        for (RideScreenMetric rideScreenMetric : RideScreenMetric.values()) {
            multiRideMetricLabelManager.setText(rideScreenMetric.isFloatType() ? R.string.placeholder_float : R.string.placeholder_int, rideScreenMetric.getId());
        }
    }

    public static void setRideMetricText(int textRes, int... labelIds) {
        for (int labelId : labelIds) {
            if (textRes > 0) {
                multiRideMetricLabelManager.setText(textRes, labelId);
            }
        }
    }

    public static void setRideMetricText(String text, int... labelIds) {
        for (int labelId : labelIds) {
            if (text != null) {
                multiRideMetricLabelManager.setText(text, labelId);
            }
        }
    }

    public static void setCompoundDrawablesWithIntrinsicBounds(RideScreenMetric rideScreenMetric, int left, int top, int right, int bottom) {
        multiRideMetricLabelManager.setCompoundDrawablesWithIntrinsicBounds(rideScreenMetric.getTitleLabelId(), left, top, right, bottom);
    }

    public void updateUnitSystem() {
        if (Prefs.getUnitSystem() == Prefs.UnitSystem.METRIC) {
            setRideMetricText(R.string.unit_distance_metric_short, RideScreenMetric.DISTANCE.getUnitLabelId());
            setRideMetricText(R.string.unit_speed_metric_short, RideScreenMetric.AVG_SPEED.getUnitLabelId(), RideScreenMetric.SPEED.getUnitLabelId());
            setRideMetricText(R.string.unit_pace_slash_metric_short, RideScreenMetric.AVG_PACE.getUnitLabelId(), RideScreenMetric.PACE.getUnitLabelId());
            setRideMetricText(R.string.unit_length_metric_short, RideScreenMetric.ELEVATION_CHANGE.getUnitLabelId(), RideScreenMetric.OVERALL_CLIMB.getUnitLabelId());
            setRideMetricText(R.string.unit_cm, RideScreenMetric.STRIDE.getUnitLabelId(), RideScreenMetric.STRIDE.getUnitLabelId());
            return;
        }
        setRideMetricText(R.string.unit_distance_imperial_short, RideScreenMetric.DISTANCE.getUnitLabelId());
        setRideMetricText(R.string.unit_speed_imperial_short, RideScreenMetric.AVG_SPEED.getUnitLabelId(), RideScreenMetric.SPEED.getUnitLabelId());
        setRideMetricText(R.string.unit_pace_slash_imperial_short, RideScreenMetric.AVG_PACE.getUnitLabelId(), RideScreenMetric.PACE.getUnitLabelId());
        setRideMetricText(R.string.unit_length_imperial_short, RideScreenMetric.ELEVATION_CHANGE.getUnitLabelId(), RideScreenMetric.OVERALL_CLIMB.getUnitLabelId());
        setRideMetricText(R.string.unit_inches, RideScreenMetric.STRIDE.getUnitLabelId(), RideScreenMetric.STRIDE.getUnitLabelId());
    }

    @Override // com.kopin.solos.view.PagerContainer.PagerInitialised
    public void onInitialised() {
        setLabels();
        updateUnitSystem();
        this.mContainer1.invalidate();
        this.mContainer2.invalidate();
        this.mContainer3.invalidate();
    }

    public static void setLabels() {
        for (RideScreenMetric metric : RideScreenMetric.values()) {
            setRideMetricText(metric.getTitleResource(), metric.getTitleLabelId());
            setRideMetricText(metric.getUnitResource(), metric.getUnitLabelId());
            setCompoundDrawablesWithIntrinsicBounds(metric, metric.getIconResource(), 0, 0, 0);
        }
    }

    @Override // com.kopin.solos.view.PagerContainer.PagerInitialised
    public void onEnabled(PagerContainer enabledPagerContainer, boolean enabled) {
        if (!enabled) {
            setEnabled(false);
            return;
        }
        containerOnEnabled(this.mContainer1, enabledPagerContainer, this.mContainer2, this.mContainer3);
        containerOnEnabled(this.mContainer2, enabledPagerContainer, this.mContainer1, this.mContainer3);
        containerOnEnabled(this.mContainer3, enabledPagerContainer, this.mContainer1, this.mContainer2);
        setLabels();
        updateUnitSystem();
        if (this.vib != null && this.allowVibrate) {
            this.vib.vibrate(300L);
        }
    }

    private void containerOnEnabled(PagerContainer mContainer, PagerContainer enabledPagerContainer, PagerContainer otherContainer, PagerContainer otherContainer2) {
        if (mContainer != enabledPagerContainer) {
            ((MetricsPagerAdapter) mContainer.getViewPager().getAdapter()).setData(Integer.valueOf(mContainer.currentPageId));
        } else {
            this.items.clear();
            this.items.addAll(this.allItems);
            this.items.remove(Integer.valueOf(otherContainer.currentPageId));
            this.items.remove(Integer.valueOf(otherContainer2.currentPageId));
            ((MetricsPagerAdapter) mContainer.getViewPager().getAdapter()).setData(this.items);
            mContainer.getViewPager().setCurrentItem(mContainer.resetPosition(this.items.indexOf(Integer.valueOf(mContainer.currentPageId)), this.items.size()), false);
        }
        resetRideMetricTexts();
    }

    public void setEnabled(boolean enabled) {
        this.mContainer1.getViewPager().getAdapter().notifyDataSetChanged();
        this.mContainer2.getViewPager().getAdapter().notifyDataSetChanged();
        this.mContainer3.getViewPager().getAdapter().notifyDataSetChanged();
        boolean adapterChanged = false;
        if (!enabled) {
            adapterChanged = true;
            if (this.mContainer1.isEnabled()) {
                ((MetricsPagerAdapter) this.mContainer1.getViewPager().getAdapter()).setData(Integer.valueOf(this.mContainer1.currentPageId));
            }
            if (this.mContainer2.isEnabled()) {
                ((MetricsPagerAdapter) this.mContainer2.getViewPager().getAdapter()).setData(Integer.valueOf(this.mContainer2.currentPageId));
            }
            if (this.mContainer3.isEnabled()) {
                ((MetricsPagerAdapter) this.mContainer3.getViewPager().getAdapter()).setData(Integer.valueOf(this.mContainer3.currentPageId));
            }
            resetRideMetricTexts();
        }
        if (this.mContainer1.isEnabled() != enabled) {
            this.mContainer1.setEnabled(enabled);
        }
        if (this.mContainer2.isEnabled() != enabled) {
            this.mContainer2.setEnabled(enabled);
        }
        if (this.mContainer3.isEnabled() != enabled) {
            this.mContainer3.setEnabled(enabled);
        }
        if (adapterChanged) {
            setLabels();
            updateUnitSystem();
        }
    }

    public void setLongPressEnabled(boolean longPressEnabled) {
        this.mContainer1.setLongPressEnabled(longPressEnabled);
        this.mContainer2.setLongPressEnabled(longPressEnabled);
        this.mContainer3.setLongPressEnabled(longPressEnabled);
    }

    public static void resetRideMetricTexts() {
        Set<Integer> keys = cachedValues.keySet();
        for (Integer key : keys) {
            multiRideMetricLabelManager.setText(cachedValues.get(key), key.intValue());
        }
    }

    public void overrideSelection(RideScreenMetric rideScreenMetric, int containerIndex) {
        PagerContainer container = getContainer(containerIndex);
        if (container != null) {
            if (!container.isEnabled()) {
                ((MetricsPagerAdapter) container.getViewPager().getAdapter()).setData(Integer.valueOf(rideScreenMetric.ordinal()));
            }
            container.overrideSelection(rideScreenMetric.ordinal());
            setLabels();
            updateUnitSystem();
        }
    }

    public void resetSelections() {
        resetSelection(0);
        resetSelection(1);
        resetSelection(2);
        setLabels();
        updateUnitSystem();
    }

    public void resetSelection(int containerIndex) {
        PagerContainer container = getContainer(containerIndex);
        if (container != null) {
            if (!container.isEnabled()) {
                ((MetricsPagerAdapter) container.getViewPager().getAdapter()).setData(Integer.valueOf(container.getSelectedId()));
            }
            container.resetSelection();
        }
    }

    private PagerContainer getContainer(int containerIndex) {
        switch (containerIndex) {
            case 0:
                return this.mContainer1;
            case 1:
                return this.mContainer2;
            case 2:
                return this.mContainer3;
            default:
                return null;
        }
    }
}
