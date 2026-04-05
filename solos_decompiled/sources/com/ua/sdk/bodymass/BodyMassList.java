package com.ua.sdk.bodymass;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassList extends AbstractEntityList<BodyMass> {
    public static Parcelable.Creator<BodyMassList> CREATOR = new Parcelable.Creator<BodyMassList>() { // from class: com.ua.sdk.bodymass.BodyMassList.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassList createFromParcel(Parcel source) {
            return new BodyMassList(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassList[] newArray(int size) {
            return new BodyMassList[size];
        }
    };
    private static final String LIST_KEY = "bodymasses";

    public BodyMassList() {
    }

    private BodyMassList(Parcel in) {
        super(in);
    }

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return LIST_KEY;
    }
}
