package com.facebook;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes41.dex */
public final class Profile implements Parcelable {
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator() { // from class: com.facebook.Profile.2
        @Override // android.os.Parcelable.Creator
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override // android.os.Parcelable.Creator
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
    private static final String FIRST_NAME_KEY = "first_name";
    private static final String ID_KEY = "id";
    private static final String LAST_NAME_KEY = "last_name";
    private static final String LINK_URI_KEY = "link_uri";
    private static final String MIDDLE_NAME_KEY = "middle_name";
    private static final String NAME_KEY = "name";
    private final String firstName;
    private final String id;
    private final String lastName;
    private final Uri linkUri;
    private final String middleName;
    private final String name;

    public static Profile getCurrentProfile() {
        return ProfileManager.getInstance().getCurrentProfile();
    }

    public static void setCurrentProfile(Profile profile) {
        ProfileManager.getInstance().setCurrentProfile(profile);
    }

    public static void fetchProfileForCurrentAccessToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            setCurrentProfile(null);
        } else {
            Utility.getGraphMeRequestWithCacheAsync(accessToken.getToken(), new Utility.GraphMeRequestWithCacheCallback() { // from class: com.facebook.Profile.1
                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onSuccess(JSONObject userInfo) {
                    String id = userInfo.optString("id");
                    if (id != null) {
                        String link = userInfo.optString("link");
                        Profile profile = new Profile(id, userInfo.optString(Profile.FIRST_NAME_KEY), userInfo.optString(Profile.MIDDLE_NAME_KEY), userInfo.optString(Profile.LAST_NAME_KEY), userInfo.optString("name"), link != null ? Uri.parse(link) : null);
                        Profile.setCurrentProfile(profile);
                    }
                }

                @Override // com.facebook.internal.Utility.GraphMeRequestWithCacheCallback
                public void onFailure(FacebookException error) {
                }
            });
        }
    }

    public Profile(String id, @Nullable String firstName, @Nullable String middleName, @Nullable String lastName, @Nullable String name, @Nullable Uri linkUri) {
        Validate.notNullOrEmpty(id, "id");
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.name = name;
        this.linkUri = linkUri;
    }

    public Uri getProfilePictureUri(int width, int height) {
        return ImageRequest.getProfilePictureUri(this.id, width, height);
    }

    public String getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getName() {
        return this.name;
    }

    public Uri getLinkUri() {
        return this.linkUri;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Profile)) {
            return false;
        }
        Profile o = (Profile) other;
        return (this.id.equals(o.id) && this.firstName == null) ? o.firstName == null : (this.firstName.equals(o.firstName) && this.middleName == null) ? o.middleName == null : (this.middleName.equals(o.middleName) && this.lastName == null) ? o.lastName == null : (this.lastName.equals(o.lastName) && this.name == null) ? o.name == null : (this.name.equals(o.name) && this.linkUri == null) ? o.linkUri == null : this.linkUri.equals(o.linkUri);
    }

    public int hashCode() {
        int result = this.id.hashCode() + 527;
        if (this.firstName != null) {
            result = (result * 31) + this.firstName.hashCode();
        }
        if (this.middleName != null) {
            result = (result * 31) + this.middleName.hashCode();
        }
        if (this.lastName != null) {
            result = (result * 31) + this.lastName.hashCode();
        }
        if (this.name != null) {
            result = (result * 31) + this.name.hashCode();
        }
        if (this.linkUri != null) {
            return (result * 31) + this.linkUri.hashCode();
        }
        return result;
    }

    JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", this.id);
            jsonObject.put(FIRST_NAME_KEY, this.firstName);
            jsonObject.put(MIDDLE_NAME_KEY, this.middleName);
            jsonObject.put(LAST_NAME_KEY, this.lastName);
            jsonObject.put("name", this.name);
            if (this.linkUri != null) {
                jsonObject.put(LINK_URI_KEY, this.linkUri.toString());
                return jsonObject;
            }
            return jsonObject;
        } catch (JSONException e) {
            return null;
        }
    }

    Profile(JSONObject jsonObject) {
        this.id = jsonObject.optString("id", null);
        this.firstName = jsonObject.optString(FIRST_NAME_KEY, null);
        this.middleName = jsonObject.optString(MIDDLE_NAME_KEY, null);
        this.lastName = jsonObject.optString(LAST_NAME_KEY, null);
        this.name = jsonObject.optString("name", null);
        String linkUriString = jsonObject.optString(LINK_URI_KEY, null);
        this.linkUri = linkUriString != null ? Uri.parse(linkUriString) : null;
    }

    private Profile(Parcel source) {
        this.id = source.readString();
        this.firstName = source.readString();
        this.middleName = source.readString();
        this.lastName = source.readString();
        this.name = source.readString();
        String linkUriString = source.readString();
        this.linkUri = linkUriString == null ? null : Uri.parse(linkUriString);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.middleName);
        dest.writeString(this.lastName);
        dest.writeString(this.name);
        dest.writeString(this.linkUri == null ? null : this.linkUri.toString());
    }
}
