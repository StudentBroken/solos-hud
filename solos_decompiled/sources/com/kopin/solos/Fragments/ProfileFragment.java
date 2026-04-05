package com.kopin.solos.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.pupil.SolosDevice;
import com.kopin.solos.FacebookBaseFragment;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.StartActivity;
import com.kopin.solos.common.SportType;
import com.kopin.solos.common.config.MetricDataType;
import com.kopin.solos.sensors.SensorsConnector;
import com.kopin.solos.share.Config;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.Sync;
import com.kopin.solos.share.TrainingCache;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Bikes;
import com.kopin.solos.storage.FTP;
import com.kopin.solos.storage.FTPHelper;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.OverallStats;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.file.FileUtil;
import com.kopin.solos.storage.settings.ConfigMetrics;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.settings.UserProfile;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.sync.SyncBarHelper;
import com.kopin.solos.view.BarView;
import com.kopin.solos.view.RoundedImageView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class ProfileFragment extends FacebookBaseFragment implements SyncBarHelper.UISyncRefresh {
    private static final int ANIM_DELAY = 55;
    private static final int DAY = 86400000;
    private static final String TAG = "ProfileFragment";
    private View barBike;
    private View barFtp;
    private View barHrz;
    private RoundedImageView imgProfileUser;
    private ImageView imgProfileUserDefault;
    private View layoutTSS;
    private String mName;
    private View mView;
    private View progressBar;
    private View spacerTSS;
    private SyncBarHelper syncBarHelper;
    private int today;
    private TextView txtDetailBike;
    private TextView txtDetailFtp;
    private TextView txtDetailHrz;
    private TextView txtDetailStats;
    private TextView txtEmailName;
    private TextView txtProfileDistance;
    private TextView txtProfileElevation;
    private TextView txtProfileName;
    private TextView txtProfileTSS;
    private TextView txtProfileTime;
    private BarView[] barViews = new BarView[7];
    private TextView[] txtBarDays = new TextView[7];
    private int mAge = -1;
    private List<Double> mDistances = new ArrayList();
    private boolean mConnected = false;
    private boolean loggingOut = false;
    private boolean working = false;
    private Handler handler = new Handler();
    private int animStep = 0;
    private double longerBar = 0.0d;

    @Override // com.kopin.solos.FacebookBaseFragment, com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mView = inflater.inflate(R.layout.fragment_profile, container, false);
        this.imgProfileUser = (RoundedImageView) this.mView.findViewById(R.id.imgProfileUser);
        this.imgProfileUserDefault = (ImageView) this.mView.findViewById(R.id.imgProfileUserDefault);
        this.txtProfileName = (TextView) this.mView.findViewById(R.id.txtProfileName);
        this.txtEmailName = (TextView) this.mView.findViewById(R.id.txtEmailName);
        this.syncBarHelper = new SyncBarHelper(getActivity(), this, this.mView.findViewById(R.id.syncBar), (TextView) this.mView.findViewById(R.id.txtSyncOnline), (TextView) this.mView.findViewById(R.id.txtSyncItems), (ImageView) this.mView.findViewById(R.id.imgCloud), (ImageView) this.mView.findViewById(R.id.imgCloudIcon), (ImageView) this.mView.findViewById(R.id.imgShare));
        this.barViews[0] = (BarView) this.mView.findViewById(R.id.bar1);
        this.barViews[1] = (BarView) this.mView.findViewById(R.id.bar2);
        this.barViews[2] = (BarView) this.mView.findViewById(R.id.bar3);
        this.barViews[3] = (BarView) this.mView.findViewById(R.id.bar4);
        this.barViews[4] = (BarView) this.mView.findViewById(R.id.bar5);
        this.barViews[5] = (BarView) this.mView.findViewById(R.id.bar6);
        this.barViews[6] = (BarView) this.mView.findViewById(R.id.bar7);
        this.txtBarDays[0] = (TextView) this.mView.findViewById(R.id.mon);
        this.txtBarDays[1] = (TextView) this.mView.findViewById(R.id.tue);
        this.txtBarDays[2] = (TextView) this.mView.findViewById(R.id.wed);
        this.txtBarDays[3] = (TextView) this.mView.findViewById(R.id.thu);
        this.txtBarDays[4] = (TextView) this.mView.findViewById(R.id.fri);
        this.txtBarDays[5] = (TextView) this.mView.findViewById(R.id.sat);
        this.txtBarDays[6] = (TextView) this.mView.findViewById(R.id.sun);
        this.txtProfileDistance = (TextView) this.mView.findViewById(R.id.txtProfileDistance);
        this.txtProfileElevation = (TextView) this.mView.findViewById(R.id.txtProfileElevation);
        this.txtProfileTime = (TextView) this.mView.findViewById(R.id.txtProfileTime);
        this.txtProfileTSS = (TextView) this.mView.findViewById(R.id.txtProfileTSS);
        this.layoutTSS = this.mView.findViewById(R.id.layoutTSS);
        this.spacerTSS = this.mView.findViewById(R.id.spacerTSS);
        this.mView.findViewById(R.id.barStats).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileFragment.this.barStatsClick(v);
            }
        });
        this.barBike = this.mView.findViewById(R.id.barBike);
        this.barBike.findViewById(R.id.barBike).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileFragment.this.barBikeClick(v);
            }
        });
        this.barFtp = this.mView.findViewById(R.id.barFtp);
        this.barFtp.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileFragment.this.barFtpClick(v);
            }
        });
        this.barHrz = this.mView.findViewById(R.id.barHrz);
        this.barHrz.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ProfileFragment.this.barHrzClick(v);
            }
        });
        this.txtDetailStats = (TextView) this.mView.findViewById(R.id.txtProfileDetailStats);
        this.txtDetailBike = (TextView) this.mView.findViewById(R.id.txtProfileDetailBikes);
        this.txtDetailFtp = (TextView) this.mView.findViewById(R.id.txtProfileDetailFtp);
        this.txtDetailHrz = (TextView) this.mView.findViewById(R.id.txtProfileDetailHrz);
        loadFacebookProfileImage();
        updateRideViews();
        updateView();
        getActivity().getActionBar().setNavigationMode(1);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        View btnTest = this.mView.findViewById(R.id.btnTest);
        btnTest.setVisibility(Config.DEBUG ? 0 : 8);
        btnTest.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!ProfileFragment.this.loggingOut) {
                    Sync.test();
                }
            }
        });
        View btnSync = this.mView.findViewById(R.id.btnSync);
        btnSync.setVisibility(Config.DEBUG ? 0 : 8);
        btnSync.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (!ProfileFragment.this.loggingOut) {
                    Sync.sync(true);
                }
            }
        });
        this.progressBar = this.mView.findViewById(R.id.progressBar);
        return this.mView;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        setRideInfo();
        setStatsInfo();
        setUIListeners();
        Sync.reset();
        if (this.syncBarHelper != null) {
            this.syncBarHelper.updateConnectivity(Utility.isNetworkAvailable(getActivity()));
        }
        if (Config.SYNC_PROVIDER != Platforms.None && Utility.isNetworkAvailable(getActivity())) {
            Sync.sync();
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (LiveRide.isActiveFtp()) {
            ((MainActivity) getActivity()).showFunctionalThresholdPowerSettings(true);
        } else {
            updateView();
        }
        this.barBike.setVisibility(LiveRide.getCurrentSport().hasFeature(SportType.Feature.BIKE) ? 0 : 8);
        this.barFtp.setVisibility((LiveRide.getCurrentSport().hasFeature(SportType.Feature.FTP) && ConfigMetrics.isMetricEnabled(MetricDataType.FUNCTIONAL_THRESHOLD_POWER)) ? 0 : 8);
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        Sync.setProfileUIListener(null);
        Sync.setSyncUpdateListener(null);
    }

    private void setUIListeners() {
        Sync.setProfileUIListener(new Sync.UIListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.7
            @Override // com.kopin.solos.share.Sync.UIListener
            public void onStart() {
                if (!ProfileFragment.this.isFinishing()) {
                    ProfileFragment.this.progressBar.setVisibility(0);
                }
            }

            @Override // com.kopin.solos.share.Sync.UIListener
            public void onComplete() {
                if (!ProfileFragment.this.isFinishing()) {
                    Log.i(ProfileFragment.TAG, "Synced profile view");
                    ProfileFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.ProfileFragment.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ProfileFragment.this.updateRideViews();
                            ProfileFragment.this.updateView();
                            ProfileFragment.this.progressBar.setVisibility(4);
                        }
                    });
                }
            }

            @Override // com.kopin.solos.share.Sync.ConnectionListener
            public void onConnectionChange(final boolean connected) {
                if (!ProfileFragment.this.isFinishing()) {
                    Log.i(ProfileFragment.TAG, "profile has connection " + connected);
                    ProfileFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.ProfileFragment.7.2
                        @Override // java.lang.Runnable
                        public void run() {
                            ProfileFragment.this.syncBarHelper.updateConnectivity(connected);
                        }
                    });
                }
            }
        });
        Sync.setSyncUpdateListener(this.syncBarHelper);
    }

    @Override // android.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(Config.SYNC_PROVIDER != Platforms.None);
        boolean isWatchMode = Prefs.isWatchMode();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setEnabled((isWatchMode || LiveRide.isActiveRide()) ? false : true);
        }
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [com.kopin.solos.Fragments.ProfileFragment$9] */
    @Override // com.kopin.solos.common.BaseFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean z = false;
        if (this.loggingOut || super.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_edit_profile /* 2131952558 */:
                ((MainActivity) getActivity()).showEditProfileFragment();
                return true;
            case R.id.action_logout /* 2131952559 */:
                if (!isFinishing() && !this.working) {
                    this.working = true;
                    new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.ProfileFragment.8
                        @Override // java.lang.Runnable
                        public void run() {
                            if (!ProfileFragment.this.isFinishing() && ProfileFragment.this.working) {
                                ProfileFragment.this.progressBar.setVisibility(0);
                            }
                        }
                    }, 1000L);
                    new Sync.UnSyncedTask(z) { // from class: com.kopin.solos.Fragments.ProfileFragment.9
                        @Override // com.kopin.solos.share.Sync.UnSyncedTask, android.os.AsyncTask
                        public void onPostExecute(Void v) {
                            if (!ProfileFragment.this.isFinishing()) {
                                ProfileFragment.this.working = false;
                                ProfileFragment.this.progressBar.setVisibility(4);
                                ProfileFragment.this.logoutCheck();
                            }
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logoutCheck() {
        if (!isFinishing()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.ProfileFragment.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case -1:
                            ProfileFragment.this.logout();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(Sync.getNumUnsyncedItems(false) > 0 ? R.string.dialog_logout_message_unsync : R.string.dialog_logout_message).setNegativeButton(R.string.dialog_logout_btn_cancel, dialogClickListener).setPositiveButton(R.string.dialog_logout_btn_logout, dialogClickListener).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logout() {
        if (!isFinishing()) {
            this.loggingOut = true;
            ((MainActivity) getActivity()).setLogoutMode(true);
            Sync.cancelTasks();
            this.progressBar.setVisibility(0);
            FTPHelper.init();
            clearFacebookProfileImage();
            if (!isFinishing()) {
                ShareHelper.logoutAll(getActivity().getApplicationContext());
            }
            Sync.clear();
            Prefs.clear();
            SQLHelper.setCurrentUsername(null);
            SolosDevice.forgetHeadset();
            SensorsConnector.reset();
            LiveRide.clear();
            TrainingCache.clearCache();
            new Handler().postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.ProfileFragment.11
                @Override // java.lang.Runnable
                public void run() {
                    ProfileFragment.this.restart();
                }
            }, 750L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restart() {
        this.loggingOut = false;
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setLogoutMode(false);
            startActivity(StartActivity.getRestartIntent(getActivity()));
            getActivity().finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRideViews() {
        if (isStillRequired()) {
            highlightToday();
            getDailyDistances();
            double longestDailyDistance = 0.1d;
            for (Double distance : this.mDistances) {
                if (distance.doubleValue() > longestDailyDistance) {
                    longestDailyDistance = distance.doubleValue();
                }
            }
            this.longerBar = longestDailyDistance;
            for (BarView barView : this.barViews) {
                barView.setColorForegroundResource(R.color.profile_bar_foreground);
                barView.setColorBackgroundResource(R.color.profile_bar_background);
                barView.setData(0, (int) longestDailyDistance);
            }
            startAnimation();
        }
    }

    private void startAnimation() {
        this.animStep = 0;
        animateBars(130);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateBars(int delay) {
        int i = this.animStep + 1;
        this.animStep = i;
        if (i <= 10) {
            this.handler.postDelayed(new Runnable() { // from class: com.kopin.solos.Fragments.ProfileFragment.12
                @Override // java.lang.Runnable
                public void run() {
                    if (ProfileFragment.this.isStillRequired()) {
                        int i2 = 0;
                        for (BarView barView : ProfileFragment.this.barViews) {
                            int j = (i2 - ProfileFragment.this.today) - 1;
                            if (j < 0) {
                                j += 7;
                            } else if (j > 6) {
                                j -= 7;
                            }
                            barView.setData(((Double) ProfileFragment.this.mDistances.get(j)).doubleValue() > 5.0d ? (int) (((Double) ProfileFragment.this.mDistances.get(j)).doubleValue() * 0.1d * ((double) ProfileFragment.this.animStep)) : ((Double) ProfileFragment.this.mDistances.get(j)).intValue(), (int) ProfileFragment.this.longerBar);
                            i2++;
                        }
                        if (ProfileFragment.this.animStep < 10) {
                            ProfileFragment.this.animateBars(ProfileFragment.ANIM_DELAY);
                        }
                    }
                }
            }, delay);
        }
    }

    private void highlightToday() {
        int day = Calendar.getInstance().get(7) - 2;
        if (day < 0) {
            day += 7;
        }
        this.today = day;
        int i = 0;
        for (TextView textView : this.txtBarDays) {
            textView.setTypeface(textView.getTypeface(), this.today == i ? 1 : 0);
            textView.setTextColor(getResources().getColor(this.today == i ? R.color.profile_day_text_highlight : R.color.profile_day_text));
            i++;
        }
    }

    private void getDailyDistances() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        long timePoint = cal.getTimeInMillis();
        List<Long> periods = new ArrayList<>();
        periods.add(Long.valueOf(timePoint - 518400000));
        periods.add(Long.valueOf(timePoint - 432000000));
        periods.add(Long.valueOf(timePoint - 345600000));
        periods.add(Long.valueOf(timePoint - 259200000));
        periods.add(Long.valueOf(timePoint - 172800000));
        periods.add(Long.valueOf(timePoint - 86400000));
        periods.add(Long.valueOf(timePoint));
        periods.add(Long.valueOf(timePoint + 86400000));
        this.mDistances.clear();
        this.mDistances = SQLHelper.getDistancesForPeriods(periods, "Ride", Platforms.Peloton.getSharedKey(), LiveRide.getCurrentSport());
    }

    @Override // com.kopin.solos.FacebookBaseFragment
    public void updateView() {
        if (isStillRequired()) {
            this.mAge = UserProfile.getAge();
            this.mName = UserProfile.getName();
            this.txtProfileName.setText(this.mName + ", " + this.mAge);
            this.txtEmailName.setVisibility(8);
            String email = PelotonPrefs.getEmail();
            if (Config.SYNC_PROVIDER != Platforms.None && email != null && !email.isEmpty()) {
                this.txtEmailName.setText(email);
                this.txtEmailName.setVisibility(0);
            }
            if (this.mBmpProfile == null) {
                this.imgProfileUser.setVisibility(8);
                this.imgProfileUserDefault.setVisibility(0);
            } else {
                this.imgProfileUserDefault.setVisibility(8);
                this.imgProfileUser.setImageBitmap(this.mBmpProfile);
                this.imgProfileUser.setVisibility(0);
            }
            boolean mRideActive = LiveRide.isActiveRide();
            boolean enable = (Prefs.isWatchMode() || mRideActive) ? false : true;
            this.barBike.setEnabled(enable);
            this.barBike.setAlpha(enable ? 1.0f : 0.3f);
            this.mView.findViewById(R.id.txtProfileTitleBikes).setEnabled(!mRideActive);
            this.mView.findViewById(R.id.txtProfileDetailBikes).setEnabled(!mRideActive);
            this.barFtp.setEnabled(enable);
            this.barFtp.setAlpha(enable ? 1.0f : 0.3f);
            this.mView.findViewById(R.id.txtProfileTitleFtp).setEnabled(!mRideActive);
            this.mView.findViewById(R.id.txtProfileDetailFtp).setEnabled(!mRideActive);
            this.barHrz.setEnabled(enable);
            this.barHrz.setAlpha(enable ? 1.0f : 0.3f);
            this.mView.findViewById(R.id.txtProfileTitleHrz).setEnabled(!mRideActive);
            this.mView.findViewById(R.id.txtProfileDetailHrz).setEnabled(mRideActive ? false : true);
            setRideInfo();
            setStatsInfo();
            setBikesInfo();
            setRideFtp();
            if (Config.DEBUG) {
                FileUtil.saveHashFile(getActivity());
            }
            this.mView.findViewById(R.id.textDev).setVisibility(Config.CLOUD_LIVE ? 4 : 0);
            boolean distanceOn = ConfigMetrics.isMetricEnabled(MetricDataType.DISTANCE);
            this.mView.findViewById(R.id.layoutDistance).setVisibility(distanceOn ? 0 : 8);
            this.mView.findViewById(R.id.viewSpaceDistance).setVisibility(distanceOn ? 0 : 8);
            boolean overallClimbOn = ConfigMetrics.isMetricEnabled(MetricDataType.OVERALL_CLIMB);
            this.mView.findViewById(R.id.layoutElevation).setVisibility(overallClimbOn ? 0 : 8);
            this.mView.findViewById(R.id.viewSpaceElevation).setVisibility(overallClimbOn ? 0 : 8);
            this.barFtp.setVisibility((LiveRide.getCurrentSport().hasFeature(SportType.Feature.FTP) && ConfigMetrics.isMetricEnabled(MetricDataType.FUNCTIONAL_THRESHOLD_POWER)) ? 0 : 8);
        }
    }

    private void setRideInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -7);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        parseRides(calendar.getTimeInMillis(), false, false);
    }

    private void parseRides(long startTs, boolean annualDistance, boolean includeTSS) {
        if (includeTSS) {
            new StatsTask(startTs, annualDistance, this.txtDetailStats, this.txtProfileDistance, this.txtProfileElevation, this.txtProfileTime, this.txtProfileTSS, this.layoutTSS, this.spacerTSS).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            new StatsTask(this, startTs, annualDistance, this.txtDetailStats, this.txtProfileDistance, this.txtProfileElevation, this.txtProfileTime).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    protected class StatsTask extends AsyncTask<Void, Void, Void> {
        View layoutTSS;
        boolean mAnnualDistance;
        private long mDuration;
        private float mElevation;
        long mStartTs;
        private double mTotalDistance;
        View spacerTSS;
        double tss;
        TextView txtDetailStats;
        TextView txtProfileDistance;
        TextView txtProfileElevation;
        TextView txtProfileTSS;
        TextView txtProfileTime;

        StatsTask(ProfileFragment this$0, long startTs, boolean annualDistance, TextView txtDetailStats, TextView txtProfileDistance, TextView txtProfileElevation, TextView txtProfileTime) {
            this(startTs, annualDistance, txtDetailStats, txtProfileDistance, txtProfileElevation, txtProfileTime, null, null, null);
        }

        StatsTask(long startTs, boolean annualDistance, TextView txtDetailStats, TextView txtProfileDistance, TextView txtProfileElevation, TextView txtProfileTime, TextView txtProfileTSS, View layoutTSS, View spacerTSS) {
            this.mTotalDistance = 0.0d;
            this.mDuration = 0L;
            this.mElevation = 0.0f;
            this.tss = -2.147483648E9d;
            this.mStartTs = startTs;
            this.mAnnualDistance = annualDistance;
            this.txtDetailStats = txtDetailStats;
            this.txtProfileDistance = txtProfileDistance;
            this.txtProfileElevation = txtProfileElevation;
            this.txtProfileTime = txtProfileTime;
            this.txtProfileTSS = txtProfileTSS;
            this.layoutTSS = layoutTSS;
            this.spacerTSS = spacerTSS;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            OverallStats stats = SavedRides.getOverallStatsForCurrentSport(this.mStartTs, Config.SYNC_PROVIDER.getSharedKey(), this.txtProfileTSS != null);
            this.mTotalDistance = stats.hasDistance() ? stats.getDistanceForLocale() : 0.0d;
            this.mDuration = stats.hasDuration() ? stats.getDuration() : 0L;
            this.mElevation = stats.hasOverallClimb() ? stats.getOverallClimbForLocale() : 0.0f;
            this.tss = stats.getTSS();
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void v) {
            if (ProfileFragment.this.isStillRequired()) {
                String distance = String.format(Locale.US, this.mTotalDistance < 10.0d ? "%.1f" : "%.0f", Double.valueOf(this.mTotalDistance));
                if (this.mAnnualDistance) {
                    this.txtDetailStats.setText(ProfileFragment.this.getString(R.string.profile_stats_year_title) + " " + distance + " " + Utility.getUserDefinedUnitShort(ProfileFragment.this.getActivity(), 1, false));
                } else {
                    this.txtProfileDistance.setText(distance + " " + Utility.getUserDefinedUnitShort(ProfileFragment.this.getActivity(), 1, false));
                    this.txtProfileElevation.setText(String.format(Locale.US, this.mElevation < 10.0f ? "%.1f" : "%.0f", Float.valueOf(this.mElevation)) + " " + Utility.getUserDefinedUnitShort(ProfileFragment.this.getActivity(), 1, true));
                    this.txtProfileTime.setText(Utility.formatTime(this.mDuration));
                }
                if (this.txtProfileTSS != null) {
                    boolean tssOn = this.tss != -2.147483648E9d && ConfigMetrics.isMetricEnabled(MetricDataType.TRAINING_STRESS_SCORE);
                    this.layoutTSS.setVisibility(tssOn ? 0 : 8);
                    this.spacerTSS.setVisibility(tssOn ? 0 : 8);
                    this.txtProfileTSS.setText(String.format(Locale.US, "%.1f", Double.valueOf(this.tss)));
                }
            }
        }
    }

    private void setStatsInfo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(5, -365);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        parseRides(calendar.getTimeInMillis(), true, false);
    }

    private void setBikesInfo() {
        long id = Prefs.getChosenBikeId();
        Bike chosenBike = SQLHelper.getBike(id);
        if (chosenBike != null) {
            this.txtDetailBike.setText(chosenBike.getName());
            return;
        }
        List<Bike> bikes = Bikes.getActiveBikes();
        if (!bikes.isEmpty()) {
            this.txtDetailBike.setText(bikes.get(0).getName());
        }
    }

    private void setRideFtp() {
        String string;
        String str;
        FTP ftp = FTPHelper.getFTP();
        TextView textView = this.txtDetailFtp;
        if (ftp != null && ftp.mValue > getResources().getInteger(R.integer.ftp_min)) {
            string = String.format(getString(R.string.profile_detail_ftp_result_text), Integer.valueOf((int) ftp.mValue));
        } else {
            string = getString(R.string.profile_detail_ftp_default_text);
        }
        textView.setText(string);
        FTP phr = FTPHelper.getPeakHR();
        int effective = 220 - UserProfile.getAge();
        TextView textView2 = this.txtDetailHrz;
        if (phr != null && phr.mValue > 0.0d) {
            str = String.format(getString(R.string.profile_detail_peakhr_text), Integer.valueOf((int) phr.mValue));
        } else {
            str = String.format(getString(R.string.profile_detail_peakhr_effective), Integer.valueOf(effective));
        }
        textView2.setText(str);
    }

    public void barStatsClick(View v) {
        if (!this.loggingOut) {
            ((MainActivity) getActivity()).showStatsFragment();
        }
    }

    public void barBikeClick(View v) {
        if (!this.loggingOut) {
            ((MainActivity) getActivity()).showBikesFragment();
        }
    }

    public void barFtpClick(View v) {
        if (!this.loggingOut) {
            ((MainActivity) getActivity()).showFunctionalThresholdPowerSettings(true);
        }
    }

    public void barHrzClick(View v) {
        if (!this.loggingOut) {
            ((MainActivity) getActivity()).showPeakHeartRateSettings(true);
        }
    }

    @Override // com.kopin.solos.sync.SyncBarHelper.UISyncRefresh
    public void refresh() {
        updateView();
    }
}
