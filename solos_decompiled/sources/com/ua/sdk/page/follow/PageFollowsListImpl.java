package com.ua.sdk.page.follow;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.AbstractEntityList;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowsListImpl extends AbstractEntityList<PageFollow> {
    public static Parcelable.Creator<PageFollowsListImpl> CREATOR = new Parcelable.Creator<PageFollowsListImpl>() { // from class: com.ua.sdk.page.follow.PageFollowsListImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowsListImpl createFromParcel(Parcel source) {
            return new PageFollowsListImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowsListImpl[] newArray(int size) {
            return new PageFollowsListImpl[size];
        }
    };
    public static final String LIST_KEY = "page_follows";

    @Override // com.ua.sdk.internal.AbstractEntityList
    protected String getListKey() {
        return "page_follows";
    }

    public PageFollowsListImpl() {
    }

    private PageFollowsListImpl(Parcel in) {
        super(in);
    }
}
