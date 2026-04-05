package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.share.internal.ShareConstants;
import com.ua.sdk.EntityRef;
import com.ua.sdk.UaLog;
import com.ua.sdk.activitytype.ActivityType;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;
import com.ua.sdk.user.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RouteImpl extends ApiTransferObject implements Route, Parcelable {
    public static final Parcelable.Creator<RouteImpl> CREATOR = new Parcelable.Creator<RouteImpl>() { // from class: com.ua.sdk.route.RouteImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteImpl createFromParcel(Parcel source) {
            return new RouteImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteImpl[] newArray(int size) {
            return new RouteImpl[size];
        }
    };
    EntityRef<ActivityType> activityTypeRef;
    String city;
    ArrayList<Climb> climbs;
    String country;
    Date createdDate;
    String dataSource;
    String description;
    Double distanceMeters;
    Double maxElevation;
    Double minElevation;
    String name;
    String postalCode;
    Privacy privacy;
    ArrayList<Point> routePoints;
    EntityRef<Route> routeRef;
    String startPointType;
    StartingLocation startingLocation;
    String state;
    String thumbnailLink;
    Double totalAscent;
    Double totalDescent;
    Date updatedDate;
    EntityRef<User> userRef;

    public RouteImpl() {
        this.routePoints = new ArrayList<>();
        this.climbs = new ArrayList<>();
    }

    protected RouteImpl(RouteBuilderImpl builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.routePoints = builder.points;
        this.startPointType = builder.startPointType;
        this.postalCode = builder.postalCode;
    }

    @Override // com.ua.sdk.route.Route
    public String getCity() {
        return this.city;
    }

    @Override // com.ua.sdk.route.Route
    public String getCountry() {
        return this.country;
    }

    @Override // com.ua.sdk.route.Route
    public String getState() {
        return this.state;
    }

    public StartingLocation getStartingLocation() {
        return this.startingLocation;
    }

    @Override // com.ua.sdk.route.Route
    public Double getStartingLatitude() {
        return Double.valueOf(this.startingLocation.coordinates[0]);
    }

    @Override // com.ua.sdk.route.Route
    public Double getStartingLongitude() {
        return Double.valueOf(this.startingLocation.coordinates[1]);
    }

    @Override // com.ua.sdk.route.Route
    public String getStartPointType() {
        return this.startPointType;
    }

    @Override // com.ua.sdk.route.Route
    public String getPostalCode() {
        return this.postalCode;
    }

    @Override // com.ua.sdk.route.Route
    public Double getDistanceMeters() {
        return this.distanceMeters;
    }

    @Override // com.ua.sdk.route.Route
    public String getName() {
        return this.name;
    }

    @Override // com.ua.sdk.route.Route
    public String getDescription() {
        return this.description;
    }

    @Override // com.ua.sdk.route.Route
    public String getDataSource() {
        return this.dataSource;
    }

    @Override // com.ua.sdk.route.Route
    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Override // com.ua.sdk.route.Route
    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    @Override // com.ua.sdk.route.Route
    public Point getPointAt(int index) {
        return this.routePoints.get(index);
    }

    @Override // com.ua.sdk.route.Route
    public Double getLatitudeAt(int index) {
        return this.routePoints.get(index).getLatitude();
    }

    @Override // com.ua.sdk.route.Route
    public Double getLongitudeAt(int index) {
        return this.routePoints.get(index).getLongitude();
    }

    @Override // com.ua.sdk.route.Route
    public Double getElevationAt(int index) {
        return this.routePoints.get(index).getElevation();
    }

    @Override // com.ua.sdk.route.Route
    public Double getDistanceAt(int index) {
        return this.routePoints.get(index).getDistanceMeters();
    }

    @Override // com.ua.sdk.route.Route
    public int getTotalPoints() {
        return this.routePoints.size();
    }

    @Override // com.ua.sdk.route.Route
    public ArrayList<Climb> getClimbs() {
        return this.climbs;
    }

    @Override // com.ua.sdk.route.Route
    public Double getTotalAscent() {
        return this.totalAscent;
    }

    @Override // com.ua.sdk.route.Route
    public Double getTotalDescent() {
        return this.totalDescent;
    }

    @Override // com.ua.sdk.route.Route
    public Double getMinElevation() {
        return this.minElevation;
    }

    @Override // com.ua.sdk.route.Route
    public Double getMaxElevation() {
        return this.maxElevation;
    }

    @Override // com.ua.sdk.route.Route
    public Privacy getPrivacy() {
        List<Link> links;
        if (this.privacy == null && (links = getLinks(ShareConstants.WEB_DIALOG_PARAM_PRIVACY)) != null) {
            try {
                int id = Integer.parseInt(links.get(0).getId());
                this.privacy = PrivacyHelper.getPrivacyFromId(id);
            } catch (NumberFormatException e) {
                UaLog.error("Unable to get privacy.", (Throwable) e);
                return null;
            }
        }
        return this.privacy;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setTotalAscent(Double totalAscent) {
        this.totalAscent = totalAscent;
    }

    public void setTotalDescent(Double totalDescent) {
        this.totalDescent = totalDescent;
    }

    public void setMinElevation(Double minElevation) {
        this.minElevation = minElevation;
    }

    public void setMaxElevation(Double maxElevation) {
        this.maxElevation = maxElevation;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public void setUserRef(EntityRef<User> userRef) {
        this.userRef = userRef;
    }

    public void setRouteRef(EntityRef<Route> routeRef) {
        this.routeRef = routeRef;
    }

    public void setActivityTypeRef(EntityRef<ActivityType> activityTypeRef) {
        this.activityTypeRef = activityTypeRef;
    }

    public void setStartPointType(String startPointType) {
        this.startPointType = startPointType;
    }

    public void setStartingLocation(StartingLocation startingLocation) {
        this.startingLocation = startingLocation;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public void setRoutePoints(ArrayList<Point> routePoints) {
        this.routePoints = routePoints;
    }

    public void setClimbs(ArrayList<Climb> climbs) {
        this.climbs = climbs;
    }

    @Override // com.ua.sdk.route.Route
    public EntityRef<User> getUserRef() {
        List<Link> links;
        if (this.userRef == null && (links = getLinks("user")) != null) {
            this.userRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.userRef;
    }

    @Override // com.ua.sdk.route.Route
    public EntityRef<ActivityType> getActivityTypeRef() {
        List<Link> links;
        if (this.activityTypeRef == null && (links = getLinks("activity_type")) != null) {
            this.activityTypeRef = new LinkEntityRef(links.get(0).getId(), links.get(0).getHref());
        }
        return this.activityTypeRef;
    }

    @Override // com.ua.sdk.route.Route
    public String getThumbnailLink() {
        List<Link> links;
        if (this.thumbnailLink == null && (links = getLinks("thumbnail")) != null) {
            this.thumbnailLink = links.get(0).getHref();
        }
        return this.thumbnailLink;
    }

    @Override // com.ua.sdk.Resource
    public RouteRef getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new RouteRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeString(this.state);
        dest.writeString(this.postalCode);
        dest.writeValue(this.distanceMeters);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.dataSource);
        dest.writeLong(this.createdDate != null ? this.createdDate.getTime() : -1L);
        dest.writeLong(this.updatedDate != null ? this.updatedDate.getTime() : -1L);
        dest.writeValue(this.totalAscent);
        dest.writeValue(this.totalDescent);
        dest.writeValue(this.minElevation);
        dest.writeValue(this.maxElevation);
        dest.writeString(this.thumbnailLink);
        dest.writeList(this.routePoints);
        dest.writeList(this.climbs);
        dest.writeLong(this.mLocalId);
        dest.writeString(this.startPointType);
        dest.writeParcelable(this.startingLocation, flags);
        dest.writeParcelable(this.userRef, flags);
        dest.writeParcelable(this.routeRef, flags);
        dest.writeParcelable(this.activityTypeRef, flags);
        dest.writeParcelable(this.privacy, flags);
    }

    private RouteImpl(Parcel in) {
        super(in);
        this.city = in.readString();
        this.country = in.readString();
        this.state = in.readString();
        this.postalCode = in.readString();
        this.distanceMeters = (Double) in.readValue(Double.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.dataSource = in.readString();
        long tmpCreatedDate = in.readLong();
        this.createdDate = tmpCreatedDate == -1 ? null : new Date(tmpCreatedDate);
        long tmpUpdatedDate = in.readLong();
        this.updatedDate = tmpUpdatedDate != -1 ? new Date(tmpUpdatedDate) : null;
        this.totalAscent = (Double) in.readValue(Double.class.getClassLoader());
        this.totalDescent = (Double) in.readValue(Double.class.getClassLoader());
        this.minElevation = (Double) in.readValue(Double.class.getClassLoader());
        this.maxElevation = (Double) in.readValue(Double.class.getClassLoader());
        this.thumbnailLink = in.readString();
        this.routePoints = in.readArrayList(PointImpl.class.getClassLoader());
        this.climbs = in.readArrayList(ClimbImpl.class.getClassLoader());
        this.mLocalId = in.readLong();
        this.startPointType = in.readString();
        this.startingLocation = (StartingLocation) in.readParcelable(StartingLocation.class.getClassLoader());
        this.userRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.routeRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.activityTypeRef = (EntityRef) in.readParcelable(LinkEntityRef.class.getClassLoader());
        this.privacy = (Privacy) in.readParcelable(Privacy.class.getClassLoader());
    }
}
