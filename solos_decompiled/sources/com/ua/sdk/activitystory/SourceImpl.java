package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.Source;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class SourceImpl extends ApiTransferObject implements Source {
    public static Parcelable.Creator<SourceImpl> CREATOR = new Parcelable.Creator<SourceImpl>() { // from class: com.ua.sdk.activitystory.SourceImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SourceImpl createFromParcel(Parcel source) {
            return new SourceImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SourceImpl[] newArray(int size) {
            return new SourceImpl[size];
        }
    };

    @SerializedName("id")
    String id;

    @SerializedName("site_name")
    String siteName;

    @SerializedName("site_url")
    String url;

    @Override // com.ua.sdk.Source
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.Source
    public void setId(String id) {
        this.id = id;
    }

    @Override // com.ua.sdk.Source
    public String getUrl() {
        return this.url;
    }

    @Override // com.ua.sdk.Source
    public void setUrl(String url) {
        this.url = url;
    }

    @Override // com.ua.sdk.Source
    public String getSiteName() {
        return this.siteName;
    }

    @Override // com.ua.sdk.Source
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Source> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.url);
        dest.writeString(this.siteName);
    }

    public SourceImpl() {
    }

    private SourceImpl(Parcel in) {
        super(in);
        this.id = in.readString();
        this.url = in.readString();
        this.siteName = in.readString();
    }
}
