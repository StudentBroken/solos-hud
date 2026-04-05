package com.ua.sdk.route;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBuilderImpl implements RouteBuilder, Parcelable {
    public static final Parcelable.Creator<RouteBuilderImpl> CREATOR = new Parcelable.Creator<RouteBuilderImpl>() { // from class: com.ua.sdk.route.RouteBuilderImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteBuilderImpl createFromParcel(Parcel source) {
            return new RouteBuilderImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RouteBuilderImpl[] newArray(int size) {
            return new RouteBuilderImpl[size];
        }
    };
    String description;
    String name;
    ArrayList<Point> points;
    String postalCode;
    String startPointType;

    public RouteBuilderImpl() {
        this.points = new ArrayList<>();
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public RouteBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public RouteBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public RouteBuilder setStartPointType(String startPointType) {
        this.startPointType = startPointType;
        return this;
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public RouteBuilder setPoints(ArrayList<Point> points) {
        this.points = points;
        return this;
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public RouteBuilder setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    @Override // com.ua.sdk.route.RouteBuilder
    public Route build() {
        if (this.name == null) {
            throw new IllegalArgumentException("A name must be specified.");
        }
        if (this.points == null || this.points.isEmpty()) {
            throw new IllegalArgumentException("Points must be specified.");
        }
        return new RouteImpl(this);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeString(this.startPointType);
        dest.writeList(this.points);
        dest.writeString(this.postalCode);
    }

    private RouteBuilderImpl(Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
        this.startPointType = in.readString();
        this.points = new ArrayList<>();
        in.readList(this.points, PointImpl.class.getClassLoader());
        this.postalCode = in.readString();
    }
}
