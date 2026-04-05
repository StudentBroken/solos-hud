package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class WorkoutListImpl extends AbstractEntityList<Workout> implements WorkoutList {
    public static final Parcelable.Creator<WorkoutListImpl> CREATOR = new Parcelable.Creator<WorkoutListImpl>() { // from class: com.ua.sdk.workout.WorkoutListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutListImpl createFromParcel(Parcel source) {
            return new WorkoutListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkoutListImpl[] newArray(int size) {
            return new WorkoutListImpl[size];
        }
    };

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "workouts";
    }

    public WorkoutListImpl() {
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.AbstractEntityList, com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private WorkoutListImpl(Parcel in) {
        super(in);
    }
}
