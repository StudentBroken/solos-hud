package com.ua.sdk.bodymass;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassRef implements EntityRef<BodyMass> {
    public static Parcelable.Creator<BodyMassRef> CREATOR = new Parcelable.Creator<BodyMassRef>() { // from class: com.ua.sdk.bodymass.BodyMassRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassRef createFromParcel(Parcel source) {
            return new BodyMassRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassRef[] newArray(int size) {
            return new BodyMassRef[size];
        }
    };
    private final String id;

    private BodyMassRef(Builder init) {
        this.id = init.id;
    }

    @Override // com.ua.sdk.Reference
    public String getId() {
        if (this.id == null || this.id.length() <= 0) {
            return null;
        }
        return this.id;
    }

    @Override // com.ua.sdk.Reference
    public String getHref() {
        if (this.id == null || this.id.isEmpty()) {
            return null;
        }
        return String.format(UrlBuilderImpl.BODYMASS_URL, this.id);
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder extends BaseReferenceBuilder {
        private String id;

        private Builder() {
            super(UrlBuilderImpl.BODYMASS_URL);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public BodyMassRef build() {
            Precondition.isNotNull(this.id, "BodyMass Id");
            return new BodyMassRef(this);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    private BodyMassRef(Parcel in) {
        this.id = in.readString();
    }
}
