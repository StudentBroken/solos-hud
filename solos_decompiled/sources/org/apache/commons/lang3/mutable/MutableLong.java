package org.apache.commons.lang3.mutable;

/* JADX INFO: loaded from: classes51.dex */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {
    private static final long serialVersionUID = 62986528375L;
    private long value;

    public MutableLong() {
    }

    public MutableLong(long value) {
        this.value = value;
    }

    public MutableLong(Number value) {
        this.value = value.longValue();
    }

    public MutableLong(String value) throws NumberFormatException {
        this.value = Long.parseLong(value);
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    /* JADX INFO: renamed from: getValue */
    public Number getValue2() {
        return Long.valueOf(this.value);
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(Number value) {
        this.value = value.longValue();
    }

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public void add(long operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.longValue();
    }

    public void subtract(long operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.longValue();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public Long toLong() {
        return Long.valueOf(longValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableLong) && this.value == ((MutableLong) obj).longValue();
    }

    public int hashCode() {
        return (int) (this.value ^ (this.value >>> 32));
    }

    @Override // java.lang.Comparable
    public int compareTo(MutableLong other) {
        long anotherVal = other.value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
