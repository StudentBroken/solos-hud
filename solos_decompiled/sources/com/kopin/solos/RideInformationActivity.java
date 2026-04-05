package com.kopin.solos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.util.RideActivity;
import com.kopin.solos.util.RunType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: loaded from: classes24.dex */
public class RideInformationActivity extends ThemeActivity {
    public static int INFO_ROUTES_REQUEST_CODE = 12;
    private ImageView imgStatusFacebook;
    private ImageView imgStatusMapMyRide;
    private ImageView imgStatusPeloton;
    private ImageView imgStatusStrava;
    private ImageView imgStatusTrainingPeaks;
    private ImageView imgStatusTwitter;
    TextView mActivityTextView;
    TextView mBikeTextView;
    TextView mDescriptionTextView;
    private CustomActionProvider.DefaultActionView mNavigationIcon;
    TextView mRideTitleTextView;
    private long mBikeId = -1;
    private int mActivityType = -1;
    Map<Platforms, ImageView> platformsImageViewMap = new HashMap();
    private final ShareHelper.UploadListener mUploadListener = new ShareHelper.UploadListener() { // from class: com.kopin.solos.RideInformationActivity.7
        @Override // com.kopin.solos.share.ShareHelper.UploadListener, com.kopin.solos.share.ShareHelper.ShareProgressListener
        public void onProgress(Platforms platform, ShareHelper.ShareProgress progress) {
            switch (AnonymousClass9.$SwitchMap$com$kopin$solos$share$ShareHelper$Status[progress.status.ordinal()]) {
                case 1:
                    ShareHelper.login(RideInformationActivity.this, platform);
                    RideInformationActivity.this.shareDone(platform, false);
                    break;
                case 2:
                case 3:
                    RideInformationActivity.this.shareDone(platform, true);
                    break;
                case 4:
                case 5:
                case 6:
                    RideInformationActivity.this.shareDone(platform, false);
                    break;
                case 7:
                case 8:
                case 9:
                    RideInformationActivity.this.sharing(platform);
                    break;
            }
            RideInformationActivity.this.shareRefresh();
        }
    };

