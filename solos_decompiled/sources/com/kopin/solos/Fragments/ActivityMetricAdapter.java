package com.kopin.solos.Fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.kopin.solos.storage.Record;
import com.kopin.solos.view.ExpandableCardView;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes24.dex */
public class ActivityMetricAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<Record.MetricData> mMetricsToShow = new ArrayList<>();

    ActivityMetricAdapter(Context context) {
        this.mContext = context;
    }

    void clear() {
        this.mMetricsToShow.clear();
        notifyDataSetInvalidated();
    }

    void addMetric(Record.MetricData type) {
        this.mMetricsToShow.add(type);
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mMetricsToShow.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mMetricsToShow.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return this.mMetricsToShow.get(position).ordinal();
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return position % 2 == 0 ? null : null;
    }

    ExpandableCardView getView(Record.MetricData metric) {
        return null;
    }
}
