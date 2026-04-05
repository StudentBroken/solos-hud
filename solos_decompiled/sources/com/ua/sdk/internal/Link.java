package com.ua.sdk.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;

/* JADX INFO: loaded from: classes65.dex */
public class Link implements Parcelable {
    public static Parcelable.Creator<Link> CREATOR = new Parcelable.Creator<Link>() { // from class: com.ua.sdk.internal.Link.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Link createFromParcel(Parcel source) {
            return new Link(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    @SerializedName("count")
    Integer mCount;

    @SerializedName("display_name")
    String mDisplayName;

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_HREF)
    String mHref;

    @SerializedName("id")
    String mId;

    @SerializedName("iteration")
    Integer mIteration;

    @SerializedName("name")
    String mName;

    public Link() {
    }

    public Link(String href) {
        this(href, null, null, null, null, null);
    }

    public Link(String href, String id) {
        this(href, id, null, null, null, null);
    }

    public Link(String href, String id, String name) {
        this(href, id, name, null, null, null);
    }

    public Link(String href, String id, String name, String displayName) {
        this(href, id, name, displayName, null, null);
    }

    public Link(String href, String id, String name, String displayName, Integer count) {
        this(href, id, name, displayName, count, null);
    }

    public Link(String href, String id, String name, String displayName, Integer count, Integer iteration) {
        this.mHref = href;
        this.mId = id;
        this.mName = name;
        this.mCount = count;
        this.mDisplayName = displayName;
        this.mIteration = iteration;
    }

    public String getHref() {
        return this.mHref;
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public Integer getCount() {
        return this.mCount;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public Integer getIteration() {
        return this.mIteration;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mHref);
        dest.writeString(this.mId);
        dest.writeString(this.mName);
        dest.writeValue(this.mCount);
        dest.writeValue(this.mIteration);
    }

    private Link(Parcel in) {
        this.mHref = in.readString();
        this.mId = in.readString();
        this.mName = in.readString();
        this.mCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mIteration = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if (this.mCount == null ? link.mCount != null : !this.mCount.equals(link.mCount)) {
            return false;
        }
        if (this.mDisplayName == null ? link.mDisplayName != null : !this.mDisplayName.equals(link.mDisplayName)) {
            return false;
        }
        if (this.mHref != null && this.mHref.equals(link.mHref)) {
            if (this.mId == null ? link.mId != null : !this.mId.equals(link.mId)) {
                return false;
            }
            if (this.mName == null ? link.mName != null : !this.mName.equals(link.mName)) {
                return false;
            }
            if (this.mIteration != null) {
                if (this.mIteration.equals(link.mIteration)) {
                    return true;
                }
            } else if (link.mIteration == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.mHref != null ? this.mHref.hashCode() : 0;
        return (((((((((result * 31) + (this.mId != null ? this.mId.hashCode() : 0)) * 31) + (this.mName != null ? this.mName.hashCode() : 0)) * 31) + (this.mCount != null ? this.mCount.hashCode() : 0)) * 31) + (this.mDisplayName != null ? this.mDisplayName.hashCode() : 0)) * 31) + (this.mIteration != null ? this.mIteration.hashCode() : 0);
    }
}
