package com.google.tagmanager;

/* JADX INFO: loaded from: classes49.dex */
class TypedNumber extends Number implements Comparable<TypedNumber> {
    private double mDouble;
    private long mInt64;
    private boolean mIsInt64 = false;

    private TypedNumber(double d) {
        this.mDouble = d;
    }

    private TypedNumber(long l) {
        this.mInt64 = l;
    }

    public static TypedNumber numberWithDouble(Double d) {
        return new TypedNumber(d.doubleValue());
    }

    public static TypedNumber numberWithInt64(long l) {
        return new TypedNumber(l);
    }

    public static TypedNumber numberWithString(String s) throws NumberFormatException {
        try {
            return new TypedNumber(Long.parseLong(s));
        } catch (NumberFormatException e) {
            try {
                return new TypedNumber(Double.parseDouble(s));
            } catch (NumberFormatException e2) {
                throw new NumberFormatException(s + " is not a valid TypedNumber");
            }
        }
    }

    public String toString() {
        return isInt64() ? Long.toString(this.mInt64) : Double.toString(this.mDouble);
    }

    public boolean equals(Object other) {
        return (other instanceof TypedNumber) && compareTo((TypedNumber) other) == 0;
    }

    public int hashCode() {
        return new Long(longValue()).hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(TypedNumber other) {
        return (isInt64() && other.isInt64()) ? new Long(this.mInt64).compareTo(Long.valueOf(other.mInt64)) : Double.compare(doubleValue(), other.doubleValue());
    }

    public boolean isDouble() {
        return !isInt64();
    }

    public boolean isInt64() {
        return this.mIsInt64;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return isInt64() ? this.mInt64 : this.mDouble;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override // java.lang.Number
    public long longValue() {
        return int64Value();
    }

    public long int64Value() {
        return isInt64() ? this.mInt64 : (long) this.mDouble;
    }

    @Override // java.lang.Number
    public int intValue() {
        return int32Value();
    }

    public int int32Value() {
        return (int) longValue();
    }

    @Override // java.lang.Number
    public short shortValue() {
        return int16Value();
    }

    public short int16Value() {
        return (short) longValue();
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return (byte) longValue();
    }
}
