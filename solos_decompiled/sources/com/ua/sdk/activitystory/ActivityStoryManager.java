package com.ua.sdk.activitystory;

import android.net.Uri;
import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.UploadCallback;
import com.ua.sdk.privacy.Privacy;
import java.io.IOException;

/* JADX INFO: loaded from: classes65.dex */
public interface ActivityStoryManager {
    Request attachImageWithStatus(ActivityStory activityStory, Uri uri, UploadCallback uploadCallback);

    Request attachVideoWithStatus(ActivityStory activityStory, Uri uri, UploadCallback uploadCallback);

    void cancelUpload() throws IOException;

    Request createComment(String str, EntityRef<ActivityStory> entityRef, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    ActivityStory createComment(String str, EntityRef<ActivityStory> entityRef, ActivityStoryActor activityStoryActor) throws UaException;

    Request createLike(EntityRef<ActivityStory> entityRef, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    ActivityStory createLike(EntityRef<ActivityStory> entityRef, ActivityStoryActor activityStoryActor) throws UaException;

    Request createStatus(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    Request createStatus(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget, CreateCallback<ActivityStory> createCallback);

    ActivityStory createStatus(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor) throws UaException;

    ActivityStory createStatus(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget) throws UaException;

    Request createStatusWithImage(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    Request createStatusWithImage(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget, CreateCallback<ActivityStory> createCallback);

    ActivityStory createStatusWithImage(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor) throws UaException;

    ActivityStory createStatusWithImage(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget) throws UaException;

    Request createStatusWithVideo(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    Request createStatusWithVideo(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget, CreateCallback<ActivityStory> createCallback);

    ActivityStory createStatusWithVideo(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor) throws UaException;

    ActivityStory createStatusWithVideo(String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, ActivityStoryTarget activityStoryTarget) throws UaException;

    Request deleteLike(ActivityStory activityStory, DeleteCallback<EntityRef<ActivityStory>> deleteCallback);

    EntityRef<ActivityStory> deleteStory(EntityRef<ActivityStory> entityRef) throws UaException;

    Request deleteStory(EntityRef<ActivityStory> entityRef, DeleteCallback<EntityRef<ActivityStory>> deleteCallback);

    Request fetchActivityStory(EntityRef<ActivityStory> entityRef, FetchCallback<ActivityStory> fetchCallback);

    ActivityStory fetchActivityStory(EntityRef<ActivityStory> entityRef) throws UaException;

    EntityList<ActivityStory> fetchActivityStoryList(EntityListRef<ActivityStory> entityListRef) throws UaException;

    Request fetchActivityStoryList(EntityListRef<ActivityStory> entityListRef, FetchCallback<EntityList<ActivityStory>> fetchCallback);

    Request repostStatus(ActivityStory activityStory, String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor, CreateCallback<ActivityStory> createCallback);

    ActivityStory repostStatus(ActivityStory activityStory, String str, Privacy.Level level, SocialSettings socialSettings, ActivityStoryActor activityStoryActor) throws UaException;

    Request updateStoryPrivacy(ActivityStory activityStory, Privacy.Level level, SaveCallback<ActivityStory> saveCallback);

    ActivityStory updateStoryPrivacy(ActivityStory activityStory, Privacy.Level level) throws UaException;

    Request updateStoryStatus(ActivityStory activityStory, String str, SaveCallback<ActivityStory> saveCallback);

    ActivityStory updateStoryStatus(ActivityStory activityStory, String str) throws UaException;
}
