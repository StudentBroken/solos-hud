package com.kopin.peloton.facebook;

import android.graphics.Bitmap;
import android.util.Log;
import com.kopin.peloton.Cloud;
import com.ua.sdk.datapoint.BaseDataTypes;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes61.dex */
public class FacebookSharingHelper {
    private static String mFacebookId = null;

    public static String getFacebookId() {
        return mFacebookId;
    }

    public static void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }

    public static void addDistance(JSONObject rideJson, double distance, String unit) {
        addJson(rideJson, BaseDataTypes.ID_DISTANCE, String.format("%.1f", Double.valueOf(distance)) + " " + unit);
    }

    public static void addAverageSpeed(JSONObject rideJson, String averageSpeed) {
        addJson(rideJson, "average_speed", averageSpeed);
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
        return setHostPrefix(Cloud.getHost(), getJsonString(jsonObject, "ContentUrl"));
    }

    public static String getImageUrl(JSONObject jsonObject) {
        return setHostPrefix(Cloud.getHost(), getJsonString(jsonObject, "ImageUrl"));
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
}
