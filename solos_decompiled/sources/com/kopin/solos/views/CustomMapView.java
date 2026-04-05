package com.kopin.solos.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.kopin.solos.R;
import com.kopin.solos.storage.Coordinate;
import com.kopin.solos.storage.IRidePartSaved;
import com.kopin.solos.storage.Record;
import com.kopin.solos.storage.SQLHelper;
import com.kopin.solos.storage.SavedWorkout;
import com.kopin.solos.view.ExpandableCardView;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes24.dex */
public class CustomMapView extends MapView implements ExpandableCardView.LoadingView {
    private LatLng lastPosition;
    private WeakReference<Activity> mActivity;
    private volatile boolean mAvailable;
    private CameraUpdate mCameraUpdate;
    private MarkerOptions mEnd;
    private GoogleMap mGoogleMap;
    private volatile boolean mInit;
    private ExpandableCardView mParent;
    private PolylineOptions mPolyline;
    private IRidePartSaved mRide;
    private PolylineOptions mRouteline;
    private MarkerOptions mStart;
    private volatile boolean mWasAnimated;
    private GoogleMap.SnapshotReadyCallback snapshotReadyCallback;

    public CustomMapView(Context context) {
        super(context);
        this.mInit = false;
        this.mWasAnimated = false;
        this.mAvailable = true;
        if (context instanceof Activity) {
            this.mActivity = new WeakReference<>((Activity) context);
        }
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mInit = false;
        this.mWasAnimated = false;
        this.mAvailable = true;
        if (context instanceof Activity) {
            this.mActivity = new WeakReference<>((Activity) context);
        }
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mInit = false;
        this.mWasAnimated = false;
        this.mAvailable = true;
        if (context instanceof Activity) {
            this.mActivity = new WeakReference<>((Activity) context);
        }
    }

