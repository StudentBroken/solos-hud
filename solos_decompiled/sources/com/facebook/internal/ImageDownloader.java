package com.facebook.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.facebook.FacebookException;
import com.facebook.R;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.WorkQueue;
import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes41.dex */
public class ImageDownloader {
    private static final int CACHE_READ_QUEUE_MAX_CONCURRENT = 2;
    private static final int DOWNLOAD_QUEUE_MAX_CONCURRENT = 8;
    private static Handler handler;
    private static WorkQueue downloadQueue = new WorkQueue(8);
    private static WorkQueue cacheReadQueue = new WorkQueue(2);
    private static final Map<RequestKey, DownloaderContext> pendingRequests = new HashMap();

    public static void downloadAsync(ImageRequest request) {
        if (request != null) {
            RequestKey key = new RequestKey(request.getImageUri(), request.getCallerTag());
            synchronized (pendingRequests) {
                DownloaderContext downloaderContext = pendingRequests.get(key);
                if (downloaderContext != null) {
                    downloaderContext.request = request;
                    downloaderContext.isCancelled = false;
                    downloaderContext.workItem.moveToFront();
                } else {
                    enqueueCacheRead(request, key, request.isCachedRedirectAllowed());
                }
            }
        }
    }

    public static boolean cancelRequest(ImageRequest request) {
        boolean cancelled = false;
        RequestKey key = new RequestKey(request.getImageUri(), request.getCallerTag());
        synchronized (pendingRequests) {
            DownloaderContext downloaderContext = pendingRequests.get(key);
            if (downloaderContext != null) {
                cancelled = true;
                if (downloaderContext.workItem.cancel()) {
                    pendingRequests.remove(key);
                } else {
                    downloaderContext.isCancelled = true;
                }
            }
        }
        return cancelled;
    }

    public static void prioritizeRequest(ImageRequest request) {
        RequestKey key = new RequestKey(request.getImageUri(), request.getCallerTag());
        synchronized (pendingRequests) {
            DownloaderContext downloaderContext = pendingRequests.get(key);
            if (downloaderContext != null) {
                downloaderContext.workItem.moveToFront();
            }
        }
    }

    public static void clearCache(Context context) {
        ImageResponseCache.clearCache(context);
        UrlRedirectCache.clearCache();
    }

    private static void enqueueCacheRead(ImageRequest request, RequestKey key, boolean allowCachedRedirects) {
        enqueueRequest(request, key, cacheReadQueue, new CacheReadWorkItem(request.getContext(), key, allowCachedRedirects));
    }

    private static void enqueueDownload(ImageRequest request, RequestKey key) {
        enqueueRequest(request, key, downloadQueue, new DownloadImageWorkItem(request.getContext(), key));
    }

    private static void enqueueRequest(ImageRequest request, RequestKey key, WorkQueue workQueue, Runnable workItem) {
        synchronized (pendingRequests) {
            DownloaderContext downloaderContext = new DownloaderContext();
            downloaderContext.request = request;
            pendingRequests.put(key, downloaderContext);
            downloaderContext.workItem = workQueue.addActiveWorkItem(workItem);
        }
    }

