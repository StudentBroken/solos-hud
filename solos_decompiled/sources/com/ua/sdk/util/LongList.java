package com.ua.sdk.util;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public class LongList implements Parcelable {
    static final int MIN_CAPACITY_INCREMENT = 12;
    long[] array;
    int size;
    static final long[] EMPTY = new long[0];
    public static final Parcelable.Creator<LongList> CREATOR = new Parcelable.Creator<LongList>() { // from class: com.ua.sdk.util.LongList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LongList createFromParcel(Parcel source) {
            return new LongList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LongList[] newArray(int size) {
            return new LongList[size];
        }
    };

    public LongList() {
        this.array = EMPTY;
        this.size = 0;
    }

    public LongList(int capacity) {
        this.array = new long[capacity];
        this.size = 0;
    }

    public LongList(long[] array) {
        if (array != null) {
            this.array = array;
            this.size = array.length;
        } else {
            this.array = EMPTY;
            this.size = 0;
        }
    }

    public long set(int index, long value) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        long[] array = this.array;
        long result = array[index];
        array[index] = value;
        return result;
    }

    public long get(int index) {
        if (index < 0 || index > this.size) {
            throwIndexOutOfBoundsException(index, this.size);
        }
        return this.array[index];
    }

    public int getSize() {
        return this.size;
    }

    public void add(long value) {
        if (this.size == this.array.length) {
            long[] array = this.array;
            int length = array.length;
            long[] newArray = new long[length < 12 ? length + 12 : length * 2];
            System.arraycopy(array, 0, newArray, 0, this.size);
            this.array = newArray;
        }
        long[] jArr = this.array;
        int i = this.size;
        this.size = i + 1;
        jArr[i] = value;
    }

    public void clear() {
        this.size = 0;
    }

    public long[] toArray() {
        long[] copy = new long[this.size];
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
        dest.writeLongArray(this.array);
        dest.writeInt(this.size);
    }

    private LongList(Parcel in) {
        this.array = in.createLongArray();
        this.size = in.readInt();
    }
}
