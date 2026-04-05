package com.kopin.solos.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import com.kopin.solos.R;
import com.kopin.solos.navigation.RouteCardView;
import com.kopin.solos.navigation.TimeDistanceHelper;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SQLHelper;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class RoutePickerDialog {

    public interface OnRouteSelectedListener {
        void onRouteSelected(long j);
    }

    public static Dialog create(Context context, boolean requireRoute, final OnRouteSelectedListener cb) {
        final RouteAdapter adapter = new RouteAdapter(context);
        adapter.changeCursor();
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.kopin.solos.debug.RoutePickerDialog.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                cb.onRouteSelected(-1L);
            }
        }).setAdapter(adapter, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.debug.RoutePickerDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = (Cursor) adapter.getItem(which);
                long routeId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                cb.onRouteSelected(routeId);
                dialog.dismiss();
            }
        }).setTitle(R.string.navigation_menu_item_ride);
        return builder.create();
    }

    private static class RouteAdapter extends CursorAdapter {
        private static final int DATE_FLAGS = 524314;
        private static final int DATE_FLAGS_YEAR = 524304;
        private final Context mContext;

        RouteAdapter(Context context) {
            super(context, (Cursor) null, false);
            this.mContext = context;
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return new RouteCardView(context);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Route.TITLE));
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(Route.DISTANCE));
            List<String> distanceUnits = TimeDistanceHelper.metresToFullUnits(context, distance);
            RouteCardView expandableCardView = (RouteCardView) view;
            expandableCardView.setTheme(cursor.getPosition() % 2 == 0);
            expandableCardView.setTitle(title);
            expandableCardView.setDistance(distanceUnits);
            expandableCardView.disableOnClick();
            ImageButton deleteRideButton = (ImageButton) expandableCardView.findViewById(R.id.btnDeleteRide);
            deleteRideButton.setVisibility(8);
        }

        public void changeCursor() {
            changeCursor(SQLHelper.getRoutesCursor(false));
        }
    }
}
