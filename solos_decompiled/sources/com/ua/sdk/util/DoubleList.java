package com.ua.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class DoubleList implements Parcelable {
    static final int MIN_CAPACITY_INCREMENT = 12;
    double[] array;
    int size;
    static final double[] EMPTY = new double[0];
    public static final Parcelable.Creator<DoubleList> CREATOR = new Parcelable.Creator<DoubleList>() { // from class: com.ua.sdk.util.DoubleList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DoubleList createFromParcel(Parcel source) {
            return new DoubleList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DoubleList[] newArray(int size) {
            return new DoubleList[size];
        }
    };

    public DoubleList() {
        this.array = EMPTY;
        this.size = 0;
    }

    public DoubleList(int capacity) {
        this.array = new double[capacity];
        this.size = 0;
    }

    public DoubleList(double[] array) {
        if (array != null) {
            this.array = array;
            this.size = array.length;
        } else {
            this.array = EMPTY;
            this.size = 0;
        }
    }

    public double set(int index, double value) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        double[] array = this.array;
        double result = array[index];
        array[index] = value;
        return result;
    }

    public double get(int index) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        return this.array[index];
    }

    public int getSize() {
        return this.size;
    }

    public void add(double value) {
        if (this.size == this.array.length) {
            double[] array = this.array;
            int length = array.length;
            double[] newArray = new double[length < 12 ? length + 12 : length * 2];
            System.arraycopy(array, 0, newArray, 0, this.size);
            this.array = newArray;
        }
        double[] dArr = this.array;
        int i = this.size;
        this.size = i + 1;
        dArr[i] = value;
    }

    public void clear() {
        this.size = 0;
    }

    public double[] toArray() {
        double[] copy = new double[this.size];
        System.arraycopy(this.array, 0, copy, 0, this.size);
        return copy;
    }

    static IndexOutOfBoundsException throwIndexOutOfBoundsException(int index, int size) {
        throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + size);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(this.array);
        dest.writeInt(this.size);
    }

    private DoubleList(Parcel in) {
        this.array = in.createDoubleArray();
        this.size = in.readInt();
    }
}
