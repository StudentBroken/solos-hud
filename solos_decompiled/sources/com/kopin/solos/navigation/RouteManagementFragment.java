package com.kopin.solos.navigation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.digits.sdk.vcard.VCardConfig;
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
import com.kopin.solos.common.config.Config;
import com.kopin.solos.navigate.geolocation.NavigationRoute;
import com.kopin.solos.navigate.geolocation.RouteBuilder;
import com.kopin.solos.navigate.geolocation.Waypoint;
import com.kopin.solos.navigate.helperclasses.BaseMapFragment;
import com.kopin.solos.share.Sync;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.Route;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.view.swipelistview.SwipeListView;
import java.util.List;

/* JADX INFO: loaded from: classes24.dex */
public class RouteManagementFragment extends BaseMapFragment implements OnMapReadyCallback {
    private ImageView clearIcon;
    private TextView distanceUnit;
    private SwipeListView listView;
    private RoutesAdapter mAdapter;
    private MapFragment mConfirmationMapFragment;
    private Location mLocation;
    private Route.Saved mSelectedRoute;
    private NavigationRoute navigationRoute;
    private EditText navigationText;
    private LinearLayout noRoutes;
    private LinearLayout searchBox;
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
    private GoogleMap mConfirmationMap = null;
    private final Runnable input_finish_checker = new Runnable() { // from class: com.kopin.solos.navigation.RouteManagementFragment.4
        @Override // java.lang.Runnable
        public void run() {
            if (System.currentTimeMillis() > (RouteManagementFragment.this.last_text_edit + 400) - 500) {
                String s = RouteManagementFragment.this.navigationText.getText().toString();
                RouteManagementFragment.this.searchRoutes(s);
            }
        }
    };

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, com.google.android.gms.maps.MapFragment, android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        try {
            this.view = inflater.inflate(R.layout.route_management_fragment, container, false);
            if (Build.VERSION.SDK_INT <= 19) {
                this.mConfirmationMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.routemap);
            } else {
                this.mConfirmationMapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.routemap);
            }
            this.mConfirmationMapFragment.getMapAsync(this);
            this.noRoutes = (LinearLayout) this.view.findViewById(R.id.directions_no_routes);
            this.navigationText = (EditText) this.view.findViewById(R.id.navigate_text_box);
            this.searchResults = (LinearLayout) this.view.findViewById(R.id.directions_results);
            this.searchIcon = (ImageView) this.view.findViewById(R.id.directions_search_icon);
            this.clearIcon = (ImageView) this.view.findViewById(R.id.directions_clear_icon);
            this.totalDistance = (TextView) this.view.findViewById(R.id.directions_total_distance);
            this.totalTime = (TextView) this.view.findViewById(R.id.directions_total_time);
            this.distanceUnit = (TextView) this.view.findViewById(R.id.directions_distance_unit);
            this.listView = new SwipeListView(getActivity(), R.id.card_back, R.id.card_front);
            this.searchResults.addView(this.listView, -1, -2);
            this.listView.setSwipeMode(1);
            this.listView.setSwipeListViewListener(new RouteSwipeListViewListener() { // from class: com.kopin.solos.navigation.RouteManagementFragment.1
                @Override // com.kopin.solos.navigation.RouteSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
                public void onStartOpen(int position, int action, boolean right) {
                    RouteManagementFragment.this.listView.closeOpenedItems();
                    super.onStartOpen(position, action, right);
                }

                @Override // com.kopin.solos.navigation.RouteSwipeListViewListener, com.kopin.solos.view.swipelistview.SwipeListViewListener
                public void onClickFrontView(int position) {
                    RouteManagementFragment.this.already_queried = true;
                    RouteManagementFragment.this.routeDetails(position);
                    RouteManagementFragment.this.pressNavigate();
                }
            });
            this.clearIcon.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.RouteManagementFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    RouteManagementFragment.this.pressClear();
                }
            });
            this.navigationText.addTextChangedListener(new TextWatcher() { // from class: com.kopin.solos.navigation.RouteManagementFragment.3
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        RouteManagementFragment.this.searchIcon.setVisibility(0);
                        RouteManagementFragment.this.clearIcon.setVisibility(4);
                    } else {
                        RouteManagementFragment.this.searchIcon.setVisibility(4);
                        RouteManagementFragment.this.clearIcon.setVisibility(0);
                    }
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    RouteManagementFragment.this.last_text_edit = System.currentTimeMillis();
                    RouteManagementFragment.this.h.removeCallbacks(RouteManagementFragment.this.input_finish_checker);
                    if (s.length() < 3 || RouteManagementFragment.this.already_queried) {
                        RouteManagementFragment.this.already_queried = false;
                    } else {
                        RouteManagementFragment.this.h.postDelayed(RouteManagementFragment.this.input_finish_checker, 400L);
                    }
                }
            });
            this.searchConfirmLayout = (LinearLayout) this.view.findViewById(R.id.directions_confirm_layout);
            this.searchBox = (LinearLayout) this.view.findViewById(R.id.directions_search_box);
        } catch (InflateException e) {
        }
        getActivity().getActionBar().setNavigationMode(0);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setTitle(R.string.navigate_my_routes);
        setUpButtons(this.view);
        fillAdapter();
        return this.view;
    }

    public void routeDetails(int position) {
        Cursor cursor = this.mAdapter.getCursor();
        if (cursor.moveToPosition(position)) {
            this.mSelectedRoute = new Route.Saved(cursor);
        }
    }

    @Override // com.google.android.gms.maps.MapFragment, android.app.Fragment
    public void onDestroyView() {
        Fragment fragment2;
        FragmentTransaction ft;
        if (!getActivity().isFinishing()) {
            getActivity().getActionBar().setDisplayShowTitleEnabled(false);
            getActivity().getActionBar().setDisplayShowHomeEnabled(true);
            getActivity().getActionBar().setNavigationMode(1);
            if (Build.VERSION.SDK_INT <= 19) {
                fragment2 = getActivity().getFragmentManager().findFragmentById(R.id.routemap);
                ft = getActivity().getFragmentManager().beginTransaction();
            } else {
                fragment2 = getChildFragmentManager().findFragmentById(R.id.routemap);
                ft = getChildFragmentManager().beginTransaction();
            }
            ft.remove(fragment2);
            ft.commit();
        }
        super.onDestroyView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchRoutes(String s) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            this.mAdapter.changeCursor(SQLHelper.getRoutesCursor(true, s));
        }
    }

    private void fillAdapter() {
        Cursor cursor = SQLHelper.getRoutesCursor(true);
        this.mAdapter = new RoutesAdapter(getActivity(), cursor, 0);
        if (this.mAdapter.isEmpty()) {
            this.searchResults.setVisibility(8);
            this.noRoutes.setVisibility(0);
        } else {
            this.listView.setAdapter((ListAdapter) this.mAdapter);
            this.searchResults.setVisibility(0);
            this.noRoutes.setVisibility(8);
        }
    }

    private void setUpButtons(View view) {
        this.useRouteButton = (Button) view.findViewById(R.id.search_use_route);
        this.useRouteButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.RouteManagementFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RouteManagementFragment.this.pressUseRoute();
            }
        });
        this.useRouteButton.setEnabled(false);
        view.findViewById(R.id.search_cancel).setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.RouteManagementFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                RouteManagementFragment.this.pressCancel();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressUseRoute() {
        Coordinate pos;
        this.mLocation = ((MainActivity) getActivity()).getLocation();
        if (this.mLocation != null) {
            pos = new Coordinate(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        } else {
            pos = this.navigationRoute.getPreProcessRoute().get(0);
        }
        Navigator.setRoute(this.navigationRoute, pos);
        Intent intent = new Intent(getActivity(), (Class<?>) MainActivity.class);
        intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressClear() {
        this.navigationText.setText("");
        this.searchIcon.setVisibility(0);
        this.clearIcon.setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressCancel() {
        pressClear();
        this.searchConfirmLayout.setVisibility(8);
        getActivity().getActionBar().setTitle(R.string.navigate_my_routes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pressNavigate() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService("input_method");
        imm.hideSoftInputFromWindow(this.navigationText.getWindowToken(), 0);
        this.useRouteButton.setEnabled(false);
        this.searchConfirmLayout.setVisibility(0);
        this.searchConfirmLayout.setClickable(true);
        getNavigationRoute();
    }

    private void getNavigationRoute() {
        getActivity().getActionBar().setTitle(R.string.navigate_your_route);
        this.navigationRoute = RouteBuilder.create(SQLHelper.getRouteDetails(false, this.mSelectedRoute.getId()));
        if (this.navigationRoute.getPreProcessRoute().size() > 1) {
            this.navigationRoute.setId(this.mSelectedRoute.getId());
            this.navigationRoute.setDistance((int) this.mSelectedRoute.getDistance());
            displayResults();
        } else {
            Toast.makeText(getActivity(), R.string.route_not_available, 1).show();
            pressCancel();
        }
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
        this.mConfirmationMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLatLngBounds, 300, 300, 70));
        dropPinAt(wayPoints.get(0), R.drawable.ic_map_marker, "Start", R.color.navigation_green);
        dropPinAt(wayPoints.get(wayPoints.size() - 1), R.drawable.ic_flag_nav, "Finish", R.color.navigation_orange);
        if (Config.DEBUG && (waypoints = this.navigationRoute.getRoute()) != null) {
            BitmapDescriptorFactory.fromBitmap(changeImageColor(BitmapFactory.decodeResource(getResources(), R.drawable.ic_map_marker), getResources().getColor(R.color.navigation_grey)));
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
    public void onStart() {
        super.onStart();
    }

    private void displayResults() {
        drawPoly(this.navigationRoute.getPreProcessRoute(), R.color.navigation_orange, 4);
        completeDisplay();
        this.useRouteButton.setEnabled(true);
    }

    private void completeDisplay() {
        if (this.mSelectedRoute != null) {
            this.totalTime.setText(TimeDistanceHelper.secondsToString(((int) this.mSelectedRoute.getTimeToBeat()) / 1000));
            List<String> displayDistance = TimeDistanceHelper.metresToUnits(getActivity(), (int) this.mSelectedRoute.getDistance());
            this.totalDistance.setText(displayDistance.get(0));
            this.distanceUnit.setText(displayDistance.get(1));
        }
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_manage_routes, menu);
        Drawable drawable = menu.findItem(R.id.menuSearch).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(-1, PorterDuff.Mode.SRC_ATOP);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override // com.kopin.solos.navigate.helperclasses.BaseMapFragment, android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddRoute /* 2131952549 */:
                showSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSearch() {
        if (this.searchBox.getVisibility() == 0) {
            this.searchBox.setVisibility(8);
        } else {
            this.searchBox.setVisibility(0);
        }
    }

    @Override // com.google.android.gms.maps.OnMapReadyCallback
    public void onMapReady(GoogleMap googleMap) {
        this.mConfirmationMap = googleMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRoute(long routeId) {
        this.listView.closeOpenedItems();
        Sync.removeRoute(routeId);
        fillAdapter();
    }

    private class RoutesAdapter extends CursorAdapter {
        public RoutesAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, 0);
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return new RouteCardView(context);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View view, Context context, Cursor cursor) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Route.TITLE));
            long routeId = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(Route.DISTANCE));
            List<String> distanceUnits = TimeDistanceHelper.metresToFullUnits(context, distance);
            RouteCardView expandableCardView = (RouteCardView) view;
            expandableCardView.setTheme(cursor.getPosition() % 2 == 0);
            expandableCardView.setTitle(title);
            expandableCardView.setDistance(distanceUnits);
            expandableCardView.disableOnClick();
            ImageButton deleteRideButton = (ImageButton) expandableCardView.findViewById(R.id.btnDeleteRide);
            deleteRideButton.setVisibility(0);
            deleteRideButton.setTag(Long.valueOf(routeId));
            deleteRideButton.setOnClickListener(new View.OnClickListener() { // from class: com.kopin.solos.navigation.RouteManagementFragment.RoutesAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    long route = ((Long) v.getTag()).longValue();
                    RouteManagementFragment.this.deleteRoute(route);
                }
            });
        }
    }
}
