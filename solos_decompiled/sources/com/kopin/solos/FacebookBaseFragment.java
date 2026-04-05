package com.kopin.solos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PointerIconCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;
import com.kopin.solos.Fragments.BaseServiceFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.menu.ShareMenuAdapter;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.Sync;
import com.kopin.solos.share.facebook.FacebookSharingHelper;
import com.kopin.solos.storage.file.FileUtil;
import com.kopin.solos.storage.file.ImageUtil;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Utility;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes24.dex */
public class FacebookBaseFragment extends BaseServiceFragment implements FacebookSharingHelper.ISharingCallback {
    private static final String FACEBOOK_IMAGE_URL = "https://graph.facebook.com/%s/picture?type=large";
    private static final String PROFILE_PICTURE = "solos_profile.jpg";
    protected AccessTokenTracker accessTokenTracker;
    protected CallbackManager callBackManager;
    protected LoginButton loginButton;
    protected Bitmap mBmpProfile;
    protected static final String[] FACEBOOK_PERMISSIONS = {"public_profile, email, user_birthday, user_friends"};
    public static int REQUEST_IMAGE_CAPTURE = 1001;
    public static int RESULT_LOAD_IMG = PointerIconCompat.TYPE_HAND;

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.callBackManager = CallbackManager.Factory.create();
    }

    public static boolean isLoggedInFacebook() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    protected void initFacebookLoginButton(View view) {
        initFacebookLoginButton(view, true);
    }

    protected void initFacebookLoginButton(View view, boolean enabled) {
        if (isStillRequired()) {
            this.loginButton = (LoginButton) view.findViewById(R.id.login_button);
            this.loginButton.setReadPermissions(Arrays.asList(FACEBOOK_PERMISSIONS));
            this.loginButton.setEnabled(enabled);
            setFacebookLoginButtonCallback(getActivity().getApplicationContext());
            listenToFacebookState();
        }
    }

    private void listenToFacebookState() {
        this.accessTokenTracker = new AccessTokenTracker() { // from class: com.kopin.solos.FacebookBaseFragment.1
            @Override // com.facebook.AccessTokenTracker
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    FacebookSharingHelper.setFacebookId(null);
                    FacebookBaseFragment.this.updateView();
                } else {
                    FacebookSharingHelper.setFacebookId(currentAccessToken.getUserId());
                }
            }
        };
    }

    @Override // android.app.Fragment
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!isFinishing()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionUtil.GrantResult cameraResult = PermissionUtil.grantedState(Permission.CAMERA, permissions, grantResults);
            switch (cameraResult) {
                case GRANTED:
                    camera(REQUEST_IMAGE_CAPTURE);
                    break;
                case DENIED:
                    addProfilePhoto(REQUEST_IMAGE_CAPTURE, RESULT_LOAD_IMG);
                    break;
            }
            PermissionUtil.GrantResult galleryResult = PermissionUtil.grantedState(Permission.READ_EXTERNAL_STORAGE, permissions, grantResults);
            switch (galleryResult) {
                case GRANTED:
                    gallery(RESULT_LOAD_IMG);
                    break;
                case DENIED:
                    addProfilePhoto(REQUEST_IMAGE_CAPTURE, RESULT_LOAD_IMG);
                    break;
            }
        }
    }

    protected void updateView() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addProfilePhoto(final int cameraId, final int galleryId) {
        if (getActivity().getPackageManager().hasSystemFeature("android.hardware.camera")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_profile_photo_title).setMessage(R.string.dialog_profile_photo_message).setCancelable(true).setNegativeButton(R.string.dialog_profile_photo_btn_camera, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.FacebookBaseFragment.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    FacebookBaseFragment.this.camera(cameraId);
                }
            }).setPositiveButton(R.string.dialog_profile_photo_btn_gallery, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.FacebookBaseFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    FacebookBaseFragment.this.gallery(galleryId);
                }
            });
            AlertDialog alert = builder.create();
            alert.getWindow().setLayout(380, 300);
            alert.show();
            DialogUtils.setDialogTitleDivider(alert);
            return;
        }
        gallery(galleryId);
    }

    protected void camera(int id) {
        if (!PermissionUtil.askPermission(getActivity(), Permission.CAMERA)) {
            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, id);
            }
        }
    }

    protected void gallery(int id) {
        if (!PermissionUtil.askPermission(getActivity(), Permission.READ_EXTERNAL_STORAGE)) {
            Intent galleryIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, id);
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
        Bitmap imageBitmap;
        super.onActivityResult(requestCode, resultCode, data);
        this.callBackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1 && isStillRequired()) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap2 = (Bitmap) extras.get(ShareConstants.WEB_DIALOG_PARAM_DATA);
            if (imageBitmap2 != null) {
                this.mBmpProfile = ImageUtil.cropToSquare(imageBitmap2);
                updateView();
                saveBitmap(this.mBmpProfile);
                return;
            }
            return;
        }
        if (requestCode == RESULT_LOAD_IMG && resultCode == -1 && data != null && isStillRequired() && (imageBitmap = ImageUtil.getCroppedImageFromGallery(getActivity(), data.getData())) != null) {
            this.mBmpProfile = imageBitmap;
            updateView();
            saveBitmap(this.mBmpProfile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processFacebookData(JSONObject jsonObject, String userId) {
        JSONObject data;
        String birthday;
        String gender;
        String name;
        if (jsonObject != null) {
            try {
                if (jsonObject.has("name") && (name = jsonObject.getString("name")) != null) {
                    UserProfile.setName(name);
                }
                if (jsonObject.has("gender") && (gender = jsonObject.getString("gender")) != null) {
                    UserProfile.setGender(gender);
                }
                if (jsonObject.has("email")) {
                    jsonObject.getString("email");
                }
                if (jsonObject.has("birthday") && (birthday = jsonObject.getString("birthday")) != null) {
                    Log.i("FB", birthday);
                    if (birthday.length() == 10) {
                        try {
                            int month = Integer.parseInt(birthday.substring(0, 2));
                            int day = Integer.parseInt(birthday.substring(3, 5));
                            int year = Integer.parseInt(birthday.substring(6));
                            UserProfile.setDOB(year, month, day, true);
                        } catch (NumberFormatException nfe) {
                            Log.e("FB frag", "process FB dob " + birthday + ", " + nfe.getMessage());
                        }
                    } else if (birthday.length() == 4) {
                    }
                }
                if (jsonObject.has("picture")) {
                    try {
                        JSONObject jo = jsonObject.getJSONObject("picture");
                        if (jo != null && jo.has(ShareConstants.WEB_DIALOG_PARAM_DATA) && (data = jo.getJSONObject(ShareConstants.WEB_DIALOG_PARAM_DATA)) != null && (!data.has("is_silhouette") || !data.getBoolean("is_silhouette"))) {
                            new ImageDownloader().execute(String.format(FACEBOOK_IMAGE_URL, userId));
                        }
                    } catch (JSONException e) {
                    }
                }
                Sync.setProfile(Platforms.Peloton.getSharedKey(), UserProfile.createRider(), System.currentTimeMillis());
                updateView();
            } catch (JSONException ex) {
                Log.e("Profile", "Facebook login parse json error, " + ex.getMessage());
            }
        }
    }

    private void setFacebookLoginButtonCallback(final Context context) {
        this.loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        this.loginButton.registerCallback(this.callBackManager, new FacebookCallback<LoginResult>() { // from class: com.kopin.solos.FacebookBaseFragment.4
            @Override // com.facebook.FacebookCallback
            public void onSuccess(LoginResult loginResult) {
                if (FacebookBaseFragment.this.isStillRequired()) {
                    final String userId = loginResult.getAccessToken().getUserId();
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() { // from class: com.kopin.solos.FacebookBaseFragment.4.1
                        @Override // com.facebook.GraphRequest.GraphJSONObjectCallback
                        public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                            if (FacebookBaseFragment.this.isStillRequired()) {
                                Log.d("LoginActivity", response.toString());
                                FacebookBaseFragment.this.processFacebookData(jsonObject, userId);
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString(GraphRequest.FIELDS_PARAM, "id,name,email,gender,birthday,picture");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
            }

            @Override // com.facebook.FacebookCallback
            public void onCancel() {
                Log.i("FB", "FB cancel");
            }

            @Override // com.facebook.FacebookCallback
            public void onError(FacebookException exception) {
                if (FacebookBaseFragment.this.isStillRequired() && context != null) {
                    Log.e("FB", "facebook error " + exception.getMessage());
                    if (!Utility.isNetworkAvailable(context)) {
                        DialogUtils.showNoNetworkDialog(FacebookBaseFragment.this.getActivity(), R.string.no_internet_msg_facebook);
                    } else {
                        Toast.makeText(context, "Unable to login to Facebook", 0).show();
                    }
                }
            }
        });
    }

    protected void loadFacebookProfileImage() {
        this.mBmpProfile = FileUtil.readBitmapInternal(getActivity(), PROFILE_PICTURE);
    }

    protected void clearFacebookProfileImage() throws Throwable {
        this.mBmpProfile = null;
        saveBitmap(null);
    }

    protected void saveBitmap(Bitmap bitmap) throws Throwable {
        if (isStillRequired()) {
            FileUtil.saveBitmapInternal(getActivity(), bitmap, PROFILE_PICTURE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(ShareMenuAdapter.ShareMenuItem menu, ShareHelper.Status status) {
        menu.onProgress(Platforms.Facebook, new ShareHelper.ShareProgress(status, ""));
    }

    protected void facebookLoginToShare(final Bitmap bitmap, final String title, final JSONObject rideShare, final ShareMenuAdapter.ShareMenuItem menu, final long rideId) {
        if (bitmap == null) {
            update(menu, ShareHelper.Status.CANCELED);
        }
        if (!isLoggedInFacebook()) {
            LoginManager.getInstance().registerCallback(this.callBackManager, new FacebookCallback<LoginResult>() { // from class: com.kopin.solos.FacebookBaseFragment.5
                @Override // com.facebook.FacebookCallback
                public void onSuccess(LoginResult loginResult) {
                    loginResult.getAccessToken().getUserId();
                    GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() { // from class: com.kopin.solos.FacebookBaseFragment.5.1
                        @Override // com.facebook.GraphRequest.GraphJSONObjectCallback
                        public void onCompleted(JSONObject json, GraphResponse response) {
                            if (response.getError() == null) {
                                FacebookBaseFragment.this.facebookShareUploadMap(bitmap, title, rideShare, menu, rideId);
                            } else {
                                Log.e("RidePrev", "error logging in ");
                                FacebookBaseFragment.this.update(menu, ShareHelper.Status.CANCELED);
                            }
                        }
                    }).executeAsync();
                }

                @Override // com.facebook.FacebookCallback
                public void onCancel() {
                    Log.e("RidePreview", "facebook login canceled");
                    FacebookBaseFragment.this.update(menu, ShareHelper.Status.CANCELED);
                }

                @Override // com.facebook.FacebookCallback
                public void onError(FacebookException e) {
                    Log.e("RidePreview", "facebook login failed error");
                    FacebookBaseFragment.this.update(menu, ShareHelper.Status.CANCELED);
                }
            });
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList(FACEBOOK_PERMISSIONS));
        } else {
            facebookShareUploadMap(bitmap, title, rideShare, menu, rideId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void facebookShareUploadMap(Bitmap bitmap, String title, JSONObject rideShare, ShareMenuAdapter.ShareMenuItem menu, long rideId) {
        if (Profile.getCurrentProfile() != null) {
            FacebookSharingHelper.setFacebookId(Profile.getCurrentProfile().getId());
        }
        if (Platforms.Facebook.isShared(rideId)) {
            update(menu, ShareHelper.Status.ALREADY_SHARED);
        } else {
            FacebookSharingHelper.uploadMap(getActivity(), bitmap, title, rideShare, rideId, this);
        }
    }

    @Override // com.kopin.solos.share.facebook.FacebookSharingHelper.ISharingCallback
    public void shared(Platforms platforms) {
    }

    private class ImageDownloader extends AsyncTask<String, String, Bitmap> {
        private ImageDownloader() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... param) throws Throwable {
            Bitmap bitmap = downloadBitmap(param[0]);
            if (!isCancelled() && FacebookBaseFragment.this.isStillRequired()) {
                FacebookBaseFragment.this.saveBitmap(bitmap);
            }
            return bitmap;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap result) {
            if (FacebookBaseFragment.this.isStillRequired()) {
                FacebookBaseFragment.this.mBmpProfile = result;
                FacebookBaseFragment.this.updateView();
            }
        }

        private Bitmap downloadBitmap(String url) {
            try {
                URL urlImage = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlImage.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("ImageDownloader", "Error downloading image " + url + " " + e.getMessage());
                return null;
            }
        }
    }
}
