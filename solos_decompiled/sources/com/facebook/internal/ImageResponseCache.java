package com.facebook.internal;

import android.content.Context;
import android.net.Uri;
import com.facebook.LoggingBehavior;
import com.facebook.internal.FileLruCache;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/* JADX INFO: loaded from: classes41.dex */
class ImageResponseCache {
    static final String TAG = ImageResponseCache.class.getSimpleName();
    private static volatile FileLruCache imageCache;

    ImageResponseCache() {
    }

    static synchronized FileLruCache getCache(Context context) throws IOException {
        if (imageCache == null) {
            imageCache = new FileLruCache(TAG, new FileLruCache.Limits());
        }
        return imageCache;
    }

    static InputStream getCachedImageStream(Uri uri, Context context) {
        if (uri == null || !isCDNURL(uri)) {
            return null;
        }
        try {
            FileLruCache cache = getCache(context);
            InputStream imageStream = cache.get(uri.toString());
            return imageStream;
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, e.toString());
            return null;
        }
    }

    static InputStream interceptAndCacheImageStream(Context context, HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() != 200) {
            return null;
        }
        Uri uri = Uri.parse(connection.getURL().toString());
        InputStream stream = connection.getInputStream();
        try {
            if (isCDNURL(uri)) {
                FileLruCache cache = getCache(context);
                return cache.interceptAndPut(uri.toString(), new BufferedHttpInputStream(stream, connection));
            }
            return stream;
        } catch (IOException e) {
            return stream;
        }
    }

    private static boolean isCDNURL(Uri uri) {
        if (uri != null) {
            String uriHost = uri.getHost();
            if (uriHost.endsWith("fbcdn.net")) {
                return true;
            }
            if (uriHost.startsWith("fbcdn") && uriHost.endsWith("akamaihd.net")) {
                return true;
            }
        }
        return false;
    }

    static void clearCache(Context context) {
        try {
            getCache(context).clearCache();
        } catch (IOException e) {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, "clearCache failed " + e.getMessage());
        }
    }

    private static class BufferedHttpInputStream extends BufferedInputStream {
        HttpURLConnection connection;

        BufferedHttpInputStream(InputStream stream, HttpURLConnection connection) {
            super(stream, 8192);
            this.connection = connection;
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            Utility.disconnectQuietly(this.connection);
        }
    }
}
