package com.google.android.gms.maps.model;

import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbr;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public final class Polygon {
    private final com.google.android.gms.maps.model.internal.zzs zzbnQ;

    public Polygon(com.google.android.gms.maps.model.internal.zzs zzsVar) {
        this.zzbnQ = (com.google.android.gms.maps.model.internal.zzs) zzbr.zzu(zzsVar);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Polygon)) {
            return false;
        }
        try {
            return this.zzbnQ.zzb(((Polygon) obj).zzbnQ);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getFillColor() {
        try {
            return this.zzbnQ.getFillColor();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final List<List<LatLng>> getHoles() {
        try {
            return this.zzbnQ.getHoles();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final String getId() {
        try {
            return this.zzbnQ.getId();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final List<LatLng> getPoints() {
        try {
            return this.zzbnQ.getPoints();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getStrokeColor() {
        try {
            return this.zzbnQ.getStrokeColor();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getStrokeJointType() {
        try {
            return this.zzbnQ.getStrokeJointType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Nullable
    public final List<PatternItem> getStrokePattern() {
        try {
            return PatternItem.zzF(this.zzbnQ.getStrokePattern());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getStrokeWidth() {
        try {
            return this.zzbnQ.getStrokeWidth();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Nullable
    public final Object getTag() {
        try {
            return com.google.android.gms.dynamic.zzn.zzE(this.zzbnQ.getTag());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getZIndex() {
        try {
            return this.zzbnQ.getZIndex();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int hashCode() {
        try {
            return this.zzbnQ.hashCodeRemote();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isClickable() {
        try {
            return this.zzbnQ.isClickable();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isGeodesic() {
        try {
            return this.zzbnQ.isGeodesic();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isVisible() {
        try {
            return this.zzbnQ.isVisible();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void remove() {
        try {
            this.zzbnQ.remove();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setClickable(boolean z) {
        try {
            this.zzbnQ.setClickable(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setFillColor(int i) {
        try {
            this.zzbnQ.setFillColor(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setGeodesic(boolean z) {
        try {
            this.zzbnQ.setGeodesic(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setHoles(List<? extends List<LatLng>> list) {
        try {
            this.zzbnQ.setHoles(list);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPoints(List<LatLng> list) {
        try {
            this.zzbnQ.setPoints(list);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokeColor(int i) {
        try {
            this.zzbnQ.setStrokeColor(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokeJointType(int i) {
        try {
            this.zzbnQ.setStrokeJointType(i);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokePattern(@Nullable List<PatternItem> list) {
        try {
            this.zzbnQ.setStrokePattern(list);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setStrokeWidth(float f) {
        try {
            this.zzbnQ.setStrokeWidth(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTag(@Nullable Object obj) {
        try {
            this.zzbnQ.setTag(com.google.android.gms.dynamic.zzn.zzw(obj));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setVisible(boolean z) {
        try {
            this.zzbnQ.setVisible(z);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setZIndex(float f) {
        try {
            this.zzbnQ.setZIndex(f);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
