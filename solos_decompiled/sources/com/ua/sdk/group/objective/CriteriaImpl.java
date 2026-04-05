package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes65.dex */
public class CriteriaImpl implements Criteria {
    public static final Parcelable.Creator<CriteriaImpl> CREATOR = new Parcelable.Creator<CriteriaImpl>() { // from class: com.ua.sdk.group.objective.CriteriaImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CriteriaImpl createFromParcel(Parcel source) {
            return new CriteriaImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CriteriaImpl[] newArray(int size) {
            return new CriteriaImpl[size];
        }
    };
    Map<String, CriteriaItem> criteria;

    public CriteriaImpl() {
        this.criteria = new HashMap(2);
    }

    private CriteriaImpl(Parcel in) {
        this.criteria = new HashMap(2);
        in.readMap(this.criteria, CriteriaItem.class.getClassLoader());
    }

    @Override // com.ua.sdk.group.objective.Criteria
    public void addCriteriaItem(CriteriaItem item) {
        this.criteria.put(item.getName(), item);
    }

    @Override // com.ua.sdk.group.objective.Criteria
    public CriteriaItem getCriteriaItem(String name) {
        return this.criteria.get(name);
    }

    @Override // com.ua.sdk.group.objective.Criteria
    public void removeAllItems() {
        this.criteria.clear();
    }

    @Override // com.ua.sdk.group.objective.Criteria
    public CriteriaItem removeItem(String name) {
        return this.criteria.remove(name);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(this.criteria);
    }
}
