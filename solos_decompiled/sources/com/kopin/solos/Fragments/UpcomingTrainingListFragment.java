package com.kopin.solos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.TrainingSummaryActivity;
import com.kopin.solos.common.BaseFragment;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.Share;
import com.kopin.solos.share.ShareHelper;
import com.kopin.solos.share.trainingpeaks.TPHelper;
import com.kopin.solos.storage.util.Utility;

/* JADX INFO: loaded from: classes24.dex */
public class UpcomingTrainingListFragment extends BaseFragment {
    private ViewGroup mEmptyPageContainer;
    private ExpandableListView mListView;
    private View mPageEmptyList;
    private View mPageError;
    private View mPageNotLoggedIn;
    private UpcomingTrainingAdapter mTrainingListAdapter;
    private final ExpandableListView.OnChildClickListener trainingClickListener = new ExpandableListView.OnChildClickListener() { // from class: com.kopin.solos.Fragments.UpcomingTrainingListFragment.3
        @Override // android.widget.ExpandableListView.OnChildClickListener
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            UpcomingTrainingListFragment.this.showTrainingDetails(groupPosition, childPosition);
            return false;
        }
    };
    private final Share.ShareListener trainingsListener = new Share.ShareListener() { // from class: com.kopin.solos.Fragments.UpcomingTrainingListFragment.4
        @Override // com.kopin.solos.share.Share.ShareListener
        public void onResponse(Share.ShareResponse response) {
            if (!response.isSuccess()) {
                Log.d("TrainingPeaks", response.responseCode + ": " + response.rawResponse);
                UpcomingTrainingListFragment.this.showPage(Page.ERROR);
                return;
            }
            if (response.serialisedResponse != null) {
                LongSparseArray<ArraySet<String>> schedule = (LongSparseArray) response.serialisedResponse;
                UpcomingTrainingListFragment.this.mTrainingListAdapter.notifyDataSetChanged(schedule);
                if (schedule.size() > 0) {
                    UpcomingTrainingListFragment.this.expandGroups();
                    return;
                }
            }
            UpcomingTrainingListFragment.this.showPage(Page.EMPTY_LIST);
            Log.d("TrainingPeaks", response.responseCode + ": " + response.rawResponse);
        }
    };

    private enum Page {
        EMPTY_LIST(R.id.pageEmptyListTp),
        LOADING(R.id.pageLoading),
        NOT_LOGGED_IN(R.id.pageLoggedOut),
        ERROR(R.id.pageError);

        int id;

        Page(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grouped_list, container, false);
        this.mEmptyPageContainer = (ViewGroup) view.findViewById(android.R.id.empty);
        this.mPageEmptyList = view.findViewById(R.id.pageEmptyListTp);
        ((TextView) this.mPageEmptyList.findViewById(R.id.textEmptyList)).setText(R.string.tp_no_upcoming_workouts_msg);
        ((TextView) this.mPageEmptyList.findViewById(R.id.textEmptyList2)).setText(R.string.tp_no_upcoming_workouts_msg2);
        this.mPageNotLoggedIn = view.findViewById(R.id.pageLoggedOut);
        this.mPageError = view.findViewById(R.id.pageError);
        this.mTrainingListAdapter = new UpcomingTrainingAdapter(getActivity(), new LongSparseArray());
        this.mListView = (ExpandableListView) view.findViewById(R.id.groupedList);
        this.mListView.setAdapter(this.mTrainingListAdapter);
        this.mListView.setOnChildClickListener(this.trainingClickListener);
        this.mListView.setEmptyView(this.mEmptyPageContainer);
        expandGroups();
        loadTrainingWorkouts();
        return view;
    }

    @Override // com.kopin.solos.common.BaseFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isStillRequired() && !ShareHelper.onLoginResult(getActivity(), requestCode, resultCode, data, new ShareHelper.AuthListener() { // from class: com.kopin.solos.Fragments.UpcomingTrainingListFragment.1
            @Override // com.kopin.solos.share.ShareHelper.AuthListener
            public void onResult(Platforms which, boolean success, String message) {
                if (UpcomingTrainingListFragment.this.isStillRequired()) {
                    UpcomingTrainingListFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.UpcomingTrainingListFragment.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            UpcomingTrainingListFragment.this.loadTrainingWorkouts();
                        }
                    });
                }
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void expandGroups() {
        int groupCount = this.mTrainingListAdapter.getGroupCount();
        while (groupCount > 0) {
            groupCount--;
            this.mListView.expandGroup(groupCount);
        }
    }

    private void updateErrorUI() {
        TextView textView = (TextView) this.mPageError.findViewById(R.id.textEmptyList);
        if (Utility.isNetworkAvailable(getActivity())) {
            textView.setText(R.string.text_network_error);
        } else {
            textView.setText(R.string.text_no_internet_strava);
        }
        this.mPageError.findViewById(R.id.textEmptyList2).setVisibility(4);
    }

    private void updateLoggedOutUI() {
        this.mPageNotLoggedIn.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.UpcomingTrainingListFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                UpcomingTrainingListFragment.this.login();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTrainingWorkouts() {
        if (TPHelper.isLoggedIn(getActivity())) {
            showPage(Page.LOADING);
            TPHelper.importWorkouts(getActivity(), this.trainingsListener);
        } else {
            showPage(Page.NOT_LOGGED_IN);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void login() {
        if (isStillRequired()) {
            if (Utility.isNetworkAvailable(getActivity())) {
                ShareHelper.login(this, Platforms.TrainingPeaks);
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTrainingDetails(int groupPosition, int childPosition) {
        Intent intent = new Intent(getActivity(), (Class<?>) TrainingSummaryActivity.class);
        intent.putExtra(TrainingSummaryActivity.TRAINING_EXTERNAL_ID, this.mTrainingListAdapter.getChild(groupPosition, childPosition).getExternalId());
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPage(Page page) {
        if (isStillRequired()) {
            int count = this.mEmptyPageContainer.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = this.mEmptyPageContainer.getChildAt(i);
                view.setVisibility(view.getId() == page.getId() ? 0 : 4);
            }
            switch (page) {
                case NOT_LOGGED_IN:
                    updateLoggedOutUI();
                    break;
                case ERROR:
                    updateErrorUI();
                    break;
            }
        }
    }
}
