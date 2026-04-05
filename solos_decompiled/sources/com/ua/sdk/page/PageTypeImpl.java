package com.ua.sdk.page;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class PageTypeImpl extends ApiTransferObject implements PageType, Parcelable {
    public static Parcelable.Creator<PageTypeImpl> CREATOR = new Parcelable.Creator<PageTypeImpl>() { // from class: com.ua.sdk.page.PageTypeImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageTypeImpl createFromParcel(Parcel source) {
            return new PageTypeImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageTypeImpl[] newArray(int size) {
            return new PageTypeImpl[size];
        }
    };

    @SerializedName("title")
    private String mTitle;

    public PageTypeImpl() {
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<PageType> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // com.ua.sdk.page.PageType
    public String getTitle() {
        return this.mTitle;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mTitle);
    }

    private PageTypeImpl(Parcel in) {
        super(in);
        this.mTitle = in.readString();
    }
}
