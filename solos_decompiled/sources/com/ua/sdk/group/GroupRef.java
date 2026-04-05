package com.ua.sdk.group;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class GroupRef implements EntityRef<Group> {
    public static Parcelable.Creator<GroupRef> CREATOR = new Parcelable.Creator<GroupRef>() { // from class: com.ua.sdk.group.GroupRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupRef createFromParcel(Parcel source) {
            return new GroupRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupRef[] newArray(int size) {
            return new GroupRef[size];
        }
    };
    private final String href;
    private final String id;

    private GroupRef(Builder init) {
        this.id = init.id;
        this.href = init.getHref();
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        return String.format(this.href, this.id);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private static final String INVITATION_CODE_KEY = "invitation_code";
        private String code;
        private String id;

        private Builder() {
            super(UrlBuilderImpl.GET_GROUP_URL);
        }

        public Builder setGroupId(String id) {
            this.id = id;
            return this;
        }

        public Builder setInvitationCode(String code) {
            this.code = code;
            return this;
        }

        public GroupRef build() {
            Precondition.isNotNull(this.id, "Group Id");
            if (this.code != null) {
                setParam(INVITATION_CODE_KEY, this.code);
            }
            return new GroupRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.href);
    }

    private GroupRef(Parcel in) {
        this.id = in.readString();
        this.href = in.readString();
    }
}
