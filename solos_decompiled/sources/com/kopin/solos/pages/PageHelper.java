package com.kopin.solos.pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.MetricData;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes37.dex */
public class PageHelper {
    public static final String MULTI_METRIC_FIRST_PAGE = "two_view_1";
    public static final String PAGE_FORMAT = "two_view_%d";
    private static final String TAG = "PageHelper";
    private static final String TARGET_1 = "target1";
    private static final String TARGET_2 = "target2";
    private static final String TIME_PAGE_ID = "time";
    private static final String UNIT = "unit";
    private static final String UNIT_1 = "unit1";
    private static final String UNIT_2 = "unit2";
    private static final String VALUE = "value";
    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    static final int[] VIEWS = {R.xml.home, R.xml.time, R.xml.paused, R.xml.countdown_time, R.xml.target_distance, R.xml.page_target_distance, R.xml.heartrate_graph, R.xml.heart_rate_zones, R.xml.intensity_factor, R.xml.power_bar, R.xml.lap_graph, R.xml.average_speed, R.xml.elevation_lap_graph, R.xml.power_normalised, R.xml.countdown, R.xml.two_view_target_target, R.xml.single_metric, R.xml.single_metric_ghost, R.xml.common_average_target, R.xml.common_graph, R.xml.navigation, R.xml.navigation_no_compass, R.xml.compass, R.xml.navigation_destination_reached, R.xml.navigation_map, R.xml.ride_stats, R.xml.ghost_stats, R.xml.fwflash, R.xml.training_next_step, R.xml.training_step, R.xml.training_step_2, R.xml.training_manual_step, R.xml.training_manual_step_2, R.xml.quad_metric};
    private final List<DoublePage> mDoublePages = new ArrayList();
    private boolean mIsGhostRide = false;
    private PageNav mPageNav;
    private TemplateManager mTemplateManager;

    PageHelper(PageNav nav) {
        this.mPageNav = nav;
    }

    private int getRefreshViewPortion(MetricType pagePart1, MetricType pagePart2) {
        if (pagePart1.isTarget() && pagePart2.isTarget()) {
            return R.xml.two_view_target_target;
        }
        if (pagePart1.isTarget()) {
            return R.xml.two_view_target_metric;
        }
        if (pagePart2.isTarget()) {
            return R.xml.two_view_metric_target;
        }
        return R.xml.two_view_metric_metric;
    }

    String homeOrTimePage() {
        return LiveRide.isStarted() ? TIME_PAGE_ID : "home";
    }

    public void setIsGhostRide(boolean isGhostRide) {
        this.mIsGhostRide = isGhostRide;
    }

