package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.support.annotation.RequiresPermission;
import android.view.View;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

/* JADX INFO: loaded from: classes10.dex */
public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate zzblB;
    private UiSettings zzblC;

    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    public interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    @Deprecated
    public interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    public interface OnCameraIdleListener {
        void onCameraIdle();
    }

    public interface OnCameraMoveCanceledListener {
        void onCameraMoveCanceled();
    }

    public interface OnCameraMoveListener {
        void onCameraMove();
    }

    public interface OnCameraMoveStartedListener {
        public static final int REASON_API_ANIMATION = 2;
        public static final int REASON_DEVELOPER_ANIMATION = 3;
        public static final int REASON_GESTURE = 1;

        void onCameraMoveStarted(int i);
    }

    public interface OnCircleClickListener {
        void onCircleClick(Circle circle);
    }

    public interface OnGroundOverlayClickListener {
        void onGroundOverlayClick(GroundOverlay groundOverlay);
    }

    public interface OnIndoorStateChangeListener {
        void onIndoorBuildingFocused();

        void onIndoorLevelActivated(IndoorBuilding indoorBuilding);
    }

    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    public interface OnInfoWindowCloseListener {
        void onInfoWindowClose(Marker marker);
    }

    public interface OnInfoWindowLongClickListener {
        void onInfoWindowLongClick(Marker marker);
    }

    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    public interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    public interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    public interface OnPoiClickListener {
        void onPoiClick(PointOfInterest pointOfInterest);
    }

    public interface OnPolygonClickListener {
        void onPolygonClick(Polygon polygon);
    }

    public interface OnPolylineClickListener {
        void onPolylineClick(Polyline polyline);
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    static final class zza extends com.google.android.gms.maps.internal.zzd {
        private final CancelableCallback zzbmc;

        zza(CancelableCallback cancelableCallback) {
            this.zzbmc = cancelableCallback;
        }

        @Override // com.google.android.gms.maps.internal.zzc
        public final void onCancel() {
            this.zzbmc.onCancel();
        }

        @Override // com.google.android.gms.maps.internal.zzc
        public final void onFinish() {
            this.zzbmc.onFinish();
        }
    }

    public GoogleMap(IGoogleMapDelegate iGoogleMapDelegate) {
        this.zzblB = (IGoogleMapDelegate) zzbr.zzu(iGoogleMapDelegate);
    }

    public final Circle addCircle(CircleOptions circleOptions) {
        try {
            return new Circle(this.zzblB.addCircle(circleOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        try {
            com.google.android.gms.maps.model.internal.zzg zzgVarAddGroundOverlay = this.zzblB.addGroundOverlay(groundOverlayOptions);
            if (zzgVarAddGroundOverlay != null) {
                return new GroundOverlay(zzgVarAddGroundOverlay);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Marker addMarker(MarkerOptions markerOptions) {
        try {
            com.google.android.gms.maps.model.internal.zzp zzpVarAddMarker = this.zzblB.addMarker(markerOptions);
            if (zzpVarAddMarker != null) {
                return new Marker(zzpVarAddMarker);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polygon addPolygon(PolygonOptions polygonOptions) {
        try {
            return new Polygon(this.zzblB.addPolygon(polygonOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions polylineOptions) {
        try {
            return new Polyline(this.zzblB.addPolyline(polylineOptions));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        try {
            com.google.android.gms.maps.model.internal.zzw zzwVarAddTileOverlay = this.zzblB.addTileOverlay(tileOverlayOptions);
            if (zzwVarAddTileOverlay != null) {
                return new TileOverlay(zzwVarAddTileOverlay);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzblB.animateCamera(cameraUpdate.zzwd());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, int i, CancelableCallback cancelableCallback) {
        try {
            this.zzblB.animateCameraWithDurationAndCallback(cameraUpdate.zzwd(), i, cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback) {
        try {
            this.zzblB.animateCameraWithCallback(cameraUpdate.zzwd(), cancelableCallback == null ? null : new zza(cancelableCallback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.zzblB.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.zzblB.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final IndoorBuilding getFocusedBuilding() {
        try {
            com.google.android.gms.maps.model.internal.zzj focusedBuilding = this.zzblB.getFocusedBuilding();
            if (focusedBuilding != null) {
                return new IndoorBuilding(focusedBuilding);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getMapType() {
        try {
            return this.zzblB.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.zzblB.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.zzblB.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.zzblB.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.zzblB.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.zzblC == null) {
                this.zzblC = new UiSettings(this.zzblB.getUiSettings());
            }
            return this.zzblC;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isBuildingsEnabled() {
        try {
            return this.zzblB.isBuildingsEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.zzblB.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.zzblB.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.zzblB.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate cameraUpdate) {
        try {
            this.zzblB.moveCamera(cameraUpdate.zzwd());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void resetMinMaxZoomPreference() {
        try {
            this.zzblB.resetMinMaxZoomPreference();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBuildingsEnabled(boolean z) {
        try {
            this.zzblB.setBuildingsEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setContentDescription(String str) {
        try {
            this.zzblB.setContentDescription(str);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean z) {
        try {
            return this.zzblB.setIndoorEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter) {
        try {
            if (infoWindowAdapter == null) {
                this.zzblB.setInfoWindowAdapter(null);
            } else {
                this.zzblB.setInfoWindowAdapter(new zzg(this, infoWindowAdapter));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) {
        try {
            this.zzblB.setLatLngBoundsForCameraTarget(latLngBounds);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setLocationSource(LocationSource locationSource) {
        try {
            if (locationSource == null) {
                this.zzblB.setLocationSource(null);
            } else {
                this.zzblB.setLocationSource(new zzl(this, locationSource));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setMapStyle(MapStyleOptions mapStyleOptions) {
        try {
            return this.zzblB.setMapStyle(mapStyleOptions);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMapType(int i) {
        try {
            this.zzblB.setMapType(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMaxZoomPreference(float f) {
        try {
            this.zzblB.setMaxZoomPreference(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMinZoomPreference(float f) {
        try {
            this.zzblB.setMinZoomPreference(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public final void setMyLocationEnabled(boolean z) {
        try {
            this.zzblB.setMyLocationEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
        try {
            if (onCameraChangeListener == null) {
                this.zzblB.setOnCameraChangeListener(null);
            } else {
                this.zzblB.setOnCameraChangeListener(new zzs(this, onCameraChangeListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraIdleListener(OnCameraIdleListener onCameraIdleListener) {
        try {
            if (onCameraIdleListener == null) {
                this.zzblB.setOnCameraIdleListener(null);
            } else {
                this.zzblB.setOnCameraIdleListener(new zzw(this, onCameraIdleListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraMoveCanceledListener(OnCameraMoveCanceledListener onCameraMoveCanceledListener) {
        try {
            if (onCameraMoveCanceledListener == null) {
                this.zzblB.setOnCameraMoveCanceledListener(null);
            } else {
                this.zzblB.setOnCameraMoveCanceledListener(new zzv(this, onCameraMoveCanceledListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraMoveListener(OnCameraMoveListener onCameraMoveListener) {
        try {
            if (onCameraMoveListener == null) {
                this.zzblB.setOnCameraMoveListener(null);
            } else {
                this.zzblB.setOnCameraMoveListener(new zzu(this, onCameraMoveListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraMoveStartedListener(OnCameraMoveStartedListener onCameraMoveStartedListener) {
        try {
            if (onCameraMoveStartedListener == null) {
                this.zzblB.setOnCameraMoveStartedListener(null);
            } else {
                this.zzblB.setOnCameraMoveStartedListener(new zzt(this, onCameraMoveStartedListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCircleClickListener(OnCircleClickListener onCircleClickListener) {
        try {
            if (onCircleClickListener == null) {
                this.zzblB.setOnCircleClickListener(null);
            } else {
                this.zzblB.setOnCircleClickListener(new zzn(this, onCircleClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnGroundOverlayClickListener(OnGroundOverlayClickListener onGroundOverlayClickListener) {
        try {
            if (onGroundOverlayClickListener == null) {
                this.zzblB.setOnGroundOverlayClickListener(null);
            } else {
                this.zzblB.setOnGroundOverlayClickListener(new zzk(this, onGroundOverlayClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnIndoorStateChangeListener(OnIndoorStateChangeListener onIndoorStateChangeListener) {
        try {
            if (onIndoorStateChangeListener == null) {
                this.zzblB.setOnIndoorStateChangeListener(null);
            } else {
                this.zzblB.setOnIndoorStateChangeListener(new com.google.android.gms.maps.zza(this, onIndoorStateChangeListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
        try {
            if (onInfoWindowClickListener == null) {
                this.zzblB.setOnInfoWindowClickListener(null);
            } else {
                this.zzblB.setOnInfoWindowClickListener(new zzd(this, onInfoWindowClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnInfoWindowCloseListener(OnInfoWindowCloseListener onInfoWindowCloseListener) {
        try {
            if (onInfoWindowCloseListener == null) {
                this.zzblB.setOnInfoWindowCloseListener(null);
            } else {
                this.zzblB.setOnInfoWindowCloseListener(new zzf(this, onInfoWindowCloseListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnInfoWindowLongClickListener(OnInfoWindowLongClickListener onInfoWindowLongClickListener) {
        try {
            if (onInfoWindowLongClickListener == null) {
                this.zzblB.setOnInfoWindowLongClickListener(null);
            } else {
                this.zzblB.setOnInfoWindowLongClickListener(new zze(this, onInfoWindowLongClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        try {
            if (onMapClickListener == null) {
                this.zzblB.setOnMapClickListener(null);
            } else {
                this.zzblB.setOnMapClickListener(new zzx(this, onMapClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        try {
            if (onMapLoadedCallback == null) {
                this.zzblB.setOnMapLoadedCallback(null);
            } else {
                this.zzblB.setOnMapLoadedCallback(new zzj(this, onMapLoadedCallback));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        try {
            if (onMapLongClickListener == null) {
                this.zzblB.setOnMapLongClickListener(null);
            } else {
                this.zzblB.setOnMapLongClickListener(new zzy(this, onMapLongClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        try {
            if (onMarkerClickListener == null) {
                this.zzblB.setOnMarkerClickListener(null);
            } else {
                this.zzblB.setOnMarkerClickListener(new zzb(this, onMarkerClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        try {
            if (onMarkerDragListener == null) {
                this.zzblB.setOnMarkerDragListener(null);
            } else {
                this.zzblB.setOnMarkerDragListener(new zzc(this, onMarkerDragListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener onMyLocationButtonClickListener) {
        try {
            if (onMyLocationButtonClickListener == null) {
                this.zzblB.setOnMyLocationButtonClickListener(null);
            } else {
                this.zzblB.setOnMyLocationButtonClickListener(new zzi(this, onMyLocationButtonClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener) {
        try {
            if (onMyLocationChangeListener == null) {
                this.zzblB.setOnMyLocationChangeListener(null);
            } else {
                this.zzblB.setOnMyLocationChangeListener(new zzh(this, onMyLocationChangeListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnPoiClickListener(OnPoiClickListener onPoiClickListener) {
        try {
            if (onPoiClickListener == null) {
                this.zzblB.setOnPoiClickListener(null);
            } else {
                this.zzblB.setOnPoiClickListener(new zzr(this, onPoiClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnPolygonClickListener(OnPolygonClickListener onPolygonClickListener) {
        try {
            if (onPolygonClickListener == null) {
                this.zzblB.setOnPolygonClickListener(null);
            } else {
                this.zzblB.setOnPolygonClickListener(new zzo(this, onPolygonClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        try {
            if (onPolylineClickListener == null) {
                this.zzblB.setOnPolylineClickListener(null);
            } else {
                this.zzblB.setOnPolylineClickListener(new zzp(this, onPolylineClickListener));
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
        try {
            this.zzblB.setPadding(i, i2, i3, i4);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean z) {
        try {
            this.zzblB.setTrafficEnabled(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback) {
        snapshot(snapshotReadyCallback, null);
    }

    public final void snapshot(SnapshotReadyCallback snapshotReadyCallback, Bitmap bitmap) {
        try {
            this.zzblB.snapshot(new zzq(this, snapshotReadyCallback), (com.google.android.gms.dynamic.zzn) (bitmap != null ? com.google.android.gms.dynamic.zzn.zzw(bitmap) : null));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void stopAnimation() {
        try {
            this.zzblB.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
