package com.kopin.solos.storage;

import java.util.Locale;

/* JADX INFO: loaded from: classes54.dex */
public class RideDataContent {
    public String RideId = "";
    public String Data = "";

    public String toString() {
        return String.format(Locale.US, "id %s\n data %s", this.RideId, this.Data);
    }
}
