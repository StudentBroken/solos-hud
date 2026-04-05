package com.ua.sdk.activitytype;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class AllActivityTypesList implements EntityList<ActivityType>, Parcelable {
    public static final Parcelable.Creator<AllActivityTypesList> CREATOR = new Parcelable.Creator<AllActivityTypesList>() { // from class: com.ua.sdk.activitytype.AllActivityTypesList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AllActivityTypesList createFromParcel(Parcel source) {
            return new AllActivityTypesList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AllActivityTypesList[] newArray(int size) {
            return new AllActivityTypesList[size];
        }
    };
    List<ActivityType> activityTypes;

    public AllActivityTypesList(List activityTypes) {
        this.activityTypes = activityTypes;
    }

    @Override // com.ua.sdk.EntityList
    public int getTotalCount() {
        return this.activityTypes.size();
    }

    @Override // com.ua.sdk.EntityList
    public int getSize() {
        return this.activityTypes.size();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.EntityList
    public ActivityType get(int index) {
        return this.activityTypes.get(index);
    }

    @Override // com.ua.sdk.EntityList
    public List<ActivityType> getAll() {
        return this.activityTypes;
    }

    @Override // com.ua.sdk.EntityList
    public boolean isEmpty() {
        return this.activityTypes.isEmpty();
    }

    @Override // com.ua.sdk.EntityList
    public boolean hasPrevious() {
        return false;
    }

    @Override // com.ua.sdk.EntityList
    public boolean hasNext() {
        return false;
    }

    @Override // com.ua.sdk.EntityList
    public EntityListRef<ActivityType> getPreviousPage() {
        return null;
    }

    @Override // com.ua.sdk.EntityList
    public EntityListRef<ActivityType> getNextPage() {
        return null;
    }

    @Override // com.ua.sdk.EntityList
    public void set(int index, ActivityType object) {
        this.activityTypes.set(index, object);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.ua.sdk.EntityList
    public ActivityType remove(int index) {
        return this.activityTypes.remove(index);
    }

    @Override // com.ua.sdk.Resource
    public EntityListRef<ActivityType> getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.activityTypes);
    }

    private AllActivityTypesList(Parcel in) {
        this.activityTypes = new ArrayList();
        in.readList(this.activityTypes, ActivityType.class.getClassLoader());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AllActivityTypesList that = (AllActivityTypesList) o;
        if (this.activityTypes != null) {
            if (this.activityTypes.equals(that.activityTypes)) {
                return true;
            }
        } else if (that.activityTypes == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.activityTypes != null) {
            return this.activityTypes.hashCode();
        }
        return 0;
    }
}
