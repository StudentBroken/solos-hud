package com.ua.sdk.page.association;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.page.Page;

/* JADX INFO: loaded from: classes65.dex */
public class PageAssociationImpl extends ApiTransferObject implements PageAssociation, Parcelable {
    public static Parcelable.Creator<PageAssociationImpl> CREATOR = new Parcelable.Creator<PageAssociationImpl>() { // from class: com.ua.sdk.page.association.PageAssociationImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageAssociationImpl createFromParcel(Parcel source) {
            return new PageAssociationImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageAssociationImpl[] newArray(int size) {
            return new PageAssociationImpl[size];
        }
    };

    public PageAssociationImpl() {
    }

    @Override // com.ua.sdk.page.association.PageAssociation
    public EntityRef<Page> getFromPage() {
        Link fromPageLink = getLink("from_page");
        if (fromPageLink == null) {
            return null;
        }
        return new LinkEntityRef(fromPageLink.getId(), fromPageLink.getHref());
    }

    @Override // com.ua.sdk.page.association.PageAssociation
    public EntityRef<Page> getToPage() {
        Link toPageLink = getLink("to_page");
        if (toPageLink == null) {
            return null;
        }
        return new LinkEntityRef(toPageLink.getId(), toPageLink.getHref());
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        Link selfLink = getLink("self");
        if (selfLink == null) {
            return null;
        }
        return new LinkEntityRef(selfLink.getId(), selfLink.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    private PageAssociationImpl(Parcel in) {
        super(in);
    }
}
