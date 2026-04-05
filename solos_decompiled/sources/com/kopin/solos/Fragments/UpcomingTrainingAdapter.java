package com.kopin.solos.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.ArraySet;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.graphics.TrainingGraph;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.share.TrainingCache;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/* JADX INFO: loaded from: classes24.dex */
class UpcomingTrainingAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LongSparseArray<ArraySet<String>> mTrainingSchedule;

    UpcomingTrainingAdapter(Context context, LongSparseArray<ArraySet<String>> schedule) {
        this.mContext = context;
        this.mTrainingSchedule = schedule == null ? new LongSparseArray<>() : schedule;
    }

    void notifyDataSetChanged(LongSparseArray<ArraySet<String>> schedule) {
        this.mTrainingSchedule = schedule;
        notifyDataSetChanged();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.mTrainingSchedule.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int groupPosition) {
        long key = this.mTrainingSchedule.keyAt(groupPosition);
        ArraySet<String> externalIds = this.mTrainingSchedule.get(key);
        if (externalIds != null) {
            return externalIds.size();
        }
        return 0;
    }

    @Override // android.widget.ExpandableListAdapter
    public GroupHeader getGroup(int groupPosition) {
        long key = this.mTrainingSchedule.keyAt(groupPosition);
        return new GroupHeader(this.mContext, key);
    }

    @Override // android.widget.ExpandableListAdapter
    public SavedTraining getChild(int groupPosition, int childPosition) {
        long key = this.mTrainingSchedule.keyAt(groupPosition);
        ArraySet<String> externalIds = this.mTrainingSchedule.get(key);
        String externalId = externalIds.valueAt(childPosition);
        return TrainingCache.get(Platforms.TrainingPeaks, externalId);
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int groupPosition) {
        return 0L;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int groupPosition, int childPosition) {
        return 0L;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(this.mContext, R.layout.view_grouped_list_header, null);
        }
        updateGroupHeaderView(convertView, getGroup(groupPosition));
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(this.mContext, R.layout.view_grouped_list_row, null);
        }
        int flatListPosition = ((ExpandableListView) parent).getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
        updateRowView(convertView, getChild(groupPosition, childPosition), (flatListPosition - groupPosition) - 1);
        return convertView;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void updateGroupHeaderView(View view, GroupHeader header) {
        ((TextView) view.findViewById(R.id.txtGroupTitle)).setText(header.title);
        ((TextView) view.findViewById(R.id.txtGroupSubTitle)).setText(header.subtitle);
    }

    private void updateRowView(View view, SavedTraining training, int flatListPosition) {
        if (training != null) {
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(training.getPrimaryMetricImageRes());
            ((TextView) view.findViewById(R.id.textTitle)).setText(training.getTitle());
            double distance = Conversion.distanceForLocale(training.getDistance());
            String distStr = distance > 0.0d ? new DecimalFormat("0.##").format(distance) + " " + Conversion.getUnitOfDistance(view.getContext()) : "---";
            long time = training.getDuration() * 1000;
            String timeStr = time > 0 ? Utility.formatTime(time) : "---";
            ((TextView) view.findViewById(R.id.txtDistance)).setText(distStr);
            ((TextView) view.findViewById(R.id.txtTime)).setText(timeStr);
            ImageView graph = (ImageView) view.findViewById(R.id.graph);
            int graphWidth = (int) view.getResources().getDimension(R.dimen.preview_training_graph_width);
            int graphHeight = (int) view.getResources().getDimension(R.dimen.preview_training_graph_height);
            Bitmap graphBitmap = TrainingGraph.getGraph(view.getContext(), training, graphWidth, graphHeight);
            graph.setImageBitmap(graphBitmap);
            view.setBackgroundResource(flatListPosition % 2 == 0 ? R.drawable.list_background1 : R.drawable.list_background2);
            view.findViewById(R.id.imageBadge).setVisibility(training.isCompleted() ? 0 : 4);
        }
    }

    private static class GroupHeader {
        final String subtitle;
        final String title;

        GroupHeader(Context context, long time) {
            this.title = Utility.formatTimeToRelativeDays(context, time);
            this.subtitle = new SimpleDateFormat("dd MMM").format(new Date(time));
        }
    }
}