    public CustomMapView(Context context, GoogleMapOptions options) {
        super(context, options);
        this.mInit = false;
        this.mWasAnimated = false;
        this.mAvailable = true;
        if (context instanceof Activity) {
            this.mActivity = new WeakReference<>((Activity) context);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case 0:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case 1:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setRide(IRidePartSaved ride) {
        this.mRide = ride;
        this.mInit = false;
        this.mParent.onExpandableViewReady();
        if (this.mGoogleMap != null) {
            this.mGoogleMap.clear();
        }
    }

    private void initIfRequired() {
        if (!this.mInit) {
            getMapAsync(new OnMapReadyCallback() { // from class: com.kopin.solos.views.CustomMapView.1
                @Override // com.google.android.gms.maps.OnMapReadyCallback
                public void onMapReady(GoogleMap googleMap) {
                    CustomMapView.this.mGoogleMap = googleMap;
                    CustomMapView.this.init();
                }
            });
            buildMap();
            init();
            animateMap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void init() {
        if (!this.mInit && this.mGoogleMap != null && this.mCameraUpdate != null && this.mAvailable) {
            this.mInit = true;
            if (this.mParent != null) {
                this.mParent.onExpandableViewReady();
            }
            if (this.mRouteline != null) {
                this.mGoogleMap.addPolyline(this.mRouteline);
            }
            this.mGoogleMap.addPolyline(this.mPolyline);
            this.mGoogleMap.addMarker(this.mEnd);
            this.mGoogleMap.addMarker(this.mStart);
            if (this.snapshotReadyCallback != null && this.mAvailable) {
                try {
                    this.mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() { // from class: com.kopin.solos.views.CustomMapView.2
                        @Override // com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback
                        public void onMapLoaded() {
                            CustomMapView.this.mGoogleMap.snapshot(CustomMapView.this.snapshotReadyCallback);
                        }
                    });
                    return;
                } catch (Exception ex) {
                    Log.e("CustomMapView", "Error with google maps for snapshot " + ex.getMessage());
                    return;
                }
            }
            return;
        }
        if (!this.mAvailable && this.mParent != null) {
            this.mParent.setExpandableViewMessage(getResources().getString(R.string.map_unavailable));
        }
    }

    private void animateMap() {
        if (this.mInit && !this.mWasAnimated && this.mGoogleMap != null && this.mActivity != null && this.mActivity.get() != null && !this.mActivity.get().isFinishing()) {
            this.mWasAnimated = true;
            this.mGoogleMap.animateCamera(this.mCameraUpdate);
        }
    }

    private void buildMap() {
        this.lastPosition = null;
        this.mStart = null;
        final PolylineOptions rectOptions = new PolylineOptions();
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (this.mRide instanceof SavedWorkout) {
            if (((SavedWorkout) this.mRide).getRouteId() != -1) {
                SQLHelper.foreachCoord(((SavedWorkout) this.mRide).getRouteId(), new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.views.CustomMapView.3
                    @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
                    public boolean onCoordinate(Coordinate coord) {
                        LatLng pos = new LatLng(coord.getLatitude(), coord.getLongitude());
                        rectOptions.add(pos);
                        builder.include(pos);
                        if (CustomMapView.this.mStart != null) {
                            CustomMapView.this.lastPosition = pos;
                            return true;
                        }
                        CustomMapView.this.mStart = new MarkerOptions().position(pos).title(CustomMapView.this.getContext().getString(R.string.map_marker_start));
                        CustomMapView.this.mStart.icon(BitmapDescriptorFactory.defaultMarker(120.0f));
                        CustomMapView.this.mStart.anchor(0.5f, 1.0f);
                        return true;
                    }
                });
            } else {
                ((SavedWorkout) this.mRide).foreachRecord(new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.views.CustomMapView.4
                    @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
                    public boolean onRecord(Record record) {
                        if (record.hasLocation()) {
                            LatLng pos = new LatLng(record.getLatitude(), record.getLongitude());
                            rectOptions.add(pos);
                            builder.include(pos);
                            if (CustomMapView.this.mStart != null) {
                                CustomMapView.this.lastPosition = pos;
                                return true;
                            }
                            CustomMapView.this.mStart = new MarkerOptions().position(pos).title(CustomMapView.this.getContext().getString(R.string.map_marker_start));
                            CustomMapView.this.mStart.icon(BitmapDescriptorFactory.defaultMarker(120.0f));
                            CustomMapView.this.mStart.anchor(0.5f, 1.0f);
                            return true;
                        }
                        return true;
                    }
                });
            }
            if (((SavedWorkout) this.mRide).getGhostRouteId() != -1) {
                final PolylineOptions lineOpts2 = new PolylineOptions();
                SQLHelper.foreachCoord(((SavedWorkout) this.mRide).getGhostRouteId(), new SQLHelper.foreachCoordCallback() { // from class: com.kopin.solos.views.CustomMapView.5
                    @Override // com.kopin.solos.storage.SQLHelper.foreachCoordCallback
                    public boolean onCoordinate(Coordinate coord) {
                        LatLng pos = new LatLng(coord.getLatitude(), coord.getLongitude());
                        lineOpts2.add(pos);
                        return true;
                    }
                });
                this.mRouteline = lineOpts2.color(-7829368);
            }
        } else {
            this.mRide.foreachRecord(0, new SavedWorkout.foreachRecordCallback() { // from class: com.kopin.solos.views.CustomMapView.6
                @Override // com.kopin.solos.storage.SavedWorkout.foreachRecordCallback
                public boolean onRecord(Record record) {
                    if (record.hasLocation()) {
                        LatLng pos = new LatLng(record.getLatitude(), record.getLongitude());
                        rectOptions.add(pos);
                        builder.include(pos);
                        if (CustomMapView.this.mStart != null) {
                            CustomMapView.this.lastPosition = pos;
                            return true;
                        }
                        CustomMapView.this.mStart = new MarkerOptions().position(pos).title(CustomMapView.this.getContext().getString(R.string.map_marker_start));
                        CustomMapView.this.mStart.icon(BitmapDescriptorFactory.defaultMarker(120.0f));
                        CustomMapView.this.mStart.anchor(0.5f, 1.0f);
                        return true;
                    }
                    return true;
                }
            });
        }
        if (this.lastPosition != null) {
            this.mEnd = new MarkerOptions().position(this.lastPosition).title(getContext().getString(R.string.map_marker_finish));
            this.mEnd.icon(BitmapDescriptorFactory.defaultMarker(0.0f));
            this.mEnd.anchor(0.5f, 1.0f);
            LatLngBounds bounds = builder.build();
            int padding = (int) (((double) Math.min(this.mParent.getWidth(), this.mParent.getHeight())) * 0.22d);
            this.mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, this.mParent.getWidth(), this.mParent.getHeight(), padding);
            this.mWasAnimated = false;
            this.mPolyline = rectOptions.color(SupportMenu.CATEGORY_MASK);
            this.mAvailable = true;
            return;
        }
        this.mAvailable = false;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public View getView() {
        return this;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public boolean isReady() {
        initIfRequired();
        return !this.mInit;
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void onScreen() {
        animateMap();
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setParent(ExpandableCardView parent) {
        this.mParent = parent;
        this.mParent.setExpandableViewMessageView(new MapMessageView(getContext()));
        this.mParent.setExpandableViewMessage(getContext().getString(R.string.map_loading));
    }

    @Override // com.kopin.solos.view.ExpandableCardView.LoadingView
    public void setTextColor(int color) {
    }

    public static class MapMessageView implements ExpandableCardView.MessageView {
        private View mContainer;
        private ImageView mImageView;
        private TextView mTextView;

        public MapMessageView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            this.mContainer = inflater.inflate(R.layout.map_message_view, (ViewGroup) null);
            this.mTextView = (TextView) this.mContainer.findViewById(R.id.map_message_text);
            this.mImageView = (ImageView) this.mContainer.findViewById(R.id.map_message_image);
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public View getView() {
            return this.mContainer;
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public void setText(String text) {
            this.mTextView.setText(text);
        }

        @Override // com.kopin.solos.view.ExpandableCardView.MessageView
        public void setTextColor(int color) {
            this.mTextView.setTextColor(color);
            this.mImageView.setColorFilter(color);
        }
    }

    public void setSnapshotReadyCallback(GoogleMap.SnapshotReadyCallback callback) {
        this.snapshotReadyCallback = callback;
    }
}
