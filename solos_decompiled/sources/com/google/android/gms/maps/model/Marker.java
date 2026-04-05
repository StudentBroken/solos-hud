package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbr;

/* JADX INFO: loaded from: classes10.dex */
public final class Marker {
    private final com.google.android.gms.maps.model.internal.zzp zzbnH;

    public Marker(com.google.android.gms.maps.model.internal.zzp zzpVar) {
        this.zzbnH = (com.google.android.gms.maps.model.internal.zzp) zzbr.zzu(zzpVar);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Marker)) {
            return false;
        }
        try {
            return this.zzbnH.zzj(((Marker) obj).zzbnH);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getAlpha() {
        try {
            return this.zzbnH.getAlpha();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzbnH.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final LatLng getPosition() {
        try {
            return this.zzbnH.getPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getRotation() {
        try {
            return this.zzbnH.getRotation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getSnippet() {
        try {
            return this.zzbnH.getSnippet();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return com.google.android.gms.dynamic.zzn.zzE(this.zzbnH.getTag());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getTitle() {
        try {
            return this.zzbnH.getTitle();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzbnH.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbnH.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void hideInfoWindow() {
        try {
            this.zzbnH.hideInfoWindow();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isDraggable() {
        try {
            return this.zzbnH.isDraggable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isFlat() {
        try {
            return this.zzbnH.isFlat();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isInfoWindowShown() {
        try {
            return this.zzbnH.isInfoWindowShown();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzbnH.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void remove() {
        try {
            this.zzbnH.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setAlpha(float f) {
        try {
            this.zzbnH.setAlpha(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setAnchor(float f, float f2) {
        try {
            this.zzbnH.setAnchor(f, f2);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setDraggable(boolean z) {
        try {
            this.zzbnH.setDraggable(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setFlat(boolean z) {
        try {
            this.zzbnH.setFlat(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setIcon(@Nullable BitmapDescriptor bitmapDescriptor) {
        try {
            if (bitmapDescriptor == null) {
                this.zzbnH.zzK(null);
            } else {
                this.zzbnH.zzK(bitmapDescriptor.zzwd());
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setInfoWindowAnchor(float f, float f2) {
        try {
            this.zzbnH.setInfoWindowAnchor(f, f2);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPosition(@NonNull LatLng latLng) {
        if (latLng == null) {
            throw new IllegalArgumentException("latlng cannot be null - a position is required.");
        }
        try {
            this.zzbnH.setPosition(latLng);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setRotation(float f) {
        try {
            this.zzbnH.setRotation(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setSnippet(@Nullable String str) {
        try {
            this.zzbnH.setSnippet(str);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzbnH.setTag(com.google.android.gms.dynamic.zzn.zzw(obj));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTitle(@Nullable String str) {
        try {
            this.zzbnH.setTitle(str);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzbnH.setVisible(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzbnH.setZIndex(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void showInfoWindow() {
        try {
            this.zzbnH.showInfoWindow();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
