package com.ua.sdk.sleep;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.sleep.SleepMetric;

/* JADX INFO: loaded from: classes65.dex */
public class SleepStateEntry implements Comparable<SleepStateEntry>, Parcelable {
    public static final Parcelable.Creator<SleepStateEntry> CREATOR = new Parcelable.Creator<SleepStateEntry>() { // from class: com.ua.sdk.sleep.SleepStateEntry.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepStateEntry createFromParcel(Parcel source) {
            return new SleepStateEntry(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepStateEntry[] newArray(int size) {
            return new SleepStateEntry[size];
        }
    };
    public final long epoch;
    public final SleepMetric.State state;

    public SleepStateEntry(long epoch, SleepMetric.State state) {
        this.epoch = epoch;
        this.state = state;
    }

    @Override // java.lang.Comparable
    public int compareTo(SleepStateEntry another) {
        if (this.epoch < another.epoch) {
            return -1;
        }
        return this.epoch == another.epoch ? 0 : 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.epoch);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
    }

    private SleepStateEntry(Parcel in) {
        this.epoch = in.readLong();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : SleepMetric.State.values()[tmpState];
    }
}
