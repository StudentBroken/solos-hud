package com.ua.sdk.actigraphysettings;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.Reference;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphySettingsImpl implements ActigraphySettings, Parcelable {
    public static Parcelable.Creator<ActigraphySettingsImpl> CREATOR = new Parcelable.Creator<ActigraphySettingsImpl>() { // from class: com.ua.sdk.actigraphysettings.ActigraphySettingsImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphySettingsImpl createFromParcel(Parcel source) {
            return new ActigraphySettingsImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphySettingsImpl[] newArray(int size) {
            return new ActigraphySettingsImpl[size];
        }
    };
    private List<String> activityPriority;
    private List<String> sleepPriority;

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettings
    public List<String> getSleepRecorderPriorities() {
        return this.sleepPriority;
    }

    @Override // com.ua.sdk.actigraphysettings.ActigraphySettings
    public List<String> getActivityRecorderPriorities() {
        return this.activityPriority;
    }

    public void setActivityPriority(List<String> activityPriority) {
        this.activityPriority = activityPriority;
    }

    public void setSleepPriority(List<String> sleepPriority) {
        this.sleepPriority = sleepPriority;
    }

    @Override // com.ua.sdk.Resource
    public Reference getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.activityPriority);
        dest.writeStringList(this.sleepPriority);
    }

    public ActigraphySettingsImpl() {
    }

    private ActigraphySettingsImpl(Parcel in) {
        in.readStringList(this.activityPriority);
        in.readStringList(this.sleepPriority);
    }
}
