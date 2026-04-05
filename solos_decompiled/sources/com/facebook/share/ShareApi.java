package com.facebook.share;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookGraphResponseException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.CollectionMapper;
import com.facebook.internal.Mutable;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.Utility;
import com.facebook.share.Sharer;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.internal.ShareContentValidation;
import com.facebook.share.internal.ShareInternalUtility;
import com.facebook.share.internal.VideoUploader;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public final class ShareApi {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_GRAPH_NODE = "me";
    private static final String GRAPH_PATH_FORMAT = "%s/%s";
    private static final String PHOTOS_EDGE = "photos";
    private static final String TAG = "ShareApi";
    private String graphNode = DEFAULT_GRAPH_NODE;
    private String message;
    private final ShareContent shareContent;

    public static void share(ShareContent shareContent, FacebookCallback<Sharer.Result> callback) {
        new ShareApi(shareContent).share(callback);
    }

    public ShareApi(ShareContent shareContent) {
        this.shareContent = shareContent;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGraphNode() {
        return this.graphNode;
    }

    public void setGraphNode(String graphNode) {
        this.graphNode = graphNode;
    }

    public ShareContent getShareContent() {
        return this.shareContent;
    }

    public boolean canShare() {
        AccessToken accessToken;
        if (getShareContent() == null || (accessToken = AccessToken.getCurrentAccessToken()) == null) {
            return false;
        }
        Set<String> permissions = accessToken.getPermissions();
        if (permissions == null || !permissions.contains("publish_actions")) {
            Log.w(TAG, "The publish_actions permissions are missing, the share will fail unless this app was authorized to publish in another installation.");
        }
        return true;
    }

    public void share(FacebookCallback<Sharer.Result> callback) {
        if (!canShare()) {
            ShareInternalUtility.invokeCallbackWithError(callback, "Insufficient permissions for sharing content via Api.");
            return;
        }
        ShareContent shareContent = getShareContent();
        try {
            ShareContentValidation.validateForApiShare(shareContent);
            if (shareContent instanceof ShareLinkContent) {
                shareLinkContent((ShareLinkContent) shareContent, callback);
                return;
            }
            if (shareContent instanceof SharePhotoContent) {
                sharePhotoContent((SharePhotoContent) shareContent, callback);
            } else if (shareContent instanceof ShareVideoContent) {
                shareVideoContent((ShareVideoContent) shareContent, callback);
            } else if (shareContent instanceof ShareOpenGraphContent) {
                shareOpenGraphContent((ShareOpenGraphContent) shareContent, callback);
            }
        } catch (FacebookException ex) {
            ShareInternalUtility.invokeCallbackWithException(callback, ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getGraphPath(String pathAfterGraphNode) {
        try {
            return String.format(Locale.ROOT, GRAPH_PATH_FORMAT, URLEncoder.encode(getGraphNode(), "UTF-8"), pathAfterGraphNode);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private void addCommonParameters(Bundle bundle, ShareContent shareContent) {
        List<String> peopleIds = shareContent.getPeopleIds();
        if (!Utility.isNullOrEmpty(peopleIds)) {
            bundle.putString("tags", TextUtils.join(", ", peopleIds));
        }
        if (!Utility.isNullOrEmpty(shareContent.getPlaceId())) {
            bundle.putString("place", shareContent.getPlaceId());
        }
        if (!Utility.isNullOrEmpty(shareContent.getRef())) {
            bundle.putString("ref", shareContent.getRef());
        }
    }

    private void shareOpenGraphContent(ShareOpenGraphContent openGraphContent, final FacebookCallback<Sharer.Result> callback) {
        final GraphRequest.Callback requestCallback = new GraphRequest.Callback() { // from class: com.facebook.share.ShareApi.1
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                JSONObject data = response.getJSONObject();
                String postId = data == null ? null : data.optString("id");
                ShareInternalUtility.invokeCallbackWithResults(callback, postId, response);
            }
        };
        final ShareOpenGraphAction action = openGraphContent.getAction();
        final Bundle parameters = action.getBundle();
        addCommonParameters(parameters, openGraphContent);
        if (!Utility.isNullOrEmpty(getMessage())) {
            parameters.putString("message", getMessage());
        }
        CollectionMapper.OnMapperCompleteListener stageCallback = new CollectionMapper.OnMapperCompleteListener() { // from class: com.facebook.share.ShareApi.2
            @Override // com.facebook.internal.CollectionMapper.OnMapperCompleteListener
            public void onComplete() {
                try {
                    ShareApi.handleImagesOnAction(parameters);
                    new GraphRequest(AccessToken.getCurrentAccessToken(), ShareApi.this.getGraphPath(URLEncoder.encode(action.getActionType(), "UTF-8")), parameters, HttpMethod.POST, requestCallback).executeAsync();
                } catch (UnsupportedEncodingException ex) {
                    ShareInternalUtility.invokeCallbackWithException(callback, ex);
                }
            }

            @Override // com.facebook.internal.CollectionMapper.OnErrorListener
            public void onError(FacebookException exception) {
                ShareInternalUtility.invokeCallbackWithException(callback, exception);
            }
        };
        stageOpenGraphAction(parameters, stageCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void handleImagesOnAction(Bundle parameters) {
        String imageStr = parameters.getString("image");
        if (imageStr != null) {
            try {
                JSONArray images = new JSONArray(imageStr);
                for (int i = 0; i < images.length(); i++) {
                    JSONObject jsonImage = images.optJSONObject(i);
                    if (jsonImage != null) {
                        putImageInBundleWithArrayFormat(parameters, i, jsonImage);
                    } else {
                        String url = images.getString(i);
                        parameters.putString(String.format(Locale.ROOT, "image[%d][url]", Integer.valueOf(i)), url);
                    }
                }
                parameters.remove("image");
            } catch (JSONException e) {
                try {
                    JSONObject image = new JSONObject(imageStr);
                    putImageInBundleWithArrayFormat(parameters, 0, image);
                    parameters.remove("image");
                } catch (JSONException e2) {
                }
            }
        }
    }

    private static void putImageInBundleWithArrayFormat(Bundle parameters, int index, JSONObject image) throws JSONException {
        Iterator<String> keys = image.keys();
        while (keys.hasNext()) {
            String property = keys.next();
            String key = String.format(Locale.ROOT, "image[%d][%s]", Integer.valueOf(index), property);
            parameters.putString(key, image.get(property).toString());
        }
    }

    /* JADX WARN: Type inference failed for: r4v9, types: [T, java.lang.Integer] */
    private void sharePhotoContent(SharePhotoContent photoContent, final FacebookCallback<Sharer.Result> callback) {
        final Mutable<Integer> requestCount = new Mutable<>(0);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        ArrayList<GraphRequest> requests = new ArrayList<>();
        final ArrayList<JSONObject> results = new ArrayList<>();
        final ArrayList<GraphResponse> errorResponses = new ArrayList<>();
        GraphRequest.Callback requestCallback = new GraphRequest.Callback() { // from class: com.facebook.share.ShareApi.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v6, types: [T, java.lang.Integer] */
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                JSONObject result = response.getJSONObject();
                if (result != null) {
                    results.add(result);
                }
                if (response.getError() != null) {
                    errorResponses.add(response);
                }
                requestCount.value = Integer.valueOf(((Integer) requestCount.value).intValue() - 1);
                if (((Integer) requestCount.value).intValue() == 0) {
                    if (!errorResponses.isEmpty()) {
                        ShareInternalUtility.invokeCallbackWithResults(callback, null, (GraphResponse) errorResponses.get(0));
                    } else if (!results.isEmpty()) {
                        String postId = ((JSONObject) results.get(0)).optString("id");
                        ShareInternalUtility.invokeCallbackWithResults(callback, postId, response);
                    }
                }
            }
        };
        try {
            for (SharePhoto photo : photoContent.getPhotos()) {
                try {
                    Bundle params = getSharePhotoCommonParameters(photo, photoContent);
                    Bitmap bitmap = photo.getBitmap();
                    Uri photoUri = photo.getImageUrl();
                    String caption = photo.getCaption();
                    if (caption == null) {
                        caption = getMessage();
                    }
                    if (bitmap != null) {
                        requests.add(GraphRequest.newUploadPhotoRequest(accessToken, getGraphPath(PHOTOS_EDGE), bitmap, caption, params, requestCallback));
                    } else if (photoUri != null) {
                        requests.add(GraphRequest.newUploadPhotoRequest(accessToken, getGraphPath(PHOTOS_EDGE), photoUri, caption, params, requestCallback));
                    }
                } catch (JSONException e) {
                    ShareInternalUtility.invokeCallbackWithException(callback, e);
                    return;
                }
            }
            requestCount.value = Integer.valueOf(requestCount.value.intValue() + requests.size());
            for (GraphRequest request : requests) {
                request.executeAsync();
            }
        } catch (FileNotFoundException ex) {
            ShareInternalUtility.invokeCallbackWithException(callback, ex);
        }
    }

    private void shareLinkContent(ShareLinkContent linkContent, final FacebookCallback<Sharer.Result> callback) {
        GraphRequest.Callback requestCallback = new GraphRequest.Callback() { // from class: com.facebook.share.ShareApi.4
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                JSONObject data = response.getJSONObject();
                String postId = data == null ? null : data.optString("id");
                ShareInternalUtility.invokeCallbackWithResults(callback, postId, response);
            }
        };
        Bundle parameters = new Bundle();
        addCommonParameters(parameters, linkContent);
        parameters.putString("message", getMessage());
        parameters.putString("link", Utility.getUriString(linkContent.getContentUrl()));
        parameters.putString("picture", Utility.getUriString(linkContent.getImageUrl()));
        parameters.putString("name", linkContent.getContentTitle());
        parameters.putString("description", linkContent.getContentDescription());
        parameters.putString("ref", linkContent.getRef());
        new GraphRequest(AccessToken.getCurrentAccessToken(), getGraphPath("feed"), parameters, HttpMethod.POST, requestCallback).executeAsync();
    }

    private void shareVideoContent(ShareVideoContent videoContent, FacebookCallback<Sharer.Result> callback) {
        try {
            VideoUploader.uploadAsync(videoContent, getGraphNode(), callback);
        } catch (FileNotFoundException ex) {
            ShareInternalUtility.invokeCallbackWithException(callback, ex);
        }
    }

    private Bundle getSharePhotoCommonParameters(SharePhoto photo, SharePhotoContent photoContent) throws JSONException {
        Bundle params = photo.getParameters();
        if (!params.containsKey("place") && !Utility.isNullOrEmpty(photoContent.getPlaceId())) {
            params.putString("place", photoContent.getPlaceId());
        }
        if (!params.containsKey("tags") && !Utility.isNullOrEmpty(photoContent.getPeopleIds())) {
            List<String> peopleIds = photoContent.getPeopleIds();
            if (!Utility.isNullOrEmpty(peopleIds)) {
                JSONArray tags = new JSONArray();
                for (String id : peopleIds) {
                    JSONObject tag = new JSONObject();
                    tag.put("tag_uid", id);
                    tags.put(tag);
                }
                params.putString("tags", tags.toString());
            }
        }
        if (!params.containsKey("ref") && !Utility.isNullOrEmpty(photoContent.getRef())) {
            params.putString("ref", photoContent.getRef());
        }
        return params;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stageArrayList(final ArrayList arrayList, final CollectionMapper.OnMapValueCompleteListener onArrayListStagedListener) {
        final JSONArray stagedObject = new JSONArray();
        CollectionMapper.Collection<Integer> collection = new CollectionMapper.Collection<Integer>() { // from class: com.facebook.share.ShareApi.5
            @Override // com.facebook.internal.CollectionMapper.Collection
            public Iterator<Integer> keyIterator() {
                final int size = arrayList.size();
                final Mutable<Integer> current = new Mutable<>(0);
                return new Iterator<Integer>() { // from class: com.facebook.share.ShareApi.5.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return ((Integer) current.value).intValue() < size;
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r1v4, types: [T, java.lang.Integer] */
                    @Override // java.util.Iterator
                    public Integer next() {
                        Integer num = (Integer) current.value;
                        Mutable mutable = current;
                        mutable.value = Integer.valueOf(((Integer) mutable.value).intValue() + 1);
                        return num;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                    }
                };
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public Object get(Integer key) {
                return arrayList.get(key.intValue());
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public void set(Integer key, Object value, CollectionMapper.OnErrorListener onErrorListener) {
                try {
                    stagedObject.put(key.intValue(), value);
                } catch (JSONException ex) {
                    String message = ex.getLocalizedMessage();
                    if (message == null) {
                        message = "Error staging object.";
                    }
                    onErrorListener.onError(new FacebookException(message));
                }
            }
        };
        CollectionMapper.OnMapperCompleteListener onStagedArrayMapperCompleteListener = new CollectionMapper.OnMapperCompleteListener() { // from class: com.facebook.share.ShareApi.6
            @Override // com.facebook.internal.CollectionMapper.OnMapperCompleteListener
            public void onComplete() {
                onArrayListStagedListener.onComplete(stagedObject);
            }

            @Override // com.facebook.internal.CollectionMapper.OnErrorListener
            public void onError(FacebookException exception) {
                onArrayListStagedListener.onError(exception);
            }
        };
        stageCollectionValues(collection, onStagedArrayMapperCompleteListener);
    }

    private <T> void stageCollectionValues(CollectionMapper.Collection<T> collection, CollectionMapper.OnMapperCompleteListener onCollectionValuesStagedListener) {
        CollectionMapper.ValueMapper valueMapper = new CollectionMapper.ValueMapper() { // from class: com.facebook.share.ShareApi.7
            @Override // com.facebook.internal.CollectionMapper.ValueMapper
            public void mapValue(Object value, CollectionMapper.OnMapValueCompleteListener onMapValueCompleteListener) {
                if (value instanceof ArrayList) {
                    ShareApi.this.stageArrayList((ArrayList) value, onMapValueCompleteListener);
                    return;
                }
                if (value instanceof ShareOpenGraphObject) {
                    ShareApi.this.stageOpenGraphObject((ShareOpenGraphObject) value, onMapValueCompleteListener);
                } else if (value instanceof SharePhoto) {
                    ShareApi.this.stagePhoto((SharePhoto) value, onMapValueCompleteListener);
                } else {
                    onMapValueCompleteListener.onComplete(value);
                }
            }
        };
        CollectionMapper.iterate(collection, valueMapper, onCollectionValuesStagedListener);
    }

    private void stageOpenGraphAction(final Bundle parameters, CollectionMapper.OnMapperCompleteListener onOpenGraphActionStagedListener) {
        CollectionMapper.Collection<String> collection = new CollectionMapper.Collection<String>() { // from class: com.facebook.share.ShareApi.8
            @Override // com.facebook.internal.CollectionMapper.Collection
            public Iterator<String> keyIterator() {
                return parameters.keySet().iterator();
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public Object get(String key) {
                return parameters.get(key);
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public void set(String key, Object value, CollectionMapper.OnErrorListener onErrorListener) {
                if (!Utility.putJSONValueInBundle(parameters, key, value)) {
                    onErrorListener.onError(new FacebookException("Unexpected value: " + value.toString()));
                }
            }
        };
        stageCollectionValues(collection, onOpenGraphActionStagedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stageOpenGraphObject(final ShareOpenGraphObject object, final CollectionMapper.OnMapValueCompleteListener onOpenGraphObjectStagedListener) {
        String type = object.getString(ShareConstants.MEDIA_TYPE);
        if (type == null) {
            type = object.getString("og:type");
        }
        if (type == null) {
            onOpenGraphObjectStagedListener.onError(new FacebookException("Open Graph objects must contain a type value."));
            return;
        }
        final JSONObject stagedObject = new JSONObject();
        CollectionMapper.Collection<String> collection = new CollectionMapper.Collection<String>() { // from class: com.facebook.share.ShareApi.9
            @Override // com.facebook.internal.CollectionMapper.Collection
            public Iterator<String> keyIterator() {
                return object.keySet().iterator();
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public Object get(String key) {
                return object.get(key);
            }

            @Override // com.facebook.internal.CollectionMapper.Collection
            public void set(String key, Object value, CollectionMapper.OnErrorListener onErrorListener) {
                try {
                    stagedObject.put(key, value);
                } catch (JSONException ex) {
                    String message = ex.getLocalizedMessage();
                    if (message == null) {
                        message = "Error staging object.";
                    }
                    onErrorListener.onError(new FacebookException(message));
                }
            }
        };
        final GraphRequest.Callback requestCallback = new GraphRequest.Callback() { // from class: com.facebook.share.ShareApi.10
            @Override // com.facebook.GraphRequest.Callback
            public void onCompleted(GraphResponse response) {
                FacebookRequestError error = response.getError();
                if (error != null) {
                    String message = error.getErrorMessage();
                    if (message == null) {
                        message = "Error staging Open Graph object.";
                    }
                    onOpenGraphObjectStagedListener.onError(new FacebookGraphResponseException(response, message));
                    return;
                }
                JSONObject data = response.getJSONObject();
                if (data == null) {
                    onOpenGraphObjectStagedListener.onError(new FacebookGraphResponseException(response, "Error staging Open Graph object."));
                    return;
                }
                String stagedObjectId = data.optString("id");
                if (stagedObjectId == null) {
                    onOpenGraphObjectStagedListener.onError(new FacebookGraphResponseException(response, "Error staging Open Graph object."));
                } else {
                    onOpenGraphObjectStagedListener.onComplete(stagedObjectId);
                }
            }
        };
        final String ogType = type;
        CollectionMapper.OnMapperCompleteListener onMapperCompleteListener = new CollectionMapper.OnMapperCompleteListener() { // from class: com.facebook.share.ShareApi.11
            @Override // com.facebook.internal.CollectionMapper.OnMapperCompleteListener
            public void onComplete() {
                String objectString = stagedObject.toString();
                Bundle parameters = new Bundle();
                parameters.putString("object", objectString);
                try {
                    new GraphRequest(AccessToken.getCurrentAccessToken(), ShareApi.this.getGraphPath("objects/" + URLEncoder.encode(ogType, "UTF-8")), parameters, HttpMethod.POST, requestCallback).executeAsync();
                } catch (UnsupportedEncodingException ex) {
                    String message = ex.getLocalizedMessage();
                    if (message == null) {
                        message = "Error staging Open Graph object.";
                    }
                    onOpenGraphObjectStagedListener.onError(new FacebookException(message));
                }
            }

            @Override // com.facebook.internal.CollectionMapper.OnErrorListener
            public void onError(FacebookException exception) {
                onOpenGraphObjectStagedListener.onError(exception);
            }
        };
        stageCollectionValues(collection, onMapperCompleteListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stagePhoto(final SharePhoto photo, final CollectionMapper.OnMapValueCompleteListener onPhotoStagedListener) {
        Bitmap bitmap = photo.getBitmap();
        Uri imageUrl = photo.getImageUrl();
        if (bitmap != null || imageUrl != null) {
            GraphRequest.Callback requestCallback = new GraphRequest.Callback() { // from class: com.facebook.share.ShareApi.12
                @Override // com.facebook.GraphRequest.Callback
                public void onCompleted(GraphResponse response) {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        String message = error.getErrorMessage();
                        if (message == null) {
                            message = "Error staging photo.";
                        }
                        onPhotoStagedListener.onError(new FacebookGraphResponseException(response, message));
                        return;
                    }
                    JSONObject data = response.getJSONObject();
                    if (data == null) {
                        onPhotoStagedListener.onError(new FacebookException("Error staging photo."));
                        return;
                    }
                    String stagedImageUri = data.optString(ShareConstants.MEDIA_URI);
                    if (stagedImageUri == null) {
                        onPhotoStagedListener.onError(new FacebookException("Error staging photo."));
                        return;
                    }
                    JSONObject stagedObject = new JSONObject();
                    try {
                        stagedObject.put("url", stagedImageUri);
                        stagedObject.put(NativeProtocol.IMAGE_USER_GENERATED_KEY, photo.getUserGenerated());
                        onPhotoStagedListener.onComplete(stagedObject);
                    } catch (JSONException ex) {
                        String message2 = ex.getLocalizedMessage();
                        if (message2 == null) {
                            message2 = "Error staging photo.";
                        }
                        onPhotoStagedListener.onError(new FacebookException(message2));
                    }
                }
            };
            if (bitmap != null) {
                ShareInternalUtility.newUploadStagingResourceWithImageRequest(AccessToken.getCurrentAccessToken(), bitmap, requestCallback).executeAsync();
                return;
            }
            try {
                ShareInternalUtility.newUploadStagingResourceWithImageRequest(AccessToken.getCurrentAccessToken(), imageUrl, requestCallback).executeAsync();
                return;
            } catch (FileNotFoundException ex) {
                String message = ex.getLocalizedMessage();
                if (message == null) {
                    message = "Error staging photo.";
                }
                onPhotoStagedListener.onError(new FacebookException(message));
                return;
            }
        }
        onPhotoStagedListener.onError(new FacebookException("Photos must have an imageURL or bitmap."));
    }
}
