package org.apache.commons.lang3.mutable;

/* JADX INFO: loaded from: classes51.dex */
public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {
    private static final long serialVersionUID = 1587163916;
    private double value;

    public MutableDouble() {
    }

    public MutableDouble(double value) {
        this.value = value;
    }

    public MutableDouble(Number value) {
        this.value = value.doubleValue();
    }

    public MutableDouble(String value) throws NumberFormatException {
        this.value = Double.parseDouble(value);
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    /* JADX INFO: renamed from: getValue */
    public Number getValue2() {
        return Double.valueOf(this.value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(Number value) {
        this.value = value.doubleValue();
    }

    public boolean isNaN() {
        return Double.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(this.value);
    }

    public void increment() {
        this.value += 1.0d;
    }

    public void decrement() {
        this.value -= 1.0d;
    }

    public void add(double operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.doubleValue();
    }

    public void subtract(double operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.doubleValue();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public Double toDouble() {
        return Double.valueOf(doubleValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableDouble) && Double.doubleToLongBits(((MutableDouble) obj).value) == Double.doubleToLongBits(this.value);
    }

    public int hashCode() {
        long bits = Double.doubleToLongBits(this.value);
        return (int) ((bits >>> 32) ^ bits);
    }

    @Override // java.lang.Comparable
    public int compareTo(MutableDouble other) {
        double anotherVal = other.value;
        return Double.compare(this.value, anotherVal);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
