package com.ua.sdk.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import com.facebook.share.internal.ShareConstants;
import com.ua.oss.org.apache.http.entity.ContentType;
import com.ua.oss.org.apache.http.entity.mime.HttpMultipartMode;
import com.ua.oss.org.apache.http.entity.mime.MultipartEntityBuilder;
import com.ua.sdk.Resource;
import com.ua.sdk.UaException;
import com.ua.sdk.UaLog;
import com.ua.sdk.UploadCallback;
import com.ua.sdk.activitystory.AttachmentDest;
import com.ua.sdk.authentication.AuthenticationManager;
import com.ua.sdk.authentication.FilemobileCredential;
import com.ua.sdk.authentication.FilemobileCredentialManager;
import com.ua.sdk.internal.net.UrlBuilder;
import com.ua.sdk.util.Media;
import com.ua.sdk.util.ProgressHttpEntity;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

/* JADX INFO: loaded from: classes65.dex */
public class MediaService<T extends Resource> {
    protected static final int MAX_HEIGHT = 1000;
    protected static final int MAX_WIDTH = 1000;
    protected final AuthenticationManager authManager;
    protected final ConnectionFactory connFactory;
    protected final Context context;
    protected FilemobileCredentialManager filemobileCredentialManager;
    protected final JsonParser<T> jsonParser;
    protected ProgressOutputStream outputStream;
    protected final UrlBuilder urlBuilder;

    public MediaService(Context context, ConnectionFactory connFactory, UrlBuilder urlBuilder, JsonParser<T> jsonParser, AuthenticationManager authManager) {
        this.context = context;
        this.connFactory = connFactory;
        this.urlBuilder = urlBuilder;
        this.jsonParser = jsonParser;
        this.authManager = authManager;
    }

    public MediaService(Context context, ConnectionFactory connFactory, UrlBuilder urlBuilder, JsonParser<T> jsonParser, AuthenticationManager authManager, FilemobileCredentialManager filemobileCredentialManager) {
        this(context, connFactory, urlBuilder, jsonParser, authManager);
        this.filemobileCredentialManager = filemobileCredentialManager;
    }