    private static void issueResponse(RequestKey key, final Exception error, final Bitmap bitmap, final boolean isCachedRedirect) {
        final ImageRequest request;
        final ImageRequest.Callback callback;
        DownloaderContext completedRequestContext = removePendingRequest(key);
        if (completedRequestContext != null && !completedRequestContext.isCancelled && (callback = (request = completedRequestContext.request).getCallback()) != null) {
            getHandler().post(new Runnable() { // from class: com.facebook.internal.ImageDownloader.1
                @Override // java.lang.Runnable
                public void run() {
                    ImageResponse response = new ImageResponse(request, error, isCachedRedirect, bitmap);
                    callback.onCompleted(response);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readFromCache(RequestKey key, Context context, boolean allowCachedRedirects) {
        Uri redirectUri;
        InputStream cachedStream = null;
        boolean isCachedRedirect = false;
        if (allowCachedRedirects && (redirectUri = UrlRedirectCache.getRedirectedUri(key.uri)) != null) {
            cachedStream = ImageResponseCache.getCachedImageStream(redirectUri, context);
            isCachedRedirect = cachedStream != null;
        }
        if (!isCachedRedirect) {
            cachedStream = ImageResponseCache.getCachedImageStream(key.uri, context);
        }
        if (cachedStream != null) {
            Bitmap bitmap = BitmapFactory.decodeStream(cachedStream);
            Utility.closeQuietly(cachedStream);
            issueResponse(key, null, bitmap, isCachedRedirect);
        } else {
            DownloaderContext downloaderContext = removePendingRequest(key);
            if (downloaderContext != null && !downloaderContext.isCancelled) {
                enqueueDownload(downloaderContext.request, key);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void download(RequestKey key, Context context) {
        HttpURLConnection connection = null;
        InputStream stream = null;
        Exception error = null;
        Bitmap bitmap = null;
        boolean issueResponse = true;
        try {
            URL url = new URL(key.uri.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            switch (connection.getResponseCode()) {
                case 200:
                    stream = ImageResponseCache.interceptAndCacheImageStream(context, connection);
                    bitmap = BitmapFactory.decodeStream(stream);
                    break;
                case 301:
                case TwitterApiErrorConstants.REGISTRATION_OPERATION_FAILED /* 302 */:
                    issueResponse = false;
                    String redirectLocation = connection.getHeaderField("location");
                    if (!Utility.isNullOrEmpty(redirectLocation)) {
                        Uri redirectUri = Uri.parse(redirectLocation);
                        UrlRedirectCache.cacheUriRedirect(key.uri, redirectUri);
                        DownloaderContext downloaderContext = removePendingRequest(key);
                        if (downloaderContext != null && !downloaderContext.isCancelled) {
                            enqueueCacheRead(downloaderContext.request, new RequestKey(redirectUri, key.tag), false);
                        }
                    }
                    break;
                default:
                    stream = connection.getErrorStream();
                    StringBuilder errorMessageBuilder = new StringBuilder();
                    if (stream != null) {
                        InputStreamReader reader = new InputStreamReader(stream);
                        char[] buffer = new char[128];
                        while (true) {
                            int bufferLength = reader.read(buffer, 0, buffer.length);
                            if (bufferLength > 0) {
                                errorMessageBuilder.append(buffer, 0, bufferLength);
                            } else {
                                Utility.closeQuietly(reader);
                            }
                        }
                    } else {
                        errorMessageBuilder.append(context.getString(R.string.com_facebook_image_download_unknown_error));
                    }
                    error = new FacebookException(errorMessageBuilder.toString());
                    break;
            }
        } catch (IOException e) {
            error = e;
        } finally {
            Utility.closeQuietly(null);
            Utility.disconnectQuietly(connection);
        }
        if (issueResponse) {
            issueResponse(key, error, bitmap, false);
        }
    }

    private static synchronized Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    private static DownloaderContext removePendingRequest(RequestKey key) {
        DownloaderContext downloaderContextRemove;
        synchronized (pendingRequests) {
            downloaderContextRemove = pendingRequests.remove(key);
        }
        return downloaderContextRemove;
    }

    private static class RequestKey {
        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        Object tag;
        Uri uri;

        RequestKey(Uri url, Object tag) {
            this.uri = url;
            this.tag = tag;
        }

        public int hashCode() {
            int result = this.uri.hashCode() + 1073;
            return (result * 37) + this.tag.hashCode();
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof RequestKey)) {
                return false;
            }
            RequestKey compareTo = (RequestKey) o;
            return compareTo.uri == this.uri && compareTo.tag == this.tag;
        }
    }

    private static class DownloaderContext {
        boolean isCancelled;
        ImageRequest request;
        WorkQueue.WorkItem workItem;

        private DownloaderContext() {
        }
    }

    private static class CacheReadWorkItem implements Runnable {
        private boolean allowCachedRedirects;
        private Context context;
        private RequestKey key;

        CacheReadWorkItem(Context context, RequestKey key, boolean allowCachedRedirects) {
            this.context = context;
            this.key = key;
            this.allowCachedRedirects = allowCachedRedirects;
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageDownloader.readFromCache(this.key, this.context, this.allowCachedRedirects);
        }
    }

    private static class DownloadImageWorkItem implements Runnable {
        private Context context;
        private RequestKey key;

        DownloadImageWorkItem(Context context, RequestKey key) {
            this.context = context;
            this.key = key;
        }

        @Override // java.lang.Runnable
        public void run() {
            ImageDownloader.download(this.key, this.context);
        }
    }
}
