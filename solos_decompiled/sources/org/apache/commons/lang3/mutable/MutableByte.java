package org.apache.commons.lang3.mutable;

/* JADX INFO: loaded from: classes51.dex */
public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {
    private static final long serialVersionUID = -1585823265;
    private byte value;

    public MutableByte() {
    }

    public MutableByte(byte value) {
        this.value = value;
    }

    public MutableByte(Number value) {
        this.value = value.byteValue();
    }

    public MutableByte(String value) throws NumberFormatException {
        this.value = Byte.parseByte(value);
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    /* JADX INFO: renamed from: getValue, reason: merged with bridge method [inline-methods] */
    public Number getValue2() {
        return Byte.valueOf(this.value);
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(Number value) {
        this.value = value.byteValue();
    }

    public void increment() {
        this.value = (byte) (this.value + 1);
    }

    public void decrement() {
        this.value = (byte) (this.value - 1);
    }

    public void add(byte operand) {
        this.value = (byte) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (byte) (this.value + operand.byteValue());
    }

    public void subtract(byte operand) {
        this.value = (byte) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (byte) (this.value - operand.byteValue());
    }

    @Override // java.lang.Number
    public byte byteValue() {
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

    public Byte toByte() {
        return Byte.valueOf(byteValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableByte) && this.value == ((MutableByte) obj).byteValue();
    }

    public int hashCode() {
        return this.value;
    }

    @Override // java.lang.Comparable
    public int compareTo(MutableByte other) {
        byte anotherVal = other.value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf((int) this.value);
    }
}
