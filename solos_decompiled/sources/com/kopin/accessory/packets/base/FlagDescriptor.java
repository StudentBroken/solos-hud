package com.kopin.accessory.packets.base;

import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes14.dex */
public final class FlagDescriptor {
    private int count;
    private int flag;
    private Type type;

    public FlagDescriptor(int flag, Type type, int count) {
        this.flag = flag;
        this.type = type;
        this.count = count;
    }

    public int getFlag() {
        return this.flag;
    }

    public Type getType() {
        return this.type;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isArray() {
        return getCount() > 1;
    }

    public boolean equals(Object obj) {
        return FlagDescriptor.class == obj.getClass() && this.flag == ((FlagDescriptor) obj).flag;
    }

    public String toString() {
        return "flag: " + this.flag + ", type: " + this.type + ", count: " + this.count;
    }
}
