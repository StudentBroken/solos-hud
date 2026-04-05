package com.ua.sdk.user.role;

import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import com.ua.sdk.role.Role;

/* JADX INFO: loaded from: classes65.dex */
public class UserRoleImpl extends ApiTransferObject implements UserRole {
    private transient Link resource;
    private transient Link role;
    private transient Link user;

    public UserRoleImpl() {
    }

    private UserRoleImpl(Builder in) {
        this.role = in.role;
        this.user = in.user;
        this.resource = in.resource;
    }

    @Override // com.ua.sdk.user.role.UserRole
    public Link getRole() {
        return this.role != null ? this.role : getLink("role");
    }

    @Override // com.ua.sdk.user.role.UserRole
    public Link getUser() {
        return this.user != null ? this.user : getLink("user");
    }

    @Override // com.ua.sdk.user.role.UserRole
    public Link getResource() {
        return this.resource != null ? this.resource : getLink("resource");
    }

    @Override // com.ua.sdk.user.role.UserRole
    public void setRole(Link role) {
        this.role = role;
    }

    @Override // com.ua.sdk.user.role.UserRole
    public void setUser(Link user) {
        this.user = user;
    }

    @Override // com.ua.sdk.user.role.UserRole
    public void setResource(Link resource) {
        this.resource = resource;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<UserRole> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static class Builder {
        Link resource;
        Link role;
        Link user;

        public Builder setRole(Role.Type role) {
            this.role = new Link((String) null, role.getValue());
            return this;
        }

        public Builder setUser(String user) {
            this.user = new Link((String) null, user);
            return this;
        }

        public Builder setResource(String resource) {
            this.resource = new Link(resource);
            return this;
        }

        public UserRoleImpl build() {
            return new UserRoleImpl(this);
        }
    }
}
