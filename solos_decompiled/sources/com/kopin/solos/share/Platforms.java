package com.kopin.solos.share;

import android.content.Context;
import android.content.Intent;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.facebook.FacebookSharingHelper;
import com.kopin.solos.share.peloton.PelotonHelper;
import com.kopin.solos.share.strava.StravaHelper;
import com.kopin.solos.share.trainingpeaks.TPHelper;
import com.kopin.solos.share.underarmour.MapMyHelper;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.Workout;
import java.util.HashSet;

/* JADX INFO: loaded from: classes4.dex */
public enum Platforms {
    FileExport(null, 0, 0, R.string.save_to_file, R.drawable.ic_save_icon),
    Strava("strava", R.string.strava, R.string.caps_strava, R.string.share_to_strava, R.drawable.ic_strava_icon),
    TrainingPeaks("tp", R.string.training_peaks, R.string.caps_tp, R.string.share_to_tp, R.drawable.ic_training_peaks),
    UnderArmour("ua", R.string.under_armour, R.string.caps_ua, R.string.share_to_ua, R.drawable.ic_map_my_ride),
    Facebook("fb", R.string.facebook, R.string.caps_fb, R.string.share_to_facebook, R.drawable.ic_facebook_icon),
    Twitter(com.twitter.sdk.android.BuildConfig.ARTIFACT_ID, R.string.twitter, R.string.caps_twitter, R.string.share_to_twitter, R.drawable.twitter_logo_ic),
    Peloton("peloton", R.string.peloton, R.string.caps_peloton, R.string.share_to_peloton, R.drawable.twitter_logo_ic),
    None("None", 0, 0, 0, 0);

    private static final int OFFSET_LOGIN_REQ = 10;
    private static final int OFFSET_SQL_SHARED = 100;
    private static final String PREF_PREFIX_LOGIN = "share_login_";
    private static final String PREF_PREFIX_SHARE = "share_auto_";
    private HashSet<String> mImportedCache = SQLHelper.getImportedRideIds(getSharedKey());
    private int mNameId;
    private int mPrefCatResId;
    private String mPrefKey;
    private int mShareMenuIconId;
    private int mShareMenuResId;

    Platforms(String k, int nameId, int catId, int titleId, int iconId) {
        this.mPrefKey = k;
        this.mNameId = nameId;
        this.mPrefCatResId = catId;
        this.mShareMenuResId = titleId;
        this.mShareMenuIconId = iconId;
    }

    public int getNameId() {
        return this.mNameId;
    }

    public int getPrefCategory() {
        return this.mPrefCatResId;
    }

    public String getLoginPrefKey() {
        return PREF_PREFIX_LOGIN + this.mPrefKey;
    }

    public String getAutoSharePrefKey() {
        return PREF_PREFIX_SHARE + this.mPrefKey;
    }

    public int getSharedKey() {
        return ordinal() + 100;
    }

    public int getRequestCode() {
        return (ordinal() + 1) * 10;
    }

    public int getMenuTitleResId() {
        return this.mShareMenuResId;
    }

    public int getMenuIconId() {
        return this.mShareMenuIconId;
    }

    public String getKey() {
        return this.mPrefKey;
    }

    public boolean isShared(long rideId) {
        switch (this) {
            case FileExport:
            default:
                return false;
            case Strava:
                return StravaHelper.isShared(rideId);
            case TrainingPeaks:
                return TPHelper.isShared(rideId);
            case UnderArmour:
                return MapMyHelper.isShared(rideId);
            case Facebook:
                return FacebookSharingHelper.isShared(rideId);
            case Peloton:
                return SQLHelper.isRideShared(rideId, Peloton.getSharedKey(), PelotonPrefs.getEmail()) && SQLHelper.isItemShared(rideId, Peloton.getSharedKey(), PelotonPrefs.getEmail(), Shared.ShareType.RIDE_DATA);
        }
    }

    public boolean isLoggedIn(Context context) {
        switch (this) {
            case FileExport:
                return false;
            case Strava:
                return StravaHelper.isLoggedIn();
            case TrainingPeaks:
                if (context != null) {
                    return TPHelper.isLoggedIn(context);
                }
                return false;
            case UnderArmour:
                return MapMyHelper.isLoggedIn();
            case Facebook:
            default:
                return true;
            case Peloton:
                return PelotonHelper.isLoggedIn();
        }
    }

    public void logout(Context context) {
        switch (this) {
            case Strava:
                StravaHelper.logOut(context);
                break;
            case TrainingPeaks:
                TPHelper.logout(context);
                break;
            case UnderArmour:
                MapMyHelper.logOut();
                break;
        }
    }

    public boolean hasAutoShare(Context context) {
        switch (this) {
            case Strava:
                return StravaHelper.hasAutoShare(context);
            case TrainingPeaks:
                return TPHelper.hasAutoShare(context);
            case UnderArmour:
                return MapMyHelper.hasAutoShare(context);
            default:
                return false;
        }
    }

    public Shared.ShareType getShareTypeForObject(Object data) {
        switch (this) {
            case Peloton:
                return PelotonHelper.getShareType(data);
            default:
                return Shared.ShareType.NONE;
        }
    }

    private void addToImportCache(String id) {
        this.mImportedCache.add(id);
    }

    public void removeFromImportCache(String id) {
        this.mImportedCache.remove(id);
    }

    public void addShare(Shared shared) {
        if (shared.wasImported()) {
            addToImportCache(shared.getImportedFromId());
        }
        SQLHelper.addShare(shared);
    }

    public void removeShare(String externalId, Shared.ShareType shareType) {
        removeFromImportCache(externalId);
        SQLHelper.removeShare(externalId, shareType);
    }

    public boolean wasImported(String platformRideId) {
        return this.mImportedCache.contains(platformRideId);
    }

    public boolean wasImported(long rideId) {
        return SQLHelper.wasImported(rideId, getSharedKey());
    }

    public boolean wasImported(SavedWorkout ride) {
        return SQLHelper.wasImported(ride.getId(), getSharedKey());
    }

    public boolean wasImported(Workout.Header ride) {
        return SQLHelper.wasImported(ride.getId(), getSharedKey());
    }

    public String getLocalIdFor(long sharedId) {
        return SQLHelper.lookupSharedId(String.valueOf(sharedId), getSharedKey(), Shared.ShareType.RIDE);
    }

    public void onActivityResult(Context context, Intent data, ShareHelper.AuthListener cb) {
        switch (this) {
            case Strava:
                StravaHelper.onActivityResult(context, data, cb);
                break;
            case TrainingPeaks:
                TPHelper.onActivityResult(context, data, cb);
                break;
            case UnderArmour:
                MapMyHelper.onActivityResult(data, cb);
                break;
            case Peloton:
                if (PelotonHelper.isLoggedIn()) {
                    cb.onResult(Peloton, true, "");
                }
                break;
        }
    }

    public static Platforms fromKey(String key) {
        for (Platforms p : values()) {
            if (p.getKey() != null && key != null && p.getKey().equals(key)) {
                return p;
            }
        }
        return null;
    }

    public static Platforms fromIdKey(int key) {
        for (Platforms p : values()) {
            if (key == p.getNameId()) {
                return p;
            }
        }
        return null;
    }

    public static void init(Context context) {
        StravaHelper.init(context);
        MapMyHelper.init(context);
    }
}
