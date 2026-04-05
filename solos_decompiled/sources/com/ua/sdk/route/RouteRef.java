package com.ua.sdk.route;

import android.os.Parcel;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.LinkEntityRef;

/* JADX INFO: loaded from: classes65.dex */
public class RouteRef extends LinkEntityRef<Route> {
    public RouteRef(String href) {
        super(href);
    }

    public RouteRef(String id, String href) {
        super(id, href);
    }

    public RouteRef(String id, long localId, String href) {
        super(id, localId, href);
    }

    public RouteRef(Parcel in) {
        super(in);
    }

    private RouteRef(Builder init) {
        super(init.id, init.getHref());
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static class Builder extends BaseReferenceBuilder {
        private String fieldSet;
        private String id;

        protected Builder() {
            super("/v7.0/route/{id}/");
        }

        public Builder setId(String id) {
            this.id = id;
            setParam("id", id);
            return this;
        }

        public Builder setFieldSet(String fieldSet) {
            this.fieldSet = fieldSet;
            setParam("field_set", String.valueOf(fieldSet));
            return this;
        }

        public RouteRef build() {
            return new RouteRef(getHref());
        }
    }
}