    @Override // com.kopin.solos.ThemeActivity, com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_information_fragment);
        this.mRideTitleTextView = (TextView) findViewById(R.id.txtRideTitle);
        this.mBikeTextView = (TextView) findViewById(R.id.txtBike);
        this.mActivityTextView = (TextView) findViewById(R.id.txtSport);
        this.mDescriptionTextView = (TextView) findViewById(R.id.txtDescr);
        this.mRideTitleTextView.setText(this.mSavedRide.getTitle());
        String comment = this.mSavedRide.getComment();
        if (comment != null && !comment.isEmpty()) {
            this.mDescriptionTextView.setText(comment);
        } else {
            this.mDescriptionTextView.setVisibility(8);
        }
        if (this.mSavedRide instanceof SavedRide) {
            updateRideUI();
        } else if (this.mSavedRide instanceof SavedRun) {
            updateRunUI();
        }
        String distance = String.format(this.mSavedRide.getDistanceForLocale() < 10.0d ? "%.1f" : "%.0f", Double.valueOf(this.mSavedRide.getDistanceForLocale()));
        ((TextView) findViewById(R.id.txtDistance)).setText("" + distance + " " + Utility.getUserDefinedUnitShort(this, 1, false));
        ((TextView) findViewById(R.id.txtDuration)).setText(Utility.formatTime(this.mSavedRide.getDuration()));
        TextView txtAltitude = (TextView) findViewById(R.id.txtAltitude);
        String suffix = " " + Utility.getUserDefinedUnitShort(this, 1, true);
        if (this.mSavedRide.hasCorrectedElevation()) {
            txtAltitude.setText(this.mSavedRide.getGainedAltitude() > 0.0f ? String.format(Locale.US, "%.0f", Float.valueOf(this.mSavedRide.getGainedAltitudeForLocale())) + suffix : getString(R.string.caps_no_data));
        }
        if (this.mSavedRide.getWorkoutMode() == Workout.RideMode.TRAINING) {
            findViewById(R.id.textShare).setVisibility(4);
            findViewById(R.id.layoutShare).setVisibility(4);
        } else {
            this.imgStatusFacebook = (ImageView) findViewById(R.id.imgStatusFacebook);
            this.imgStatusTrainingPeaks = (ImageView) findViewById(R.id.imgStatusTrainingPeaks);
            this.imgStatusTwitter = (ImageView) findViewById(R.id.imgStatusTwitter);
            this.imgStatusStrava = (ImageView) findViewById(R.id.imgStatusStrava);
            this.imgStatusMapMyRide = (ImageView) findViewById(R.id.imgStatusMapMyRide);
            this.imgStatusPeloton = (ImageView) findViewById(R.id.imgStatusPeloton);
            this.platformsImageViewMap.put(Platforms.Facebook, this.imgStatusFacebook);
            this.platformsImageViewMap.put(Platforms.TrainingPeaks, this.imgStatusTrainingPeaks);
            this.platformsImageViewMap.put(Platforms.Twitter, this.imgStatusTwitter);
            this.platformsImageViewMap.put(Platforms.Strava, this.imgStatusStrava);
            this.platformsImageViewMap.put(Platforms.UnderArmour, this.imgStatusMapMyRide);
            setUpButtons();
        }
        this.mNavigationIcon = new CustomActionProvider.DefaultActionView(this);
        this.mNavigationIcon.setActiveColor(getResources().getColor(R.color.app_actionbar_divider));
        this.mNavigationIcon.setInactiveColor(getResources().getColor(R.color.unfocused_grey));
        getActionBar().setNavigationMode(0);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getStringFromTheme(R.attr.strWorkoutInfo));
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        RidePreview.shouldPop = true;
        shareRefresh();
    }

    private void updateRunUI() {
        findViewById(R.id.layoutBikeInput).setVisibility(8);
        findViewById(R.id.divider1).setVisibility(8);
        findViewById(R.id.divider2).setVisibility(8);
        this.mActivityType = this.mSavedRide.getActivity();
        if (this.mActivityType != -1) {
            this.mActivityTextView.setText(RunType.getLabel(this.mActivityType));
        }
        Conversion.getUnitOfPace(this);
        TextView textPace = (TextView) findViewById(R.id.txtSpeed);
        textPace.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pace, 0, 0, 0);
        textPace.setText(PaceUtil.formatPace(!Ride.hasData(this.mSavedRide.getAveragePace()) ? 0.0d : this.mSavedRide.getAveragePaceForLocale(), true, this));
    }

    private void updateRideUI() {
        Bike bike;
        this.mBikeId = this.mSavedRide.getBikeId();
        this.mBikeTextView.setText(R.string.bike_name_not_found);
        if (this.mBikeId != -1 && (bike = SQLHelper.getBike(this.mBikeId)) != null && bike.getName() != null) {
            this.mBikeTextView.setText(bike.getName());
        }
        this.mActivityType = this.mSavedRide.getActivity();
        if (this.mActivityType != -1) {
            this.mActivityTextView.setText(RideActivity.RideType.getLabel(this.mActivityType));
        }
        TextView textSpeed = (TextView) findViewById(R.id.txtSpeed);
        textSpeed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stride_small, 0, 0, 0);
        String unitOfSpeed = Conversion.getUnitOfSpeed(this);
        textSpeed.setText(!Ride.hasData(this.mSavedRide.getAverageSpeed()) ? "0 " + unitOfSpeed : String.format("%.1f", Double.valueOf(this.mSavedRide.getAverageSpeedForLocale())) + " " + unitOfSpeed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareRefresh() {
        for (Platforms platform : Platforms.values()) {
            if (platform.isShared(this.mSavedRide.getId())) {
                switch (platform) {
                    case Facebook:
                    case Twitter:
                        this.platformsImageViewMap.get(platform).setImageResource(android.R.color.transparent);
                        break;
                    default:
                        if (this.platformsImageViewMap.get(platform) != null) {
                            this.platformsImageViewMap.get(platform).setImageResource(R.drawable.tick_icon);
                        }
                        break;
                }
            }
        }
        invalidate(this.imgStatusStrava, this.imgStatusTrainingPeaks, this.imgStatusMapMyRide, this.imgStatusFacebook, this.imgStatusTwitter, this.imgStatusPeloton);
    }

    private void invalidate(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.postInvalidate();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDone(Platforms platform, boolean success) {
        switch (platform) {
            case Facebook:
            case Twitter:
                this.platformsImageViewMap.get(platform).setImageResource(android.R.color.transparent);
                break;
            default:
                this.platformsImageViewMap.get(platform).setImageResource(success ? R.drawable.tick_icon : R.drawable.failed_icon);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sharing(Platforms platform) {
        this.platformsImageViewMap.get(platform).setImageResource(R.drawable.uploading_icon);
    }

    private void setUpButtons() {
        findViewById(R.id.imgFacebook).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.Facebook);
            }
        });
        findViewById(R.id.imgTwitter).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.Twitter);
            }
        });
        findViewById(R.id.imgTrainingPeaks).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.TrainingPeaks);
            }
        });
        findViewById(R.id.imgStrava).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.Strava);
            }
        });
        findViewById(R.id.imgMapMyRide).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.UnderArmour);
            }
        });
        findViewById(R.id.imgPeloton).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.RideInformationActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RideInformationActivity.this.shareRide(Platforms.Peloton);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareRide(Platforms platform) {
        saveRide(platform);
        if (Utility.isNetworkAvailable(this)) {
            if (platform == Platforms.Facebook || platform == Platforms.Twitter) {
                showRideActivity(platform);
                sharing(platform);
                return;
            } else {
                if (!platform.isShared(this.mSavedRide.getId())) {
                    ShareHelper.upload(this, this.mSavedRide, null, platform);
                    sharing(platform);
                    return;
                }
                return;
            }
        }
        DialogUtils.showNoNetworkDialog(this);
    }

    private void saveRide(Platforms platform) {
        this.mSavedRide.setTitle(this.mRideTitleTextView.getText().toString());
        this.mSavedRide.setComment(this.mDescriptionTextView.getText().toString());
        if (this.mSavedRide instanceof SavedRide) {
            this.mSavedRide.setBikeId(this.mBikeId);
        }
        this.mSavedRide.setActivity(this.mActivityType);
        SavedRides.updateWorkout(this.mSavedRide);
    }

    /* JADX INFO: renamed from: com.kopin.solos.RideInformationActivity$9, reason: invalid class name */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$solos$share$ShareHelper$Status = new int[ShareHelper.Status.values().length];

        static {
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.AUTH_FAIL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.ALREADY_SHARED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.DONE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.NETWORK_ERROR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.CANCELED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.UNKNOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.PREPARING.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.UPLOADING.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$ShareHelper$Status[ShareHelper.Status.PROCESSING.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            $SwitchMap$com$kopin$solos$share$Platforms = new int[Platforms.values().length];
            try {
                $SwitchMap$com$kopin$solos$share$Platforms[Platforms.Facebook.ordinal()] = 1;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$solos$share$Platforms[Platforms.Twitter.ordinal()] = 2;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    private void showRideActivity(Platforms platform) {
        Intent intent = WorkoutSummaryActivity.intentToLaunch(this, this.mSavedRide, false, platform);
        startActivity(intent);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        ShareHelper.registerListener(this.mSavedRide.getId(), this.mUploadListener);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        ShareHelper.unregisterListener(this.mSavedRide.getId(), this.mUploadListener);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!ShareHelper.onLoginResult(this, requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.RideInformationActivity.8
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(final Platforms which, boolean success, String message) {
                if (success) {
                    RideInformationActivity.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.RideInformationActivity.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RideInformationActivity.this.shareRide(which);
                        }
                    });
                    return;
                }
                AlertDialog dialog = DialogUtils.createDialog(RideInformationActivity.this, "", "", RideInformationActivity.this.getString(android.R.string.ok), (Runnable) null);
                dialog.setTitle(R.string.share_please_login_title);
                dialog.setMessage(RideInformationActivity.this.getString(R.string.share_please_login_message, new Object[]{RideInformationActivity.this.getString(which.getNameId())}));
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean z = false;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ride_info, menu);
        MenuItem item = menu.findItem(R.id.menuAddRoute);
        if (Prefs.isWatchMode()) {
            item.setEnabled(false);
        } else {
            if (rideHasCoordinates() && !isAlreadySavedRoute()) {
                z = true;
            }
            item.setVisible(z);
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean z = false;
        MenuItem item = menu.findItem(R.id.menuAddRoute);
        if (Prefs.isWatchMode()) {
            item.setEnabled(false);
        } else {
            if (rideHasCoordinates() && !isAlreadySavedRoute()) {
                z = true;
            }
            item.setVisible(z);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // com.kopin.solos.common.BaseActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menuAddRoute /* 2131952549 */:
                showAddRouteFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean rideHasCoordinates() {
        return this.mSavedRide != null && SQLHelper.hasLocations(this.mSavedRide.getRouteId());
    }

    private boolean isAlreadySavedRoute() {
        return SQLHelper.isSavedRoute(this.mSavedRide.getRouteId());
    }

    private void showAddRouteFragment() {
        setResult(-1);
        finish();
    }

    public static void showRideInformation(Activity activity, SavedWorkout ride) {
        Intent intent = new Intent(activity, (Class<?>) RideInformationActivity.class);
        intent.putExtra("ride_id", ride.getId());
        intent.putExtra(ThemeActivity.EXTRA_WORKOUT_TYPE, ride.getSportType().name());
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        activity.startActivityForResult(intent, INFO_ROUTES_REQUEST_CODE);
    }
}
