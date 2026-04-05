package com.kopin.solos.navigation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.solos.Fragments.WorkoutFragment;
import com.kopin.solos.R;
import com.kopin.solos.RidePreview;
import com.kopin.solos.ThemeActivity;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.SportType;
import com.kopin.solos.navigate.helperclasses.BaseMapFragment;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes24.dex */
public class AddRouteFragment extends BaseMapFragment implements OnMapReadyCallback {
    private static final int MAX_RIDE_NAME_LENGTH = 25;
    private LatLng lastPosition;
    private boolean mAvailable;
    private CameraUpdate mCameraUpdate;
    private MarkerOptions mEnd;
    private MapFragment mMapFragment;
    private GoogleMap mMapView;
    private LatLngBounds mNavbounds;
    private PolylineOptions mPolyline;
    TextView mRideTitleTextView;
    SavedWorkout mSavedRide;
    private MarkerOptions mStart;
    private String routeName;
    private Button useRouteButton;
    private LatLng startPosition = null;
    List<Coordinate> initialCoordinateList = new ArrayList();

    public void setArguments(SportType sportType, long workoutId) {
        Bundle bundle = new Bundle();
        bundle.putLong("ride_id", workoutId);
        bundle.getString(ThemeActivity.EXTRA_WORKOUT_TYPE, sportType.name());
        setArguments(bundle);
    }

