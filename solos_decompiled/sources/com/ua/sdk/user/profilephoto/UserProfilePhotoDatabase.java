package com.ua.sdk.user.profilephoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ua.sdk.cache.database.LegacyEntityDatabase;
import com.ua.sdk.suggestedfriends.SuggestedFriendsImpl;

/* JADX INFO: loaded from: classes65.dex */
public class UserProfilePhotoDatabase extends LegacyEntityDatabase<UserProfilePhoto> {
    public UserProfilePhotoDatabase(Context context) {
        super(context, SuggestedFriendsImpl.REF_PROFILE_PIC, null, null, null, 1);
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    protected void createEntityTable(SQLiteDatabase db) {
    }

    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public void onEntityUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public UserProfilePhotoImpl getEntityFromCursor(Cursor cursor) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ua.sdk.cache.database.LegacyEntityDatabase
    public ContentValues getContentValuesFromEntity(UserProfilePhoto entity) {
        return null;
    }
}
