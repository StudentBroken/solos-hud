package com.ua.sdk.page;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class PageListImpl extends AbstractEntityList<Page> {
    public static Parcelable.Creator<PageListImpl> CREATOR = new Parcelable.Creator<PageListImpl>() { // from class: com.ua.sdk.page.PageListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageListImpl createFromParcel(Parcel source) {
            return new PageListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageListImpl[] newArray(int size) {
            return new PageListImpl[size];
        }
    };
    public static final String LIST_KEY = "pages";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "pages";
    }

    public PageListImpl() {
    }

    private PageListImpl(Parcel in) {
        super(in);
    }
}