    public void close() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
        }
    }

    public T uploadUserProfileImage(Uri image, T entity) throws UaException {
        Precondition.isNotNull(image, "image");
        try {
            URL url = this.urlBuilder.buildGetUserProfilePhotoUrl(entity.getRef());
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            File imageFile = getFile(image);
            Precondition.isNotNull(imageFile, "imageFile");
            try {
                this.authManager.signAsUser(conn);
                ByteArrayOutputStream bos = compressBitmap(resizeBitmap(imageFile, 1000, 1000));
                int length = bos.size();
                conn.setRequestMethod(HttpRequest.METHOD_PUT);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "image/jpeg");
                conn.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, String.valueOf(length));
                conn.setFixedLengthStreamingMode(length);
                bos.writeTo(conn.getOutputStream());
                bos.close();
                Precondition.isResponseSuccess(conn);
                return this.jsonParser.parse(conn.getInputStream());
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Upload image cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable t) {
            UaLog.error("Unable to upload image.", t);
            throw new UaException("Unable to upload image.", t);
        }
    }

    public T uploadImage(Uri image, AttachmentDest dest, UploadCallback callback) throws UaException {
        Precondition.isNotNull(dest, "dest");
        Precondition.isNotNull(image, "image");
        try {
            URL url = this.urlBuilder.buildPostImageUrl();
            HttpsURLConnection conn = this.connFactory.getSslConnection(url);
            try {
                this.authManager.signAsUser(conn);
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                ProgressHttpEntity body = createHttpBodyEntity(image, dest);
                long length = body.getContentLength();
                conn.setFixedLengthStreamingMode((int) length);
                conn.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, String.valueOf(length));
                Header contentType = body.getContentType();
                conn.setRequestProperty(contentType.getName(), contentType.getValue());
                Header contentEncoding = body.getContentEncoding();
                if (contentEncoding != null) {
                    conn.setRequestProperty(contentEncoding.getName(), contentEncoding.getValue());
                }
                this.outputStream = new ProgressOutputStream(conn.getOutputStream(), length, callback);
                body.writeTo(this.outputStream);
                Precondition.isResponseSuccess(conn);
                return null;
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Upload image cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable t) {
            UaLog.error("Unable to upload image.", t);
            throw new UaException("Unable to upload image.", t);
        }
    }

    public T uploadVideo(Uri video, AttachmentDest dest, UploadCallback callback) throws UaException {
        Precondition.isNotNull(dest, "dest");
        Precondition.isNotNull(video, "video");
        try {
            URL url = this.urlBuilder.buildPostVideoUrl();
            HttpURLConnection conn = this.connFactory.getConnection(url);
            try {
                FilemobileCredential credentials = this.filemobileCredentialManager.getFilemobileTokenCredentials();
                conn.setRequestMethod(HttpRequest.METHOD_POST);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                ProgressHttpEntity body = createHttpBodyEntity(credentials, video, dest);
                long length = body.getContentLength();
                conn.setFixedLengthStreamingMode((int) length);
                conn.setRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, String.valueOf(length));
                Header contentType = body.getContentType();
                conn.setRequestProperty(contentType.getName(), contentType.getValue());
                Header contentEncoding = body.getContentEncoding();
                if (contentEncoding != null) {
                    conn.setRequestProperty(contentEncoding.getName(), contentEncoding.getValue());
                }
                this.outputStream = new ProgressOutputStream(conn.getOutputStream(), length, callback);
                body.writeTo(this.outputStream);
                Precondition.isResponseSuccess(conn);
                return null;
            } finally {
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            UaLog.debug("Upload video cancelled.");
            throw new UaException(UaException.Code.CANCELED, e);
        } catch (Throwable t) {
            UaLog.error("Unable to upload video.", t);
            throw new UaException("Unable to upload video.", t);
        }
    }

    protected ContentType getContentType(Uri uri) {
        String extension;
        String type = this.context.getContentResolver().getType(uri);
        if (type == null && (extension = MimeTypeMap.getFileExtensionFromUrl(uri.getPath())) != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        if (type == null) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(uri.getPath(), opt);
            type = opt.outMimeType;
        }
        if (type != null) {
            return ContentType.create(type);
        }
        return null;
    }

    protected File getFile(Uri uri) throws UaException {
        String path = Media.getPath(this.context, uri);
        Precondition.isNotNull(path, "path");
        return new File(path);
    }

    protected ByteArrayOutputStream compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        return bos;
    }

    protected int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    protected Bitmap rotateBitmap(Bitmap photo, int rotationDeg) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDeg);
        return Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
    }

    protected Bitmap resizeBitmap(File photo, int availableWidth, int availableHeight) throws IOException {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmapOptions.inDither = true;
        BitmapFactory.decodeFile(photo.getAbsolutePath(), bitmapOptions);
        bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, availableWidth, availableHeight);
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap thumbnailBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), bitmapOptions);
        ExifInterface exif = new ExifInterface(photo.getAbsolutePath());
        switch (exif.getAttributeInt("Orientation", 1)) {
            case 3:
                return rotateBitmap(thumbnailBitmap, 180);
            case 4:
            case 5:
            case 7:
            default:
                return thumbnailBitmap;
            case 6:
                return rotateBitmap(thumbnailBitmap, 90);
            case 8:
                return rotateBitmap(thumbnailBitmap, -90);
        }
    }

    private ProgressHttpEntity createHttpBodyEntity(FilemobileCredential credentials, Uri video, AttachmentDest dest) throws UaException {
        ContentType videoType = getContentType(video);
        File videoFile = getFile(video);
        Precondition.isNotNull(videoFile, "videoFile");
        HttpEntity entity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addTextBody("sessiontoken", credentials.getToken()).addTextBody("vhost", credentials.getVhost()).addTextBody("uid", credentials.getUid()).addTextBody("meta[user][user_id]", dest.getUserId()).addTextBody("meta[user][href]", dest.getHref()).addTextBody("meta[user][index]", String.valueOf(dest.getIndex())).addTextBody("meta[user][rel]", dest.getRel()).addBinaryBody("file", videoFile, videoType, video.getLastPathSegment()).build();
        return new ProgressHttpEntity(entity);
    }

    private ProgressHttpEntity createHttpBodyEntity(Uri image, AttachmentDest dest) throws UaException {
        ContentType jsonType = ContentType.create("application/json");
        ContentType imageType = getContentType(image);
        File imageFile = getFile(image);
        Precondition.isNotNull(imageFile);
        UaLog.debug("request=" + dest.toString());
        HttpEntity entity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addBinaryBody(ShareConstants.WEB_DIALOG_PARAM_DATA, dest.toString().getBytes(), jsonType, "page_json.json").addBinaryBody("image", imageFile, imageType, image.getLastPathSegment()).build();
        return new ProgressHttpEntity(entity);
    }
}
