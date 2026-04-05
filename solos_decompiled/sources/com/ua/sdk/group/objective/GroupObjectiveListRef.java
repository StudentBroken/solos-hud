package com.ua.sdk.group.objective;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.group.Group;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;

/* JADX INFO: loaded from: classes65.dex */
public class GroupObjectiveListRef implements EntityListRef<GroupObjective> {
    public static Parcelable.Creator<GroupObjectiveListRef> CREATOR = new Parcelable.Creator<GroupObjectiveListRef>() { // from class: com.ua.sdk.group.objective.GroupObjectiveListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveListRef createFromParcel(Parcel source) {
            return new GroupObjectiveListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GroupObjectiveListRef[] newArray(int size) {
            return new GroupObjectiveListRef[size];
        }
    };
    private final String href;

    private GroupObjectiveListRef(Parcel in) {
        this.href = in.readString();
    }

    private GroupObjectiveListRef(Builder init) {
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

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.href);
    }

    public static class Builder extends BaseReferenceBuilder {
        private static final String GROUP_ID_KEY = "group_id";
        String id;

        public Builder() {
            super("/v7.0/group_objective/");
        }

        public Builder setGroupRef(EntityRef<Group> ref) {
            Precondition.isNotNull(ref);
            Precondition.isNotNull(ref.getId());
            this.id = ref.getId();
            setParam("group_id", this.id);
            return this;
        }

        public GroupObjectiveListRef build() {
            Precondition.isNotNull(this.id);
            return new GroupObjectiveListRef(this);
        }
    }
}
