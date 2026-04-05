package com.ua.sdk.moderation;

import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.NativeProtocol;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;

/* JADX INFO: loaded from: classes65.dex */
public class ModerationActionImpl extends ApiTransferObject implements ModerationAction {
    public static final Parcelable.Creator<ModerationActionImpl> CREATOR = new Parcelable.Creator<ModerationActionImpl>() { // from class: com.ua.sdk.moderation.ModerationActionImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModerationActionImpl createFromParcel(Parcel source) {
            return new ModerationActionImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModerationActionImpl[] newArray(int size) {
            return new ModerationActionImpl[size];
        }
    };

    @SerializedName(NativeProtocol.WEB_DIALOG_ACTION)
    String actionHref;

    @SerializedName("resource")
    String resourceHref;

    public ModerationActionImpl() {
    }

    @Override // com.ua.sdk.moderation.ModerationAction
    public String getAction() {
        return this.actionHref;
    }

    @Override // com.ua.sdk.moderation.ModerationAction
    public String getResource() {
        return this.resourceHref;
    }

    @Override // com.ua.sdk.moderation.ModerationAction
    public void setAction(String href) {
        this.actionHref = href;
    }

    @Override // com.ua.sdk.moderation.ModerationAction
    public void setResource(String href) {
        this.resourceHref = href;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.actionHref);
        dest.writeString(this.resourceHref);
    }

    private ModerationActionImpl(Parcel in) {
        super(in);
        this.actionHref = in.readString();
        this.resourceHref = in.readString();
    }
}
