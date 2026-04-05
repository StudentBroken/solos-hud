package com.kopin.solos.pages;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.MetricData;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.pages.PageHelper;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.virtualworkout.VirtualCoach;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes37.dex */
public class PageNav {
    private static PageHelper helper;
    private static PageNav self;
    private static TemplateManager templates;
    private static AppService vcapp;
    private Context mContext;
    private PageMode pageMode = PageMode.SINGLE;
    private List<MetricType> metricPageList = new ArrayList();
    private int index = 0;
    private List<Integer> doublePageIds = new ArrayList();
    private boolean initialised = false;

    public enum PageMode {
        IDLE,
        PAUSED,
        NORMAL,
        SINGLE,
        DOUBLE,
        GHOST,
        FUNCTIONAL_THRESHOLD_POWER,
        NAVIGATION,
        COUNTDOWN,
        FWFLASH,
        TRAINING
    }

    private PageNav(Context context) {
        this.mContext = context;
    }

    public static void init(Context context, AppService app, TemplateManager templateManager) {
        self = new PageNav(context);
        vcapp = app;
        helper = new PageHelper(self);
        helper.setTemplateManager(templateManager);
        templates = templateManager;
    }

    public static int[] loadPages() {
        helper.refreshDoublePage(self.mContext);
        return PageHelper.VIEWS;
    }

    public static void checkCurrentPage() {
        String mLastPage = getCurrentPageId();
        switch (self.pageMode) {
            case DOUBLE:
                if (mLastPage.startsWith(AppService.TWO_VIEW)) {
                    PageHelper.MetricTuple tuple = getMetrics(mLastPage);
                    if (!self.isMetricAvailable(tuple.metric1) || !self.isMetricAvailable(tuple.metric2)) {
                        String nextPage = getCurrentPageId();
                        Bundle extra = new Bundle();
                        extra.putBoolean(AppService.NO_REFRESH, true);
                        vcapp.gotoPage(PageHelper.convertPage(nextPage), nextPage, extra);
                        return;
                    }
                }
                break;
            case NORMAL:
            case SINGLE:
            case IDLE:
            case PAUSED:
            case COUNTDOWN:
            case GHOST:
            case FUNCTIONAL_THRESHOLD_POWER:
                if (!self.isMetricAvailable(mLastPage)) {
                    String nextPage2 = getNextPageId();
                    if (!nextPage2.equals(mLastPage)) {
                        vcapp.gotoPage(PageHelper.convertPage(nextPage2), nextPage2, null);
                        return;
                    }
                }
                break;
            case NAVIGATION:
                String nextPage3 = Navigator.getScreenToShow();
                if (!mLastPage.equals(nextPage3)) {
                    showPage(nextPage3);
                    return;
                }
                break;
            case TRAINING:
                String nextPage4 = VirtualCoach.getCurrentHeadsetPageId();
                if (!mLastPage.equals(nextPage4)) {
                    showPage(nextPage4);
                    return;
                }
                break;
        }
        if (mLastPage.startsWith(AppService.TWO_VIEW)) {
            PageHelper.MetricTuple tuple2 = getMetrics(mLastPage);
            templates.updateMetric(tuple2.metric1, false);
            templates.updateMetric(tuple2.metric2, false);
            return;
        }
        templates.updateMetric(mLastPage, false);
    }

    public static boolean hasAutoChange() {
        switch (self.pageMode) {
            case NAVIGATION:
            case TRAINING:
                return false;
            default:
                return Prefs.hasAutoScreenChange();
        }
    }

    public static void nextPage() {
        String nextPage = getNextPageId();
        vcapp.tryGotoPage(PageHelper.convertPage(nextPage), nextPage, null);
    }

    public static void prevPage() {
        String prevPage = getPrevPageId();
        vcapp.tryGotoPage(PageHelper.convertPage(prevPage), prevPage, null);
    }

    private static void showMultiPage(String pageId) {
        String foundPageId = helper.cycleToMultiPage(pageId);
        if (foundPageId == null) {
            foundPageId = helper.cycleToMultiPage(PageHelper.MULTI_METRIC_FIRST_PAGE);
        }
        vcapp.tryGotoPage(PageHelper.convertPage(foundPageId), foundPageId, null);
    }

    private static void showMultiPageWithMetric(String metric) {
        String foundPageId = helper.findMultiPage(metric);
        if (foundPageId == null) {
            foundPageId = helper.cycleToMultiPage(PageHelper.MULTI_METRIC_FIRST_PAGE);
        }
        vcapp.tryGotoPage(PageHelper.convertPage(foundPageId), foundPageId, null);
    }

