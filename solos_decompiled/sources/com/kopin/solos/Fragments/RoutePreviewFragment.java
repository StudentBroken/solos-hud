package com.kopin.solos.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.digits.sdk.vcard.VCardConfig;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.common.config.Config;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.RouteBuilder;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.navigation.TimeDistanceHelper;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.util.Utility;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class RoutePreviewFragment extends WorkoutFragment {
    private Button cancelButton;
    private TextView distanceUnit;
    private LatLng lastPosition;
    private Handler mBuilderHandler;
    private CameraUpdate mCameraUpdate;
    private Context mContext;
    private MarkerOptions mEnd;
    private final Runnable mMapBuilder = new Runnable() { // from class: com.kopin.solos.Fragments.RoutePreviewFragment.4
        @Override // java.lang.Runnable
        public void run() {
            List<Waypoint> wayPoints;
            RoutePreviewFragment.this.startPosition = null;
            RoutePreviewFragment.this.lastPosition = null;
            final PolylineOptions rectOptions = new PolylineOptions();
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            final List<Coordinate> initialCoordinateList = new ArrayList<>();
            SQLHelper.foreachCoord(RoutePreviewFragment.this.mWorkout.getRouteId(), new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.Fragments.RoutePreviewFragment.4.1
                @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
                public boolean onCoordinate(Coordinate coord) {
                    LatLng pos = new LatLng(coord.getLatitude(), coord.getLongitude());
                    if (RoutePreviewFragment.this.startPosition == null) {
                        RoutePreviewFragment.this.startPosition = pos;
                    } else {
                        RoutePreviewFragment.this.lastPosition = pos;
                    }
                    initialCoordinateList.add(new Coordinate(pos.latitude, pos.longitude));
                    rectOptions.add(pos);
                    builder.include(pos);
                    return true;
                }
            });
            RoutePreviewFragment.this.navigationRoute = RouteBuilder.create(initialCoordinateList);
            RoutePreviewFragment.this.navigationRoute.setId(RoutePreviewFragment.this.mWorkout.getRouteId());
            RoutePreviewFragment.this.navigationRoute.setDistance((int) RoutePreviewFragment.this.mWorkout.getDistance());
            long sTime = RoutePreviewFragment.this.mWorkout.getStartTime();
            long eTime = RoutePreviewFragment.this.mWorkout.getEndTime();
            RoutePreviewFragment.this.navigationRoute.setTime((int) (eTime - sTime));
            if (RoutePreviewFragment.this.lastPosition != null && RoutePreviewFragment.this.isStillRequired()) {
                RoutePreviewFragment.this.mStart = new MarkerOptions().position(RoutePreviewFragment.this.startPosition).title("").snippet("").icon(BitmapDescriptorFactory.fromBitmap(RoutePreviewFragment.changeImageColor(BitmapFactory.decodeResource(RoutePreviewFragment.this.getResources(), R.drawable.ic_map_marker), RoutePreviewFragment.this.getResources().getColor(R.color.navigation_green))));
                RoutePreviewFragment.this.mEnd = new MarkerOptions().position(RoutePreviewFragment.this.lastPosition).title("").snippet("").anchor(1.0f, 1.0f).icon(BitmapDescriptorFactory.fromBitmap(RoutePreviewFragment.changeImageColor(BitmapFactory.decodeResource(RoutePreviewFragment.this.getResources(), R.drawable.ic_flag_nav), RoutePreviewFragment.this.getResources().getColor(R.color.navigation_orange))));
                RoutePreviewFragment.this.mNavbounds = builder.build();
                RoutePreviewFragment.this.mCameraUpdate = CameraUpdateFactory.newLatLngBounds(RoutePreviewFragment.this.mNavbounds, 300, 300, 0);
                RoutePreviewFragment.this.mPolyline = rectOptions.color(SupportMenu.CATEGORY_MASK);
                RoutePreviewFragment.this.navMap.addPolyline(RoutePreviewFragment.this.mPolyline);
                RoutePreviewFragment.this.navMap.addMarker(RoutePreviewFragment.this.mEnd);
                RoutePreviewFragment.this.navMap.addMarker(RoutePreviewFragment.this.mStart);
                RoutePreviewFragment.this.navMap.animateCamera(RoutePreviewFragment.this.mCameraUpdate);
                if (Config.DEBUG && (wayPoints = RoutePreviewFragment.this.navigationRoute.getRoute()) != null) {
                    BitmapDescriptor pin = BitmapDescriptorFactory.fromBitmap(RoutePreviewFragment.changeImageColor(BitmapFactory.decodeResource(RoutePreviewFragment.this.getResources(), R.drawable.ic_map_marker), RoutePreviewFragment.this.getResources().getColor(R.color.navigation_grey)));
                    for (Waypoint wp : wayPoints) {
                        if (!wp.isStart && !wp.isEnd) {
                            LatLng pos = new LatLng(wp.getLatitude(), wp.getLongitude());
                            MarkerOptions mapPin = new MarkerOptions().position(pos).title(wp.maneuver).snippet(wp.instruction).icon(pin);
                            RoutePreviewFragment.this.navMap.addMarker(mapPin);
                        }
                    }
                }
                RoutePreviewFragment.this.completeDisplay();
                RoutePreviewFragment.this.setUpButtons();
            }
        }
    };
    private View mMapLoadingView;
    private MapView mNavMapView;
    private LatLngBounds mNavbounds;
    private PolylineOptions mPolyline;
    private MarkerOptions mStart;
    private GoogleMap navMap;
    private NavigationRoute navigationRoute;
    private LatLng startPosition;
    private TextView totalDistance;
    private TextView totalTime;
    private Button useRouteButton;

    public static RoutePreviewFragment newInstance(Bundle args) {
        RoutePreviewFragment fragment = new RoutePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override // com.kopin.solos.Fragments.WorkoutFragment, com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mWorkout != null && isStillRequired() && this.mBuilderHandler != null) {
            this.mBuilderHandler.postDelayed(this.mMapBuilder, 1000L);
        }
        if (this.mNavMapView != null && isStillRequired()) {
            this.mNavMapView.onResume();
        }
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        this.mNavMapView.onPause();
        this.mBuilderHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mNavMapView.onDestroy();
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mNavMapView.onSaveInstanceState(outState);
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        this.mNavMapView.onLowMemory();
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route_preview, container, false);
        if (!isFinishing()) {
            this.mBuilderHandler = new Handler();
            this.totalDistance = (TextView) view.findViewById(R.id.directions_total_distance);
            this.totalTime = (TextView) view.findViewById(R.id.directions_total_time);
            this.distanceUnit = (TextView) view.findViewById(R.id.directions_distance_unit);
            this.mNavMapView = (MapView) view.findViewById(R.id.preview_minimap);
            this.mNavMapView.onCreate(savedInstanceState);
            this.mMapLoadingView = view.findViewById(R.id.preview_loading);
            this.mNavMapView.getMapAsync(new OnMapReadyCallback() { // from class: com.kopin.solos.Fragments.RoutePreviewFragment.1
                @Override // com.google.android.gms.maps.OnMapReadyCallback
                public void onMapReady(GoogleMap googleMap) {
                    RoutePreviewFragment.this.navMap = googleMap;
                    RoutePreviewFragment.this.navMap.getUiSettings().setMyLocationButtonEnabled(false);
                    RoutePreviewFragment.this.navMap.setMyLocationEnabled(true);
                }
            });
            this.useRouteButton = (Button) view.findViewById(R.id.search_use_route);
            this.cancelButton = (Button) view.findViewById(R.id.search_cancel);
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpButtons() {
        this.useRouteButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.RoutePreviewFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RoutePreviewFragment.this.pressUseRoute();
            }
        });
        this.useRouteButton.setEnabled(true);
        this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.Fragments.RoutePreviewFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RoutePreviewFragment.this.pressCancel();
            }
        });
        this.cancelButton.setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressUseRoute() {
        Navigator.setRoute(this.navigationRoute, new Coordinate(this.startPosition));
        Intent intent = new Intent(this.mContext, (Class<?>) MainActivity.class);
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressCancel() {
        this.startPosition = null;
        getActivity().onBackPressed();
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void completeDisplay() {
        this.mNavMapView.setVisibility(0);
        this.mMapLoadingView.setVisibility(8);
        int totalDistanceValue = this.navigationRoute.getDistance();
        this.totalTime.setText(Utility.formatTime(this.mWorkout.getDuration(), true));
        List<String> displayDistance = TimeDistanceHelper.metresToUnits(getActivity(), totalDistanceValue);
        this.totalDistance.setText(displayDistance.get(0));
        this.distanceUnit.setText(displayDistance.get(1));
    }

    private static Coordinate convert(Record r) {
        Coordinate c = new Coordinate(0.0d, 0.0d);
        c.setLatitude(r.getLatitude());
        c.setLongitude(r.getLongitude());
        c.setAltitude(r.getAltitude());
        return c;
    }

    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity.getBaseContext();
    }
}
