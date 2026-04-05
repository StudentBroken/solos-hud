package com.ua.sdk.activitystory;

import android.net.Uri;
import com.ua.sdk.CreateCallback;
import com.ua.sdk.DeleteCallback;
import com.ua.sdk.EntityList;
import com.ua.sdk.EntityListRef;
import com.ua.sdk.EntityRef;
import com.ua.sdk.FetchCallback;
import com.ua.sdk.Reference;
import com.ua.sdk.Request;
import com.ua.sdk.SaveCallback;
import com.ua.sdk.UaException;
import com.ua.sdk.UploadCallback;
import com.ua.sdk.activitystory.ActivityStoryActor;
import com.ua.sdk.activitystory.ActivityStoryObject;
import com.ua.sdk.activitystory.ActivityStoryRpcPostObject;
import com.ua.sdk.activitystory.Attachment;
import com.ua.sdk.activitystory.actor.ActivityStoryUserActorImpl;
import com.ua.sdk.activitystory.object.ActivityStoryCommentObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryLikeObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryPrivacyObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryRepostObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryRouteObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryStatusObjectImpl;
import com.ua.sdk.activitystory.object.ActivityStoryWorkoutObjectImpl;
import com.ua.sdk.activitystory.target.ActivityStoryUnknownTarget;
import com.ua.sdk.cache.Cache;
import com.ua.sdk.cache.CacheSettings;
import com.ua.sdk.cache.DiskCache;
import com.ua.sdk.concurrent.CreateRequest;
import com.ua.sdk.concurrent.EntityEventHandler;
import com.ua.sdk.concurrent.SaveRequest;
import com.ua.sdk.concurrent.SynchronousRequest;
import com.ua.sdk.concurrent.UploadRequest;
import com.ua.sdk.internal.AbstractManager;
import com.ua.sdk.internal.EntityService;
import com.ua.sdk.internal.MediaService;
import com.ua.sdk.internal.Precondition;
import com.ua.sdk.internal.net.v7.UrlBuilderImpl;
import com.ua.sdk.privacy.Privacy;
import com.ua.sdk.privacy.PrivacyHelper;
import com.ua.sdk.user.User;
import com.ua.sdk.user.UserManager;
import com.ua.sdk.user.profilephoto.UserProfilePhotoImpl;
import com.ua.sdk.user.profilephoto.UserProfilePhotoManager;
import com.ua.sdk.user.profilephoto.UserProfilePhotoRef;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* JADX INFO: loaded from: classes65.dex */
public class ActivityStoryManagerImpl extends AbstractManager<ActivityStory> implements ActivityStoryManager {
    private final UserManager mUserManager;
    private final UserProfilePhotoManager mUserProfilePhotoManager;
    private final MediaService<ActivityStory> mediaService;

