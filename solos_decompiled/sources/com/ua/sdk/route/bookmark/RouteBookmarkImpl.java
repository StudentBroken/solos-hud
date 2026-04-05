package com.ua.sdk.route.bookmark;

import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.route.RouteBookmark;

/* JADX INFO: loaded from: classes65.dex */
public class RouteBookmarkImpl extends ApiTransferObject implements RouteBookmark {
    private String fromUserHref;
    private transient Link route;
    private transient Link user;

    public RouteBookmarkImpl() {
    }

    private RouteBookmarkImpl(Builder in) {
        this.route = in.route;
        this.user = in.user;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<RouteBookmark> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public Link getRoute() {
        return this.route != null ? this.route : getLink("route");
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public Link getUser() {
        return this.user != null ? this.user : getLink("user");
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public String getFromUserHref() {
        return this.fromUserHref;
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public void setRoute(Link route) {
        setLink("route", route);
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public void setUser(Link user) {
        setLink("user", user);
    }

    @Override // com.ua.sdk.route.RouteBookmark
    public void setFromUserHref(String fromUserHref) {
        this.fromUserHref = fromUserHref;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static class Builder {
        Link route;
        Link user;

        public Builder setRoute(String route) {
            this.route = new Link((String) null, route);
            return this;
        }

        public Builder setUser(String user) {
            this.user = new Link((String) null, user);
            return this;
        }

        public RouteBookmarkImpl build() {
            return new RouteBookmarkImpl(this);
        }
    }
}
