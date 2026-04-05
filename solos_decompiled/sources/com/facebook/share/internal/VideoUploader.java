package com.facebook.share.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookGraphResponseException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.internal.WorkQueue;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public class VideoUploader {
    private static final String ERROR_BAD_SERVER_RESPONSE = "Unexpected error in server response";
    private static final String ERROR_UPLOAD = "Video upload failed";
    private static final int MAX_RETRIES_PER_PHASE = 2;
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_END_OFFSET = "end_offset";
    private static final String PARAM_FILE_SIZE = "file_size";
    private static final String PARAM_REF = "ref";
    private static final String PARAM_SESSION_ID = "upload_session_id";
    private static final String PARAM_START_OFFSET = "start_offset";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_UPLOAD_PHASE = "upload_phase";
    private static final String PARAM_VALUE_UPLOAD_FINISH_PHASE = "finish";
    private static final String PARAM_VALUE_UPLOAD_START_PHASE = "start";
    private static final String PARAM_VALUE_UPLOAD_TRANSFER_PHASE = "transfer";
    private static final String PARAM_VIDEO_FILE_CHUNK = "video_file_chunk";
    private static final String PARAM_VIDEO_ID = "video_id";
    private static final int RETRY_DELAY_BACK_OFF_FACTOR = 3;
    private static final int RETRY_DELAY_UNIT_MS = 5000;
    private static final String TAG = "VideoUploader";
    private static final int UPLOAD_QUEUE_MAX_CONCURRENT = 8;
    private static AccessTokenTracker accessTokenTracker;
    private static Handler handler;
    private static boolean initialized;
    private static WorkQueue uploadQueue = new WorkQueue(8);
    private static Set<UploadContext> pendingUploads = new HashSet();

    public static synchronized void uploadAsync(ShareVideoContent videoContent, FacebookCallback<Sharer.Result> callback) throws FileNotFoundException {
        uploadAsync(videoContent, "me", callback);
    }

    public static synchronized void uploadAsync(ShareVideoContent videoContent, String graphNode, FacebookCallback<Sharer.Result> callback) throws FileNotFoundException {
        if (!initialized) {
            registerAccessTokenTracker();
            initialized = true;
        }
        Validate.notNull(videoContent, "videoContent");
        Validate.notNull(graphNode, "graphNode");
        ShareVideo video = videoContent.getVideo();
        Validate.notNull(video, "videoContent.video");
        Uri videoUri = video.getLocalUrl();
        Validate.notNull(videoUri, "videoContent.video.localUrl");
        UploadContext uploadContext = new UploadContext(videoContent, graphNode, callback);
        uploadContext.initialize();
        pendingUploads.add(uploadContext);
        enqueueUploadStart(uploadContext, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void cancelAllRequests() {
        for (UploadContext uploadContext : pendingUploads) {
            uploadContext.isCanceled = true;
        }
    }

    private static synchronized void removePendingUpload(UploadContext uploadContext) {
        pendingUploads.remove(uploadContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void issueResponse(UploadContext uploadContext, FacebookException error, String videoId) {
        removePendingUpload(uploadContext);
        Utility.closeQuietly(uploadContext.videoStream);
        if (uploadContext.callback != null) {
            if (error != null) {
                ShareInternalUtility.invokeOnErrorCallback(uploadContext.callback, error);
            } else if (uploadContext.isCanceled) {
                ShareInternalUtility.invokeOnCancelCallback(uploadContext.callback);
            } else {
                ShareInternalUtility.invokeOnSuccessCallback(uploadContext.callback, videoId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void enqueueUploadStart(UploadContext uploadContext, int completedRetries) {
        enqueueRequest(uploadContext, new StartUploadWorkItem(uploadContext, completedRetries));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void enqueueUploadChunk(UploadContext uploadContext, String chunkStart, String chunkEnd, int completedRetries) {
        enqueueRequest(uploadContext, new TransferChunkWorkItem(uploadContext, chunkStart, chunkEnd, completedRetries));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void enqueueUploadFinish(UploadContext uploadContext, int completedRetries) {
        enqueueRequest(uploadContext, new FinishUploadWorkItem(uploadContext, completedRetries));
    }

    private static synchronized void enqueueRequest(UploadContext uploadContext, Runnable workItem) {
        uploadContext.workItem = uploadQueue.addActiveWorkItem(workItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] getChunk(UploadContext uploadContext, String chunkStart, String chunkEnd) throws IOException {
        int len;
        if (!Utility.areObjectsEqual(chunkStart, uploadContext.chunkStart)) {
            logError(null, "Error reading video chunk. Expected chunk '%s'. Requested chunk '%s'.", uploadContext.chunkStart, chunkStart);
            return null;
        }
        long chunkStartLong = Long.parseLong(chunkStart);
        long chunkEndLong = Long.parseLong(chunkEnd);
        int chunkSize = (int) (chunkEndLong - chunkStartLong);
        ByteArrayOutputStream byteBufferStream = new ByteArrayOutputStream();
        int bufferSize = Math.min(8192, chunkSize);
        byte[] buffer = new byte[bufferSize];
        do {
            len = uploadContext.videoStream.read(buffer);
            if (len != -1) {
                byteBufferStream.write(buffer, 0, len);
                chunkSize -= len;
                if (chunkSize == 0) {
                }
            }
            uploadContext.chunkStart = chunkEnd;
            return byteBufferStream.toByteArray();
        } while (chunkSize >= 0);
        logError(null, "Error reading video chunk. Expected buffer length - '%d'. Actual - '%d'.", Integer.valueOf(chunkSize + len), Integer.valueOf(len));
        return null;
    }

    private static void registerAccessTokenTracker() {
        accessTokenTracker = new AccessTokenTracker() { // from class: com.facebook.share.internal.VideoUploader.1
            @Override // com.facebook.AccessTokenTracker
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (oldAccessToken != null) {
                    if (currentAccessToken == null || !Utility.areObjectsEqual(currentAccessToken.getUserId(), oldAccessToken.getUserId())) {
                        VideoUploader.cancelAllRequests();
                    }
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logError(Exception e, String format, Object... args) {
        Log.e(TAG, String.format(Locale.ROOT, format, args), e);
    }

    private static class UploadContext {
        public final AccessToken accessToken;
        public final FacebookCallback<Sharer.Result> callback;
        public String chunkStart;
        public final String description;
        public final String graphNode;
        public boolean isCanceled;
        public Bundle params;
        public final String ref;
        public String sessionId;
        public final String title;
        public String videoId;
        public long videoSize;
        public InputStream videoStream;
        public final Uri videoUri;
        public WorkQueue.WorkItem workItem;

        private UploadContext(ShareVideoContent videoContent, String graphNode, FacebookCallback<Sharer.Result> callback) {
            this.chunkStart = "0";
            this.accessToken = AccessToken.getCurrentAccessToken();
            this.videoUri = videoContent.getVideo().getLocalUrl();
            this.title = videoContent.getContentTitle();
            this.description = videoContent.getContentDescription();
            this.ref = videoContent.getRef();
            this.graphNode = graphNode;
            this.callback = callback;
            this.params = videoContent.getVideo().getParameters();
            if (!Utility.isNullOrEmpty(videoContent.getPeopleIds())) {
                this.params.putString("tags", TextUtils.join(", ", videoContent.getPeopleIds()));
            }
            if (!Utility.isNullOrEmpty(videoContent.getPlaceId())) {
                this.params.putString("place", videoContent.getPlaceId());
            }
            if (!Utility.isNullOrEmpty(videoContent.getRef())) {
                this.params.putString(VideoUploader.PARAM_REF, videoContent.getRef());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initialize() throws FileNotFoundException {
            try {
                if (Utility.isFileUri(this.videoUri)) {
                    ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(new File(this.videoUri.getPath()), VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
                    this.videoSize = fileDescriptor.getStatSize();
                    this.videoStream = new ParcelFileDescriptor.AutoCloseInputStream(fileDescriptor);
                } else {
                    if (Utility.isContentUri(this.videoUri)) {
                        this.videoSize = Utility.getContentSize(this.videoUri);
                        this.videoStream = FacebookSdk.getApplicationContext().getContentResolver().openInputStream(this.videoUri);
                        return;
                    }
                    throw new FacebookException("Uri must be a content:// or file:// uri");
                }
            } catch (FileNotFoundException e) {
                Utility.closeQuietly(this.videoStream);
                throw e;
            }
        }
    }

    private static class StartUploadWorkItem extends UploadWorkItemBase {
        static final Set<Integer> transientErrorCodes = new HashSet<Integer>() { // from class: com.facebook.share.internal.VideoUploader.StartUploadWorkItem.1
            {
                add(6000);
            }
        };

        public StartUploadWorkItem(UploadContext uploadContext, int completedRetries) {
            super(uploadContext, completedRetries);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        public Bundle getParameters() {
            Bundle parameters = new Bundle();
            parameters.putString(VideoUploader.PARAM_UPLOAD_PHASE, VideoUploader.PARAM_VALUE_UPLOAD_START_PHASE);
            parameters.putLong(VideoUploader.PARAM_FILE_SIZE, this.uploadContext.videoSize);
            return parameters;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleSuccess(JSONObject jsonObject) throws JSONException {
            this.uploadContext.sessionId = jsonObject.getString(VideoUploader.PARAM_SESSION_ID);
            this.uploadContext.videoId = jsonObject.getString(VideoUploader.PARAM_VIDEO_ID);
            String startOffset = jsonObject.getString(VideoUploader.PARAM_START_OFFSET);
            String endOffset = jsonObject.getString(VideoUploader.PARAM_END_OFFSET);
            VideoUploader.enqueueUploadChunk(this.uploadContext, startOffset, endOffset, 0);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleError(FacebookException error) {
            VideoUploader.logError(error, "Error starting video upload", new Object[0]);
            endUploadWithFailure(error);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected Set<Integer> getTransientErrorCodes() {
            return transientErrorCodes;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void enqueueRetry(int retriesCompleted) {
            VideoUploader.enqueueUploadStart(this.uploadContext, retriesCompleted);
        }
    }

    private static class TransferChunkWorkItem extends UploadWorkItemBase {
        static final Set<Integer> transientErrorCodes = new HashSet<Integer>() { // from class: com.facebook.share.internal.VideoUploader.TransferChunkWorkItem.1
            {
                add(1363019);
                add(1363021);
                add(1363030);
                add(1363033);
                add(1363041);
            }
        };
        private String chunkEnd;
        private String chunkStart;

        public TransferChunkWorkItem(UploadContext uploadContext, String chunkStart, String chunkEnd, int completedRetries) {
            super(uploadContext, completedRetries);
            this.chunkStart = chunkStart;
            this.chunkEnd = chunkEnd;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        public Bundle getParameters() throws IOException {
            Bundle parameters = new Bundle();
            parameters.putString(VideoUploader.PARAM_UPLOAD_PHASE, VideoUploader.PARAM_VALUE_UPLOAD_TRANSFER_PHASE);
            parameters.putString(VideoUploader.PARAM_SESSION_ID, this.uploadContext.sessionId);
            parameters.putString(VideoUploader.PARAM_START_OFFSET, this.chunkStart);
            byte[] chunk = VideoUploader.getChunk(this.uploadContext, this.chunkStart, this.chunkEnd);
            if (chunk != null) {
                parameters.putByteArray(VideoUploader.PARAM_VIDEO_FILE_CHUNK, chunk);
                return parameters;
            }
            throw new FacebookException("Error reading video");
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleSuccess(JSONObject jsonObject) throws JSONException {
            String startOffset = jsonObject.getString(VideoUploader.PARAM_START_OFFSET);
            String endOffset = jsonObject.getString(VideoUploader.PARAM_END_OFFSET);
            if (Utility.areObjectsEqual(startOffset, endOffset)) {
                VideoUploader.enqueueUploadFinish(this.uploadContext, 0);
            } else {
                VideoUploader.enqueueUploadChunk(this.uploadContext, startOffset, endOffset, 0);
            }
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleError(FacebookException error) {
            VideoUploader.logError(error, "Error uploading video '%s'", this.uploadContext.videoId);
            endUploadWithFailure(error);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected Set<Integer> getTransientErrorCodes() {
            return transientErrorCodes;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void enqueueRetry(int retriesCompleted) {
            VideoUploader.enqueueUploadChunk(this.uploadContext, this.chunkStart, this.chunkEnd, retriesCompleted);
        }
    }

    private static class FinishUploadWorkItem extends UploadWorkItemBase {
        static final Set<Integer> transientErrorCodes = new HashSet<Integer>() { // from class: com.facebook.share.internal.VideoUploader.FinishUploadWorkItem.1
            {
                add(1363011);
            }
        };

        public FinishUploadWorkItem(UploadContext uploadContext, int completedRetries) {
            super(uploadContext, completedRetries);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        public Bundle getParameters() {
            Bundle parameters = new Bundle();
            if (this.uploadContext.params != null) {
                parameters.putAll(this.uploadContext.params);
            }
            parameters.putString(VideoUploader.PARAM_UPLOAD_PHASE, VideoUploader.PARAM_VALUE_UPLOAD_FINISH_PHASE);
            parameters.putString(VideoUploader.PARAM_SESSION_ID, this.uploadContext.sessionId);
            Utility.putNonEmptyString(parameters, "title", this.uploadContext.title);
            Utility.putNonEmptyString(parameters, "description", this.uploadContext.description);
            Utility.putNonEmptyString(parameters, VideoUploader.PARAM_REF, this.uploadContext.ref);
            return parameters;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleSuccess(JSONObject jsonObject) throws JSONException {
            if (jsonObject.getBoolean(GraphResponse.SUCCESS_KEY)) {
                issueResponseOnMainThread(null, this.uploadContext.videoId);
            } else {
                handleError(new FacebookException(VideoUploader.ERROR_BAD_SERVER_RESPONSE));
            }
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void handleError(FacebookException error) {
            VideoUploader.logError(error, "Video '%s' failed to finish uploading", this.uploadContext.videoId);
            endUploadWithFailure(error);
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected Set<Integer> getTransientErrorCodes() {
            return transientErrorCodes;
        }

        @Override // com.facebook.share.internal.VideoUploader.UploadWorkItemBase
        protected void enqueueRetry(int retriesCompleted) {
            VideoUploader.enqueueUploadFinish(this.uploadContext, retriesCompleted);
        }
    }

    private static abstract class UploadWorkItemBase implements Runnable {
        protected int completedRetries;
        protected UploadContext uploadContext;

        protected abstract void enqueueRetry(int i);

        protected abstract Bundle getParameters() throws Exception;

        protected abstract Set<Integer> getTransientErrorCodes();

        protected abstract void handleError(FacebookException facebookException);

        protected abstract void handleSuccess(JSONObject jSONObject) throws JSONException;

        protected UploadWorkItemBase(UploadContext uploadContext, int completedRetries) {
            this.uploadContext = uploadContext;
            this.completedRetries = completedRetries;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.uploadContext.isCanceled) {
                try {
                    executeGraphRequestSynchronously(getParameters());
                    return;
                } catch (FacebookException fe) {
                    endUploadWithFailure(fe);
                    return;
                } catch (Exception e) {
                    endUploadWithFailure(new FacebookException(VideoUploader.ERROR_UPLOAD, e));
                    return;
                }
            }
            endUploadWithFailure(null);
        }

        protected void executeGraphRequestSynchronously(Bundle parameters) {
            GraphRequest request = new GraphRequest(this.uploadContext.accessToken, String.format(Locale.ROOT, "%s/videos", this.uploadContext.graphNode), parameters, HttpMethod.POST, null);
            GraphResponse response = request.executeAndWait();
            if (response != null) {
                FacebookRequestError error = response.getError();
                JSONObject responseJSON = response.getJSONObject();
                if (error != null) {
                    if (!attemptRetry(error.getSubErrorCode())) {
                        handleError(new FacebookGraphResponseException(response, VideoUploader.ERROR_UPLOAD));
                        return;
                    }
                    return;
                } else {
                    if (responseJSON != null) {
                        try {
                            handleSuccess(responseJSON);
                            return;
                        } catch (JSONException e) {
                            endUploadWithFailure(new FacebookException(VideoUploader.ERROR_BAD_SERVER_RESPONSE, e));
                            return;
                        }
                    }
                    handleError(new FacebookException(VideoUploader.ERROR_BAD_SERVER_RESPONSE));
                    return;
                }
            }
            handleError(new FacebookException(VideoUploader.ERROR_BAD_SERVER_RESPONSE));
        }

        private boolean attemptRetry(int errorCode) {
            if (this.completedRetries >= 2 || !getTransientErrorCodes().contains(Integer.valueOf(errorCode))) {
                return false;
            }
            int delay = ((int) Math.pow(3.0d, this.completedRetries)) * VideoUploader.RETRY_DELAY_UNIT_MS;
            VideoUploader.getHandler().postDelayed(new Runnable() { // from class: com.facebook.share.internal.VideoUploader.UploadWorkItemBase.1
                @Override // java.lang.Runnable
                public void run() {
                    UploadWorkItemBase.this.enqueueRetry(UploadWorkItemBase.this.completedRetries + 1);
                }
            }, delay);
            return true;
        }

        protected void endUploadWithFailure(FacebookException error) {
            issueResponseOnMainThread(error, null);
        }

        protected void issueResponseOnMainThread(final FacebookException error, final String videoId) {
            VideoUploader.getHandler().post(new Runnable() { // from class: com.facebook.share.internal.VideoUploader.UploadWorkItemBase.2
                @Override // java.lang.Runnable
                public void run() {
                    VideoUploader.issueResponse(UploadWorkItemBase.this.uploadContext, error, videoId);
                }
            });
        }
    }
}
