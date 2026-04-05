package com.ua.sdk.page;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.ImageUrl;
import com.ua.sdk.UaLog;
import com.ua.sdk.actigraphy.Actigraphy;
import com.ua.sdk.actigraphy.ActigraphyTransferObject;
import com.ua.sdk.activitystory.ActivityStory;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.ImageUrlImpl;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.internal.LinkListRef;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.location.Location;
import com.ua.sdk.page.association.PageAssociation;
import com.ua.sdk.page.follow.PageFollow;
import com.ua.sdk.user.User;
import com.ua.sdk.workout.Workout;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class PageImpl extends ApiTransferObject implements Page, Parcelable {
    public static Parcelable.Creator<PageImpl> CREATOR = new Parcelable.Creator<PageImpl>() { // from class: com.ua.sdk.page.PageImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageImpl createFromParcel(Parcel source) {
            return new PageImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PageImpl[] newArray(int size) {
            return new PageImpl[size];
        }
    };
    protected static final String REF_PAGE_TYPE = "privacy";
    private EntityListRef<Actigraphy> actigraphyRef;
    private EntityListRef<ActivityStory> activityFeedRef;
    private String alias;
    private ImageUrl coverPhoto;
    private EntityListRef<ActivityStory> featuredFeedRef;
    private Integer followerCount;
    private EntityListRef<PageFollow> followerRef;
    private Integer followingCount;
    private EntityListRef<PageFollow> followingRef;
    private Integer fromPageAssociationCount;
    private EntityListRef<PageAssociation> fromPageAssociationRef;
    private String headline;
    private Location location;
    private String pageDescription;
    private EntityRef<PageFollow> pageFollowRef;
    private EntityRef<Page> pageRef;
    private PageSetting pageSetting;
    private EntityRef<PageType> pageTypeRef;
    private EntityRef<PageFollow> pageUnfollowRef;
    private ImageUrl profilePhoto;
    private String title;
    private EntityListRef<PageAssociation> toPageAssocationRef;
    private Integer toPageAssociationCount;
    private URI url;
    private EntityRef<User> userRef;
    private URI website;
    private EntityListRef<Workout> workoutsRef;

    PageImpl() {
        this.profilePhoto = new ImageUrlImpl();
        this.coverPhoto = new ImageUrlImpl();
    }

    PageImpl(Page page) {
        this.profilePhoto = new ImageUrlImpl();
        this.coverPhoto = new ImageUrlImpl();
        Precondition.isNotNull(page);
        this.alias = page.getAlias();
        this.title = page.getTitle();
        this.pageDescription = page.getDescription();
        this.url = page.getUrl();
        this.website = page.getWebsite();
        this.location = page.getLocation();
        this.userRef = page.getUserRef();
        this.pageRef = page.getRef();
        this.pageTypeRef = page.getPageTypeRef();
        this.pageFollowRef = page.getPageFollowRef();
        this.pageUnfollowRef = page.getPageUnfollowRef();
        this.workoutsRef = page.getWorkoutsRef();
        this.actigraphyRef = page.getActigraphyRef();
        this.fromPageAssociationRef = page.getFromPageAssociationsRef();
        this.toPageAssocationRef = page.getToPageAssociationsRef();
        this.followerRef = page.getFollowersRef();
        this.activityFeedRef = page.getActivityFeedRef();
        this.featuredFeedRef = page.getFeaturedFeedRef();
        this.followerCount = page.getFollowerCount();
        this.fromPageAssociationCount = page.getFromPageAssociationCount();
        this.toPageAssociationCount = page.getToPageAssociationCount();
        this.profilePhoto = page.getProfilePhoto();
        this.coverPhoto = page.getCoverPhoto();
        this.headline = page.getHeadline();
        this.pageSetting = page.getPageSetting();
        this.followingCount = page.getFollowingCount();
        this.followingRef = page.getFollowingRef();
        if (page instanceof PageImpl) {
            copyLinkMap(((PageImpl) page).getLinkMap());
        }
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Page> getRef() {
        List<Link> links;
        if (this.pageRef == null && (links = getLinks("self")) != null) {
            this.pageRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.pageRef;
    }

    @Override // com.ua.sdk.page.Page
    public URI getWebsite() {
        return this.website;
    }

    @Override // com.ua.sdk.page.Page
    public String getDescription() {
        return this.pageDescription;
    }

    @Override // com.ua.sdk.page.Page
    public String getTitle() {
        return this.title;
    }

    @Override // com.ua.sdk.page.Page
    public URI getUrl() {
        return this.url;
    }

    @Override // com.ua.sdk.page.Page
    public String getAlias() {
        return this.alias;
    }

    @Override // com.ua.sdk.page.Page
    public Location getLocation() {
        return this.location;
    }

    @Override // com.ua.sdk.page.Page
    public Integer getFollowerCount() {
        if (this.followerCount == null) {
            parseFollowersLink();
        }
        return this.followerCount;
    }

    @Override // com.ua.sdk.page.Page
    public Integer getFollowingCount() {
        if (this.followingCount == null) {
            parseFollowersLink();
        }
        return this.followingCount;
    }

    @Override // com.ua.sdk.page.Page
    public String getHeadline() {
        return this.headline;
    }

    @Override // com.ua.sdk.page.Page
    public PageSetting getPageSetting() {
        return this.pageSetting;
    }

    @Override // com.ua.sdk.page.Page
    public Integer getFromPageAssociationCount() {
        if (this.fromPageAssociationCount == null) {
            parseAssociationsLink();
        }
        return this.fromPageAssociationCount;
    }

    @Override // com.ua.sdk.page.Page
    public Integer getToPageAssociationCount() {
        if (this.toPageAssociationCount == null) {
            parseAssociationsLink();
        }
        return this.toPageAssociationCount;
    }

    @Override // com.ua.sdk.page.Page
    public PageTypeEnum getPageType() {
        if (getPageTypeRef() == null) {
            return null;
        }
        String pageTypeId = this.pageTypeRef.getId();
        return PageTypeEnum.getById(pageTypeId);
    }

    @Override // com.ua.sdk.page.Page
    public EntityRef<User> getUserRef() {
        List<Link> links;
        if (this.userRef == null && (links = getLinks("user")) != null) {
            this.userRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.userRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityRef<PageType> getPageTypeRef() {
        List<Link> links;
        if (this.pageTypeRef == null && (links = getLinks("page_type")) != null) {
            this.pageTypeRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.pageTypeRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityRef<PageFollow> getPageFollowRef() {
        List<Link> links;
        if (this.pageFollowRef == null && (links = getLinks("follow")) != null) {
            this.pageFollowRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.pageFollowRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityRef<PageFollow> getPageUnfollowRef() {
        List<Link> links;
        if (this.pageUnfollowRef == null && (links = getLinks("unfollow")) != null) {
            this.pageUnfollowRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.pageUnfollowRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<Workout> getWorkoutsRef() {
        List<Link> links;
        if (this.workoutsRef == null && (links = getLinks("workouts")) != null) {
            this.workoutsRef = new LinkListRef(links.get(0).getHref());
        }
        return this.workoutsRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<Actigraphy> getActigraphyRef() {
        List<Link> links;
        if (this.actigraphyRef == null && (links = getLinks(ActigraphyTransferObject.KEY_ACTIGRAPHY)) != null) {
            this.actigraphyRef = new LinkListRef(links.get(0).getHref());
        }
        return this.actigraphyRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<PageAssociation> getFromPageAssociationsRef() {
        if (this.fromPageAssociationRef == null) {
            parseAssociationsLink();
        }
        return this.fromPageAssociationRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<PageAssociation> getToPageAssociationsRef() {
        if (this.toPageAssocationRef == null) {
            parseAssociationsLink();
        }
        return this.toPageAssocationRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<PageFollow> getFollowersRef() {
        if (this.followerRef == null) {
            parseFollowersLink();
        }
        return this.followerRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<PageFollow> getFollowingRef() {
        if (this.followingRef == null) {
            parseFollowersLink();
        }
        return this.followingRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<ActivityStory> getActivityFeedRef() {
        Link link;
        if (this.activityFeedRef == null && (link = getLink("activity_feed", "activity_feed")) != null) {
            this.activityFeedRef = new LinkListRef(link.getHref());
        }
        return this.activityFeedRef;
    }

    @Override // com.ua.sdk.page.Page
    public EntityListRef<ActivityStory> getFeaturedFeedRef() {
        Link link;
        if (this.featuredFeedRef == null && (link = getLink("activity_feed", "featured")) != null) {
            this.featuredFeedRef = new LinkListRef(link.getHref());
        }
        return this.featuredFeedRef;
    }

    private void parseFollowersLink() {
        List<Link> links = getLinks("followers");
        if (links != null) {
            for (Link link : links) {
                if (link.getName().equals("followers")) {
                    this.followerRef = new LinkListRef(link.getHref());
                    this.followerCount = link.getCount();
                } else if (link.getName().equals("following")) {
                    this.followingRef = new LinkListRef(link.getHref());
                    this.followingCount = link.getCount();
                }
            }
        }
    }

    private void parseAssociationsLink() {
        List<Link> links = getLinks("associations");
        if (links != null) {
            for (Link link : links) {
                if (link.getName().equals("from")) {
                    this.fromPageAssociationRef = new LinkListRef(link.getHref());
                    this.fromPageAssociationCount = link.getCount();
                } else if (link.getName().equals("to")) {
                    this.toPageAssocationRef = new LinkListRef(link.getHref());
                    this.toPageAssociationCount = link.getCount();
                }
            }
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    void setAlias(String alias) {
        this.alias = alias;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    void setUrl(URI url) {
        this.url = url;
    }

    void setWebsite(URI website) {
        this.website = website;
    }

    void setLocation(Location location) {
        this.location = location;
    }

    void setUserRef(EntityRef<User> userRef) {
        this.userRef = userRef;
    }

    void setPageRef(EntityRef<Page> pageRef) {
        this.pageRef = pageRef;
    }

    void setPageTypeRef(EntityRef<PageType> pageTypeRef) {
        this.pageTypeRef = pageTypeRef;
    }

    void setPageFollowRef(EntityRef<PageFollow> pageFollowRef) {
        this.pageFollowRef = pageFollowRef;
    }

    void setPageUnfollowRef(EntityRef<PageFollow> pageUnfollowRef) {
        this.pageUnfollowRef = pageUnfollowRef;
    }

    void setWorkoutsRef(EntityListRef<Workout> workoutsRef) {
        this.workoutsRef = workoutsRef;
    }

    void setActigraphyRef(EntityListRef<Actigraphy> actigraphyRef) {
        this.actigraphyRef = actigraphyRef;
    }

    void setFromPageAssociationRef(EntityListRef<PageAssociation> associationRef) {
        this.fromPageAssociationRef = associationRef;
    }

    void setToPageAssociationRef(EntityListRef<PageAssociation> associationRef) {
        this.toPageAssocationRef = associationRef;
    }

    void setFollowerRef(EntityListRef<PageFollow> followerRef) {
        this.followerRef = followerRef;
    }

    void setFollowingRef(EntityListRef<PageFollow> followingRef) {
        this.followingRef = followingRef;
    }

    void setActivityFeedRef(EntityListRef<ActivityStory> activityFeedRef) {
        this.activityFeedRef = activityFeedRef;
    }

    void setFeaturedFeedRef(EntityListRef<ActivityStory> featuredFeedRef) {
        this.featuredFeedRef = featuredFeedRef;
    }

    void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    void setFromPageAssociationCount(Integer fromPageAssociationCount) {
        this.fromPageAssociationCount = fromPageAssociationCount;
    }

    void setToPageAssociationCount(Integer toPageAssociationCount) {
        this.toPageAssociationCount = toPageAssociationCount;
    }

    void setHeadline(String headline) {
        this.headline = headline;
    }

    void setPageSetting(PageSetting pageSetting) {
        this.pageSetting = pageSetting;
    }

    @Override // com.ua.sdk.page.Page
    public ImageUrl getProfilePhoto() {
        return this.profilePhoto;
    }

    public void setProfilePhoto(ImageUrlImpl profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override // com.ua.sdk.page.Page
    public ImageUrl getCoverPhoto() {
        return this.coverPhoto;
    }

    public void setCoverPhoto(ImageUrlImpl coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.alias);
        dest.writeString(this.title);
        dest.writeString(this.pageDescription);
        if (this.url == null) {
            dest.writeString("");
        } else {
            dest.writeString(this.url.toString());
        }
        if (this.website == null) {
            dest.writeString("");
        } else {
            dest.writeString(this.website.toString());
        }
        dest.writeParcelable(this.location, 0);
        dest.writeParcelable(this.userRef, 0);
        dest.writeParcelable(this.pageRef, 0);
        dest.writeParcelable(this.pageTypeRef, 0);
        dest.writeParcelable(this.pageFollowRef, 0);
        dest.writeParcelable(this.pageUnfollowRef, 0);
        dest.writeParcelable(this.workoutsRef, 0);
        dest.writeParcelable(this.actigraphyRef, 0);
        dest.writeParcelable(this.fromPageAssociationRef, 0);
        dest.writeParcelable(this.toPageAssocationRef, 0);
        dest.writeParcelable(this.followerRef, 0);
        dest.writeParcelable(this.activityFeedRef, 0);
        dest.writeParcelable(this.featuredFeedRef, 0);
        dest.writeValue(this.followerCount);
        dest.writeValue(this.fromPageAssociationCount);
        dest.writeValue(this.toPageAssociationCount);
        dest.writeParcelable(this.coverPhoto, 0);
        dest.writeParcelable(this.profilePhoto, 0);
        dest.writeString(this.headline);
        dest.writeParcelable(this.pageSetting, 0);
        dest.writeParcelable(this.followingRef, 0);
        dest.writeValue(this.followingCount);
    }

    private PageImpl(Parcel in) {
        super(in);
        this.profilePhoto = new ImageUrlImpl();
        this.coverPhoto = new ImageUrlImpl();
        this.alias = in.readString();
        this.title = in.readString();
        this.pageDescription = in.readString();
        String inUrl = in.readString();
        if (inUrl.equals("")) {
            this.url = null;
        } else {
            try {
                this.url = new URI(inUrl);
            } catch (URISyntaxException e) {
                UaLog.error("Error unparceling Page URL: " + inUrl, (Throwable) e);
            }
        }
        String inWebsite = in.readString();
        if (inWebsite.equals("")) {
            this.website = null;
        } else {
            try {
                this.website = new URI(inWebsite);
            } catch (URISyntaxException e2) {
                UaLog.error("Error unparceling Page website: " + inWebsite, (Throwable) e2);
            }
        }
        this.location = (Location) in.readParcelable(Location.class.getClassLoader());
        this.userRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.pageRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.pageTypeRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.pageFollowRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.pageUnfollowRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.workoutsRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.actigraphyRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.fromPageAssociationRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.toPageAssocationRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.followerRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.activityFeedRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.featuredFeedRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.followerCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fromPageAssociationCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.toPageAssociationCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.coverPhoto = (ImageUrl) in.readParcelable(ImageUrl.class.getClassLoader());
        this.profilePhoto = (ImageUrl) in.readParcelable(ImageUrl.class.getClassLoader());
        this.headline = in.readString();
        this.pageSetting = (PageSetting) in.readParcelable(PageSetting.class.getClassLoader());
        this.followingRef = (EntityListRef) in.readParcelable(LinkListRef.class.getClassLoader());
        this.followingCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }
}
