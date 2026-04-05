package com.ua.sdk.privacy;

import android.os.Parcel;
import android.os.Parcelable;
import com.ua.sdk.EntityRef;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.privacy.Privacy;

/* JADX INFO: loaded from: classes65.dex */
public class PrivacyImpl extends ApiTransferObject implements Privacy, Parcelable {
    public static Parcelable.Creator<PrivacyImpl> CREATOR = new Parcelable.Creator<PrivacyImpl>() { // from class: com.ua.sdk.privacy.PrivacyImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivacyImpl createFromParcel(Parcel source) {
            return new PrivacyImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrivacyImpl[] newArray(int size) {
            return new PrivacyImpl[size];
        }
    };
    private String mDescription;
    private Privacy.Level mLevel;

    protected PrivacyImpl(Privacy.Level level, String description) {
        this.mLevel = (Privacy.Level) Precondition.isNotNull(level);
        this.mDescription = description;
    }

    @Override // com.ua.sdk.privacy.Privacy
    public String getPrivacyDescription() {
        return this.mDescription;
    }

    @Override // com.ua.sdk.privacy.Privacy
    public Privacy.Level getLevel() {
        return this.mLevel;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<Privacy> getRef() {
        return null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mLevel.toString());
        dest.writeString(this.mDescription);
    }

    private PrivacyImpl(Parcel in) {
        super(in);
        this.mLevel = Privacy.Level.valueOf(in.readString());
        this.mDescription = in.readString();
    }
}
