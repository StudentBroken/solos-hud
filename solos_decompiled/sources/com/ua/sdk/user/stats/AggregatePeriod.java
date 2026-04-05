package com.ua.sdk.user.stats;

import android.os.Parcelable;
import com.ua.sdk.LocalDate;

/* JADX INFO: loaded from: classes65.dex */
public interface AggregatePeriod extends Parcelable {
    LocalDate getEndDate();

    LocalDate getStartDate();
}
