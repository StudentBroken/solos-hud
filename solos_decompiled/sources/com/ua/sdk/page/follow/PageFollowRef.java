package com.ua.sdk.page.follow;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowRef implements EntityListRef<PageFollow> {
    public static Parcelable.Creator<PageFollowRef> CREATOR = new Parcelable.Creator<PageFollowRef>() { // from class: com.ua.sdk.page.follow.PageFollowRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowRef createFromParcel(Parcel source) {
            return new PageFollowRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowRef[] newArray(int size) {
            return new PageFollowRef[size];
        }
    };
    private String href;

    private PageFollowRef(Builder init) {
        this.href = init.getHref();
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private Builder() {
            super("/v7.0/page_follow/");
        }

        public Builder setUserId(String userId) {
            setParam("user_id", userId);
            return this;
        }

        public Builder setPageId(String pageId) {
            setParam("page_id", pageId);
            return this;
        }

        public PageFollowRef build() {
            PageFollowRef pageFollowRef;
            synchronized (PageFollowRef.class) {
                pageFollowRef = new PageFollowRef(this);
            }
            return pageFollowRef;
        }
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    private PageFollowRef(Parcel in) {
        this.href = in.readString();
    }
}
