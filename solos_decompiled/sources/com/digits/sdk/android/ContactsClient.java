package com.digits.sdk.android;

import android.content.Context;
import android.content.Intent;
import com.digits.sdk.vcard.VCardConfig;
import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.TwitterCore;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/* JADX INFO: loaded from: classes18.dex */
public class ContactsClient {
    public static final int MAX_PAGE_SIZE = 100;
    private ActivityClassManagerFactory activityClassManagerFactory;
    private ContactsService contactsService;
    private final ContactsPreferenceManager prefManager;
    private final TwitterCore twitterCore;

    interface ContactsService {
        @POST("/1.1/contacts/destroy/all.json")
        void deleteAll(ContactsCallback<Response> contactsCallback);

        @POST("/1.1/contacts/upload.json")
        UploadResponse upload(@Body Vcards vcards);

        @GET("/1.1/contacts/users_and_uploaded_by.json")
        void usersAndUploadedBy(@Query("next_cursor") String str, @Query("count") Integer num, ContactsCallback<Contacts> contactsCallback);
    }

    ContactsClient() {
        this(TwitterCore.getInstance(), new ContactsPreferenceManager(), new ActivityClassManagerFactory(), null);
    }

    ContactsClient(TwitterCore twitterCore, ContactsPreferenceManager prefManager, ActivityClassManagerFactory activityClassManagerFactory, ContactsService contactsService) {
        if (twitterCore == null) {
            throw new IllegalArgumentException("twitter must not be null");
        }
        if (prefManager == null) {
            throw new IllegalArgumentException("preference manager must not be null");
        }
        if (activityClassManagerFactory == null) {
            throw new IllegalArgumentException("activityClassManagerFactory must not be null");
        }
        this.twitterCore = twitterCore;
        this.prefManager = prefManager;
        this.activityClassManagerFactory = activityClassManagerFactory;
        this.contactsService = contactsService;
    }

    public void startContactsUpload() {
        startContactsUpload(R.style.Digits_default);
    }

    public void startContactsUpload(int themeResId) {
        startContactsUpload(this.twitterCore.getContext(), themeResId);
    }

    public boolean hasUserGrantedPermission() {
        return this.prefManager.hasContactImportPermissionGranted();
    }

    protected void startContactsUpload(Context context, int themeResId) {
        if (!hasUserGrantedPermission()) {
            startContactsActivity(context, themeResId);
        } else {
            startContactsService(context);
        }
    }

    private void startContactsActivity(Context context, int themeResId) {
        ActivityClassManager activityClassManager = this.activityClassManagerFactory.createActivityClassManager(context, themeResId);
        Intent intent = new Intent(context, activityClassManager.getContactsActivity());
        intent.putExtra(ThemeUtils.THEME_RESOURCE_ID, themeResId);
        intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        context.startActivity(intent);
    }

    private void startContactsService(Context context) {
        context.startService(new Intent(context, (Class<?>) ContactsUploadService.class));
    }

    private ContactsService getContactsService() {
        if (this.contactsService != null) {
            return this.contactsService;
        }
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(new DigitsApi().getBaseHostUrl()).setClient(new AuthenticatedClient(this.twitterCore.getAuthConfig(), Digits.getSessionManager().getActiveSession(), this.twitterCore.getSSLSocketFactory())).build();
        this.contactsService = (ContactsService) adapter.create(ContactsService.class);
        return this.contactsService;
    }

    public void deleteAllUploadedContacts(ContactsCallback<Response> callback) {
        getContactsService().deleteAll(callback);
    }

    public void lookupContactMatches(String nextCursor, Integer count, ContactsCallback<Contacts> callback) {
        if (count == null || count.intValue() < 1 || count.intValue() > 100) {
            getContactsService().usersAndUploadedBy(nextCursor, null, callback);
        } else {
            getContactsService().usersAndUploadedBy(nextCursor, count, callback);
        }
    }

    UploadResponse uploadContacts(Vcards vcards) {
        return getContactsService().upload(vcards);
    }
}
