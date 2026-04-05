package com.kopin.solos.share.facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.facebook.Profile;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kopin.solos.common.CommonFileUtil;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.R;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.util.Utility;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes4.dex */
public class FacebookSharingHelper {
    private static String mFacebookId = null;

    public interface ISharingCallback {
        void shared(Platforms platforms);
    }

    public static void uploadMap(final Activity activity, Bitmap bitmap, final String title, final JSONObject rideShare, final long rideId, final ISharingCallback sharing) {
        File file = CommonFileUtil.getExternalFile(activity, "solos_map.jpg");
        SharingAsync sharingAsync = new SharingAsync() { // from class: com.kopin.solos.share.facebook.FacebookSharingHelper.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.kopin.solos.share.facebook.SharingAsync, android.os.AsyncTask
            public void onPostExecute(JSONObject result) {
                if (!activity.isFinishing()) {
                    super.onPostExecute(result);
                    boolean failed = true;
                    if (result != null) {
                        Log.i("RidePreviewFrag", result.toString());
                        try {
                            if (result.has("ContentUrl")) {
                                String contentUrl = FacebookSharingHelper.getContentUrl(result);
                                String imageUrl = FacebookSharingHelper.getImageUrl(result);
                                failed = FacebookSharingHelper.facebookShare(activity, title, contentUrl, imageUrl, rideShare, rideId);
                                if (sharing != null && !failed) {
                                    sharing.shared(Platforms.Facebook);
                                }
                            } else {
                                Log.e("RidePreviewFrag", "json server message " + result.getString("message"));
                            }
                        } catch (JSONException e) {
                            Log.e("RidePreviewFrag", "error doing json from server");
                        }
                    }
                    if (failed) {
                        FacebookSharingHelper.showWarning(activity);
                    }
                }
            }
        };
        sharingAsync.setMapImage(bitmap);
        sharingAsync.setMapFile(file);
        sharingAsync.execute(rideShare);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void showWarning(Context context) {
        if (context != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog dialog = alertDialogBuilder.setTitle(R.string.share_fail_title).setMessage(R.string.share_fail_message).setCancelable(true).setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.share.facebook.FacebookSharingHelper.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog2, int id) {
                }
            }).create();
            dialog.show();
            DialogUtils.setDialogTitleDivider(dialog);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean facebookShare(Activity activity, String title, String contentUrl, String imageUrl, JSONObject rideShare, long rideId) {
        if (contentUrl != null && !contentUrl.isEmpty() && imageUrl != null && !imageUrl.isEmpty()) {
            ShareLinkContent content = new ShareLinkContent.Builder().setContentTitle(title).setContentUrl(Uri.parse(contentUrl)).setImageUrl(Uri.parse(imageUrl)).setContentDescription("Solos " + getRideInfo(rideShare)).build();
            ShareDialog dialog = new ShareDialog(activity);
            if (ShareDialog.canShow((Class<? extends ShareContent>) ShareLinkContent.class)) {
                String fid = Profile.getCurrentProfile() != null ? Profile.getCurrentProfile().getId() : "";
                SQLHelper.addShare(new Shared(rideId, Platforms.Facebook.getSharedKey(), fid));
                dialog.show(content);
                return false;
            }
        }
        Log.e("RidePrvFrag", "you cannot share your ride");
        return true;
    }

    public static String getFacebookId() {
        return mFacebookId;
    }

    public static void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }

    public static boolean isShared(long rideId) {
        return SQLHelper.isRideShared(rideId, Platforms.Facebook.getSharedKey(), mFacebookId);
    }

    public static void addDistance(JSONObject rideJson, double distance, String unit) {
        addJson(rideJson, BaseDataTypes.ID_DISTANCE, String.format("%.1f", Double.valueOf(distance)) + " " + unit);
    }

    public static void addTime(JSONObject rideJson, long time) {
        addJson(rideJson, "time", Utility.formatTime(time));
    }

    public static void addAverageSpeed(JSONObject rideJson, String averageSpeed) {
        addJson(rideJson, "average_speed", averageSpeed);
    }

    public static void addAveragePace(JSONObject rideJson, String averagePace) {
        addJson(rideJson, "average_pace", averagePace);
    }

    public static String getRideInfo(JSONObject rideJson) {
        return "" + getJsonString(rideJson, BaseDataTypes.ID_DISTANCE) + ", " + getJsonString(rideJson, "time");
    }

    public static void addImageAttributes(JSONObject rideJson, Bitmap bitmap) {
        try {
            rideJson.put("width", bitmap.getWidth());
            rideJson.put("height", bitmap.getHeight());
            Log.i("SharingAsync", "share image w,h = " + bitmap.getWidth() + ", " + bitmap.getHeight());
        } catch (JSONException je) {
            Log.e("SharingAsync", "share image json error " + je.getMessage());
        }
    }

    public static void addJson(JSONObject jsonObject, String name, String value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException je) {
            Log.e("FacebookSharingHelper", "Problem adding to Json " + je.getMessage());
            Log.e("FacebookSharingHelper", "Json name = " + name + ", " + value);
        }
    }

    public static String getContentUrl(JSONObject jsonObject) {
        return setHostPrefix(SharingAsync.getHost(), getJsonString(jsonObject, "ContentUrl"));
    }

    public static String getImageUrl(JSONObject jsonObject) {
        return setHostPrefix(SharingAsync.getHost(), getJsonString(jsonObject, "ImageUrl"));
    }

    private static String setHostPrefix(String host, String url) {
        if (url != null && !url.startsWith("http")) {
            return host + url;
        }
        return url;
    }

    private static String getJsonString(JSONObject jsonObject, String name) {
        if (jsonObject != null && jsonObject.has(name)) {
            try {
                return jsonObject.getString(name);
            } catch (JSONException je) {
                Log.e("FacebookSharingHelper", "getJsonString error " + name + ", " + je.getMessage());
            }
        }
        return null;
    }

    public static ShareHelper.ShareTask getShareTask(String id, Context context) {
        return new FacebookShareTask(id, context);
    }

    private static class FacebookShareTask extends ShareHelper.ShareTask {
        ShareHelper.Status mStatus;

        protected FacebookShareTask(String id, Context context) {
            super(Platforms.Facebook, id, context);
            this.mStatus = ShareHelper.Status.PROCESSING;
        }

        @Override // com.kopin.solos.share.ShareHelper.ShareTask
        public ShareHelper.ShareProgress doTaskInBackground(SavedWorkout ride) {
            return new ShareHelper.ShareProgress(this.mStatus, "");
        }
    }
}
