package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.internal.MapLifecycleDelegate;
import com.google.android.gms.maps.internal.zzbw;
import com.google.android.gms.maps.internal.zzbx;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public class MapView extends FrameLayout {
    private final zzb zzbmy;

    static class zza implements MapLifecycleDelegate {
        private final IMapViewDelegate zzbmA;
        private View zzbmB;
        private final ViewGroup zzbmz;

        public zza(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.zzbmA = (IMapViewDelegate) zzbr.zzu(iMapViewDelegate);
            this.zzbmz = (ViewGroup) zzbr.zzu(viewGroup);
        }

        @Override // com.google.android.gms.maps.internal.MapLifecycleDelegate
        public final void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            try {
                this.zzbmA.getMapAsync(new zzab(this, onMapReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onCreate(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmA.onCreate(bundle2);
                zzbw.zzd(bundle2, bundle);
                this.zzbmB = (View) com.google.android.gms.dynamic.zzn.zzE(this.zzbmA.getView());
                this.zzbmz.removeAllViews();
                this.zzbmz.addView(this.zzbmB);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroy() {
            try {
                this.zzbmA.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        public final void onEnterAmbient(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmA.onEnterAmbient(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public final void onExitAmbient() {
            try {
                this.zzbmA.onExitAmbient();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onLowMemory() {
            try {
                this.zzbmA.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onPause() {
            try {
                this.zzbmA.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onResume() {
            try {
                this.zzbmA.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onSaveInstanceState(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmA.onSaveInstanceState(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStart() {
            try {
                this.zzbmA.onStart();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStop() {
            try {
                this.zzbmA.onStop();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final ViewGroup zzbmC;
        private final Context zzbmD;
        private final GoogleMapOptions zzbmE;
        private com.google.android.gms.dynamic.zzo<zza> zzbmw;
        private final List<OnMapReadyCallback> zzbmx = new ArrayList();

        zzb(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.zzbmC = viewGroup;
            this.zzbmD = context;
            this.zzbmE = googleMapOptions;
        }

        public final void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
            if (zztw() != null) {
                zztw().getMapAsync(onMapReadyCallback);
            } else {
                this.zzbmx.add(onMapReadyCallback);
            }
        }

        @Override // com.google.android.gms.dynamic.zza
        protected final void zza(com.google.android.gms.dynamic.zzo<zza> zzoVar) {
            this.zzbmw = zzoVar;
            if (this.zzbmw == null || zztw() != null) {
                return;
            }
            try {
                MapsInitializer.initialize(this.zzbmD);
                IMapViewDelegate iMapViewDelegateZza = zzbx.zzbh(this.zzbmD).zza(com.google.android.gms.dynamic.zzn.zzw(this.zzbmD), this.zzbmE);
                if (iMapViewDelegateZza == null) {
                    return;
                }
                this.zzbmw.zza(new zza(this.zzbmC, iMapViewDelegateZza));
                Iterator<OnMapReadyCallback> it = this.zzbmx.iterator();
                while (it.hasNext()) {
                    zztw().getMapAsync(it.next());
                }
                this.zzbmx.clear();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }
    }

    public MapView(Context context) {
        super(context);
        this.zzbmy = new zzb(this, context, null);
        setClickable(true);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzbmy = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
        setClickable(true);
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzbmy = new zzb(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
        setClickable(true);
    }

    public MapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context);
        this.zzbmy = new zzb(this, context, googleMapOptions);
        setClickable(true);
    }

    public void getMapAsync(OnMapReadyCallback onMapReadyCallback) {
        zzbr.zzcz("getMapAsync() must be called on the main thread");
        this.zzbmy.getMapAsync(onMapReadyCallback);
    }

    public final void onCreate(Bundle bundle) {
        this.zzbmy.onCreate(bundle);
        if (this.zzbmy.zztw() == null) {
            com.google.android.gms.dynamic.zza.zzb(this);
        }
    }

    public final void onDestroy() {
        this.zzbmy.onDestroy();
    }

    public final void onEnterAmbient(Bundle bundle) {
        zzbr.zzcz("onEnterAmbient() must be called on the main thread");
        zzb zzbVar = this.zzbmy;
        if (zzbVar.zztw() != null) {
            zzbVar.zztw().onEnterAmbient(bundle);
        }
    }

    public final void onExitAmbient() {
        zzbr.zzcz("onExitAmbient() must be called on the main thread");
        zzb zzbVar = this.zzbmy;
        if (zzbVar.zztw() != null) {
            zzbVar.zztw().onExitAmbient();
        }
    }

    public final void onLowMemory() {
        this.zzbmy.onLowMemory();
    }

    public final void onPause() {
        this.zzbmy.onPause();
    }

    public final void onResume() {
        this.zzbmy.onResume();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        this.zzbmy.onSaveInstanceState(bundle);
    }

    public final void onStart() {
        this.zzbmy.onStart();
    }

    public final void onStop() {
        this.zzbmy.onStop();
    }
}
