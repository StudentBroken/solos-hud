package com.ua.sdk.authentication;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.LinkEntityRef;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class FilemobileCredentialImpl extends ApiTransferObject implements FilemobileCredential, Parcelable {
    public static Parcelable.Creator<FilemobileCredentialImpl> CREATOR = new Parcelable.Creator<FilemobileCredentialImpl>() { // from class: com.ua.sdk.authentication.FilemobileCredentialImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilemobileCredentialImpl createFromParcel(Parcel source) {
            return new FilemobileCredentialImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FilemobileCredentialImpl[] newArray(int size) {
            return new FilemobileCredentialImpl[size];
        }
    };
    private Date created;
    private String token;
    private String uid;
    private Uri uploaderUri;
    private String vhost;

    public FilemobileCredentialImpl() {
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public String getToken() {
        return this.token;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public void setToken(String token) {
        this.token = token;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public String getVhost() {
        return this.vhost;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public String getUid() {
        return this.uid;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public Uri getUploaderUri() {
        return this.uploaderUri;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public void setUploaderUri(Uri uploaderUri) {
        this.uploaderUri = uploaderUri;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public Date getCreated() {
        return this.created;
    }

    @Override // com.ua.sdk.authentication.FilemobileCredential
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        return new LinkEntityRef("", getLocalId(), getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.vhost);
        dest.writeString(this.uid);
        dest.writeParcelable(this.uploaderUri, flags);
        dest.writeLong(this.created != null ? this.created.getTime() : -1L);
    }

    private FilemobileCredentialImpl(Parcel in) {
        super(in);
        this.token = in.readString();
        this.vhost = in.readString();
        this.uid = in.readString();
        this.uploaderUri = (Uri) in.readParcelable(Uri.class.getClassLoader());
        long tempCreated = in.readLong();
        this.created = tempCreated == -1 ? null : new Date(tempCreated);
    }
}
