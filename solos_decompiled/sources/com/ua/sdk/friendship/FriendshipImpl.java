package com.ua.sdk.friendship;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class FriendshipImpl extends ApiTransferObject implements Friendship {
    public static final String ARG_FROM_USER = "from_user";
    public static final String ARG_SELF = "self";
    public static final String ARG_TO_USER = "to_user";
    public static Parcelable.Creator<FriendshipImpl> CREATOR = new Parcelable.Creator<FriendshipImpl>() { // from class: com.ua.sdk.friendship.FriendshipImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipImpl createFromParcel(Parcel source) {
            return new FriendshipImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FriendshipImpl[] newArray(int size) {
            return new FriendshipImpl[size];
        }
    };
    private transient List<Friendship> friendships;
    private Date mCreatedDateTime;
    private FriendshipStatus mFriendshipStatus;
    private String mMessage;

    protected FriendshipImpl() {
        this.mFriendshipStatus = FriendshipStatus.NONE;
    }

    protected FriendshipImpl(FriendshipStatus friendshipStatus, Date createdDateTime, String message) {
        this.mFriendshipStatus = FriendshipStatus.NONE;
        this.mFriendshipStatus = friendshipStatus;
        this.mCreatedDateTime = createdDateTime;
        this.mMessage = message;
    }

    protected FriendshipImpl(FriendshipRef ref) {
        this.mFriendshipStatus = FriendshipStatus.NONE;
        ArrayList<Link> links = new ArrayList<>(1);
        links.add(new Link(ref.getHref(), ref.getId()));
        setLinksForRelation("self", links);
        ArrayList<Link> toLinks = new ArrayList<>(1);
        toLinks.add(new Link("/v7.0/user/" + ref.getToUserId() + "/", ref.getToUserId()));
        setLinksForRelation(ARG_TO_USER, toLinks);
        ArrayList<Link> fromLinks = new ArrayList<>(1);
        fromLinks.add(new Link("/v7.0/user/" + ref.getFromUserId() + "/", ref.getFromUserId()));
        setLinksForRelation(ARG_FROM_USER, fromLinks);
    }

    public void addFriendship(Friendship friendship) {
        if (this.friendships == null) {
            this.friendships = new ArrayList();
        }
        this.friendships.add(friendship);
    }

    public List<Friendship> getFriendships() {
        if (this.friendships == null) {
            this.friendships = new ArrayList();
        }
        return this.friendships;
    }

    @Override // com.ua.sdk.friendship.Friendship
    public FriendshipStatus getFriendshipStatus() {
        return this.mFriendshipStatus;
    }

    @Override // com.ua.sdk.friendship.Friendship
    public Date getCreatedDateTime() {
        return this.mCreatedDateTime;
    }

    @Override // com.ua.sdk.friendship.Friendship
    public String getMessage() {
        return this.mMessage;
    }

    @Override // com.ua.sdk.friendship.Friendship
    public EntityRef<User> getToUserEntityRef() {
        List<Link> selfLinks = getLinks(ARG_TO_USER);
        if (selfLinks == null && selfLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
    }

    @Override // com.ua.sdk.friendship.Friendship
    public EntityRef<User> getFromUserEntityRef() {
        List<Link> selfLinks = getLinks(ARG_FROM_USER);
        if (selfLinks == null && selfLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Friendship> getRef() {
        List<Link> selfLinks = getLinks("self");
        if (selfLinks == null && selfLinks.isEmpty()) {
            return null;
        }
        return new LinkEntityRef(selfLinks.get(0).getId(), selfLinks.get(0).getHref());
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.mFriendshipStatus = friendshipStatus;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.mCreatedDateTime = createdDateTime;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mFriendshipStatus == null ? -1 : this.mFriendshipStatus.ordinal());
        dest.writeLong(this.mCreatedDateTime != null ? this.mCreatedDateTime.getTime() : -1L);
        dest.writeString(this.mMessage);
    }

    private FriendshipImpl(Parcel in) {
        super(in);
        this.mFriendshipStatus = FriendshipStatus.NONE;
        int tmpMFriendshipStatus = in.readInt();
        this.mFriendshipStatus = tmpMFriendshipStatus == -1 ? null : FriendshipStatus.values()[tmpMFriendshipStatus];
        long tmpMCreatedDateTime = in.readLong();
        this.mCreatedDateTime = tmpMCreatedDateTime != -1 ? new Date(tmpMCreatedDateTime) : null;
        this.mMessage = in.readString();
    }
}
