package com.facebook.internal;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.facebook.FacebookContentProvider;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes41.dex */
public final class NativeAppCallAttachmentStore {
    static final String ATTACHMENTS_DIR_NAME = "com.facebook.NativeAppCallAttachmentStore.files";
    private static final String TAG = NativeAppCallAttachmentStore.class.getName();
    private static File attachmentsDirectory;

    private NativeAppCallAttachmentStore() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Attachment createAttachment(UUID uuid, Bitmap bitmap) {
        Validate.notNull(uuid, "callId");
        Validate.notNull(bitmap, "attachmentBitmap");
        return new Attachment(uuid, bitmap, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Attachment createAttachment(UUID uuid, Uri uri) {
        Validate.notNull(uuid, "callId");
        Validate.notNull(uri, "attachmentUri");
        return new Attachment(uuid, null, uri);
    }

    private static void processAttachmentBitmap(Bitmap bitmap, File outputFile) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } finally {
            Utility.closeQuietly(outputStream);
        }
    }

    private static void processAttachmentFile(Uri imageUri, boolean isContentUri, File outputFile) throws IOException {
        InputStream inputStream;
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        try {
            if (!isContentUri) {
                InputStream inputStream2 = new FileInputStream(imageUri.getPath());
                inputStream = inputStream2;
            } else {
                inputStream = FacebookSdk.getApplicationContext().getContentResolver().openInputStream(imageUri);
            }
            Utility.copyAndCloseInputStream(inputStream, outputStream);
        } finally {
            Utility.closeQuietly(outputStream);
        }
    }

    public static void addAttachments(Collection<Attachment> attachments) {
        if (attachments != null && attachments.size() != 0) {
            if (attachmentsDirectory == null) {
                cleanupAllAttachments();
            }
            ensureAttachmentsDirectoryExists();
            List<File> filesToCleanup = new ArrayList<>();
            try {
                for (Attachment attachment : attachments) {
                    if (attachment.shouldCreateFile) {
                        File file = getAttachmentFile(attachment.callId, attachment.attachmentName, true);
                        filesToCleanup.add(file);
                        if (attachment.bitmap != null) {
                            processAttachmentBitmap(attachment.bitmap, file);
                        } else if (attachment.originalUri != null) {
                            processAttachmentFile(attachment.originalUri, attachment.isContentUri, file);
                        }
                    }
                }
            } catch (IOException exception) {
                Log.e(TAG, "Got unexpected exception:" + exception);
                Iterator<File> it = filesToCleanup.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().delete();
                    } catch (Exception e) {
                    }
                }
                throw new FacebookException(exception);
            }
        }
    }

    public static void cleanupAttachmentsForCall(UUID callId) {
        File dir = getAttachmentsDirectoryForCall(callId, false);
        if (dir != null) {
            Utility.deleteDirectory(dir);
        }
    }

    public static File openAttachment(UUID callId, String attachmentName) throws FileNotFoundException {
        if (Utility.isNullOrEmpty(attachmentName) || callId == null) {
            throw new FileNotFoundException();
        }
        try {
            return getAttachmentFile(callId, attachmentName, false);
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    static synchronized File getAttachmentsDirectory() {
        if (attachmentsDirectory == null) {
            attachmentsDirectory = new File(FacebookSdk.getApplicationContext().getCacheDir(), ATTACHMENTS_DIR_NAME);
        }
        return attachmentsDirectory;
    }

    static File ensureAttachmentsDirectoryExists() {
        File dir = getAttachmentsDirectory();
        dir.mkdirs();
        return dir;
    }

    static File getAttachmentsDirectoryForCall(UUID callId, boolean create) {
        if (attachmentsDirectory == null) {
            return null;
        }
        File dir = new File(attachmentsDirectory, callId.toString());
        if (create && !dir.exists()) {
            dir.mkdirs();
            return dir;
        }
        return dir;
    }

    static File getAttachmentFile(UUID callId, String attachmentName, boolean createDirs) throws IOException {
        File dir = getAttachmentsDirectoryForCall(callId, createDirs);
        if (dir == null) {
            return null;
        }
        try {
            return new File(dir, URLEncoder.encode(attachmentName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static void cleanupAllAttachments() {
        File dir = getAttachmentsDirectory();
        Utility.deleteDirectory(dir);
    }

    public static final class Attachment {
        private final String attachmentName;
        private final String attachmentUrl;
        private Bitmap bitmap;
        private final UUID callId;
        private boolean isContentUri;
        private Uri originalUri;
        private boolean shouldCreateFile;

        private Attachment(UUID callId, Bitmap bitmap, Uri uri) {
            this.callId = callId;
            this.bitmap = bitmap;
            this.originalUri = uri;
            if (uri != null) {
                String scheme = uri.getScheme();
                if ("content".equalsIgnoreCase(scheme)) {
                    this.isContentUri = true;
                    this.shouldCreateFile = (uri.getAuthority() == null || uri.getAuthority().startsWith("media")) ? false : true;
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    this.shouldCreateFile = true;
                } else if (!Utility.isWebUri(uri)) {
                    throw new FacebookException("Unsupported scheme for media Uri : " + scheme);
                }
            } else if (bitmap != null) {
                this.shouldCreateFile = true;
            } else {
                throw new FacebookException("Cannot share media without a bitmap or Uri set");
            }
            this.attachmentName = !this.shouldCreateFile ? null : UUID.randomUUID().toString();
            this.attachmentUrl = !this.shouldCreateFile ? this.originalUri.toString() : FacebookContentProvider.getAttachmentUrl(FacebookSdk.getApplicationId(), callId, this.attachmentName);
        }

        public String getAttachmentUrl() {
            return this.attachmentUrl;
        }
    }
}
