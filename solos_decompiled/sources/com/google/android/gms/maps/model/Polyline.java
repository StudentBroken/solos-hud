package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class Polyline {
    private final IPolylineDelegate zzbnV;

    public Polyline(IPolylineDelegate iPolylineDelegate) {
        this.zzbnV = (IPolylineDelegate) zzbr.zzu(iPolylineDelegate);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Polyline)) {
            return false;
        }
        try {
            return this.zzbnV.equalsRemote(((Polyline) obj).zzbnV);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getColor() {
        try {
            return this.zzbnV.getColor();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @NonNull
    public final Cap getEndCap() {
        try {
            return this.zzbnV.getEndCap().zzwj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzbnV.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getJointType() {
        try {
            return this.zzbnV.getJointType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Nullable
    public final List<PatternItem> getPattern() {
        try {
            return PatternItem.zzF(this.zzbnV.getPattern());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final List<LatLng> getPoints() {
        try {
            return this.zzbnV.getPoints();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @NonNull
    public final Cap getStartCap() {
        try {
            return this.zzbnV.getStartCap().zzwj();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return com.google.android.gms.dynamic.zzn.zzE(this.zzbnV.getTag());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getWidth() {
        try {
            return this.zzbnV.getWidth();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzbnV.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbnV.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isClickable() {
        try {
            return this.zzbnV.isClickable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isGeodesic() {
        try {
            return this.zzbnV.isGeodesic();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzbnV.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void remove() {
        try {
            this.zzbnV.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setClickable(boolean z) {
        try {
            this.zzbnV.setClickable(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setColor(int i) {
        try {
            this.zzbnV.setColor(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setEndCap(@NonNull Cap cap) {
        zzbr.zzb(cap, "endCap must not be null");
        try {
            this.zzbnV.setEndCap(cap);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setGeodesic(boolean z) {
        try {
            this.zzbnV.setGeodesic(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setJointType(int i) {
        try {
            this.zzbnV.setJointType(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPattern(@Nullable List<PatternItem> list) {
        try {
            this.zzbnV.setPattern(list);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPoints(List<LatLng> list) {
        try {
            this.zzbnV.setPoints(list);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStartCap(@NonNull Cap cap) {
        zzbr.zzb(cap, "startCap must not be null");
        try {
            this.zzbnV.setStartCap(cap);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzbnV.setTag(com.google.android.gms.dynamic.zzn.zzw(obj));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzbnV.setVisible(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setWidth(float f) {
        try {
            this.zzbnV.setWidth(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzbnV.setZIndex(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
