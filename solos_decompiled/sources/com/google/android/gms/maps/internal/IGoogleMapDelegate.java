package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;

/* JADX INFO: loaded from: classes10.dex */
public interface IGoogleMapDelegate extends IInterface {
    com.google.android.gms.maps.model.internal.zzd addCircle(CircleOptions circleOptions) throws RemoteException;

    com.google.android.gms.maps.model.internal.zzg addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException;

    com.google.android.gms.maps.model.internal.zzp addMarker(MarkerOptions markerOptions) throws RemoteException;

    com.google.android.gms.maps.model.internal.zzs addPolygon(PolygonOptions polygonOptions) throws RemoteException;

    IPolylineDelegate addPolyline(PolylineOptions polylineOptions) throws RemoteException;

    com.google.android.gms.maps.model.internal.zzw addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException;

    void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzc zzcVar) throws RemoteException;

    void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzc zzcVar) throws RemoteException;

    void clear() throws RemoteException;

    CameraPosition getCameraPosition() throws RemoteException;

    com.google.android.gms.maps.model.internal.zzj getFocusedBuilding() throws RemoteException;

    void getMapAsync(zzap zzapVar) throws RemoteException;

    int getMapType() throws RemoteException;

    float getMaxZoomLevel() throws RemoteException;

    float getMinZoomLevel() throws RemoteException;

    Location getMyLocation() throws RemoteException;

    IProjectionDelegate getProjection() throws RemoteException;

    IUiSettingsDelegate getUiSettings() throws RemoteException;

    boolean isBuildingsEnabled() throws RemoteException;

    boolean isIndoorEnabled() throws RemoteException;

    boolean isMyLocationEnabled() throws RemoteException;

    boolean isTrafficEnabled() throws RemoteException;

    void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException;

    void onCreate(Bundle bundle) throws RemoteException;

    void onDestroy() throws RemoteException;

    void onEnterAmbient(Bundle bundle) throws RemoteException;

    void onExitAmbient() throws RemoteException;

    void onLowMemory() throws RemoteException;

    void onPause() throws RemoteException;

    void onResume() throws RemoteException;

    void onSaveInstanceState(Bundle bundle) throws RemoteException;

    void onStart() throws RemoteException;

    void onStop() throws RemoteException;

    void resetMinMaxZoomPreference() throws RemoteException;

    void setBuildingsEnabled(boolean z) throws RemoteException;

    void setContentDescription(String str) throws RemoteException;

    boolean setIndoorEnabled(boolean z) throws RemoteException;

    void setInfoWindowAdapter(zzh zzhVar) throws RemoteException;

    void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) throws RemoteException;

    void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException;

    boolean setMapStyle(MapStyleOptions mapStyleOptions) throws RemoteException;

    void setMapType(int i) throws RemoteException;

    void setMaxZoomPreference(float f) throws RemoteException;

    void setMinZoomPreference(float f) throws RemoteException;

    void setMyLocationEnabled(boolean z) throws RemoteException;

    void setOnCameraChangeListener(zzl zzlVar) throws RemoteException;

    void setOnCameraIdleListener(zzn zznVar) throws RemoteException;

    void setOnCameraMoveCanceledListener(zzp zzpVar) throws RemoteException;

    void setOnCameraMoveListener(zzr zzrVar) throws RemoteException;

    void setOnCameraMoveStartedListener(zzt zztVar) throws RemoteException;

    void setOnCircleClickListener(zzv zzvVar) throws RemoteException;

    void setOnGroundOverlayClickListener(zzx zzxVar) throws RemoteException;

    void setOnIndoorStateChangeListener(zzz zzzVar) throws RemoteException;

    void setOnInfoWindowClickListener(zzab zzabVar) throws RemoteException;

    void setOnInfoWindowCloseListener(zzad zzadVar) throws RemoteException;

    void setOnInfoWindowLongClickListener(zzaf zzafVar) throws RemoteException;

    void setOnMapClickListener(zzaj zzajVar) throws RemoteException;

    void setOnMapLoadedCallback(zzal zzalVar) throws RemoteException;

    void setOnMapLongClickListener(zzan zzanVar) throws RemoteException;

    void setOnMarkerClickListener(zzar zzarVar) throws RemoteException;

    void setOnMarkerDragListener(zzat zzatVar) throws RemoteException;

    void setOnMyLocationButtonClickListener(zzav zzavVar) throws RemoteException;

    void setOnMyLocationChangeListener(zzax zzaxVar) throws RemoteException;

    void setOnPoiClickListener(zzaz zzazVar) throws RemoteException;

    void setOnPolygonClickListener(zzbb zzbbVar) throws RemoteException;

    void setOnPolylineClickListener(zzbd zzbdVar) throws RemoteException;

    void setPadding(int i, int i2, int i3, int i4) throws RemoteException;

    void setTrafficEnabled(boolean z) throws RemoteException;

    void setWatermarkEnabled(boolean z) throws RemoteException;

    void snapshot(zzbq zzbqVar, IObjectWrapper iObjectWrapper) throws RemoteException;

    void snapshotForTest(zzbq zzbqVar) throws RemoteException;

    void stopAnimation() throws RemoteException;

    boolean useViewLifecycleWhenInFragment() throws RemoteException;
}
