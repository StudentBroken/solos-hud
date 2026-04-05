package com.kopin.solos.navigate.apimodels.googledirectionsapimodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleDistance;
import com.kopin.solos.navigate.apimodels.sharedmodel.GoogleLatLng;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.io.Serializable;

/* JADX INFO: loaded from: classes47.dex */
public class DirectionsStep implements Serializable {

    @SerializedName("html_instructions")
    public String instructions;

    @SerializedName("maneuver")
    public String maneuver;

    @SerializedName("start_location")
    public GoogleLatLng start = null;

    @SerializedName("end_location")
    public GoogleLatLng end = null;

    @SerializedName(BaseDataTypes.ID_DISTANCE)
    public GoogleDistance distance = null;

    public void normalise() {
        this.instructions = this.instructions.replaceAll("\\/", " \\/ ");
        this.instructions = this.instructions.replaceAll("\\<div", " \\<div");
        this.instructions = this.instructions.replaceAll("\\<[^\\>]*\\>", "");
        int getDest = this.instructions.indexOf("Destination");
        if (getDest > 0) {
            this.instructions = this.instructions.substring(0, getDest);
        }
        this.instructions += " ";
        this.instructions = this.instructions.replaceAll(" Rd ", " Road ");
        this.instructions = this.instructions.replaceAll(" St\\.", " Saint");
        this.instructions = this.instructions.replaceAll(" St ", " Street ");
        this.instructions = this.instructions.replaceAll(" Ln ", " Lane ");
        this.instructions = this.instructions.replaceAll(" Dr ", " Drive ");
        this.instructions = this.instructions.replaceAll(" Rd ", " Road ");
        this.instructions = this.instructions.replaceAll(" Cl ", " Close ");
        this.instructions = this.instructions.replaceAll(" Pl ", " Place ");
        this.instructions = this.instructions.replaceAll(" Blvd ", " Boulevard ");
        this.instructions = this.instructions.replaceAll(" Ave ", " Avenue ");
        this.instructions = this.instructions.replaceAll(" Rte ", " Route ");
    }
}