    public ActivityStoryManagerImpl(UserManager userManager, UserProfilePhotoManager userProfilePhotoManager, MediaService mediaService, CacheSettings cacheSettings, Cache memCache, DiskCache<ActivityStory> diskCache, EntityService<ActivityStory> service, ExecutorService executor) {
        super(cacheSettings, memCache, diskCache, service, executor);
        this.mUserManager = (UserManager) Precondition.isNotNull(userManager, "userManager");
        this.mUserProfilePhotoManager = (UserProfilePhotoManager) Precondition.isNotNull(userProfilePhotoManager, "userProfilePhotoManager");
        this.mediaService = (MediaService) Precondition.isNotNull(mediaService, "mediaService");
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request fetchActivityStoryList(EntityListRef<ActivityStory> ref, FetchCallback<EntityList<ActivityStory>> callback) {
        return fetchPage(ref, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public EntityList<ActivityStory> fetchActivityStoryList(EntityListRef<ActivityStory> ref) throws UaException {
        return fetchPage(ref);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request fetchActivityStory(EntityRef<ActivityStory> ref, FetchCallback<ActivityStory> callback) {
        return fetch(ref, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory fetchActivityStory(EntityRef<ActivityStory> ref) throws UaException {
        return fetch(ref);
    }

    private ActivityStoryImpl newPost(ActivityStoryObject object, EntityRef<User> currentUserRef, EntityRef<ActivityStory> targetStory, ActivityStoryActor actor) {
        Precondition.isNotNull(object, "object");
        Precondition.isNotNull(currentUserRef, "currentUserRef");
        ActivityStoryImpl reply = new ActivityStoryImpl();
        if (actor == null) {
            reply.mActor = new ActivityStoryUserActorImpl();
            ((ActivityStoryUserActorImpl) reply.mActor).setUser(currentUserRef);
        } else {
            reply.mActor = actor;
        }
        reply.mObject = object;
        if (targetStory != null) {
            ActivityStoryUnknownTarget target = new ActivityStoryUnknownTarget();
            target.setId(targetStory.getId());
            reply.mTarget = target;
        }
        switch (object.getType()) {
            case LIKE:
                reply.mVerb = ActivityStoryVerb.LIKE;
                return reply;
            case COMMENT:
                reply.mVerb = ActivityStoryVerb.COMMENT;
                return reply;
            case STATUS:
                reply.mVerb = ActivityStoryVerb.POST;
            default:
                throw new IllegalArgumentException("story object needs to be like or comment.");
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createLike(EntityRef<ActivityStory> targetStory, ActivityStoryActor actor) throws UaException {
        EntityRef<User> currentUserRef = this.mUserManager.getCurrentUser().getRef();
        ActivityStoryImpl like = newPost(new ActivityStoryLikeObjectImpl(), currentUserRef, targetStory, actor);
        return create(like);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createLike(final EntityRef<ActivityStory> targetStory, final ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.createLike(targetStory, actor);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createComment(String text, EntityRef<ActivityStory> targetStory, ActivityStoryActor actor) throws UaException {
        EntityRef<User> currentUserRef = this.mUserManager.getCurrentUser().getRef();
        ActivityStoryImpl comment = newPost(new ActivityStoryCommentObjectImpl(text), currentUserRef, targetStory, actor);
        return create(comment);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createComment(final String text, final EntityRef<ActivityStory> targetStory, final ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.createComment(text, targetStory, actor);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatus(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor) throws UaException {
        return createStatus(text, privacy, socialSettings, actor, (ActivityStoryTarget) null);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatus(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, ActivityStoryTarget target) throws UaException {
        ActivityStoryImpl status = initStatus(text, privacy, socialSettings, actor);
        if (target != null) {
            status.mTarget = target;
        }
        return create(status);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatusWithImage(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor) throws UaException {
        return createStatusWithImage(text, privacy, socialSettings, actor, (ActivityStoryTarget) null);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatusWithImage(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, ActivityStoryTarget target) throws UaException {
        ActivityStoryImpl status = initStatus(text, privacy, socialSettings, actor);
        status.mAttachments = new Attachments();
        status.mAttachments.addAttachment(Attachment.Type.PHOTO);
        if (target != null) {
            status.mTarget = target;
        }
        return create(status);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatusWithVideo(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor) throws UaException {
        return createStatusWithVideo(text, privacy, socialSettings, actor, (ActivityStoryTarget) null);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory createStatusWithVideo(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, ActivityStoryTarget target) throws UaException {
        ActivityStoryImpl status = initStatus(text, privacy, socialSettings, actor);
        status.mAttachments = new Attachments();
        status.mAttachments.addAttachment(Attachment.Type.VIDEO);
        if (target != null) {
            status.mTarget = target;
        }
        return create(status);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public void cancelUpload() throws IOException {
        this.mediaService.close();
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatus(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        return createStatus(text, privacy, socialSettings, actor, null, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatus(final String text, final Privacy.Level privacy, final SocialSettings socialSettings, final ActivityStoryActor actor, final ActivityStoryTarget target, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.createStatus(text, privacy, socialSettings, actor, target);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory repostStatus(ActivityStory activityStory, String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor) throws UaException {
        ActivityStoryImpl status = initStatus(text, privacy, socialSettings, actor);
        status.mObject = new ActivityStoryRepostObjectImpl(activityStory.getId(), text, PrivacyHelper.getPrivacy(privacy));
        status.mVerb = ActivityStoryVerb.REPOST;
        ActivityStory story = create(status);
        if (story != null) {
            ((ActivityStoryRepostObjectImpl) story.getObject()).setOriginalStory(activityStory);
        }
        return story;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request repostStatus(final ActivityStory activityStory, final String text, final Privacy.Level privacy, final SocialSettings socialSettings, final ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.repostStatus(activityStory, text, privacy, socialSettings, actor);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatusWithImage(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        return createStatusWithImage(text, privacy, socialSettings, actor, null, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatusWithImage(final String text, final Privacy.Level privacy, final SocialSettings socialSettings, final ActivityStoryActor actor, final ActivityStoryTarget target, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.createStatusWithImage(text, privacy, socialSettings, actor, target);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatusWithVideo(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor, CreateCallback<ActivityStory> callback) {
        return createStatusWithVideo(text, privacy, socialSettings, actor, null, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request createStatusWithVideo(final String text, final Privacy.Level privacy, final SocialSettings socialSettings, final ActivityStoryActor actor, final ActivityStoryTarget target, CreateCallback<ActivityStory> callback) {
        final CreateRequest<ActivityStory> request = new CreateRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.createStatusWithVideo(text, privacy, socialSettings, actor, target);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request attachImageWithStatus(final ActivityStory story, final Uri image, final UploadCallback callback) {
        final UploadRequest request = new UploadRequest(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String href = story.getRef().getHref();
                    AttachmentDest dest = new AttachmentDest(href, "attachments", 0);
                    ActivityStoryManagerImpl.this.mediaService.uploadImage(image, dest, callback);
                    request.done(story, null);
                } catch (UaException error) {
                    request.done(story, error);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request attachVideoWithStatus(final ActivityStory story, final Uri video, final UploadCallback callback) {
        final UploadRequest request = new UploadRequest(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.8
            @Override // java.lang.Runnable
            public void run() {
                try {
                    String href = story.getRef().getHref();
                    AttachmentDest dest = new AttachmentDest(href, "attachments", 0, ActivityStoryManagerImpl.this.mUserManager.getCurrentUserRef().getId());
                    ActivityStoryManagerImpl.this.mediaService.uploadVideo(video, dest, callback);
                    request.done(story, null);
                } catch (UaException error) {
                    request.done(story, error);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    private ActivityStoryImpl initStatus(String text, Privacy.Level privacy, SocialSettings socialSettings, ActivityStoryActor actor) throws UaException {
        EntityRef<User> currentUserRef = this.mUserManager.getCurrentUser().getRef();
        ActivityStoryImpl status = new ActivityStoryImpl();
        if (actor == null) {
            actor = new ActivityStoryUserActorImpl(currentUserRef.getId());
        }
        status.mActor = actor;
        status.mObject = new ActivityStoryStatusObjectImpl(text, PrivacyHelper.getPrivacy(privacy));
        status.mVerb = ActivityStoryVerb.POST;
        if (socialSettings != null) {
            status.mSharingSettngs = socialSettings;
        }
        return status;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public EntityRef<ActivityStory> deleteStory(EntityRef<ActivityStory> storyRef) throws UaException {
        return (EntityRef) delete(storyRef);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request deleteStory(EntityRef<ActivityStory> storyRef, DeleteCallback<EntityRef<ActivityStory>> callback) {
        return delete(storyRef, callback);
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request deleteLike(ActivityStory targetStory, DeleteCallback<EntityRef<ActivityStory>> callback) {
        if (targetStory.isLikedByCurrentUser()) {
            return deleteStory(targetStory.getLikesSummary().getReplyRef(), callback);
        }
        EntityEventHandler.callOnDeleted(null, new UaException("Story is not liked by the current user."), callback);
        return SynchronousRequest.INSTANCE;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory updateStoryPrivacy(ActivityStory targetStory, Privacy.Level newPrivacyLevel) throws UaException {
        ActivityStoryObject.Type type = targetStory.getObject().getType();
        switch (type) {
            case STATUS:
            case REPOST:
            case WORKOUT:
                ActivityStoryImpl impl = new ActivityStoryImpl();
                impl.mObject = new ActivityStoryPrivacyObjectImpl.Builder(type, newPrivacyLevel).build();
                return patch(impl, targetStory.getRef());
            case ROUTE:
                return update(new ActivityStoryRpcPostObject.Builder(PrivacyHelper.getPrivacy(newPrivacyLevel)).setLink(String.format(UrlBuilderImpl.PATCH_ROUTE, ((ActivityStoryRouteObjectImpl) targetStory.getObject()).getRouteId())).build());
            default:
                throw new UaException("Attempted to update privacy on a privacy story type that is incompatible");
        }
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request updateStoryPrivacy(final ActivityStory targetStory, final Privacy.Level newPrivacyLevel, SaveCallback<ActivityStory> callback) {
        final SaveRequest<ActivityStory> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.9
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.updateStoryPrivacy(targetStory, newPrivacyLevel);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public ActivityStory updateStoryStatus(ActivityStory targetStory, String status) throws UaException {
        ActivityStoryObject.Type type = targetStory.getObject().getType();
        ActivityStoryImpl impl = new ActivityStoryImpl();
        switch (type) {
            case STATUS:
                impl.mObject = new ActivityStoryStatusObjectImpl(status, null);
                break;
            case ROUTE:
            default:
                throw new UaException("Attempted to update status on a story type that is not compatible");
            case REPOST:
                impl.mObject = new ActivityStoryRepostObjectImpl(null, status, null);
                break;
            case WORKOUT:
                impl.mObject = new ActivityStoryWorkoutObjectImpl(status);
                break;
        }
        return patch(impl, targetStory.getRef());
    }

    @Override // com.ua.sdk.activitystory.ActivityStoryManager
    public Request updateStoryStatus(final ActivityStory targetStory, final String status, SaveCallback<ActivityStory> callback) {
        final SaveRequest<ActivityStory> request = new SaveRequest<>(callback);
        Future<?> future = this.executor.submit(new Runnable() { // from class: com.ua.sdk.activitystory.ActivityStoryManagerImpl.10
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ActivityStory story = ActivityStoryManagerImpl.this.updateStoryStatus(targetStory, status);
                    request.done(story, null);
                } catch (UaException e) {
                    request.done(null, e);
                }
            }
        });
        request.setFuture(future);
        return request;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.internal.AbstractManager
    public ActivityStory onPostServiceFetch(Reference ref, ActivityStory entity) throws UaException {
        if (entity == null) {
            return null;
        }
        ActivityStoryImpl storyImpl = (ActivityStoryImpl) entity;
        if (storyImpl.getActor() != null && storyImpl.getActor().getType() == ActivityStoryActor.Type.USER) {
            ActivityStoryUserActorImpl userActor = (ActivityStoryUserActorImpl) storyImpl.getActor();
            UserProfilePhotoRef picRef = UserProfilePhotoRef.getBuilder().setId(userActor.getId()).build();
            UserProfilePhotoImpl userProfilePhoto = (UserProfilePhotoImpl) this.mUserProfilePhotoManager.fetchCurrentProfilePhoto(picRef);
            ((ActivityStoryUserActorImpl) storyImpl.getActor()).setUserProfilePicture(userProfilePhoto.toImageUrl());
            return storyImpl;
        }
        return storyImpl;
    }

    @Override // com.ua.sdk.internal.AbstractManager
    protected EntityList<ActivityStory> onPostServiceFetchPage(Reference ref, EntityList<ActivityStory> list) throws UaException {
        if (list == null) {
            return null;
        }
        for (ActivityStory story : list.getAll()) {
            onPostServiceFetch(ref, story);
        }
        return list;
    }
}
