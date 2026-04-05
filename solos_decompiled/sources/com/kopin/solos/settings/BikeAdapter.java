package com.kopin.solos.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Bike;
import com.kopin.solos.storage.Bikes;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Conversion;
import com.kopin.solos.storage.util.Utility;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class BikeAdapter extends CursorAdapter {
    private static final String TAG = "BikeAdapter";
    private long firstBikeId;
    private Context mContext;

    public BikeAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        this.firstBikeId = -1L;
        this.mContext = context;
    }

    @Override // android.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.list_item_bike, null);
    }

    @Override // android.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        this.firstBikeId = Bikes.getActiveBikes().get(0).getId();
        final Bike bike = new Bike(cursor);
        ((TextView) view.findViewById(R.id.txtPrefBikeTitle)).setText(bike.getName());
        Locale locale = Locale.US;
        Object[] objArr = new Object[2];
        objArr[0] = Double.valueOf(Utility.isMetric() ? bike.getWheelSize() : Conversion.mmIntoInches(bike.getWheelSize()));
        objArr[1] = Conversion.getUnitOfLengthSmall(context);
        String wheelSize = String.format(locale, "%.2f %s", objArr);
        String bikeWeight = String.format(Locale.US, "%.2f %s", Double.valueOf(Utility.weightInUnitSystem(Double.toString(bike.getWeight()), 0)), Utility.getUnitOfWeight(context));
        String defaultBikeString = this.mContext.getResources().getString(R.string.circumference) + " " + wheelSize + ", " + this.mContext.getResources().getString(R.string.bike_weight) + " " + bikeWeight;
        ((TextView) view.findViewById(R.id.txtPrefBikeDetail)).setText(defaultBikeString);
        view.findViewById(R.id.imageSelection).setVisibility(Prefs.getChosenBikeId() == bike.getId() ? 0 : 4);
        view.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.settings.BikeAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BikeAdapter.this.deleteBike(bike);
            }
        });
    }

    protected boolean deleteBike(Bike bike) {
        if (canDeleteBike(bike)) {
            Sync.removeBike(bike);
            changeCursor(SQLHelper.getActiveBikesCursor());
            notifyDataSetChanged();
            return true;
        }
        showAlertDialog();
        return false;
    }

    public boolean canDeleteBike(Bike bike) {
        return getCount() > 1 && bike.getId() != Prefs.getChosenBikeId();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext).setTitle(R.string.parent_ride_delete_dialog_title).setMessage(getCount() > 1 ? R.string.msg_cannot_remove_bike_2 : R.string.msg_cannot_remove_bike_1).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.settings.BikeAdapter.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                BikeAdapter.this.notifyDataSetChanged();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
    }
}
