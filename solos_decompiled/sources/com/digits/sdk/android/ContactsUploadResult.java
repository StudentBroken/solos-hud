package com.digits.sdk.android;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes18.dex */
public class ContactsUploadResult implements Parcelable {
    public static final Parcelable.Creator<ContactsUploadResult> CREATOR = new Parcelable.Creator<ContactsUploadResult>() { // from class: com.digits.sdk.android.ContactsUploadResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContactsUploadResult createFromParcel(Parcel in) {
            return new ContactsUploadResult(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContactsUploadResult[] newArray(int size) {
            return new ContactsUploadResult[size];
        }
    };
    public final int successCount;
    public final int totalCount;

    ContactsUploadResult(int successCount, int totalCount) {
        this.successCount = successCount;
        this.totalCount = totalCount;
    }

    ContactsUploadResult(Parcel parcel) {
        this.successCount = parcel.readInt();
        this.totalCount = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.successCount);
        parcel.writeInt(this.totalCount);
    }
}
