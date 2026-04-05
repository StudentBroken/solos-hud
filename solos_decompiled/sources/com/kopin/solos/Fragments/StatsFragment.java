package com.kopin.solos.Fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.TextMenuAdapter;
import com.kopin.solos.util.StatsHelper;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class StatsFragment extends SafeFragment implements StatsHelper.StatsResultCallback {
    private static final String TAG = StatsFragment.class.getSimpleName();
    CustomMenuItem mLastItem = null;
    private CustomActionProvider.OnItemClickListener mMenuClickListener = new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.Fragments.StatsFragment.1
        @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
        public void onItemClick(int position, CustomMenuItem menuItem) {
            if (StatsFragment.this.mLastItem != null) {
                StatsFragment.this.mLastItem.setEnabled(true);
            }
            menuItem.setEnabled(false);
            StatsFragment.this.mLastItem = menuItem;
            StatsFragment.this.createStats(StatsHelper.Period.values()[menuItem.getId()]);
        }
    };
    private ProgressBar mSpinner;
    private StatsAdapter mStatsAdapter;
    private CustomActionProvider<TextMenuAdapter.TextMenuItem> mStatsProvider;
    private TextView mTxtStatsCurrentInterval;

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mStatsProvider = new CustomActionProvider<>(getActivity());
        this.mStatsProvider.setMenuAdapter(new TextMenuAdapter(getActivity()));
        TextMenuAdapter.TextMenuItem itemL24H = new TextMenuAdapter.TextMenuItem(getString(StatsHelper.Period.DAY.getLabel()), StatsHelper.Period.DAY.ordinal(), TextMenuAdapter.TextMenuType.SMALL);
        itemL24H.setDismissOnTap(true);
        itemL24H.setEnabled(false);
        this.mLastItem = itemL24H;
        this.mStatsProvider.addMenuItem(itemL24H);
        addMenu(StatsHelper.Period.WEEK);
        addMenu(StatsHelper.Period.MONTH);
        addMenu(StatsHelper.Period.QUARTER);
        addMenu(StatsHelper.Period.YEAR);
        addMenu(StatsHelper.Period.FOREVER);
    }

    @Override // android.app.Fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        this.mStatsAdapter = new StatsAdapter(getActivity());
        ((ListView) view.findViewById(android.R.id.list)).setAdapter((ListAdapter) this.mStatsAdapter);
        this.mTxtStatsCurrentInterval = (TextView) view.findViewById(R.id.txtStatsCurrentInterval);
        setHasOptionsMenu(true);
        this.mSpinner = (ProgressBar) view.findViewById(R.id.progressSpinner);
        this.mStatsProvider.setOnItemClickListener(this.mMenuClickListener);
        createStats(StatsHelper.Period.DAY);
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setTitle(R.string.profile_detail_stats_title);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        return view;
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarFragmentBackEnabled(false);
        }
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            getActivity().getActionBar().setHomeButtonEnabled(true);
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        StatsHelper.cancelStatsFetch();
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stats_menu, menu);
        Drawable drawable = menu.findItem(R.id.menu_stats).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getActivity().getResources().getColor(R.color.navigation_orange), PorterDuff.Mode.SRC_ATOP);
        }
        menu.findItem(R.id.menu_stats).setActionProvider(this.mStatsProvider);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void addMenu(StatsHelper.Period period) {
        TextMenuAdapter.TextMenuItem item = new TextMenuAdapter.TextMenuItem(getString(period.getLabel()), period.ordinal(), TextMenuAdapter.TextMenuType.SMALL);
        item.setDismissOnTap(true);
        this.mStatsProvider.addMenuItem(item);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createStats(StatsHelper.Period period) {
        if (this.mTxtStatsCurrentInterval != null) {
            this.mTxtStatsCurrentInterval.setText(getString(period.getLabel()));
        }
        StatsHelper.fetchStatsForPeriod(period, this);
        this.mSpinner.setVisibility(0);
    }

    @Override // com.kopin.solos.util.StatsHelper.StatsResultCallback
    public void onStatsResult(List<StatsHelper.MetricStat> stats) {
        updateStatsUI(stats, false);
    }

    @Override // com.kopin.solos.util.StatsHelper.StatsResultCallback
    public void onStatsProgressUpdate(List<StatsHelper.MetricStat> stats) {
        updateStatsUI(stats, true);
    }

    @Override // com.kopin.solos.util.StatsHelper.StatsResultCallback
    public void onStatsFetchCancelled() {
    }

    private void updateStatsUI(List<StatsHelper.MetricStat> stats, boolean progressUpdate) {
        if (!isAdded()) {
            Log.w(TAG, "Detached");
            return;
        }
        if (!progressUpdate) {
            this.mSpinner.setVisibility(4);
        }
        this.mStatsAdapter.notifyDataSetChanged(stats);
    }

    private class StatsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<StatsHelper.MetricStat> stats;

        private StatsAdapter(Context context) {
            this.stats = new ArrayList();
            this.mInflater = LayoutInflater.from(context);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyDataSetChanged(List<StatsHelper.MetricStat> stats) {
            this.stats = stats;
            notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.stats.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int position) {
            return this.stats.get(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = this.mInflater.inflate(R.layout.stats_list_item, parent, false);
            }
            TextView textTitle = (TextView) convertView.findViewById(R.id.textTitle);
            TextView textDetail = (TextView) convertView.findViewById(R.id.textDetail);
            StatsHelper.MetricStat metricStat = (StatsHelper.MetricStat) getItem(position);
            textTitle.setText(metricStat.titleRes);
            String unit = metricStat.unitRes > 0 ? StatsFragment.this.getString(metricStat.unitRes) : "";
            textDetail.setText(metricStat.stat + " " + unit);
            return convertView;
        }
    }
}
