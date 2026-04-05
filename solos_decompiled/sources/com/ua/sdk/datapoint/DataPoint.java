package com.ua.sdk.datapoint;

import android.os.Parcelable;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface DataPoint extends Parcelable {
    Date getDatetime();

    Date getStartDatetime();

    Double getValueDouble(DataField dataField);

    Long getValueLong(DataField dataField);
}
