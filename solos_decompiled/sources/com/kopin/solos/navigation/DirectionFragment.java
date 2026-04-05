package com.kopin.solos.navigation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.common.DialogUtils;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.common.permission.Permission;
import com.kopin.solos.common.permission.PermissionUtil;
import com.kopin.solos.navigate.apimodels.googleplacesmodel.GooglePlacesResult;
import com.kopin.solos.navigate.communication.CallAPI;
import com.kopin.solos.navigate.communication.tasks.DirectionsTask;
import com.kopin.solos.navigate.communication.tasks.PlacesTask;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.RouteBuilder;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigate.helperclasses.BaseMapFragment;
import com.kopin.solos.sensors.gps.LocationHandler;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.settings.Prefs;
import com.kopin.solos.storage.util.Utility;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class DirectionFragment extends BaseMapFragment implements OnMapReadyCallback {
    private static boolean shouldCancelTasks = false;
    private ImageView clearIcon;
    private TextView distanceUnit;
    private ListView listView;
    private PlacesAdapter mAdapter;
    private MapFragment mConfirmationMapFragment;
    private LatLng mLatLng;
    private Location mLocation;
    private MapFragment mMapFragment;
    private NavigationRoute navigationRoute;
    private EditText navigationText;
    private LinearLayout searchConfirmLayout;
    private ImageView searchIcon;
    private LinearLayout searchResults;
    private TextView totalDistance;
    private TextView totalTime;
    private Button useRouteButton;
    private View view;
    private final long idle_min = 400;
    private final int NUMBER_OF_CHARS_MINIMUM = 3;
    private long last_text_edit = 0;
    private final Handler h = new Handler();
    private boolean already_queried = false;
    private GoogleMap mMapView = null;
    private GoogleMap mConfirmationMap = null;
    DirectionsTask directionsTask = null;
    PlacesTask placesTask = null;
    AlertDialog mWaitForFixDialog = null;
    private final Runnable input_finish_checker = new Runnable() { // from class: com.kopin.solos.navigation.DirectionFragment.4
        @Override // java.lang.Runnable
        public void run() {
            if (DirectionFragment.this.isStillRequired() && System.currentTimeMillis() > (DirectionFragment.this.last_text_edit + 400) - 500) {
                String s = DirectionFragment.this.navigationText.getText().toString();
                DirectionFragment.this.getPlacesResult(s);
            }
        }
    };
    private final NavigationRoute.RouteObserver mRouteObserver = new NavigationRoute.RouteObserver() { // from class: com.kopin.solos.navigation.DirectionFragment.9
        @Override // com.kopin.solos.navigate.geolocation.NavigationRoute.RouteObserver
        public void onRouteCalculated() {
            if (DirectionFragment.this.isStillRequired()) {
                DirectionFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.navigation.DirectionFragment.9.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DirectionFragment.this.displayResults();
                    }
                });
            }
        }
    };
    private final LocationHandler.InternalLocationListener mLocationListener = new LocationHandler.InternalLocationListener() { // from class: com.kopin.solos.navigation.DirectionFragment.11
        @Override // com.kopin.solos.sensors.gps.LocationHandler.InternalLocationListener
        public void onLocationChanged(Location location, double distanceFromLastLocation) {
            if (location != null) {
                if (DirectionFragment.this.mWaitForFixDialog != null) {
                    DirectionFragment.this.mWaitForFixDialog.dismiss();
                    DirectionFragment.this.mWaitForFixDialog = null;
                }
                if (DirectionFragment.this.mMapView != null && DirectionFragment.this.mLocation != null) {
                    DirectionFragment.this.mLatLng = new LatLng(DirectionFragment.this.mLocation.getLatitude(), DirectionFragment.this.mLocation.getLongitude());
                    DirectionFragment.this.mMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(DirectionFragment.this.mLatLng, 8.0f));
                }
            }
        }
    };

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.view = inflater.inflate(R.layout.direction_fragment, container, false);
        if (Utility.isNetworkAvailable(getActivity())) {
            try {
                if (Build.VERSION.SDK_INT <= 19) {
                    this.mMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
                    this.mConfirmationMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.minimap);
                } else {
                    this.mMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                    this.mConfirmationMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.minimap);
                }
                this.mMapFragment.getMapAsync(this);
                this.navigationText = (EditText) this.view.findViewById(R.id.navigate_text_box);
                this.searchResults = (LinearLayout) this.view.findViewById(R.id.directions_results);
                this.searchIcon = (ImageView) this.view.findViewById(R.id.directions_search_icon);
                this.clearIcon = (ImageView) this.view.findViewById(R.id.directions_clear_icon);
                this.totalDistance = (TextView) this.view.findViewById(R.id.directions_total_distance);
                this.totalTime = (TextView) this.view.findViewById(R.id.directions_total_time);
                this.distanceUnit = (TextView) this.view.findViewById(R.id.directions_distance_unit);
                this.listView = (ListView) this.view.findViewById(R.id.places_list_view);
                this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.1
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DirectionFragment.this.already_queried = true;
                        String dReport = (String) parent.getItemAtPosition(position);
                        DirectionFragment.this.navigationText.setText(dReport);
                        DirectionFragment.this.searchResults.setVisibility(8);
                        DirectionFragment.this.pressNavigate();
                    }
                });
                this.clearIcon.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        DirectionFragment.this.pressClear();
                    }
                });
                this.navigationText.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.navigation.DirectionFragment.3
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        if (s.length() == 0) {
                            DirectionFragment.this.searchIcon.setVisibility(0);
                            DirectionFragment.this.clearIcon.setVisibility(4);
                        } else {
                            DirectionFragment.this.searchIcon.setVisibility(4);
                            DirectionFragment.this.clearIcon.setVisibility(0);
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        DirectionFragment.this.last_text_edit = System.currentTimeMillis();
                        DirectionFragment.this.h.removeCallbacks(DirectionFragment.this.input_finish_checker);
                        if (s.length() < 3 || DirectionFragment.this.already_queried) {
                            DirectionFragment.this.already_queried = false;
                        } else {
                            DirectionFragment.this.h.postDelayed(DirectionFragment.this.input_finish_checker, 400L);
                        }
                    }
                });
                this.searchConfirmLayout = (LinearLayout) this.view.findViewById(R.id.directions_confirm_layout);
            } catch (InflateException e) {
            }
            setUpButtons(this.view);
        } else {
            this.view.findViewById(R.id.directions_search_box).setVisibility(8);
            this.view.findViewById(R.id.directions_no_internet).setVisibility(0);
        }
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setTitle(R.string.navigation_menu_item_address);
        return this.view;
    }

    @Override // com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onDestroyView() {
        Fragment fragment;
        Fragment fragment2;
        FragmentTransaction ft;
        if (Build.VERSION.SDK_INT <= 19) {
            fragment = getActivity().getFragmentManager().findFragmentById(R.id.map);
            fragment2 = getActivity().getFragmentManager().findFragmentById(R.id.minimap);
            ft = getActivity().getFragmentManager().beginTransaction();
        } else {
            fragment = getChildFragmentManager().findFragmentById(R.id.map);
            fragment2 = getChildFragmentManager().findFragmentById(R.id.minimap);
            ft = getChildFragmentManager().beginTransaction();
        }
        ft.remove(fragment);
        ft.remove(fragment2);
        ft.commitAllowingStateLoss();
        super.onDestroyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillPlacesAdapter(List<String> places) {
        this.mAdapter = new PlacesAdapter(getActivity(), places);
        if (this.mAdapter.isEmpty()) {
            this.searchResults.setVisibility(8);
        } else {
            this.listView.setAdapter((ListAdapter) this.mAdapter);
            this.searchResults.setVisibility(0);
        }
    }

    private void setUpButtons(View view) {
        this.useRouteButton = (Button) view.findViewById(R.id.search_use_route);
        this.useRouteButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DirectionFragment.this.pressUseRoute();
            }
        });
        this.useRouteButton.setEnabled(false);
        view.findViewById(R.id.search_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                DirectionFragment.this.pressCancel();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressUseRoute() {
        long routeId = SQLHelper.addRoute();
        SQLHelper.addCoordList(this.navigationRoute.toCoordinates(), routeId);
        this.navigationRoute.setId(routeId);
        Navigator.setRoute(this.navigationRoute, new Coordinate(this.mLatLng));
        ((MainActivity) getActivity()).popFromAddressSelector();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressClear() {
        this.navigationText.setText("");
        this.searchIcon.setVisibility(0);
        this.clearIcon.setVisibility(4);
        this.searchResults.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressCancel() {
        pressClear();
        this.searchConfirmLayout.setVisibility(8);
        getActivity().getActionBar().setTitle(R.string.navigation_menu_item_address);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressNavigate() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService("input_method");
        imm.hideSoftInputFromWindow(this.navigationText.getWindowToken(), 0);
        this.useRouteButton.setEnabled(false);
        this.searchResults.setVisibility(8);
        String theDestination = this.navigationText.getText().toString();
        this.searchConfirmLayout.setVisibility(0);
        getDirectionsResult(this.mLatLng, theDestination);
        getActivity().getActionBar().setTitle(R.string.navigate_your_route);
    }

    private void drawPoly(List<Coordinate> wayPoints, int colour, int theWidth) {
        List<Waypoint> waypoints;
        this.mConfirmationMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Coordinate w : wayPoints) {
            LatLng l = new LatLng(w.getLatitude(), w.getLongitude());
            polylineOptions.add(l);
            builder.include(l);
        }
        this.mConfirmationMap.addPolyline(polylineOptions.color(SupportMenu.CATEGORY_MASK).width(theWidth));
        LatLngBounds mLatLngBounds = builder.build();
        this.mConfirmationMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 70));
        dropPinAt(wayPoints.get(0), R.drawable.ic_map_marker, "Start", R.color.navigation_green);
        dropPinAt(wayPoints.get(wayPoints.size() - 1), R.drawable.ic_flag_nav, "Finish", R.color.navigation_orange);
        if (Config.DEBUG && this.navigationRoute != null && (waypoints = this.navigationRoute.getRoute()) != null) {
            for (Waypoint wp : waypoints) {
                if (!wp.isStart && !wp.isEnd) {
                    dropPinAt(wp, R.drawable.ic_map_marker, wp.instruction, R.color.navigation_grey);
                }
            }
        }
    }

    private void dropPinAt(Coordinate point, int PinIcon, String s, int colour) {
        Bitmap marker = BitmapFactory.decodeResource(getResources(), PinIcon);
        float theXValue = 0.5f;
        if (PinIcon == R.drawable.ic_flag_nav) {
            theXValue = 1.0f;
        }
        this.mConfirmationMap.addMarker(new MarkerOptions().position(new LatLng(point.getLatitude(), point.getLongitude())).title(s).snippet(s).anchor(theXValue, 1.0f).icon(BitmapDescriptorFactory.fromBitmap(changeImageColor(marker, getResources().getColor(colour)))));
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

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        shouldCancelTasks = false;
        if (isStillRequired()) {
            if (!Navigator.isLocationEnabled()) {
                new AlertDialog.Builder(getActivity()).setMessage(R.string.dialog_gps_disabled).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.7
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (DirectionFragment.this.isStillRequired()) {
                            ((MainActivity) DirectionFragment.this.getActivity()).popFromAddressSelector();
                        }
                    }
                }).setCancelable(false).create().show();
                return;
            }
            if (!PermissionUtil.permitted(getActivity(), Permission.ACCESS_FINE_LOCATION)) {
                PermissionUtil.askPermission(getActivity(), Permission.ACCESS_FINE_LOCATION);
                return;
            }
            Navigator.startLocationUpdates(this.mLocationListener);
            if (!Navigator.hasRecentLocation()) {
                this.mWaitForFixDialog = new AlertDialog.Builder(getActivity()).setMessage(R.string.dialog_wait_for_gps_fix).setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.kopin.solos.navigation.DirectionFragment.8
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (DirectionFragment.this.isStillRequired()) {
                            ((MainActivity) DirectionFragment.this.getActivity()).popFromAddressSelector();
                        }
                    }
                }).setCancelable(false).create();
                this.mWaitForFixDialog.show();
            }
        }
    }

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        Navigator.stopLocationUpdates(this.mLocationListener);
        if (this.placesTask != null) {
            this.placesTask.cancel(true);
        }
        if (this.directionsTask != null) {
            this.directionsTask.cancel(true);
        }
        if (this.navigationText != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService("input_method");
            imm.hideSoftInputFromWindow(this.navigationText.getWindowToken(), 0);
        }
        shouldCancelTasks = true;
    }

    private void getDirectionsResult(LatLng myPosition, String dest) {
        this.navigationRoute = RouteBuilder.create(dest, getTravelMode());
        this.navigationRoute.calculateRoute(myPosition, this.mRouteObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayResults() {
        List<Waypoint> waypoints;
        if (isStillRequired()) {
            if (!this.navigationRoute.hasDirections()) {
                this.searchConfirmLayout.setVisibility(8);
                Dialog dialog = DialogUtils.createDialog(getActivity(), R.string.no_routes_available, R.string.no_cycle_routes_available, R.string.dialog_button_ok, (Runnable) null);
                dialog.show();
                DialogUtils.setDialogTitleDivider(dialog);
                return;
            }
            drawPoly(this.navigationRoute.getPreProcessRoute(), R.color.navigation_orange, 4);
            completeDisplay();
            if (Config.DEBUG && (waypoints = this.navigationRoute.getRoute()) != null) {
                for (Waypoint wp : waypoints) {
                    if (!wp.isStart && !wp.isEnd) {
                        dropPinAt(wp, R.drawable.ic_map_marker, wp.instruction, R.color.navigation_grey);
                    }
                }
            }
            this.useRouteButton.setEnabled(true);
        }
    }

    private void completeDisplay() {
        int totalDistanceValue = this.navigationRoute.getDistance();
        int totalTimeValueseconds = this.navigationRoute.getTime();
        this.totalTime.setText(Utility.formatTime(totalTimeValueseconds * 1000));
        List<String> displayDistance = TimeDistanceHelper.metresToUnits(getActivity(), totalDistanceValue);
        this.totalDistance.setText(displayDistance.get(0));
        this.distanceUnit.setText(displayDistance.get(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getPlacesResult(String requestedString) {
        URL url = null;
        try {
            URL url2 = new URL(CallAPI.GOOGLE_PLACE_URL + requestedString.replaceAll(" ", "%20") + "&location=" + this.mLatLng.latitude + "," + this.mLatLng.longitude + "&key=" + CallAPI.googleAPIKey);
            url = url2;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (isStillRequired()) {
            this.placesTask = new PlacesTask(url) { // from class: com.kopin.solos.navigation.DirectionFragment.10
                @Override // android.os.AsyncTask
                public void onPostExecute(final GooglePlacesResult result) {
                    if (DirectionFragment.this.isStillRequired() && !DirectionFragment.shouldCancelTasks) {
                        DirectionFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.kopin.solos.navigation.DirectionFragment.10.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (result != null && DirectionFragment.this.isStillRequired()) {
                                    DirectionFragment.this.fillPlacesAdapter(result.getList());
                                }
                            }
                        });
                    }
                }
            };
            this.placesTask.execute(new Object[0]);
        }
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        if (isStillRequired()) {
            if (this.mMapView == null) {
                this.mMapView = googleMap;
                this.mLocation = ((MainActivity) getActivity()).getLocation();
                if (this.mLocation != null) {
                    this.mLatLng = new LatLng(this.mLocation.getLatitude(), this.mLocation.getLongitude());
                    this.mMapView.animateCamera(CameraUpdateFactory.newLatLngZoom(this.mLatLng, 8.0f));
                }
                this.mConfirmationMapFragment.getMapAsync(this);
                return;
            }
            this.mConfirmationMap = googleMap;
            if (this.mLatLng != null) {
                this.mConfirmationMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.mLatLng, 8.0f));
            }
        }
    }

    private String getTravelMode() {
        switch (LiveRide.getCurrentSport()) {
        }
        return NavigationRoute.TRAVEL_MODE_BICYCLING;
    }

    @Override // android.app.Fragment
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (isStillRequired()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (PermissionUtil.grantedState(Permission.ACCESS_FINE_LOCATION, permissions, grantResults)) {
                case DENIED:
                    getFragmentManager().popBackStack();
                    break;
                case GRANTED:
                    Prefs.setGPSEnabled(true);
                    ((MainActivity) getActivity()).fragmentReplace(R.id.main_content, new DirectionFragment());
                    break;
            }
        }
    }
}
