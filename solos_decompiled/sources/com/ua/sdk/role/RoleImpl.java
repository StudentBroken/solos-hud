package com.ua.sdk.role;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.NativeProtocol;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.Reference;
import com.ua.sdk.role.Role;
import java.util.List;

/* JADX INFO: loaded from: classes65.dex */
public class RoleImpl implements Role {
    public static Parcelable.Creator<RoleImpl> CREATOR = new Parcelable.Creator<RoleImpl>() { // from class: com.ua.sdk.role.RoleImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RoleImpl createFromParcel(Parcel source) {
            return new RoleImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RoleImpl[] newArray(int size) {
            return new RoleImpl[size];
        }
    };

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private Role.Type name;

    @SerializedName(NativeProtocol.RESULT_ARGS_PERMISSIONS)
    private List<Role.Permission> permissions;

    public RoleImpl() {
    }

    private RoleImpl(Parcel in) {
        this.description = in.readString();
        int nameTemp = in.readInt();
        this.name = nameTemp == -1 ? null : Role.Type.values()[nameTemp];
        this.permissions = in.readArrayList(Role.Permission.class.getClassLoader());
    }

    protected RoleImpl(Role.Type name, List<Role.Permission> permissions, String description) {
        this.name = name;
        this.permissions = permissions;
        this.description = description;
    }

    @Override // com.ua.sdk.role.Role
    public String getDescription() {
        return this.description;
    }

    @Override // com.ua.sdk.role.Role
    public Role.Type getName() {
        return this.name;
    }

    @Override // com.ua.sdk.role.Role
    public List<Role.Permission> getPermissions() {
        return this.permissions;
    }

    @Override // com.ua.sdk.role.Role
    public void setDescription(String description) {
        this.description = description;
    }

    @Override // com.ua.sdk.role.Role
    public void setName(Role.Type name) {
        this.name = name;
    }

    @Override // com.ua.sdk.role.Role
    public void setPermissions(List<Role.Permission> permissions) {
        this.permissions = permissions;
    }

    @Override // com.ua.sdk.Resource
    public Reference getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeInt(this.name == null ? -1 : this.name.ordinal());
        dest.writeList(this.permissions);
    }
}
