package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.activitystory.Attachment;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes65.dex */
public class Attachments implements Parcelable {
    public static final Parcelable.Creator<Attachments> CREATOR = new Parcelable.Creator<Attachments>() { // from class: com.ua.sdk.activitystory.Attachments.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Attachments createFromParcel(Parcel source) {
            return new Attachments(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Attachments[] newArray(int size) {
            return new Attachments[size];
        }
    };

    @SerializedName("items")
    ArrayList<Attachment> attachments;

    @SerializedName("count")
    Integer count;

    public Attachment getAttachment(int index) {
        if (this.attachments == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.attachments.get(index);
    }

    public int getCount() {
        if (this.count == null) {
            if (this.attachments == null) {
                return 0;
            }
            return this.attachments.size();
        }
        return this.count.intValue();
    }

    public void addAttachment(Attachment.Type type) {
        if (this.attachments == null) {
            this.attachments = new ArrayList<>();
        }
        if (type == Attachment.Type.PHOTO) {
            this.attachments.add(new PhotoAttachmentImpl(type));
        }
        if (type == Attachment.Type.VIDEO) {
            this.attachments.add(new VideoAttachmentImpl(type));
        }
    }

    public ArrayList<Attachment> getAttachments() {
        return this.attachments;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.count);
        dest.writeList(this.attachments);
    }

    public Attachments() {
    }

    private Attachments(Parcel in) {
        this.count = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attachments = in.readArrayList(PhotoAttachmentImpl.class.getClassLoader());
    }
}
