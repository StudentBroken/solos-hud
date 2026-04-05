package com.ua.sdk.activitystory;

import com.ua.sdk.Entity;
import com.ua.sdk.EntityRef;
import com.ua.sdk.Source;
import java.util.Date;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStory extends Entity<EntityRef<ActivityStory>> {
    ActivityStoryActor getActor();

    Attachment getAttachment(int i) throws IndexOutOfBoundsException;

    int getAttachmentCount();

    int getCommentCount();

    ActivityStoryReplySummary getCommentsSummary();

    ActivityStoryListRef getCommmentsRef();

    String getId();

    int getLikeCount();

    ActivityStoryListRef getLikesRef();

    ActivityStoryReplySummary getLikesSummary();

    ActivityStoryObject getObject();

    Date getPublished();

    int getRepostCount();

    ActivityStoryRepostSummary getRepostSummary();

    ActivityStoryListRef getRepostsRef();

    SocialSettings getSocialSettings();

    Source getSource();

    EntityRef<ActivityStory> getTargetRef();

    ActivityStoryTemplate getTemplate();

    ActivityStoryVerb getVerb();

    boolean isCommentedByCurrentUser();

    boolean isLikedByCurrentUser();

    boolean isRepostedByCurrentUser();
}
