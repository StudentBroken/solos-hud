package com.twitter.sdk.android.tweetcomposer;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import java.io.File;

/* JADX INFO: loaded from: classes29.dex */
class FileUtils {
    private static final String MEDIA_SCHEME = "com.android.providers.media.documents";

    FileUtils() {
    }

    @TargetApi(19)
    static String getPath(Context context, Uri uri) {
        boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        if (isKitKat && isMediaDocumentAuthority(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            String[] parts = documentId.split(":");
            String type = parts[0];
            if (!"image".equals(type)) {
                return null;
            }
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] args = {parts[1]};
            return resolveFilePath(context, contentUri, "_id=?", args);
        }
        if (isContentScheme(uri)) {
            return resolveFilePath(context, uri, null, null);
        }
        if (isFileScheme(uri)) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isMediaDocumentAuthority(Uri uri) {
        return MEDIA_SCHEME.equalsIgnoreCase(uri.getAuthority());
    }

    public static boolean isContentScheme(Uri uri) {
        return "content".equalsIgnoreCase(uri.getScheme());
    }

    public static boolean isFileScheme(Uri uri) {
        return "file".equalsIgnoreCase(uri.getScheme());
    }

    static String resolveFilePath(Context context, Uri uri, String selection, String[] args) {
        Cursor cursor = null;
        String[] projection = {"_data"};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, args, null);
            if (cursor != null && cursor.moveToFirst()) {
                int i = cursor.getColumnIndexOrThrow("_data");
                String string = cursor.getString(i);
            }
            if (cursor != null) {
                cursor.close();
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    static String getMimeType(File file) {
        String ext = getExtension(file.getName());
        return !TextUtils.isEmpty(ext) ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) : "application/octet-stream";
    }

    static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int i = filename.lastIndexOf(".");
        return i < 0 ? "" : filename.substring(i + 1);
    }
}