    public static void showPage(String pageId) {
        if (self.pageMode == PageMode.DOUBLE) {
            if (pageId.startsWith(AppService.TWO_VIEW)) {
                showMultiPage(pageId);
                return;
            } else {
                showMultiPageWithMetric(pageId);
                return;
            }
        }
        if (self.pageMode == PageMode.FUNCTIONAL_THRESHOLD_POWER) {
            showMultiPage(PageHelper.MULTI_METRIC_FIRST_PAGE);
            return;
        }
        int index = findPageId(pageId);
        if (index >= 0) {
            self.index = index;
            if (!vcapp.tryGotoPage(PageHelper.convertPage(pageId), pageId, null)) {
                Log.e("PageNav", "Couldn't show page: " + pageId);
            }
        }
    }

    public static String getCurrentPageId() {
        return self.isSinglePages() ? self.getCurrent().getResource() : helper.getCurrentPageId(self.mContext);
    }

    public static String getNextPageId() {
        return self.getNextPageId(false);
    }

    public static String getPrevPageId() {
        return self.getNextPageId(true);
    }

    private String getNextPageId(boolean reverseOrder) {
        switch (this.pageMode) {
            case DOUBLE:
            case FUNCTIONAL_THRESHOLD_POWER:
                return helper.getNextAvailableMultiPageId(reverseOrder);
            case NORMAL:
            default:
                return "home";
            case SINGLE:
            case PAUSED:
            case GHOST:
                return getAvailableNextPageId(reverseOrder);
            case IDLE:
                return "home";
            case COUNTDOWN:
                return MetricType.COUNTDOWN.getResource();
            case NAVIGATION:
            case TRAINING:
                return getTimePageId();
        }
    }

    private String getAvailableNextPageId(boolean reverseOrder) {
        if (!LiveRide.isNavigtionRideMode() || !Prefs.navigationIsFullScreen()) {
            if (reverseOrder) {
                prev();
            } else {
                next();
            }
            int maxSkip = size();
            while (getCurrent() != null && !helper.getAvailabilitySingle(getCurrent().getResource())) {
                if (reverseOrder) {
                    prev();
                } else {
                    next();
                }
                int maxSkip2 = maxSkip - 1;
                if (maxSkip <= 0) {
                    return helper.homeOrTimePage();
                }
                maxSkip = maxSkip2;
            }
        }
        return (size() <= 0 || getCurrent() == null) ? helper.homeOrTimePage() : getCurrent().getResource();
    }

    static int findPageId(String pageId) {
        for (int i = 0; i < self.size(); i++) {
            if (self.metricPageList.get(i).getResource().contentEquals(pageId)) {
                return i;
            }
        }
        return -1;
    }

    public static String getTimePageId() {
        switch (self.pageMode) {
            case DOUBLE:
            case FUNCTIONAL_THRESHOLD_POWER:
                self.reset();
                return PageHelper.MULTI_METRIC_FIRST_PAGE;
            case NAVIGATION:
                return Navigator.getScreenToShow();
            case TRAINING:
                return VirtualCoach.getCurrentHeadsetPageId();
            default:
                self.reset();
                return getCurrentPageId();
        }
    }

    public static PageHelper.MetricTuple getMetrics(String pageId) {
        return helper.getMetrics(pageId);
    }

    public static MetricData getMetricData(String pageId) {
        return helper.getMetricData(pageId);
    }

    public static PageBoxInfo getPageBoxInfo(String metricId) {
        return helper.getMetricData(self.mContext, metricId);
    }

    public static void showCountdown(boolean onOrOff) {
        refreshPages(onOrOff ? PageMode.COUNTDOWN : PageMode.NORMAL);
        showPage(getCurrentPageId());
    }

    public static void refreshPages(PageMode forMode) {
        if (forMode == PageMode.NORMAL) {
            if (!LiveRide.isStarted()) {
                forMode = PageMode.IDLE;
            } else if (LiveRide.isPaused()) {
                forMode = PageMode.PAUSED;
            } else if (LiveRide.isGhostWorkout()) {
                forMode = PageMode.GHOST;
            } else if (LiveRide.isVirtualWorkout() && LiveRide.getMode() == Workout.RideMode.TRAINING) {
                forMode = PageMode.TRAINING;
            } else if (LiveRide.isFunctionalThresholdPowerMode()) {
                forMode = PageMode.FUNCTIONAL_THRESHOLD_POWER;
            } else if (LiveRide.isNavigtionRideMode()) {
                forMode = PageMode.NAVIGATION;
            } else {
                forMode = Prefs.isSingleMetrics() ? PageMode.SINGLE : PageMode.DOUBLE;
            }
        }
        switch (forMode) {
            case DOUBLE:
                self.initDouble();
                break;
            case NORMAL:
            case IDLE:
            default:
                self.metricPageList.clear();
                self.metricPageList.add(0, MetricType.HOME);
                self.metricPageList.add(MetricType.RIDE_STATS);
                break;
            case SINGLE:
                self.initSingle();
                break;
            case PAUSED:
                self.metricPageList.clear();
                self.metricPageList.add(MetricType.PAUSED_PAGE);
                self.metricPageList.add(MetricType.RIDE_STATS);
                break;
            case COUNTDOWN:
                self.metricPageList.clear();
                self.metricPageList.add(0, MetricType.COUNTDOWN);
                break;
            case GHOST:
                self.initGhost();
                break;
            case FUNCTIONAL_THRESHOLD_POWER:
                self.initFTP();
                break;
            case NAVIGATION:
                self.initNavigation();
                break;
            case TRAINING:
                self.initTraining();
                break;
            case FWFLASH:
                self.initFwFlash();
                break;
        }
        self.index = 0;
        self.pageMode = forMode;
    }

