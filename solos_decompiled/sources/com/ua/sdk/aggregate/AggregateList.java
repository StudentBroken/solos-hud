package com.ua.sdk.aggregate;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class AggregateList extends AbstractEntityList<Aggregate> {
    public static Parcelable.Creator<AggregateList> CREATOR = new Parcelable.Creator<AggregateList>() { // from class: com.ua.sdk.aggregate.AggregateList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateList createFromParcel(Parcel source) {
            return new AggregateList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AggregateList[] newArray(int size) {
            return new AggregateList[size];
        }
    };
    private static final String LIST_KEY = "aggregates";

    public AggregateList() {
    }

    protected AggregateList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
