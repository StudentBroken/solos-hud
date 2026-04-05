package com.ua.sdk.activitystory.object;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryUserObject;
import com.ua.sdk.friendship.FriendshipImpl;
import com.ua.sdk.friendship.FriendshipStatus;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.location.Location;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.Gender;
import com.ua.sdk.user.User;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryUserObjectImpl extends ApiTransferObject implements ActivityStoryUserObject {
    public static Parcelable.Creator<ActivityStoryUserObjectImpl> CREATOR = new Parcelable.Creator<ActivityStoryUserObjectImpl>() { // from class: com.ua.sdk.activitystory.object.ActivityStoryUserObjectImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUserObjectImpl createFromParcel(Parcel source) {
            return new ActivityStoryUserObjectImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryUserObjectImpl[] newArray(int size) {
            return new ActivityStoryUserObjectImpl[size];
        }
    };

    @SerializedName("date_joined")
    Date dateJoined;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("friendship")
    Friendship friendship;

    @SerializedName("gender")
    Gender gender;

    @SerializedName("id")
    String id;

    @SerializedName("is_mvp")
    Boolean isMvp;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("location")
    Location location;

    @SerializedName(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)
    Privacy privacy;

    @SerializedName("title")
    String title;

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

    public ActivityStoryUserObjectImpl() {
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject, com.ua.sdk.activitystory.ActivityStoryObject
    public ActivityStoryObject.Type getType() {
        return ActivityStoryObject.Type.USER;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public Privacy getPrivacy() {
        return this.privacy;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public Location getLocation() {
        return this.location;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public Date getJoinedDate() {
        return this.dateJoined;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public String getFirstName() {
        return this.firstName;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public String getLastName() {
        return this.lastName;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public String getTitle() {
        return this.title;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public Gender getGender() {
        return this.gender;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public Boolean isMvp() {
        return this.isMvp;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public EntityRef<User> getUserRef() {
        Link link = getLink("self");
        if (link == null) {
            return null;
        }
        return new LinkEntityRef(link.getId(), link.getHref());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryUserObject
    public FriendshipStatus getFriendshipStatus() {
        return this.friendship == null ? FriendshipStatus.NONE : this.friendship.status;
    }

    protected void createFriendship() {
        if (this.friendship == null) {
            this.friendship = new Friendship();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        int iOrdinal = -1;
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.title);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeValue(this.isMvp);
        dest.writeParcelable(this.privacy, flags);
        dest.writeParcelable(this.location, flags);
        dest.writeLong(this.dateJoined == null ? -1L : this.dateJoined.getTime());
        dest.writeString((this.friendship == null || this.friendship.fromUser == null) ? "" : this.friendship.fromUser);
        if (this.friendship != null && this.friendship.status != null) {
            iOrdinal = this.friendship.status.ordinal();
        }
        dest.writeInt(iOrdinal);
        dest.writeString((this.friendship == null || this.friendship.toUser == null) ? "" : this.friendship.toUser);
    }

    private ActivityStoryUserObjectImpl(Parcel in) {
        super(in);
        this.id = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.title = in.readString();
        int tmpMGender = in.readInt();
        this.gender = tmpMGender == -1 ? null : Gender.values()[tmpMGender];
        this.isMvp = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.privacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
        this.location = (Location) in.readParcelable(Location.class.getClassLoader());
        Long tmpDate = Long.valueOf(in.readLong());
        this.dateJoined = tmpDate.longValue() == -1 ? null : new Date(tmpDate.longValue());
        String tmpFromUser = in.readString();
        int tmpStatus = in.readInt();
        String tmpToUser = in.readString();
        if (!tmpFromUser.equals("") || tmpStatus != -1 || !tmpToUser.equals("")) {
            createFriendship();
            this.friendship.fromUser = tmpFromUser.equals("") ? null : tmpFromUser;
            this.friendship.status = tmpStatus == -1 ? null : FriendshipStatus.values()[tmpStatus];
            this.friendship.toUser = tmpToUser.equals("") ? null : tmpToUser;
        }
    }
}
