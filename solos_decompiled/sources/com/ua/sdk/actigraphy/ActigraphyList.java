package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyList extends AbstractEntityList<Actigraphy> {
    public static Parcelable.Creator<ActigraphyList> CREATOR = new Parcelable.Creator<ActigraphyList>() { // from class: com.ua.sdk.actigraphy.ActigraphyList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyList createFromParcel(Parcel source) {
            return new ActigraphyList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyList[] newArray(int size) {
            return new ActigraphyList[size];
        }
    };
    private static final String LIST_KEY = "actigraphies";

    public ActigraphyList() {
    }

    private ActigraphyList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "actigraphies";
    }
}
