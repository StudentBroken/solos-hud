package com.google.android.gms.maps.internal;

import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.facebook.internal.FacebookRequestErrorClassification;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzed;
import com.google.android.gms.internal.zzef;
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
public final class zzg extends zzed implements IGoogleMapDelegate {
    zzg(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.maps.internal.IGoogleMapDelegate");
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzd addCircle(CircleOptions circleOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, circleOptions);
        Parcel parcelZza = zza(35, parcelZzY);
        com.google.android.gms.maps.model.internal.zzd zzdVarZzab = com.google.android.gms.maps.model.internal.zze.zzab(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzdVarZzab;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzg addGroundOverlay(GroundOverlayOptions groundOverlayOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, groundOverlayOptions);
        Parcel parcelZza = zza(12, parcelZzY);
        com.google.android.gms.maps.model.internal.zzg zzgVarZzac = com.google.android.gms.maps.model.internal.zzh.zzac(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzgVarZzac;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzp addMarker(MarkerOptions markerOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, markerOptions);
        Parcel parcelZza = zza(11, parcelZzY);
        com.google.android.gms.maps.model.internal.zzp zzpVarZzaf = com.google.android.gms.maps.model.internal.zzq.zzaf(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzpVarZzaf;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzs addPolygon(PolygonOptions polygonOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, polygonOptions);
        Parcel parcelZza = zza(10, parcelZzY);
        com.google.android.gms.maps.model.internal.zzs zzsVarZzag = com.google.android.gms.maps.model.internal.zzt.zzag(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzsVarZzag;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final IPolylineDelegate addPolyline(PolylineOptions polylineOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, polylineOptions);
        Parcel parcelZza = zza(9, parcelZzY);
        IPolylineDelegate iPolylineDelegateZzah = IPolylineDelegate.zza.zzah(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return iPolylineDelegateZzah;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzw addTileOverlay(TileOverlayOptions tileOverlayOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, tileOverlayOptions);
        Parcel parcelZza = zza(13, parcelZzY);
        com.google.android.gms.maps.model.internal.zzw zzwVarZzai = com.google.android.gms.maps.model.internal.zzx.zzai(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzwVarZzai;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCamera(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(5, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCameraWithCallback(IObjectWrapper iObjectWrapper, zzc zzcVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzef.zza(parcelZzY, zzcVar);
        zzb(6, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void animateCameraWithDurationAndCallback(IObjectWrapper iObjectWrapper, int i, zzc zzcVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        parcelZzY.writeInt(i);
        zzef.zza(parcelZzY, zzcVar);
        zzb(7, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void clear() throws RemoteException {
        zzb(14, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final CameraPosition getCameraPosition() throws RemoteException {
        Parcel parcelZza = zza(1, zzY());
        CameraPosition cameraPosition = (CameraPosition) zzef.zza(parcelZza, CameraPosition.CREATOR);
        parcelZza.recycle();
        return cameraPosition;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final com.google.android.gms.maps.model.internal.zzj getFocusedBuilding() throws RemoteException {
        Parcel parcelZza = zza(44, zzY());
        com.google.android.gms.maps.model.internal.zzj zzjVarZzad = com.google.android.gms.maps.model.internal.zzk.zzad(parcelZza.readStrongBinder());
        parcelZza.recycle();
        return zzjVarZzad;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void getMapAsync(zzap zzapVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzapVar);
        zzb(53, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final int getMapType() throws RemoteException {
        Parcel parcelZza = zza(15, zzY());
        int i = parcelZza.readInt();
        parcelZza.recycle();
        return i;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final float getMaxZoomLevel() throws RemoteException {
        Parcel parcelZza = zza(2, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final float getMinZoomLevel() throws RemoteException {
        Parcel parcelZza = zza(3, zzY());
        float f = parcelZza.readFloat();
        parcelZza.recycle();
        return f;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final Location getMyLocation() throws RemoteException {
        Parcel parcelZza = zza(23, zzY());
        Location location = (Location) zzef.zza(parcelZza, Location.CREATOR);
        parcelZza.recycle();
        return location;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final IProjectionDelegate getProjection() throws RemoteException {
        IProjectionDelegate zzbpVar;
        Parcel parcelZza = zza(26, zzY());
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbpVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IProjectionDelegate");
            zzbpVar = iInterfaceQueryLocalInterface instanceof IProjectionDelegate ? (IProjectionDelegate) iInterfaceQueryLocalInterface : new zzbp(strongBinder);
        }
        parcelZza.recycle();
        return zzbpVar;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final IUiSettingsDelegate getUiSettings() throws RemoteException {
        IUiSettingsDelegate zzbvVar;
        Parcel parcelZza = zza(25, zzY());
        IBinder strongBinder = parcelZza.readStrongBinder();
        if (strongBinder == null) {
            zzbvVar = null;
        } else {
            IInterface iInterfaceQueryLocalInterface = strongBinder.queryLocalInterface("com.google.android.gms.maps.internal.IUiSettingsDelegate");
            zzbvVar = iInterfaceQueryLocalInterface instanceof IUiSettingsDelegate ? (IUiSettingsDelegate) iInterfaceQueryLocalInterface : new zzbv(strongBinder);
        }
        parcelZza.recycle();
        return zzbvVar;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean isBuildingsEnabled() throws RemoteException {
        Parcel parcelZza = zza(40, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean isIndoorEnabled() throws RemoteException {
        Parcel parcelZza = zza(19, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean isMyLocationEnabled() throws RemoteException {
        Parcel parcelZza = zza(21, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean isTrafficEnabled() throws RemoteException {
        Parcel parcelZza = zza(17, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void moveCamera(IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(4, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onCreate(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(54, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onDestroy() throws RemoteException {
        zzb(57, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onEnterAmbient(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        zzb(81, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onExitAmbient() throws RemoteException {
        zzb(82, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onLowMemory() throws RemoteException {
        zzb(58, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onPause() throws RemoteException {
        zzb(56, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onResume() throws RemoteException {
        zzb(55, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onSaveInstanceState(Bundle bundle) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, bundle);
        Parcel parcelZza = zza(60, parcelZzY);
        if (parcelZza.readInt() != 0) {
            bundle.readFromParcel(parcelZza);
        }
        parcelZza.recycle();
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onStart() throws RemoteException {
        zzb(101, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void onStop() throws RemoteException {
        zzb(FacebookRequestErrorClassification.EC_INVALID_SESSION, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void resetMinMaxZoomPreference() throws RemoteException {
        zzb(94, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setBuildingsEnabled(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(41, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setContentDescription(String str) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeString(str);
        zzb(61, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean setIndoorEnabled(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        Parcel parcelZza = zza(20, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setInfoWindowAdapter(zzh zzhVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzhVar);
        zzb(33, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, latLngBounds);
        zzb(95, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setLocationSource(ILocationSourceDelegate iLocationSourceDelegate) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, iLocationSourceDelegate);
        zzb(24, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean setMapStyle(MapStyleOptions mapStyleOptions) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, mapStyleOptions);
        Parcel parcelZza = zza(91, parcelZzY);
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMapType(int i) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        zzb(16, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMaxZoomPreference(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(93, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMinZoomPreference(float f) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeFloat(f);
        zzb(92, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setMyLocationEnabled(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(22, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraChangeListener(zzl zzlVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzlVar);
        zzb(27, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraIdleListener(zzn zznVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zznVar);
        zzb(99, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraMoveCanceledListener(zzp zzpVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzpVar);
        zzb(98, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraMoveListener(zzr zzrVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzrVar);
        zzb(97, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCameraMoveStartedListener(zzt zztVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zztVar);
        zzb(96, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnCircleClickListener(zzv zzvVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzvVar);
        zzb(89, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnGroundOverlayClickListener(zzx zzxVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzxVar);
        zzb(83, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnIndoorStateChangeListener(zzz zzzVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzzVar);
        zzb(45, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnInfoWindowClickListener(zzab zzabVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzabVar);
        zzb(32, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnInfoWindowCloseListener(zzad zzadVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzadVar);
        zzb(86, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnInfoWindowLongClickListener(zzaf zzafVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzafVar);
        zzb(84, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMapClickListener(zzaj zzajVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzajVar);
        zzb(28, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMapLoadedCallback(zzal zzalVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzalVar);
        zzb(42, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMapLongClickListener(zzan zzanVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzanVar);
        zzb(29, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMarkerClickListener(zzar zzarVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzarVar);
        zzb(30, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMarkerDragListener(zzat zzatVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzatVar);
        zzb(31, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMyLocationButtonClickListener(zzav zzavVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzavVar);
        zzb(37, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnMyLocationChangeListener(zzax zzaxVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzaxVar);
        zzb(36, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnPoiClickListener(zzaz zzazVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzazVar);
        zzb(80, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnPolygonClickListener(zzbb zzbbVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbbVar);
        zzb(85, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setOnPolylineClickListener(zzbd zzbdVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbdVar);
        zzb(87, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setPadding(int i, int i2, int i3, int i4) throws RemoteException {
        Parcel parcelZzY = zzY();
        parcelZzY.writeInt(i);
        parcelZzY.writeInt(i2);
        parcelZzY.writeInt(i3);
        parcelZzY.writeInt(i4);
        zzb(39, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setTrafficEnabled(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(18, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void setWatermarkEnabled(boolean z) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, z);
        zzb(51, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void snapshot(zzbq zzbqVar, IObjectWrapper iObjectWrapper) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbqVar);
        zzef.zza(parcelZzY, iObjectWrapper);
        zzb(38, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void snapshotForTest(zzbq zzbqVar) throws RemoteException {
        Parcel parcelZzY = zzY();
        zzef.zza(parcelZzY, zzbqVar);
        zzb(71, parcelZzY);
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final void stopAnimation() throws RemoteException {
        zzb(8, zzY());
    }

    @Override // com.google.android.gms.maps.internal.IGoogleMapDelegate
    public final boolean useViewLifecycleWhenInFragment() throws RemoteException {
        Parcel parcelZza = zza(59, zzY());
        boolean zZza = zzef.zza(parcelZza);
        parcelZza.recycle();
        return zZza;
    }
}
