package com.ua.sdk.activitytype;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.UaLog;
import com.ua.sdk.util.Convert;
import com.ua.sdk.util.Utility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutMetsSpeed implements Parcelable {
    public static Parcelable.Creator<WorkoutMetsSpeed> CREATOR = new Parcelable.Creator<WorkoutMetsSpeed>() { // from class: com.ua.sdk.activitytype.WorkoutMetsSpeed.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutMetsSpeed createFromParcel(Parcel source) {
            return new WorkoutMetsSpeed(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutMetsSpeed[] newArray(int size) {
            return new WorkoutMetsSpeed[size];
        }
    };
    private Double mMetersPerSec;
    private Double mMets;
    private Double mMph;

    public WorkoutMetsSpeed() {
    }

    public WorkoutMetsSpeed(Double mps, Double mets) {
        this.mMph = mps != null ? Convert.meterPerSecToMilePerHour(mps) : null;
        this.mMetersPerSec = mps != null ? Double.valueOf(mps.doubleValue()) : null;
        this.mMets = mets != null ? Double.valueOf(mets.doubleValue()) : null;
    }

    public Double getMets() {
        if (this.mMets != null) {
            return Double.valueOf(this.mMets.doubleValue());
        }
        return null;
    }

    public Double getSpeed() {
        if (this.mMetersPerSec != null) {
            return Double.valueOf(this.mMetersPerSec.doubleValue());
        }
        return null;
    }

    public Double getSpeedMilesPerHour() {
        if (this.mMph != null) {
            return Double.valueOf(this.mMph.doubleValue());
        }
        return null;
    }

    public boolean isValid() {
        return (this.mMph == null || this.mMets == null) ? false : true;
    }

    public void setMets(Double mets) {
        this.mMets = mets != null ? Double.valueOf(mets.doubleValue()) : null;
    }

    public void setSpeed(Double mph) {
        this.mMph = mph != null ? Double.valueOf(mph.doubleValue()) : null;
        this.mMetersPerSec = mph != null ? Convert.milePerHourToMeterPerSecond(mph) : null;
    }

    public static List<WorkoutMetsSpeed> parseSpeedList(String metsSpeed) {
        List<WorkoutMetsSpeed> list = new ArrayList<>();
        if (!Utility.isEmpty(metsSpeed)) {
            try {
                JSONTokener tokener = new JSONTokener(metsSpeed);
                Object obj = tokener.nextValue();
                if (obj instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) obj;
                    Iterator<String> keys = jsonObj.keys();
                    if (keys.hasNext()) {
                        do {
                            String key = keys.next();
                            Double mets = Double.valueOf(Double.parseDouble(key));
                            Double mps = Double.valueOf(Double.parseDouble(jsonObj.getString(key)));
                            if (mps != null && mets != null) {
                                WorkoutMetsSpeed add = new WorkoutMetsSpeed(mps, mets);
                                if (add.isValid()) {
                                    list.add(add);
                                }
                            }
                        } while (keys.hasNext());
                    }
                }
                Collections.sort(list, new Comparable());
            } catch (NumberFormatException e) {
                UaLog.error("Expected Number Value : %s\n, %s", metsSpeed, e);
                list.clear();
            } catch (JSONException e2) {
                UaLog.error("Malformed JSON", (Throwable) e2);
                list.clear();
            }
        }
        return list;
    }

    public static class Comparable implements Comparator<WorkoutMetsSpeed> {
        @Override // java.util.Comparator
        public int compare(WorkoutMetsSpeed ms1, WorkoutMetsSpeed ms2) {
            Double s1 = ms1.getSpeedMilesPerHour();
            Double s2 = ms2.getSpeedMilesPerHour();
            if (s1 == null && s2 == null) {
                return 0;
            }
            if (s2 == null) {
                return 1;
            }
            if (s1 == null) {
                return -1;
            }
            if (s1.doubleValue() > s2.doubleValue()) {
                return 1;
            }
            return s1 != s2 ? -1 : 0;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mMph);
        dest.writeValue(this.mMetersPerSec);
        dest.writeValue(this.mMets);
    }

    private WorkoutMetsSpeed(Parcel in) {
        this.mMph = (Double) in.readValue(Double.class.getClassLoader());
        this.mMetersPerSec = (Double) in.readValue(Double.class.getClassLoader());
        this.mMets = (Double) in.readValue(Double.class.getClassLoader());
    }
}
