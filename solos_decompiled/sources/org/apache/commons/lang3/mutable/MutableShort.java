package org.apache.commons.lang3.mutable;

/* JADX INFO: loaded from: classes51.dex */
public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
    private static final long serialVersionUID = -2135791679;
    private short value;

    public MutableShort() {
    }

    public MutableShort(short value) {
        this.value = value;
    }

    public MutableShort(Number value) {
        this.value = value.shortValue();
    }

    public MutableShort(String value) throws NumberFormatException {
        this.value = Short.parseShort(value);
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    /* JADX INFO: renamed from: getValue, reason: avoid collision after fix types in other method */
    public Number getValue2() {
        return Short.valueOf(this.value);
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(Number value) {
        this.value = value.shortValue();
    }

    public void increment() {
        this.value = (short) (this.value + 1);
    }

    public void decrement() {
        this.value = (short) (this.value - 1);
    }

    public void add(short operand) {
        this.value = (short) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (short) (this.value + operand.shortValue());
    }

    public void subtract(short operand) {
        this.value = (short) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (short) (this.value - operand.shortValue());
    }

    @Override // java.lang.Number
    public short shortValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.value;
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

    public Short toShort() {
        return Short.valueOf(shortValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableShort) && this.value == ((MutableShort) obj).shortValue();
    }

    public int hashCode() {
        return this.value;
    }

    @Override // java.lang.Comparable
    public int compareTo(MutableShort other) {
        short anotherVal = other.value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf((int) this.value);
    }
}
