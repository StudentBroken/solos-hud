package com.ua.sdk.datapoint;

import android.os.Parcelable;

/* JADX INFO: loaded from: classes65.dex */
public interface DataField extends Parcelable {
    String getId();

    String getType();

    DataUnits getUnits();
}
