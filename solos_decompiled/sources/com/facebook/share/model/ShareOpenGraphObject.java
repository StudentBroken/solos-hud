package com.facebook.share.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.NativeProtocol;
import com.facebook.share.model.ShareOpenGraphValueContainer;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareOpenGraphObject extends ShareOpenGraphValueContainer<ShareOpenGraphObject, Builder> {
    public static final Parcelable.Creator<ShareOpenGraphObject> CREATOR = new Parcelable.Creator<ShareOpenGraphObject>() { // from class: com.facebook.share.model.ShareOpenGraphObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphObject createFromParcel(Parcel in) {
            return new ShareOpenGraphObject(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ShareOpenGraphObject[] newArray(int size) {
            return new ShareOpenGraphObject[size];
        }
    };

    private ShareOpenGraphObject(Builder builder) {
        super(builder);
    }

    ShareOpenGraphObject(Parcel in) {
        super(in);
    }

    public static final class Builder extends ShareOpenGraphValueContainer.Builder<ShareOpenGraphObject, Builder> {
        public Builder() {
            putBoolean(NativeProtocol.OPEN_GRAPH_CREATE_OBJECT_KEY, true);
        }

        @Override // com.facebook.share.ShareBuilder
        public ShareOpenGraphObject build() {
            return new ShareOpenGraphObject(this);
        }

        Builder readFrom(Parcel parcel) {
            return readFrom((ShareOpenGraphObject) parcel.readParcelable(ShareOpenGraphObject.class.getClassLoader()));
        }
    }
}
