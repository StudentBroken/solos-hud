package com.ua.sdk.activitystory.actor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.activitystory.ActivityStoryActor;
import com.ua.sdk.activitystory.ActivityStoryUserActor;
import com.ua.sdk.friendship.FriendshipImpl;
import com.ua.sdk.friendship.FriendshipStatus;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryUserActorImpl extends ApiTransferObject implements ActivityStoryUserActor {
    public static Parcelable.Creator<ActivityStoryUserActorImpl> CREATOR = new Parcelable.Creator<ActivityStoryUserActorImpl>() { // from class: com.ua.sdk.activitystory.actor.ActivityStoryUserActorImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUserActorImpl createFromParcel(Parcel source) {
            return new ActivityStoryUserActorImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUserActorImpl[] newArray(int size) {
            return new ActivityStoryUserActorImpl[size];
        }
    };

    @SerializedName("first_name")
    String mFirstName;

    @SerializedName("friendship")
    Friendship mFriendship;

    @SerializedName("gender")
    Gender mGender;

    @SerializedName("id")
    String mId;

    @SerializedName("is_mvp")
    Boolean mIsMvp;

    @SerializedName("last_name")
    String mLastName;
    transient ImageUrl mProfilePicture;

    @SerializedName("title")
    String mTitle;

    static class Friendship {

        @SerializedName(FriendshipImpl.ARG_FROM_USER)
        String fromUser;

        @SerializedName("status")
        FriendshipStatus status;

        @SerializedName(FriendshipImpl.ARG_TO_USER)
        String toUser;

        Friendship() {
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public String getFirstName() {
        return this.mFirstName;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public String getLastName() {
        return this.mLastName;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public Gender getGender() {
        return this.mGender;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public Boolean isMvp() {
        return this.mIsMvp;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public ActivityStoryActor.Type getType() {
        return ActivityStoryActor.Type.USER;
    }

    public ActivityStoryUserActorImpl(String id) {
        this.mId = id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryActor
    public String getId() {
        return this.mId;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public ImageUrl getProfilePhoto() {
        return this.mProfilePicture;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public FriendshipStatus getFriendshipStatus() {
        return this.mFriendship == null ? FriendshipStatus.NONE : this.mFriendship.status;
    }

    public void setUserProfilePicture(ImageUrl mProfilePicture) {
        this.mProfilePicture = mProfilePicture;
    }

    protected void createFriendship() {
        if (this.mFriendship == null) {
            this.mFriendship = new Friendship();
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserActor
    public EntityRef<User> getUserRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void setUser(EntityRef<User> ref) {
        if (ref == null) {
            this.mId = null;
        } else {
            this.mId = ref.getId();
        }
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        int iOrdinal = -1;
        super.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeString(this.mFirstName);
        dest.writeString(this.mLastName);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mGender == null ? -1 : this.mGender.ordinal());
        dest.writeValue(this.mIsMvp);
        dest.writeParcelable(this.mProfilePicture, flags);
        dest.writeString((this.mFriendship == null || this.mFriendship.fromUser == null) ? "" : this.mFriendship.fromUser);
        if (this.mFriendship != null && this.mFriendship.status != null) {
            iOrdinal = this.mFriendship.status.ordinal();
        }
        dest.writeInt(iOrdinal);
        dest.writeString((this.mFriendship == null || this.mFriendship.toUser == null) ? "" : this.mFriendship.toUser);
    }

    public ActivityStoryUserActorImpl() {
    }

    private ActivityStoryUserActorImpl(Parcel in) {
        super(in);
        this.mId = in.readString();
        this.mFirstName = in.readString();
        this.mLastName = in.readString();
        this.mTitle = in.readString();
        int tmpMGender = in.readInt();
        this.mGender = tmpMGender == -1 ? null : Gender.values()[tmpMGender];
        this.mIsMvp = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mProfilePicture = (ImageUrl) in.readParcelable(ImageUrl.class.getClassLoader());
        String tmpFromUser = in.readString();
        int tmpStatus = in.readInt();
        String tmpToUser = in.readString();
        if (!tmpFromUser.equals("") || tmpStatus != -1 || !tmpToUser.equals("")) {
            createFriendship();
            this.mFriendship.fromUser = tmpFromUser.equals("") ? null : tmpFromUser;
            this.mFriendship.status = tmpStatus == -1 ? null : FriendshipStatus.values()[tmpStatus];
            this.mFriendship.toUser = tmpToUser.equals("") ? null : tmpToUser;
        }
    }

    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
