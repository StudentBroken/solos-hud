package com.kopin.solos.metrics;

import android.location.Location;
import android.util.Log;
import com.kopin.solos.AppService;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.navigation.Navigation;
import com.kopin.solos.sensors.Sensor;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.storage.DataListener;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.TimeHelper;
import com.kopin.solos.update.FwFlash;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes37.dex */
public class TemplateManager {
    public static final String TAG = "TemplateManager";
    private Template<Number> mCountdown;
    private HardwareReceiverService mService;
    private TimeHelper mTimeHelper;
    private ArrayList<Template> mTemplates = new ArrayList<>();
    private HashMap<String, Template> mTemplateMap = new HashMap<>();
    private ArrayList<Template<Double>> mCadenceList = new ArrayList<>();
    private ArrayList<Template<Float>> mElevationList = new ArrayList<>();
    private ArrayList<Template<Integer>> mHeartRateList = new ArrayList<>();
    private ArrayList<Template<Double>> mPowerList = new ArrayList<>();
    private ArrayList<Template<Double>> mSpeedList = new ArrayList<>();
    private ArrayList<Template<Integer>> mOxygenList = new ArrayList<>();
    private ArrayList<Template<Long>> mTimeList = new ArrayList<>();
    private ArrayList<Template<Double>> mStrideList = new ArrayList<>();
    private ArrayList<Template<Double>> mPaceList = new ArrayList<>();
    private ArrayList<Template<Double>> mStepList = new ArrayList<>();
    private ArrayList<Template<Double>> mKickList = new ArrayList<>();
    private DataListener mDataListener = new DataListener() { // from class: com.kopin.solos.metrics.TemplateManager.1
        @Override // com.kopin.solos.storage.DataListener
        public void onSpeed(double mphSpeed) {
            TemplateManager.this.updateSpeed(mphSpeed, true);
            TemplateManager.this.updatePace(Conversion.speedToPace(mphSpeed), true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onCadence(double cadenceRPM) {
            TemplateManager.this.updateCadence(cadenceRPM, true);
            TemplateManager.this.updateStep(cadenceRPM, true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onHeartRate(int heartrate) {
            TemplateManager.this.updateHeartRate(heartrate, true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onBikePower(double power) {
            TemplateManager.this.updatePower(power, true);
            TemplateManager.this.updateKick(power, true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onAltitude(float value) {
            TemplateManager.this.updateElevation(value, true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onLocation(Location location) {
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onButtonPress(Sensor.ButtonAction action) {
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onOxygen(int oxygen) {
            TemplateManager.this.updateOxygen(oxygen, true);
        }

        @Override // com.kopin.solos.storage.DataListener
        public void onStride(double stride) {
            TemplateManager.this.updateStride(stride, true);
        }
    };

    public enum DataType {
        CADENCE(Double.class, Sensor.SensorType.CADENCE),
        ELEVATION(Float.class),
        HEART_RATE(Integer.class, Sensor.SensorType.HEARTRATE),
        POWER(Double.class, Sensor.SensorType.POWER),
        SPEED(Double.class, Sensor.SensorType.SPEED),
        TIME(Long.class),
        VOID(Number.class),
        OXYGEN(Integer.class, Sensor.SensorType.OXYGEN),
        STRIDE(Double.class, Sensor.SensorType.STRIDE),
        STEP(Double.class, Sensor.SensorType.RUNNING_SPEED_CADENCE),
        PACE(Double.class, Sensor.SensorType.RUNNING_SPEED_CADENCE),
        KICK(Double.class, Sensor.SensorType.FOOT_POD),
        DERIVED_POWER(Double.class);

        public final Class TYPE;
        private final Sensor.SensorType sensor;

        DataType(Class clazz) {
            this.TYPE = clazz;
            this.sensor = Sensor.SensorType.UNKNOWN;
        }

        DataType(Class clazz, Sensor.SensorType type) {
            this.TYPE = clazz;
            this.sensor = type;
        }

        public boolean isSupported() {
            return this.sensor == Sensor.SensorType.UNKNOWN || SensorsConnector.isSupportedType(this.sensor);
        }

        public static DataType fromString(String metricName) {
            if (metricName.contains("time")) {
                return TIME;
            }
            if (metricName.contains("cadence")) {
                return CADENCE;
            }
            if (metricName.contains(BaseDataTypes.ID_SPEED)) {
                return SPEED;
            }
            if (metricName.contains("heart")) {
                return HEART_RATE;
            }
            if (metricName.contains("normalised_power")) {
                return DERIVED_POWER;
            }
            if (metricName.contains("power")) {
                return POWER;
            }
            if (metricName.contains("oxygen")) {
                return OXYGEN;
            }
            if (metricName.contains(BaseDataTypes.ID_ELEVATION)) {
                return ELEVATION;
            }
            if (metricName.contains("stride")) {
                return STRIDE;
            }
            if (metricName.contains("pace")) {
                return PACE;
            }
            if (metricName.contains("step")) {
                return STEP;
            }
            if (metricName.contains("intensity_factor")) {
                return DERIVED_POWER;
            }
            if (metricName.contains("kick")) {
                return KICK;
            }
            return VOID;
        }
    }

    public TemplateManager(AppService appService) {
        addTemplate(new AverageCadence(appService));
        addTemplate(new AverageHeartRate(appService));
        addTemplate(new AveragePower(appService));
        addTemplate(new AverageSpeed(appService));
        addTemplate(new Cadence(appService));
        addTemplate(new TargetSpeed(appService));
        addTemplate(new Calories(appService));
        addTemplate(new Climb(appService));
        addTemplate(new Distance(appService));
        addTemplate(new Elevation(appService));
        addTemplate(new ElevationChange(appService));
        addTemplate(new HeartRate(appService));
        addTemplate(new Home(appService));
        addTemplate(new Navigation(appService));
        addTemplate(new IntensityFactor(appService));
        addTemplate(new NormalisedPower(appService));
        addTemplate(new Power(appService));
        addTemplate(new Speed(appService));
        addTemplate(new Oxygen(appService));
        addTemplate(new TargetDistance(appService));
        addTemplate(new TargetTime(appService));
        addTemplate(new Time(appService));
        addTemplate(new Countdown(appService));
        addTemplate(new RideStats(appService));
        addTemplate(new GhostStats(appService));
        addTemplate(new FwFlash(appService));
        addTemplate(new Step(appService));
        addTemplate(new AverageStep(appService));
        addTemplate(new Stride(appService));
        addTemplate(new AverageStride(appService));
        addTemplate(new Pace(appService));
        addTemplate(new AveragePace(appService));
        addTemplate(new PaceGraph(appService));
        addTemplate(new AveragePaceGraph(appService));
        addTemplate(new TrainingWorkout(appService));
        addTemplate(new Kick(appService));
        addTemplate(new AverageKick(appService));
        addTemplate(new MetricOverview(appService));
        updateUnitSystem(Prefs.getUnitSystem());
    }

    private void addTemplate(Template template) {
        this.mTemplates.add(template);
        template.mTemplateManager = this;
        refreshTemplate(template);
        switch (template.getDataType()) {
            case CADENCE:
                this.mCadenceList.add(template);
                break;
            case ELEVATION:
                this.mElevationList.add(template);
                break;
            case HEART_RATE:
                this.mHeartRateList.add(template);
                break;
            case POWER:
                this.mPowerList.add(template);
                break;
            case SPEED:
                this.mSpeedList.add(template);
                break;
            case OXYGEN:
                this.mOxygenList.add(template);
                break;
            case TIME:
                this.mTimeList.add(template);
                break;
            case STRIDE:
                this.mStrideList.add(template);
                break;
            case PACE:
                this.mPaceList.add(template);
                break;
            case STEP:
                this.mStepList.add(template);
                break;
            case KICK:
                this.mKickList.add(template);
                break;
            case DERIVED_POWER:
                this.mPowerList.add(template);
                this.mKickList.add(template);
                break;
        }
        if (template instanceof Countdown) {
            this.mCountdown = template;
        }
    }

    public boolean isMetricAvailable(String metricId) {
        if (metricId == null || metricId.isEmpty()) {
            return false;
        }
        Template template = this.mTemplateMap.get(metricId);
        if (template != null) {
            return isTemplateAvailable(template);
        }
        Log.e(TAG, "Metric <" + metricId + "> not added to any Template!", new Exception("stack trace"));
        return false;
    }

    private boolean isTemplateAvailable(Template template) {
        if (this.mService == null || !template.isSupported()) {
            return false;
        }
        switch (template.getDataType()) {
        }
        return false;
    }

    void refreshTemplate(Template template) {
        for (Object pageObj : template.mPages) {
            String page = pageObj.toString();
            Template temp = this.mTemplateMap.get(page);
            if (temp != null) {
                if (temp != template) {
                    throw new RuntimeException("Both " + temp + " and " + template + " contain page '" + page + "'. which one should update it?");
                }
            } else {
                this.mTemplateMap.put(page, template);
            }
        }
    }

    public void prepare() {
        for (Template template : this.mTemplates) {
            template.prepare();
        }
    }

    public void graphAdjust() {
        for (Template template : this.mTemplates) {
            template.graphAdjust();
        }
    }

    public void start() {
        for (Template template : this.mTemplates) {
            template.start();
        }
    }

    public void stop() {
        for (Template template : this.mTemplates) {
            template.stop();
        }
    }

    public void setService(HardwareReceiverService service) {
        for (Template template : this.mTemplates) {
            template.setService(service);
        }
        if (service != null) {
            service.addDataListener(this.mDataListener);
            this.mTimeHelper = service.getTimeHelper();
        } else if (this.mService != null) {
            this.mService.removeDataListener(this.mDataListener);
        }
        this.mService = service;
    }

    public void updateAll() {
        for (Template template : this.mTemplates) {
            updateTemplate(template, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCadence(double value, boolean sensorData) {
        for (Template<Double> template : this.mCadenceList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStep(double value, boolean sensorData) {
        for (Template<Double> template : this.mStepList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateElevation(float value, boolean sensorData) {
        for (Template<Float> template : this.mElevationList) {
            template.update(Float.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHeartRate(int value, boolean sensorData) {
        for (Template<Integer> template : this.mHeartRateList) {
            template.update(Integer.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePower(double value, boolean sensorData) {
        for (Template<Double> template : this.mPowerList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateKick(double value, boolean sensorData) {
        for (Template<Double> template : this.mKickList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeed(double value, boolean sensorData) {
        for (Template<Double> template : this.mSpeedList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOxygen(int value, boolean sensorData) {
        for (Template<Integer> template : this.mOxygenList) {
            template.update(Integer.valueOf(value), sensorData, false);
        }
    }

    public void updateTime(long value) {
        for (Template<Long> template : this.mTimeList) {
            template.update(Long.valueOf(value), true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateStride(double value, boolean sensorData) {
        for (Template<Double> template : this.mStrideList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePace(double value, boolean sensorData) {
        for (Template<Double> template : this.mPaceList) {
            template.update(Double.valueOf(value), sensorData, false);
        }
    }

    public void updateCountdown(int countdown) {
        this.mCountdown.update(Integer.valueOf(countdown), false, true);
    }

    public void updateMetric(String metricId, boolean force) {
        if (metricId != null && !metricId.isEmpty()) {
            Template template = this.mTemplateMap.get(metricId);
            if (template != null) {
                updateTemplate(template, force);
            } else {
                Log.e(TAG, "Metric <" + metricId + "> not added to any Template!", new Exception("stack trace"));
            }
        }
    }

    private void updateTemplate(Template template, boolean force) {
        if (this.mService != null && template.isSupported()) {
            switch (template.getDataType()) {
                case CADENCE:
                case STEP:
                    template.update(Double.valueOf(this.mService.getLastCadence()), false, force);
                    break;
                case ELEVATION:
                    template.update(Float.valueOf(this.mService.getLastElevation()), false, force);
                    break;
                case HEART_RATE:
                    template.update(Integer.valueOf(this.mService.getLastHeartRate()), false, force);
                    break;
                case POWER:
                case KICK:
                case DERIVED_POWER:
                    template.update(Double.valueOf(this.mService.getLastPower()), false, force);
                    break;
                case SPEED:
                    template.update(Double.valueOf(this.mService.getLastSpeed()), false, force);
                    break;
                case OXYGEN:
                    template.update(Integer.valueOf(this.mService.getLastOxygen()), false, force);
                    break;
                case TIME:
                    template.update(Long.valueOf(this.mTimeHelper.getTime()), false, force);
                    break;
                case STRIDE:
                    template.update(Double.valueOf(this.mService.getLastStride()), false, force);
                    break;
                case PACE:
                    template.update(Double.valueOf(this.mService.getLastPace()), false, force);
                    break;
            }
        }
    }

    public void updateUnitSystem(Prefs.UnitSystem unitSystem) {
        for (Template template : this.mTemplates) {
            template.updateUnitSystem(unitSystem);
        }
    }
}
