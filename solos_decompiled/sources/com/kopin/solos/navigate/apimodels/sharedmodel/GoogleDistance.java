package com.kopin.solos.navigate.apimodels.sharedmodel;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.annotations.SerializedName;
import com.kopin.pupil.ui.PageHelper;
import java.io.Serializable;

/* JADX INFO: loaded from: classes47.dex */
public class GoogleDistance implements Serializable {

    @SerializedName(PageHelper.TEXT_PART_TAG)
    public String textValue;

    @SerializedName(FirebaseAnalytics.Param.VALUE)
    public double value;
}
