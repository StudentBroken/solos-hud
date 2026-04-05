package com.kopin.solos.debug;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.pupil.ConnectionManager;
import com.kopin.pupil.PupilDevice;
import com.kopin.solos.AppService;
import com.kopin.solos.Fragments.StartStopFragment;
import com.kopin.solos.HardwareReceiverService;
import com.kopin.solos.MainActivity;
import com.kopin.solos.R;
import com.kopin.solos.RideControl;
import com.kopin.solos.common.SafeFragment;
import com.kopin.solos.debug.RidePickerDialog;
import com.kopin.solos.debug.RoutePickerDialog;
import com.kopin.solos.menu.CustomActionProvider;
import com.kopin.solos.menu.CustomMenuItem;
import com.kopin.solos.menu.TextMenuAdapter;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigation.Navigator;
import com.kopin.solos.share.Config;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.LiveRide;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.storage.Workout;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class DebugFragment extends SafeFragment {
    private static final int CANCEL_ID = 255;
    private static final int RIDE_REPLAY_ID = 252;
    private static final int ROUTE_REPLAY_ID = 254;
    private static final int START_NAV_REPLAY_ID = 253;
    private static final int START_REPLAY_ID = 251;
    private ImageView imgScreenWatcher;
    private TextMenuAdapter.TextMenuItem mCancelReplay;
    private Location mCurLocation;
    private Marker mHere;
    private GoogleMap mMap;
    private MapFragment mMapFragment;
    private TextMenuAdapter.TextMenuItem mPickReplay;
    private TextMenuAdapter.TextMenuItem mPickRoute;
    private CustomActionProvider.DefaultActionView mReplayIcon;
    private CustomActionProvider<TextMenuAdapter.TextMenuItem> mReplayProvider;
    private HardwareReceiverService mService;
    private TextMenuAdapter.TextMenuItem mStartNavReplay;
    private TextMenuAdapter.TextMenuItem mStartReplay;
    private AppService mVCApp;
    private final RidePickerDialog.OnRideSelectedListener mRideSelectListener = new RidePickerDialog.OnRideSelectedListener() { // from class: com.kopin.solos.debug.DebugFragment.4
        @Override // com.kopin.solos.debug.RidePickerDialog.OnRideSelectedListener
        public void onRideSelected(long rideId) {
            RideReplayer.selectRide(rideId);
        }
    };
    private final RoutePickerDialog.OnRouteSelectedListener mRouteSelectListener = new RoutePickerDialog.OnRouteSelectedListener() { // from class: com.kopin.solos.debug.DebugFragment.5
        @Override // com.kopin.solos.debug.RoutePickerDialog.OnRouteSelectedListener
        public void onRouteSelected(long routeId) {
            RideReplayer.selectRoute(routeId);
        }
    };
    private final Runnable mUpdateHere = new Runnable() { // from class: com.kopin.solos.debug.DebugFragment.7
        @Override // java.lang.Runnable
        public void run() {
            if (DebugFragment.this.isStillRequired()) {
                DebugFragment.this.mCurLocation = ((MainActivity) DebugFragment.this.getActivity()).getLocation();
                if (DebugFragment.this.mMap != null && LiveRide.isActiveRide() && !LiveRide.isActiveFtp()) {
                    DebugFragment.this.resetMap();
                    DebugFragment.this.drawRide();
                }
                if (DebugFragment.this.mHere != null && DebugFragment.this.mCurLocation != null) {
                    DebugFragment.this.mHere.setPosition(new LatLng(DebugFragment.this.mCurLocation.getLatitude(), DebugFragment.this.mCurLocation.getLongitude()));
                }
                DebugFragment.this.mHandler.postDelayed(this, 2000L);
            }
        }
    };
    private final OnMapReadyCallback mMapReadyCallback = new OnMapReadyCallback() { // from class: com.kopin.solos.debug.DebugFragment.8
        @Override // com.google.android.gms.maps.OnMapReadyCallback
        public void onMapReady(GoogleMap googleMap) {
            DebugFragment.this.mMap = googleMap;
            DebugFragment.this.mMap.setOnMarkerClickListener(DebugFragment.this.mMarkerSelected);
            DebugFragment.this.mCurLocation = ((MainActivity) DebugFragment.this.getActivity()).getLocation();
            DebugFragment.this.resetMap();
            DebugFragment.this.drawRide();
        }
    };
    private final GoogleMap.OnMarkerClickListener mMarkerSelected = new GoogleMap.OnMarkerClickListener() { // from class: com.kopin.solos.debug.DebugFragment.9
        @Override // com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
        public boolean onMarkerClick(Marker marker) {
            Waypoint wp = DebugFragment.this.findWaypointForMarker(marker);
            if (wp != null) {
            }
            return false;
        }
    };
    private final RideControl.RideObserver mRideObserver = new RideControl.RideObserver() { // from class: com.kopin.solos.debug.DebugFragment.10
        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideConfig(Workout.RideMode mode, long rideOrRouteId) {
            if (DebugFragment.this.mMap != null && DebugFragment.this.isStillRequired()) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                if (DebugFragment.this.drawRoute(builder, mode, rideOrRouteId)) {
                    LatLngBounds mLatLngBounds = builder.build();
                    DebugFragment.this.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 70));
                }
            }
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideIdle() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideReady(RideControl.StartMode startMode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStarted(Workout.RideMode mode) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public boolean okToStop() {
            return true;
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideStopped(SavedWorkout ride) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRidePaused(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onRideResumed(boolean userOrAuto) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onNewLap(double lastDistance, long lastTime) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownStarted() {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdown(int counter) {
        }

        @Override // com.kopin.solos.RideControl.RideObserver
        public void onCountdownComplete(boolean wasCancelled) {
        }
    };
    private final Connection.PacketWatcher mPacketWatcher = new Connection.PacketWatcher() { // from class: com.kopin.solos.debug.DebugFragment.11
        @Override // com.kopin.accessory.base.Connection.PacketWatcher
        public void onPacketSent(Connection connection, Packet packet) {
            Log.d("Solos", ">> " + packet);
        }

        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            Log.d("Solos", "<< " + packet);
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private long mThreadId = Looper.getMainLooper().getThread().getId();

    public DebugFragment() {
        RideControl.registerObserver(this.mRideObserver);
    }

    @Override // android.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debug, container, false);
        setupScreenWatcherViews(view);
        setupMap(view);
        return view;
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mReplayIcon = new CustomActionProvider.DefaultActionView(getActivity());
        this.mReplayIcon.setActiveColor(getResources().getColor(R.color.app_actionbar_divider));
        this.mReplayIcon.setInactiveColor(getResources().getColor(R.color.unfocused_grey));
        this.mReplayProvider = new CustomActionProvider<>(getActivity());
        this.mPickReplay = new TextMenuAdapter.TextMenuItem(getString(R.string.navigation_menu_item_ride), RIDE_REPLAY_ID, TextMenuAdapter.TextMenuType.MEDIUM);
        this.mPickReplay.setDismissOnTap(true);
        this.mStartReplay = new TextMenuAdapter.TextMenuItem(getString(R.string.option_debug_start), START_REPLAY_ID, TextMenuAdapter.TextMenuType.MEDIUM);
        this.mStartReplay.setDismissOnTap(true);
        this.mPickRoute = new TextMenuAdapter.TextMenuItem(getString(R.string.navigation_menu_item_route), ROUTE_REPLAY_ID, TextMenuAdapter.TextMenuType.MEDIUM);
        this.mPickRoute.setDismissOnTap(true);
        this.mStartNavReplay = new TextMenuAdapter.TextMenuItem(getString(R.string.navigation_menu_use_route), START_NAV_REPLAY_ID, TextMenuAdapter.TextMenuType.MEDIUM);
        this.mStartNavReplay.setDismissOnTap(true);
        this.mCancelReplay = new TextMenuAdapter.TextMenuItem(getString(android.R.string.cancel), 255, TextMenuAdapter.TextMenuType.MEDIUM);
        this.mCancelReplay.setDismissOnTap(true);
        setHasOptionsMenu(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StartStopFragment.ServiceActivity) {
            ((StartStopFragment.ServiceActivity) activity).getService(new StartStopFragment.ServiceCallback() { // from class: com.kopin.solos.debug.DebugFragment.1
                @Override // com.kopin.solos.Fragments.StartStopFragment.ServiceCallback
                public void onService(HardwareReceiverService service) {
                    DebugFragment.this.mService = service;
                    DebugFragment.this.mVCApp = service.getAppService();
                }
            });
        }
        this.mHandler.postDelayed(this.mUpdateHere, 500L);
    }

    @Override // android.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mService = null;
        this.mVCApp = null;
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.mReplayProvider.setMenuAdapter(new TextMenuAdapter(getActivity()));
        this.mReplayProvider.setActionView(this.mReplayIcon);
        this.mReplayProvider.setOnPrepareListener(new CustomActionProvider.OnPrepareListener<TextMenuAdapter.TextMenuItem>() { // from class: com.kopin.solos.debug.DebugFragment.2
            @Override // com.kopin.solos.menu.CustomActionProvider.OnPrepareListener
            public void onPrepare(CustomActionProvider<TextMenuAdapter.TextMenuItem> actionProvider) {
                DebugFragment.this.mReplayIcon.setActive(LiveRide.isActiveRide());
                if (LiveRide.isActiveRide()) {
                    DebugFragment.this.mReplayProvider.addMenuItem(DebugFragment.this.mCancelReplay);
                    DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mPickReplay);
                    DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mStartReplay);
                    DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mPickRoute);
                    DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mStartNavReplay);
                    return;
                }
                DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mCancelReplay);
                DebugFragment.this.mReplayProvider.addMenuItem(DebugFragment.this.mPickReplay);
                if (RideReplayer.ready()) {
                    DebugFragment.this.mReplayProvider.addMenuItem(DebugFragment.this.mStartReplay);
                    if (LiveRide.isNavigtionRideMode()) {
                        DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mPickRoute);
                        return;
                    } else {
                        DebugFragment.this.mReplayProvider.addMenuItem(DebugFragment.this.mPickRoute);
                        return;
                    }
                }
                DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mStartReplay);
                DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mStartNavReplay);
                if (LiveRide.isNavigtionRideMode()) {
                    DebugFragment.this.mReplayProvider.removeMenuItem(DebugFragment.this.mPickRoute);
                } else {
                    DebugFragment.this.mReplayProvider.addMenuItem(DebugFragment.this.mPickRoute);
                }
            }
        });
        this.mReplayProvider.setOnItemClickListener(new CustomActionProvider.OnItemClickListener() { // from class: com.kopin.solos.debug.DebugFragment.3
            @Override // com.kopin.solos.menu.CustomActionProvider.OnItemClickListener
            public void onItemClick(int position, CustomMenuItem menuItem) {
                switch (menuItem.getId()) {
                    case DebugFragment.START_REPLAY_ID /* 251 */:
                        RideReplayer.start();
                        DebugFragment.this.mReplayIcon.setActive(true);
                        break;
                    case DebugFragment.RIDE_REPLAY_ID /* 252 */:
                        Dialog ridePicker = RidePickerDialog.create(DebugFragment.this.getActivity(), false, DebugFragment.this.mRideSelectListener);
                        ridePicker.show();
                        break;
                    case DebugFragment.START_NAV_REPLAY_ID /* 253 */:
                        RideReplayer.start();
                        DebugFragment.this.mReplayIcon.setActive(true);
                        break;
                    case DebugFragment.ROUTE_REPLAY_ID /* 254 */:
                        Dialog routePicker = RoutePickerDialog.create(DebugFragment.this.getActivity(), false, DebugFragment.this.mRouteSelectListener);
                        routePicker.show();
                        break;
                    case 255:
                        RideReplayer.stop();
                        DebugFragment.this.mReplayIcon.setActive(false);
                        break;
                }
            }
        });
        inflater.inflate(R.menu.record_menu, menu);
        menu.findItem(R.id.menu_ride_mode).setActionProvider(this.mReplayProvider);
        menu.findItem(R.id.menu_warnings).setVisible(false);
        menu.findItem(R.id.menu_group_com).setVisible(false);
        menu.findItem(R.id.menu_show_sensors).setVisible(false);
    }

    private void setScreenWatcher(boolean watchScreen) {
        if (isStillRequired()) {
            if (this.imgScreenWatcher != null) {
                this.imgScreenWatcher.setVisibility((Config.SHOW_GLASSES_MIRROR && watchScreen) ? 0 : 8);
            }
            if (this.mService != null && this.mService.getAppService() != null) {
                this.mService.getAppService().setScreenWatcher((Config.SHOW_GLASSES_MIRROR && watchScreen) ? new ConnectionManager.ScreenWatcher() { // from class: com.kopin.solos.debug.DebugFragment.6
                    @Override // com.kopin.pupil.ConnectionManager.ScreenWatcher
                    public void onScreenUpdated(final Bitmap bitmap, final Rect updateRect) {
                        if (DebugFragment.this.isStillRequired() && bitmap != null) {
                            DebugFragment.this.runOnMainThread(new Runnable() { // from class: com.kopin.solos.debug.DebugFragment.6.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (DebugFragment.this.isStillRequired() && DebugFragment.this.imgScreenWatcher != null) {
                                        if (updateRect != null) {
                                            Paint brush = new Paint();
                                            brush.setColor(SupportMenu.CATEGORY_MASK);
                                            brush.setStrokeWidth(2.0f);
                                            brush.setStyle(Paint.Style.STROKE);
                                            Canvas canvas = new Canvas(bitmap);
                                            canvas.drawRect(updateRect, brush);
                                        }
                                        DebugFragment.this.imgScreenWatcher.setImageBitmap(bitmap);
                                    }
                                }
                            });
                        }
                    }
                } : null);
            }
        }
    }

    private void setupScreenWatcherViews(View view) {
        this.imgScreenWatcher = (ImageView) view.findViewById(R.id.imgScreenWatcher);
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onResume() {
        super.onResume();
        setScreenWatcher(true);
        if (PupilDevice.isConnected()) {
            PupilDevice.setDebugPacketWatcher(this.mPacketWatcher);
        }
    }

    @Override // com.kopin.solos.common.SafeFragment, android.app.Fragment
    public void onPause() {
        super.onPause();
        setScreenWatcher(false);
        if (PupilDevice.isConnected()) {
            PupilDevice.setDebugPacketWatcher(null);
        }
    }

    public void runOnMainThread(Runnable runnable) {
        if (Thread.currentThread().getId() != this.mThreadId) {
            this.mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private void setupMap(View root) {
        try {
            if (Build.VERSION.SDK_INT <= 19) {
                this.mMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            } else {
                this.mMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            }
            this.mMapFragment.getMapAsync(this.mMapReadyCallback);
        } catch (InflateException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean drawRoute(LatLngBounds.Builder builder, Workout.RideMode mode, long id) {
        switch (mode) {
            case ROUTE:
                boolean hasRoute = drawPoly(builder, Navigator.getRouteCoordinates(), R.color.navigation_grey, 6, true);
                if (hasRoute) {
                    drawPins(Navigator.getWaypoints(), Navigator.getNextWaypoint(), false, false);
                    return hasRoute;
                }
                return hasRoute;
            case GHOST_RIDE:
                if (id == -1) {
                    return false;
                }
                long routeId = Route.getRouteIdForRide(id);
                if (routeId == -1) {
                    return false;
                }
                boolean hasRoute2 = drawPoly(builder, Route.getRouteCoordinates(routeId), R.color.navigation_grey, 6, true);
                return hasRoute2;
            case NORMAL:
                if (id == -1) {
                    return false;
                }
                boolean hasRoute3 = drawPoly(builder, Route.getRouteCoordinates(id), R.color.navigation_green, 3, false);
                return hasRoute3;
            default:
                resetMap();
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawRide() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (LiveRide.isGhostWorkout()) {
            drawRoute(builder, Workout.RideMode.GHOST_RIDE, LiveRide.getVirtualWorkoutId());
        } else if (LiveRide.isNavigtionRideMode()) {
            drawRoute(builder, Workout.RideMode.ROUTE, -1L);
        }
        if (LiveRide.isActiveRide()) {
            drawRoute(builder, Workout.RideMode.NORMAL, LiveRide.getCurrentRide().getRouteId());
        }
        if (this.mCurLocation != null) {
            builder.include(new LatLng(this.mCurLocation.getLatitude(), this.mCurLocation.getLongitude()));
        }
        try {
            LatLngBounds mLatLngBounds = builder.build();
            this.mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 70));
        } catch (IllegalStateException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetMap() {
        LatLng latLng;
        this.mMap.clear();
        if (this.mCurLocation != null) {
            latLng = new LatLng(this.mCurLocation.getLatitude(), this.mCurLocation.getLongitude());
        } else {
            latLng = new LatLng(52.932456771078044d, -1.1377499888795635d);
        }
        this.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        this.mHere = dropPinAt(latLng, R.drawable.ic_bike, "Here", R.color.solos_orange_dark);
    }

    private boolean drawPoly(LatLngBounds.Builder builder, List<Coordinate> wayPoints, int colour, int theWidth, boolean markEnd) {
        if (wayPoints.isEmpty()) {
            return false;
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        for (Coordinate w : wayPoints) {
            LatLng l = new LatLng(w.getLatitude(), w.getLongitude());
            polylineOptions.add(l);
            builder.include(l);
        }
        int lineColour = getResources().getColor(colour);
        this.mMap.addPolyline(polylineOptions.color(lineColour).width(theWidth));
        dropPinAt(wayPoints.get(0), R.drawable.ic_map_marker, "Start", R.color.navigation_green);
        if (markEnd) {
            dropPinAt(wayPoints.get(wayPoints.size() - 1), R.drawable.ic_flag_nav, "Finish", R.color.navigation_orange);
        }
        return true;
    }

    private void drawPins(List<Waypoint> waypoints, boolean markStart, boolean markEnd) {
        drawPins(waypoints, null, markStart, markEnd);
    }

    private void drawPins(List<Waypoint> waypoints, Waypoint highlight, boolean markStart, boolean markEnd) {
        if (waypoints.size() >= 2) {
            int last = waypoints.size() - 1;
            if (markStart) {
                dropPinAt(waypoints.get(0), R.drawable.ic_map_marker, "Start", R.color.navigation_green);
            }
            for (int i = 1; i < last; i++) {
                Waypoint wp = waypoints.get(i);
                if (highlight != null && highlight.equals((Coordinate) wp)) {
                    dropPinAt(wp, R.drawable.ic_map_marker, wp.instruction, R.color.navigation_green);
                } else {
                    dropPinAt(wp, R.drawable.ic_map_marker, wp.instruction, R.color.navigation_grey);
                }
            }
            if (markEnd) {
                dropPinAt(waypoints.get(last), R.drawable.ic_flag_nav, "Finish", R.color.navigation_orange);
            }
        }
    }

    private void dropPinAt(Coordinate point, int PinIcon, String s, int colour) {
        dropPinAt(new LatLng(point.getLatitude(), point.getLongitude()), PinIcon, s, colour);
    }

    private Marker dropPinAt(LatLng latlng, int PinIcon, String s, int colour) {
        Bitmap marker = BitmapFactory.decodeResource(getResources(), PinIcon);
        float theXValue = 0.5f;
        if (PinIcon == R.drawable.ic_flag_nav) {
            theXValue = 1.0f;
        }
        return this.mMap.addMarker(new MarkerOptions().position(latlng).title(s).snippet(s).anchor(theXValue, 1.0f).icon(BitmapDescriptorFactory.fromBitmap(changeImageColor(marker, getResources().getColor(colour)))));
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
    public Waypoint findWaypointForMarker(Marker marker) {
        LatLng pos = marker.getPosition();
        List<Waypoint> wps = Navigator.getWaypoints();
        for (Waypoint wp : wps) {
            if (wp.equals(pos)) {
                return wp;
            }
        }
        return null;
    }
}
