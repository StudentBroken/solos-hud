package com.ua.sdk.page.follow;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.page.Page;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class PageFollowImpl extends ApiTransferObject implements PageFollow {
    public static Parcelable.Creator<PageFollowImpl> CREATOR = new Parcelable.Creator<PageFollowImpl>() { // from class: com.ua.sdk.page.follow.PageFollowImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowImpl createFromParcel(Parcel source) {
            return new PageFollowImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageFollowImpl[] newArray(int size) {
            return new PageFollowImpl[size];
        }
    };
    List<PageFollow> pageFollows;
    private EntityRef pageRef;
    private EntityRef self;
    private EntityRef userRef;

    PageFollowImpl() {
    }

    PageFollowImpl(PageFollow pageFollow) {
        Precondition.isNotNull(pageFollow);
        this.self = pageFollow.getRef();
        this.userRef = pageFollow.getUserReference();
        this.pageRef = pageFollow.getPageReference();
        if (pageFollow instanceof PageFollowImpl) {
            copyLinkMap(((PageFollowImpl) pageFollow).getLinkMap());
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<PageFollow> getRef() {
        List<Link> links;
        if (this.self == null && (links = getLinks("self")) != null) {
            this.self = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.self;
    }

    @Override // com.ua.sdk.page.follow.PageFollow
    public EntityRef<User> getUserReference() {
        List<Link> links;
        if (this.userRef == null && (links = getLinks("user")) != null) {
            this.userRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.userRef;
    }

    @Override // com.ua.sdk.page.follow.PageFollow
    public EntityRef<Page> getPageReference() {
        List<Link> links;
        if (this.pageRef == null && (links = getLinks("page")) != null) {
            this.pageRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.pageRef;
    }

    public void addPageFollow(PageFollow pageFollow) {
        if (this.pageFollows == null) {
            this.pageFollows = new ArrayList();
        }
        this.pageFollows.add(pageFollow);
    }

    public List<PageFollow> getPageFollows() {
        if (this.pageFollows == null) {
            this.pageFollows = new ArrayList();
        }
        return this.pageFollows;
    }

    void setRef(EntityRef<PageFollow> ref) {
        this.self = ref;
    }

    void setUserRef(EntityRef<User> ref) {
        this.userRef = ref;
    }

    void setPageRef(EntityRef<Page> ref) {
        this.pageRef = ref;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.self, 0);
        dest.writeParcelable(this.userRef, 0);
        dest.writeParcelable(this.pageRef, 0);
        dest.writeList(this.pageFollows);
    }

    private PageFollowImpl(Parcel in) {
        super(in);
        this.self = (EntityRef) in.readParcelable(EntityRef.class.getClassLoader());
        this.userRef = (EntityRef) in.readParcelable(EntityRef.class.getClassLoader());
        this.pageRef = (EntityRef) in.readParcelable(EntityRef.class.getClassLoader());
        this.pageFollows = in.readArrayList(PageFollow.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
