package com.kopin.solos.navigate.apimodels.sharedmodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.storage.Coordinate;
import java.io.Serializable;

/* JADX INFO: loaded from: classes47.dex */
public class GoogleLatLng implements Serializable {

    @SerializedName("lat")
    public double gLat;

    @SerializedName("lng")
    public double gLng;

    public String toString() {
        return "(" + this.gLat + "," + this.gLng + ")";
    }

    public Coordinate toWaypoint() {
        return new Coordinate(this.gLat, this.gLng);
    }
}
