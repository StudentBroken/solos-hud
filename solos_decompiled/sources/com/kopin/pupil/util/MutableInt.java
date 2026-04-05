package com.kopin.pupil.util;

/* JADX INFO: loaded from: classes25.dex */
public class MutableInt {
    public int value;

    public MutableInt() {
    }

    public MutableInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(float value) {
        this.value = (int) value;
    }

    public void setValue(long value) {
        this.value = (int) value;
    }

    public void setValue(double value) {
        this.value = (int) value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
