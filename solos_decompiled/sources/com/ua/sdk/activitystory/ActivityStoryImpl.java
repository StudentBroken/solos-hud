package com.ua.sdk.activitystory;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.ua.sdk.EntityRef;
import com.ua.sdk.Source;
import com.ua.sdk.internal.ApiTransferObject;
import com.ua.sdk.internal.Link;
import com.ua.sdk.internal.LinkEntityRef;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryImpl extends ApiTransferObject implements ActivityStory {
    public static Parcelable.Creator<ActivityStoryImpl> CREATOR = new Parcelable.Creator<ActivityStoryImpl>() { // from class: com.ua.sdk.activitystory.ActivityStoryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryImpl createFromParcel(Parcel source) {
            return new ActivityStoryImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ActivityStoryImpl[] newArray(int size) {
            return new ActivityStoryImpl[size];
        }
    };

    @SerializedName("actor")
    ActivityStoryActor mActor;

    @SerializedName("attachments")
    Attachments mAttachments;

    @SerializedName("comments")
    ActivityStoryReplySummaryImpl mCommentSummary;

    @SerializedName("id")
    String mId;

    @SerializedName("likes")
    ActivityStoryReplySummaryImpl mLikeSummary;

    @SerializedName("object")
    ActivityStoryObject mObject;

    @SerializedName("published")
    Date mPublishedTime;

    @SerializedName("reposts")
    ActivityStoryRepostSummaryImpl mRepostSummary;

    @SerializedName("sharing")
    SocialSettings mSharingSettngs;

    @SerializedName("target")
    ActivityStoryTarget mTarget;

    @SerializedName("template")
    ActivityStoryTemplateImpl mTemplate;

    @SerializedName("verb")
    ActivityStoryVerb mVerb;

    @SerializedName("source")
    Source source;

    @Override // com.ua.sdk.activitystory.ActivityStory
    public String getId() {
        return this.mId;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryActor getActor() {
        return this.mActor;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryObject getObject() {
        return this.mObject;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryVerb getVerb() {
        return this.mVerb;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryTemplate getTemplate() {
        return this.mTemplate;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public Date getPublished() {
        return this.mPublishedTime;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public EntityRef<ActivityStory> getTargetRef() {
        if (this.mTarget != null) {
            return new LinkEntityRef(this.mTarget.getId(), "");
        }
        return null;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryReplySummary getCommentsSummary() {
        return this.mCommentSummary;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryReplySummary getLikesSummary() {
        return this.mLikeSummary;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryRepostSummary getRepostSummary() {
        return this.mRepostSummary;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryListRef getCommmentsRef() {
        return ActivityStoryListRef.getBuilder().setId(getRef().getId()).setReplyType(ActivityStoryReplyType.COMMENTS).build();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryListRef getLikesRef() {
        return ActivityStoryListRef.getBuilder().setId(getRef().getId()).setReplyType(ActivityStoryReplyType.LIKES).build();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public ActivityStoryListRef getRepostsRef() {
        return ActivityStoryListRef.getBuilder().setId(getRef().getId()).setReplyType(ActivityStoryReplyType.REPOSTS).build();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public boolean isLikedByCurrentUser() {
        if (this.mLikeSummary != null) {
            return this.mLikeSummary.isReplied();
        }
        return false;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public boolean isCommentedByCurrentUser() {
        if (this.mCommentSummary != null) {
            return this.mCommentSummary.isReplied();
        }
        return false;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public boolean isRepostedByCurrentUser() {
        if (this.mRepostSummary != null) {
            return this.mRepostSummary.isReposted();
        }
        return false;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public int getLikeCount() {
        if (this.mLikeSummary == null) {
            return 0;
        }
        return this.mLikeSummary.getTotalCount();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public int getCommentCount() {
        if (this.mCommentSummary == null) {
            return 0;
        }
        return this.mCommentSummary.getTotalCount();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public int getRepostCount() {
        if (this.mRepostSummary == null) {
            return 0;
        }
        return this.mRepostSummary.getTotalCount();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public int getAttachmentCount() {
        if (this.mAttachments == null) {
            return 0;
        }
        return this.mAttachments.getCount();
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public Attachment getAttachment(int index) throws IndexOutOfBoundsException {
        if (this.mAttachments == null) {
            throw new IndexOutOfBoundsException("Activity Story does not have any attachments.");
        }
        return this.mAttachments.getAttachment(index);
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public SocialSettings getSocialSettings() {
        return this.mSharingSettngs;
    }

    @Override // com.ua.sdk.activitystory.ActivityStory
    public Source getSource() {
        return this.source;
    }

    @Override // com.ua.sdk.Resource
    public EntityRef<ActivityStory> getRef() {
        Link self = getLink("self");
        if (self == null) {
            return null;
        }
        return new LinkEntityRef(self.getId(), self.getHref());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.ua.sdk.internal.ApiTransferObject, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mId);
        dest.writeParcelable(this.mActor, 0);
        dest.writeInt(this.mVerb == null ? -1 : this.mVerb.ordinal());
        dest.writeParcelable(this.mObject, 0);
        dest.writeLong(this.mPublishedTime != null ? this.mPublishedTime.getTime() : -1L);
        dest.writeParcelable(this.mTemplate, flags);
        dest.writeParcelable(this.mTarget, flags);
        dest.writeParcelable(this.mCommentSummary, flags);
        dest.writeParcelable(this.mLikeSummary, flags);
        dest.writeParcelable(this.mRepostSummary, flags);
        dest.writeParcelable(this.mAttachments, flags);
        dest.writeParcelable(this.mSharingSettngs, flags);
        dest.writeParcelable(this.source, flags);
    }

    public ActivityStoryImpl() {
    }

    private ActivityStoryImpl(Parcel in) {
        super(in);
        this.mId = in.readString();
        this.mActor = (ActivityStoryActor) in.readParcelable(ActivityStoryActor.class.getClassLoader());
        int tmpMVerb = in.readInt();
        this.mVerb = tmpMVerb == -1 ? null : ActivityStoryVerb.values()[tmpMVerb];
        this.mObject = (ActivityStoryObject) in.readParcelable(ActivityStoryObject.class.getClassLoader());
        long tmpMPublishedTime = in.readLong();
        this.mPublishedTime = tmpMPublishedTime != -1 ? new Date(tmpMPublishedTime) : null;
        this.mTemplate = (ActivityStoryTemplateImpl) in.readParcelable(ActivityStoryTemplate.class.getClassLoader());
        this.mTarget = (ActivityStoryTarget) in.readParcelable(ActivityStoryTarget.class.getClassLoader());
        this.mCommentSummary = (ActivityStoryReplySummaryImpl) in.readParcelable(ActivityStoryReplySummaryImpl.class.getClassLoader());
        this.mLikeSummary = (ActivityStoryReplySummaryImpl) in.readParcelable(ActivityStoryReplySummaryImpl.class.getClassLoader());
        this.mRepostSummary = (ActivityStoryRepostSummaryImpl) in.readParcelable(ActivityStoryReplySummaryImpl.class.getClassLoader());
        this.mAttachments = (Attachments) in.readParcelable(Attachments.class.getClassLoader());
        this.mSharingSettngs = (SocialSettings) in.readParcelable(SocialSettings.class.getClassLoader());
        this.source = (Source) in.readParcelable(Source.class.getClassLoader());
    }
}
