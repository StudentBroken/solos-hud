package com.ua.sdk.actigraphy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.internal.LinkEntityRef;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class ActigraphyLinkEntityRef extends LinkEntityRef {
    public static Parcelable.Creator<ActigraphyLinkEntityRef> CREATOR = new Parcelable.Creator<ActigraphyLinkEntityRef>() { // from class: com.ua.sdk.actigraphy.ActigraphyLinkEntityRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyLinkEntityRef createFromParcel(Parcel source) {
            return new ActigraphyLinkEntityRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActigraphyLinkEntityRef[] newArray(int size) {
            return new ActigraphyLinkEntityRef[size];
        }
    };
    private Date mDate;
    private String mUserId;

    protected ActigraphyLinkEntityRef(String id, String href, Date date, String userId) {
        super(id, href);
        this.mDate = date;
        this.mUserId = userId;
    }

    public Date getDate() {
        return this.mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, com.ua.sdk.Reference
    public String getId() {
        return super.getId();
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, com.ua.sdk.Reference
    public String getHref() {
        return super.getHref();
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public int describeContents() {
        return super.describeContents();
    }

    @Override // com.ua.sdk.internal.LinkEntityRef, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mDate == null ? -1L : this.mDate.getTime());
        dest.writeString(this.mUserId);
    }

    private ActigraphyLinkEntityRef(Parcel in) {
        super(in);
        Long tmpDate = Long.valueOf(in.readLong());
        this.mDate = tmpDate.longValue() == -1 ? null : new Date(tmpDate.longValue());
        this.mUserId = in.readString();
    }
}
