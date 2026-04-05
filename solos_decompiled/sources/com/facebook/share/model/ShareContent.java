package com.facebook.share.model;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Nullable;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareContent.Builder;
import com.facebook.share.model.ShareHashtag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes41.dex */
public abstract class ShareContent<P extends ShareContent, E extends Builder> implements ShareModel {
    private final Uri contentUrl;
    private final ShareHashtag hashtag;
    private final List<String> peopleIds;
    private final String placeId;
    private final String ref;

    protected ShareContent(Builder builder) {
        this.contentUrl = builder.contentUrl;
        this.peopleIds = builder.peopleIds;
        this.placeId = builder.placeId;
        this.ref = builder.ref;
        this.hashtag = builder.hashtag;
    }

    protected ShareContent(Parcel in) {
        this.contentUrl = (Uri) in.readParcelable(Uri.class.getClassLoader());
        this.peopleIds = readUnmodifiableStringList(in);
        this.placeId = in.readString();
        this.ref = in.readString();
        this.hashtag = new ShareHashtag.Builder().readFrom(in).build();
    }

    @Nullable
    public Uri getContentUrl() {
        return this.contentUrl;
    }

    @Nullable
    public List<String> getPeopleIds() {
        return this.peopleIds;
    }

    @Nullable
    public String getPlaceId() {
        return this.placeId;
    }

    @Nullable
    public String getRef() {
        return this.ref;
    }

    @Nullable
    public ShareHashtag getShareHashtag() {
        return this.hashtag;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.contentUrl, 0);
        out.writeStringList(this.peopleIds);
        out.writeString(this.placeId);
        out.writeString(this.ref);
        out.writeParcelable(this.hashtag, 0);
    }

    private List<String> readUnmodifiableStringList(Parcel in) {
        List<String> list = new ArrayList<>();
        in.readStringList(list);
        if (list.size() == 0) {
            return null;
        }
        return Collections.unmodifiableList(list);
    }

    public static abstract class Builder<P extends ShareContent, E extends Builder> implements ShareModelBuilder<P, E> {
        private Uri contentUrl;
        private ShareHashtag hashtag;
        private List<String> peopleIds;
        private String placeId;
        private String ref;

        public E setContentUrl(@Nullable Uri contentUrl) {
            this.contentUrl = contentUrl;
            return this;
        }

        public E setPeopleIds(@Nullable List<String> peopleIds) {
            this.peopleIds = peopleIds == null ? null : Collections.unmodifiableList(peopleIds);
            return this;
        }

        public E setPlaceId(@Nullable String placeId) {
            this.placeId = placeId;
            return this;
        }

        public E setRef(@Nullable String ref) {
            this.ref = ref;
            return this;
        }

        public E setShareHashtag(@Nullable ShareHashtag shareHashtag) {
            this.hashtag = shareHashtag;
            return this;
        }

        @Override // com.facebook.share.model.ShareModelBuilder
        public E readFrom(P content) {
            return content == null ? this : setContentUrl(content.getContentUrl()).setPeopleIds(content.getPeopleIds()).setPlaceId(content.getPlaceId()).setRef(content.getRef());
        }
    }
}
