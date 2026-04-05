package com.kopin.peloton;

import java.util.Locale;

/* JADX INFO: loaded from: classes61.dex */
public class Gear {
    public String GearId = "";
    public String Name = "";
    public int Lifespan = 0;
    public long DateNew = System.currentTimeMillis();
    public long TimeStamp = System.currentTimeMillis();

    public String toString() {
        return String.format(Locale.US, "gearId %s\nname %s", this.GearId, this.Name);
    }
}
