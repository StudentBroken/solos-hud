package com.ua.sdk.user.profilephoto;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.ImageUrlImpl;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoImpl extends ApiTransferObject implements UserProfilePhoto, Parcelable {
    public static Parcelable.Creator<UserProfilePhotoImpl> CREATOR = new Parcelable.Creator<UserProfilePhotoImpl>() { // from class: com.ua.sdk.user.profilephoto.UserProfilePhotoImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfilePhotoImpl createFromParcel(Parcel source) {
            return new UserProfilePhotoImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserProfilePhotoImpl[] newArray(int size) {
            return new UserProfilePhotoImpl[size];
        }
    };
    public static final String REF_LARGE = "large";
    public static final String REF_MEDIUM = "medium";
    public static final String REF_SMALL = "small";
    private String mLargeProfilePhotoURL;
    private String mMediumProfilePhotoURL;
    private String mSmallProfilePhotoURL;
    private transient EntityRef<UserProfilePhoto> ref;

    public UserProfilePhotoImpl() {
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<UserProfilePhoto> getRef() {
        if (this.ref == null) {
            List<Link> selfLinks = getLinks("self");
            if (selfLinks == null || selfLinks.isEmpty()) {
                return null;
            }
            this.ref = new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
        }
        return this.ref;
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhoto
    public String getSmallImageURL() {
        if (this.mSmallProfilePhotoURL == null) {
            this.mSmallProfilePhotoURL = getLinks("small") != null ? getLinks("small").get(0).getHref() : null;
        }
        return this.mSmallProfilePhotoURL;
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhoto
    public String getMediumImageURL() {
        if (this.mMediumProfilePhotoURL == null) {
            this.mMediumProfilePhotoURL = getLinks("medium") != null ? getLinks("medium").get(0).getHref() : null;
        }
        return this.mMediumProfilePhotoURL;
    }

    @Override // com.ua.sdk.user.profilephoto.UserProfilePhoto
    public String getLargeImageURL() {
        if (this.mLargeProfilePhotoURL == null) {
            this.mLargeProfilePhotoURL = getLinks("large") != null ? getLinks("large").get(0).getHref() : null;
        }
        return this.mLargeProfilePhotoURL;
    }

    public void setRef(EntityRef<UserProfilePhoto> ref) {
        this.ref = ref;
    }

    public void setSmallImageURL(String smallImageURL) {
        this.mSmallProfilePhotoURL = smallImageURL;
    }

    public void setMediumImageURL(String mediumImageURL) {
        this.mMediumProfilePhotoURL = mediumImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.mLargeProfilePhotoURL = largeImageURL;
    }

    public ImageUrlImpl toImageUrl() {
        ImageUrlImpl imageUrl = ImageUrlImpl.getBuilder().setSmall(getSmallImageURL()).setMedium(getMediumImageURL()).setLarge(getLargeImageURL()).build();
        return imageUrl;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mSmallProfilePhotoURL);
        dest.writeString(this.mMediumProfilePhotoURL);
        dest.writeString(this.mLargeProfilePhotoURL);
    }

    private UserProfilePhotoImpl(Parcel in) {
        super(in);
        this.mSmallProfilePhotoURL = in.readString();
        this.mMediumProfilePhotoURL = in.readString();
        this.mLargeProfilePhotoURL = in.readString();
    }
}
