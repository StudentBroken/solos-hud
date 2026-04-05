package com.kopin.solos.Fragments;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.facebook.Profile;
import com.google.android.gms.maps.GoogleMap;
import com.kopin.solos.FacebookBaseFragment;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideControl;
import com.kopin.solos.RideInformationActivity;
import com.kopin.solos.RoutePreview;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.CommonFileUtil;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.graphics.GraphBuilder;
import com.kopin.solos.menu.ShareMenuAdapter;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.TCXGenerator;
import com.kopin.solos.share.facebook.FacebookSharingHelper;
import com.kopin.solos.share.twitter.TwitterSharingHelper;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedRun;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.file.FileUtil;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.HeartRateZones;
import com.kopin.solos.storage.util.TrainingStressScore;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.sync.SyncBarHelper;
import com.kopin.solos.util.PaceUtil;
import com.kopin.solos.view.ExpandableCardView;
import com.kopin.solos.view.SimpleCardView;
import com.kopin.solos.view.graphics.GraphRenderer;
import com.kopin.solos.views.CustomMapView;
import com.kopin.solos.views.GraphView;
import com.kopin.solos.views.MetricsListView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.ua.sdk.datapoint.BaseDataTypes;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes24.dex */
public class RidePreviewFragment extends FacebookBaseFragment implements GoogleMap.SnapshotReadyCallback, FacebookSharingHelper.ISharingCallback {
    private static final String MAP_IMAGE_FILE = "solos_map.jpg";
    private static final String TAG = "RidePreviewFragment";
    private View listLapsLayout;
    private ListView listViewLaps;
    private Context mContext;
    private boolean mCorrectedElvRequested;
    private ExpandableCardView mElevationView;
    private ShareMenuAdapter.ShareMenuItem mFacebookItem;
    private ExpandableCardView mIntensityFactorView;
    private MetricsListView mLayout;
    private CustomMapView mMapView;
    private boolean mNewRide;
    private ExpandableCardView mNormalisedPowerView;
    private SavedWorkout mRide;
    private ShareMenuAdapter.ShareMenuItem mStravaItem;
    private ShareMenuAdapter.ShareMenuItem mTPItem;
    private ShareMenuAdapter.ShareMenuItem mTwitterItem;
    private ShareMenuAdapter.ShareMenuItem mUAItem;
    private boolean mWarningShown;
    private View mWarningView;
    private IRidePartSaved mWorkoutData;
    private SyncBarHelper syncBarHelper;
    private SimpleCardView tssView;
    private TextView txtLapChoice;
    private static final SimpleDateFormat COMPACT_DATE = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final MetricDataType[] RIDE_ROWS = {MetricDataType.ELEVATION, MetricDataType.SPEED, MetricDataType.CADENCE, MetricDataType.HEART_RATE, MetricDataType.POWER, MetricDataType.CALORIES};
    private static final MetricDataType[] RUN_ROWS = {MetricDataType.ELEVATION, MetricDataType.PACE, MetricDataType.STEP, MetricDataType.STRIDE, MetricDataType.HEART_RATE, MetricDataType.KICK, MetricDataType.CALORIES};
    private boolean mRideActive = false;
    private int lastLapId = 0;
    private int messageShared = 0;
    private JSONObject rideShare = new JSONObject();
    private Bitmap mBitmap = null;
    private boolean waitingForMap = false;
    private boolean waitingForMapTwitter = false;
    private final ShareHelper.UploadListener mUploadListener = new ShareHelper.UploadListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.8
        @Override // com.kopin.solos.share.ShareHelper.UploadListener, com.kopin.solos.share.ShareHelper.ShareProgressListener
        public void onProgress(Platforms which, ShareHelper.ShareProgress progress) {
            switch (progress.status) {
                case AUTH_FAIL:
                    ShareHelper.login(RidePreviewFragment.this, which);
                    break;
                case DONE:
                    if (RidePreviewFragment.this.syncBarHelper != null) {
                        RidePreviewFragment.this.syncBarHelper.sharedNotification(which, RidePreviewFragment.this.messageShared);
                    }
                    break;
            }
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long mThreadId = Looper.getMainLooper().getThread().getId();

    public static RidePreviewFragment newInstance(Bundle args) {
        RidePreviewFragment fragment = new RidePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBitmap = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mRide = WorkoutFragment.getWorkout(arguments);
            this.mRideActive = arguments.getBoolean(ThemeActivity.EXTRA_RIDE_ACTIVE, false);
            this.mNewRide = arguments.getBoolean(ThemeActivity.EXTRA_NEW_RIDE, false);
        }
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        this.mMapView = new CustomMapView(getActivity());
        this.mMapView.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.messageShared = R.string.sync_ride_shared;
        if (this.mRide != null && (this.mRide instanceof SavedRun)) {
            this.messageShared = R.string.sync_run_shared;
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mRide != null) {
            getActivity().getActionBar().setTitle(this.mNewRide ? ThemeUtil.getString(getActivity(), R.attr.strWorkout) : getString(R.string.ride_preview_back));
        }
        if (this.mMapView != null && isStillRequired()) {
            this.mMapView.onResume();
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        this.mMapView.onPause();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        ShareHelper.unregisterListener(this.mRide.getId(), this.mUploadListener);
        this.mLayout.recycle();
        this.syncBarHelper = null;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mMapView.onDestroy();
    }

    @Override // android.preference.PreferenceFragment, android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mMapView.onSaveInstanceState(outState);
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        this.mMapView.onLowMemory();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_preview, container, false);
        if (!isFinishing()) {
            this.mLayout = (MetricsListView) view.findViewById(R.id.cards_container);
            this.txtLapChoice = (TextView) view.findViewById(R.id.txtLaps);
            this.txtLapChoice.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    RidePreviewFragment.this.showLapList();
                }
            });
            this.listViewLaps = (ListView) view.findViewById(R.id.listLaps);
            this.listLapsLayout = view.findViewById(R.id.listLapsLayout);
            this.listLapsLayout.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    v.setVisibility(8);
                }
            });
            this.syncBarHelper = new SyncBarHelper(getActivity(), null, view.findViewById(R.id.syncBar), (TextView) view.findViewById(R.id.txtSyncOnline), (TextView) view.findViewById(R.id.txtSyncItems), (ImageView) view.findViewById(R.id.imgCloud), (ImageView) view.findViewById(R.id.imgCloudIcon), (ImageView) view.findViewById(R.id.imgShare));
            this.mWarningView = view.findViewById(R.id.txtWarning);
            int peakHR = this.mRide.getEffectivePeakHR();
            if (peakHR <= 0) {
                HeartRateZones.setFromAge(UserProfile.getAge());
            } else {
                HeartRateZones.setMaxHR(peakHR);
            }
            initLaps();
            populateLapsView(0);
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLapList() {
        this.listLapsLayout.setVisibility(this.listLapsLayout.getVisibility() == 0 ? 8 : 0);
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_share, menu);
        menu.findItem(R.id.menuShare).setVisible(false);
        menu.findItem(R.id.menuRideInfo).setVisible(this.mRide == null || !Platforms.Strava.wasImported(this.mRide));
        menu.findItem(R.id.action_dump_to_csv).setVisible(Config.DEBUG);
        menu.findItem(R.id.action_dump_to_tcx).setVisible(Config.DEBUG);
        Drawable drawable = menu.findItem(R.id.menuNav).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getActivity().getResources().getColor(R.color.unfocused_grey), PorterDuff.Mode.SRC_ATOP);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        boolean isWatchMode = Prefs.isWatchMode();
        MenuItem ghostItem = menu.findItem(R.id.menuGhostRider);
        MenuItem navItem = menu.findItem(R.id.menuNav);
        ghostItem.setEnabled(!isWatchMode);
        navItem.setEnabled(!isWatchMode);
        ghostItem.setVisible(!LiveRide.isActiveRide());
        navItem.setVisible(this.mRide.hasLocations() && !LiveRide.isActiveRide());
        super.onPrepareOptionsMenu(menu);
    }

    private boolean facebookLoginShareMap() {
        if (isLoggedInFacebook() && Profile.getCurrentProfile() != null) {
            FacebookSharingHelper.setFacebookId(Profile.getCurrentProfile().getId());
        }
        if (isLoggedInFacebook() && Platforms.Facebook.isShared(this.mRide.getId())) {
            return false;
        }
        if (this.mBitmap == null || this.mBitmap.getWidth() < 1) {
            this.waitingForMap = true;
            this.mLayout.forceMapLayout();
            return true;
        }
        facebookLoginToShare(this.mBitmap, this.mRide.getTitle(), this.rideShare, this.mFacebookItem, this.mRide.getId());
        return true;
    }

    private boolean twitterLoginShareMap() throws Throwable {
        if (TwitterSharingHelper.isShared(this.mRide.getId())) {
            return false;
        }
        if (this.mBitmap == null || this.mBitmap.getWidth() < 1) {
            this.waitingForMapTwitter = true;
            this.mLayout.forceMapLayout();
            return true;
        }
        FileUtil.saveBitmapExternal(getActivity(), this.mBitmap, MAP_IMAGE_FILE);
        twitterShare();
        return true;
    }

    private void twitterShare() {
        String s;
        String s2;
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity());
        File mapImage = CommonFileUtil.getExternalFile(getActivity(), MAP_IMAGE_FILE);
        if (mapImage != null && mapImage.exists()) {
            builder.image(FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", mapImage));
        }
        String caption = "Solos ";
        try {
            if (this.rideShare.has(BaseDataTypes.ID_DISTANCE) && (s2 = this.rideShare.getString(BaseDataTypes.ID_DISTANCE)) != null && !s2.isEmpty()) {
                caption = "Solos " + s2 + ", ";
            }
        } catch (JSONException e) {
        }
        try {
            if (this.rideShare.has("time") && (s = this.rideShare.getString("time")) != null && !s.isEmpty()) {
                caption = caption + s + ", ";
            }
        } catch (JSONException e2) {
        }
        if (caption.endsWith(", ")) {
            caption = caption.substring(0, caption.length() - 2);
        }
        builder.text(caption).show();
    }

    @Override // android.app.Fragment
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuGhostRider /* 2131952551 */:
                Intent intent = new Intent(this.mContext, (Class<?>) MainActivity.class);
                intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                RideControl.setWorkoutMode(Workout.RideMode.GHOST_RIDE, this.mRide.getId());
                intent.putExtra("GhostRideId", this.mRide.getId());
                startActivity(intent);
                return true;
            case R.id.menuNav /* 2131952552 */:
                Intent intent2 = RoutePreview.intentToLaunch(getActivity(), this.mRide);
                intent2.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                return true;
            case R.id.menuShare /* 2131952553 */:
            default:
                return super.onOptionsItemSelected(item);
            case R.id.menuRideInfo /* 2131952554 */:
                RideInformationActivity.showRideInformation(getActivity(), this.mRide);
                return true;
            case R.id.action_dump_to_csv /* 2131952555 */:
                File file = new File(CommonFileUtil.getExternalPath(getActivity()), "ride_" + this.mRide.getId() + ".txt");
                Log.d("action_dump_to_csv", file.getAbsolutePath());
                SavedRides.dumpToCSV(file.getAbsolutePath(), this.mRide);
                return true;
            case R.id.action_dump_to_tcx /* 2131952556 */:
                File file2 = new File(CommonFileUtil.getExternalPath(getActivity()), "ride_" + this.mRide.getId() + ".tcx");
                Log.d("action_dump_to_tcx", file2.getAbsolutePath());
                TCXGenerator.exportToFile(this.mRide, file2);
                return true;
        }
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity.getBaseContext();
    }

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws Throwable {
        if (!ShareHelper.onLoginResult(getActivity(), requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.3
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(final Platforms which, boolean success, String message) {
                if (success) {
                    RidePreviewFragment.this.runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RidePreviewFragment.this.shareRide(which, false);
                        }
                    });
                    return;
                }
                AlertDialog dialog = DialogUtils.createDialog(RidePreviewFragment.this.getActivity(), "", "", RidePreviewFragment.this.getString(android.R.string.ok), (Runnable) null);
                dialog.setTitle(R.string.share_please_login_title);
                dialog.setMessage(RidePreviewFragment.this.getString(R.string.share_please_login_message, new Object[]{RidePreviewFragment.this.getString(which.getNameId())}));
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initLaps() {
        if (!isFinishing()) {
            int numLaps = this.mRide.numLaps();
            if (numLaps < 2) {
                this.txtLapChoice.setVisibility(8);
                this.listLapsLayout.setVisibility(8);
                return;
            }
            this.txtLapChoice.setVisibility(0);
            List<String> laps = new ArrayList<>();
            laps.add(getString(R.string.laps_all));
            for (int i = 1; i <= numLaps; i++) {
                laps.add(getString(R.string.laps_single) + " " + i);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, laps);
            this.listViewLaps.setAdapter((ListAdapter) adapter);
            this.listViewLaps.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.4
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> parent, View view, int lapPosition, long id) {
                    RidePreviewFragment.this.populateLapsView(lapPosition);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void populateLapsView(int lapNum) {
        this.txtLapChoice.setText(lapNum < 1 ? getString(R.string.laps_all) : getString(R.string.laps_single) + " " + lapNum);
        this.listLapsLayout.setVisibility(8);
        if (lapNum == 0 || lapNum != this.lastLapId) {
            this.lastLapId = lapNum;
            boolean fullLap = lapNum > 0 && lapNum < this.mRide.numLaps();
            this.mWorkoutData = lapNum == 0 ? this.mRide : this.mRide.getLap(lapNum - 1);
            populateLapsView(this.mWorkoutData, fullLap);
        }
    }

    private void populateLapsView(IRidePartSaved data, boolean fullLap) {
        MetricDataType[] metricRows;
        enableTransitionType(this.mLayout);
        if (!isFinishing() && this.mRide != null && data != null) {
            this.mLayout.reset();
            Utility.getUserUnitLabel(getActivity(), 4);
            String unitOfDistance = Utility.getUserUnitLabel(getActivity(), 1);
            Utility.getUserUnitLabel(getActivity(), 0);
            ExpandableCardView mTimeDistance = this.mLayout.addCardFor(Record.MetricData.TIME, data);
            mTimeDistance.setDate(this.mRide.getActualStartTime());
            mTimeDistance.setCardName(Utility.formatTimeOnly(this.mRide.getActualStartTime()));
            mTimeDistance.setTopTitle(Utility.formatDate(this.mRide.getActualStartTime()));
            ViewGroup myView = (ViewGroup) this.mMapView.getParent();
            if (myView != null) {
                myView.removeView(this.mMapView);
            }
            mTimeDistance.setExpandableView(this.mMapView);
            mTimeDistance.setLoadingView(this.mMapView);
            this.mMapView.setRide(data);
            this.mMapView.setSnapshotReadyCallback(this);
            this.rideShare = new JSONObject();
            FacebookSharingHelper.addDistance(this.rideShare, data.getDistanceForLocale(), unitOfDistance);
            FacebookSharingHelper.addTime(this.rideShare, data.getDuration());
            if (Ride.hasData(data.getMaxPace())) {
                FacebookSharingHelper.addAveragePace(this.rideShare, PaceUtil.formatPace(data.getAveragePaceForLocale(), true, getActivity()));
            } else {
                String unitOfSpeed = Utility.getUserUnitLabel(getActivity(), 2);
                FacebookSharingHelper.addAverageSpeed(this.rideShare, String.format(Locale.US, "%.1f %s", Double.valueOf(data.getAverageSpeedForLocale()), unitOfSpeed));
            }
            int resActivityTypes = 0;
            int resDefaultActivityType = 0;
            switch (this.mRide.getSportType()) {
                case RIDE:
                    metricRows = RIDE_ROWS;
                    resActivityTypes = R.array.sport_types_icons;
                    resDefaultActivityType = R.drawable.road_practice;
                    break;
                case RUN:
                    metricRows = RUN_ROWS;
                    resActivityTypes = R.array.sport_types_icons_run;
                    resDefaultActivityType = R.drawable.road_practice_run;
                    break;
                default:
                    metricRows = new MetricDataType[0];
                    break;
            }
            if (Platforms.Strava.wasImported(this.mRide)) {
                mTimeDistance.setImage(R.drawable.strava_imported);
            } else {
                int rideType = this.mRide.getActivity();
                TypedArray rideTypeIcons = getResources().obtainTypedArray(resActivityTypes);
                mTimeDistance.setImage(rideTypeIcons.getResourceId(rideType, resDefaultActivityType));
                rideTypeIcons.recycle();
            }
            for (MetricDataType row : metricRows) {
                if (ConfigMetrics.isMetricEnabled(row)) {
                    populateRowView(row, data);
                }
            }
        }
    }

    private void updateElevationView(IRidePartSaved data) {
        if (this.mRide.hasCorrectedElevation()) {
            clearWarning();
            if (this.mElevationView != null && data != null) {
                this.mElevationView.setValue1(data.getGainedAltitude() > 0.0f ? String.format(Locale.US, "%.0f", Float.valueOf(data.getGainedAltitudeForLocale())) : getString(R.string.caps_no_data));
                this.mElevationView.setValue2(String.format(Locale.US, "%.0f", Double.valueOf(data.getMaxAltitudeDiffForLocale())));
                ExpandableCardView.LoadingView elevationView = this.mElevationView.getLoadingView();
                if (elevationView instanceof GraphView) {
                    if (Config.DEBUG) {
                        ((GraphView) elevationView).storeData(new GraphBuilder.GraphHelper(data, GraphBuilder.getValueProviderFor(Record.MetricData.ALTITUDE), GraphRenderer.GraphDataSet.GraphType.ELEVATION), new GraphBuilder.GraphHelper(data, GraphBuilder.getValueProviderFor(Record.MetricData.CORRECTED_ALTITUDE), GraphRenderer.GraphDataSet.GraphType.OXYGENATION));
                    } else {
                        ((GraphView) elevationView).storeData(new GraphBuilder.GraphHelper(data, GraphBuilder.getValueProviderFor(Record.MetricData.CORRECTED_ALTITUDE), GraphRenderer.GraphDataSet.GraphType.CORRECTED_ELEVATION));
                    }
                }
            }
        }
    }

    private void populateRowView(MetricDataType metric, IRidePartSaved data) {
        switch (metric) {
            case ELEVATION:
                if (Ride.hasData(data.getGainedAltitude()) && data.getDistance() != -2.147483648E9d && data.getDistance() > 0.0d && isStillRequired()) {
                    if (this.mRide.hasCorrectedElevation()) {
                        clearWarning();
                        this.mLayout.addCardFor(Record.MetricData.CORRECTED_ALTITUDE, data);
                    } else if (this.mRide.hasRoute()) {
                        showWarning(0, R.string.warning_wait_corrected_altitude);
                        this.mElevationView = this.mLayout.addCardFor(Record.MetricData.ALTITUDE, data);
                    }
                    break;
                }
                break;
            case SPEED:
                if (Ride.hasData(data.getMaxSpeed()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.SPEED, data);
                    break;
                }
                break;
            case PACE:
                if (Ride.hasData(data.getMaxPace()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.PACE, data);
                    break;
                }
                break;
            case OXYGEN:
                if (Ride.hasData(data.getAverageOxygen()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.OXYGEN, data);
                    break;
                }
                break;
            case POWER:
                if (Ride.hasData(data.getMaxPower()) && Ride.hasData(data.getAveragePower()) && data.getAveragePower() > 0.0d && data.getMaxPower() > 0.0d && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.POWER, data);
                    if ((data instanceof SavedRide) && ConfigMetrics.isMetricEnabled(MetricDataType.NORMALISED_POWER)) {
                        this.mNormalisedPowerView = this.mLayout.addCardFor(Record.MetricData.NORMALISED_POWER, data);
                        double ftp = ((SavedRide) this.mRide).getFunctionalThresholdPower();
                        if (ftp > 1.0d && Ride.hasData(data.getMaxPower()) && Ride.hasData(data.getAveragePower()) && data.getAveragePower() > 0.0d && data.getMaxPower() > 0.0d) {
                            if (ConfigMetrics.isMetricEnabled(MetricDataType.INTENSITY_FACTOR)) {
                                this.mIntensityFactorView = this.mLayout.addCardFor(Record.MetricData.INTENSITY_FACTOR, data);
                            }
                            if (ConfigMetrics.isMetricEnabled(MetricDataType.TRAINING_STRESS_SCORE)) {
                                this.tssView = this.mLayout.addCardFor(MetricDataType.TRAINING_STRESS_SCORE, data);
                            }
                        }
                        populateNormPowerAndIntensity(data.getAverageNormalisedPower(), data.getMaxNormalisedPower(), data.getAverageIntensity(), data.getMaxIntensity());
                        break;
                    }
                }
                break;
            case HEART_RATE:
                if (Ride.hasData(data.getMaxHeartrate()) && Ride.hasData(data.getAverageHeartrate()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.HEARTRATE, data);
                    break;
                }
                break;
            case CADENCE:
                if (Ride.hasData(data.getMaxCadence()) && Ride.hasData(data.getAverageCadence()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.CADENCE, data);
                    break;
                }
                break;
            case STEP:
                if (Ride.hasData(data.getMaxCadence()) && Ride.hasData(data.getAverageCadence()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.STEP, data);
                    break;
                }
                break;
            case STRIDE:
                if (Ride.hasData(data.getAverageStride()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.STRIDE, data);
                    break;
                }
                break;
            case KICK:
                if (Ride.hasData(data.getMaxPower()) && Ride.hasData(data.getAveragePower()) && isStillRequired()) {
                    this.mLayout.addCardFor(Record.MetricData.KICK, data);
                    if ((data instanceof SavedRun) && ConfigMetrics.isMetricEnabled(MetricDataType.NORMALISED_POWER)) {
                        this.mNormalisedPowerView = this.mLayout.addCardFor(Record.MetricData.NORMALISED_POWER, data);
                        double ftp2 = ((SavedRun) this.mRide).getFunctionalThresholdPower();
                        if (ftp2 > 1.0d && Ride.hasData(data.getMaxPower()) && Ride.hasData(data.getAveragePower()) && data.getAveragePower() > 0.0d && data.getMaxPower() > 0.0d) {
                            if (ConfigMetrics.isMetricEnabled(MetricDataType.INTENSITY_FACTOR)) {
                                this.mIntensityFactorView = this.mLayout.addCardFor(Record.MetricData.INTENSITY_FACTOR, data);
                            }
                            if (ConfigMetrics.isMetricEnabled(MetricDataType.TRAINING_STRESS_SCORE)) {
                                this.tssView = this.mLayout.addCardFor(MetricDataType.TRAINING_STRESS_SCORE, data);
                            }
                        }
                        populateNormPowerAndIntensity(data.getAverageNormalisedPower(), data.getMaxNormalisedPower(), data.getAverageIntensity(), data.getMaxIntensity());
                        break;
                    }
                }
                break;
            case CALORIES:
                if (Ride.hasData(data.getCalories()) && isStillRequired()) {
                    this.mLayout.addCardFor(MetricDataType.CALORIES, data);
                    break;
                }
                break;
        }
    }

    private void showWarning(int iconId, int warningId) {
        if (isStillRequired() && this.mWarningView != null) {
            ((TextView) this.mWarningView).setText(warningId);
            this.mWarningView.setVisibility(0);
            this.mWarningShown = true;
            this.mHandler.postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.5
                @Override // java.lang.Runnable
                public void run() {
                    RidePreviewFragment.this.clearWarning();
                }
            }, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearWarning() {
        if (isStillRequired() && this.mWarningView != null && this.mWarningShown) {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_slow);
            this.mWarningView.startAnimation(fadeInAnimation);
            this.mWarningView.setVisibility(8);
            this.mWarningShown = false;
        }
    }

    private void enableTransitionType(ViewGroup viewGroup) {
        LayoutTransition layoutTransition = viewGroup.getLayoutTransition();
        if (layoutTransition != null) {
            layoutTransition.enableTransitionType(4);
            layoutTransition.setDuration(1000L);
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableTransitionType((ViewGroup) child);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public void onCorrectedElevationResult() {
        updateElevationView(this.mWorkoutData);
    }

    public void shareRide(Platforms which, boolean inBackground) {
        if (this.mRide != null && getActivity() != null) {
            if (which == Platforms.Twitter) {
                if (this.mTwitterItem == null) {
                    this.mTwitterItem = new ShareMenuAdapter.ShareMenuItem(getString(R.string.share_to_twitter), R.drawable.twitter_logo_ic, Platforms.Twitter.getNameId()) { // from class: com.kopin.solos.Fragments.RidePreviewFragment.6
                        @Override // com.kopin.solos.menu.ShareMenuAdapter.ShareMenuItem
                        public boolean isShared() {
                            boolean shared = TwitterSharingHelper.isShared(RidePreviewFragment.this.mRide.getId());
                            if (shared) {
                                onProgress(Platforms.Twitter, new ShareHelper.ShareProgress(ShareHelper.Status.DONE, ""));
                                if (RidePreviewFragment.this.syncBarHelper != null) {
                                    RidePreviewFragment.this.syncBarHelper.sharedNotification(Platforms.Twitter, RidePreviewFragment.this.messageShared);
                                }
                            }
                            return shared;
                        }
                    };
                }
                if (!TwitterSharingHelper.isShared(this.mRide.getId())) {
                    this.mTwitterItem.onProgress(Platforms.Twitter, new ShareHelper.ShareProgress(twitterLoginShareMap() ? ShareHelper.Status.PROCESSING : ShareHelper.Status.ALREADY_SHARED, ""));
                    return;
                } else {
                    this.mTwitterItem.onProgress(Platforms.Twitter, new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, ""));
                    return;
                }
            }
            if (which == Platforms.Facebook) {
                if (this.mFacebookItem == null) {
                    this.mFacebookItem = new ShareMenuAdapter.ShareMenuItem(getString(R.string.share_to_facebook), R.drawable.ic_facebook_icon, Platforms.Facebook.getNameId()) { // from class: com.kopin.solos.Fragments.RidePreviewFragment.7
                        @Override // com.kopin.solos.menu.ShareMenuAdapter.ShareMenuItem
                        public boolean isShared() {
                            boolean shared = Platforms.Facebook.isShared(RidePreviewFragment.this.mRide.getId());
                            if (shared) {
                                onProgress(Platforms.Facebook, new ShareHelper.ShareProgress(ShareHelper.Status.DONE, ""));
                                if (RidePreviewFragment.this.syncBarHelper != null) {
                                    RidePreviewFragment.this.syncBarHelper.sharedNotification(Platforms.Facebook, RidePreviewFragment.this.messageShared);
                                }
                            }
                            return shared;
                        }
                    };
                }
                if (!Platforms.Facebook.isShared(this.mRide.getId())) {
                    this.mFacebookItem.onProgress(Platforms.Facebook, new ShareHelper.ShareProgress(facebookLoginShareMap() ? ShareHelper.Status.PROCESSING : ShareHelper.Status.ALREADY_SHARED, ""));
                    return;
                } else {
                    this.mFacebookItem.onProgress(Platforms.Facebook, new ShareHelper.ShareProgress(ShareHelper.Status.ALREADY_SHARED, ""));
                    return;
                }
            }
            if (!which.isLoggedIn(getActivity())) {
                if (!inBackground) {
                    ShareHelper.login(this, which);
                }
            } else {
                ShareHelper.UploadListener listener = inBackground ? null : this.mUploadListener;
                ShareHelper.upload(this.mContext, this.mRide, listener, which);
            }
        }
    }

    public static void showRideInfo(Context context, String name, String sport, String bike, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.ride_info_dialog_title);
        FrameLayout frameView = new FrameLayout(context);
        builder.setView(frameView);
        builder.setPositiveButton(R.string.dialog_button_dismiss, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.RidePreviewFragment.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        LayoutInflater inflater = alertDialog.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_ride_info, frameView);
        ((TextView) dialoglayout.findViewById(R.id.txtRideName)).setText(name);
        ((TextView) dialoglayout.findViewById(R.id.txtRideSport)).setText(sport);
        ((TextView) dialoglayout.findViewById(R.id.txtRideBike)).setText(bike);
        if (description != null && !description.isEmpty()) {
            ((TextView) dialoglayout.findViewById(R.id.txtRideDescription)).setText(description);
        } else {
            dialoglayout.findViewById(R.id.txtRideDescription).setVisibility(8);
            dialoglayout.findViewById(R.id.txtRideDescriptionTitle).setVisibility(8);
        }
        alertDialog.show();
        DialogUtils.setDialogTitleDivider(alertDialog);
    }

    @Override // com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
    public void onSnapshotReady(Bitmap bitmap) throws Throwable {
        if (isStillRequired() && bitmap != null && bitmap.getWidth() > 1) {
            this.mBitmap = bitmap;
            FileUtil.saveBitmapExternal(getActivity(), bitmap, MAP_IMAGE_FILE);
            if (this.waitingForMap) {
                this.waitingForMap = false;
                facebookLoginToShare(this.mBitmap, this.mRide.getTitle(), this.rideShare, this.mFacebookItem, this.mRide.getId());
                return;
            } else if (this.waitingForMapTwitter) {
                this.waitingForMapTwitter = false;
                twitterShare();
                return;
            }
        }
        if (this.waitingForMap) {
        }
        this.waitingForMap = false;
    }

    private void populateNormPowerAndIntensity(double avgNormPower, double maxNormPower, double avgIntensity, double maxIntensity) {
        if (isStillRequired()) {
            if (this.mNormalisedPowerView != null) {
                this.mNormalisedPowerView.setValue1(avgNormPower > 0.0d ? String.format("%.0f", Double.valueOf(avgNormPower)) : getString(R.string.caps_no_data));
                this.mNormalisedPowerView.setValue2(maxNormPower > 0.0d ? String.format("%.0f", Double.valueOf(maxNormPower)) : getString(R.string.caps_no_data));
                ExpandableCardView.LoadingView graphView = this.mNormalisedPowerView.getLoadingView();
                if (graphView instanceof GraphView) {
                    ((GraphView) graphView).addAverage(GraphRenderer.GraphDataSet.GraphType.METRIC, avgNormPower, getString(R.string.graph_average_text), getResources().getColor(R.color.average_line_color_metric), true);
                }
            }
            if (this.mIntensityFactorView != null) {
                this.mIntensityFactorView.setValue1(avgIntensity > 0.0d ? String.format("%.2f", Double.valueOf(avgIntensity)) : getString(R.string.caps_no_data));
                this.mIntensityFactorView.setValue2(maxIntensity > 0.0d ? String.format("%.2f", Double.valueOf(maxIntensity)) : getString(R.string.caps_no_data));
            }
            if (this.mRide instanceof SavedRide) {
                SavedRide ride = (SavedRide) this.mRide;
                if (this.tssView != null && ride.getFunctionalThresholdPower() > 1.0d && avgNormPower > 0.0d && avgIntensity != -2.147483648E9d) {
                    double tss = TrainingStressScore.calculate(this.mRide.getDuration(), avgNormPower, avgIntensity, ride.getFunctionalThresholdPower());
                    if (tss > TrainingStressScore.TSS_MIN && tss < TrainingStressScore.TSS_MAX) {
                        this.tssView.setValue(String.format(Locale.US, "%.1f", Double.valueOf(tss)));
                    } else {
                        this.tssView.setValue(R.string.caps_no_data);
                    }
                }
            }
        }
    }

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.share.facebook.FacebookSharingHelper.ISharingCallback
    public void shared(Platforms platform) {
        if (this.syncBarHelper != null) {
            this.syncBarHelper.sharedNotification(platform, this.messageShared);
        }
    }
}
