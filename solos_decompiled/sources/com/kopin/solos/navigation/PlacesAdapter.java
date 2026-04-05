package com.kopin.solos.navigation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kopin.solos.R;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class PlacesAdapter extends ArrayAdapter<String> {
    private final Activity mActivity;

    public PlacesAdapter(Activity activity, List<String> values) {
        super(activity, R.layout.places_result_list_item, values);
        this.mActivity = activity;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            row = inflater.inflate(R.layout.places_result_list_item, (ViewGroup) null);
            EventViewHolder viewHolder = new EventViewHolder();
            viewHolder.activityName = (TextView) row.findViewById(R.id.place_name);
            row.setTag(viewHolder);
        }
        String dReport = getItem(position);
        EventViewHolder holder = (EventViewHolder) row.getTag();
        holder.activityName.setText(dReport);
        return row;
    }

    static class EventViewHolder {
        public TextView activityName;

        EventViewHolder() {
        }
    }
}
