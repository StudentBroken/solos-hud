package org.apache.commons.lang3.mutable;

import java.io.Serializable;

/* JADX INFO: loaded from: classes51.dex */
public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean> {
    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean(Boolean value) {
        this.value = value.booleanValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.commons.lang3.mutable.Mutable
    public Boolean getValue() {
        return Boolean.valueOf(this.value);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang3.mutable.Mutable
    public void setValue(Boolean value) {
        this.value = value.booleanValue();
    }

    public boolean isTrue() {
        return this.value;
    }

    public boolean isFalse() {
        return !this.value;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public Boolean toBoolean() {
        return Boolean.valueOf(booleanValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableBoolean) && this.value == ((MutableBoolean) obj).booleanValue();
    }

    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(MutableBoolean other) {
        boolean anotherVal = other.value;
        if (this.value == anotherVal) {
            return 0;
        }
        return this.value ? 1 : -1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
