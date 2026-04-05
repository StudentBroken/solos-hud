package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.user.User;

/* JADX INFO: loaded from: classes65.dex */
public class RouteListRef implements EntityListRef<Route>, Parcelable {
    public static final Parcelable.Creator<RouteListRef> CREATOR = new Parcelable.Creator<RouteListRef>() { // from class: com.ua.sdk.route.RouteListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteListRef createFromParcel(Parcel source) {
            return new RouteListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteListRef[] newArray(int size) {
            return new RouteListRef[size];
        }
    };
    private String href;

    private RouteListRef(Builder init) {
        Precondition.isNotNull(init);
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.href;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private String city;
        private String country;
        private String fieldSet;
        private double latitude;
        private double longitude;
        private double maxDistance;
        private double minDistance;
        private Privacy.Level privacy;
        private double radius;
        private String state;
        private EntityRef<User> user;

        protected Builder() {
            super("/v7.0/route/");
        }

        public Builder setUser(EntityRef<User> user) {
            this.user = user;
            setParam("user", user.getId());
            return this;
        }

        public Builder setLocation(double lat, double lng) {
            this.latitude = lat;
            this.longitude = lng;
            setParam("close_to_location", String.format("%f,%f", Double.valueOf(this.latitude), Double.valueOf(this.longitude)));
            return this;
        }

        public Builder setRadius(double radius) {
            this.radius = radius;
            setParam("search_radius", String.valueOf(radius));
            return this;
        }

        public Builder setMinDistance(double minDistance) {
            this.minDistance = minDistance;
            setParam("minimum_distance", String.valueOf(minDistance));
            return this;
        }

        public Builder setMaxDistance(double maxDistance) {
            this.maxDistance = maxDistance;
            setParam("maximum_distance", String.valueOf(maxDistance));
            return this;
        }

        public Builder setPrivacy(Privacy.Level privacy) {
            this.privacy = privacy;
            setParam(ShareConstants.WEB_DIALOG_PARAM_PRIVACY, privacy.id);
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            setParam("city", String.valueOf(city));
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            setParam(ServerProtocol.DIALOG_PARAM_STATE, String.valueOf(state));
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            setParam("country", String.valueOf(country));
            return this;
        }

        public Builder setFieldSet(String fieldSet) {
            this.fieldSet = fieldSet;
            setParam("field_set", String.valueOf(fieldSet));
            return this;
        }

        public RouteListRef build() {
            if (getParam("user") == null && getParam("close_to_location") == null) {
                throw new IllegalStateException("Must specify either user or location.");
            }
            return new RouteListRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    private RouteListRef(Parcel in) {
        this.href = in.readString();
    }
}
