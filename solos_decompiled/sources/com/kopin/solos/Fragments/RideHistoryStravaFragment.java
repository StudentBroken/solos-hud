package com.kopin.solos.Fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.kopin.peloton.PelotonPrefs;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.ThemeUtil;
import com.kopin.solos.common.BaseListFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SportType;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.strava.StravaHelper;
import com.kopin.solos.share.strava.StravaRide;
import com.kopin.solos.share.strava.StravaSync;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRide;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Shared;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.ExpandableCardView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.jstrava.entities.activity.Activity;

/* JADX INFO: loaded from: classes24.dex */
public class RideHistoryStravaFragment extends BaseListFragment {
    private static final int DATE_FLAGS = 524314;
    private static final int DATE_FLAGS_YEAR = 524304;
    private static final String TAG = "RideHistStravaFragment";
    private static EndlessScrollListener endlessScrollListener;
    ActionBar actionBar;
    private Button btnStravaLogin;
    private ImageView imageStravaNoRide;
    private StravaHelper.GetListActivitiesTask loadRidesTask;
    private StravaRideAdapter mAdapter;
    private boolean mShowIncomplete;
    private View pageList;
    private View pageLoading;
    private View pageLoggedOut;
    private View pageNoRides;
    private View progressList;
    private Toast toast;
    private TextView txtNoRides;
    private TextView txtNoRides2;
    private TextView txtStravaLogin;
    private int pageNum = 1;
    BroadcastReceiver networkConnectivityReceiver = new BroadcastReceiver() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            RideHistoryStravaFragment.this.loadStravaRides();
        }
    };
    private final StravaSync.DownloadListener mStravaDownloadListner = new StravaSync.DownloadListener() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.5
        @Override // com.kopin.solos.share.strava.StravaSync.DownloadListener
        public void onSuccess() {
            if (RideHistoryStravaFragment.this.isStillRequired() && RideHistoryStravaFragment.this.mAdapter != null) {
                RideHistoryStravaFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        RideHistoryStravaFragment.this.mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override // com.kopin.solos.share.strava.StravaSync.DownloadListener
        public void onFailure() {
            Log.w(RideHistoryStravaFragment.TAG, "strava download fail");
            if (RideHistoryStravaFragment.this.isStillRequired() && RideHistoryStravaFragment.this.mAdapter != null) {
                RideHistoryStravaFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CharSequence string;
                        RideHistoryStravaFragment.this.mAdapter.notifyDataSetChanged();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RideHistoryStravaFragment.this.getActivity());
                        AlertDialog.Builder title = builder.setTitle(R.string.strava_import_dialog_importing_title);
                        if (Platforms.Strava.isLoggedIn(RideHistoryStravaFragment.this.getActivity())) {
                            string = RideHistoryStravaFragment.this.getStringFromTheme(R.attr.strStravaImportMessageFailed);
                        } else {
                            string = RideHistoryStravaFragment.this.getString(R.string.strava_acess_revoked);
                        }
                        title.setMessage(string).setCancelable(true).setPositiveButton(R.string.strava_import_btn_done, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.5.2.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        DialogUtils.setDialogTitleDivider(alert);
                    }
                });
            }
        }
    };

    private enum Page {
        LIST,
        NO_RIDES,
        LOADING,
        NOT_LOGGED_IN,
        NO_INTERNET
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        this.actionBar = getActivity().getActionBar();
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarFragmentBackEnabled(true);
        }
        StravaSync.setEmail(PelotonPrefs.getEmail());
        StravaSync.registerStravaListener(this.mStravaDownloadListner);
        updateActionBar();
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.networkConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.networkConnectivityReceiver);
        if (this.toast != null) {
            this.toast.cancel();
        }
        this.toast = null;
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.loadRidesTask != null) {
            this.loadRidesTask.cancel(true);
        }
        if (endlessScrollListener != null) {
            endlessScrollListener.init();
        }
        endlessScrollListener = null;
        StravaSync.unRegisterStravaListener(this.mStravaDownloadListner);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).resetActionBar();
        }
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isStillRequired() && !ShareHelper.onLoginResult(getActivity(), requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.1
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(Platforms which, boolean success, String message) {
                if (RideHistoryStravaFragment.this.isStillRequired()) {
                    RideHistoryStravaFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            RideHistoryStravaFragment.this.loadStravaRides();
                        }
                    });
                }
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadStravaRides() {
        this.pageNum = 1;
        loadStravaRidesNextPage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadStravaRidesNextPage() {
        if (isStillRequired()) {
            if (!Utility.isNetworkAvailable(getActivity())) {
                showPage(Page.NO_INTERNET);
                return;
            }
            if (!Platforms.Strava.isLoggedIn(getActivity())) {
                showPage(Page.NOT_LOGGED_IN);
                return;
            }
            if (this.pageNum >= 0) {
                if (this.pageNum == 1) {
                    showPage(Page.LOADING);
                } else {
                    this.progressList.setVisibility(0);
                }
                this.loadRidesTask = new StravaHelper.GetListActivitiesTask(this.pageNum, LiveRide.getCurrentSport()) { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.2
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // android.os.AsyncTask
                    public void onPostExecute(StravaHelper.StravaActivitySet stravaSet) {
                        if (!isCancelled() && RideHistoryStravaFragment.this.isVisible() && stravaSet != null) {
                            RideHistoryStravaFragment.this.pageNum = stravaSet.page;
                            List<StravaRide> stravaRideList = RideHistoryStravaFragment.this.getRides(stravaSet.activities);
                            if ((stravaRideList == null || stravaRideList.isEmpty()) && (RideHistoryStravaFragment.this.mAdapter == null || RideHistoryStravaFragment.this.mAdapter.isEmpty())) {
                                RideHistoryStravaFragment.this.showPage(Page.NO_RIDES);
                            } else {
                                if (RideHistoryStravaFragment.this.mAdapter != null) {
                                    RideHistoryStravaFragment.this.mAdapter.addAll(stravaRideList);
                                } else {
                                    RideHistoryStravaFragment.this.mAdapter = RideHistoryStravaFragment.this.new StravaRideAdapter(RideHistoryStravaFragment.this.getActivity(), 0, stravaRideList);
                                    RideHistoryStravaFragment.this.setListAdapter(RideHistoryStravaFragment.this.mAdapter);
                                }
                                RideHistoryStravaFragment.this.showPage(Page.LIST);
                            }
                            RideHistoryStravaFragment.this.progressList.setVisibility(8);
                        }
                    }
                };
                this.loadRidesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<StravaRide> getRides(List<Activity> activities) {
        List<StravaRide> stravaRideList = new ArrayList<>();
        if (activities != null && !isFinishing()) {
            for (Activity activity : activities) {
                if (isStillRequired()) {
                    String name = activity.getName();
                    long id = activity.getId();
                    long solosId = getSolosIdFromStrava(activity);
                    StravaRide stravaRide = new StravaRide(id, name, solosId);
                    Long startTime = StravaHelper.getStravaDate(activity.getStart_date());
                    if (startTime != null) {
                        stravaRide.setActualStartTime(startTime.longValue());
                    }
                    stravaRide.setStats(activity);
                    stravaRideList.add(stravaRide);
                }
            }
        }
        return stravaRideList;
    }

    private long getSolosIdFromStrava(Activity activity) {
        try {
            String mSolosId = Platforms.Strava.getLocalIdFor(activity.getUpload_id());
            if (mSolosId == null || mSolosId.isEmpty() || mSolosId.equals("0")) {
                mSolosId = Platforms.Strava.getLocalIdFor(activity.getId());
            }
            return Long.parseLong(mSolosId);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPage(Page page) {
        CharSequence stringFromTheme;
        if (isStillRequired()) {
            this.pageList.setVisibility(page == Page.LIST ? 0 : 4);
            this.txtStravaLogin.setVisibility(page == Page.LIST ? 0 : 8);
            this.pageNoRides.setVisibility((page == Page.NO_RIDES || page == Page.NO_INTERNET) ? 0 : 4);
            TextView textView = this.txtNoRides;
            if (page == Page.NO_INTERNET) {
                stringFromTheme = getString(R.string.text_no_internet_strava);
            } else {
                stringFromTheme = getStringFromTheme(R.attr.strStravaNoRide);
            }
            textView.setText(stringFromTheme);
            this.txtNoRides2.setVisibility(page == Page.NO_INTERNET ? 4 : 0);
            this.btnStravaLogin.setVisibility(page == Page.NO_INTERNET ? 4 : 0);
            this.pageLoading.setVisibility(page == Page.LOADING ? 0 : 4);
            this.pageLoggedOut.setVisibility(page != Page.NOT_LOGGED_IN ? 4 : 0);
            if (page == Page.NO_RIDES) {
                this.imageStravaNoRide.clearColorFilter();
            } else {
                this.imageStravaNoRide.setColorFilter(getActivity().getResources().getColor(R.color.unfocused_grey));
            }
            this.pageLoggedOut.postInvalidate();
            this.pageLoading.postInvalidate();
        }
    }

    private void importRide(StravaRide stravaRide, int position, View view) {
        if (stravaRide != null && isStillRequired()) {
            if (stravaRide.isImported()) {
                updateableToast(getStringFromTheme(R.attr.strStravaAlreadyImported).toString());
                this.mAdapter.updateState(view, false, true);
            } else {
                if (StravaSync.isDownloading()) {
                    updateableToast(getStringFromTheme(R.attr.strStravaAlreadyImporting).toString());
                    return;
                }
                StravaSync.setStravaApi(StravaHelper.jStravaV3);
                if (StravaSync.downloadStrava(stravaRide)) {
                    stravaRide.setImportState(StravaRide.ImportState.Importing);
                    this.mAdapter.updateState(view, true, false);
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void updateableToast(String text) {
        if (this.toast != null) {
            this.toast.cancel();
        }
        this.toast = Toast.makeText(getActivity(), text, 0);
        this.toast.show();
    }

    @Override // android.app.ListFragment, android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strava_rides, container, false);
        if (isStillRequired()) {
            this.pageList = view.findViewById(R.id.pageList);
            this.pageNoRides = view.findViewById(R.id.pageNoRides);
            this.pageLoading = view.findViewById(R.id.pageLoading);
            this.pageLoggedOut = view.findViewById(R.id.pageLoggedOut);
            this.txtNoRides = (TextView) view.findViewById(R.id.txtNoRides);
            this.txtNoRides2 = (TextView) view.findViewById(R.id.txtNoRides2);
            this.txtStravaLogin = (TextView) view.findViewById(R.id.txtStravaLogin);
            this.btnStravaLogin = (Button) view.findViewById(R.id.btnStravaLogin);
            this.btnStravaLogin.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.RideHistoryStravaFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    ShareHelper.login(RideHistoryStravaFragment.this, Platforms.Strava);
                }
            });
            ImageView imageView = (ImageView) view.findViewById(R.id.imageStrava);
            imageView.setColorFilter(getActivity().getResources().getColor(R.color.unfocused_grey));
            this.imageStravaNoRide = (ImageView) view.findViewById(R.id.imageStravaNoRide);
            this.progressList = view.findViewById(R.id.progressList);
            updateActionBar();
        }
        return view;
    }

    private void updateActionBar() {
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setTitle(getStringFromTheme(R.attr.strStravaYourRides));
    }

    @Override // android.app.ListFragment, android.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isStillRequired()) {
            this.pageNum = 1;
            if (endlessScrollListener == null) {
                endlessScrollListener = new EndlessScrollListener();
            }
            endlessScrollListener.init();
            if (getListView() != null) {
                getListView().setOnScrollListener(endlessScrollListener);
            }
            if (this.mAdapter != null) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override // android.app.ListFragment
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (isStillRequired()) {
            View view = l.getAdapter().getView(position, null, null);
            StravaRide ride = (StravaRide) view.getTag();
            importRide(ride, position, view);
        }
    }

    @Override // com.kopin.solos.common.BaseListFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item) || isFinishing()) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showActivitiesFragment();
                    return true;
                }
                break;
            case R.id.action_incomplete /* 2131952533 */:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        this.mShowIncomplete = !this.mShowIncomplete;
        return true;
    }

    private class StravaRideAdapter extends ArrayAdapter<StravaRide> {
        public StravaRideAdapter(Context context, int resource, List<StravaRide> items) {
            super(context, resource, items);
        }

        public void add(List<StravaRide> items) {
            super.addAll(items);
            notifyDataSetChanged();
        }

        public View newView(Context context, ViewGroup parent) {
            return new ExpandableCardView(context);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = newView(RideHistoryStravaFragment.this.getActivity(), parent);
            }
            bindView(position, view);
            return view;
        }

        public void updateState(View view, boolean importing, boolean imported) {
            ExpandableCardView expandableCardView = (ExpandableCardView) view;
            expandableCardView.setImage(R.drawable.strava_not_imported);
            expandableCardView.setProgressBar(importing);
            if (imported) {
                expandableCardView.setImageTint(R.color.strava);
            }
            expandableCardView.invalidate();
            expandableCardView.postInvalidate();
        }

        public void bindView(int position, View view) {
            String cardName;
            StravaRide ride = getItem(position);
            boolean failed = false;
            String stravaId = String.valueOf(ride.getId());
            if (Platforms.Strava.wasImported(stravaId)) {
                SportType sport = LiveRide.getCurrentSport();
                long id = SQLHelper.getLocalId(String.valueOf(ride.getId()), Platforms.Strava.getSharedKey(), PelotonPrefs.getEmail(), Shared.ShareType.fromSportType(sport));
                SavedWorkout localWorkout = SavedRides.getWorkout(sport, id);
                if (localWorkout == null) {
                    if (!StravaSync.isDownloading()) {
                        ride.setImportState(StravaRide.ImportState.None);
                        Platforms.Strava.removeFromImportCache(stravaId);
                        switch (sport) {
                            case RIDE:
                                SQLHelper.removeRide(id);
                                break;
                            case RUN:
                                SQLHelper.removeRun(id);
                                break;
                        }
                    } else {
                        ride.setImportState(StravaRide.ImportState.Importing);
                    }
                } else {
                    ride.setImportState(StravaRide.ImportState.Imported);
                }
            } else if (!StravaSync.isDownloading() && ride.isImporting()) {
                ride.setImportState(StravaRide.ImportState.None);
                failed = true;
            }
            ExpandableCardView expandableCardView = (ExpandableCardView) view;
            expandableCardView.setTag(ride);
            expandableCardView.setTheme(position % 2 == 0);
            expandableCardView.setCalendarView(false);
            expandableCardView.setDate(ride.getActualStartTime());
            Calendar cal = Calendar.getInstance();
            cal.add(1, -1);
            boolean yearAgo = ride.getActualStartTime() < cal.getTime().getTime();
            expandableCardView.setTopTitle(DateUtils.formatDateTime(RideHistoryStravaFragment.this.getActivity(), ride.getActualStartTime(), yearAgo ? 524304 : 524314));
            expandableCardView.setMetric1(Utility.getUserUnitLabel(RideHistoryStravaFragment.this.getActivity(), 1));
            expandableCardView.setMetric2(R.string.caps_time);
            double dist = ride.getDistance() / (Prefs.isMetric() ? 1000.0d : 1609.34d);
            expandableCardView.setValue1(Utility.formatDecimal(Double.valueOf(dist), dist >= 100.0d, 0, 1));
            expandableCardView.setValue2(Utility.formatTime(ride.getDuration()));
            expandableCardView.setImage(R.drawable.strava_not_imported);
            expandableCardView.setProgressBar(ride.isImporting());
            if (ride.isImported()) {
                expandableCardView.setImageTint(R.color.strava);
            }
            expandableCardView.setCardChevronVisible(4);
            if (ride.isImported()) {
                cardName = RideHistoryStravaFragment.this.getString(R.string.strava_imported);
            } else {
                cardName = failed ? RideHistoryStravaFragment.this.getString(R.string.strava_failed) : Utility.formatTimeOnly(ride.getActualStartTime());
            }
            expandableCardView.setCardName(cardName);
            expandableCardView.setTitle(SavedRide.isGeneratedTitle(ride.name) ? "" : ride.name);
            expandableCardView.disableOnClick();
            expandableCardView.setTextColor(ride.isImported() ? R.color.strava : R.color.white);
            expandableCardView.invalidate();
        }
    }

    public class EndlessScrollListener implements AbsListView.OnScrollListener {
        private int currentPage;
        private boolean loading;
        private int previousTotal;
        private int visibleThreshold;

        public EndlessScrollListener() {
            this.visibleThreshold = 5;
            this.currentPage = 0;
            this.previousTotal = 0;
            this.loading = true;
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = 5;
            this.currentPage = 0;
            this.previousTotal = 0;
            this.loading = true;
            this.visibleThreshold = visibleThreshold;
        }

        public void init() {
            this.currentPage = 0;
            this.previousTotal = 0;
            this.loading = true;
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (this.loading && totalItemCount > this.previousTotal) {
                this.loading = false;
                this.previousTotal = totalItemCount;
                this.currentPage++;
            }
            if (!this.loading && totalItemCount - visibleItemCount <= this.visibleThreshold + firstVisibleItem) {
                RideHistoryStravaFragment.this.loadStravaRidesNextPage();
                this.loading = true;
            }
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    public CharSequence getStringFromTheme(int attr) {
        return ThemeUtil.getString(getActivity(), attr);
    }
}
