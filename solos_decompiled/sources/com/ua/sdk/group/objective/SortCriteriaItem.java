package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class SortCriteriaItem implements CriteriaItem<String> {
    public static final Parcelable.Creator<SortCriteriaItem> CREATOR = new Parcelable.Creator<SortCriteriaItem>() { // from class: com.ua.sdk.group.objective.SortCriteriaItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SortCriteriaItem createFromParcel(Parcel source) {
            return new SortCriteriaItem(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SortCriteriaItem[] newArray(int size) {
            return new SortCriteriaItem[size];
        }
    };
    private String value;

    public enum Value {
        ASC,
        DESC
    }

    public SortCriteriaItem() {
    }

    private SortCriteriaItem(Parcel in) {
        this.value = in.readString();
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public String getName() {
        return CriteriaTypes.SORT;
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public String getValue() {
        return this.value;
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public void setValue(String value) {
        if (value != null) {
            Value[] arr$ = Value.values();
            for (Value val : arr$) {
                if (value.equalsIgnoreCase(val.toString())) {
                    setValue(val);
                    return;
                }
            }
            throw new EnumConstantNotPresentException(Value.class, "Unable to set value: " + value);
        }
    }

    public void setValue(Value value) {
        if (value != null) {
            this.value = value.toString().toLowerCase();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
    }
}
