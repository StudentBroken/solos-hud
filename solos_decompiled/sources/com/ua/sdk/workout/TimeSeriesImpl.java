package com.ua.sdk.workout;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.workout.BaseTimeSeriesEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes65.dex */
public class TimeSeriesImpl<T extends BaseTimeSeriesEntry> implements TimeSeries<T> {
    public static final Parcelable.Creator<TimeSeriesImpl> CREATOR = new Parcelable.Creator<TimeSeriesImpl>() { // from class: com.ua.sdk.workout.TimeSeriesImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeSeriesImpl createFromParcel(Parcel source) {
            return new TimeSeriesImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TimeSeriesImpl[] newArray(int size) {
            return new TimeSeriesImpl[size];
        }
    };
    List<T> arrayList;

    public TimeSeriesImpl() {
        this.arrayList = new ArrayList();
    }

    protected TimeSeriesImpl(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
    }

    public void add(T object) {
        this.arrayList.add(object);
    }

    public void sort() {
        Collections.sort(this.arrayList);
    }

    @Override // com.ua.sdk.workout.TimeSeries
    public T get(int index) {
        return this.arrayList.get(index);
    }

    @Override // com.ua.sdk.workout.TimeSeries
    public int getSize() {
        return this.arrayList.size();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {
        private int index;

        private MyIterator() {
            this.index = 0;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < TimeSeriesImpl.this.getSize();
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.index < TimeSeriesImpl.this.getSize()) {
                T object = TimeSeriesImpl.this.arrayList.get(this.index);
                this.index++;
                return object;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.arrayList);
    }

    protected TimeSeriesImpl(Parcel in) {
        this.arrayList = new ArrayList();
        in.readList(this.arrayList, BaseTimeSeriesEntry.class.getClassLoader());
    }
}
