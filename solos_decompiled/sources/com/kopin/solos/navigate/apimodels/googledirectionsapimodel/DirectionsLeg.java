package com.kopin.solos.navigate.apimodels.googledirectionsapimodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleDistance;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleTime;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.io.Serializable;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionsLeg implements Serializable {

    @SerializedName(BaseDataTypes.ID_STEPS)
    public List<DirectionsStep> stepList = null;

    @SerializedName(BaseDataTypes.ID_DISTANCE)
    public GoogleDistance totalDistance = null;

    @SerializedName("duration")
    public GoogleTime totalTime = null;
}
