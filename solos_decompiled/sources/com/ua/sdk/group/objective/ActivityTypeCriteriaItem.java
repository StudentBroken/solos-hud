package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityTypeCriteriaItem implements CriteriaItem<Integer> {
    public static final Parcelable.Creator<ActivityTypeCriteriaItem> CREATOR = new Parcelable.Creator<ActivityTypeCriteriaItem>() { // from class: com.ua.sdk.group.objective.ActivityTypeCriteriaItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeCriteriaItem createFromParcel(Parcel source) {
            return new ActivityTypeCriteriaItem(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityTypeCriteriaItem[] newArray(int size) {
            return new ActivityTypeCriteriaItem[size];
        }
    };
    private Integer value;

    public ActivityTypeCriteriaItem() {
    }

    private ActivityTypeCriteriaItem(Parcel in) {
        this.value = Integer.valueOf(in.readInt());
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public String getName() {
        return CriteriaTypes.ACTIVITY_TYPE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.group.objective.CriteriaItem
    public Integer getValue() {
        return this.value;
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.value.intValue());
    }
}
