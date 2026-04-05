package com.kopin.solos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.SetupActivity;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.settings.BikeAdapter;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Bikes;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.view.swipelistview.BaseSwipeListViewListener;
import com.kopin.solos.view.swipelistview.SwipeListView;

/* JADX INFO: loaded from: classes24.dex */
public class BikeFragment extends SafeFragment {
    private static final String TAG = "BikeF";
    private SwipeListView listView;
    private BikeAdapter mBikeAdapter;

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.layout_manage_bikes, null);
        view.findViewById(R.id.btnAddBike).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.BikeFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BikeFragment.this.onPrefAddBikeClick();
            }
        });
        this.mBikeAdapter = new BikeAdapter(getActivity(), SQLHelper.getActiveBikesCursor(), true) { // from class: com.kopin.solos.Fragments.BikeFragment.2
            @Override // com.kopin.solos.settings.BikeAdapter
            protected boolean deleteBike(Bike bike) {
                boolean deleted = super.deleteBike(bike);
                if (!deleted) {
                    BikeFragment.this.listView.closeOpenedItems();
                }
                return deleted;
            }
        };
        this.listView = (SwipeListView) view.findViewById(android.R.id.list);
        this.listView.setAdapter((ListAdapter) this.mBikeAdapter);
        this.listView.setSwipeMode(1);
        this.listView.setSwipeListViewListener(new BaseSwipeListViewListener() { // from class: com.kopin.solos.Fragments.BikeFragment.3
            @Override // com.kopin.solos.view.swipelistview.BaseSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
            public void onStartOpen(int position, int action, boolean right) {
                BikeFragment.this.listView.closeOpenedItems();
                super.onStartOpen(position, action, right);
            }

            @Override // com.kopin.solos.view.swipelistview.BaseSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
            public void onClickFrontView(int position) {
                BikeFragment.this.selectBike(position);
            }
        });
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setTitle(R.string.pref_bike_title);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        return view;
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        refreshBikeList();
        Sync.setProfileUIListener(new Sync.UIListener() { // from class: com.kopin.solos.Fragments.BikeFragment.4
            @Override // com.kopin.solos.share.Sync.UIListener
            public void onStart() {
            }

            @Override // com.kopin.solos.share.Sync.UIListener
            public void onComplete() {
                if (!BikeFragment.this.isFinishing()) {
                    BikeFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.Fragments.BikeFragment.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Log.i(BikeFragment.TAG, "sync completed so update the bike menu");
                            BikeFragment.this.refreshBikeList();
                        }
                    });
                }
            }

            @Override // com.kopin.solos.share.Sync.ConnectionListener
            public void onConnectionChange(boolean connected) {
            }
        });
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarFragmentBackEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshBikeList() {
        this.mBikeAdapter.changeCursor(SQLHelper.getActiveBikesCursor());
        this.mBikeAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectBike(int position) {
        Bikes.selectBike(position);
        this.mBikeAdapter.notifyDataSetChanged();
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarFragmentBackEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPrefAddBikeClick() {
        Intent intent = new Intent(getActivity(), (Class<?>) SetupActivity.class);
        intent.putExtra(SetupActivity.SETUP_INTENT_EXTRA_KEY, 7);
        intent.putExtra(SetupActivity.INTENT_KEY_PROFILE, true);
        getActivity().startActivity(intent);
    }
}
