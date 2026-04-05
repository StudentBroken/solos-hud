package com.kopin.solos.navigate.apimodels.googleplacesmodel;

import com.google.gson.annotations.SerializedName;
import com.kopin.solos.navigate.apimodels.sharedmodel.AbstractModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes47.dex */
public class GooglePlacesResult extends AbstractModel implements Serializable {

    @SerializedName("predictions")
    public List<GooglePlacesPlace> thePlaces = null;

    public List<String> getList() {
        List<String> theList = new ArrayList<>();
        for (GooglePlacesPlace p : this.thePlaces) {
            theList.add(p.thePlace);
        }
        return theList;
    }
}
