package com.kopin.solos.storage;

import android.database.Cursor;
import android.provider.BaseColumns;
import com.kopin.solos.common.SportType;

/* JADX INFO: loaded from: classes54.dex */
public class Shared implements BaseColumns {
    public static final String EXTERNAL_ID = "sExternalId";
    public static final String ITEM_ID = "itemId";
    public static final String NAME = "Shared";
    public static final String PROVIDER_ID = "sProId";
    public static final String TYPE = "Type";
    public static final String UPDATED_TIME = "UpdatedTime";
    public static final String USERNAME = "sUser";
    public static final String WAS_IMPORTED = "sWasImported";
    public String mExternalId;
    private long mId;
    private int mProviderId;
    private String mUserName;
    private boolean mWasImported;
    public ShareType shareType;
    public long updatedTime;

    public enum ShareType {
        RIDE,
        BIKE,
        FTP,
        PROFILE,
        ROUTE,
        RIDE_DATA,
        NONE,
        GEAR,
        RUN,
        RUN_DATA,
        TRAINING;

        public int getId() {
            return ordinal();
        }

        public static ShareType get(int id) {
            return id < 0 ? RIDE : values()[id];
        }

        public static ShareType fromSportType(SportType sportType) {
            switch (sportType) {
                case RIDE:
                    return RIDE;
                case RUN:
                    return RUN;
                default:
                    return null;
            }
        }
    }

    public Shared(Cursor cursor) {
        boolean z = false;
        this.mExternalId = "";
        this.shareType = ShareType.RIDE;
        this.updatedTime = 0L;
        int pos = cursor.getColumnIndex(ITEM_ID);
        this.mId = pos != -1 ? cursor.getLong(pos) : 0L;
        int pos2 = cursor.getColumnIndex(PROVIDER_ID);
        this.mProviderId = pos2 != -1 ? cursor.getInt(pos2) : 0;
        int pos3 = cursor.getColumnIndex(USERNAME);
        this.mUserName = pos3 != -1 ? cursor.getString(pos3) : "";
        int pos4 = cursor.getColumnIndex(EXTERNAL_ID);
        this.mExternalId = pos4 != -1 ? cursor.getString(pos4) : "";
        int pos5 = cursor.getColumnIndex(WAS_IMPORTED);
        if (pos5 != -1 && cursor.getInt(pos5) != 0) {
            z = true;
        }
        this.mWasImported = z;
        int pos6 = cursor.getColumnIndex(TYPE);
        this.shareType = ShareType.get(pos6 != -1 ? cursor.getInt(pos6) : -1);
        int pos7 = cursor.getColumnIndex(UPDATED_TIME);
        this.updatedTime = pos7 != -1 ? cursor.getLong(pos7) : 0L;
    }

    public Shared(long id, int providerKey, String userName) {
        this.mExternalId = "";
        this.shareType = ShareType.RIDE;
        this.updatedTime = 0L;
        this.mId = id;
        this.mProviderId = providerKey;
        this.mUserName = userName;
        this.mExternalId = "";
        this.mWasImported = false;
    }

    public static Shared newShare(long id, int providerKey, String userName, String uploadId) {
        return new Shared(id, providerKey, userName, uploadId, false);
    }

    public static Shared newShare(long id, int providerKey, String userName, String uploadId, SportType sportType) {
        return new Shared(id, providerKey, userName, uploadId, false, ShareType.fromSportType(sportType));
    }

    public static Shared newImport(long id, int providerKey, String userName, String importId, ShareType shareType) {
        return new Shared(id, providerKey, userName, importId, true, shareType);
    }

    public Shared(long id, int providerKey, String userName, String extId, boolean wasImport) {
        this.mExternalId = "";
        this.shareType = ShareType.RIDE;
        this.updatedTime = 0L;
        this.mId = id;
        this.mProviderId = providerKey;
        this.mUserName = userName;
        this.mExternalId = extId;
        this.mWasImported = wasImport;
    }

    public Shared(long id, int providerKey, String userName, String externalId, boolean wasImport, ShareType shareType) {
        this.mExternalId = "";
        this.shareType = ShareType.RIDE;
        this.updatedTime = 0L;
        this.mId = id;
        this.mProviderId = providerKey;
        this.mUserName = userName;
        this.mExternalId = externalId;
        this.mWasImported = wasImport;
        this.shareType = shareType;
    }

    public Shared(long id, int providerKey, String userName, String externalId, boolean wasImport, ShareType shareType, long updatedTime) {
        this.mExternalId = "";
        this.shareType = ShareType.RIDE;
        this.updatedTime = 0L;
        this.mId = id;
        this.mProviderId = providerKey;
        this.mUserName = userName;
        this.mExternalId = externalId;
        this.mWasImported = wasImport;
        this.shareType = shareType;
        this.updatedTime = updatedTime;
    }

    public long getId() {
        return this.mId;
    }

    public int getProviderId() {
        return this.mProviderId;
    }

    public String getUserName() {
        return this.mUserName;
    }

    public String getImportedFromId() {
        return this.mExternalId;
    }

    public boolean wasImported() {
        return this.mWasImported;
    }

    public boolean isUnsynced() {
        return this.mExternalId == null || this.mExternalId.isEmpty() || this.updatedTime == 0;
    }

    public boolean equals(Object o) {
        return (o instanceof Shared) && ((Shared) o).mProviderId == this.mProviderId;
    }

    public String toString() {
        return "" + this.mId + ", provider " + this.mProviderId + ", user " + this.mUserName + ", mExternalId " + this.mExternalId + ", shareType " + this.shareType.name();
    }
}
