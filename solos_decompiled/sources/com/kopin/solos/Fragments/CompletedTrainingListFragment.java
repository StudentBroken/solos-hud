package com.kopin.solos.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.TrainingSummaryActivity;
import com.kopin.solos.common.BaseListFragment;
import com.kopin.solos.graphics.TrainingGraph;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.SavedTraining;
import com.kopin.solos.storage.SavedTrainingWorkouts;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.text.DecimalFormat;
import java.util.Calendar;

/* JADX INFO: loaded from: classes24.dex */
public class CompletedTrainingListFragment extends BaseListFragment {
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() { // from class: com.kopin.solos.Fragments.CompletedTrainingListFragment.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(CompletedTrainingListFragment.this.getActivity(), (Class<?>) TrainingSummaryActivity.class);
            intent.putExtras((Bundle) view.getTag());
            CompletedTrainingListFragment.this.startActivity(intent);
        }
    };

    @Override // android.app.ListFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_training_list, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setEmptyView(view.findViewById(android.R.id.empty));
        listView.addHeaderView(new View(getContext()), null, true);
        listView.addFooterView(new View(getContext()), null, true);
        Cursor cursor = SavedRides.getWorkoutHeaderCursor(Workout.RideMode.TRAINING);
        setListAdapter(new CompletedTrainingAdapter(getContext(), cursor, false));
        return view;
    }

    @Override // android.app.ListFragment, android.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(this.itemClickListener);
    }

    private class CompletedTrainingAdapter extends CursorAdapter {
        public CompletedTrainingAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return View.inflate(context, R.layout.view_grouped_list_row, null);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            Workout.Header header = new Ride.Header(cursor);
            SavedTraining training = SavedTrainingWorkouts.get(header.getVirtualWorkoutId());
            Bundle args = new Bundle();
            args.putLong("ride_id", header.getId());
            args.putLong(TrainingDetailFragment.TRAINING_ID_KEY, header.getVirtualWorkoutId());
            args.putString(ThemeActivity.EXTRA_WORKOUT_TYPE, training.getSport().name());
            view.setTag(args);
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(training.getPrimaryMetricImageRes());
            ((TextView) view.findViewById(R.id.textTitle)).setText(training.getTitle());
            double distance = Conversion.distanceForLocale(header.getDistance());
            String distStr = distance > 0.0d ? new DecimalFormat("0.##").format(distance) + " " + Conversion.getUnitOfDistance(view.getContext()) : "---";
            long time = header.getDuration();
            String timeStr = time > 0 ? Utility.formatTime(time) : "---";
            ((TextView) view.findViewById(R.id.txtDistance)).setText(distStr);
            ((TextView) view.findViewById(R.id.txtTime)).setText(timeStr);
            ImageView graph = (ImageView) view.findViewById(R.id.graph);
            int graphWidth = (int) view.getResources().getDimension(R.dimen.preview_training_graph_width);
            int graphHeight = (int) view.getResources().getDimension(R.dimen.preview_training_graph_height);
            SavedWorkout savedWorkout = SavedRides.getWorkout(training.getSport(), header.getId());
            int totalLaps = savedWorkout.getLaps().size();
            Bitmap graphBitmap = TrainingGraph.getGraph(view.getContext(), training, graphWidth, graphHeight, totalLaps);
            graph.setImageBitmap(graphBitmap);
            view.setBackgroundResource(cursor.getPosition() % 2 == 0 ? R.drawable.list_background1 : R.drawable.list_background2);
            ((TextView) view.findViewById(R.id.textTitle)).setText(training.getTitle());
            Calendar cal = Calendar.getInstance();
            cal.add(1, -1);
            boolean yearAgo = header.getActualStartTime() < cal.getTime().getTime();
            String date = DateUtils.formatDateTime(CompletedTrainingListFragment.this.getActivity(), header.getActualStartTime(), yearAgo ? WorkoutHistoryFragment.DATE_FLAGS_YEAR : WorkoutHistoryFragment.DATE_FLAGS);
            TextView textViewDate = (TextView) view.findViewById(R.id.textDate);
            textViewDate.setVisibility(0);
            textViewDate.setText(date);
            TextView textViewTime = (TextView) view.findViewById(R.id.textTime);
            textViewTime.setVisibility(0);
            textViewTime.setText(Utility.formatTimeOnly(header.getActualStartTime()));
        }
    }
}
