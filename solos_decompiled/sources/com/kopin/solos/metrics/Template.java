package com.kopin.solos.metrics;

import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.core.R;
import com.kopin.solos.metrics.TemplateManager;
import com.kopin.solos.pages.PageBoxInfo;
import com.kopin.solos.pages.PageHelper;
import com.kopin.solos.pages.PageNav;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.MetricType;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.views.Graph;
import java.lang.Number;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;

/* JADX INFO: loaded from: classes37.dex */
public abstract class Template<T extends Number> {
    protected AppService mAppService;
    private final TemplateManager.DataType mDataType;
    TemplateManager mTemplateManager;
    protected final String TAG = getClass().getSimpleName();
    protected Graph mGraph = null;
    HashSet<String> mPages = new HashSet<>();
    private String mUnit = "";
    private String mLabel = "";
    private String mImage = "";
    private boolean mBound = false;

    public abstract boolean isAvailable(T t);

    protected abstract void onServiceConnected(HardwareReceiverService hardwareReceiverService);

    protected abstract void onServiceDisconnected();

    protected abstract void updateBackground(T t);

    protected abstract String updatePage(PageBoxInfo pageBoxInfo, T t);

    public Template(AppService appService, TemplateManager.DataType dataType) {
        this.mAppService = appService;
        this.mDataType = dataType;
        Type templateClass = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (!dataType.TYPE.equals(templateClass)) {
            throw new RuntimeException(dataType.name() + " requires " + dataType.TYPE + " type, not " + templateClass);
        }
    }

    protected void addPage(String pageId) {
        this.mPages.add(pageId);
        if (this.mTemplateManager != null) {
            this.mTemplateManager.refreshTemplate(this);
        }
    }

    protected void addPage(MetricType dt) {
        this.mPages.add(dt.getResource());
        if (this.mTemplateManager != null) {
            this.mTemplateManager.refreshTemplate(this);
        }
    }

    protected void updateElement(String page, String element, String attribute, Object value) {
        this.mAppService.updateElement(page, element, attribute, value);
    }

    public boolean isSupported() {
        return this.mDataType.isSupported();
    }

    public boolean containsMetric(String metric) {
        return this.mPages.contains(metric);
    }

    public final boolean isBound() {
        return this.mBound;
    }

    public String getUnit() {
        return this.mUnit;
    }

    public TemplateManager.DataType getDataType() {
        return this.mDataType;
    }

    public void setUnit(String unit) {
        this.mUnit = unit;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String label) {
        this.mLabel = label;
    }

    public String getImage() {
        return this.mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public void setImage(MetricResource metricResource) {
        this.mImage = metricResource.name();
    }

    protected boolean before(T value) {
        return false;
    }

    protected void prepare() {
    }

    protected void graphAdjust() {
        if (this.mGraph != null) {
            this.mGraph.adjust();
        }
    }

    protected void graphUpdate(String pageId, String graphId) {
        if (this.mGraph != null) {
            this.mAppService.addBitmap(graphId, this.mGraph.getBitmap());
            this.mAppService.updateElement(pageId, graphId, "source_changed", true);
        }
    }

    protected void start() {
    }

    protected void stop() {
    }

    void setService(HardwareReceiverService service) {
        if (service != null) {
            this.mBound = true;
            onServiceConnected(service);
        } else {
            this.mBound = false;
            onServiceDisconnected();
        }
    }

    protected String updatePageNeutral(PageBoxInfo pageBoxInfo) {
        return "----";
    }

    void update(T value, boolean sensorValue, boolean force) {
        if (!before(value)) {
            if (sensorValue) {
                updateBackground(value);
                if (!force) {
                    return;
                }
            }
            if (this.mAppService.isOpened()) {
                boolean isAvailable = isAvailable(value);
                if (isAvailable || force) {
                    for (String page : this.mPages) {
                        PageBoxInfo pageBoxInfo = PageNav.getPageBoxInfo(page);
                        if (pageBoxInfo != null) {
                            if (isAvailable) {
                                update(pageBoxInfo, value);
                            } else {
                                updateNeutral(pageBoxInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    private void update(PageBoxInfo pageBoxInfo, T value) {
        updatePage(pageBoxInfo, updatePage(pageBoxInfo, value));
    }

    private void updateNeutral(PageBoxInfo pageBoxInfo) {
        updatePage(pageBoxInfo, updatePageNeutral(pageBoxInfo));
        if (pageBoxInfo.isMatch(MetricType.AVERAGE_TARGET_SPEED, MetricType.AVERAGE_TARGET_HEARTRATE, MetricType.AVERAGE_TARGET_POWER, MetricType.AVERAGE_TARGET_CADENCE, MetricType.AVERAGE_TARGET_PACE, MetricType.AVERAGE_TARGET_KICK, MetricType.AVERAGE_TARGET_STEP)) {
            this.mAppService.updateElement(pageBoxInfo.page, pageBoxInfo.value, "color", Integer.valueOf(this.mAppService.getResources().getColor(R.color.behind_color)));
        }
    }

    private void updatePage(PageBoxInfo pageBoxInfo, String contentValue) {
        String page = PageHelper.convertPage(pageBoxInfo.page);
        this.mAppService.updateElement(page, pageBoxInfo.value, "content", contentValue);
        this.mAppService.updateElement(page, pageBoxInfo.unit, "content", getUnit());
        this.mAppService.updateElement(page, "label", "content", getLabel());
        this.mAppService.updateElement(page, "label", "source", getImage());
        this.mAppService.updateElement(page, "metric_image", "source", getImage());
        updateTopBar(pageBoxInfo);
        this.mAppService.updateElements(page);
    }

    public void updateTopBar(PageBoxInfo pageBoxInfo) {
        this.mAppService.addBitmap(AppService.BATTERY_LEVEL_ICON, Utility.getBitmapForDensity(this.mAppService.getApplicationContext(), this.mAppService.getBatteryDrawable(), 160));
        this.mAppService.updateElement(pageBoxInfo.page, "time", "content", Utility.formatTimeForPage());
        this.mAppService.updateGroupComStatus(pageBoxInfo.page);
    }

    public String toString() {
        Type templateClass = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return "{" + getClass().getSimpleName() + "<" + templateClass + "> " + this.mPages + "}";
    }

    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
    }
}
