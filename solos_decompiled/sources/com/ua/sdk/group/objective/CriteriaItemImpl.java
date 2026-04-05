package com.ua.sdk.group.objective;

import android.os.Parcel;

/* JADX INFO: loaded from: classes65.dex */
public class CriteriaItemImpl implements CriteriaItem<Object> {
    String name;
    private Object value;

    public CriteriaItemImpl() {
    }

    private CriteriaItemImpl(Parcel in) {
        this.value = in.readValue(Object.class.getClassLoader());
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public Object getValue() {
        return this.value;
    }

    @Override // com.ua.sdk.group.objective.CriteriaItem
    public void setValue(Object value) {
        this.value = value;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.value);
    }
}