    public void setArguments(SavedWorkout workout) {
        setArguments(workout.getSportType(), workout.getId());
    }

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mSavedRide = WorkoutFragment.getWorkout(getArguments());
        View view = new View(getActivity());
        try {
            view = inflater.inflate(R.layout.add_route_fragment, container, false);
            double totalDistanceValue = this.mSavedRide.getDistance();
            List<String> displayDistance = TimeDistanceHelper.metresToUnits(getActivity(), totalDistanceValue);
            this.mRideTitleTextView = (TextView) view.findViewById(R.id.txtRideTitle);
            this.routeName = this.mSavedRide.getTitle();
            this.mRideTitleTextView.setText(this.routeName);
            ((TextView) view.findViewById(R.id.directions_total_time)).setText(Utility.formatTime(this.mSavedRide.getDuration(), true));
            String distance = displayDistance.get(0);
            if (distance.contains(".")) {
                double d = Double.valueOf(distance).doubleValue();
                distance = String.format(Locale.US, d >= 100.0d ? "%.0f" : "%.1f", Double.valueOf(d));
            }
            ((TextView) view.findViewById(R.id.directions_total_distance)).setText(distance);
            ((TextView) view.findViewById(R.id.directions_distance_unit)).setText(displayDistance.get(1));
            if (Build.VERSION.SDK_INT <= 19) {
                this.mMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            } else {
                this.mMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            }
            this.mMapFragment.getMapAsync(this);
        } catch (Exception e) {
        }
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setTitle(R.string.navigate_save_route);
        setUpButtons(view);
        return view;
    }

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        RidePreview.shouldPop = true;
    }

    private void setUpButtons(View view) {
        view.findViewById(R.id.layoutTitleInput).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.AddRouteFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AddRouteFragment.this.showEditTextDialog(R.string.finish_ride_name_msg, AddRouteFragment.this.routeName, 25);
            }
        });
        view.findViewById(R.id.route_save_route).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.AddRouteFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AddRouteFragment.this.saveAsRoute();
            }
        });
        view.findViewById(R.id.route_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.AddRouteFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                AddRouteFragment.this.returnToRide();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnToRide() {
        ((RidePreview) getActivity()).performBack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveAsRoute() {
        long routeId = this.mSavedRide.getRouteId();
        SQLHelper.updateRoute(routeId, this.mSavedRide.getDuration(), this.mSavedRide.getDistance(), this.routeName);
        Toast.makeText(getActivity(), R.string.route_saved, 1).show();
        Route.Saved route = new Route.Saved(routeId, this.routeName, this.mSavedRide.getDuration(), this.mSavedRide.getDistance());
        Sync.addRoute(route, this.initialCoordinateList);
        returnToRide();
    }

    @Override // com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onDestroyView() {
        Fragment fragment;
        FragmentTransaction ft;
        if (!getActivity().isFinishing()) {
            if (Build.VERSION.SDK_INT <= 19) {
                fragment = getActivity().getFragmentManager().findFragmentById(R.id.map);
                ft = getActivity().getFragmentManager().beginTransaction();
            } else {
                fragment = getChildFragmentManager().findFragmentById(R.id.map);
                ft = getChildFragmentManager().beginTransaction();
            }
            ft.remove(fragment);
            ft.commit();
        }
        super.onDestroyView();
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        this.mMapView = googleMap;
        updateMap();
    }

    private void updateMap() {
        buildMap();
        this.mMapView.addPolyline(this.mPolyline);
        this.mMapView.addMarker(this.mEnd);
        this.mMapView.addMarker(this.mStart);
        this.mMapView.animateCamera(this.mCameraUpdate);
    }

    private void buildMap() {
        this.lastPosition = null;
        final PolylineOptions rectOptions = new PolylineOptions();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        this.mSavedRide.foreachRecord(new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.navigation.AddRouteFragment.4
            @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
            public boolean onRecord(Record record) {
                if (record.hasLocation()) {
                    LatLng pos = new LatLng(record.getLatitude(), record.getLongitude());
                    if (AddRouteFragment.this.startPosition == null) {
                        AddRouteFragment.this.startPosition = pos;
                    }
                    AddRouteFragment.this.initialCoordinateList.add(record.getTimestampedCoordinate());
                    rectOptions.add(pos);
                    builder.include(pos);
                    if (AddRouteFragment.this.mStart != null) {
                        AddRouteFragment.this.lastPosition = pos;
                        return true;
                    }
                    AddRouteFragment.this.mStart = new MarkerOptions().position(pos).title("").snippet("").icon(BitmapDescriptorFactory.fromBitmap(AddRouteFragment.changeImageColor(BitmapFactory.decodeResource(AddRouteFragment.this.getResources(), R.drawable.ic_map_marker), AddRouteFragment.this.getResources().getColor(R.color.navigation_green))));
                    return true;
                }
                return true;
            }
        });
        if (this.lastPosition != null) {
            this.mEnd = new MarkerOptions().position(this.lastPosition).title("").snippet("").anchor(1.0f, 1.0f).icon(BitmapDescriptorFactory.fromBitmap(changeImageColor(BitmapFactory.decodeResource(getResources(), R.drawable.ic_flag_nav), getResources().getColor(R.color.navigation_orange))));
            this.mNavbounds = builder.build();
            this.mCameraUpdate = CameraUpdateFactory.newLatLngBounds(this.mNavbounds, 300, 300, 0);
            this.mPolyline = rectOptions.color(SupportMenu.CATEGORY_MASK);
            return;
        }
        this.mAvailable = false;
    }

    public static Bitmap changeImageColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0.0f, 0.0f, p);
        return resultBitmap;
    }

    void showEditTextDialog(int titleResId, String defaultValue, final int maxLength) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_custom_edittext, (ViewGroup) null);
        final EditText editText = (EditText) view.findViewById(android.R.id.edit);
        if (defaultValue == null) {
            defaultValue = "";
        }
        editText.append(defaultValue);
        final TextView txtCharsRemaining = (TextView) view.findViewById(R.id.txtCharsRemaining);
        txtCharsRemaining.setVisibility(maxLength > 0 ? 0 : 8);
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() { // from class: com.kopin.solos.navigation.AddRouteFragment.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (which == -1) {
                    Editable text = editText.getText();
                    String value = text.toString();
                    AddRouteFragment.this.mRideTitleTextView.setText(value);
                    AddRouteFragment.this.routeName = value;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(titleResId).setView(view).setPositiveButton(android.R.string.ok, clickListener).setNegativeButton(android.R.string.cancel, clickListener);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(4);
        dialog.show();
        DialogUtils.setDialogTitleDivider(dialog);
        TextWatcher mTextEditorWatcher = new TextWatcher() { // from class: com.kopin.solos.navigation.AddRouteFragment.6
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int i = maxLength - s.length();
                txtCharsRemaining.setText(AddRouteFragment.this.getResources().getQuantityString(R.plurals.label_chars_remaining, i, Integer.valueOf(i)));
                if (s.length() == 0) {
                    dialog.getButton(-1).setEnabled(false);
                } else {
                    dialog.getButton(-1).setEnabled(true);
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }
        };
        if (maxLength > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            int i = maxLength - editText.getText().toString().length();
            txtCharsRemaining.setText(getResources().getQuantityString(R.plurals.label_chars_remaining, i, Integer.valueOf(i)));
            editText.addTextChangedListener(mTextEditorWatcher);
        }
    }
}
