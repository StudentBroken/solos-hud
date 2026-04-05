package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;

/* JADX INFO: loaded from: classes11.dex */
class MediaStoreRequestHandler extends ContentStreamRequestHandler {
    private static final String[] CONTENT_ORIENTATION = {"orientation"};

    MediaStoreRequestHandler(Context context) {
        super(context);
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request data) {
        Uri uri = data.uri;
        return "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
        Bitmap bitmap;
        ContentResolver contentResolver = this.context.getContentResolver();
        int exifOrientation = getExifOrientation(contentResolver, request.uri);
        String mimeType = contentResolver.getType(request.uri);
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");
        if (request.hasSize()) {
            PicassoKind picassoKind = getPicassoKind(request.targetWidth, request.targetHeight);
            if (!isVideo && picassoKind == PicassoKind.FULL) {
                return new RequestHandler.Result(null, getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
            }
            long id = ContentUris.parseId(request.uri);
            BitmapFactory.Options options = createBitmapOptions(request);
            options.inJustDecodeBounds = true;
            calculateInSampleSize(request.targetWidth, request.targetHeight, picassoKind.width, picassoKind.height, options, request);
            if (isVideo) {
                int kind = picassoKind == PicassoKind.FULL ? 1 : picassoKind.androidKind;
                bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, kind, options);
            } else {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, options);
            }
            if (bitmap != null) {
                return new RequestHandler.Result(bitmap, null, Picasso.LoadedFrom.DISK, exifOrientation);
            }
        }
        return new RequestHandler.Result(null, getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
    }

    static PicassoKind getPicassoKind(int targetWidth, int targetHeight) {
        if (targetWidth <= PicassoKind.MICRO.width && targetHeight <= PicassoKind.MICRO.height) {
            return PicassoKind.MICRO;
        }
        if (targetWidth <= PicassoKind.MINI.width && targetHeight <= PicassoKind.MINI.height) {
            return PicassoKind.MINI;
        }
        return PicassoKind.FULL;
    }

    static int getExifOrientation(ContentResolver contentResolver, Uri uri) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, CONTENT_ORIENTATION, null, null, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return 0;
            }
            int i = cursor.getInt(0);
            if (cursor == null) {
                return i;
            }
            cursor.close();
            return i;
        } catch (RuntimeException e) {
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    enum PicassoKind {
        MICRO(3, 96, 96),
        MINI(1, 512, 384),
        FULL(2, -1, -1);

        final int androidKind;
        final int height;
        final int width;

        PicassoKind(int androidKind, int width, int height) {
            this.androidKind = androidKind;
            this.width = width;
            this.height = height;
        }
    }
}
