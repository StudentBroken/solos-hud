package com.ua.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class IntList implements Parcelable {
    static final int MIN_CAPACITY_INCREMENT = 12;
    int[] array;
    int size;
    static final int[] EMPTY = new int[0];
    public static final Parcelable.Creator<IntList> CREATOR = new Parcelable.Creator<IntList>() { // from class: com.ua.sdk.util.IntList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntList createFromParcel(Parcel source) {
            return new IntList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public IntList[] newArray(int size) {
            return new IntList[size];
        }
    };

    public IntList() {
        this.array = EMPTY;
        this.size = 0;
    }

    public IntList(int capacity) {
        this.array = new int[capacity];
        this.size = 0;
    }

    public IntList(int[] array) {
        if (array != null) {
            this.array = array;
            this.size = array.length;
        } else {
            this.array = EMPTY;
            this.size = 0;
        }
    }

    public int set(int index, int value) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        int[] array = this.array;
        int result = array[index];
        array[index] = value;
        return result;
    }

    public int get(int index) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        return this.array[index];
    }

    public int getSize() {
        return this.size;
    }

    public void add(int value) {
        if (this.size == this.array.length) {
            int[] array = this.array;
            int length = array.length;
            int[] newArray = new int[length < 12 ? length + 12 : length * 2];
            System.arraycopy(array, 0, newArray, 0, this.size);
            this.array = newArray;
        }
        int[] iArr = this.array;
        int i = this.size;
        this.size = i + 1;
        iArr[i] = value;
    }

    public void clear() {
        this.size = 0;
    }

    public int[] toArray() {
        int[] copy = new int[this.size];
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
        dest.writeIntArray(this.array);
        dest.writeInt(this.size);
    }

    private IntList(Parcel in) {
        this.array = in.createIntArray();
        this.size = in.readInt();
    }
}
