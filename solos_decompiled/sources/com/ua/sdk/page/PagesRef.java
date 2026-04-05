package com.ua.sdk.page;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class PagesRef implements EntityListRef<Page> {
    public static Parcelable.Creator<PagesRef> CREATOR = new Parcelable.Creator<PagesRef>() { // from class: com.ua.sdk.page.PagesRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PagesRef createFromParcel(Parcel source) {
            return new PagesRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PagesRef[] newArray(int size) {
            return new PagesRef[size];
        }
    };
    private String params;

    private PagesRef(Builder init) {
        this.params = "";
        this.params = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private Builder() {
            super(UrlBuilderImpl.GET_PAGES_URL);
        }

        public Builder setPageType(PageTypeEnum type) {
            Precondition.isNotNull(type, "PageTypeEnum");
            setParam("page_type_id", type.getName());
            return this;
        }

        public Builder setPageListView(PageListViewEnum view) {
            Precondition.isNotNull(view, "PageListViewEnum");
            setParam("view", view.getName());
            return this;
        }

        public PagesRef build() {
            PagesRef pagesRef;
            synchronized (PagesRef.class) {
                pagesRef = new PagesRef(this);
            }
            return pagesRef;
        }
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.params;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.params);
    }

    private PagesRef(Parcel in) {
        this.params = "";
        this.params = in.readString();
    }
}