    private void initSingle() {
        this.metricPageList.clear();
        this.metricPageList.addAll(Prefs.getSingleMetricChoices());
        this.metricPageList.add(0, MetricType.TIME);
    }

    private void initDouble() {
        this.metricPageList.clear();
        this.metricPageList.addAll(Prefs.getDoubleChoices());
        fillDoubleMetrics();
        helper.refreshDoublePage(self.mContext);
        reloadDoublePages(this.doublePageIds);
    }

    private void initGhost() {
        this.metricPageList.clear();
        this.metricPageList.addAll(Prefs.getSingleMetricGhostChoices());
        this.metricPageList.add(0, MetricType.TIME);
        this.metricPageList.add(MetricType.GHOST_STATS);
    }

    private void initTraining() {
        this.metricPageList.clear();
        this.metricPageList.add(MetricType.TRAINING_NEXT_STEP);
        this.metricPageList.add(MetricType.TRAINING_STEP);
        this.metricPageList.add(MetricType.TRAINING_STEP_2);
        this.metricPageList.add(MetricType.TRAINING_MANUAL_STEP);
        this.metricPageList.add(MetricType.TRAINING_MANUAL_STEP_2);
    }

    private void initNavigation() {
        this.metricPageList.clear();
        this.metricPageList.add(MetricType.NAVIGATION_NO_COMPASS);
        this.metricPageList.add(MetricType.NAVIGATION);
        this.metricPageList.add(MetricType.COMPASS);
        this.metricPageList.add(MetricType.NAVIGATION_DESTINATION_REACHED);
        this.metricPageList.add(MetricType.NAVIGATION_WITH_MAP);
    }

    private void initFwFlash() {
        this.metricPageList.clear();
        this.metricPageList.add(MetricType.FWFLASH);
    }

    private void initFTP() {
        this.metricPageList.clear();
        this.metricPageList.add(MetricType.COUNTDOWN);
        if (SensorsConnector.isAllowedType(Sensor.DataType.POWER)) {
            this.metricPageList.add(MetricType.AVERAGE_POWER);
        } else if (SensorsConnector.isAllowedType(Sensor.DataType.KICK)) {
            this.metricPageList.add(MetricType.AVERAGE_KICK);
        } else {
            this.metricPageList.add(MetricType.POWER_NORMALISED);
        }
        this.doublePageIds.clear();
        this.doublePageIds.add(Integer.valueOf(R.xml.two_view_metric_metric));
        helper.refreshDoublePage(self.mContext);
        reloadDoublePages(this.doublePageIds);
    }

    private void reloadDoublePages(List<Integer> pageIds) {
        ArrayList<String> pages = new ArrayList<>();
        int i = 0;
        Iterator<Integer> it = pageIds.iterator();
        while (it.hasNext()) {
            int pageId = it.next().intValue();
            if (pageId > 0) {
                pages.add("two_view_" + (i + 1));
            }
            i++;
        }
        vcapp.addPages(pages, pageIds);
    }

    private void fillDoubleMetrics() {
        this.doublePageIds.clear();
        for (int i = 0; i < this.metricPageList.size(); i += 2) {
            int j = i + 1;
            if (i < 6 && j < this.metricPageList.size()) {
                if (this.metricPageList.get(i).isTarget()) {
                    this.doublePageIds.add(Integer.valueOf(this.metricPageList.get(j).isTarget() ? R.xml.two_view_target_target : R.xml.two_view_target_metric));
                } else {
                    this.doublePageIds.add(Integer.valueOf(this.metricPageList.get(j).isTarget() ? R.xml.two_view_metric_target : R.xml.two_view_metric_metric));
                }
            }
        }
    }

    public PageMode getPageMode() {
        return this.pageMode;
    }

