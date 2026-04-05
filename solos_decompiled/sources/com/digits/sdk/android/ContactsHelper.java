package com.digits.sdk.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.digits.sdk.vcard.VCardBuilder;
import com.digits.sdk.vcard.VCardConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes18.dex */
class ContactsHelper {
    private static final int MAX_CONTACTS = 2500;
    private static final String[] allProjectionColumns = {"mimetype", "lookup", "data2", "data3", "is_primary", "data1", "data1", "data2", "data3", "is_primary", "data1", "data2", "data3"};
    private static final String[] selectionArgs = {"vnd.android.cursor.item/phone_v2", "vnd.android.cursor.item/email_v2", "vnd.android.cursor.item/name"};
    private static final String selectionQuery = "mimetype=? OR mimetype=? OR mimetype=?";
    private final Context context;

    ContactsHelper(Context context) {
        this.context = context;
    }

    public Cursor getContactsCursor() {
        HashSet<String> tempSet = new HashSet<>(Arrays.asList(allProjectionColumns));
        String[] projectionColumns = (String[]) tempSet.toArray(new String[tempSet.size()]);
        Uri uri = ContactsContract.Data.CONTENT_URI.buildUpon().appendQueryParameter("limit", Integer.toString(MAX_CONTACTS)).build();
        return this.context.getContentResolver().query(uri, projectionColumns, selectionQuery, selectionArgs, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<java.lang.String> createContactList(android.database.Cursor r10) {
        /*
            Method dump skipped, instruction units count: 204
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.digits.sdk.android.ContactsHelper.createContactList(android.database.Cursor):java.util.List");
    }

    private List<String> processContactsMap(Map<String, List<ContentValues>> mapContactsData) {
        List<String> vCards = new ArrayList<>();
        Map<String, List<ContentValues>> contactMimeTypeMap = new HashMap<>();
        VCardBuilder builder = new VCardBuilder(VCardConfig.VCARD_TYPE_V30_GENERIC, "UTF-8");
        for (String key : mapContactsData.keySet()) {
            List<ContentValues> contentValuesList = mapContactsData.get(key);
            boolean hasPhoneOrEmail = false;
            contactMimeTypeMap.clear();
            builder.clear();
            for (ContentValues cv : contentValuesList) {
                String mimeType = cv.getAsString("mimetype");
                if ("vnd.android.cursor.item/phone_v2".equals(mimeType) || "vnd.android.cursor.item/email_v2".equals(mimeType)) {
                    hasPhoneOrEmail = true;
                }
                List<ContentValues> group = contactMimeTypeMap.get(mimeType);
                if (group == null) {
                    group = new ArrayList<>();
                    contactMimeTypeMap.put(mimeType, group);
                }
                group.add(cv);
            }
            if (hasPhoneOrEmail) {
                builder.appendNameProperties(contactMimeTypeMap.get("vnd.android.cursor.item/name")).appendPhones(contactMimeTypeMap.get("vnd.android.cursor.item/phone_v2"), null).appendEmails(contactMimeTypeMap.get("vnd.android.cursor.item/email_v2"));
                String vcard = builder.toString();
                vCards.add(vcard);
            }
        }
        return vCards;
    }
}
