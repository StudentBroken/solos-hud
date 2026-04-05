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
import com.google.android.gms.maps.internal.IStreetViewPanoramaViewDelegate;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.maps.internal.zzbw;
import com.google.android.gms.maps.internal.zzbx;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes10.dex */
public class StreetViewPanoramaView extends FrameLayout {
    private final zzb zzbmW;

    static class zza implements StreetViewLifecycleDelegate {
        private final IStreetViewPanoramaViewDelegate zzbmX;
        private View zzbmY;
        private final ViewGroup zzbmz;

        public zza(ViewGroup viewGroup, IStreetViewPanoramaViewDelegate iStreetViewPanoramaViewDelegate) {
            this.zzbmX = (IStreetViewPanoramaViewDelegate) zzbr.zzu(iStreetViewPanoramaViewDelegate);
            this.zzbmz = (ViewGroup) zzbr.zzu(viewGroup);
        }

        @Override // com.google.android.gms.maps.internal.StreetViewLifecycleDelegate
        public final void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            try {
                this.zzbmX.getStreetViewPanoramaAsync(new zzai(this, onStreetViewPanoramaReadyCallback));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onCreate(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmX.onCreate(bundle2);
                zzbw.zzd(bundle2, bundle);
                this.zzbmY = (View) com.google.android.gms.dynamic.zzn.zzE(this.zzbmX.getView());
                this.zzbmz.removeAllViews();
                this.zzbmz.addView(this.zzbmY);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            throw new UnsupportedOperationException("onCreateView not allowed on StreetViewPanoramaViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroy() {
            try {
                this.zzbmX.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on StreetViewPanoramaViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            throw new UnsupportedOperationException("onInflate not allowed on StreetViewPanoramaViewDelegate");
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onLowMemory() {
            try {
                this.zzbmX.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onPause() {
            try {
                this.zzbmX.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onResume() {
            try {
                this.zzbmX.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onSaveInstanceState(Bundle bundle) {
            try {
                Bundle bundle2 = new Bundle();
                zzbw.zzd(bundle, bundle2);
                this.zzbmX.onSaveInstanceState(bundle2);
                zzbw.zzd(bundle2, bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStart() {
        }

        @Override // com.google.android.gms.dynamic.LifecycleDelegate
        public final void onStop() {
        }
    }

    static class zzb extends com.google.android.gms.dynamic.zza<zza> {
        private final ViewGroup zzbmC;
        private final Context zzbmD;
        private final List<OnStreetViewPanoramaReadyCallback> zzbmO = new ArrayList();
        private final StreetViewPanoramaOptions zzbmZ;
        private com.google.android.gms.dynamic.zzo<zza> zzbmw;

        zzb(ViewGroup viewGroup, Context context, StreetViewPanoramaOptions streetViewPanoramaOptions) {
            this.zzbmC = viewGroup;
            this.zzbmD = context;
            this.zzbmZ = streetViewPanoramaOptions;
        }

        public final void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
            if (zztw() != null) {
                zztw().getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
            } else {
                this.zzbmO.add(onStreetViewPanoramaReadyCallback);
            }
        }

        @Override // com.google.android.gms.dynamic.zza
        protected final void zza(com.google.android.gms.dynamic.zzo<zza> zzoVar) {
            this.zzbmw = zzoVar;
            if (this.zzbmw == null || zztw() != null) {
                return;
            }
            try {
                this.zzbmw.zza(new zza(this.zzbmC, zzbx.zzbh(this.zzbmD).zza(com.google.android.gms.dynamic.zzn.zzw(this.zzbmD), this.zzbmZ)));
                Iterator<OnStreetViewPanoramaReadyCallback> it = this.zzbmO.iterator();
                while (it.hasNext()) {
                    zztw().getStreetViewPanoramaAsync(it.next());
                }
                this.zzbmO.clear();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            } catch (GooglePlayServicesNotAvailableException e2) {
            }
        }
    }

    public StreetViewPanoramaView(Context context) {
        super(context);
        this.zzbmW = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.zzbmW = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.zzbmW = new zzb(this, context, null);
    }

    public StreetViewPanoramaView(Context context, StreetViewPanoramaOptions streetViewPanoramaOptions) {
        super(context);
        this.zzbmW = new zzb(this, context, streetViewPanoramaOptions);
    }

    public void getStreetViewPanoramaAsync(OnStreetViewPanoramaReadyCallback onStreetViewPanoramaReadyCallback) {
        zzbr.zzcz("getStreetViewPanoramaAsync() must be called on the main thread");
        this.zzbmW.getStreetViewPanoramaAsync(onStreetViewPanoramaReadyCallback);
    }

    public final void onCreate(Bundle bundle) {
        this.zzbmW.onCreate(bundle);
        if (this.zzbmW.zztw() == null) {
            com.google.android.gms.dynamic.zza.zzb(this);
        }
    }

    public final void onDestroy() {
        this.zzbmW.onDestroy();
    }

    public final void onLowMemory() {
        this.zzbmW.onLowMemory();
    }

    public final void onPause() {
        this.zzbmW.onPause();
    }

    public final void onResume() {
        this.zzbmW.onResume();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        this.zzbmW.onSaveInstanceState(bundle);
    }
}
