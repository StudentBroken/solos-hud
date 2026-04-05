package com.facebook.share.internal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.util.Pair;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookGraphResponseException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.NativeAppCallAttachmentStore;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.Sharer;
import com.facebook.share.internal.OpenGraphJSONUtility;
import com.facebook.share.model.ShareMedia;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.LikeView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareInternalUtility {
    public static final String MY_PHOTOS = "me/photos";
    private static final String MY_STAGING_RESOURCES = "me/staging_resources";
    private static final String STAGING_PARAM = "file";

    public static void invokeCallbackWithException(FacebookCallback<Sharer.Result> callback, Exception exception) {
        if (exception instanceof FacebookException) {
            invokeOnErrorCallback(callback, (FacebookException) exception);
        } else {
            invokeCallbackWithError(callback, "Error preparing share content: " + exception.getLocalizedMessage());
        }
    }

    public static void invokeCallbackWithError(FacebookCallback<Sharer.Result> callback, String error) {
        invokeOnErrorCallback(callback, error);
    }

    public static void invokeCallbackWithResults(FacebookCallback<Sharer.Result> callback, String postId, GraphResponse graphResponse) {
        FacebookRequestError requestError = graphResponse.getError();
        if (requestError != null) {
            String errorMessage = requestError.getErrorMessage();
            if (Utility.isNullOrEmpty(errorMessage)) {
                errorMessage = "Unexpected error sharing.";
            }
            invokeOnErrorCallback(callback, graphResponse, errorMessage);
            return;
        }
        invokeOnSuccessCallback(callback, postId);
    }

    public static String getNativeDialogCompletionGesture(Bundle result) {
        return result.containsKey(NativeProtocol.RESULT_ARGS_DIALOG_COMPLETION_GESTURE_KEY) ? result.getString(NativeProtocol.RESULT_ARGS_DIALOG_COMPLETION_GESTURE_KEY) : result.getString(NativeProtocol.EXTRA_DIALOG_COMPLETION_GESTURE_KEY);
    }

    public static String getShareDialogPostId(Bundle result) {
        if (result.containsKey(ShareConstants.RESULT_POST_ID)) {
            return result.getString(ShareConstants.RESULT_POST_ID);
        }
        if (result.containsKey(ShareConstants.EXTRA_RESULT_POST_ID)) {
            return result.getString(ShareConstants.EXTRA_RESULT_POST_ID);
        }
        return result.getString(ShareConstants.WEB_DIALOG_RESULT_PARAM_POST_ID);
    }

    public static boolean handleActivityResult(int requestCode, int resultCode, Intent data, ResultProcessor resultProcessor) {
        AppCall appCall = getAppCallFromActivityResult(requestCode, resultCode, data);
        if (appCall == null) {
            return false;
        }
        NativeAppCallAttachmentStore.cleanupAttachmentsForCall(appCall.getCallId());
        if (resultProcessor == null) {
            return true;
        }
        FacebookException exception = NativeProtocol.getExceptionFromErrorData(NativeProtocol.getErrorDataFromResultIntent(data));
        if (exception != null) {
            if (exception instanceof FacebookOperationCanceledException) {
                resultProcessor.onCancel(appCall);
                return true;
            }
            resultProcessor.onError(appCall, exception);
            return true;
        }
        Bundle results = NativeProtocol.getSuccessResultsFromIntent(data);
        resultProcessor.onSuccess(appCall, results);
        return true;
    }

    public static ResultProcessor getShareResultProcessor(final FacebookCallback<Sharer.Result> callback) {
        return new ResultProcessor(callback) { // from class: com.facebook.share.internal.ShareInternalUtility.1
            @Override // com.facebook.share.internal.ResultProcessor
            public void onSuccess(AppCall appCall, Bundle results) {
                if (results != null) {
                    String gesture = ShareInternalUtility.getNativeDialogCompletionGesture(results);
                    if (gesture == null || "post".equalsIgnoreCase(gesture)) {
                        String postId = ShareInternalUtility.getShareDialogPostId(results);
                        ShareInternalUtility.invokeOnSuccessCallback(callback, postId);
                    } else if ("cancel".equalsIgnoreCase(gesture)) {
                        ShareInternalUtility.invokeOnCancelCallback(callback);
                    } else {
                        ShareInternalUtility.invokeOnErrorCallback((FacebookCallback<Sharer.Result>) callback, new FacebookException(NativeProtocol.ERROR_UNKNOWN_ERROR));
                    }
                }
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onCancel(AppCall appCall) {
                ShareInternalUtility.invokeOnCancelCallback(callback);
            }

            @Override // com.facebook.share.internal.ResultProcessor
            public void onError(AppCall appCall, FacebookException error) {
                ShareInternalUtility.invokeOnErrorCallback((FacebookCallback<Sharer.Result>) callback, error);
            }
        };
    }

    private static AppCall getAppCallFromActivityResult(int requestCode, int resultCode, Intent data) {
        UUID callId = NativeProtocol.getCallIdFromIntent(data);
        if (callId == null) {
            return null;
        }
        return AppCall.finishPendingCall(callId, requestCode);
    }

    public static void registerStaticShareCallback(final int requestCode) {
        CallbackManagerImpl.registerStaticCallback(requestCode, new CallbackManagerImpl.Callback() { // from class: com.facebook.share.internal.ShareInternalUtility.2
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(requestCode, resultCode, data, ShareInternalUtility.getShareResultProcessor(null));
            }
        });
    }

    public static void registerSharerCallback(final int requestCode, CallbackManager callbackManager, final FacebookCallback<Sharer.Result> callback) {
        if (!(callbackManager instanceof CallbackManagerImpl)) {
            throw new FacebookException("Unexpected CallbackManager, please use the provided Factory.");
        }
        ((CallbackManagerImpl) callbackManager).registerCallback(requestCode, new CallbackManagerImpl.Callback() { // from class: com.facebook.share.internal.ShareInternalUtility.3
            @Override // com.facebook.internal.CallbackManagerImpl.Callback
            public boolean onActivityResult(int resultCode, Intent data) {
                return ShareInternalUtility.handleActivityResult(requestCode, resultCode, data, ShareInternalUtility.getShareResultProcessor(callback));
            }
        });
    }

    public static List<String> getPhotoUrls(SharePhotoContent photoContent, final UUID appCallId) {
        List<SharePhoto> photos;
        if (photoContent == null || (photos = photoContent.getPhotos()) == null) {
            return null;
        }
        List<NativeAppCallAttachmentStore.Attachment> attachments = Utility.map(photos, new Utility.Mapper<SharePhoto, NativeAppCallAttachmentStore.Attachment>() { // from class: com.facebook.share.internal.ShareInternalUtility.4
            @Override // com.facebook.internal.Utility.Mapper
            public NativeAppCallAttachmentStore.Attachment apply(SharePhoto item) {
                return ShareInternalUtility.getAttachment(appCallId, item);
            }
        });
        List<String> map = Utility.map(attachments, new Utility.Mapper<NativeAppCallAttachmentStore.Attachment, String>() { // from class: com.facebook.share.internal.ShareInternalUtility.5
            @Override // com.facebook.internal.Utility.Mapper
            public String apply(NativeAppCallAttachmentStore.Attachment item) {
                return item.getAttachmentUrl();
            }
        });
        NativeAppCallAttachmentStore.addAttachments(attachments);
        return map;
    }

    public static String getVideoUrl(ShareVideoContent videoContent, UUID appCallId) {
        if (videoContent == null || videoContent.getVideo() == null) {
            return null;
        }
        NativeAppCallAttachmentStore.Attachment attachment = NativeAppCallAttachmentStore.createAttachment(appCallId, videoContent.getVideo().getLocalUrl());
        ArrayList<NativeAppCallAttachmentStore.Attachment> attachments = new ArrayList<>(1);
        attachments.add(attachment);
        NativeAppCallAttachmentStore.addAttachments(attachments);
        return attachment.getAttachmentUrl();
    }

    public static List<Bundle> getMediaInfos(ShareMediaContent mediaContent, final UUID appCallId) {
        List<ShareMedia> media;
        if (mediaContent == null || (media = mediaContent.getMedia()) == null) {
            return null;
        }
        final List<NativeAppCallAttachmentStore.Attachment> attachments = new ArrayList<>();
        List<Bundle> map = Utility.map(media, new Utility.Mapper<ShareMedia, Bundle>() { // from class: com.facebook.share.internal.ShareInternalUtility.6
            @Override // com.facebook.internal.Utility.Mapper
            public Bundle apply(ShareMedia item) {
                NativeAppCallAttachmentStore.Attachment attachment = ShareInternalUtility.getAttachment(appCallId, item);
                attachments.add(attachment);
                Bundle mediaInfo = new Bundle();
                mediaInfo.putString(ShareConstants.MEDIA_TYPE, item.getMediaType().name());
                mediaInfo.putString(ShareConstants.MEDIA_URI, attachment.getAttachmentUrl());
                return mediaInfo;
            }
        });
        NativeAppCallAttachmentStore.addAttachments(attachments);
        return map;
    }

    public static JSONObject toJSONObjectForCall(final UUID callId, ShareOpenGraphContent content) throws JSONException {
        ShareOpenGraphAction action = content.getAction();
        final ArrayList<NativeAppCallAttachmentStore.Attachment> attachments = new ArrayList<>();
        JSONObject actionJSON = OpenGraphJSONUtility.toJSONObject(action, new OpenGraphJSONUtility.PhotoJSONProcessor() { // from class: com.facebook.share.internal.ShareInternalUtility.7
            @Override // com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor
            public JSONObject toJSONObject(SharePhoto photo) {
                NativeAppCallAttachmentStore.Attachment attachment = ShareInternalUtility.getAttachment(callId, photo);
                if (attachment == null) {
                    return null;
                }
                attachments.add(attachment);
                JSONObject photoJSONObject = new JSONObject();
                try {
                    photoJSONObject.put("url", attachment.getAttachmentUrl());
                    if (photo.getUserGenerated()) {
                        photoJSONObject.put(NativeProtocol.IMAGE_USER_GENERATED_KEY, true);
                        return photoJSONObject;
                    }
                    return photoJSONObject;
                } catch (JSONException e) {
                    throw new FacebookException("Unable to attach images", e);
                }
            }
        });
        NativeAppCallAttachmentStore.addAttachments(attachments);
        if (content.getPlaceId() != null) {
            String placeTag = actionJSON.optString("place");
            if (Utility.isNullOrEmpty(placeTag)) {
                actionJSON.put("place", content.getPlaceId());
            }
        }
        if (content.getPeopleIds() != null) {
            JSONArray peopleTags = actionJSON.optJSONArray("tags");
            Set<String> peopleIdSet = peopleTags == null ? new HashSet<>() : Utility.jsonArrayToSet(peopleTags);
            for (String peopleId : content.getPeopleIds()) {
                peopleIdSet.add(peopleId);
            }
            actionJSON.put("tags", new ArrayList(peopleIdSet));
        }
        return actionJSON;
    }

    public static JSONObject toJSONObjectForWeb(ShareOpenGraphContent shareOpenGraphContent) throws JSONException {
        ShareOpenGraphAction action = shareOpenGraphContent.getAction();
        return OpenGraphJSONUtility.toJSONObject(action, new OpenGraphJSONUtility.PhotoJSONProcessor() { // from class: com.facebook.share.internal.ShareInternalUtility.8
            @Override // com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor
            public JSONObject toJSONObject(SharePhoto photo) {
                Uri photoUri = photo.getImageUrl();
                JSONObject photoJSONObject = new JSONObject();
                try {
                    photoJSONObject.put("url", photoUri.toString());
                    return photoJSONObject;
                } catch (JSONException e) {
                    throw new FacebookException("Unable to attach images", e);
                }
            }
        });
    }

    public static JSONArray removeNamespacesFromOGJsonArray(JSONArray jsonArray, boolean requireNamespace) throws JSONException {
        JSONArray newArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONArray) {
                value = removeNamespacesFromOGJsonArray((JSONArray) value, requireNamespace);
            } else if (value instanceof JSONObject) {
                value = removeNamespacesFromOGJsonObject((JSONObject) value, requireNamespace);
            }
            newArray.put(value);
        }
        return newArray;
    }

    public static JSONObject removeNamespacesFromOGJsonObject(JSONObject jsonObject, boolean requireNamespace) {
        if (jsonObject == null) {
            return null;
        }
        try {
            JSONObject newJsonObject = new JSONObject();
            JSONObject data = new JSONObject();
            JSONArray names = jsonObject.names();
            for (int i = 0; i < names.length(); i++) {
                String key = names.getString(i);
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    value = removeNamespacesFromOGJsonObject((JSONObject) value, true);
                } else if (value instanceof JSONArray) {
                    value = removeNamespacesFromOGJsonArray((JSONArray) value, true);
                }
                Pair<String, String> fieldNameAndNamespace = getFieldNameAndNamespaceFromFullName(key);
                String namespace = (String) fieldNameAndNamespace.first;
                String fieldName = (String) fieldNameAndNamespace.second;
                if (requireNamespace) {
                    if (namespace != null && namespace.equals("fbsdk")) {
                        newJsonObject.put(key, value);
                    } else if (namespace == null || namespace.equals("og")) {
                        newJsonObject.put(fieldName, value);
                    } else {
                        data.put(fieldName, value);
                    }
                } else if (namespace != null && namespace.equals("fb")) {
                    newJsonObject.put(key, value);
                } else {
                    newJsonObject.put(fieldName, value);
                }
            }
            if (data.length() > 0) {
                newJsonObject.put(ShareConstants.WEB_DIALOG_PARAM_DATA, data);
                return newJsonObject;
            }
            return newJsonObject;
        } catch (JSONException e) {
            throw new FacebookException("Failed to create json object from share content");
        }
    }

    public static Pair<String, String> getFieldNameAndNamespaceFromFullName(String fullName) {
        String fieldName;
        String namespace = null;
        int index = fullName.indexOf(58);
        if (index != -1 && fullName.length() > index + 1) {
            namespace = fullName.substring(0, index);
            fieldName = fullName.substring(index + 1);
        } else {
            fieldName = fullName;
        }
        return new Pair<>(namespace, fieldName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NativeAppCallAttachmentStore.Attachment getAttachment(UUID callId, ShareMedia medium) {
        Bitmap bitmap = null;
        Uri uri = null;
        if (medium instanceof SharePhoto) {
            SharePhoto photo = (SharePhoto) medium;
            bitmap = photo.getBitmap();
            uri = photo.getImageUrl();
        } else if (medium instanceof ShareVideo) {
            ShareVideo video = (ShareVideo) medium;
            uri = video.getLocalUrl();
        }
        if (bitmap != null) {
            NativeAppCallAttachmentStore.Attachment attachment = NativeAppCallAttachmentStore.createAttachment(callId, bitmap);
            return attachment;
        }
        if (uri == null) {
            return null;
        }
        NativeAppCallAttachmentStore.Attachment attachment2 = NativeAppCallAttachmentStore.createAttachment(callId, uri);
        return attachment2;
    }

    static void invokeOnCancelCallback(FacebookCallback<Sharer.Result> callback) {
        logShareResult(AnalyticsEvents.PARAMETER_SHARE_OUTCOME_CANCELLED, null);
        if (callback != null) {
            callback.onCancel();
        }
    }

    static void invokeOnSuccessCallback(FacebookCallback<Sharer.Result> callback, String postId) {
        logShareResult(AnalyticsEvents.PARAMETER_SHARE_OUTCOME_SUCCEEDED, null);
        if (callback != null) {
            callback.onSuccess(new Sharer.Result(postId));
        }
    }

    static void invokeOnErrorCallback(FacebookCallback<Sharer.Result> callback, GraphResponse response, String message) {
        logShareResult("error", message);
        if (callback != null) {
            callback.onError(new FacebookGraphResponseException(response, message));
        }
    }

    static void invokeOnErrorCallback(FacebookCallback<Sharer.Result> callback, String message) {
        logShareResult("error", message);
        if (callback != null) {
            callback.onError(new FacebookException(message));
        }
    }

    static void invokeOnErrorCallback(FacebookCallback<Sharer.Result> callback, FacebookException ex) {
        logShareResult("error", ex.getMessage());
        if (callback != null) {
            callback.onError(ex);
        }
    }

    private static void logShareResult(String shareOutcome, String errorMessage) {
        Context context = FacebookSdk.getApplicationContext();
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AnalyticsEvents.PARAMETER_SHARE_OUTCOME, shareOutcome);
        if (errorMessage != null) {
            parameters.putString(AnalyticsEvents.PARAMETER_SHARE_ERROR_MESSAGE, errorMessage);
        }
        logger.logSdkEvent(AnalyticsEvents.EVENT_SHARE_RESULT, null, parameters);
    }

    public static GraphRequest newUploadStagingResourceWithImageRequest(AccessToken accessToken, Bitmap image, GraphRequest.Callback callback) {
        Bundle parameters = new Bundle(1);
        parameters.putParcelable(STAGING_PARAM, image);
        return new GraphRequest(accessToken, MY_STAGING_RESOURCES, parameters, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadStagingResourceWithImageRequest(AccessToken accessToken, File file, GraphRequest.Callback callback) throws FileNotFoundException {
        ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        GraphRequest.ParcelableResourceWithMimeType<ParcelFileDescriptor> resourceWithMimeType = new GraphRequest.ParcelableResourceWithMimeType<>(descriptor, "image/png");
        Bundle parameters = new Bundle(1);
        parameters.putParcelable(STAGING_PARAM, resourceWithMimeType);
        return new GraphRequest(accessToken, MY_STAGING_RESOURCES, parameters, HttpMethod.POST, callback);
    }

    public static GraphRequest newUploadStagingResourceWithImageRequest(AccessToken accessToken, Uri imageUri, GraphRequest.Callback callback) throws FileNotFoundException {
        if (Utility.isFileUri(imageUri)) {
            return newUploadStagingResourceWithImageRequest(accessToken, new File(imageUri.getPath()), callback);
        }
        if (!Utility.isContentUri(imageUri)) {
            throw new FacebookException("The image Uri must be either a file:// or content:// Uri");
        }
        GraphRequest.ParcelableResourceWithMimeType<Uri> resourceWithMimeType = new GraphRequest.ParcelableResourceWithMimeType<>(imageUri, "image/png");
        Bundle parameters = new Bundle(1);
        parameters.putParcelable(STAGING_PARAM, resourceWithMimeType);
        return new GraphRequest(accessToken, MY_STAGING_RESOURCES, parameters, HttpMethod.POST, callback);
    }

    @Nullable
    public static LikeView.ObjectType getMostSpecificObjectType(LikeView.ObjectType objectType1, LikeView.ObjectType objectType2) {
        if (objectType1 != objectType2) {
            if (objectType1 == LikeView.ObjectType.UNKNOWN) {
                return objectType2;
            }
            if (objectType2 != LikeView.ObjectType.UNKNOWN) {
                return null;
            }
            return objectType1;
        }
        return objectType1;
    }
}
