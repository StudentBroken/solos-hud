package com.ua.sdk.group;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.group.purpose.GroupPurposeType;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class GroupListRef implements EntityListRef<Group> {
    public static Parcelable.Creator<GroupListRef> CREATOR = new Parcelable.Creator<GroupListRef>() { // from class: com.ua.sdk.group.GroupListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupListRef createFromParcel(Parcel source) {
            return new GroupListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupListRef[] newArray(int size) {
            return new GroupListRef[size];
        }
    };
    private String params;

    private GroupListRef(Builder init) {
        this.params = "";
        this.params = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return null;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return this.params;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        String view;

        private Builder() {
            super(UrlBuilderImpl.GET_GROUPS_LIST_URL);
        }

        public Builder setUser(String id) {
            setParam("user_id", id);
            return this;
        }

        public Builder setGroupViewType(GroupViewType type) {
            Precondition.isNotNull(type);
            this.view = type.toString();
            setParam("view", type.toString());
            return this;
        }

        public GroupListRef build() {
            if (this.view != null) {
                setParam("purpose", GroupPurposeType.CHALLENGE.toString().toLowerCase());
            }
            return new GroupListRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.params);
    }

    private GroupListRef(Parcel in) {
        this.params = "";
        this.params = in.readString();
    }
}
