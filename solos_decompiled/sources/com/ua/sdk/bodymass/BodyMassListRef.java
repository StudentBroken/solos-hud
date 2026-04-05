package com.ua.sdk.bodymass;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.oss.org.codehaus.jackson.map.util.Iso8601DateFormat;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.internal.BaseReferenceBuilder;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class BodyMassListRef implements EntityListRef<BodyMass> {
    public static Parcelable.Creator<BodyMassListRef> CREATOR = new Parcelable.Creator<BodyMassListRef>() { // from class: com.ua.sdk.bodymass.BodyMassListRef.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassListRef createFromParcel(Parcel source) {
            return new BodyMassListRef(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BodyMassListRef[] newArray(int size) {
            return new BodyMassListRef[size];
        }
    };
    private final String href;

    private BodyMassListRef(Parcel in) {
        this.href = in.readString();
    }

    private BodyMassListRef(Builder init) {
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
        private static final String TARGET_END_DT_KEY = "target_end_datetime";
        private static final String TARGET_START_DT_KEY = "target_start_datetime";

        public Builder() {
            super(UrlBuilderImpl.BODYMASS_COLLECTION_URL);
        }

        public Builder setStartDateTime(Date startDateTime) {
            Precondition.isNotNull(startDateTime);
            setParam("target_start_datetime", Iso8601DateFormat.format(startDateTime));
            return this;
        }

        public Builder setEndDateTime(Date endDateTime) {
            Precondition.isNotNull(endDateTime);
            setParam("target_end_datetime", Iso8601DateFormat.format(endDateTime));
            return this;
        }

        public BodyMassListRef build() {
            Precondition.isNotNull(getParam("target_start_datetime"), "Start DateTime");
            return new BodyMassListRef(this);
        }
    }
}
