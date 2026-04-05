package com.facebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.internal.NativeAppCallAttachmentStore;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

/* JADX INFO: loaded from: classes41.dex */
public class FacebookContentProvider extends ContentProvider {
    private static final String ATTACHMENT_URL_BASE = "content://com.facebook.app.FacebookContentProvider";
    private static final String TAG = FacebookContentProvider.class.getName();

    public static String getAttachmentUrl(String applicationId, UUID callId, String attachmentName) {
        return String.format("%s%s/%s/%s", ATTACHMENT_URL_BASE, applicationId, callId.toString(), attachmentName);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Pair<UUID, String> callIdAndAttachmentName = parseCallIdAndAttachmentName(uri);
        if (callIdAndAttachmentName == null) {
            throw new FileNotFoundException();
        }
        try {
            File file = NativeAppCallAttachmentStore.openAttachment((UUID) callIdAndAttachmentName.first, (String) callIdAndAttachmentName.second);
            return ParcelFileDescriptor.open(file, VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        } catch (FileNotFoundException exception) {
            Log.e(TAG, "Got unexpected exception:" + exception);
            throw exception;
        }
    }

    Pair<UUID, String> parseCallIdAndAttachmentName(Uri uri) {
        try {
            String callIdAndAttachmentName = uri.getPath().substring(1);
            String[] parts = callIdAndAttachmentName.split("/");
            String callIdString = parts[0];
            String attachmentName = parts[1];
            UUID callId = UUID.fromString(callIdString);
            return new Pair<>(callId, attachmentName);
        } catch (Exception e) {
            return null;
        }
    }
}