    private String getPersistedValueExtra(Context context, String key) {
        if (key == null || key.isEmpty()) {
            return "";
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> prefs = sharedPref.getAll();
        Object o = prefs.get(key);
        if (o != null) {
            String val = o instanceof Float ? "" + sharedPref.getFloat(key, 0.0f) : "";
            if (o instanceof Integer) {
                val = val + sharedPref.getInt(key, 0);
            }
            if (o instanceof Long) {
                val = val + Utility.toTime(sharedPref.getLong(key, 0L), false);
            }
            if (o instanceof String) {
                return val + sharedPref.getString(key, "");
            }
            return val;
        }
        return "";
    }

    private int setupPage(Context context, String page, String pageId, String key1, String key2) {
        String page1_2 = Prefs.getPageOption(key2);
        String title1_1 = Utility.getPageTitle(context, key1);
        String title1_2 = Utility.getPageTitle(context, key2);
        MetricType metricType1 = MetricType.getMetricType(page);
        MetricType metricType2 = MetricType.getMetricType(page1_2);
        String target1 = getPersistedValueExtra(context, metricType1.getKey());
        String target2 = getPersistedValueExtra(context, metricType2.getKey());
        DoublePage page1 = new DoublePage(this, page, page1_2, pageId, title1_1, title1_2, target1, target2);
        this.mDoublePages.add(page1);
        page1.getPageId();
        return getRefreshViewPortion(metricType1, metricType2);
    }

    void refreshDoublePage(Context context) {
        this.mDoublePages.clear();
        if (LiveRide.isFunctionalThresholdPowerMode()) {
            MetricType pwr = MetricType.POWER_NORMALISED;
            if (SensorsConnector.isAllowedType(Sensor.DataType.POWER)) {
                pwr = MetricType.AVERAGE_POWER;
            } else if (SensorsConnector.isAllowedType(Sensor.DataType.KICK)) {
                pwr = MetricType.AVERAGE_KICK;
            }
            DoublePage page1 = new DoublePage(this, MetricType.TARGET_TIME, pwr, MULTI_METRIC_FIRST_PAGE, "" + LiveRide.getTargetTime(), "");
            this.mDoublePages.add(page1);
            page1.getPageId();
            return;
        }
        for (int i = 1; i <= 3; i++) {
            String key1 = Prefs.getMultiScreenOptionKey(i, 1);
            String key2 = Prefs.getMultiScreenOptionKey(i, 2);
            String pageKey = String.format(Locale.US, PAGE_FORMAT, Integer.valueOf(i));
            checkDoublePage(context, key1, key2, Prefs.getMultiScreenKey(i), pageKey);
        }
    }

    private void checkDoublePage(Context context, String key, String key2, String multiScreen, String pageId) {
        String page = Prefs.getPageOption(key);
        if (Prefs.isMultiMetricPageEnabled(multiScreen) && !page.isEmpty()) {
            setupPage(context, page, pageId, key, key2);
        }
    }

    PageBoxInfo getMetricData(Context context, String metricId) {
        if (metricId.isEmpty()) {
            return null;
        }
        if (metricId.equals("home") || metricId.equals(AppService.PAUSED_PAGE)) {
            return new PageBoxInfo(metricId, "value", UNIT);
        }
        if (this.mPageNav.isSinglePages() && this.mPageNav.size() > 0 && this.mPageNav.getCurrent() != null && metricId.equals(this.mPageNav.getCurrent().getResource())) {
            return new PageBoxInfo(metricId, "value", UNIT);
        }
        if (this.mPageNav.isSinglePages() || this.mDoublePages.size() <= this.mPageNav.getDoubleIndex()) {
            return null;
        }
        DoublePage doublePage = this.mDoublePages.get(this.mPageNav.getDoubleIndex());
        if (doublePage.hasMetric(metricId)) {
            return doublePage.getMetricId(metricId);
        }
        return null;
    }

    MetricTuple getMetrics(String pageId) {
        for (DoublePage page : this.mDoublePages) {
            if (page.pageId.equals(pageId)) {
                return page.getMetrics();
            }
        }
        return MetricTuple.EMPTY;
    }

    MetricData getMetricData(String pageId) {
        for (DoublePage page : this.mDoublePages) {
            if (page.pageId.equals(pageId)) {
                return page.getData();
            }
        }
        return null;
    }

    public String getCurrentPageId(Context context) {
        if (!Prefs.isReady()) {
            return "home";
        }
        if (Navigator.isNavScreenShowing()) {
            return Navigator.getScreenToShow();
        }
        if (!LiveRide.isStarted()) {
            return getAvailabilitySingle(MetricType.TIME.getResource()) ? MetricType.TIME.getResource() : "home";
        }
        if (LiveRide.isPaused()) {
            return AppService.PAUSED_PAGE;
        }
        if (this.mPageNav.isSinglePages()) {
            if (this.mPageNav.size() == 0) {
                return homeOrTimePage();
            }
            return this.mPageNav.getCurrent().getResource();
        }
        if (this.mDoublePages.size() <= 0) {
            refreshDoublePage(context);
        }
        if (this.mDoublePages.size() == 0) {
            return homeOrTimePage();
        }
        return this.mDoublePages.get(this.mPageNav.getDoubleIndex()).getPageId();
    }

    public String getNextAvailableMultiPageId(boolean reverseOrder) {
        if (this.mDoublePages.size() == 0) {
            return homeOrTimePage();
        }
        if (reverseOrder) {
            this.mPageNav.prev();
        } else {
            this.mPageNav.next();
        }
        int maxSkip = 3;
        while (!this.mDoublePages.get(this.mPageNav.getDoubleIndex()).isAvailable()) {
            if (reverseOrder) {
                this.mPageNav.prev();
            } else {
                this.mPageNav.next();
            }
            int maxSkip2 = maxSkip - 1;
            if (maxSkip <= 0) {
                return homeOrTimePage();
            }
            maxSkip = maxSkip2;
        }
        return this.mDoublePages.get(this.mPageNav.getDoubleIndex()).getPageId();
    }

    public String cycleToMultiPage(String pageId) {
        DoublePage page = this.mDoublePages.get(this.mPageNav.getDoubleIndex());
        for (int maxSkip = 3; maxSkip > 0; maxSkip--) {
            if (!pageId.contentEquals(page.getPageId())) {
                this.mPageNav.next();
                DoublePage page2 = this.mDoublePages.get(this.mPageNav.getDoubleIndex());
                page = page2;
            } else {
                return pageId;
            }
        }
        return null;
    }

    String findMultiPage(String metric) {
        DoublePage page = this.mDoublePages.get(this.mPageNav.getDoubleIndex());
        for (int maxSkip = 3; maxSkip > 0; maxSkip--) {
            if (page.hasMetric(metric)) {
                return page.getPageId();
            }
            this.mPageNav.next();
            DoublePage page2 = this.mDoublePages.get(this.mPageNav.getDoubleIndex());
            page = page2;
        }
        return null;
    }

    public void setTemplateManager(TemplateManager templateManager) {
        this.mTemplateManager = templateManager;
    }

    boolean getAvailabilitySingle(String pageId) {
        boolean availability1 = this.mTemplateManager != null && this.mTemplateManager.isMetricAvailable(pageId);
        boolean availability2 = getAvailabilityMulti(pageId);
        Log.d(TAG, "getAvailabilitySingle " + pageId + " : " + (availability1 && availability2));
        return availability1 && availability2;
    }

    boolean getAvailabilityMulti(String pageId) {
        return this.mTemplateManager != null && this.mTemplateManager.isMetricAvailable(pageId);
    }

    public static class PageDef {
        String name;
        int resId;

        PageDef(int res) {
            this(null, res);
        }

        PageDef(String n, int res) {
            this.name = n;
            this.resId = res;
        }
    }

    private class DoublePage {
        boolean inFront1;
        boolean inFront2;
        String metric1;
        String metric2;
        PageBoxInfo pageBoxInfo1;
        PageBoxInfo pageBoxInfo2;
        String pageId;
        String target1;
        String target2;
        String title;

        public DoublePage(PageHelper pageHelper, MetricType metric1, MetricType metric2, String pageId, String target1, String target2) {
            this(pageHelper, metric1.getResource(), metric2.getResource(), pageId, metric1.name(), metric2.name(), target1, target2);
        }

        public DoublePage(PageHelper pageHelper, String metric1, String metric2, String pageId, String title1, String title2, String target1, String target2) {
            this(metric1, metric2, pageId, title1, title2, target1, target2, false, false);
        }

        public DoublePage(String metric1, String metric2, String pageId, String title1, String title2, String target1, String target2, boolean inFront1, boolean inFront2) {
            this.title = "";
            Log.w("DoublePage", metric1 + "," + metric2 + "," + pageId + "," + title1 + "," + title2 + ", " + target1 + ", " + target2);
            this.metric1 = metric1;
            this.metric2 = metric2;
            this.pageId = pageId;
            if (title1 != null && title2 != null) {
                this.title = title1 + " / " + title2;
            } else if (title1 != null) {
                this.title = title1;
            } else if (title2 != null) {
                this.title = title2;
            }
            this.target1 = target1;
            this.target2 = target2;
            this.inFront1 = inFront1;
            this.inFront2 = inFront2;
        }

        String getPageId() {
            this.pageBoxInfo1 = new PageBoxInfo(this.pageId, this.metric1, PageHelper.VALUE_1, PageHelper.UNIT_1, PageHelper.TARGET_1);
            this.pageBoxInfo2 = new PageBoxInfo(this.pageId, this.metric2, PageHelper.VALUE_2, PageHelper.UNIT_2, PageHelper.TARGET_2);
            return this.pageId;
        }

        PageBoxInfo getMetricId(String metricId) {
            if (this.pageBoxInfo1 != null && this.metric1.equals(metricId)) {
                return this.pageBoxInfo1;
            }
            if (this.pageBoxInfo2 != null && this.metric2.equals(metricId)) {
                return this.pageBoxInfo2;
            }
            return null;
        }

        public MetricTuple getMetrics() {
            return new MetricTuple(this.metric1, this.metric2, this.title, this.target1, this.target2);
        }

        public MetricData getData() {
            MetricData data = new MetricData(this.title, this.metric1, this.metric2, this.target1, this.target2);
            if (this.metric1 != null) {
                data.setMetricType1(MetricType.getMetricType(this.metric1));
            }
            if (this.metric2 != null) {
                data.setMetricType2(MetricType.getMetricType(this.metric2));
            }
            return data;
        }

        public String getTitle() {
            return this.title;
        }

        public boolean hasMetric(String metric) {
            return metric.equals(this.metric1) || metric.equals(this.metric2) || metric.equals(this.target1) || metric.equals(this.target2);
        }

        boolean isAvailable() {
            return PageHelper.this.getAvailabilityMulti(this.metric1) || PageHelper.this.getAvailabilityMulti(this.metric2);
        }

        public String toString() {
            return "DoublePage (" + this.metric1 + ", " + this.metric2 + ", " + this.pageId + ", " + this.title + ", " + this.pageBoxInfo1 + ", " + this.pageBoxInfo2 + ")";
        }
    }

    public static class MetricTuple {
        public static final MetricTuple EMPTY = new MetricTuple("", "", "", "", "");
        public String metric1;
        public String metric2;
        public String target1;
        public String target2;
        public String title;

        private MetricTuple(String metric1, String metric2, String title, String target1, String target2) {
            this.metric1 = metric1;
            this.metric2 = metric2;
            this.title = title;
            this.target1 = target1;
            this.target2 = target2;
        }

        public String toString() {
            return "MetricTuple(" + this.metric1 + ", " + this.metric2 + ", " + this.title + ")";
        }
    }

    public static String convertPage(String pageName) {
        if (pageName != null) {
            if (pageName.endsWith("_average_target")) {
                return "common_average_target";
            }
            if (pageName.contains("ghost") && !pageName.contains(TIME_PAGE_ID) && !pageName.contains("_stats")) {
                return "single_metric_ghost";
            }
            if (pageName.equals("cadence") || pageName.equals(BaseDataTypes.ID_DISTANCE) || pageName.equals("heartrate") || pageName.equals("power") || pageName.equals("oxygenation") || pageName.equals(BaseDataTypes.ID_SPEED) || pageName.equals("pace") || pageName.equals("step") || pageName.equals("stride") || pageName.equals("kick")) {
                return LiveRide.isGhostWorkout() ? "single_metric_ghost" : "single_metric";
            }
            if (pageName.equals(BaseDataTypes.ID_ELEVATION) || pageName.equals("elevation_change") || pageName.equals("calories") || pageName.equals("power_normalised") || pageName.equals("overall_climb")) {
                return "single_metric";
            }
            if (pageName.endsWith("_graph")) {
                return LiveRide.isGhostWorkout() ? "single_metric_ghost" : "common_graph";
            }
            if (pageName.equals("kick_bar")) {
                return "power_bar";
            }
            if (pageName.equals("metric_overview")) {
                return "quad_metric";
            }
            return pageName;
        }
        return pageName;
    }
}
