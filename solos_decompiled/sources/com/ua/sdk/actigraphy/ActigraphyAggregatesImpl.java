package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyAggregatesImpl implements ActigraphyAggregates, Parcelable {
    public static Parcelable.Creator<ActigraphyAggregatesImpl> CREATOR = new Parcelable.Creator<ActigraphyAggregatesImpl>() { // from class: com.ua.sdk.actigraphy.ActigraphyAggregatesImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyAggregatesImpl createFromParcel(Parcel source) {
            return new ActigraphyAggregatesImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyAggregatesImpl[] newArray(int size) {
            return new ActigraphyAggregatesImpl[size];
        }
    };
    private AggregateValueImpl mActiveTime;
    private AggregateValueImpl mBodyMass;
    private AggregateValueImpl mDistance;
    private AggregateValueImpl mEnergyBurned;
    private AggregateValueImpl mSleep;
    private AggregateValueImpl mSteps;

    protected ActigraphyAggregatesImpl() {
    }

    protected ActigraphyAggregatesImpl(AggregateValueImpl distance, AggregateValueImpl bodyMass, AggregateValueImpl activeTime, AggregateValueImpl energyBurned, AggregateValueImpl sleep, AggregateValueImpl steps) {
        this.mDistance = distance;
        this.mBodyMass = bodyMass;
        this.mActiveTime = activeTime;
        this.mEnergyBurned = energyBurned;
        this.mSleep = sleep;
        this.mSteps = steps;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getDistance() {
        return this.mDistance;
    }

    public void setDistance(AggregateValueImpl distance) {
        this.mDistance = distance;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getBodyMass() {
        return this.mBodyMass;
    }

    public void setBodyMass(AggregateValueImpl bodyMass) {
        this.mBodyMass = bodyMass;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getActiveTime() {
        return this.mActiveTime;
    }

    public void setActiveTime(AggregateValueImpl activeTime) {
        this.mActiveTime = activeTime;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getEnergyBurned() {
        return this.mEnergyBurned;
    }

    public void setEnergyBurned(AggregateValueImpl energyBurned) {
        this.mEnergyBurned = energyBurned;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getSleep() {
        return this.mSleep;
    }

    public void setSleep(AggregateValueImpl sleep) {
        this.mSleep = sleep;
    }

    @Override // com.ua.sdk.actigraphy.ActigraphyAggregates
    public AggregateValueImpl getSteps() {
        return this.mSteps;
    }

    public void setSteps(AggregateValueImpl steps) {
        this.mSteps = steps;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mDistance, flags);
        dest.writeParcelable(this.mBodyMass, flags);
        dest.writeParcelable(this.mActiveTime, flags);
        dest.writeParcelable(this.mEnergyBurned, flags);
        dest.writeParcelable(this.mSleep, flags);
        dest.writeParcelable(this.mSteps, flags);
    }

    private ActigraphyAggregatesImpl(Parcel in) {
        this.mDistance = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mBodyMass = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mActiveTime = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mEnergyBurned = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mSleep = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
        this.mSteps = (AggregateValueImpl) in.readParcelable(AggregateValueImpl.class.getClassLoader());
    }
}
