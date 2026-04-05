package com.kopin.solos.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.kopin.solos.R;
import com.kopin.solos.share.Platforms;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Ride;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedRides;
import com.kopin.solos.storage.util.Utility;
import com.kopin.solos.view.ExpandableCardView;
import java.util.Calendar;

/* JADX INFO: loaded from: classes24.dex */
public class RidePickerDialog {

    public interface OnRideSelectedListener {
        void onRideSelected(long j);
    }

    public static Dialog create(Context context, boolean requireRoute, final OnRideSelectedListener cb) {
        final RideAdapter adapter = new RideAdapter(context, requireRoute);
        adapter.changeCursor();
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.kopin.solos.debug.RidePickerDialog.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                cb.onRideSelected(-1L);
            }
        }).setAdapter(adapter, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.debug.RidePickerDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = (Cursor) adapter.getItem(which);
                Ride.Header ride = new Ride.Header(cursor);
                cb.onRideSelected(ride.getId());
                dialog.dismiss();
            }
        }).setTitle(R.string.navigation_menu_item_ride);
        return builder.create();
    }

    private static class RideAdapter extends CursorAdapter {
        private static final int DATE_FLAGS = 524314;
        private static final int DATE_FLAGS_YEAR = 524304;
        private final Context mContext;
        private final boolean mRoutePicker;

        RideAdapter(Context context, boolean requireRoute) {
            super(context, (Cursor) null, false);
            this.mContext = context;
            this.mRoutePicker = requireRoute;
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return new ExpandableCardView(context);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            Ride.Header ride = new Ride.Header(cursor);
            int numLaps = SQLHelper.countLaps(ride.getId(), true);
            ExpandableCardView expandableCardView = (ExpandableCardView) view;
            expandableCardView.setTag(ride);
            expandableCardView.setTheme(cursor.getPosition() % 2 == 0);
            expandableCardView.setDate(ride.getActualStartTime());
            expandableCardView.setMetric1(Utility.getUserUnitLabel(context, 1));
            expandableCardView.setMetric2(R.string.caps_time);
            double dist = ride.getDistanceForLocale();
            expandableCardView.setValue1(Utility.formatDecimal(Double.valueOf(dist), dist >= 100.0d, 0, 1));
            expandableCardView.setValue2(Utility.formatTime(ride.getDuration()));
            expandableCardView.setCardName(Utility.formatTimeOnly(ride.getActualStartTime()));
            Calendar cal = Calendar.getInstance();
            cal.add(1, -1);
            boolean yearAgo = ride.getActualStartTime() < cal.getTime().getTime();
            expandableCardView.setTopTitle(DateUtils.formatDateTime(this.mContext, ride.getActualStartTime(), yearAgo ? 524304 : 524314));
            expandableCardView.setGhostRide(ride.getGhostRideId() > 0);
            expandableCardView.setLaps(numLaps > 1);
            expandableCardView.setTitle(ride.getTitle());
            expandableCardView.setCalendarView(false);
            if (Platforms.Strava.wasImported(ride)) {
                expandableCardView.setImage(R.drawable.strava_imported);
            } else {
                int rideType = ride.getActivity();
                TypedArray rideTypeIcons = this.mContext.getResources().obtainTypedArray(R.array.sport_types_icons);
                expandableCardView.setImage(rideTypeIcons.getResourceId(rideType, R.drawable.road_practice));
                rideTypeIcons.recycle();
            }
            expandableCardView.disableOnClick();
        }

        public void changeCursor() {
            changeCursor(SavedRides.getWorkoutHeadersCursor(LiveRide.getCurrentSport(), this.mRoutePicker, false));
        }
    }
}
