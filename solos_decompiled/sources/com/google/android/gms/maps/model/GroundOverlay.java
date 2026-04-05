package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
public final class GroundOverlay {
    private final com.google.android.gms.maps.model.internal.zzg zzbns;

    public GroundOverlay(com.google.android.gms.maps.model.internal.zzg zzgVar) {
        this.zzbns = (com.google.android.gms.maps.model.internal.zzg) zzbr.zzu(zzgVar);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof GroundOverlay)) {
            return false;
        }
        try {
            return this.zzbns.zzb(((GroundOverlay) obj).zzbns);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getBearing() {
        try {
            return this.zzbns.getBearing();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final LatLngBounds getBounds() {
        try {
            return this.zzbns.getBounds();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getHeight() {
        try {
            return this.zzbns.getHeight();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzbns.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final LatLng getPosition() {
        try {
            return this.zzbns.getPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Object getTag() {
        try {
            return com.google.android.gms.dynamic.zzn.zzE(this.zzbns.getTag());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getTransparency() {
        try {
            return this.zzbns.getTransparency();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getWidth() {
        try {
            return this.zzbns.getWidth();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzbns.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbns.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isClickable() {
        try {
            return this.zzbns.isClickable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzbns.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void remove() {
        try {
            this.zzbns.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBearing(float f) {
        try {
            this.zzbns.setBearing(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setClickable(boolean z) {
        try {
            this.zzbns.setClickable(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setDimensions(float f) {
        try {
            this.zzbns.setDimensions(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setDimensions(float f, float f2) {
        try {
            this.zzbns.zzf(f, f2);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setImage(@NonNull BitmapDescriptor bitmapDescriptor) {
        zzbr.zzb(bitmapDescriptor, "imageDescriptor must not be null");
        try {
            this.zzbns.zzJ(bitmapDescriptor.zzwd());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPosition(LatLng latLng) {
        try {
            this.zzbns.setPosition(latLng);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPositionFromBounds(LatLngBounds latLngBounds) {
        try {
            this.zzbns.setPositionFromBounds(latLngBounds);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(Object obj) {
        try {
            this.zzbns.setTag(com.google.android.gms.dynamic.zzn.zzw(obj));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTransparency(float f) {
        try {
            this.zzbns.setTransparency(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzbns.setVisible(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzbns.setZIndex(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
