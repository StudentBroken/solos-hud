package com.kopin.solos.navigate.apimodels.googledirectionsapimodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.navigate.apimodels.sharedmodel.GooglePolyLine;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionRoute implements Serializable {

    @SerializedName("legs")
    public List<DirectionsLeg> legList = null;

    @SerializedName("overview_polyline")
    public GooglePolyLine polyLine = null;
}