    public boolean isSinglePages() {
        return (this.pageMode == PageMode.DOUBLE || this.pageMode == PageMode.FUNCTIONAL_THRESHOLD_POWER) ? false : true;
    }

    public int size() {
        return this.metricPageList.size();
    }

    public void reset() {
        this.index = 0;
    }

    private String getAvailableNextPageId() {
        next();
        int maxSkip = size();
        while (getCurrent() != null && !helper.getAvailabilitySingle(getCurrent().getResource())) {
            next();
            int maxSkip2 = maxSkip - 1;
            if (maxSkip <= 0) {
                return helper.homeOrTimePage();
            }
            maxSkip = maxSkip2;
        }
        return (size() <= 0 || getCurrent() == null) ? helper.homeOrTimePage() : getCurrent().getResource();
    }

    public void next() {
        this.index++;
        if (this.index >= size()) {
            this.index = 0;
        }
        if (this.pageMode == PageMode.DOUBLE) {
            this.index++;
            if (this.index >= size()) {
                this.index = 0;
            }
        }
    }

    public void prev() {
        if (this.index == 0) {
            this.index = size();
        }
        this.index--;
        if (this.pageMode == PageMode.DOUBLE) {
            if (this.index == 0) {
                this.index = size();
            }
            this.index--;
        }
    }

    public static boolean isPageAvailable(String pageId) {
        return self.isMetricAvailable(pageId);
    }

    private boolean isMetricAvailable(String metricId) {
        if (metricId == null) {
            return false;
        }
        if (this.pageMode == PageMode.COUNTDOWN) {
            return metricId.equals(MetricType.COUNTDOWN.getResource());
        }
        if (LiveRide.isStarted() || metricId.equals("home") || metricId.equals(MetricType.RIDE_STATS.getResource()) || metricId.equals(MetricType.GHOST_STATS.getResource())) {
            return templates.isMetricAvailable(metricId);
        }
        return false;
    }

    public MetricType getCurrent() {
        if (this.index < size()) {
            return this.metricPageList.get(this.index);
        }
        return null;
    }

    public DoubleMetric getCurrentDouble() {
        int i = this.index + 1;
        return new DoubleMetric(getCurrent(), i < size() ? this.metricPageList.get(i) : null);
    }

    public int getIndex() {
        return this.index;
    }

    public int getDoubleIndex() {
        return this.index / 2;
    }

    public static List<MetricType> getAllMetrics() {
        return self.metricPageList;
    }

    public static List<String> getAllMetricResources() {
        List<String> list = new ArrayList<>();
        for (MetricType metricType : self.metricPageList) {
            if (metricType != null) {
                list.add(metricType.getResource());
            }
        }
        return list;
    }

    public static HashMap<String, String> getAvailableMetricsMap() {
        String name;
        HashMap<String, String> list = new HashMap<>();
        for (MetricType metricType : self.metricPageList) {
            if (metricType != null && (name = metricType.getMetricName(self.mContext)) != null) {
                list.put(metricType.getResource(), name);
            }
        }
        return list;
    }

    public static List<MetricType> getMetricChoices() {
        switch (LiveRide.getMode()) {
            case GHOST_RIDE:
                return Prefs.getSingleMetricGhostChoices();
            case TRAINING:
                return getTrainingMetrics();
            default:
                if (Prefs.isSingleMetrics()) {
                    return Prefs.getSingleMetricChoices();
                }
                return Prefs.getDoubleChoices();
        }
    }

    public static List<MetricType> getTargetMetrics() {
        return getTrainingMetrics();
    }

    private static List<MetricType> getTrainingMetrics() {
        return new ArrayList<MetricType>() { // from class: com.kopin.solos.pages.PageNav.1
            {
                add(MetricType.AVERAGE_TARGET_CADENCE);
                add(MetricType.AVERAGE_TARGET_STEP);
                add(MetricType.AVERAGE_TARGET_HEARTRATE);
                add(MetricType.AVERAGE_TARGET_SPEED);
                add(MetricType.AVERAGE_TARGET_POWER);
                add(MetricType.AVERAGE_TARGET_PACE);
                add(MetricType.AVERAGE_TARGET_KICK);
            }
        };
    }

    public static List<Integer> getDoublePageIds() {
        self.fillDoubleMetrics();
        return new ArrayList(self.doublePageIds);
    }

    public class DoubleMetric {
        public MetricType metricType1;
        public MetricType metricType2;

        public DoubleMetric(MetricType metricType1, MetricType metricType2) {
            this.metricType1 = MetricType.NONE;
            this.metricType2 = MetricType.NONE;
            this.metricType1 = metricType1;
            this.metricType2 = metricType2;
        }
    }
}
