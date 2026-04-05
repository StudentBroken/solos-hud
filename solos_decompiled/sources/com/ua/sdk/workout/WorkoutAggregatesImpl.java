package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.IntensityCalculator;
import com.ua.sdk.datapoint.BaseDataTypes;
import com.ua.sdk.heartrate.HeartRateZones;
import com.ua.sdk.internal.IntensityCalculatorImpl;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutAggregatesImpl implements WorkoutAggregates {
    public static final Parcelable.Creator<WorkoutAggregatesImpl> CREATOR = new Parcelable.Creator<WorkoutAggregatesImpl>() { // from class: com.ua.sdk.workout.WorkoutAggregatesImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutAggregatesImpl createFromParcel(Parcel source) {
            return new WorkoutAggregatesImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutAggregatesImpl[] newArray(int size) {
            return new WorkoutAggregatesImpl[size];
        }
    };

    @SerializedName("active_time_total")
    Double activeTimeTotal;

    @SerializedName("cadence_avg")
    Integer cadenceAvg;

    @SerializedName("cadence_max")
    Integer cadenceMax;

    @SerializedName("cadence_min")
    Integer cadenceMin;

    @SerializedName("distance_total")
    Double distanceTotal;

    @SerializedName("elapsed_time_total")
    Double elapsedTimeTotal;

    @SerializedName("heartrate_avg")
    Integer heartRateAvg;

    @SerializedName("heartrate_max")
    Integer heartRateMax;

    @SerializedName("heartrate_min")
    Integer heartRateMin;
    transient IntensityCalculator intensityCalculator;

    @SerializedName("metabolic_energy_total")
    Double metabolicEnergyTotal;

    @SerializedName("power_avg")
    Double powerAvg;

    @SerializedName("power_max")
    Double powerMax;

    @SerializedName("power_min")
    Double powerMin;

    @SerializedName("speed_avg")
    Double speedAvg;

    @SerializedName("speed_max")
    Double speedMax;

    @SerializedName("speed_min")
    Double speedMin;

    @SerializedName("steps_total")
    Integer stepsTotal;

    @SerializedName("torque_avg")
    Double torqueAvg;

    @SerializedName("torque_max")
    Double torqueMax;

    @SerializedName("torque_min")
    Double torqueMin;

    @SerializedName(BaseDataTypes.ID_WILLPOWER)
    Double willPower;

    public WorkoutAggregatesImpl() {
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getHeartRateMin() {
        return this.heartRateMin;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getHeartRateMax() {
        return this.heartRateMax;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getHeartRateAvg() {
        return this.heartRateAvg;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getIntensityMin(HeartRateZones heartRateZones) {
        if (this.heartRateMin == null || heartRateZones == null) {
            return null;
        }
        if (this.intensityCalculator == null) {
            this.intensityCalculator = new IntensityCalculatorImpl();
        }
        return Double.valueOf(this.intensityCalculator.calculateCurrentIntensity(heartRateZones, this.heartRateMin.intValue()));
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getIntensityMax(HeartRateZones heartRateZones) {
        if (this.heartRateMax == null || heartRateZones == null) {
            return null;
        }
        if (this.intensityCalculator == null) {
            this.intensityCalculator = new IntensityCalculatorImpl();
        }
        return Double.valueOf(this.intensityCalculator.calculateCurrentIntensity(heartRateZones, this.heartRateMax.intValue()));
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getIntensityAvg(HeartRateZones heartRateZones) {
        if (this.heartRateAvg == null || heartRateZones == null) {
            return null;
        }
        if (this.intensityCalculator == null) {
            this.intensityCalculator = new IntensityCalculatorImpl();
        }
        return Double.valueOf(this.intensityCalculator.calculateCurrentIntensity(heartRateZones, this.heartRateAvg.intValue()));
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getSpeedMin() {
        return this.speedMin;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getSpeedMax() {
        return this.speedMax;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getSpeedAvg() {
        return this.speedAvg;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getCadenceMin() {
        return this.cadenceMin;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getCadenceMax() {
        return this.cadenceMax;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getCadenceAvg() {
        return this.cadenceAvg;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getPowerMin() {
        return this.powerMin;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getPowerMax() {
        return this.powerMax;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getPowerAvg() {
        return this.powerAvg;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getTorqueMin() {
        return this.torqueMin;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getTorqueMax() {
        return this.torqueMax;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getTorqueAvg() {
        return this.torqueAvg;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getWillPower() {
        return this.willPower;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getDistanceTotal() {
        return this.distanceTotal;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getMetabolicEnergyTotal() {
        return this.metabolicEnergyTotal;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getActiveTimeTotal() {
        return this.activeTimeTotal;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Double getElapsedTimeTotal() {
        return this.elapsedTimeTotal;
    }

    @Override // com.ua.sdk.workout.WorkoutAggregates
    public Integer getStepsTotal() {
        return this.stepsTotal;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.heartRateMin);
        dest.writeValue(this.heartRateMax);
        dest.writeValue(this.heartRateAvg);
        dest.writeValue(this.speedMin);
        dest.writeValue(this.speedMax);
        dest.writeValue(this.speedAvg);
        dest.writeValue(this.cadenceMin);
        dest.writeValue(this.cadenceMax);
        dest.writeValue(this.cadenceAvg);
        dest.writeValue(this.powerMin);
        dest.writeValue(this.powerMax);
        dest.writeValue(this.powerAvg);
        dest.writeValue(this.torqueMin);
        dest.writeValue(this.torqueMax);
        dest.writeValue(this.torqueAvg);
        dest.writeValue(this.willPower);
        dest.writeValue(this.distanceTotal);
        dest.writeValue(this.metabolicEnergyTotal);
        dest.writeValue(this.activeTimeTotal);
        dest.writeValue(this.elapsedTimeTotal);
        dest.writeValue(this.stepsTotal);
    }

    private WorkoutAggregatesImpl(Parcel in) {
        this.heartRateMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.heartRateMax = (Integer) in.readValue(Integer.class.getClassLoader());
        this.heartRateAvg = (Integer) in.readValue(Integer.class.getClassLoader());
        this.speedMin = (Double) in.readValue(Double.class.getClassLoader());
        this.speedMax = (Double) in.readValue(Double.class.getClassLoader());
        this.speedAvg = (Double) in.readValue(Double.class.getClassLoader());
        this.cadenceMin = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cadenceMax = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cadenceAvg = (Integer) in.readValue(Integer.class.getClassLoader());
        this.powerMin = (Double) in.readValue(Double.class.getClassLoader());
        this.powerMax = (Double) in.readValue(Double.class.getClassLoader());
        this.powerAvg = (Double) in.readValue(Double.class.getClassLoader());
        this.torqueMin = (Double) in.readValue(Double.class.getClassLoader());
        this.torqueMax = (Double) in.readValue(Double.class.getClassLoader());
        this.torqueAvg = (Double) in.readValue(Double.class.getClassLoader());
        this.willPower = (Double) in.readValue(Double.class.getClassLoader());
        this.distanceTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.metabolicEnergyTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.activeTimeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.elapsedTimeTotal = (Double) in.readValue(Double.class.getClassLoader());
        this.stepsTotal = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkoutAggregatesImpl that = (WorkoutAggregatesImpl) o;
        if (this.activeTimeTotal == null ? that.activeTimeTotal != null : !this.activeTimeTotal.equals(that.activeTimeTotal)) {
            return false;
        }
        if (this.cadenceAvg == null ? that.cadenceAvg != null : !this.cadenceAvg.equals(that.cadenceAvg)) {
            return false;
        }
        if (this.cadenceMax == null ? that.cadenceMax != null : !this.cadenceMax.equals(that.cadenceMax)) {
            return false;
        }
        if (this.cadenceMin == null ? that.cadenceMin != null : !this.cadenceMin.equals(that.cadenceMin)) {
            return false;
        }
        if (this.distanceTotal == null ? that.distanceTotal != null : !this.distanceTotal.equals(that.distanceTotal)) {
            return false;
        }
        if (this.elapsedTimeTotal == null ? that.elapsedTimeTotal != null : !this.elapsedTimeTotal.equals(that.elapsedTimeTotal)) {
            return false;
        }
        if (this.heartRateAvg == null ? that.heartRateAvg != null : !this.heartRateAvg.equals(that.heartRateAvg)) {
            return false;
        }
        if (this.heartRateMax == null ? that.heartRateMax != null : !this.heartRateMax.equals(that.heartRateMax)) {
            return false;
        }
        if (this.heartRateMin == null ? that.heartRateMin != null : !this.heartRateMin.equals(that.heartRateMin)) {
            return false;
        }
        if (this.metabolicEnergyTotal == null ? that.metabolicEnergyTotal != null : !this.metabolicEnergyTotal.equals(that.metabolicEnergyTotal)) {
            return false;
        }
        if (this.powerAvg == null ? that.powerAvg != null : !this.powerAvg.equals(that.powerAvg)) {
            return false;
        }
        if (this.powerMax == null ? that.powerMax != null : !this.powerMax.equals(that.powerMax)) {
            return false;
        }
        if (this.powerMin == null ? that.powerMin != null : !this.powerMin.equals(that.powerMin)) {
            return false;
        }
        if (this.speedAvg == null ? that.speedAvg != null : !this.speedAvg.equals(that.speedAvg)) {
            return false;
        }
        if (this.speedMax == null ? that.speedMax != null : !this.speedMax.equals(that.speedMax)) {
            return false;
        }
        if (this.speedMin == null ? that.speedMin != null : !this.speedMin.equals(that.speedMin)) {
            return false;
        }
        if (this.stepsTotal == null ? that.stepsTotal != null : !this.stepsTotal.equals(that.stepsTotal)) {
            return false;
        }
        if (this.torqueAvg == null ? that.torqueAvg != null : !this.torqueAvg.equals(that.torqueAvg)) {
            return false;
        }
        if (this.torqueMax == null ? that.torqueMax != null : !this.torqueMax.equals(that.torqueMax)) {
            return false;
        }
        if (this.torqueMin == null ? that.torqueMin != null : !this.torqueMin.equals(that.torqueMin)) {
            return false;
        }
        if (this.willPower != null) {
            if (this.willPower.equals(that.willPower)) {
                return true;
            }
        } else if (that.willPower == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.heartRateMin != null ? this.heartRateMin.hashCode() : 0;
        return (((((((((((((((((((((((((((((((((((((((result * 31) + (this.heartRateMax != null ? this.heartRateMax.hashCode() : 0)) * 31) + (this.heartRateAvg != null ? this.heartRateAvg.hashCode() : 0)) * 31) + (this.speedMin != null ? this.speedMin.hashCode() : 0)) * 31) + (this.speedMax != null ? this.speedMax.hashCode() : 0)) * 31) + (this.speedAvg != null ? this.speedAvg.hashCode() : 0)) * 31) + (this.cadenceMin != null ? this.cadenceMin.hashCode() : 0)) * 31) + (this.cadenceMax != null ? this.cadenceMax.hashCode() : 0)) * 31) + (this.cadenceAvg != null ? this.cadenceAvg.hashCode() : 0)) * 31) + (this.powerMin != null ? this.powerMin.hashCode() : 0)) * 31) + (this.powerMax != null ? this.powerMax.hashCode() : 0)) * 31) + (this.powerAvg != null ? this.powerAvg.hashCode() : 0)) * 31) + (this.torqueMin != null ? this.torqueMin.hashCode() : 0)) * 31) + (this.torqueMax != null ? this.torqueMax.hashCode() : 0)) * 31) + (this.torqueAvg != null ? this.torqueAvg.hashCode() : 0)) * 31) + (this.willPower != null ? this.willPower.hashCode() : 0)) * 31) + (this.distanceTotal != null ? this.distanceTotal.hashCode() : 0)) * 31) + (this.metabolicEnergyTotal != null ? this.metabolicEnergyTotal.hashCode() : 0)) * 31) + (this.activeTimeTotal != null ? this.activeTimeTotal.hashCode() : 0)) * 31) + (this.elapsedTimeTotal != null ? this.elapsedTimeTotal.hashCode() : 0)) * 31) + (this.stepsTotal != null ? this.stepsTotal.hashCode() : 0);
    }
}
